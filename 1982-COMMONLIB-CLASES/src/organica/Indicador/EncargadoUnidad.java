package organica.Indicador;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.datos.VerComponente;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package Indicador:
//            Periodo, DetIndicador, ValorIndicador

public class EncargadoUnidad extends MyHttpServlet
{

    public EncargadoUnidad()
    {

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n>>---> Entro al doPost de EncargadoUnidad");
        user = Usuario.rescatarUsuario(req);
        String strEmpresa = req.getParameter("empresa");
        String strUnidad = req.getParameter("unidad");
        String strUnidadDescrip = req.getParameter("unidad_desc");
        String strPeriodo = req.getParameter("peri");
        String strPeriodoPal = "";
        if(strEmpresa == null || strEmpresa.trim().equals(""))
            strEmpresa = user.getEmpresa();
        if(strPeriodo == null || strPeriodo.trim().equals(""))
        {
            Periodo peri = new Periodo(conexion);
            strPeriodo = peri.getPeriodo();
            strPeriodoPal = peri.getPeriodoPalabras();
        } else
        {
            Periodo peri = new Periodo(conexion, strPeriodo);
            strPeriodoPal = peri.getPeriodoPalabras();
        }
        if(strUnidadDescrip == null || strUnidadDescrip.trim().equals(""))
        {
            Consulta consul_unid = new Consulta(conexion);
            String sql = String.valueOf(new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE unid_empresa = '" +  strEmpresa + "' AND unid_id = '" + strUnidad + "'"));
            consul_unid.exec(sql);
            if(consul_unid.next())
                strUnidadDescrip = consul_unid.getString("unid_desc");
            consul_unid.close();
        }
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("ver", verComp.getSimpleHash());
        modelRoot.put("unid_desc", strUnidadDescrip);
        modelRoot.put("unid_id", strUnidad);
        modelRoot.put("empresa", strEmpresa);
        modelRoot.put("peri", strPeriodo);
        modelRoot.put("peri_palabras", strPeriodoPal);
        modelRoot.put("VDD", req.getParameter("VDD"));
        DetIndicador detIndic = new DetIndicador(conexion, "9-1", strPeriodo);
        if(detIndic.existeIndicador())
        {
            ValorIndicador valorIndic = new ValorIndicador(conexion, detIndic, strPeriodo, strUnidad, "U", strEmpresa);
            modelRoot.put("dotacion", valorIndic.getValorIndic());
        }
        String sql = String.valueOf(new StringBuilder("SELECT a.rut_encargado,a.nombre, a.cargo_desc,a.anexo,a.e_mail,a.foto,a.mision FROM view_unidad_encargado a,eje_ges_unidad_rama b WHERE a.periodo = " + strPeriodo + " AND a.unid_empresa = '" + strEmpresa + "' AND b.unidad = '" + strUnidad + "' AND a.estado='1' AND b.tipo = 'U' AND  b.uni_rama = a.unid_id AND a.unid_empresa = b.empresa"));
        Consulta consul = new Consulta(conexion);
        OutMessage.OutMessagePrint("UEn --> ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        if(consul.next())
        {
            modelRoot.put("tiene", "SI");
            modelRoot.put("rut", consul.getString("rut_encargado"));
            modelRoot.put("nombre", consul.getString("nombre"));
            modelRoot.put("cargo", consul.getString("cargo_desc"));
            modelRoot.put("anexo", consul.getString("anexo"));
            modelRoot.put("e_mail", consul.getString("e_mail"));
            modelRoot.put("foto", consul.getString("foto"));
            modelRoot.put("mision", consul.getString("mision"));
        }
        consul.close();
        super.retTemplate(resp,"Gestion/Indicadores/EncargadoUnidad.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
    }

    private Usuario user;
    private VerComponente verComp;
}