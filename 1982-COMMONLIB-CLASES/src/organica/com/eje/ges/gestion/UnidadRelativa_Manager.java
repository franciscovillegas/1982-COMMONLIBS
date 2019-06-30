package organica.com.eje.ges.gestion;

import java.sql.Connection;
import java.util.ResourceBundle;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class UnidadRelativa_Manager {

	public UnidadRelativa_Manager(Connection conex) {
		con = conex;
		mensajeError = "";
	}

	public String getError() {
		return mensajeError;
	}

	public Consulta GetTrabajadores(String unidad) {
    	consul = new Consulta(con);
    	ResourceBundle proper;
    	proper = ResourceBundle.getBundle("db");
    	String sql = null;
    	String tipoUsuario = proper.getString("usuarios.unidadrelativa");
    	if(tipoUsuario.equals("0")) {
    		sql = "SELECT rut,nombre FROM eje_ges_trabajador where unidad = '" + unidad + 
    		      "' ORDER BY nombre";
    	}
    	else {
    		sql = "select t.rut,t.nombre from eje_ges_trabajador t where t.unidad='" + unidad + 
    		      "' and t.rut in (select distinct rut from eje_ges_unidad_encargado where " +
    		      "estado=1 and unid_id='" + unidad + "') ORDER BY t.nombre";
    	}
        OutMessage.OutMessagePrint("Trabajadores Unidad: " + sql);
        consul.exec(sql);
    	return consul;
	}

	public Consulta GetUnidadesxRut(String rut) {
    	consul = new Consulta(con);
    	String sql = "select unid_id nodo_id,unid_desc from eje_ges_unidades where unid_id " +
    	             "not in (select unidad from eje_ges_usuario_unidad where " +
    	             "rut_usuario=" + rut + ") order by unid_desc";
    	OutMessage.OutMessagePrint("Unidades: " + sql);
    	consul.exec(sql);
    	return consul;
	}

	public Consulta GetUnidades(String unidad) {
    	consul = new Consulta(con);
    	ResourceBundle proper;
    	proper = ResourceBundle.getBundle("db");
    	String sql = null;
    	String tipoUsuario = proper.getString("usuarios.unidadrelativa");
    	if(tipoUsuario.equals("0")) {
    		sql = "select j.nodo_id,u.unid_desc from eje_ges_jerarquia j inner " +
    		      "join eje_ges_unidades u on j.nodo_id=u.unid_id where nodo_id in " +
    		      "(select unidad from GetDescendientes('" + unidad + "')) order by u.unid_desc";
    	}
    	else {
    		sql = "select j.nodo_id,u.unid_desc from eje_ges_jerarquia j inner join " +
    		      "eje_ges_unidades u on j.nodo_id=u.unid_id where nodo_id<>'" + unidad + 
    		      "' order by u.unid_desc";
    	}
        OutMessage.OutMessagePrint("Unidades: " + sql);
    	consul.exec(sql);
    	return consul;
	}

	public Consulta GetUnidadesRelativasRut(String rut) {
    	consul = new Consulta(con);
    	String sql = "select uu.unidad,u.unid_desc,uu.tipo from eje_ges_usuario_unidad uu " +
    	             "left outer join eje_ges_unidades u on uu.unidad=u.unid_id where " +
    	             "uu.rut_usuario=" + rut + " order by uu.rut_usuario,u.unid_desc";
        OutMessage.OutMessagePrint("Unidades Relativas: " + sql);
    	consul.exec(sql);
    	return consul;
	}
	
	public boolean grabaUnidadRelativa(String rut, String unidadrelativa, String empresa, String planta, String tipo) {
    	boolean salida = false;
		consul = new Consulta(con);
    	String sql = "insert into eje_ges_usuario_unidad(rut_usuario,unidad,empresa,tipo) " +
    	             "values(" + rut + ",'" + unidadrelativa + "'," + empresa + ",'" + tipo + "')";
        OutMessage.OutMessagePrint("graba Unidades Relativas: " + sql);
    	if (consul.insert(sql)) {
    		Consulta consul2 = new Consulta(con);
    		String sql2 = "select count(rut_usuario) existe from eje_ges_user_app where app_id='df' " +
    		              "and rut_usuario=" + rut;
    		consul2.exec(sql2); consul2.next();
    		if(consul2.getString("existe").equals("0")) {
    			Consulta consul3 = new Consulta(con);
    			String sql3 = "insert into eje_ges_user_app " +
    			              "values('df'," + rut + ",NULL," + empresa + "," + planta + ")";
    			consul3.insert(sql3);
    		}
    		salida=true;
    	}
    	return salida;
	}
	
	public boolean borraUnidadRelativa(String rut, String unidadrelativa) {
    	boolean salida = false;
		consul = new Consulta(con);
    	String sql = "delete from eje_ges_usuario_unidad where rut_usuario=" + rut + 
    	             " and unidad='" + unidadrelativa + "'";
        OutMessage.OutMessagePrint("borra Unidades Relativas: " + sql);
    	if (consul.insert(sql)) {
    		Consulta consul2 = new Consulta(con);
    		String sql2 = "select count(rut_usuario) existe from eje_ges_usuario_unidad " +
    		              "where rut_usuario=" + rut;
    		consul2.exec(sql2); consul2.next();
    		if(consul2.getString("existe").equals("0")) {
    			Consulta consul3 = new Consulta(con);
    			String sql3 = "delete eje_ges_user_app where app_id='df' and rut_usuario=" + rut;
    			consul3.insert(sql3);
    		}
    		salida = true;
    	}
    	return salida;
	}

    public SimpleList getSimpleSecuences(Consulta rs) {  
    	SimpleList ss = new SimpleList();
    	if(rs != null) {  
    		ss = rs.getSimpleList(); rs.close(); 
    	}
    	return ss;
    }

    public SimpleHash getSimpleHash(Consulta rs) {  
    	SimpleHash sh = new SimpleHash();
    	if(rs != null) {  
    		sh = rs.getSimpleHash(); 
    		rs.close(); 
    	}
    	return sh;
    }

	private Connection con;
	private Consulta consul;
	private String mensajeError;
}