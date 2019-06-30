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
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.FormatoFecha;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.formularios.beneficios:
//            BenefManager

public class S_AdmBeneficios extends MyHttpServlet
{

    public S_AdmBeneficios()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
        {
            Usuario user = SessionMgr.rescatarUsuario(req);
            if(!user.esValido())
                super.mensaje.devolverPaginaSinSesion(resp, "Administrar Beneficios", "");
            else
            if(!user.tieneApp("op_adm_mod_beneficios"))
            {
                super.mensaje.devolverPaginaMensage(resp, "Administrar Beneficios", "No tiene permiso...");
            } else
            {
                String accion = req.getParameter("accion");
                if("update".equals(accion))
                    retPaginaUpdate(user, req, resp, Conexion, null, -1);
                else
                if("delete".equals(accion))
                    retPaginaDelete(user, req, resp, Conexion, null);
                else
                    retPagina(user, req, resp, Conexion, null, false);
            }
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas Tecnicos", "Errores en la Conexion.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
        {
            Usuario user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
                guardaDatos(user, req, resp, Conexion, null);
            else
                super.mensaje.devolverPaginaSinSesion(resp, "Administrar Beneficios", "");
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas Tecnicos", "Errores en la Conexion.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    private void retPagina(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection conexion, String msg, boolean resulOk)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
            String pHtm = "beneficios/admBeneficios.htm";
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("msg", msg);
            modelRoot.put("accion", "new");
            modelRoot.put("ok", resulOk);
            retTemplate(resp, pHtm, modelRoot);
            OutMessage.OutMessagePrint("******* Fin *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPagina en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
        }
    }

    private void retPaginaMsg(HttpServletRequest req, HttpServletResponse resp, String titulo, String msg, boolean resulOk)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
            String pHtm = "pag/mensaje.htm";
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("mensaje", msg);
            modelRoot.put("titulo", titulo);
            modelRoot.put("ok", resulOk);
            retTemplate(resp, pHtm, modelRoot);
            OutMessage.OutMessagePrint("******* Fin *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPaginaMsg en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
        }
    }

    private void retPaginaUpdate(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection conexion, String msg, int pId)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
            BenefManager bMgr = new BenefManager(conexion);
            Validar valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            if(pId == -1)
                pId = valida.validarNum(req.getParameter("beneficio"), 0);
            String pHtm = "beneficios/admBeneficios.htm";
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("msg", msg);
            modelRoot.put("accion", "update");
            Consulta consul = bMgr.getBeneficio(pId);
            modelRoot.put("benef", consul.getSimpleHash());
            consul.close();
            retTemplate(resp, pHtm, modelRoot);
            OutMessage.OutMessagePrint("******* Fin *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPaginaUpdate; en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
        }
    }

    private void retPaginaDelete(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection conexion, String msg)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
            OutMessage.OutMessagePrint("******* Fin *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPaginaUpdate; en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
        }
    }

    private void guardaDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection conexion, String msg)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
            BenefManager bMgr = new BenefManager(conexion);
            Validar valida = new Validar();
            boolean seguir = true;
            boolean resulOk = false;
            String pAccion = req.getParameter("accion");
            int pId = valida.validarNum(req.getParameter("beneficio"), 0);
            String pGlosa = req.getParameter("glosa");
            String pVigente = req.getParameter("vigente");
            if("update".equals(pAccion))
                seguir = bMgr.updateBeneficio(pId, pGlosa, pVigente);
            else
            if("new".equals(pAccion))
            {
                pId = bMgr.addBeneficio(pGlosa, pVigente);
                seguir = pId != -1;
            }
            if(seguir)
            {
                msg = "Beneficio guardado con exito";
                resulOk = true;
            } else
            {
                getServletContext().log(bMgr.getError(), bMgr.getException());
                msg = "Error: ".concat(String.valueOf(String.valueOf(bMgr.getError())));
            }
            if(resulOk)
                retPaginaUpdate(user, req, resp, conexion, msg, pId);
            else
                retPaginaMsg(req, resp, "Guardar P\341gina", msg, resulOk);
            OutMessage.OutMessagePrint("******* Fin *******".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPagina en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
        }
    }
}