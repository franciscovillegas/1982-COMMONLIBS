package organica.com.eje.ges.anexos.visor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class VerAsignacion extends MyHttpServlet
{

    public VerAsignacion()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de VerAsignacion");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Visor de Anexos", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String strMsg = "";

            SimpleHash modelRoot = new SimpleHash();
            OutMessage.OutMessagePrint("QueryString --> ".concat(String.valueOf(String.valueOf(req.getQueryString()))));
            String paramEmpresa = req.getParameter("empresa");
            String paramUnidad = req.getParameter("unidad");
            String paramAccion = req.getParameter("accion");
            String paramValor = req.getParameter("valor");
            modelRoot.put("empresa", paramEmpresa);
            modelRoot.put("unidad", paramUnidad);
            modelRoot.put("valor", paramValor);
            modelRoot.put("accion", paramAccion);
            String sql = "";
            if(paramAccion != null)
            {
                if(paramAccion.equals("AN"))
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT t.unidad, t.rut, t.nombre, t.cargo, t.e_mail, t.digito_ver, t.foto FROM view_ges_rut_all t INNER JOIN eje_ges_trabajadores_anexos a ON t.rut = a.rut AND t.empresa = a.empresa WHERE (a.anexo = '")).append(paramValor).append("')")));
                else
                if(paramAccion.equals("FO"))
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT t.unidad, t.rut, t.nombre, t.cargo, t.e_mail, t.digito_ver, t.foto FROM view_ges_rut_all t INNER JOIN eje_ges_trabajadores_fono a ON t.rut = a.rut AND t.empresa = a.empresa WHERE (a.fono = '")).append(paramValor).append("')")));
                else
                if(paramAccion.equals("FA"))
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT t.unidad, t.rut, t.nombre, t.cargo, t.e_mail, t.digito_ver, t.foto FROM view_ges_rut_all t INNER JOIN eje_ges_trabajadores_fax a ON t.rut = a.rut AND t.empresa = a.empresa WHERE (a.fax = '")).append(paramValor).append("')")));
                if(paramUnidad != null)
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND (a.unidad = '")).append(paramUnidad).append("')"))));
                Consulta consul = new Consulta(conexion);
                consul.exec(sql);
                OutMessage.OutMessagePrint("Ver Asig. --> ".concat(String.valueOf(String.valueOf(sql))));
                SimpleList empleados = new SimpleList();
                int cant_emp = 0;
                SimpleHash hash;
                for(; consul.next(); empleados.add(hash))
                {
                    cant_emp++;
                    hash = new SimpleHash();
                    hash.put("n", String.valueOf(cant_emp));
                    hash.put("unidad", consul.getString("unidad"));
                    hash.put("rut", consul.getString("rut"));
                    hash.put("rutf", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                    hash.put("nombre", consul.getString("nombre"));
                    hash.put("cargo", consul.getString("cargo"));
                    hash.put("foto", consul.getString("foto"));
                    hash.put("e_mail", consul.getString("e_mail"));
                }

                modelRoot.put("empleados", empleados);
                modelRoot.put("cant_emp", String.valueOf(cant_emp));
                consul.close();
            }

            
            super.retTemplate(resp,"Gestion/Anexos/Visor/VerAsignacion.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}