package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_FichaBasica extends MyHttpServlet
{

    public S_FichaBasica()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
            MuestraDatos(req, resp, Conexion);
        else
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n****Entro al MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            SimpleHash simplehash1 = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Validar valida = new Validar();
            String rut[] = req.getParameterValues("rut");
            String strRut = "";
            int largo = 0;
            int x = 0;
            if(rut == null)
                largo = 0;
            else
                largo = rut.length;
            if(largo == 0)
            {
                OutMessage.OutMessagePrint("No se encontraron ruts********");
            } else
            {
                String modo = req.getParameter("modo");
                System.err.println("-------->modo= ".concat(String.valueOf(String.valueOf(modo))));
                String Xrut = "";
                for(x = 0; x < largo; x++)
                {
                    strRut = rut[x];
                    simplehash1 = new SimpleHash();
                    userRut = new datosRut(conexion, strRut);
                    Xrut = NumberFormat.getInstance(Locale.GERMAN).format(Integer.parseInt(strRut)).toString();
                    if(modo.equals("1"))
                        simplehash1.put("boton", "1");
                    else
                        simplehash1.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Xrut)))).append("-").append(userRut.Digito_Ver))));
                    simplehash1.put("xrut", strRut);
                    simplehash1.put("nombre", userRut.Nombres);
                    simplehash1.put("fono", valida.validarDato(userRut.fono));
                    simplehash1.put("empresa", userRut.Empresa);
                    simplehash1.put("unidad", userRut.Unidad);
                    simplehash1.put("id_unidad", userRut.Id_Unidad);
                    simplehash1.put("cargo", userRut.Cargo);
                    simplehash1.put("email", valida.validarDato(userRut.Email));
                    simplehash1.put("anexo", valida.validarDato(userRut.Anexo));
                    simplelist.add(simplehash1);
                }

                modelRoot.put("detalle", simplelist);
                super.retTemplate(resp,"buscar/grafica/resultado.htm",modelRoot);
            }
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("\n****FIN de MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private datosRut userRut;
    private Tools tool;
}