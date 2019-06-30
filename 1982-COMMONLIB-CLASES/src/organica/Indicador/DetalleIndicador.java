package organica.Indicador;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package Indicador:
//            Periodo, DetIndicador, TMUnidEstado, ValorIndicador

public class DetalleIndicador extends MyHttpServlet
{

    public DetalleIndicador()
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
        OutMessage.OutMessagePrint("\n>>---> Entro al doPost de DetalleIndicador");
        user = Usuario.rescatarUsuario(req);
        String strItem = req.getParameter("item");
        String strUnidad = req.getParameter("unidad");
        String strUnidadDescrip = req.getParameter("unidad_desc");
        String strPeriodo = req.getParameter("peri");
        String strPeriodoPal = "";
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
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(user.getEmpresa()).append("') AND (unid_id = '").append(strUnidad).append("')")));
            consul_unid.exec(sql);
            if(consul_unid.next())
                strUnidadDescrip = consul_unid.getString("unid_desc");
            consul_unid.close();
        }
        DetIndicador indicador = new DetIndicador(conexion, strItem, strPeriodo);
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("peri", strPeriodo);
        modelRoot.put("peri_palabras", strPeriodoPal);
        modelRoot.put("empresa", user.getEmpresa());
        modelRoot.put("indic", strItem);
        modelRoot.put("unid_id", strUnidad);
        modelRoot.put("unid_desc", strUnidadDescrip);
        modelRoot.put("semV", req.getParameter("semaforov"));
        modelRoot.put("semA", req.getParameter("semaforoa"));
        modelRoot.put("semR", req.getParameter("semaforor"));
        modelRoot.put("UnidEstado", new TMUnidEstado(conexion, getTemplatePath(), indicador));
        if(indicador.existeIndicador())
        {
            modelRoot.put("existe_indic", "SI");
            modelRoot.put("grupo", indicador.getGrupo());
            modelRoot.put("grupo_desc", indicador.getDescGrupo());
            modelRoot.put("item", indicador.getDescItem());
            modelRoot.put("nombre", indicador.getNombre());
            modelRoot.put("glosa", indicador.getGlosa());
            modelRoot.put("formula", indicador.getFormula());
            modelRoot.put("descrip", indicador.getDescIndicador());
            modelRoot.put("tramo_inf2", indicador.getTramoInf2());
            modelRoot.put("tramo_med1", indicador.getTramoMedio1());
            modelRoot.put("tramo_med2", indicador.getTramoMedio2());
            modelRoot.put("tramo_sup1", indicador.getTramoSup1());
            ValorIndicador valIndic = new ValorIndicador(conexion, indicador, strPeriodo, strUnidad, "R", user.getEmpresa());
            if(valIndic.existeValor())
            {
                modelRoot.put("existe_valor", "SI");
                modelRoot.put("foto", valIndic.getImagen());
                modelRoot.put("valor", valIndic.getValorIndic());
                modelRoot.put("tramo", valIndic.getQueTramo());
                modelRoot.put("tramo_desc", valIndic.getDescTramo());
            }
        }
        super.retTemplate(resp,"Gestion/Indicadores/DetalleIndicador.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
    }

    private Usuario user;
}