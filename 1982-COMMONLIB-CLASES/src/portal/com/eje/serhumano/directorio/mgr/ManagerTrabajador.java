package portal.com.eje.serhumano.directorio.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import organica.datos.Consulta;
import portal.com.eje.tools.ExtrasOrganica;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerTrabajador {
	private static ManagerTrabajador instance;
	private List<String> columnName;
	
	public ManagerTrabajador() {
		columnName = new ArrayList<String>();
		columnName.add("rut");
		columnName.add("nombre");
		columnName.add("ape_paterno");
		columnName.add("ape_materno");
		columnName.add("cargo");
		columnName.add("desc_cargo");
		columnName.add("unidad");
		columnName.add("desc_unidad");
		columnName.add("telefono"); 
		columnName.add("celular"); 
		columnName.add("anexo"); 
		columnName.add("mail");
	}
	
	public static ManagerTrabajador getInstance() {
		if(instance == null) {
			synchronized (ManagerTrabajador.class) {
				if(instance == null) {
					instance = new ManagerTrabajador();
				}
			}
		}
		
		return instance;
	}

	
	public ConsultaData getTrabajadores() {
		return getTrabajadores(null);
	}
	
	public ConsultaData getTrabajadores(Integer rut) {
		StringBuilder strConsulta = new StringBuilder();

		if(rut == null) {
			strConsulta.append(" select	rut, ltrim(rtrim(nombres))+' '+ltrim(rtrim(ape_paterno))+' '+ltrim(rtrim(ape_materno))nombre, ''ape_paterno, ''ape_materno,  \n");
		}else{
			strConsulta.append(" select	rut, ltrim(rtrim(nombres))nombre, ltrim(rtrim(ape_paterno))ape_paterno, ltrim(rtrim(ape_materno))ape_materno,  \n");
		}

		strConsulta.append(" 		a.cargo, isnull(ltrim(rtrim(b.descrip)),'SIN CARGO')desc_cargo, a.unidad,  \n");
		strConsulta.append(" 		isnull(ltrim(rtrim(c.unid_desc)),'SIN UNIDAD')desc_unidad, telefono, celular, anexo, isnull(mail, e_mail)mail \n");
		strConsulta.append(" from	eje_ges_trabajador			a \n");
		strConsulta.append(" 		left join eje_ges_cargos	b on a.cargo = b.cargo and a.empresa = b.empresa \n");
		strConsulta.append(" 		left join eje_ges_unidades	c on a.empresa = c.unid_empresa and a.unidad = c.unid_id \n");
		
		ArrayList<Object> lista = new ArrayList<Object>();
		if(rut != null) {
			strConsulta.append(" where a.rut = ?");
			lista.add(rut);
		}
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),lista.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
//	public boolean existeTrabajador(int rut) {
//		StringBuilder strConsulta = new StringBuilder();
//		strConsulta.append(" SELECT 1 FROM eje_ges_trabajador ");
//		strConsulta.append(" WHERE rut = ? ");
//		
//		Object[] params = {rut};
//
//		ConsultaData data = null;
//		try {
//			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return data.next();
//	}
	
	
	

	
	
//	public ConsultaData getTrabajadores(Connection Conexion, String empresa, String superNodo) {
//		
//			String strPersonas = "";
//	        List orgUnidades = new ArrayList();
//            List lstPersonas = new ArrayList();
//            orgUnidades = ExtrasOrganica.NodosHijosRecursivos(superNodo,empresa,Conexion,orgUnidades); 
//            orgUnidades.add(superNodo); 
//            
//            lstPersonas = getTrabajadoresUnidades(Conexion, orgUnidades);
//            strPersonas = lstPersonas.toString().replace("[", "").replace("]", "");
//
//            return getTrabajadoresPerfilados(strPersonas, null);
//	}
	
	public ConsultaData getTrabajador(String rut) {
		return getTrabajadores(Validar.getInstance().validarInt(rut, -1));
	}
	
	public ConsultaData getTrabajador(int rut) {
		return getTrabajadores(rut);
	}
	
	
    public List getTrabajadoresUnidades(Connection conn, List orgUnidades) {
		Consulta consulta = new Consulta(conn);
        List lstTrabajadores = new ArrayList();

		for (int i=0; i < orgUnidades.size(); i++) {
			if(orgUnidades.get(i) != null) {
				String strUnidad = orgUnidades.get(i).toString();
				StringBuilder sql = new StringBuilder("select distinct rut from eje_ges_trabajador where unidad = '"+ strUnidad +"' ");
	
				consulta.exec(String.valueOf(sql));
				for(;consulta.next();) {
					lstTrabajadores.add(consulta.getString("rut"));
			    }
			}
		}

		return lstTrabajadores;
	} 
    
	


}
