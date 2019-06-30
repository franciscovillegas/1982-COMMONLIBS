package portal.com.eje.serhumano.efemerides;

import java.sql.Connection;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.output.Escape;
import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class Efemerides_Manager {

    public Efemerides_Manager(Connection conex) {
        con = conex;
        mensajeError = "";
    }

    public String getError() {
        return mensajeError;
    }

    public Consulta GetCodDiaCumple(int dia, int mes, int ano) {
    	consul		= new Consulta(con);
    	String sql	= null;
    	String sql2	= null;
    	String cod	= "";
    	
    	sql = "SELECT cod FROM eje_ges_calendario WHERE dia ="+dia+" and codmes = "+mes+" and codano = "+ano;
    	consul.exec(sql);
    	
    	if (consul.next()) {
    		cod		= String.valueOf(Integer.valueOf(consul.getString("cod"))-1);
    	}
    	
    	sql2 = "SELECT cod,dia,codmes,codano,festivo FROM eje_ges_calendario WHERE cod = " + cod;
        
    	OutMessage.OutMessagePrint("Cod: " + sql2);
    	consul.exec(sql2);
    	return consul;
    }
    
    public Consulta GetDiaCumpleFestivo(int cod) {
    	consul		= new Consulta(con);
    	String sql	= null;
    	
    	sql = "SELECT cod,dia,codmes,codano,festivo FROM eje_ges_calendario WHERE cod = "+cod;
        
    	OutMessage.OutMessagePrint("Cod: " + sql);
    	consul.exec(sql);
    	return consul;
    }
    
    public Consulta GetUsuariosCumple(String qry) {
    	consul = new Consulta(con);
    	ResourceBundle proper;
    	proper = ResourceBundle.getBundle("DataFolder");
    	String sql=null;
    	String ua = proper.getString("portal.unidad_administrativa");
        String up = proper.getString("portal.unidad_pertenencia");
        if(ua.equals("0"))
        	if(up.equals("0"))
            	sql = " SELECT distinct trabajador.nombre, trabajador.rut, trabajador.fecha_nacim AS fecha, DAY(trabajador.fecha_nacim) AS dia," +
            		  " MONTH(trabajador.fecha_nacim) AS mes,  unidad.unid_desc AS unidad, trabajador.anexo, trabajador.rut AS Expr1," +
            		  " trabajador.e_mail FROM eje_ges_trabajador trabajador INNER JOIN eje_ges_unidades unidad ON trabajador.unidad = unidad.unid_id " +
            		  qry +
            		  " ORDER BY trabajador.dia,trabajador.mes,trabajador.nombre";
        	else
        	    sql = " SELECT distinct trabajador.nombre, trabajador.rut, trabajador.fecha_nacim AS fecha, DAY(trabajador.fecha_nacim) AS dia," +
        	    	  " MONTH(trabajador.fecha_nacim) AS mes,  unidad.unid_desc AS unidad, trabajador.anexo, trabajador.rut AS Expr1," +
        	    	  " trabajador.e_mail FROM eje_ges_trabajador trabajador INNER JOIN eje_sh_unidades unidad ON trabajador.unidad = unidad.unid_id and trabajador.wp_cod_empresa=unidad.unid_empresa " +
        	    	  qry +
        	    	  " ORDER BY trabajador.mes,trabajador.dia,trabajador.nombre";
        
    	OutMessage.OutMessagePrint("Cumple: " + sql);
    	consul.exec(sql);
    	return consul;
    }
    
    public Consulta GetCumple() {
    	consul = new Consulta(con);
    	ResourceBundle proper;
    	proper = ResourceBundle.getBundle("DataFolder");
    	String sql=null;
    	String ua = proper.getString("portal.unidad_administrativa");
        String up = proper.getString("portal.unidad_pertenencia");
        if(ua.equals("0"))
        	if(up.equals("0"))
            	sql = "SELECT distinct trabajador.nombre, trabajador.rut, trabajador.fecha_nacim AS fecha, DAY(trabajador.fecha_nacim) AS dia,MONTH(trabajador.fecha_nacim) AS mes,  unidad.unid_desc AS unidad, trabajador.anexo, trabajador.rut AS Expr1,trabajador.e_mail FROM eje_ges_trabajador trabajador INNER JOIN eje_ges_unidades unidad ON trabajador.unidad = unidad.unid_id ORDER BY trabajador.dia";
        	else
        	    sql = "SELECT distinct trabajador.nombre, trabajador.rut, trabajador.fecha_nacim AS fecha, DAY(trabajador.fecha_nacim) AS dia,MONTH(trabajador.fecha_nacim) AS mes,  unidad.unid_desc AS unidad, trabajador.anexo, trabajador.rut AS Expr1,trabajador.e_mail FROM eje_ges_trabajador trabajador INNER JOIN eje_sh_unidades unidad ON trabajador.unidad = unidad.unid_id ORDER BY trabajador.dia";
        
    	OutMessage.OutMessagePrint("Cumple: " + sql);
    	consul.exec(sql);
    	return consul;
    }

    public Consulta GetDeSanto(String santo) {
        consul = new Consulta(con);
        String sql = "SELECT trab.rut, trab.e_mail,unidades.unid_desc AS unidad,anexos.anexo, trab.nombre FROM eje_ges_trabajador trab LEFT OUTER JOIN eje_ges_trabajadores_anexos anexos ON trab.rut = anexos.rut AND trab.empresa = anexos.empresa INNER JOIN eje_sh_unidades unidades ON trab.empresa = unidades.unid_empresa AND trab.unidad = unidades.unid_id WHERE (trab.nombre LIKE '%" + santo.toLowerCase() + "%') OR (trab.nombre LIKE '%" + santo.toUpperCase() + "%')";
        OutMessage.OutMessagePrint("Empleados de Santo: " + sql);
        consul.exec(sql);
        return consul;
    }

	public Consulta GetDeSantos(String santo) {
		
		ResourceBundle proper;
    	String sql=null;
    	
    	if(santo != null) {
    		consul = new Consulta(con);
    		
    		String[] santos = santo.split(",");
    		 
    		StringBuilder or = new StringBuilder();
    		for(String s: santos) {
    			if(or.length() > 0) {
    				or.append(" or ");	
    			}
    			
    			or.append(" (trab.nombres LIKE '%" + StringEscapeUtils.escapeSql(s.trim()) + " %') ");
    		}
    	
	    	sql = " SELECT trab.rut, trab.e_mail, trab.mail,isnull(unidades.unid_desc,'no asociado') AS unidad,trab.anexo,trab.nombre, trab.nombres ";
	    	sql+= " FROM eje_ges_trabajador trab ";
	    	sql+= " 	LEFT OUTER JOIN eje_ges_unidades unidades ON trab.unidad = unidades.unid_id ";
	    	sql+= " WHERE "+or.toString();
	
			consul.exec(sql);
			return consul;
		
    	}
    	
    	return null;
	}

	public Consulta GetSantos(int dia, int mes) {
		consul = new Consulta(con);
		String sql = "SELECT santo FROM eje_ges_santo WHERE (mes = " + mes + ") ";
		if (dia != 0)
			sql = sql + " AND (dia=" + dia + ")";
		OutMessage.OutMessagePrint("Santo: " + sql);
		consul.exec(sql);
		return consul;
	}

    public Consulta getNacimientos(int mes, int anio) {
        consul = new Consulta(con);
        String sql = "SELECT  gf.rut, gf.nombre, convert(varchar,gf.fecha_nacim,103) as fecha_nacim, tr.nombre AS empleado, tr.e_mail,  gf.rut_carga FROM dbo.eje_ges_grupo_familiar gf INNER JOIN  dbo.eje_ges_trabajador tr ON gf.rut = tr.rut WHERE (MONTH(gf.fecha_nacim) = " + mes + ") AND (YEAR(gf.fecha_nacim) = " + anio + ") and  (gf.parentesco = 'HIJO')";
        OutMessage.OutMessagePrint("Nacimientos: " + sql);
        consul.exec(sql);
        return consul;
    }

    public Consulta getNacimientos(String rut, String rutcarga) {
        consul = new Consulta(con);
        String sql = "SELECT  gf.rut, gf.nombre, convert(varchar,gf.fecha_nacim,103) as fecha_nacim, tr.nombre AS empleado, tr.e_mail,  gf.rut_carga FROM dbo.eje_ges_grupo_familiar gf INNER JOIN  dbo.eje_ges_trabajador tr ON gf.rut = tr.rut where gf.rut_carga='" + rutcarga + "' and gf.rut='" + rut + "'";
        OutMessage.OutMessagePrint("Nacimientos: " + sql);
        consul.exec(sql);
        return consul;
    }

    public Consulta getInfoFotoCarga(Connection condf, String rutcarga) {
        consul = new Consulta(condf);
        String sql = "SELECT rut,foto  FROM  eje_ges_fotos_carga WHERE  (eje_ges_fotos_carga.rut = " + rutcarga + ") ";
        OutMessage.OutMessagePrint("InfoRutCarga: " + sql);
        consul.exec(sql);
        return consul;
    }

    public Consulta getDefunciones(int mes, int anio) {
        consul = new Consulta(con);
        String sql = "SELECT   df.rut, tr.nombre AS emple, df.nombre, df.detalle,convert(varchar, df.fecha,103) as fec FROM  dbo.eje_ges_defunciones df INNER JOIN   dbo.eje_ges_trabajador tr ON df.rut = tr.rut WHERE  (MONTH(df.fecha) = " + mes + ") AND (YEAR(df.fecha) = " + anio + ")";
        OutMessage.OutMessagePrint("InfoRutCarga: " + sql);
        consul.exec(sql);
        return consul;
    }

    public Consulta getMatrimonios(int mes, int anio) {
        consul = new Consulta(con);
        String sql = "SELECT  tr.rut, tr.nombre AS emple, mt.nombre, mt.detalle, mt.foto, mt.rut_conyugue,convert(varchar, mt.fecha,103) as fec FROM   dbo.eje_ges_trabajador tr INNER JOIN  dbo.eje_ges_matrimonios mt ON tr.rut = mt.rut WHERE  (YEAR(mt.fecha) = " + anio + ") AND (MONTH(mt.fecha) = " + mes + ")";
        OutMessage.OutMessagePrint("InfoRutCarga: " + sql);
        consul.exec(sql);
        return consul;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}