package cl.eje.qsmcom.managers;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;

public class ManagerOrganica
{
  private static ManagerOrganica instance;
  
  public static ManagerOrganica getInstance()
  {
    if (instance == null) {
      synchronized (ManagerOrganica.class)
      {
        if (instance == null) {
          instance = new ManagerOrganica();
        }
      }
    }
    return instance;
  }
  
  public ConsultaData getUnidadesDescendientes(String unidId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" select unidad from dbo.GetDescendientes('").append(unidId).append("'); ");
    
    ConsultaData data = null;
    try
    {
      data = ConsultaTool.getInstance().getData("portal", strConsulta.toString());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return data;
  }
  
  public boolean delNodo(String nodo)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" DELETE FROM eje_ges_jerarquia WHERE nodo_id = ? ");
    
    Object[] param = { nodo };
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update("portal", strConsulta.toString(), param) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  public boolean delUnidad(String nodo)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" DELETE FROM eje_ges_unidades WHERE unid_id = ? ");
    
    Object[] param = { nodo };
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update("portal", strConsulta.toString(), param) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  public boolean existeNodo(String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" SELECT 1 FROM eje_ges_unidades WHERE UNID_ID =  ? ");
    strConsulta.append(" UNION ");
    strConsulta.append(" SELECT 1 FROM eje_ges_jerarquia WHERE NODO_ID = ? ");
    Object[] param = { newId, newId };
    
    ConsultaData data = null;
    try
    {
      data = ConsultaTool.getInstance().getData("portal", strConsulta.toString(), param);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return (data != null) && (data.size() > 0);
  }
  
  public boolean addNodo(String unidadPadre, String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" INSERT INTO EJE_GES_JERARQUIA (compania, nodo_id, nodo_padre, nodo_nivel, nodo_imagen, nodo_corr, nodo_hijos) ");
    strConsulta.append(" VALUES (?,?,?,?,?,?,?) ");
    
    Object[] params = { Integer.valueOf(0), newId, unidadPadre, "0" };
    
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
  
  public boolean addUnidad(String newId, String newGlosa)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" INSERT INTO eje_ges_unidades (unid_empresa,unid_id,unid_desc, area, vigente) ");
    strConsulta.append(" VALUES (?,?,?,?,?)  ");
    
    Object[] params = { Integer.valueOf(0), newId, newGlosa, 0, "S" };
    
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
  
  public boolean updateNodo(String id, String name)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_unidades SET unid_desc = ? ");
    strConsulta.append(" WHERE unid_id = ?  ");
    
    Object[] params = { name, id };
    
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
  
  public boolean updateNodoID(String id, String newId)
  {
    Connection conn = null;
    boolean ok = true;
    try
    {
      conn = DBConnectionManager.getInstance().getConnection("portal");
      conn.setAutoCommit(false);
      
      ok &= updateNodoID_unidad(conn, id, newId);
      ok &= updateNodoID_jerarquia(conn, id, newId);
      updateNodoID_Trabajador(conn, id, newId);
      updateNodoID_UnidadEncargado(conn, id, newId);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      try
      {
        if (conn != null)
        {
          if (ok) {
            conn.commit();
          } else {
            conn.rollback();
          }
          if (!conn.getAutoCommit()) {
            conn.setAutoCommit(true);
          }
          DBConnectionManager.getInstance().freeConnection("portal", conn);
        }
      }
      catch (SQLException e1)
      {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (conn != null)
        {
          if (ok) {
            conn.commit();
          } else {
            conn.rollback();
          }
          if (!conn.getAutoCommit()) {
            conn.setAutoCommit(true);
          }
          DBConnectionManager.getInstance().freeConnection("portal", conn);
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return ok;
  }
  
  private boolean updateNodoID_unidad(Connection conn, String id, String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_unidades SET unid_id = ? ");
    strConsulta.append(" WHERE unid_id = ?  ");
    
    Object[] params = { newId, id };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update(conn, strConsulta.toString(), params) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  private boolean updateNodoID_jerarquia(Connection conn, String id, String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_jerarquia SET nodo_id = ? ");
    strConsulta.append(" WHERE nodo_id = ?  ");
    
    Object[] params = { newId, id };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update(conn, strConsulta.toString(), params) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  private boolean updateNodoID_UnidadEncargado(Connection conn, String id, String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_unidad_encargado SET unid_id = ? ");
    strConsulta.append(" WHERE unid_id = ? and estado = 1 ");
    
    Object[] params = { newId, id };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update(conn, strConsulta.toString(), params) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
  
  private boolean updateNodoID_Trabajador(Connection conn, String id, String newId)
  {
    StringBuffer strConsulta = new StringBuffer();
    strConsulta.append(" UPDATE eje_ges_trabajador SET unidad = ? ");
    strConsulta.append(" WHERE unidad = ? ");
    
    Object[] params = { newId, id };
    
    boolean ok = false;
    try
    {
      ok = ConsultaTool.getInstance().update(conn, strConsulta.toString(), params) > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ok;
  }
}
