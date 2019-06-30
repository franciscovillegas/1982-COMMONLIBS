package portal.com.eje.datos;

import portal.com.eje.tools.ManagersBase;
import java.sql.Connection;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public abstract class Managers extends ManagersBase
{

    public Managers(Connection conex)
    {
        conexion = conex;
    }
    
    public SimpleList getSimpleSecuences(Consulta rs)
    {  SimpleList ss = new SimpleList();
       if(rs != null) {  ss = rs.getSimpleList(); rs.close(); }
       return ss;
    }

    public SimpleHash getSimpleHash(Consulta rs)
    {  SimpleHash sh = new SimpleHash();
       if(rs != null) {  sh = rs.getSimpleHash(); rs.close(); }
       return sh;
    }

    protected Connection conexion;
}