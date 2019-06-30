package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import organica.datos.Consulta;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class MaestroOrganicaCalidad extends MyHttpServlet {

	private static final long	serialVersionUID	= 1L;

	public MaestroOrganicaCalidad() {}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		doGet(req,resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		DespResultado(req,resp);
	}

	private void DespResultado(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		StringBuffer strSQL = new StringBuffer();
		
		SimpleHash modelRoot = new SimpleHash();
		String excel = (req.getParameter("excel") == null || req.getParameter("excel").equals("0")) ? "0" : req.getParameter("excel");
		if(excel.equals("1")) {
			super.setExcel(resp,"MaestroPersonalCalidad.xls");
			modelRoot.put("excel", "1");
        }
		else {
			modelRoot.put("excel", "0");
		}
		
		java.sql.Connection Conexion =  getConnMgr().getConnection("portal");//connMgr.getConnection("portal");


		try {

			strSQL.append("select distinct t.unidad \n")
			.append("from eje_ges_trabajador t \n")
			.append("inner join eje_ges_unidades u on u.unid_id=t.unidad \n")
			.append("inner join eje_ges_cargos c on c.empresa=t.wp_cod_empresa and c.cargo=t.cargo \n");

			ConsultaData unidades = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			
			strSQL.setLength(0);
			strSQL.append("declare @jefatura table (id_unidad varchar(max), id_jefatura int, nif varchar(max), nombre varchar(max), cargo varchar(max), id_jefatura_padre int, nif_padre varchar(max), nombre_padre varchar(max), cargo_padre varchar(max)) \n")
			.append("declare @gerencia table (id_unidad varchar(max), id_gerencia int, nif varchar(max), nombre varchar(max), cargo varchar(max)) \n\n");
			
			while (unidades!=null && unidades.next()){
				String strUnidad = unidades.getString("unidad");
				
				ConsultaData jefatura = OrganicaLocator.getInstance().getJefeResponsableDeLaUnidad(strUnidad);
				//ConsultaData jefatura = OrganicaLocator.getInstance().getJefeUnidad(strUnidad);
				
				while (jefatura!=null && jefatura.next()){
					strSQL.append("insert into @jefatura (id_unidad, id_jefatura) ")
					.append("values (")
					.append("'").append(strUnidad).append("', ")
					.append(jefatura.getForcedString("rut"))
					.append(") \n");
				}

				String strGerente = Gerente(strUnidad, Conexion);
				if (!strGerente.equals("")){
					strSQL.append("insert into @gerencia (id_unidad, nombre) ")
					.append("values (")
					.append("'").append(strUnidad).append("', ")
					.append("'").append(strGerente).append("') \n");
				}
	
			}
			
			strSQL.append("\n")
			
			.append("update j set nif=cast(t.rut as varchar)+'-'+t.digito_ver, nombre=rtrim(ltrim(t.nombre)), cargo=upper(rtrim(ltrim(c.descrip))) \n")
			.append("from @jefatura j \n")
			.append("inner join eje_ges_trabajador t on t.rut=j.id_jefatura \n")
			.append("inner join eje_ges_cargos c on c.empresa=t.wp_cod_empresa and c.cargo=t.cargo \n\n")

			
			.append("update j set id_jefatura_padre=t.rut, nif_padre=cast(t.rut as varchar)+'-'+t.digito_ver, nombre_padre=t.nombre, cargo=c.descrip \n")
			.append("from @jefatura j \n")
			.append("inner join eje_ges_jerarquia r on r.nodo_id=j.id_unidad \n")
			.append("inner join (select unid_id, periodo=max(periodo) from eje_ges_unidad_encargado group by unid_id) mp on mp.unid_id=r.nodo_padre \n")
			.append("inner join eje_ges_unidad_encargado ue on ue.unid_id=mp.unid_id and ue.periodo=mp.periodo \n")
			.append("inner join eje_ges_trabajador t on t.rut=ue.rut_encargado \n")
			.append("left join eje_ges_cargos c on c.empresa=t.empresa and c.cargo=t.cargo \n\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		strSQL.append("select u.unid_id, unid_desc=upper(rtrim(ltrim(u.unid_desc))), t.rut, \n")
		.append("t.digito_ver, nombre=rtrim(ltrim(t.nombre)), cargo=upper(rtrim(ltrim(c.descrip))), \n")
		.append("rutjefe=(case when j.id_jefatura=t.rut then j.nif_padre else j.nif end),  \n")
		.append("nombrejefe=(case when j.id_jefatura=t.rut then j.nombre_padre else j.nombre end),  \n")
		.append("cargojefe=(case when j.id_jefatura=t.rut then j.cargo_padre else j.cargo end), \n")
		.append("gerente=g.nombre \n")
		.append("from eje_ges_trabajador t \n") 
		.append("inner join eje_ges_unidades u on t.unidad=u.unid_id \n")
		.append("inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa \n") 
		.append("left join @jefatura j on j.id_unidad=u.unid_id \n")
		.append("left join @gerencia g on g.id_unidad=u.unid_id \n")
		.append("order by t.nombre \n");
		
		insTracking(req, "Maestro de Calidad".intern(),null );
		Consulta dotacion = new Consulta(Conexion);

		System.out.println(strSQL.toString());
		
		dotacion.exec(strSQL.toString());
		SimpleHash trabajador;
		SimpleList trabajadores = new SimpleList();
		for(;dotacion.next();) {
			trabajador = new SimpleHash();
			trabajador.put("unid_id", dotacion.getString("unid_id"));
			trabajador.put("unid_desc", dotacion.getString("unid_desc"));
			trabajador.put("rut", dotacion.getString("rut") + "-" + dotacion.getString("digito_ver"));
			trabajador.put("nombre", dotacion.getString("nombre"));
			trabajador.put("cargo", dotacion.getString("cargo"));
			trabajador.put("rutjefe", dotacion.getString("rutjefe"));
			trabajador.put("nombrejefe", dotacion.getString("nombrejefe"));
			trabajador.put("cargojefe", dotacion.getString("cargojefe"));
			trabajador.put("gerente", dotacion.getString("gerente"));
			trabajadores.add(trabajador);
		}
		
		modelRoot.put("trabajadores", trabajadores );
		dotacion.close();
		super.retTemplate(resp,"Gestion/MaestroCalidad.htm",modelRoot);
		connMgr.freeConnection("portal",Conexion);
	}
	
	
//MMA 20161229	
//	private void DespResultado(HttpServletRequest req, HttpServletResponse resp) 
//			throws ServletException, IOException {
//		SimpleHash modelRoot = new SimpleHash();
//		String excel = (req.getParameter("excel") == null || req.getParameter("excel").equals("0")) ? "0" : req.getParameter("excel");
//		if(excel.equals("1")) {
//			super.setExcel(resp,"MaestroPersonalCalidad.xls");
//			modelRoot.put("excel", "1");
//        }
//		else {
//			modelRoot.put("excel", "0");
//		}
//		java.sql.Connection Conexion = connMgr.getConnection("portal");
//		insTracking(req, "Maestro de Calidad".intern(),null );
//		Consulta dotacion = new Consulta(Conexion);
//		String query = "SELECT u.unid_id,upper(rtrim(ltrim(u.unid_desc))) unid_desc,t.rut,t.digito_ver, " +
//				"rtrim(ltrim(t.nombre)) nombre,upper(rtrim(ltrim(c.descrip))) cargo FROM (eje_ges_trabajador t inner join " +
//				"eje_ges_unidades u on t.unidad=u.unid_id) inner join eje_ges_cargos c on " +
//				"t.cargo=c.cargo and t.wp_cod_empresa=c.empresa ORDER BY t.nombre";
//		dotacion.exec(query);
//		SimpleHash trabajador;
//		SimpleList trabajadores = new SimpleList();
//		for(;dotacion.next();) {
//			trabajador = new SimpleHash();
//			trabajador.put("unid_id", dotacion.getString("unid_id"));
//			trabajador.put("unid_desc", dotacion.getString("unid_desc"));
//			trabajador.put("rut", dotacion.getString("rut") + "-" + dotacion.getString("digito_ver"));
//			trabajador.put("nombre", dotacion.getString("nombre"));
//			trabajador.put("cargo", dotacion.getString("cargo"));
//			//Jefe
//			Map<String, String> jefe = Jefe(dotacion.getString("unid_id"), dotacion.getString("rut"),Conexion);
//			Iterator it = jefe.keySet().iterator();
//			while(it.hasNext()){
//				Object key = it.next();
//				trabajador.put("rutjefe", (String)key);
//				String[] array = jefe.get(key).split("-");
//				trabajador.put("nombrejefe", array[0]);
//				trabajador.put("cargojefe", array[1]);
//				System.out.println(dotacion.getString("rut") + " @@ " + "Rut: " + key + " -> Nombre: " + array[0] + ", Cargo: " + array[1]);
//			}
//			//gerente area
//			String gerente = Gerente(dotacion.getString("unid_id"), Conexion);
//			trabajador.put("gerente", gerente);
//			trabajadores.add(trabajador);
//		}
//		modelRoot.put("trabajadores", trabajadores );
//		dotacion.close();
//		super.retTemplate(resp,"Gestion/MaestroCalidad.htm",modelRoot);
//		connMgr.freeConnection("portal",Conexion);
//	}
	
	private String unidadPadre(String unidad, Connection con) {
		Consulta padre = new Consulta(con);
		String query = "SELECT nodo_padre FROM eje_ges_jerarquia where nodo_id= '" + unidad + 
				"' and id_tipo is null";
		padre.exec(query);
		if( padre.next() ) {
			return padre.getString("nodo_padre");
		}
		else {
			return "-1";
		}
	}
	
	private Map<String, String> Jefe(String unidad, String rut, Connection con) {
		Consulta dotacion = new Consulta(con);
		String query = "SELECT t.rut,t.digito_ver,rtrim(ltrim(nombre)) nombre,upper(rtrim(ltrim(c.descrip))) cargo FROM eje_ges_trabajador t " +
				"inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa where rut in " +
				"(select rut_encargado from eje_ges_unidad_encargado where unid_id='" + unidad + 
				"' and estado=1)";
		dotacion.exec(query);
		Map<String, String> map = new HashMap<String, String>();
		if( dotacion.next() ) {
			if(!rut.equals(dotacion.getString("rut"))) {
				map.put(dotacion.getString("rut") + "-" + dotacion.getString("digito_ver"), dotacion.getString("nombre") + "-" + dotacion.getString("cargo"));
			}
			else {
				String unidadSig = unidadPadre(unidad, con);
				if(!unidadSig.equals("-1")) {
					map = Jefe(unidadSig, rut, con);
				}
				else {
					map.put("NO APLICA", "NO APLICA-NO APLICA");
				}
			}
		}
		else {
			String unidadSig = unidadPadre(unidad, con);
			if(!unidadSig.equals("-1")) {
				map = Jefe(unidadSig, rut, con);
			} 
			else {
				map.put("NO APLICA", "NO APLICA-NO APLICA");
			}
		}
		return map;	
	}
	
	private String Gerente(String unidad, Connection con) {
		Consulta dotacion = new Consulta(con);
		StringBuilder query = new StringBuilder("select distinct top 1 nombre=ltrim(rtrim(t.nombre)), j.nodo_nivel, ")
			.append("t.cargo,c.descrip from ((eje_ges_jerarquia j inner join eje_ges_unidad_encargado ue ")
			.append("on j.nodo_id=ue.unid_id and ue.estado=1) inner join eje_ges_trabajador t on ue.rut_encargado=t.rut) ")
			.append("inner join eje_ges_cargos c on t.cargo=c.cargo where j.id_tipo is null and ")
			.append("ue.unid_id in (select unidad from GetAscendentes('").append(unidad)
			.append("')) and (lower(c.descrip) like 'gerente%' or lower(c.descrip) like 'director%') ")
			.append("order by j.nodo_nivel desc");
		dotacion.exec(query.toString());
		if( dotacion.next() ) {
			return dotacion.getString("nombre");
		}
		else {
			return "NO APLICA";
		}
	}

}