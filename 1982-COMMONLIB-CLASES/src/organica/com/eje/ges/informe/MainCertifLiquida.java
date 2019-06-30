package organica.com.eje.ges.informe;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.arbol.MiArbolTM;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class MainCertifLiquida extends MyHttpServlet
{

    public MainCertifLiquida()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de MainCertifLiquida(Carga Organica)");
        user = Usuario.rescatarUsuario(req);
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("***Rut: ")).append(user.getRutConsultado()).append("-->Es valido?: ").append(user.esValido()))));
        String sql = "";
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            devolverPaginaMensage(resp, "Ingreso", "");
        } else
        {
            String reporte = "";
            String html = "";
            int cual = Integer.parseInt(req.getParameter("cual"));
            OutMessage.OutMessagePrint("--->Cual Reporte: ".concat(String.valueOf(String.valueOf(cual))));
            switch(cual)
            {
            case 1: // '\001'
                reporte = "CertifAfp.rpt";
                html = "main_Afp.htm";
                break;

            case 2: // '\002'
                reporte = "CertifSalud.rpt";
                html = "main_Salud.htm";
                break;

            case 3: // '\003'
                reporte = "CertifCaja.rpt";
                html = "main_Caja.htm";
                break;

            case 4: // '\004'
                reporte = "CertifInp.rpt";
                html = "main_Inp.htm";
                break;

            case 5: // '\005'
                reporte = "LiquidaAlias.rpt";
                html = "main_Certif.htm";
                break;
            }
            SimpleHash modelRoot = new SimpleHash();
            String HTM = "Gestion/arbol/miarbol.htm";
            modelRoot.put("TraeArbol", new MiArbolTM(getTemplate(HTM), user));
            modelRoot.put("Control", new ControlAccesoTM(control));
            modelRoot.put("reporte", reporte);
            FiltroUnidad fu = new FiltroUnidad(user);
            modelRoot.put("unidad", fu.getUnidadRel().getUnidad());
            modelRoot.put("empresa", fu.getUnidadRel().getEmpresa());
            Consulta consul2 = new Consulta(conexion);
            switch(cual)
            {
            case 1: // '\001'
                sql = "SELECT afp, descrip FROM eje_ges_afp order by descrip";
                consul2.exec(sql);
                modelRoot.put("afps", consul2.getSimpleList());
                break;

            case 2: // '\002'
                sql = "SELECT isapre, descrip FROM eje_ges_isapres order by descrip";
                consul2.exec(sql);
                modelRoot.put("isapres", consul2.getSimpleList());
                break;

            case 3: // '\003'
                sql = "SELECT caja_id, caja_nombre FROM eje_ges_caja_compensacion ORDER BY caja_nombre";
                consul2.exec(sql);
                modelRoot.put("cajas", consul2.getSimpleList());
                sql = "SELECT isapre, descrip FROM eje_ges_isapres order by descrip";
                consul2.exec(sql);
                modelRoot.put("isapres", consul2.getSimpleList());
                break;

            case 4: // '\004'
                sql = "SELECT afp, descrip FROM eje_ges_afp order by descrip";
                consul2.exec(sql);
                modelRoot.put("afps", consul2.getSimpleList());
                break;
            }
            consul2.close();
            modelRoot.put("rut_user", user.getRutUsuario());
            super.retTemplate(resp,"Certificados/".concat(String.valueOf(String.valueOf(html))),modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Certificados/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}