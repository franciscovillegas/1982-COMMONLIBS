package portal.com.eje.orgpeople.manager;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import java.sql.Connection;

public class orgManager
{

    public orgManager()
    {
    }

    public Consulta getColaborador(String strEmpresa, String strRut, String strUnidad, Connection Conexion)
    {
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = new Consulta(Conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT   rut, digito_ver, ltrim(rtrim(nombres)) as nombres, ltrim(rtrim(ape_materno)) as ape_materno, ltrim(rtrim(ape_paterno)) as ape_paterno, ltrim(rtrim(e_mail)) as e_mail, ltrim(rtrim(foto)) as foto, ltrim(rtrim(id_unidad)) as id_unidad, ltrim(rtrim(id_cargo)) as id_cargo, ltrim(rtrim(unidad)) as unidad, ltrim(rtrim(cargo)) as cargo FROM dbo.view_rut_all WHERE    (rut = ")).append(strRut).append(")  AND (empresa = '").append(strEmpresa).append("') ").append("ORDER BY sueldo DESC")));
            rs.exec(sql);
        }
        return rs;
    }

    public Consulta getColaboradoresHijos(String strEmpresa, String strRut, String strUnidad, Connection Conexion)
    {
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = new Consulta(Conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT   rut, digito_ver, ltrim(rtrim(nombres)) as nombres, ltrim(rtrim(ape_materno)) as ape_materno, ltrim(rtrim(ape_paterno)) as ape_paterno, ltrim(rtrim(e_mail)) as e_mail, ltrim(rtrim(foto)) as foto, ltrim(rtrim(id_unidad)) as id_unidad, ltrim(rtrim(id_cargo)) as id_cargo, ltrim(rtrim(unidad)) as unidad, ltrim(rtrim(cargo)) as cargo FROM dbo.view_rut_all WHERE    (rut_supd = ")).append(strRut).append(")  AND (empresa = '").append(strEmpresa).append("') ").append("ORDER BY sueldo DESC")));
            rs.exec(sql);
        }
        return rs;
    }

    public Consulta getEncargadoUnidad(String strEmpresa, String strUnidad, Connection Conexion)
    {
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = new Consulta(Conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_encargado  FROM view_unidad_encargado WHERE (unid_empresa = '")).append(strEmpresa).append("') AND (unid_id = '").append(strUnidad).append("') ").append(" order by periodo desc ")));
            rs.exec(sql);
        }
        return rs;
    }

    public SimpleList getColaboradorRaiz(String strEmpresa, String strRut, String strUnidad, Connection Conexion, int intNivel)
    {
        SimpleList ss = new SimpleList();
        SimpleHash modelRoot = new SimpleHash();
        Validar vld = new Validar();
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = getColaborador(strEmpresa, strRut, strUnidad, Conexion);
            String strRutId = "";
            String strDvId = "";
            if(rs.next())
            {
                SimpleHash sh = new SimpleHash();
                strRutId = vld.validarDato(rs.getString("rut"), "");
                strDvId = vld.validarDato(rs.getString("digito_ver"), "");
                sh.put("strRut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strRutId)))).append("-").append(strDvId))));
                sh.put("strRutId", strRutId);
                sh.put("strNom", vld.validarDato(rs.getString("nombres"), ""));
                sh.put("strAP", vld.validarDato(rs.getString("ape_paterno"), ""));
                sh.put("strAm", vld.validarDato(rs.getString("ape_materno"), ""));
                sh.put("strUnidad", vld.validarDato(rs.getString("unidad"), ""));
                sh.put("strRol", vld.validarDato(rs.getString("cargo"), ""));
                sh.put("strFoto", vld.validarDato(rs.getString("foto"), ""));
                sh.put("strNiv", (new Integer(intNivel)).toString());
                sh.put("list", getColaboradores(strEmpresa, strRutId, "", Conexion, intNivel));
                ss.add(sh);
            }
            rs.close();
        }
        return ss;
    }

    public SimpleList getColaboradoresRaiz(String strEmpresa, String strRut, String strUnidad, Connection Conexion, int intNivel)
    {
        SimpleList ss = new SimpleList();
        SimpleHash modelRoot = new SimpleHash();
        Validar vld = new Validar();
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = getColaboradoresHijos(strEmpresa, strRut, strUnidad, Conexion);
            String strRutId = "";
            String strDvId = "";
            SimpleHash sh;
            for(; rs.next(); ss.add(sh))
            {
                sh = new SimpleHash();
                strRutId = vld.validarDato(rs.getString("rut"), "");
                strDvId = vld.validarDato(rs.getString("digito_ver"), "");
                sh.put("strRut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strRutId)))).append("-").append(strDvId))));
                sh.put("strRutId", strRutId);
                sh.put("strNom", vld.validarDato(rs.getString("nombres"), ""));
                sh.put("strAP", vld.validarDato(rs.getString("ape_paterno"), ""));
                sh.put("strAm", vld.validarDato(rs.getString("ape_materno"), ""));
                sh.put("strUnidad", vld.validarDato(rs.getString("unidad"), ""));
                sh.put("strRol", vld.validarDato(rs.getString("cargo"), ""));
                sh.put("strFoto", vld.validarDato(rs.getString("foto"), ""));
                sh.put("strNiv", (new Integer(intNivel)).toString());
                sh.put("list", getColaboradores(strEmpresa, strRutId, "", Conexion, intNivel));
            }

            rs.close();
        }
        return ss;
    }

    public SimpleList getColaboradores(String strEmpresa, String strRut, String strUnidad, Connection Conexion, int intNivel)
    {
        SimpleList ss = new SimpleList();
        Validar vld = new Validar();
        intNivel++;
        Consulta rs = null;
        if(Conexion != null)
        {
            rs = getColaboradoresHijos(strEmpresa, strRut, strUnidad, Conexion);
            String strRutId = "";
            String strDvId = "";
            SimpleHash sh;
            for(; rs.next(); ss.add(sh))
            {
                sh = new SimpleHash();
                strRutId = vld.validarDato(rs.getString("rut"), "");
                strDvId = vld.validarDato(rs.getString("digito_ver"), "");
                sh.put("strRut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strRutId)))).append("-").append(strDvId))));
                sh.put("strRutId", strRutId);
                sh.put("strNom", vld.validarDato(rs.getString("nombres"), ""));
                sh.put("strAP", vld.validarDato(rs.getString("ape_paterno"), ""));
                sh.put("strAm", vld.validarDato(rs.getString("ape_materno"), ""));
                sh.put("strUnidad", vld.validarDato(rs.getString("unidad"), ""));
                sh.put("strRol", vld.validarDato(rs.getString("cargo"), ""));
                sh.put("strFoto", vld.validarDato(rs.getString("foto"), ""));
                sh.put("strNiv", (new Integer(intNivel)).toString());
                sh.put("list", getColaboradores(strEmpresa, strRutId, "", Conexion, intNivel));
            }

            rs.close();
        }
        return ss;
    }
}