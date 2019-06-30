package organica.com.eje.datos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;

import organica.datos.Consulta;
import organica.tools.OutMessage;

import freemarker.template.SimpleList;

public class InfoTipos
    implements Serializable
{

    public InfoTipos(Connection conn)
    {
        conexion = conn;
        consul = new Consulta(conexion);
    }

    public SimpleList getSimpleList(String sql)
    {
        OutMessage.OutMessagePrint("--> getSimpleList\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getUnidades(String strEmpresa)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_id as id, unid_desc as descrip FROM eje_ges_unidades WHERE (unid_empresa = '")).append(strEmpresa).append("') and vigente='S'")));
        return getSimpleList(sql);
    }

    public SimpleList getUnidadesEmpresa()
    {
        boolean resul = true;
        String sql = "SELECT unid_empresa as empresa, unid_id as id, unid_desc as descrip FROM eje_ges_unidades where vigente='S' Order by empresa, descrip";
        return getSimpleList(sql);
    }

    public SimpleList getEmpresas()
    {
        boolean resul = true;
        String sql = "SELECT empresa AS id, descrip AS descrip FROM eje_ges_empresa Order by descrip";
        return getSimpleList(sql);
    }

    public void close()
    {
        consul.close();
    }

    private void writeObject(ObjectOutputStream oos)
        throws IOException
    {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        ois.defaultReadObject();
    }

    private Connection conexion;
    private Consulta consul;
}