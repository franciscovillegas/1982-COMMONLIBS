package organica.com.eje.ges.indicadores;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ListaIndicadores extends MyHttpServlet
{

    public ListaIndicadores()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de ListaIndicadores");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Mantener Indicadores", "Tiempo de Sesi\363n expirado...");
        else
            generaDatos(req, resp, user, conexion);
        OutMessage.OutMessagePrint("Fin...");
        connMgr.freeConnection("portal", conexion);
    }

    public void generaDatos(HttpServletRequest req, HttpServletResponse resp, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        Validar valida = new Validar();
        SimpleHash modelRoot = new SimpleHash();
        String strPeriodo = (new Periodo(conexion)).getPeriodo();
        Consulta consulIndic = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT gral.gral_id, gral.gral_nombre, histo.grupo, histo.grupo_desc, histo.item, histo.nombre, histo.periodo_desde, histo.periodo_hasta, histo.tramo_inf1, histo.tramo_inf2, histo.tramo_medio1, histo.tramo_medio2, histo.tramo_sup1, histo.tramo_sup2, glosa, formula,desc_ind, desc_tramo_inf,desc_tramo_med,desc_tramo_sup FROM eje_ges_def_indicador_gral gral INNER JOIN eje_ges_def_indicadores_rel rel ON  gral.gral_id = rel.indic_gral INNER JOIN view_def_indicadores histo ON rel.item = histo.item WHERE (histo.periodo_desde <= ")).append(strPeriodo).append(") AND").append(" (histo.periodo_hasta >= ").append(strPeriodo).append(" OR").append(" histo.periodo_hasta IS NULL)").append(" ORDER BY gral.gral_nombre, gral.gral_id, histo.grupo_desc")));
        consulIndic.exec(sql);
        SimpleList listaIndic = new SimpleList();
        SimpleList listaGrupos = null;
        SimpleHash indicador = null;
        SimpleHash indicadorGrupo = null;
        String strGralIdAnt = "";
        String strGralId = "";
        for(; consulIndic.next(); listaGrupos.add(indicadorGrupo))
        {
            strGralId = consulIndic.getString("gral_id");
            if(!strGralId.equals(strGralIdAnt))
            {
                if(indicador != null)
                {
                    indicador.put("indic", listaGrupos);
                    listaIndic.add(indicador);
                }
                indicador = new SimpleHash();
                strGralIdAnt = strGralId;
                indicador.put("gral_id", strGralId);
                indicador.put("gral_nombre", consulIndic.getString("gral_nombre"));
                listaGrupos = new SimpleList();
            }
            indicadorGrupo = new SimpleHash();
            for(int column = 3; column <= consulIndic.getColumnCount(); column++)
            {
                String columnName = consulIndic.getColumnName(column);
                String columnValor = valida.validarDato(consulIndic.getValor(column), "");
                indicadorGrupo.put(columnName, columnValor);
            }

        }

        if(indicador != null)
        {
            indicador.put("indic", listaGrupos);
            listaIndic.add(indicador);
        }
        modelRoot.put("indicadores", listaIndic);
        modelRoot.put("gral_id", req.getParameter("gral_id"));
        super.retTemplate(resp,"Gestion/ManIndicadores/ListaIndicadores.htm",modelRoot);
        consulIndic.close();
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}