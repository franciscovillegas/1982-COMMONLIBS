package portal.com.eje.portal.correspondencia.groupviewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.NotImplementedException;

import portal.com.eje.portal.correspondencia.CorrespondenciaMailLocator;
import portal.com.eje.tools.ArrayFactory;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;

public class GroupViewer_javaSearch implements IGroupViewer {

	
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario>  finalLista, Double idGrupo, RecipientType tipo) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		String sql = "SELECT id_grupo,nombre,descripcion,fec_creacion,include_personas_nuevas,include_personas_decumpleanos,";
		sql += "include_personas_genero,include_personas_cc,include_personas_selected,include_personas_empresa, include_personas_cargo, ";
		sql += "include_personas_ciudad,include_personas_comuna,include_personas_organica, ";
		sql += "path_class,vigente ";
		sql += "FROM eje_correspondencia_grupo WHERE id_grupo = ? and isnull(vigente,1) = 1";
		
		
		try {
			Object[] params = {idGrupo};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			List<Map<IVoDestinatario, Boolean>> listas = new ArrayList<Map<IVoDestinatario,Boolean>>();
			
			while( data != null && data.next()) {
				if(data.getBoolean("include_personas_nuevas")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasNuevas(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_decumpleanos")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasDeCumpleanos(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_genero")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasConGenero(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_cc")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasConCC(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_selected")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasSelected(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				/*2016-04-29*/
				if(data.getBoolean("include_personas_empresa")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasEmpresas(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_cargo")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasCargo(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_ciudad")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasCiudad(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_comuna")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasComuna(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
				
				if(data.getBoolean("include_personas_organica")) {
					Map<IVoDestinatario, Boolean>  listHere  = new LinkedHashMap<IVoDestinatario, Boolean>();
					addDestinatarios_personasUnidades(listHere, idGrupo, tipo);
					listas.add(listHere);
				}
			}
			
			
			for(Map<IVoDestinatario, Boolean> map : listas) {
				Set<IVoDestinatario> setMap  = map .keySet();
				
				for(IVoDestinatario vo : setMap) {
					if(isInAll(vo, listas)) {
						if(vo != null && finalLista.get(vo.toString()) == null ) {
							try {
								IVoDestinatario vo2 = new VoDestinatario(vo.getRut(), vo.getNombre(),
									    CorrespondenciaMailLocator.getInstance().getCorreo(vo.getRut()).getEmail(), vo.getTipo());
								finalLista.put(vo2.toString(), vo2);	
							}
							catch(Exception e) {
								e.printStackTrace();
							}
								
						}
						
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Personas encontradas: " + finalLista.size() + " " + cro.GetTimeHHMMSS());
	}
	
	

	private void addDestinatarios_personasNuevas(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		String sql = " select distinct top 2  periodo from eje_ges_certif_histo_liquidacion_cabecera order by periodo desc ";
		ConsultaData data;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			int i=0;
			String ultimo = null;
			String penUltimo = null;
			
			while(data != null && data.next()) {
				if(i == 0){
					ultimo = data.getForcedString("periodo");
				}
				else if(i > 0) {
					penUltimo = data.getForcedString("periodo");
				}
				
				i++;
			}
			
			sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where not t.rut in (select c.rut from eje_ges_certif_histo_liquidacion_cabecera c where c.periodo = ? ) order by ltrim(rtrim(t.nombre)) ";
			Object[] params = {penUltimo};
			
			data = ConsultaTool.getInstance().getData("portal", sql, params );
		 
			while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private boolean isInAll(IVoDestinatario vo, List<Map<IVoDestinatario, Boolean>> listas) {
		boolean isPresent = true;
		
		for(Map<IVoDestinatario, Boolean> map : listas) {
			Set<IVoDestinatario> setVO = map.keySet();
			
			if(setVO.size() == 0 ) {
				isPresent &= false;
			}
			else {
				boolean isPresentInside = false;
				
				
				for(IVoDestinatario v : setVO) {
					if(v != null && vo != null && v.toString().equals(vo.toString()) ) {
						isPresentInside = true;
						break;
					}
				}
				
				isPresent &= isPresentInside;
			}
		}
		
		return isPresent;
	}
	


	private boolean isValidMail(String mail) {
		if(mail == null) {
			return false;
		}
		else if(mail.lastIndexOf("@") != mail.indexOf("@")) {
			return false;
		}
		else if( mail.indexOf("@") == -1) {
			return false;
		}
		else {
			return true;
		}
	}
	private void addDestinatarios_personasDeCumpleanos(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where substring(convert(varchar(8), fecha_nacim, 112),5,8) = substring(convert(varchar(8), getdate(), 112),5,8)    order by ltrim(rtrim(t.nombre)) ";

		ConsultaData data;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql );
			
			while(data != null && data.next()) {
				IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
				
				list.put(vo,true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private void addDestinatarios_personasConGenero(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory sexos = new ArrayFactory();
		
		{
			String sql ="select id from eje_correspondencia_grupo_genero where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					sexos.add(data.getForcedString("id"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where sexo in "+sexos.getArrayString()+"  order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasConCC(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory sexos = new ArrayFactory();
		
		{
			String sql ="select id from eje_correspondencia_grupo_cc where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					sexos.add(data.getForcedString("id"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where t.ccosto in "+sexos.getArrayString()+"  order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasSelected(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory sexos = new ArrayFactory();
		
		{
			String sql ="select rut from eje_correspondencia_grupo_selected where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					sexos.add(data.getForcedString("rut"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, t.nombres, t.mail from eje_ges_trabajador t where t.rut in "+sexos.getArrayString()+" order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasEmpresas(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory empresas = new ArrayFactory();
		
		{
			String sql ="select empresa from eje_correspondencia_grupo_empresa where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					if( data.getForcedString("empresa") != null && !"".equals(data.getForcedString("empresa"))) {
						empresas.add(data.getForcedString("empresa"));		
					}

				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t ";
			
			if(empresas.size() > 0) {
				sql += "where t.wp_cod_empresa in "+empresas.getArrayString()+" ";
			}
			
			sql += " order by ltrim(rtrim(t.nombre))";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasCargo(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory cargos = new ArrayFactory();
		
		{
			String sql ="select nombre from eje_correspondencia_grupo_cargo where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					cargos.add(data.getForcedString("nombre"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where t.cargo in "+cargos.getArrayString()+"  order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasCiudad(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory ciudad = new ArrayFactory();
		
		{
			String sql ="select nombre from eje_correspondencia_grupo_ciudad where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					ciudad.add(data.getForcedString("nombre"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where t.ciudad in "+ciudad.getArrayString()+" order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasComuna(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory comuna = new ArrayFactory();
		
		{
			String sql ="select nombre from eje_correspondencia_grupo_comuna where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					comuna.add(data.getForcedString("nombre"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where t.comuna in "+comuna.getArrayString()+" order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addDestinatarios_personasUnidades(Map<IVoDestinatario, Boolean> list, Double idGrupo, RecipientType tipo) {
		ArrayFactory unidades = new ArrayFactory();
		
		{
			String sql ="select nombre from eje_correspondencia_grupo_unidad where id_grupo = ? ";
			Object[] params = {idGrupo};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					unidades.add(data.getForcedString("nombre"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		{
			String sql = "select t.rut, nombres=t.nombre, t.mail from eje_ges_trabajador t where t.unidad in "+unidades.getArrayString()+" order by ltrim(rtrim(t.nombre)) ";
	
			ConsultaData data;
			try {
				data = ConsultaTool.getInstance().getData("portal", sql );
				
				while(data != null && data.next()) {
					IVoDestinatario vo = new VoDestinatario(data.getInt("rut"), data.getString("nombres"),data.getForcedString("mail"), tipo);
					list.put(vo, true);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario> finalLista, Double idGrupo, Double idTimer,RecipientType tipo) {
		throw new NotImplementedException();
	}
	
}
