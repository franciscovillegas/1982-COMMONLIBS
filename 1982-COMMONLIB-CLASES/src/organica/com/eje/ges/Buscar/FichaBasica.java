package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import organica.tools.enviaCorreo;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class FichaBasica extends MyHttpServlet
{

    public FichaBasica()
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
        OutMessage.OutMessagePrint("\n************Entro al doPost de Ficha Basica");
        if(Usuario.rescatarUsuario(req).esValido())
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
                String Xrut = "";
                for(x = 0; x < largo; x++)
                {
                    strRut = rut[x];
                    simplehash1 = new SimpleHash();
                    userRut = new Rut(conexion, strRut);
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
                super.retTemplate(resp,"Gestion/BusquedaPorFiltros/resultado.htm",modelRoot);;
            }
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
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
        super.retTemplate(resp,"Gestion/BusquedaPorFiltros/mensaje.htm",modelRoot);
    }

    private Rut userRut;
    private Tools tool;
    private enviaCorreo X;
    private Mensaje mensaje;
}