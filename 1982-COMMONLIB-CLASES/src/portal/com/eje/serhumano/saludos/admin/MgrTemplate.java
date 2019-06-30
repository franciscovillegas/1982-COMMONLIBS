package portal.com.eje.serhumano.saludos.admin;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class MgrTemplate extends AbsClaseWeb{

    protected DBConnectionManager connMgr;

	public MgrTemplate(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	
	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {

	}

	public ConsultaData getTemplates() {
		String sql = "select * from eje_ges_template";
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql);
			return data;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ConsultaData getTemplates(int idTemplate) {
		String sql = "select * from eje_ges_template where id_template = ? ";
		
		try {
			Object[] params = {idTemplate};
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql, params);
			return data;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean addTemplate(String nombreTemplate, String textoTemplate, int idFile){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into eje_ges_template ");
		sql.append(" 	(mensaje, nombre, id_imagen) ");
		sql.append(" values ");
		sql.append(" 	(?,?,?) ");
	
		try {
			Object[] params = {textoTemplate, nombreTemplate, idFile};
			ConsultaTool.getInstance().insert("portal",sql.toString(),params);
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			
		return false;
	
	}
	
	
	public boolean updTemplate(String nombreTemplate, String textoTemplate, int idFile){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE eje_ges_template ");
		sql.append(" 	set mensaje = ?, nombre = ?, id_imagen = ? ");
		sql.append(" WHERE ");
		sql.append(" 	(?,?,?) ");
	
		try {
			Object[] params = {textoTemplate, nombreTemplate, idFile};
			ConsultaTool.getInstance().insert("portal",sql.toString(),params);
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			
		return false;
	}


	public double addProgramacion(String idPlantilla, String hora, String minuto, String solounavez, String estado, String destinatarios) {
		String sql = "insert into eje_ges_template_programacion (idtemplate,hora, minuto, solounavez, estado,destinatarios) values  (?,?,?,?,?,?)";
		Object[] params = {idPlantilla, hora, minuto, solounavez, estado, destinatarios};
		
		try {
			return ConsultaTool.getInstance().insertIdentity("portal",sql, params);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public ConsultaData getProgramacions() {
		String sql = "select * from eje_ges_template_programacion";
		
		try {
			return  ConsultaTool.getInstance().getData("portal",sql);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ConsultaData getProgramacions(String idProgramacion) {
		String sql = "select * from eje_ges_template_programacion where id_programacion = ?";
		
		try {
			Object[] params = {idProgramacion};
			return  ConsultaTool.getInstance().getData("portal",sql, params);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
