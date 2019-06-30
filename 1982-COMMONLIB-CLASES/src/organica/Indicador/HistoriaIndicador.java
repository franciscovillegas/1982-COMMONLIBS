package organica.Indicador;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package Indicador:
//            Periodo, DetIndicador, ValorIndicador

public class HistoriaIndicador extends MyHttpServlet
{

    public HistoriaIndicador()
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
        OutMessage.OutMessagePrint("\n>>---> Entro al doPost de HistoriaIndicador");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strItem = req.getParameter("item");
        String strUnidad = req.getParameter("unidad");
        String strUnidadDescrip = req.getParameter("unidad_desc");
        String strPeriodo = req.getParameter("peri");
        String strTipoIndicador = req.getParameter("tipo");
        if(strPeriodo == null || strPeriodo.trim().equals(""))
        {
            Periodo peri = new Periodo(conexion);
            strPeriodo = peri.getPeriodo();
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
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("unid_desc", strUnidadDescrip);
        modelRoot.put("unid_id", strUnidad);
        modelRoot.put("tipo_indic", strTipoIndicador);
        DetIndicador indicador;
        indicador = indicador = new DetIndicador(conexion, strItem, strPeriodo);
        modelRoot.put("peri", strPeriodo);
        if(indicador.existeIndicador())
        {
            modelRoot.put("existe_indic", "SI");
            modelRoot.put("grupo", indicador.getGrupo());
            modelRoot.put("grupo_desc", indicador.getDescGrupo());
            modelRoot.put("item", indicador.getDescItem());
            modelRoot.put("nombre", indicador.getNombre());
        }
        SimpleHash simplehash = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        Consulta consul = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT TOP 12 peri_id FROM eje_ges_periodo WHERE (peri_id <= ")).append(strPeriodo).append(") ORDER BY peri_id DESC")));
        consul.exec(sql);
        for(; consul.next(); simplelist.add(simplehash))
        {
            String periodo_consulta = consul.getString("peri_id");
            if(!indicador.existeIndicador() || !indicador.perteneceAlPeriodo(periodo_consulta))
                indicador = new DetIndicador(conexion, strItem, periodo_consulta);
            simplehash = new SimpleHash();
            simplehash.put("periodo", periodo_consulta);
            if(indicador.existeIndicador())
            {
                simplehash.put("existe_indic", "SI");
                simplehash.put("formula", valida.validarDato(indicador.getFormula()));
                simplehash.put("glosa", valida.validarDato(indicador.getGlosa()));
                simplehash.put("descrip", valida.validarDato(indicador.getDescIndicador()));
                simplehash.put("tramo_inf1", valida.validarDato(indicador.getTramoInf1()));
                simplehash.put("tramo_inf2", valida.validarDato(indicador.getTramoInf2()));
                simplehash.put("tramo_med1", valida.validarDato(indicador.getTramoMedio1()));
                simplehash.put("tramo_med2", valida.validarDato(indicador.getTramoMedio2()));
                simplehash.put("tramo_sup1", valida.validarDato(indicador.getTramoSup1()));
                simplehash.put("tramo_sup2", valida.validarDato(indicador.getTramoSup2()));
                ValorIndicador valIndic = new ValorIndicador(conexion, indicador, periodo_consulta, strUnidad, strTipoIndicador, user.getEmpresa());
                if(valIndic.existeValor())
                {
                    simplehash.put("existe_valor", "SI");
                    simplehash.put("foto", valIndic.getImagen());
                    simplehash.put("valor", valida.validarDato(valIndic.getValorIndic()));
                    simplehash.put("tramo", valida.validarDato(valIndic.getQueTramo()));
                    simplehash.put("tramo_desc", valida.validarDato(valIndic.getDescTramo()));
                }
            }
        }

        consul.close();
        modelRoot.put("indicadores", simplelist);
        super.retTemplate(resp,"Gestion/Indicadores/HistoriaIndicador.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
    }

    private Usuario user;
}