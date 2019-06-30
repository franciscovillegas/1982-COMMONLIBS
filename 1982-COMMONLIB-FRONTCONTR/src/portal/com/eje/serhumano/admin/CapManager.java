package portal.com.eje.serhumano.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.web.datos.ConsultaTool;

import organica.tools.Validar;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class CapManager
{

    public CapManager(Connection conex)
    {
        con = conex;
    }

    public String getError()
    {
        return mensajeError;
    }

    public Consulta getConsulta(String sql)
    {
        Consulta consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta getRoles(String app_id)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rol_id, rol_glosa  FROM   eje_ges_roles  WHERE  (app_id = '")).append(app_id).append("')")));
        OutMessage.OutMessagePrint("getRoles :  ".concat(String.valueOf(String.valueOf(sql))));
        return getConsulta(sql);
    }

    public Consulta getDatosUser(String rut)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT password_usuario, passw_ult_cambio, md5 FROM   eje_ges_usuario WHERE  (rut_usuario = ")).append(rut).append(") ")));
        OutMessage.OutMessagePrint("getDatosUser :  ".concat(String.valueOf(String.valueOf(sql))));
        return getConsulta(sql);
    }

    public Consulta getDatosUserPass(String rut)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT password_usuario, passw_ult_cambio, md5 FROM   eje_ges_usuario WHERE  (rut_usuario = ")).append(rut).append(") ")));
        Consulta consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta getRolUser(String rut, String app)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  rol_id, vigente FROM    view_eje_ges_user WHERE  (rut_usuario = ")).append(rut).append(") and (app_id = '").append(app).append("') ")));
        OutMessage.OutMessagePrint("getRolUser :  ".concat(String.valueOf(String.valueOf(sql))));
        return getConsulta(sql);
    }

    public String getDigRut(String rut, Connection df)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  digito_ver FROM    eje_ges_trabajador WHERE   (rut =  ")).append(rut).append(") ")));
        OutMessage.OutMessagePrint("getDigRut :  ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul = new Consulta(df);
        consul.exec(sql);
        String dig = "";
        if(consul.next())
            dig = consul.getString("digito_ver");
        return dig;
    }

    public String getNombregRut(String rut, Connection df)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  nombre FROM    eje_ges_trabajador WHERE   (rut =  ")).append(rut).append(") ")));
        OutMessage.OutMessagePrint("getNombregRut :  ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul = new Consulta(df);
        consul.exec(sql);
        String dig = "";
        if(consul.next())
            dig = consul.getString("nombre");
        return dig;
    }

    /**
     * retorna la descripción del cargo para la persona
     * 
     * @author Pancho
     * @since 2017-07-25
     * */
    public String getCargoRut(String rut, Connection df, String empresa) {
        String cargo = null;
        ConsultaData data = getCargoRutConsultaData(rut, df, empresa);
		
        if(data != null && data.next())
            cargo = data.getString("nombrecargo");
		 
        return cargo;
    }
    
    /**
     * retorna el id del cargo para la persona
     * 
     * @author Pancho
     * @since 2017-07-25
     * */
    public String getCargoIdRut(String rut, Connection df, String empresa) {
        String cargo = null;
        ConsultaData data = getCargoRutConsultaData(rut, df, empresa);
		
        if(data != null && data.next())
            cargo = data.getString("cargo");
		 
        return cargo;
    }
    
    public ConsultaData getCargoRutConsultaData(String rut, Connection df, String empresa)
    {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select Distinct cargo=ltrim(rtrim(a.cargo)), nombrecargo=ltrim(rtrim(b.descrip)) ");
    	sql.append(" from eje_ges_trabajador a ");
    	sql.append(" 	INNER JOIN eje_ges_cargos b  ON a.cargo=b.cargo and a.empresa = b.empresa ");
    	sql.append(" WHERE (  a.rut = '").append(rut).append("')");
        OutMessage.OutMessagePrint("getCargoRut :  ".concat(String.valueOf(String.valueOf(sql))));
        
        ConsultaData data = null;
        try {
			data = ConsultaTool.getInstance().getData("portal",sql.toString());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

        return data;
    }

    public String getUnidadRut(String rut, Connection df, String empresa)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("select Distinct a.unidad, b.unid_desc AS nombreunidad from eje_ges_trabajador a INNER JOIN eje_ges_unidades b ON a.unidad=b.unid_id  WHERE (  a.rut = ")).append(rut).append(")")));
        OutMessage.OutMessagePrint("getUnidadRut :  ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul = new Consulta(df);
        consul.exec(sql);
        String unidad = "";
        if(consul.next())
            unidad = consul.getString("nombreunidad");
        return unidad;
    }

    public String getCodUnidadRut(String rut, Connection df, String empresa)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("select Distinct a.unidad from eje_ges_trabajador a WHERE (  a.rut = ")).append(rut).append(")")));
        OutMessage.OutMessagePrint("getUnidadRut :  ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul = new Consulta(df);
        consul.exec(sql);
        String unidad = "";
        if(consul.next())
            unidad = consul.getString("unidad");
        return unidad;
    }
  
    
    /**
     * Se modificó el retorno del método desde Consulta a ConsultaData.
     * El objeto Consulta careía de cierre de conexiones.
     * 
     * @since 19 Marzo 2014
     * 
     * */
    public ConsultaData getApp(String rut) {
    	
    	PropertiesTools tool = new PropertiesTools();
    	StringBuilder sql = new StringBuilder("");
    	String rutRoot = "";
    	
    	if(tool.existsBundle("portal")) {
    		rutRoot = tool.getString(ResourceBundle.getBundle("portal"),"portal.root.rut","");
    	}
    	
    	if(rutRoot != null && rutRoot.equals(rut)) {
    		sql.append("SELECT orden, app_id FROM eje_ges_aplicacion");
    	}
    	else {
	    	sql.append(" SELECT pa.id_corr, app_id \n");
	    	sql.append(" FROM eje_generico_perfil_usuario pu \n");
	    	sql.append(" 		INNER JOIN eje_generico_perfil_app pa ON pa.id_perfil = pu.id_perfil \n");
	    	sql.append(" WHERE  pu.rut = '").append(rut).append("' \n");
	    	sql.append(" union \n");
	    	sql.append(" SELECT  a.orden + 100, ua.app_id \n");
	    	sql.append(" FROM   dbo.eje_ges_user_app ua \n");
	    	sql.append(" 		INNER JOIN dbo.eje_ges_aplicacion a  ON ua.app_id = a.app_id  \n");
	    	sql.append(" WHERE  ua.rut_usuario = '").append(rut).append("' \n");
    	}
        OutMessage.OutMessagePrint("getApp :  ".concat(String.valueOf(String.valueOf(sql))));
        
        try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString());
			
			if(data != null) {
				System.out.println("	Privilegios:"+data.size());
			}
			else {
				System.out.println("	Privilegios:0");
			}
			
			return data;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }

    public boolean InsertNuevoFactor(String nombre, String pInicial, String pFinal, String nivel, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_ggd_factor_excelencia  (factor_desc, factor_porinicial, factor_porfinal, factor_nivel, factor_glosa)   VALUES   (?,?,?,?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, pInicial);
            pstmt.setString(3, pFinal);
            pstmt.setString(4, nivel);
            pstmt.setString(5, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }
    
    public boolean InsertNuevaDimension(String nombre, String factor, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_perf_dimensiones  (descripcion, factor, glosa, vigente)   VALUES   (?,?,?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, factor);
            pstmt.setString(3, glosa);
            pstmt.setString(4, "s");
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean InsertNuevaAreaNegocio(String nombre, String empresa, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_perf_areanegocio (descrip, empresa, glosa)   VALUES   (?,?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, empresa);
            pstmt.setString(3, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean InsertNuevaClase(String nombre, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_perf_catfuncion (desccatfun, glosa)   VALUES   (?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean InsertNuevoEstamento(String nombre, String glosa, String empresa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_perf_estamento (descrip, glosa, empresa)   VALUES   (?,?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, glosa);
            pstmt.setString(3, empresa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }
    

    public boolean InsertNuevoTipoCompetencia(String descripcion, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_ggd_tipo_competencia  (descripcion, glosa)   VALUES   (?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, descripcion);
            pstmt.setString(2, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateFactorExcelencia(String factor, String nombre, String pInicial, String pFinal, String nivel, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_ggd_factor_excelencia  SET factor_desc=?,factor_porinicial=?,factor_porfinal=?,factor_nivel=?,factor_glosa=?  where factor_id=".concat(String.valueOf(String.valueOf(factor)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, pInicial);
            pstmt.setString(3, pFinal);
            pstmt.setString(4, nivel);
            pstmt.setString(5, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateDimension(String dimension, String factor, String nombre, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_perf_dimensiones  SET descripcion=?,factor=?,glosa=? where id_dimension=".concat(String.valueOf(String.valueOf(dimension)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, factor);
            pstmt.setString(3, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateAreaNegocio(String areaNegocio, String empresa, String nombre, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_perf_areanegocio SET descrip=?,empresa=?,glosa=? where id_areanegocio=".concat(String.valueOf(String.valueOf(areaNegocio)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, empresa);
            pstmt.setString(3, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateClase(String clase, String nombre, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_perf_catfuncion SET desccatfun=?,glosa=? where idcatfun=".concat(String.valueOf(String.valueOf(clase)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateEstamento(String estamento, String nombre, String glosa, String empresa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_perf_estamento SET descrip=?,glosa=?,empresa=? where id_estamento=".concat(String.valueOf(String.valueOf(estamento)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, glosa);
            pstmt.setString(3, empresa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }
    
    public boolean UpdateTipoCompetencia(String id_tipo, String descripcion, String glosa)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE eje_ggd_tipo_competencia  SET descripcion=?,glosa=? where id_tipo=".concat(String.valueOf(String.valueOf(id_tipo)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, descripcion);
            pstmt.setString(2, glosa);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }
    
    public boolean AddUser(String login, int rut, String pass)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_ges_usuario  (login_usuario, password_usuario, rut_usuario,passw_ult_cambio)   VALUES   (?,?,?,getdate())";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, pass);
            pstmt.setInt(3, rut);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUser(String rut, String pass)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE    eje_ges_usuario  SET password_usuario =?,passw_ult_cambio=getdate()  where rut_usuario=".concat(String.valueOf(String.valueOf(rut)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pass);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateTrabajador(String rut, String empresa, String unidad)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_trabajador SET   unidad = '")).append(unidad).append("' ").append("WHERE  (rut = '").append(rut).append("') ")));
        OutMessage.OutMessagePrint("Update Trabajaror --> ".concat(String.valueOf(String.valueOf(sql))));
        try
        {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            con.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                con.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            con.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        
        return valor;
    }

    public boolean UpdateProcesoEvaluacion(String grabar, String proceso)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ggd_procesos SET   vigente = ")).append(grabar).append(" ").append("WHERE  (id_proceso = '").append(proceso).append("') ")));
        OutMessage.OutMessagePrint("Update Trabajaror --> ".concat(String.valueOf(String.valueOf(sql))));
        try
        {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            con.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                con.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            con.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        
        return valor;
    }

    public boolean UpdateProcesoEvaluacionCargos(String grabar, String proceso)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_perf_procesos SET vigente = ")).append(grabar).append(" ").append("WHERE  (id_proceso = '").append(proceso).append("') ")));
        OutMessage.OutMessagePrint("Update Trabajaror --> ".concat(String.valueOf(String.valueOf(sql))));
        try
        {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            con.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                con.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            con.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        
        return valor;
    }
    
    public boolean DeleteUser(String rut, String pass)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "UPDATE    eje_ges_usuario  SET password_usuario =?  where rut_usuario=".concat(String.valueOf(String.valueOf(rut)));
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pass);
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean AddUserApp(int rut, String app)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_ges_user_app  (app_id, rut_usuario)  VALUES     (?,?) ";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, app);
            pstmt.setInt(2, rut);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteUserAppAll(int rut)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_ges_user_app  WHERE  (rut_usuario = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, rut);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteFactorExcelencia(String factor)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_ggd_factor_excelencia  WHERE  (factor_id = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, factor);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteDimension(String dimension)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_perf_dimensiones  WHERE  (id_dimension = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dimension);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteAreaNegocio(String areaNegocio)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_perf_areanegocio WHERE  (id_areanegocio = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, areaNegocio);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteClase(String clase)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_perf_catfuncion WHERE  (idcatfun = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, clase);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteEstamento(String estamento)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_perf_estamento WHERE  (id_estamento = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, estamento);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteTipoCompetencia(String id_tipo)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_ggd_tipo_competencia  WHERE  (id_tipo = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id_tipo);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteUserApp(int rut, String app)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_ges_user_app  WHERE     (app_id = ?) AND (rut_usuario = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, app);
            pstmt.setInt(2, rut);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean AddRolUserApp(int rut, String rol, String app_id)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO eje_ges_usuario_rol (rol_id, rut_usuario,app_id) Values (?,?,?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rol);
            pstmt.setInt(2, rut);
            pstmt.setString(3, app_id);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateRolUserApp(int rut, int estado, String rol, String app_id)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_user_app SET    vigente = ")).append(estado).append(" ").append("WHERE  (app_id = '").append(app_id).append("') AND (rut_usuario = ").append(rut).append(") ")));
        String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_rol   (rut_usuario, app_id, rol_id) VALUES     (")).append(rut).append(", '").append(app_id).append("', '").append(rol).append("')")));
        String sql2 = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario_rol SET    rol_id  = '")).append(rol).append("' ").append("WHERE  (app_id = '").append(app_id).append("') AND (rut_usuario = ").append(rut).append(") ")));
        try
        {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            if(!stmt.execute(sql1))
                stmt.execute(sql2);
            con.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                con.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            con.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteRolUserApp(int rut, String app)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "DELETE FROM eje_ges_usuario_rol  WHERE     (app_id = ?) AND (rut_usuario = ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, app);
            pstmt.setInt(2, rut);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean AddUserDataFolder(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO  eje_ges_usuario  (login_usuario, password_usuario, rut_usuario, error,passw_ult_cambio)   VALUES     ('")).append(rut).append("', '").append(consul.getString("password_usuario")).append("', ").append(rut).append(", ").append("0,getdate() )")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean DeleteUserDataFolder(int rut, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM  eje_ges_usuario WHERE  (rut_usuario = ")).append(rut).append(")")));
        try
        {
            conDf.setAutoCommit(false);
            Statement stmt = conDf.createStatement();
            stmt.execute(sql);
            conDf.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                conDf.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            conDf.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUserDatafolder(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE  eje_ges_usuario  SET    login_usuario  = '")).append(rut).append("', password_usuario  = '").append(consul.getString("password_usuario")).append("', error = 0, passw_cambiar = 'S' , passw_ult_cambio = GETDATE() ").append("  WHERE     (rut_usuario  = ").append(rut).append(")")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean AddUserWorkFlow(int rut, String app_id, Connection conWf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_workflow_usuario  (user_rut,  user_clave, user_estado)   VALUES     (")).append(rut).append(", '").append(consul.getString("password_usuario")).append("', ").append(consul.getString("vigente")).append(")")));
                OutMessage.OutMessagePrint("AddUserWorkFlow --> ".concat(String.valueOf(String.valueOf(sql1))));
                conWf.setAutoCommit(false);
                Statement stmt = conWf.createStatement();
                stmt.execute(sql1);
                conWf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conWf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conWf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean DeleteUserWorkFlow(int rut, Connection conWf)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_workflow_usuario WHERE  (user_rut = ")).append(rut).append(")")));
        try
        {
            conWf.setAutoCommit(false);
            Statement stmt = conWf.createStatement();
            stmt.execute(sql);
            conWf.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                conWf.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            conWf.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUserWorkFlow(int rut, String app_id, Connection conWf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_workflow_usuario  SET    user_clave = '")).append(consul.getString("password_usuario")).append("',passw_ult_cambio=getdate() ").append("  WHERE     (user_rut = ").append(rut).append(")")));
                conWf.setAutoCommit(false);
                Statement stmt = conWf.createStatement();
                stmt.execute(sql1);
                conWf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conWf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conWf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean UpdateRolUserWorkFlow(int rut, String app_id, Connection conWf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT rol_id, password_usuario, vigente FROM   view_eje_ges_user WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_workflow_usuario  SET    user_clave = '")).append(consul.getString("password_usuario")).append("', user_rol = ").append(consul.getString("rol_id")).append(", user_estado = ").append(consul.getString("vigente")).append("  WHERE     (user_rut = ").append(rut).append(")")));
                conWf.setAutoCommit(false);
                Statement stmt = conWf.createStatement();
                stmt.execute(sql1);
                conWf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conWf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conWf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean AddUserCert(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_certif  (login_usuario, password_usuario, rut_usuario, error,passw_ult_cambio)   VALUES     ('")).append(rut).append("', '").append(consul.getString("password_usuario")).append("', ").append(rut).append(", ").append("0,GETDATE() )")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean DeleteUserCert(int rut, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_certif WHERE  (rut_usuario = ")).append(rut).append(")")));
        try
        {
            conDf.setAutoCommit(false);
            Statement stmt = conDf.createStatement();
            stmt.execute(sql);
            conDf.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                conDf.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            conDf.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUserCert(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario_certif  SET    login_usuario  = '")).append(rut).append("', password_usuario  = '").append(consul.getString("password_usuario")).append("', error = 0, passw_cambiar = 'S' , passw_ult_cambio = GETDATE() ").append("  WHERE     (rut_usuario  = ").append(rut).append(")")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean AddUserAnexos(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO  eje_ges_usuario_anexos  (login_usuario, password_usuario, rut_usuario, error,passw_ult_cambio)   VALUES     ('")).append(rut).append("', '").append(consul.getString("password_usuario")).append("', ").append(rut).append(", ").append("0, GETDATE() )")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean DeleteUserAnexos(int rut, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM  eje_ges_usuario_anexos WHERE  (rut_usuario = ")).append(rut).append(")")));
        try
        {
            conDf.setAutoCommit(false);
            Statement stmt = conDf.createStatement();
            stmt.execute(sql);
            conDf.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                conDf.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            conDf.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUserAnexos(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE  eje_ges_usuario_anexos  SET    login_usuario  = '")).append(rut).append("', password_usuario  = '").append(consul.getString("password_usuario")).append("', error = 0, passw_cambiar = 'S' , passw_ult_cambio = GETDATE() ").append("  WHERE     (rut_usuario  = ").append(rut).append(")")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean AddUserVigilantes(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO  eje_ges_usuario_vigilantes  (login_usuario, password_usuario, rut_usuario, error,passw_ult_cambio)   VALUES     ('")).append(rut).append("', '").append(consul.getString("password_usuario")).append("', ").append(rut).append(", ").append("0, GETDATE() )")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment y/o User Existe--> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }

    public boolean DeleteUserVigilantes(int rut, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM  eje_ges_usuario_vigilantes WHERE  (rut_usuario = ")).append(rut).append(")")));
        try
        {
            conDf.setAutoCommit(false);
            Statement stmt = conDf.createStatement();
            stmt.execute(sql);
            conDf.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                conDf.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            conDf.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        return valor;
    }

    public boolean UpdateUserVigilantes(int rut, String app_id, Connection conDf)
    {
        boolean valor = false;
        mensajeError = "";
        String query = String.valueOf(String.valueOf((new StringBuilder("SELECT  password_usuario, vigente FROM   view_user_app WHERE  (app_id = '")).append(app_id).append("') AND (rut_usuario = ").append(rut).append(")")));
        Consulta consul = getConsulta(query);
        if(consul.next())
        {
            try
            {
                String sql1 = String.valueOf(String.valueOf((new StringBuilder("UPDATE  eje_ges_usuario_vigilantes  SET    login_usuario  = '")).append(rut).append("', password_usuario  = '").append(consul.getString("password_usuario")).append("', error = 0, passw_cambiar = 'S' , passw_ult_cambio = GETDATE() ").append("  WHERE     (rut_usuario  = ").append(rut).append(")")));
                conDf.setAutoCommit(false);
                Statement stmt = conDf.createStatement();
                stmt.execute(sql1);
                conDf.commit();
                valor = true;
            }
            catch(Exception e)
            {
                mensajeError = e.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                try
                {
                    conDf.rollback();
                }
                catch(SQLException b)
                {
                    mensajeError = b.getMessage();
                    OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
                }
            }
            consul.close();
            try
            {
                conDf.setAutoCommit(true);
            }
            catch(SQLException c)
            {
                mensajeError = c.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
            }
        }
        return valor;
    }
    
    public SimpleList GetListaUsuariosCargo()
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select t.rut,t.digito_ver,t.nombres,t.ape_paterno,t.ape_materno,t.cargo,c.descrip from eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo order by t.nombres,t.ape_paterno"))));
        consul.exec(sql);
        SimpleList UsuarioList = new SimpleList();
        SimpleHash UsuarioHash;
        valida = new Validar();
        for(; consul.next(); UsuarioList.add(UsuarioHash))
        {
            UsuarioHash = new SimpleHash();
            UsuarioHash.put("rut", valida.validarDato(consul.getString("rut")));
            UsuarioHash.put("dv", valida.validarDato(consul.getString("digito_ver")));
            UsuarioHash.put("nombre", valida.validarDato(consul.getString("nombres")));
            UsuarioHash.put("ape_paterno", valida.validarDato(consul.getString("ape_paterno")));
            UsuarioHash.put("ape_materno", valida.validarDato(consul.getString("ape_materno")));
            UsuarioHash.put("ape_cargo", valida.validarDato(consul.getString("cargo")));
            UsuarioHash.put("descripcion", valida.validarDato(consul.getString("descrip")));
        }
        return UsuarioList;
    }

    public SimpleList GetListaUsuarios() {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select distinct cast(t.rut as varchar)+'-'+cast(t.digito_ver as varchar) as rutfull,t.nombre,cast(u.password_usuario as varchar) as clave from eje_ges_trabajador as t inner join eje_ges_usuario as u on t.rut=u.login_usuario order by t.nombre"))));
        consul.exec(sql);
        SimpleList UsuarioList = new SimpleList();
        SimpleHash UsuarioHash;
        valida = new Validar();
        for(; consul.next(); UsuarioList.add(UsuarioHash)) {
            UsuarioHash = new SimpleHash();
            UsuarioHash.put("rut", valida.validarDato(consul.getString("rutfull")));
            UsuarioHash.put("nombre", valida.validarDato(consul.getString("nombre")));
            UsuarioHash.put("clave", valida.validarDato(consul.getString("clave")));
        }
        return UsuarioList;
    }

    public boolean UpdateUnidadTrabajador(String rut, String unidadVieja, String unidadNueva)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = String.valueOf((new StringBuilder("insert into eje_ges_rotacion_unidad(rut,unidorig,uniddest,fechamov) values(").append(rut).append(",'").append(unidadVieja).append("','").append(unidadNueva).append("',getdate())")));
        OutMessage.OutMessagePrint("Update Trabajaror --> ".concat(String.valueOf(String.valueOf(sql))));
        try
        {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            con.commit();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            try
            {
                con.rollback();
            }
            catch(SQLException b)
            {
                mensajeError = b.getMessage();
                OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(b.getMessage()))));
            }
        }
        try
        {
            con.setAutoCommit(true);
        }
        catch(SQLException c)
        {
            mensajeError = c.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(c.getMessage()))));
        }
        
        return valor;
    }

    public String IgualdadUnidadTrabajador(String rut)
    {
    	String valor="-1";
        String sql = String.valueOf((new StringBuilder("select unidad from eje_ges_trabajador where rut=").append(rut)));
        Consulta Igualdad = new Consulta(con);
        Igualdad.exec(sql); 
        if(Igualdad.next())
        {
        	valor = Igualdad.getString("unidad");
        }
        return valor;
    }

    private Connection con;
    private String mensajeError;
    
    private Consulta consul;
    private Validar valida;    

}