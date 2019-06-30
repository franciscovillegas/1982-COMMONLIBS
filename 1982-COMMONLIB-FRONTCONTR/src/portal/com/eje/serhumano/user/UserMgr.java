package portal.com.eje.serhumano.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import cl.ejedigital.ExceptionNotImplemented;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;

import organica.tools.Validar;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.datos.Consulta;
import portal.com.eje.permiso.PerfilPortal;
import portal.com.eje.tools.OutMessage;

public class UserMgr {

    public UserMgr(Connection conexion) {
        conex = conexion;
    }

    public boolean setCantIngresos(Rut rut, int ingresos) {
        String sql = "UPDATE eje_ges_usuario  SET  cant_ingresos = ?  WHERE (rut_usuario = ?)";
        boolean ok = false;
        mensajeError = "";
        try {
            PreparedStatement pstmt = conex.prepareStatement(sql);
            pstmt.setInt(1, ingresos);
            pstmt.setString(2, rut.getRutId());
            pstmt.executeUpdate();
            ok = true;
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }
    
    public boolean addAccesoGestion(Connection conn, String rut) {
        boolean si = false;
        Validar vld = new Validar();
        ConsultaData consulta = this.getUnidadEncargadoAnterior(rut);
        consulta.next();
        String unidad = vld.validarDato(consulta.getString("unidad"));
	    this.borrarEncargadoAnterior(unidad,2);    
        String query2 = "SELECT MAX(id_perfil_usuario) as id_perfil_usuario FROM eje_generico_perfil_usuario";
        Consulta consulta2 = new Consulta(conex); 
        consulta2.exec(query2);
        consulta2.next();
        
        Integer correlativo = Integer.valueOf( vld.validarDato(consulta2.getString("id_perfil_usuario"),"0")) + 1;
        consulta2.close();
        
        if(conn != null) {
            Consulta consul = new Consulta(conex);
            String sql = "insert into eje_generico_perfil_usuario (rut, id_perfil,id_perfil_usuario) values " +
            			 "(".concat(rut).concat(",".concat(String.valueOf(PerfilPortal.JefeUnidad))).concat(",".concat(String.valueOf(correlativo)).concat(")"));
            si = consul.insert(sql);
            consul.close();
        }
	    
        return si;
    }
    
    private ConsultaData getUnidadEncargadoAnterior( String rut)
    {
      
        String sql = "SELECT unidad FROM eje_ges_trabajador WHERE rut = ?";
        Object[] params = {rut};
        ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData(conex, sql, params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        
        return data;
    }
    
    public ConsultaData getEmpresaPadre()
    {
      
        String sql = "SELECT empresa FROM eje_ges_uni_sup";
        Object[] params = {};
        ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData(conex, sql, params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        
        return data;
    }
    
    public void insertUsuarioUnidad(String rut, String unidad){
		this.borrarEncargadoAnterior(unidad,1);
		ConsultaData consulta = this.getEmpresaPadre();
        consulta.next();
        String empresa = consulta.getString("empresa");
        StringBuilder sql = new StringBuilder();
        sql.append("insert into eje_ges_usuario_unidad (rut_usuario, empresa, unidad, tipo) ");
        sql.append("values(?,?,?,'R')");
		Object[] params =
			{rut, empresa, unidad};

		boolean data = false;
		try {
			data = ConsultaTool.getInstance().insert("portal",sql.toString(),params);

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public boolean borrarTrabajadorUnidad(String rut){
 	   StringBuilder sql = new StringBuilder();
 	   sql.append("delete from eje_ges_trabajador_unidad where rut=? and tipo_jerarquia is null");
 	   boolean data = false;
 	   Object[] params = {rut};
 	   try {
 		   data = ConsultaTool.getInstance().insert(conex,sql.toString(),params);
 	   }
 	   catch (SQLException e) {
 		   e.printStackTrace();
 	   }	
 	   return data;
    }

    public void insertTrabajadorUnidad(String rut, String unidad){
  	   	StringBuilder sql = new StringBuilder();
        sql.append("insert eje_ges_trabajador_unidad ");
        sql.append("select rut,wp_cod_empresa,wp_cod_planta,unidad=?,tipo_jerarquia=NULL from eje_ges_trabajador where rut=?");
        Object[] params = {unidad, rut};
        boolean data = false;
		try {
			data = ConsultaTool.getInstance().insert("portal",sql.toString(),params);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

    
    public void insertUsuarioEncargado(String rut, String unidad, String periodo, String rutCambios, String estado, String accOrg){
    	
    	ConsultaData consulta = ManagerTrabajador.getInstance().getTrabajador(rut);
    	String empresa = null;
    	if(consulta != null && consulta.next()) {
    		empresa = consulta.getString("empresa");
    	} else {
    		consulta = this.getEmpresaPadre();
    		if(consulta.next()) {
    			empresa = consulta.getString("empresa");
    		}
    	}
    		
        consulta.next();
        
		this.borrarEncargadoAnterior(unidad,1);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into eje_ges_unidad_encargado ");
        sql.append("(unid_empresa,unid_id,periodo,rut_encargado,mision,");
        sql.append("estado,fec_actualiza,rut_cambios,fec_ini,acc_org) ");
        sql.append("values(?, ?, ?, ?, NULL, ?, getdate(), ?, NULL , ?)");
		Object[] params =
			{empresa, unidad, periodo, rut, estado, rutCambios, accOrg};

		boolean data = false;
		try {
			data = ConsultaTool.getInstance().insert("portal",sql.toString(),params);

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public boolean borrarEncargadoAnterior (String unidad, int num){
	   StringBuilder sql = new StringBuilder();
	   if(num == 1){
		   sql.append("delete  from eje_ges_usuario_unidad where unidad = ?");
	   }
	   else{
		   if(num == 2){
				sql.append("delete from eje_generico_perfil_usuario where id_perfil = -1 and  rut in ");
				sql.append("(select t.rut from eje_ges_trabajador as t inner join eje_generico_perfil_usuario  as p ");
				sql.append("ON t.rut=p.rut where t.unidad = ? and p.id_perfil = -1)");
		   }
		   else{
			
		   }
	   }
	   
		Object[] params =
			{unidad};

		boolean data = false;
		try {
			data = ConsultaTool.getInstance().insert(conex,sql.toString(),params);

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
   }
    
    public boolean borrarEncargadoAnteriorPorRut (String rut){
 	   String sql ="delete  from eje_ges_usuario_unidad where rut_usuario = ?";  
 		Object[] params =
 			{rut};
 		boolean data = false;
 		try {
 			data = ConsultaTool.getInstance().insert(conex,sql,params);

 		}
 		catch (SQLException e) {
 			e.printStackTrace();
 		}

 		return data;
    }
   
   public boolean actualizarEstadoEncargadoUnidad(String unidad) {
		
		String sql = "UPDATE eje_ges_unidad_encargado SET estado = 0 WHERE unid_id = ?";
		
			
		Object[] params = {unidad};

		boolean data = false;
		try {
			data = ConsultaTool.getInstance().insert(conex,sql,params);

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
     

    public boolean updateUser(String rut, String pass) {
        String sql = "UPDATE eje_ges_usuario  SET  password_usuario = '" + pass.trim() + "', md5 = 'S'  WHERE (rut_usuario = " + rut + ")";
        boolean ok = false;
        mensajeError = "";
        OutMessage.OutMessagePrint("\n\n :" + sql);
        try {
            PreparedStatement pstmt = conex.prepareStatement(sql);
            pstmt.executeUpdate();
            ok = true;
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean updateUserWinper(String rut, String pass) {
        consul = new Consulta(conex);
        boolean ok = false;
        String sql = "UPDATE UserGlobal SET clave = '" + pass.trim() + "' WHERE (rut_trabajador = " + rut + ")";
        if(consul.insert(sql)) {
        	ok = true;
        }
        return ok;
    }

    public Consulta selectUserWinper(String rut, String pass) {
        consul = new Consulta(conex);
        String sql = "SELECT codigouser FROM UserGlobal  WHERE (rut_trabajador = " + rut + ") AND (codigouser <> '') AND (codigouser is not null )";
        OutMessage.OutMessagePrint("\n" + sql);
        consul.exec(sql);
        return consul;
    }

    public boolean updateUserWinper2(String codigo, String pass) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "UPDATE USUARIOS SET PWD = '" + pass + "' WHERE ( CODIGO = '" + codigo + "')";
            OutMessage.OutMessagePrint("\n" + sql);
            if(consul.insert(sql)) {
            	ok = true;
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public String getError() {
        return mensajeError;
    }

    public boolean primerIngresoPortal(String rut) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "select count(*) existe from eje_ges_usuario where login_usuario=" + rut +
                         "and cant_ingresos=1 and passw_ult_cambio is null";
            consul.exec(sql); consul.next();
            if(consul.getString("existe").equals("1")) {
            	ok = true;
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean tieneClaveCaducada(String rut, int cota) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "SELECT isnull(DATEDIFF(day, passw_ult_cambio, getdate()),'" + cota + "') as dias FROM " +
                         "eje_ges_usuario where login_usuario=" + rut;
            consul.exec(sql); consul.next();
            if( Integer.parseInt(consul.getString("dias")) >= cota) {
            	ok = true;
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean resetAccesoFallidos(String rut) {
    	boolean ok = false;
        try {
        	consul = new Consulta(conex);
        	String sql = "delete from eje_ges_bloqueo_acceso_usuario where rut=" + rut + " and bloqueado='N'";
        	if( consul.insert(sql) ) {
        		ok = true;
        	}        		
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean resetBloqueos(String rut) {
    	boolean ok = false;
        try {
        	consul = new Consulta(conex);
       
        	String sql = "select tipo_bloqueo from eje_ges_bloqueo_acceso_usuario where rut=" + rut + 
        	             " and bloqueado='S'";
        	consul.exec(sql);
        	if(consul.next()) {
        		ResourceBundle proper = ResourceBundle.getBundle("db");
        		if(consul.getString("tipo_bloqueo").equals(proper.getString("clave.mensajebloqueoEmail2"))) {
        			String sql2 = "UPDATE eje_ges_usuario_ult_acceso set fecha_ult_acceso=getdate() " +
        			              "FROM (SELECT TOP 1 * FROM eje_ges_usuario_ult_acceso where rut=" + rut +
        			              " ORDER BY fecha_ult_acceso desc) AS t1 WHERE " +
        			              "(eje_ges_usuario_ult_acceso.rut = t1.rut) and " +
        			              "(eje_ges_usuario_ult_acceso.fecha_ult_acceso = t1.fecha_ult_acceso)";
        			consul.insert(sql2);
        		}
        	}
        	sql = "delete from eje_ges_bloqueo_acceso_usuario where rut=" + rut + " and bloqueado='S'";
        	if( consul.insert(sql) ) {
        		ok = true;
        	}        		
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean resetAccesoFallidosTiempo(String rut) {
    	boolean ok = false;
        try {
        	ResourceBundle proper = ResourceBundle.getBundle("db");
            int cotaTiempoResetFallos = Integer.parseInt(proper.getString("clave.tiemporeseteofallos"));

        	if(siResetearAccesoFallidosTiempo(rut,cotaTiempoResetFallos)) {
        		consul = new Consulta(conex);
                String sql = "delete from eje_ges_bloqueo_acceso_usuario where rut=" + rut + " and bloqueado='N'";
                if( consul.insert(sql) ) {
                	ok = true;
                }        		
        	}
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }
    
    public boolean siResetearAccesoFallidosTiempo(String rut, int cota) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "SELECT isnull(DATEDIFF(hour, fecha_ultimo_fallo, getdate()),'" + cota + "') as horas FROM " +
                         "eje_ges_bloqueo_acceso_usuario where rut=" + rut + " and bloqueado='N'";
            consul.exec(sql); 
            if(consul.next()) {
	            if( Integer.parseInt(consul.getString("horas")) >= cota) {
	            	ok = true;
	            }
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    
    public boolean tieneAccesosFallidos(String rut) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "SELECT count(rut) existe from eje_ges_bloqueo_acceso_usuario where rut=" + rut;
            consul.exec(sql); 
            if(consul.next()) {
	            if( !consul.getString("existe").equals("0") ) {
	            	ok = true;
	            }
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean registraAccesoFallido(String rut, boolean fallorepetitivo, String tipo) {
    	boolean ok = false;
        try {
            consul = new Consulta(conex);
            if(fallorepetitivo) {
            	
            	ResourceBundle proper = ResourceBundle.getBundle("db");
                int cotaAccesos = Integer.parseInt(proper.getString("clave.intentosacceso"));
            	
            	String sql = "SELECT intentos_fallidos,bloqueado from eje_ges_bloqueo_acceso_usuario where rut=" + rut;
                consul.exec(sql); consul.next();
                int accesosActuales = Integer.parseInt(consul.getString("intentos_fallidos"))+1;
                if( accesosActuales == cotaAccesos ) {
                	String sql2 = "update eje_ges_bloqueo_acceso_usuario set " +
                	              "intentos_fallidos=" + accesosActuales + ",bloqueado='S'," +
                	              "fecha_bloqueo=getdate(),fecha_ultimo_fallo=getdate(),tipo_bloqueo='" + tipo + "' where rut=" + rut;
                	if(consul.insert(sql2)) {
                		ok = true;
                	}
                }
                else if( accesosActuales < cotaAccesos ) {
                	String sql2 = "update eje_ges_bloqueo_acceso_usuario set " +
                				  "intentos_fallidos=" + accesosActuales + ",fecha_ultimo_fallo=getdate() where rut=" + rut;
                	consul.insert(sql2);
                } 
            }
            else {
            	String sql = "insert eje_ges_bloqueo_acceso_usuario values(" + rut + ",1,'N',NULL,getdate(),NULL)";
                consul.insert(sql);
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean userBloqueado(String rut) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "SELECT count(rut) existe from eje_ges_bloqueo_acceso_usuario " +
                         "where rut=" + rut + " and bloqueado='S'";
            consul.exec(sql); consul.next();
            if( !consul.getString("existe").equals("0") ) {
            	ok = true;
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public SimpleList getListaUsuariosBloqueados() {
        consul = new Consulta(conex);
        String sql = String.valueOf((new StringBuilder("select b.rut,t.digito_ver,t.nombre,b.intentos_fallidos,").
        		append("convert(varchar(10),b.fecha_bloqueo,103) fecha_bloqueo,b.tipo_bloqueo from eje_ges_bloqueo_acceso_usuario b inner join ").
        		append("eje_ges_trabajador t on b.rut=t.rut where b.bloqueado='S'")));
        consul.exec(sql);
        SimpleList UsuarioList = new SimpleList();
        SimpleHash UsuarioHash;
        for(; consul.next(); UsuarioList.add(UsuarioHash)) {
            UsuarioHash = new SimpleHash();
            UsuarioHash.put("rut", consul.getString("rut"));
            UsuarioHash.put("dv", consul.getString("digito_ver"));
            UsuarioHash.put("nombre", consul.getString("nombre"));
            UsuarioHash.put("intentos", consul.getString("intentos_fallidos"));
            UsuarioHash.put("bloqueo", consul.getString("fecha_bloqueo"));
            UsuarioHash.put("tipo", consul.getString("tipo_bloqueo"));
        }
        return UsuarioList;
    }

    public boolean registraBloqueoNoUso(String rut, String tipo) {
    	boolean ok = false;
        try {
            consul = new Consulta(conex);
           	ResourceBundle proper = ResourceBundle.getBundle("db");
            String cotaAccesos = proper.getString("clave.intentosacceso");
            String sql = "insert eje_ges_bloqueo_acceso_usuario values(" + rut + "," + cotaAccesos + ",'S',getdate(),getdate(),'" + tipo + "')";
            if(consul.insert(sql)) {
            	ok = true;
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean tieneNoUsada(String rut, int cota) {
        boolean ok = false;
        try {
            consul = new Consulta(conex);
            String sql = "select top 1 isnull(DATEDIFF(day, fecha_ult_acceso, getdate()),'0') as dias " +
                         "from eje_ges_usuario_ult_acceso where rut='" + rut + "' order by fecha_ult_acceso desc";
            consul.exec(sql); 
            if(consul.next()) {
	            if( Integer.parseInt(consul.getString("dias")) >= cota) {
	            	ok = true;
	            }
            }
        }
        catch(Exception e) {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Consulta --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }
    
    public void close() {
    	if(consul != null ) {
    		try {
    			consul.close();
    		} catch (Exception e) {
    			
    		}
    	
    	}
    }

    private Consulta consul;
    private Connection conex;
    private String mensajeError;
}