package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.datos.Unidades;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Buscar_por_Nombre extends MyHttpServlet
{

    public Buscar_por_Nombre()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void ArmaConsulta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            control = new ControlAcceso(user);
            x = new Unidades();
            int cual = 0;
            if(req.getParameter("Operacion") != null)
                cual = Integer.parseInt(req.getParameter("Operacion"));
            else
                cual = 1;
            OutMessage.OutMessagePrint("---->Inicio cual: ".concat(String.valueOf(String.valueOf(cual))));
            switch(cual)
            {
            case 1: // '\001'
                PagIngreso(req, resp, Conexion);
                break;

            case 2: // '\002'
                DespResultado(req, resp, Conexion);
                break;
            }
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("---->Termino!!!!!!!!!!*****************");
    }

    private void PagIngreso(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "Gestion/Buscar/form_busqueda.html";
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda Alfab\351tica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String tipo = null;
            if(control.tienePermiso("df_jer_comp"))
                modelRoot.put("uni", x.getUnidades(Conexion, user.getEmpresa()));
            else
                modelRoot.put("uni", x.getUnidades(Conexion, user.getEmpresa(), user.getUnidad(), tipo));
            modelRoot.put("rut", user.getRutUsuario().toString());
            modelRoot.put("unidad", user.getUnidad().toString());
            modelRoot.put("cargo", user.getCargo().toString());
        }
        super.retTemplate(resp,htmls,modelRoot);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "Gestion/Buscar/Desp_busqueda.html";
        String consulta = null;
        String nombre = null;
        String Bus = null;
        String Bus2 = "";
        String Boton = "";
        SimpleHash modelRoot = new SimpleHash();
        boolean valor = false;
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda Alfab\351tica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("FNum", new FormatoNumero());
            nombre = req.getParameter("nombre");
            Consulta Buscar = new Consulta(Conexion);
            String tipo = null;
            if(control.tienePermiso("df_jer_comp"))
            {
                Bus = "SELECT Distinct rut,digito, nombres, ape_paterno, ape_materno, unid_desc,     unid_id FROM view_ges_busqueda where (1=1) ";
                tipo = "R";
                modelRoot.put("uni", x.getUnidades(Conexion, user.getEmpresa()));
            } else
            {
                FiltroUnidad fu = new FiltroUnidad(user);
                Bus = String.valueOf(String.valueOf((new StringBuilder("SELECT Distinct  rut,digito, nombres, ape_paterno, ape_materno, unid_desc, unid_id FROM view_ges_busqueda WHERE ( ")).append(fu.getFiltro()).append(" )")));
                tipo = "R";
                modelRoot.put("uni", x.getUnidades(Conexion, user.getEmpresa(), user.getUnidad(), tipo));
            }
            if(req.getParameter("rut") != null && !req.getParameter("rut").trim().equals(""))
            {
                Vector rutcompleto = new Vector();
                rutcompleto = Tools.separaLista(req.getParameter("rut"), "-");
                if(rutcompleto.size() > 1)
                {
                    OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--->Rut:    ")).append((String)rutcompleto.elementAt(0)).append("\n").append("--->Digito: ").append((String)rutcompleto.elementAt(1)))));
                    Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND rut ='")).append((String)rutcompleto.elementAt(0)).append("'").append(Bus2)));
                    Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND digito ='")).append((String)rutcompleto.elementAt(1)).append("'").append(Bus2)));
                    valor = true;
                }
            }
            if(req.getParameter("nombre") != null && !req.getParameter("nombre").trim().equals(""))
            {
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND nombres LIKE'%")).append(req.getParameter("nombre").toUpperCase()).append("%'").append(Bus2)));
                valor = true;
            }
            if(req.getParameter("apaterno") != null && !req.getParameter("apaterno").trim().equals(""))
            {
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ape_paterno LIKE '%")).append(req.getParameter("apaterno").toUpperCase()).append("%'").append(Bus2)));
                valor = true;
            }
            if(req.getParameter("amaterno") != null && !req.getParameter("amaterno").trim().equals(""))
            {
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ape_materno LIKE '%")).append(req.getParameter("amaterno").toUpperCase()).append("%'").append(Bus2)));
                valor = true;
            }
            if(req.getParameter("unidad") != null && !req.getParameter("unidad").trim().equals("") && !req.getParameter("unidad").trim().equals("Todas"))
            {
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND unid_id ='")).append(req.getParameter("unidad").toUpperCase()).append("' ").append(Bus2)));
                Consulta y = new Consulta(Conexion);
                y.exec(String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(user.getEmpresa()).append("')  AND (unid_id = '").append(req.getParameter("unidad").toUpperCase()).append("')"))));
                if(y.next())
                    modelRoot.put("unides", y.getString(1));
                y.close();
            }
            String sel1 = "";
            String sel2 = "";
            String sel3 = "";
            String sel4 = "";
            if(req.getParameter("sel1") != null && !req.getParameter("sel1").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel1  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel1").trim()))));
                sel1 = req.getParameter("sel1").trim();
                if(sel1.trim().equals("Rut"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                if(sel1.trim().equals("Nombre"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                if(sel1.trim().equals("A. Paterno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                if(sel1.trim().equals("A. Materno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
            }
            if(req.getParameter("sel2") != null && !req.getParameter("sel2").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel2 : ".concat(String.valueOf(String.valueOf(req.getParameter("sel2").trim()))));
                if(!req.getParameter("sel2").trim().equals(sel1))
                {
                    sel2 = req.getParameter("sel2").trim();
                    if(sel2.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel2.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel2.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel2.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(req.getParameter("sel3") != null && !req.getParameter("sel3").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel3  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel3").trim()))));
                if(!req.getParameter("sel3").trim().equals(sel1) && !req.getParameter("sel3").trim().equals(sel2))
                {
                    sel3 = req.getParameter("sel4").trim();
                    if(sel3.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel3.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel3.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel3.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(req.getParameter("sel4") != null && !req.getParameter("sel4").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel4  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel4").trim()))));
                if(!req.getParameter("sel4").trim().equals(sel1) && !req.getParameter("sel4").trim().equals(sel2) && !req.getParameter("sel4").trim().equals(sel3))
                {
                    sel4 = req.getParameter("sel4").trim();
                    if(sel4.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel4.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel4.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel4.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(!Boton.trim().equals(""))
            {
                Boton = Boton.substring(0, Boton.length() - 1);
                Boton = " ORDER BY ".concat(String.valueOf(String.valueOf(Boton)));
            }
            consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Bus)))).append(Bus2).append(Boton)));
            OutMessage.OutMessagePrint("SELECT : ".concat(String.valueOf(String.valueOf(consulta))));
            if(valor)
            {
                Buscar.exec(consulta);
                modelRoot.put("varios", Buscar.getSimpleList());
            }
        }
        super.retTemplate(resp,htmls,modelRoot);
    }

    private Usuario user;
    private ControlAcceso control;
    private Unidades x;
    private Mensaje mensaje;
}