package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MuestraInfoRut extends MyHttpServlet
{

    public MuestraInfoRut()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Connection conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
        {
            if(conexion != null)
                GeneraDatos(req, resp, conexion);
            else
                devolverPaginaError(resp, "Error de conexi\363n a la BD");
        } else
        {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
        connMgr.freeConnection("portal", conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException
    {
        user = Usuario.rescatarUsuario(req);
        portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
        
        
        String rut = req.getParameter("rut");
        if(rut == null || rut.equals(""))
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Datos Personales", "Tiempo de Sesi\363n expirado...");
        else
        if(  !userPortal.tieneApp(PermisoPortal.BOTON_GESTION) )
        {
            mensaje.devolverPaginaMensage(resp, "Datos Personales", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            String digito = "";
            SimpleHash modelRoot = new SimpleHash();
            String edad = null;
            String Ncargas = null;
            int anos = 0;
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            Consulta cargas = new Consulta(Conexion);
            consulta = "select count(nombre) as cargas from eje_ges_grupo_familiar where (es_carga = 'S') and rut=" + rut;
            cargas.exec(consulta);
            cargas.next();
            Ncargas = cargas.getString("cargas");
            cargas.close();
            consulta = "select digito_ver,nombre,afp,isapre,fecha_nacim,estado_civil,telefono,nacionalidad,Xedad,grupo_sangre,domicilio,comuna,id_foto,cargo,id_unidad, unidad,anexo,e_mail  from view_ges_InfoRut where rut = '" + rut + "'";
            OutMessage.OutMessagePrint("---->consulta: " + consulta);
            info.exec(consulta);
            if(info.next())
            {
                digito = info.getString("digito_ver");
                modelRoot.put("rut", rut);
                Tools _tmp = tool;
                modelRoot.put("rut2", Tools.setFormatNumber(rut) + "-" + digito);
                modelRoot.put("nombre", info.getString("nombre"));
                modelRoot.put("afp", valida.validarDato(info.getString("afp"), "NR"));
                modelRoot.put("isapre", valida.validarDato(info.getString("isapre"), "NR"));
                modelRoot.put("fec_nac", valida.validarFecha(info.getValor("fecha_nacim")));
                modelRoot.put("est_civil", valida.validarDato(info.getString("estado_civil"), "NR"));
                modelRoot.put("fono", valida.validarDato(info.getString("telefono"), "NR"));
                modelRoot.put("nacionalidad", valida.validarDato(info.getString("nacionalidad"), "NR"));
                edad = valida.validarDato(info.getString("Xedad"), "0");
                OutMessage.OutMessagePrint("--------edad meses  : " + edad);
                anos = Integer.parseInt(edad) / 12;
                if(anos < 0)
                    anos *= -1;
                modelRoot.put("edad", String.valueOf(anos));
                OutMessage.OutMessagePrint("--------cargas   : " + Ncargas);
                modelRoot.put("numcargas", valida.validarDato(Ncargas, "0"));
                modelRoot.put("sangre", valida.validarDato(info.getString("grupo_sangre"), "NR"));
                modelRoot.put("domicilio", valida.validarDato(info.getString("domicilio"), "NR"));
                modelRoot.put("comuna", valida.validarDato(info.getString("comuna"), "NR"));
                modelRoot.put("foto", info.getString("id_foto"));
                modelRoot.put("cargo", info.getString("cargo"));
                modelRoot.put("unidad", info.getString("unidad"));
                modelRoot.put("id_unidad", info.getString("id_unidad"));
                modelRoot.put("email", valida.validarDato(info.getString("e_mail"), "NR"));
                modelRoot.put("anexo", valida.validarDato(info.getString("anexo"), "NR"));
            } else
            {
                modelRoot.put("error", "0");
                OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
            }
            info.close();
            modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
            super.retTemplate(resp,"Gestion/InfoUsuario/InfoBasicaRut.htm",modelRoot);
        }
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg)
    {
        try
        {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}