package portal.com.eje.serhumano.formularios.beneficios;

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
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.formularios.beneficios:
//            BenefManager

public class S_ListaBeneficios extends MyHttpServlet
{

    public S_ListaBeneficios()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
        {
            Usuario user = SessionMgr.rescatarUsuario(req);
            if(!user.esValido())
                super.mensaje.devolverPaginaSinSesion(resp, "Formulario Beneficios", "Tiempo de Sesi\363n expirado...");
            else
            if(!user.tieneApp("op_adm_mod_beneficios"))
                super.mensaje.devolverPaginaMensage(resp, "Administrar Beneficios", "No tiene permiso...");
            else
                generaPagina(req, resp, Conexion, user);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    protected void generaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String pHtm = "beneficios/lista_beneficios.html";
        BenefManager bMgr = new BenefManager(conexion);
        SimpleHash modelRoot = new SimpleHash();
        String pVigente = req.getParameter("vigente");
        if(pVigente == null || "".equals(pVigente))
            pVigente = "S";
        Consulta consul = bMgr.getBeneficios(pVigente);
        modelRoot.put("beneficios", consul.getSimpleList());
        consul.close();
        modelRoot.put("vigente", pVigente);
        retTemplate(resp, pHtm, modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }
}