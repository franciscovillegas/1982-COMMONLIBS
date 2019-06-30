package organica.com.eje.ges.anexos;

import organica.datos.Consulta;
import freemarker.template.SimpleList;
import java.sql.Connection;

public class ManAnexo
{

    public ManAnexo(Connection conn)
    {
        conexion = conn;
        consul = new Consulta(conexion);
    }

    public SimpleList getRutAnexos(String rut)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.anexo, trab.anexo as id, trab.unidad, unid.publico FROM eje_ges_trabajadores_anexos trab INNER JOIN eje_ges_unidad_anexo unid ON trab.anexo = unid.anexo AND  trab.unidad = unid.unidad WHERE (trab.rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getRutFonos(String rut)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.fono, trab.fono as id, trab.unidad, unid.publico FROM eje_ges_trabajadores_fono trab INNER JOIN eje_ges_unidad_fono unid ON trab.fono = unid.fono AND  trab.unidad = unid.unidad WHERE (trab.rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getRutFaxes(String rut)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT fax, fax as id, unidad FROM eje_ges_trabajadores_fax WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getAnexosExistentes(String empresa, String unidad)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, empresa, anexo, anexo as id, publico FROM eje_ges_unidad_anexo WHERE (unidad = '")).append(unidad).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getAnexosExistentes(String unidad)
    {
        return getAnexosExistentes(null, unidad);
    }

    public SimpleList getFonosExistentes(String empresa, String unidad)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, empresa, fono, fono as id, publico FROM eje_ges_unidad_fono WHERE (unidad = '")).append(unidad).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getFonosExistentes(String unidad)
    {
        return getFonosExistentes(null, unidad);
    }

    public SimpleList getFaxExistentes(String empresa, String unidad)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, empresa, fax, fax as id FROM eje_ges_unidad_fax WHERE (unidad = '")).append(unidad).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getFaxExistentes(String unidad)
    {
        return getFaxExistentes(null, unidad);
    }

    public SimpleList getAnexosRelacionados(String empresa, String unidad, String anexo)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT DISTINCT ar.unidad, ar.anexo, ar.unidad_rel, u.unid_desc AS unidad_rel_desc, ar.anexo_rel, ar.empresa, a.publico FROM eje_ges_anexo_rel ar INNER JOIN eje_ges_unidades u ON ar.unidad_rel = u.unid_id INNER JOIN eje_ges_unidad_anexo a ON ar.unidad_rel = a.unidad AND ar.anexo_rel = a.anexo WHERE (ar.unidad = '")).append(unidad).append("') AND (ar.anexo = '").append(anexo).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (ar.empresa = '")).append(empresa).append("')"))));
        sql = String.valueOf(String.valueOf(sql)).concat(" ORDER BY u.unid_desc");
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public SimpleList getAnexosRelacionados(String unidad, String anexo)
    {
        return getAnexosRelacionados(null, unidad, anexo);
    }

    public boolean cambiarAnexo(String strEmpresa, String rutTrab, String nuevoAnexo)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_trabajadores_anexos SET anexo = '")).append(nuevoAnexo).append("'").append(" WHERE (rut = ").append(rutTrab).append(")")));
        if(strEmpresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(strEmpresa).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean quitarAnexoRelacion(String empresa, String unidad, String anexo, String unidadRel, String anexoRel)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_anexo_rel WHERE (unidad = '")).append(unidad).append("') AND (anexo = '").append(anexo).append("')").append(" AND (unidad_rel = '").append(unidadRel).append("') AND (anexo_rel = '").append(anexoRel).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean eliminarAnexo(String empresa, String unidad, String anexo)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidad_anexo WHERE (unidad = '")).append(unidad).append("') AND (anexo = '").append(anexo).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_anexos WHERE (anexo = '")).append(anexo).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_anexo_rel WHERE (unidad = '")).append(unidad).append("') AND (anexo = '").append(anexo).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_anexo_rel WHERE (unidad_rel = '")).append(unidad).append("') AND (anexo_rel = '").append(anexo).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean eliminarFono(String empresa, String unidad, String fono)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidad_fono WHERE (unidad = '")).append(unidad).append("') AND (fono = '").append(fono).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_fono WHERE (fono = '")).append(fono).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean eliminarFax(String empresa, String unidad, String fax)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidad_fax WHERE (unidad = '")).append(unidad).append("') AND (fax = '").append(fax).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_fax WHERE (fax = '")).append(fax).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean agregarAnexoRelacion(String strEmpresa, String strUnidad, String strAnexo, String strUnidadRel, String strAnexoRel)
    {
        boolean resul = false;
        if(!strUnidad.equals(strUnidadRel) || !strAnexo.equals(strAnexoRel))
        {
            String sql = "INSERT INTO eje_ges_anexo_rel (empresa, unidad, anexo, unidad_rel, anexo_rel)";
            if(strEmpresa != null)
                sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" VALUES ('")).append(strEmpresa).append("',"))));
            else
                sql = String.valueOf(String.valueOf(sql)).concat(" VALUES (null,");
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" '")).append(strUnidad).append("', '").append(strAnexo).append("', '").append(strUnidadRel).append("', '").append(strAnexoRel).append("')"))));
            resul = consul.insert(sql);
        }
        return resul;
    }

    public boolean crearAnexo(String empresa, String anexo, String unidad, String publico)
    {
        boolean resul = true;
        String sql = "INSERT INTO eje_ges_unidad_anexo (empresa, anexo, unidad, publico)";
        if(empresa == null)
            sql = String.valueOf(String.valueOf(sql)).concat(" VALUES (null, '");
        else
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" VALUES ('")).append(empresa).append("', '"))));
        sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(anexo)))).append("', '").append(unidad).append("', '").append(publico).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean modifAnexo(String empresa, String anexo, String unidad, String publico)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_anexo SET publico = '")).append(publico).append("'").append(" WHERE (unidad = '").append(unidad).append("') AND (anexo = '").append(anexo).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean crearFono(String empresa, String fono, String unidad, String publico)
    {
        boolean resul = true;
        String sql = "INSERT INTO eje_ges_unidad_fono (empresa, fono, unidad, publico)";
        if(empresa == null)
            sql = String.valueOf(String.valueOf(sql)).concat(" VALUES (null, '");
        else
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" VALUES ('")).append(empresa).append("', '"))));
        sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fono)))).append("', '").append(unidad).append("', '").append(publico).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean modifFono(String empresa, String fono, String unidad, String publico)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_fono SET publico = '")).append(publico).append("'").append(" WHERE (unidad = '").append(unidad).append("') AND (fono = '").append(fono).append("')")));
        if(empresa != null)
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (empresa = '")).append(empresa).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean crearFax(String empresa, String fax, String unidad)
    {
        boolean resul = true;
        String sql = "INSERT INTO eje_ges_unidad_fax (empresa, fax, unidad)";
        if(empresa == null)
            sql = String.valueOf(String.valueOf(sql)).concat(" VALUES (null, '");
        else
            sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" VALUES ('")).append(empresa).append("', '"))));
        sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fax)))).append("', '").append(unidad).append("')"))));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean modificarAnexo(String empresa, String anexo, String nuevo_anexo, String unidad, String fax)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_anexo SET anexo = '")).append(nuevo_anexo).append("', unidad = '").append(unidad).append("', fax = '").append(fax).append("'").append(" WHERE (empresa = '").append(empresa).append("') AND (anexo = '").append(anexo).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean AsigRutAnexo(String empresa, String rut, String anexo, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_trabajadores_anexos (empresa, rut, anexo, unidad) VALUES ('")).append(empresa).append("', ").append(rut).append(", '").append(anexo).append("', '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean AsigRutFono(String empresa, String rut, String fono, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_trabajadores_fono (empresa, rut, fono, unidad) VALUES ('")).append(empresa).append("', ").append(rut).append(", '").append(fono).append("', '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean AsigRutFax(String empresa, String rut, String fax, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_trabajadores_fax (empresa, rut, fax, unidad) VALUES ('")).append(empresa).append("', ").append(rut).append(", '").append(fax).append("', '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean QuitarRutAnexo(String empresa, String rut, String anexo, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_anexos WHERE (empresa = '")).append(empresa).append("') AND (rut = ").append(rut).append(") ").append(" AND (anexo = '").append(anexo).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean QuitarRutFono(String empresa, String rut, String fono, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_fono WHERE (empresa = '")).append(empresa).append("') AND (rut = ").append(rut).append(") ").append(" AND (fono = '").append(fono).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public boolean QuitarRutFax(String empresa, String rut, String fax, String unidad)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_trabajadores_fax WHERE (empresa = '")).append(empresa).append("') AND (rut = ").append(rut).append(") ").append(" AND (fax = '").append(fax).append("') AND (unidad = '").append(unidad).append("')")));
        resul = consul.insert(sql);
        return resul;
    }

    public void close()
    {
        consul.close();
    }

    private Connection conexion;
    private Consulta consul;
}