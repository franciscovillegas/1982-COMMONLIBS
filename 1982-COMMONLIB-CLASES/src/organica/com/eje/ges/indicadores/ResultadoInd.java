package organica.com.eje.ges.indicadores;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.DetIndicador;
import organica.Indicador.Periodo;
import organica.Indicador.ValorIndicador;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ResultadoInd extends MyHttpServlet
{

    public ResultadoInd()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro Lista de Indicadores");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(user.esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexion);
            FiltroUnidad fu = new FiltroUnidad(user);
            String emp = fu.getUnidadRel().getEmpresa();
            String uni = fu.getUnidadRel().getUnidad();
            if(control.tienePermiso("df_jer_comp"))
            {
                emp = ControlAcceso.EmpSup;
                uni = ControlAcceso.UniSup;
            }
            String sql = "";
            String sqlor = "";
            String sql1 = "";
            String sql2 = "";
            String sql3 = "";
            String BAE = null;
            String XXI = null;
            String DF = null;
            if(req.getParameter("cbrojo") != null || req.getParameter("cbverde") != null || req.getParameter("cbamarillo") != null)
                BAE = "BAE";
            if(req.getParameter("cxrojo") != null || req.getParameter("cxverde") != null || req.getParameter("cxamarillo") != null)
                XXI = "XXI";
            if(req.getParameter("cdrojo") != null || req.getParameter("cdverde") != null || req.getParameter("cdamarillo") != null)
                DF = "DF";
            sql = "SELECT distinct gral_id, gral_nombre,item FROM view_def_indic_grupo ";
            sqlor = " order by gral_nombre";
            if(req.getParameter("todos") != null)
            {
                sql = String.valueOf(sql) + String.valueOf(sqlor);
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
            }
            if(req.getParameter("alerta") != null)
            {
                if(BAE != null)
                    sql3 = " grupo='BAE'";
                if(XXI != null)
                    if(BAE != null)
                        sql3 = String.valueOf(String.valueOf(sql3)).concat(" OR grupo='XXI'");
                    else
                        sql3 = " grupo='XXI'";
                if(DF != null)
                    if(XXI != null)
                        sql3 = String.valueOf(String.valueOf(sql3)).concat(" OR grupo='DF'");
                    else
                    if(BAE != null)
                        sql3 = String.valueOf(String.valueOf(sql3)).concat(" OR grupo='DF'");
                    else
                        sql3 = " grupo='DF'";
                if(!sql1.trim().equals("") || !sql2.trim().equals("") || !sql3.trim().equals(""))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" where ").append(sql3).append(sqlor)));
                    OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                    consul.exec(sql);
                }
            }
            String periodo = "";
            String strUnidadDescrip = req.getParameter("unidad_desc");
            periodo = req.getParameter("periodo");
            if(periodo == null || periodo.trim().equals(""))
                periodo = (new Periodo(conexion)).getPeriodo();
            if(strUnidadDescrip == null || strUnidadDescrip.trim().equals(""))
            {
                Consulta consul_unid = new Consulta(conexion);
                String sql10 = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(emp).append("') AND (unid_id = '").append(uni).append("')")));
                consul_unid.exec(sql10);
                if(consul_unid.next())
                    strUnidadDescrip = consul_unid.getString("unid_desc");
                consul_unid.close();
            }
            modelRoot.put("periodo", periodo);
            modelRoot.put("fecha_act", Tools.RescataFecha(new Date()));
            modelRoot.put("unidad_desc", strUnidadDescrip);
            DetIndicador indica = new DetIndicador();
            ValorIndicador valor = new ValorIndicador();
            int gral_id = 0;
            int gral_id_ant = 0;
            SimpleList ListaBae = new SimpleList();
            SimpleList ListaXxi = new SimpleList();
            SimpleList ListaDf = new SimpleList();
            SimpleList Lista = new SimpleList();
            SimpleHash Grupo = new SimpleHash();
            String Nombre = "";
            do
            {
                if(!consul.next())
                    break;
                gral_id = consul.getInt("gral_id");
                int sw = 0;
                indica = new DetIndicador(conexion, consul.getString("item"), periodo);
                valor = new ValorIndicador(conexion, indica, periodo, uni, "R", emp);
                if(req.getParameter("todos") != null)
                {
                    if(req.getParameter("ctrojo") != null && valor.getSemaforo() == "R")
                        sw = 1;
                    if(req.getParameter("ctamarillo") != null && valor.getSemaforo() == "A")
                        sw = 1;
                    if(req.getParameter("ctverde") != null && valor.getSemaforo() == "V")
                        sw = 1;
                }
                if(req.getParameter("alerta") != null)
                {
                    if(BAE != null)
                    {
                        if(req.getParameter("cbrojo") != null && valor.getSemaforo() == "R")
                            sw = 1;
                        if(req.getParameter("cbamarillo") != null && valor.getSemaforo() == "A")
                            sw = 1;
                        if(req.getParameter("cbverde") != null && valor.getSemaforo() == "V")
                            sw = 1;
                    }
                    if(XXI != null)
                    {
                        if(req.getParameter("cxrojo") != null && valor.getSemaforo() == "R")
                            sw = 1;
                        if(req.getParameter("cxamarillo") != null && valor.getSemaforo() == "A")
                            sw = 1;
                        if(req.getParameter("cxverde") != null && valor.getSemaforo() == "V")
                            sw = 1;
                    }
                    if(DF != null)
                    {
                        if(req.getParameter("cdrojo") != null && valor.getSemaforo() == "R")
                            sw = 1;
                        if(req.getParameter("cdamarillo") != null && valor.getSemaforo() == "A")
                            sw = 1;
                        if(req.getParameter("cdverde") != null && valor.getSemaforo() == "V")
                            sw = 1;
                    }
                }
                if(sw == 1)
                {
                    SimpleHash detalleitem = new SimpleHash();
                    detalleitem.put("Id", (new Integer(gral_id)).toString());
                    detalleitem.put("item", consul.getString("item"));
                    detalleitem.put("formula", indica.getFormula());
                    detalleitem.put("desc_item", indica.getDescItem());
                    detalleitem.put("glosa", indica.getGlosa());
                    detalleitem.put("nombre", indica.getNombre());
                    detalleitem.put("valor", valor.getValorIndic());
                    detalleitem.put("desctramo", valor.getDescTramo());
                    detalleitem.put("quetramo", valor.getQueTramo());
                    detalleitem.put("semaforo", valor.getImagen());
                    detalleitem.put("unidad", uni);
                    detalleitem.put("tinf", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indica.getTramoInf1())))).append(" - ").append(indica.getTramoInf2()))));
                    detalleitem.put("tmed", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indica.getTramoMedio1())))).append(" - ").append(indica.getTramoMedio2()))));
                    detalleitem.put("tsup", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indica.getTramoSup1())))).append(" - ").append(indica.getTramoSup2()))));
                    detalleitem.put("grupo", indica.getGrupo());
                    if(gral_id != gral_id_ant)
                    {
                        Grupo = new SimpleHash();
                        Grupo.put("Nombre", Nombre);
                        if(gral_id_ant != 0)
                        {
                            Grupo.put("Bae", ListaBae);
                            Grupo.put("Xxi", ListaXxi);
                            Grupo.put("Df", ListaDf);
                            Lista.add(Grupo);
                        }
                        Nombre = consul.getString("gral_nombre");
                        gral_id_ant = gral_id;
                        ListaBae = new SimpleList();
                        ListaXxi = new SimpleList();
                        ListaDf = new SimpleList();
                    }
                    if(indica.getGrupo().trim().equals("BAE"))
                        ListaBae.add(detalleitem);
                    if(indica.getGrupo().trim().equals("XXI"))
                        ListaXxi.add(detalleitem);
                    if(indica.getGrupo().trim().equals("DF"))
                        ListaDf.add(detalleitem);
                }
            } while(true);
            Grupo = new SimpleHash();
            Grupo.put("Nombre", Nombre);
            Grupo.put("Bae", ListaBae);
            Grupo.put("Xxi", ListaXxi);
            Grupo.put("Df", ListaDf);
            Lista.add(Grupo);
            modelRoot.put("lista_ind", Lista);
            consul.close();
            super.retTemplate(resp,"Gestion/Indicadores/ListaInd_resp.html",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}