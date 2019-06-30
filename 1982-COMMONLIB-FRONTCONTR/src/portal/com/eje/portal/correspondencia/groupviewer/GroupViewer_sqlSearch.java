package portal.com.eje.portal.correspondencia.groupviewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Message.RecipientType;

import portal.com.eje.portal.correspondencia.CorrespondenciaMailLocator;
import portal.com.eje.tools.ArrayFactory;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;

public class GroupViewer_sqlSearch implements IGroupViewer {

	
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario>  finalLista, Double idGrupo, RecipientType tipo) {
		addDestinatariosFromGrupo(finalLista, idGrupo, null, tipo);
	}
	
	private ArrayFactory getArrayFromBD(String tabla, String campo, Double idGrupo) {
		String sql = "SELECT "+campo+" FROM "+tabla+" WHERE id_grupo = ? ";
		
		Object[] params = {idGrupo};
		ArrayFactory factory = new ArrayFactory();
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			while(data != null && data.next()) {
				if(data.getForcedString(campo) != null && !"".equals(data.getForcedString(campo))) {
					factory.add(data.getForcedString(campo));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return factory;
	} 
	

	@Override
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario> finalLista, Double idGrupo, Double idTimer, RecipientType tipo) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		String sql = "SELECT id_grupo,nombre,descripcion,fec_creacion,include_personas_nuevas,include_personas_decumpleanos,";
		sql += "include_personas_genero,include_personas_cc,include_personas_selected,include_personas_empresa, include_personas_cargo, ";
		sql += "include_personas_ciudad,include_personas_comuna,include_personas_organica, ";
		sql += "path_class,vigente ";
		sql += "FROM eje_correspondencia_grupo WHERE id_grupo = ? and isnull(vigente,1) = 1";
		
		StringBuilder sqlIntern = new StringBuilder();

		try {
			Object[] params = {idGrupo};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			List<Map<IVoDestinatario, Boolean>> listas = new ArrayList<Map<IVoDestinatario,Boolean>>();
			
			sqlIntern.append("SELECT t.rut, t.nombre \n");
			sqlIntern.append("FROM EJE_GES_TRABAJADOR T LEFT OUTER JOIN EJE_GES_CARGOS C ON C.CARGO = T.CARGO and c.empresa = t.wp_cod_empresa  \n");
			sqlIntern.append(" WHERE 1=1 \n");
			
			if( data != null && data.next()) {
				
				if(data.getBoolean("include_personas_nuevas")) {
					sqlIntern.append(" and not t.rut in (select c1.rut \n");
					sqlIntern.append(" 					from eje_ges_certif_histo_liquidacion_cabecera c1 \n"); 
					sqlIntern.append(" 					where c1.periodo in ( \n");
					sqlIntern.append(" 											select top 1 m2.periodo \n"); 
					sqlIntern.append(" 											from (select top 2  periodo from eje_ges_certif_histo_liquidacion_cabecera c group by periodo order by periodo desc) as m2 order by m2.periodo asc) \n");
					sqlIntern.append(" "); 											 
					sqlIntern.append(" 					)\n").append(" \n\n"); 
				}
				
				if(data.getBoolean("include_personas_decumpleanos")) {
					
					if(idTimer != null) {
						sqlIntern.append(" and substring(CONVERT(VARCHAR,t.fecha_nacim, 112),5,8) in ( select futuro = substring(CONVERT(VARCHAR,fec_execute, 112),5,8) from eje_correspondencia_timer where id_timer = "+idTimer+") \n\n");	
					}
					else {
						sqlIntern.append(" and substring(CONVERT(VARCHAR,t.fecha_nacim, 112),5,8) = substring(CONVERT(VARCHAR,GETDATE(), 112),5,8) \n\n");	
					}
					
					
				}
				
				if(data.getBoolean("include_personas_genero")) {
					ArrayFactory generos = getArrayFromBD("eje_correspondencia_grupo_genero", "id", idGrupo);
					if(generos.size() > 0) {
						sqlIntern.append(" and t.sexo in").append(generos.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_cc")) {
					ArrayFactory cc = getArrayFromBD("eje_correspondencia_grupo_cc", "nombre", idGrupo);
					if(cc.size() > 0) {
						sqlIntern.append(" and t.ccosto in").append(cc.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_selected")) {
					ArrayFactory generos = getArrayFromBD("eje_correspondencia_grupo_selected", "rut", idGrupo);
					if(generos.size() > 0) {
						sqlIntern.append(" and t.rut in").append(generos.getArrayString()).append(" \n\n");
					}
				}
				/*2016-04-29*/
				if(data.getBoolean("include_personas_empresa")) {
					ArrayFactory empresas = getArrayFromBD("eje_correspondencia_grupo_empresa", "empresa", idGrupo);
					if(empresas.size() > 0) { 
						sqlIntern.append(" and t.wp_cod_empresa in").append(empresas.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_cargo")) {
					ArrayFactory cargos = getArrayFromBD("eje_correspondencia_grupo_cargo", "nombre", idGrupo);
					if( cargos.size() > 0) {
						sqlIntern.append(" and c.descrip in").append(cargos.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_ciudad")) {
					ArrayFactory ciudades = getArrayFromBD("eje_correspondencia_grupo_ciudad", "nombre", idGrupo);
					if( ciudades.size() > 0) {
						sqlIntern.append(" and t.ciudad in").append(ciudades.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_comuna")) {
					ArrayFactory comunas = getArrayFromBD("eje_correspondencia_grupo_comuna", "nombre", idGrupo);
					if( comunas.size() > 0 ){
						sqlIntern.append(" and t.comuna in").append(comunas.getArrayString()).append(" \n\n");
					}
				}
				
				if(data.getBoolean("include_personas_organica")) {
					ArrayFactory unidades = getArrayFromBD("eje_correspondencia_grupo_unidad", "nombre", idGrupo);
					if( unidades.size() > 0 ) {
						sqlIntern.append(" and t.unidad in").append(unidades.getArrayString()).append(" \n\n");
					}
				}
			}
			
			//System.out.println(sqlIntern.toString());
			
		    data = ConsultaTool.getInstance().getData("portal", sqlIntern.toString());
			while(data != null && data.next()) {
				IVoDestinatario vo2 = new VoDestinatario(data.getInt("rut"), data.getForcedString("nombre"), 
														 CorrespondenciaMailLocator.getInstance().getCorreo(data.getInt("rut")).getEmail(), tipo); 
				
				finalLista.put(vo2.toString(), vo2);
			}
	 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Personas encontradas: " + finalLista.size() + " " + cro.GetTimeHHMMSS());
		
	}
	
}
