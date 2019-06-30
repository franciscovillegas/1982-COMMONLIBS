package portal.com.eje.tools.portal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import organica.datos.Consulta;

import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.UserMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.excel.xlsx.Excel2010;
import portal.com.eje.tools.excel.xlsx.ExcelProcessConsultaData;
import portal.com.eje.tools.excel.xlsx.IExcel;
import portal.com.eje.tools.jqtree.JqNodo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ToolNewOrganicaIO {

	
	public boolean procesaJerarquia(File f, Usuario userPortal)   {
		boolean ok = true;
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
	
		try {
			
			IExcel excel = new Excel2010(true);
			ExcelProcessConsultaData process = new ExcelProcessConsultaData(3);
			excel.getWorkBookDinamicamente(f, 0, 3, process);
			ConsultaData data = process.getDataExcel();
			
			String unidId = "";
			String unidGlosa = "";
			String unidIdPadre = ""; 
			
			ConsultaTool.getInstance().update("portal", "DELETE FROM eje_ges_jerarquia");
			ConsultaTool.getInstance().update("portal", "DELETE FROM eje_ges_unidades");
			
			String insertJerarquia  = "INSERT INTO eje_ges_jerarquia (compania,nodo_id, nodo_padre) VALUES (0,?,?) ";
			String insertUnidad 	= "INSERT INTO EJE_GES_UNIDADES  (unid_empresa, unid_id, unid_desc, vigente)  VALUES (0,?,?,'S')";
			
			while(data.next()) {
				unidId		= data.getForcedString(0);
				unidGlosa	= data.getForcedString(1);
				unidIdPadre	= data.getForcedString(2);
				
				if("".equals(unidIdPadre) || "0".equals(unidIdPadre) || "-1".equals(unidIdPadre) ) {
					unidIdPadre = getNodoPadre(conn, 0).getIdNodo();
				}
				
				String[] paramsJerarquia = {unidId,unidIdPadre};
				String[] paramsUnidad = {unidId,unidGlosa};
				
				ConsultaTool.getInstance().update(conn, insertUnidad, paramsUnidad);
				ConsultaTool.getInstance().update(conn, insertJerarquia, paramsJerarquia);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if(conn != null) {
				DBConnectionManager.getInstance().freeConnection("portal", conn);
			}
			
		}
		
		return ok;
	}
	
	public void getSubTree(Connection conn,JqNodo nodoJqStart, int tipoOrg) {
		int cantHijos = 0;
		Cronometro cro = new Cronometro();
		cro.Start();
		StringBuilder str = new StringBuilder("");

		//System.out.println("nodoJqStart -->"+nodoJqStart);
		
		if (tipoOrg == 0){
			str.append("SELECT j.nodo_id , u.unid_desc ");
			str.append(" FROM dbo.eje_ges_jerarquia j left outer join eje_ges_unidades u on j.nodo_id = u.unid_id ");
			str.append(" WHERE j.nodo_padre = '").append(nodoJqStart.getIdNodo()).append("'");
			str.append(" and isnull(u.id_tipo,0) = ").append(tipoOrg);
		}else {
			str.append("select	distinct a.unidad nodo_id, b.unid_desc ");
			str.append(" from	eje_ges_trabajador_unidad a left outer join eje_ges_unidades b on a.unidad = b.unid_id ");
			str.append(" where	rut in (select	distinct rut ");
			str.append(" 				from	eje_ges_trabajador_unidad");
			str.append(" 				where	unidad in (select	distinct nodo_id");
			str.append(" 									from	eje_ges_jerarquia");
			str.append(" 									where	nodo_padre = '").append(nodoJqStart.getIdNodo()).append("'))") ;
			str.append(" 		and isnull(a.tipo_jerarquia,0) = ").append(tipoOrg);
		}
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(str.toString());
			rs = pst.executeQuery();
			
			while(rs.next()) {
				cantHijos++;
				JqNodo nodoJqHijo = new JqNodo(rs.getString("unid_desc"),rs.getString("nodo_id"));
				nodoJqStart.addHijo(nodoJqHijo);
				getSubTree(conn, nodoJqHijo, tipoOrg);
			}
			
			rs.close();
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		//System.out.println(new StringBuilder("->getHijos H:").append(cantHijos).append("    T:").append(cro.GetMilliseconds()).append(" (").append(nodoJq).append(") "));
	}
	
	public JqNodo getNodoPadre(Connection conn, int tipoOrg) {
		
		StringBuilder str = new StringBuilder();
		str.append(" SELECT nodo_raiz as nodo_padre,");
		str.append("			case ");
		str.append("				when id >= 0 then (select descrip from eje_ges_empresa where padre_empresa is null) ");
		str.append("	   		else ");
		str.append("				unid_desc ");
		str.append("			end unid_desc ");
		str.append(" FROM		eje_ges_tipo_jerarquia		a ");
		str.append("	left join eje_ges_unidades	b on a.nodo_raiz = b.unid_id ");
		str.append(" WHERE id = ").append(String.valueOf(tipoOrg));

		
		JqNodo nodo = null ;
		ResultSet rs = null;
		PreparedStatement pst = null;
		
		System.out.println("Conexion->"+conn);
		System.out.println(str.toString());
		
		try {
			pst = conn.prepareStatement(str.toString());
			rs = pst.executeQuery();

			if(rs != null && rs.next()) {
				nodo = new JqNodo(rs.getString("unid_desc"),rs.getString("nodo_padre"));
			}
			else {
				nodo = new JqNodo("Empresa","0");
			}

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return nodo;
	}
	
	/**
	 * Descativa todo registro de encargado de unidad, es lo mismo que :
	 * <p>UPDATE eje_ges_unidad_encargado set estado = 0 WHERE u.rut_encargado = ?</p>
	 * */
	public boolean delEncargadoUnidad(int rut) {
		String sql = "UPDATE eje_ges_unidad_encargado set  estado = 0 WHERE rut_encargado = ?";
		Object[] params = {rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal", sql,params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean procesaAsociacion(File f, Usuario userPortal, boolean soloUnJefePorUnidad)   {
		boolean ok = true;
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
	
		try {
			CapManager cm = new CapManager(conn);
			SimpleHash modelRoot = new SimpleHash();
			UserMgr mgr = new UserMgr(conn);
			
			IExcel excel = new Excel2010(true);
			ExcelProcessConsultaData process = new ExcelProcessConsultaData(3);
			excel.getWorkBookDinamicamente(f, 0, 3, process);
			ConsultaData data = process.getDataExcel();
			
			int filas = 3,estadoIU = 0,exito = 0,fallos = 0;
			String rut = "",unidad = "",encargado = "",celda = "",acc_org = "0";
			SimpleList FallosList = new SimpleList();
			SimpleHash FallosIter = null;
		
			PreparedStatement updateUnidad =
					conn.prepareStatement("update eje_ges_trabajador set unidad=? where rut=?");
			while(data.next()) {
				rut		= data.getForcedString(0);
				unidad	= data.getForcedString(1);
				encargado	= data.getForcedString(2);

				delEncargadoUnidad(Validar.getInstance().validarInt(rut,-1));
//				this.insertarPriv(rut, encargado, acc_org);
				// determinar si amerita registrar rotacion de unidad
				if(!unidad.equals(cm.IgualdadUnidadTrabajador(rut)))
					cm.UpdateUnidadTrabajador(rut,cm.IgualdadUnidadTrabajador(rut),unidad);

				// updatear la unidad dado el rut
				updateUnidad.setString(1,unidad);
				updateUnidad.setString(2,rut);
				estadoIU = updateUnidad.executeUpdate();
				if(estadoIU == 1) {
					exito++;
					// determinar si es encargado de unidad
					if(encargado.equals("1")) {
						
 						Consulta encargado_unidad = new Consulta(conn);
						Calendar ahoraCal = Calendar.getInstance();
						String mes = null;
						if((ahoraCal.get(Calendar.MONTH) + 1) < 10)
							mes = "0" + String.valueOf(ahoraCal.get(Calendar.MONTH) + 1);
						else
							mes = String.valueOf(ahoraCal.get(Calendar.MONTH) + 1);
						String ano = String.valueOf(ahoraCal.get(Calendar.YEAR));
						String periodo = ano + mes;
						String query = "";
						

							
						// insertamos el nuevo encargado de unidad					
						mgr.insertUsuarioEncargado(rut, unidad, periodo, userPortal.getRutId(), "1", acc_org);
						mgr.insertUsuarioUnidad(rut, unidad);

						if(soloUnJefePorUnidad) {
							// anulamos al encargado actual de la unidad
							mgr.actualizarEstadoEncargadoUnidad(unidad);
							
							//se borra el usuario de eje_generico_perfil_usuario
							mgr.borrarEncargadoAnterior(unidad, 2);
						}

						
						if("1".equals(acc_org)) {
							mgr.addAccesoGestion(conn,rut);
						}
						
						// insertamos permisos para organica
						query =
							String.valueOf(String.valueOf((new StringBuilder(
								"select app_id,wp_cod_empresa,wp_cod_planta from eje_ges_user_app where rut_usuario="
									+ rut + ""))));
						encargado_unidad.exec(query);
						String ee = "-1";
						String pe = "1";
						int k = 0,ish = 0,idf = 0,iac = 0;
						for(; encargado_unidad.next();) {
							if((!encargado_unidad.getString("wp_cod_empresa").equals(ee)) && (k == 0)) {
								ee = encargado_unidad.getString("wp_cod_empresa");
								pe = encargado_unidad.getString("wp_cod_planta");
								k = 1;
							}
							if(encargado_unidad.getString("app_id").equals("sh"))
								ish = 1;
							else if(encargado_unidad.getString("app_id").equals("df"))
								idf = 1;
							else if(encargado_unidad.getString("app_id").equals("adm_corp"))
								iac = 1;
						}
						if(ish == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('sh',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
						if(idf == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('df',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
						if(iac == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('adm_corp',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
					}
					else {
						mgr.borrarEncargadoAnteriorPorRut(rut);
						userPortal.quitarAccesoGestionPorUnidad(conn,unidad,rut);
						Consulta encargado_unidad = new Consulta(conn);
						// anulamos al encargado actual de la unidad
						String query =
							String.valueOf(String.valueOf((new StringBuilder(
								"update eje_ges_unidad_encargado set estado=0 where rut_encargado=")
								.append(rut))));
						//userPortal.addAccesoGestion(Conexion,rut);

						encargado_unidad.insert(query);
						// borramos permisos para organica
						query =
							String.valueOf(String.valueOf((new StringBuilder(
								"delete from eje_ges_user_app where rut_usuario=" + rut
									+ " and app_id in ('df','adm_corp')"))));
						encargado_unidad.insert(query);
					}
				}
				else {
					fallos++;
					FallosIter = new SimpleHash();
					FallosIter.put("rut",rut);
					FallosIter.put("unidad",unidad);
					FallosList.add(FallosIter);
				}
			}

			modelRoot.put("exito",String.valueOf(exito));
			modelRoot.put("fallos",String.valueOf(fallos));
			if(fallos != 0)
				modelRoot.put("lfallos",FallosList);
			
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
			
		} catch (FileNotFoundException e) {
			ok = false;
			e.printStackTrace();
			
		} catch (IOException e) {
			ok = false;
			e.printStackTrace();
			
		}
		finally {
			if(conn != null) {
				DBConnectionManager.getInstance().freeConnection("portal", conn);
			}
			
		}
	
		return ok;
	}
	
	public ConsultaData getConsulta_DetalleTrabajadoresYUnidades() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT t.rut, t.digito_ver, t.nombre, t.nombres, t.ape_materno, t.ape_paterno, ");
		sql.append(" 		u.unid_id as unidad, u.unid_desc, u.unid_id_padre, e.estado as encargado_unidad ");
		sql.append(" FROM eje_ges_trabajador t LEFT OUTER JOIN "); 
		sql.append(" 	(select u.unid_id,U.unid_desc, j.nodo_padre as unid_id_padre from eje_ges_unidades u inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id and u.vigente = 'S') u ");
		sql.append(" 	on U.unid_id = t.unidad ");
		sql.append(" LEFT OUTER JOIN eje_ges_unidad_encargado e on e.rut_encargado = t.rut and e.estado = 1");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	
	public boolean setNodoParent(String nodoId, String newNodoIdParent) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update eje_ges_jerarquia ");
		sql.append(" 	set nodo_padre = ? "); 
		sql.append(" where nodo_id = ? ");

		boolean ok = false;
		try {
			Object[] params = {newNodoIdParent, nodoId};
			ok = ConsultaTool.getInstance().update("portal", sql.toString(),params) > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	/**
	 * Retorna la unidad en la que es Jefe, si no es jefe de ninguna unidad, entonces retorna NULL
	 * */
	public String getUnidadWhereIsEncargado(int rut) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.unid_id,u.unid_desc \n ");
		sql.append(" FROM eje_ges_unidad_encargado e \n "); 
		sql.append(" 	INNER JOIN eje_ges_jerarquia j on j.nodo_id = e.unid_id "); 
		sql.append(" 	INNER JOIN eje_ges_unidades u on u.unid_id = e.unid_id ");
		sql.append(" WHERE e.estado = 1 and e.rut_encargado = ? ");
		
		Object[] params = {rut};
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(),params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(data != null && data.next()) {
			return data.getString("unid_id");
		}
		else {
			return null;
		}
	
	}
	
	/**
	 * Método que entrega la unidad sin dependencias ni asociación de hijos.
	 * En el caso de no encontrar la unidad, retorna el objeto sin valores.
	 * 
	 * */
	public NodoUnidad getUnidad(String unidIid) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select unid_empresa,unid_id,unid_desc,area,vigente from eje_ges_unidades where unid_id = ? ");
		
		Object[] params = {unidIid};
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(),params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		NodoUnidad unid = new NodoUnidad();
		
		if(data != null && data.next()) {
			unid.setVigente(data.getString("vigente"));
			unid.setUnidId(data.getString("unid_id"));
			unid.setUnidEmpresa(data.getString("unid_empresa"));
			unid.setUnidDesc(data.getString("unid_desc"));
			unid.setArea(data.getString("area"));
		}
		
		
		return unid;
	}
	
	
	public class NodoUnidad {
		private String unidEmpresa;
		private String unidId;
		private String unidDesc;
		private String area;
		private String vigente;
		private List<NodoUnidad> hijos;
		
		public NodoUnidad() {
			hijos = new ArrayList<NodoUnidad>();
		}	
		public List<NodoUnidad> getHijos() {
			return hijos;
		}
		public void setHijos(List<NodoUnidad> hijos) {
			this.hijos = hijos;
		}
		public String getUnidEmpresa() {
			return unidEmpresa;
		}
		public void setUnidEmpresa(String unidEmpresa) {
			this.unidEmpresa = unidEmpresa;
		}
		public String getUnidId() {
			return unidId;
		}
		public void setUnidId(String unidId) {
			this.unidId = unidId;
		}
		public String getUnidDesc() {
			return unidDesc;
		}
		public void setUnidDesc(String unidDesc) {
			this.unidDesc = unidDesc;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getVigente() {
			return vigente;
		}
		public void setVigente(String vigente) {
			this.vigente = vigente;
		}
		
		
	}

}
