package portal.com.eje.serhumano.misdatos;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.misdatos:
//            Prev_Manager

public class S_HistoriaIsapre extends MyHttpServlet
{

    public S_HistoriaIsapre()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
        {
            generaTab(req, resp, Conexion);
            insTracking(req, "Historial Isapre".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutId();
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Datos Previsionales", "Tiempo de Sesi\363n expirado...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            SimpleList ListIsapre = new SimpleList();
            Validar valida = new Validar();
            Prev_Manager dataprev = new Prev_Manager(Conexion);
            SimpleHash simplehash1;
            Consulta detalle;
            for(detalle = dataprev.GetDetalleCargas(rut); detalle.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("nombre", detalle.getString("nombre"));
                simplehash1.put("parentesco", detalle.getString("parentesco"));
            }

            modelRoot.put("cargas", simplelist);
            simplehash1 = new SimpleHash();
            for(detalle = dataprev.GetHistoIsapre(rut); detalle.next(); ListIsapre.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("NOMBRE", detalle.getString("nombre"));
                simplehash1.put("FECHA_ING", valida.validarFecha(detalle.getValor("fecha_ing")));
            }

            modelRoot.put("ISAPRE", ListIsapre);
            detalle.close();
            super.retTemplate(resp,"misdatos/historia_isapre.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private Tools tool;
}