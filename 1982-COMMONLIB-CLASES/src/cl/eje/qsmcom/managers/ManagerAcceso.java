package cl.eje.qsmcom.managers;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerAcceso
{
  private static ManagerAcceso instance;
  
  public static ManagerAcceso getInstance()
  {
    if (instance == null) {
      synchronized (ManagerAcceso.class)
      {
        if (instance == null) {
          instance = new ManagerAcceso();
        }
      }
    }
    return instance;
  }
  
  public boolean insertPerfil(String newPassword, int rut)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_usuario SET password_usuario = ?  WHERE login_usuario = ?  ");
    
    Object[] params = { newPassword, Integer.valueOf(rut) };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update("portal", strConsulta.toString(), params) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  public ConsultaData getUltimoAcceso(int rut)
  {
    String sql = "select rut,fecha_ult_acceso,clave,clave_enc from eje_ges_usuario_ult_acceso where rut = ?";
    
    ConsultaData data = null;
    Object[] params = { Integer.valueOf(rut) };
    try
    {
      data = ConsultaTool.getInstance().getData("portal", sql, params);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return data;
  }
  
  public ConsultaData getEjeGesUsuario(int rut)
  {
    String sql = "select login_usuario,password_usuario,rut_usuario,emp_rel,uni_rel,error,passw_cambiar,passw_ult_cambio,tipo_rel,cant_ingresos,wp_cod_empresa,wp_cod_planta,md5 from eje_ges_usuario where rut_usuario = ?";
    
    ConsultaData data = null;
    Object[] params = { Integer.valueOf(rut) };
    try
    {
      data = ConsultaTool.getInstance().getData("portal", sql, params);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return data;
  }
  
  public List<String> getPrivilegios(int rut)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" SELECT app_id,rut_usuario, vigente, wp_cod_empresa, wp_cod_planta FROM eje_ges_user_app WHERE rut_usuario = ? ");
    ArrayList<String> lista = new ArrayList();
    Object[] params = { Integer.valueOf(rut) };
    
    ConsultaData data = null;
    try
    {
      data = ConsultaTool.getInstance().getData("portal", strConsulta.toString(), params);
      while (data.next()) {
        lista.add(data.getString("app_id"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return lista;
  }
  
  public boolean addPrivilegios(int rut, String privilegio)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" INSERT INTO  eje_ges_user_app ");
    strConsulta.append(" \t(app_id,rut_usuario, vigente, wp_cod_empresa, wp_cod_planta) ");
    strConsulta.append(" VALUES (?,?,NULL,-1,-1)");
    
    Object[] params = { privilegio, Integer.valueOf(rut) };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().insert("portal", strConsulta.toString(), params);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
}
