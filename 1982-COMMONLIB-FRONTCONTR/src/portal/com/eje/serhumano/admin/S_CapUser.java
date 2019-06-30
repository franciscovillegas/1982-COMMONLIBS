package portal.com.eje.serhumano.admin;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.web.FreemakerTool;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.admin.configdf.UserManager;
import portal.com.eje.serhumano.admin.configqs.QSUserManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.FormatoFecha;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.admin:
//            CapManager

public class S_CapUser extends MyHttpServlet
{

    public S_CapUser()
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
//    	MMA 20170112
//        Connection ConDf = super.connMgr.getConnection("portal");
//        Connection ConWf = super.connMgr.getConnection("portal");
//        Connection ConSh = super.connMgr.getConnection("portal");
//        Connection ConQSDiario = super.connMgr.getConnection("qs_diario");
//        Connection ConQSNoticias = super.connMgr.getConnection("qs_noticias");
//        Connection ConQSRevista = super.connMgr.getConnection("qs_revista");
//        Connection ConQSBenef = super.connMgr.getConnection("qs_benef");
	    Connection ConDf = getConnMgr().getConnection("portal");
	    Connection ConWf = getConnMgr().getConnection("portal");
	    Connection ConSh = getConnMgr().getConnection("portal");
        Connection ConQSDiario = null; //super.connMgr.getConnection("qs_diario");
        Connection ConQSNoticias = null; //super.connMgr.getConnection("qs_noticias");
        Connection ConQSRevista = null; //super.connMgr.getConnection("qs_revista");
        Connection ConQSBenef = null; //super.connMgr.getConnection("qs_benef");
        if(ConDf != null && ConSh != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
            {
                if(user.tieneApp("adm"))
                    MuestraDatos(user, req, resp, ConDf, ConSh, ConWf, ConQSDiario, ConQSNoticias, ConQSRevista, ConQSBenef);
                else
                    super.mensaje.devolverPaginaMensage(resp, "Cap", "No tiene permiso...");
            } else
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Cap", "Tiempo de Sesi\363n expirado...");
            }
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
//    	MMA 20170112
//        super.connMgr.freeConnection("portal", ConDf);
//        super.connMgr.freeConnection("portal", ConSh);
//        super.connMgr.freeConnection("portal", ConWf);
//        super.connMgr.freeConnection("qs_diario", ConQSDiario);
//        super.connMgr.freeConnection("qs_noticias", ConQSNoticias);
//        super.connMgr.freeConnection("qs_revista", ConQSRevista);
//        super.connMgr.freeConnection("qs_benef", ConQSBenef);
        getConnMgr().freeConnection("portal", ConDf);
        getConnMgr().freeConnection("portal", ConSh);
        getConnMgr().freeConnection("portal", ConWf);
        getConnMgr().freeConnection("qs_diario", ConQSDiario);
        getConnMgr().freeConnection("qs_noticias", ConQSNoticias);
        getConnMgr().freeConnection("qs_revista", ConQSRevista);
        getConnMgr().freeConnection("qs_benef", ConQSBenef);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection ConDf, Connection Consh, Connection ConWf, Connection ConQSDiario, 
            Connection ConQSNoticias, Connection ConQSRevista, Connection ConQSBenef)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n************Entro al doPost de CAP");
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("FF", new FormatoFecha());
            Validar valida = new Validar();
            String pRut = req.getParameter("pRut");
            String pDig = req.getParameter("dig");
            String pNombre = req.getParameter("nombre");
            String ps = req.getParameter("pass1");
            String ps_ant = req.getParameter("pass_ant");
            CapManager cm = new CapManager(Consh);
            String grabar = req.getParameter("grabar");
            String m_pass = "";
            String m_elim = "";
            m_elim = "Usuario se Elimina de : <br>";
            modelRoot.put("pass", "");
            modelRoot.put("nuevo", "si");
            UserManager um = new UserManager(ConDf);
            QSUserManager umDiario = new QSUserManager(ConQSDiario);
            QSUserManager umNot = new QSUserManager(ConQSNoticias);
            QSUserManager umRev = new QSUserManager(ConQSRevista);
            QSUserManager umBenef = new QSUserManager(ConQSBenef);
            if(pRut != null && !"".equals(pRut))
            {
                if(grabar != null && !"".equals(grabar) && "si".equals(grabar))
                {
                    int checks_largo = Integer.parseInt(valida.validarDato(req.getParameter("cant_app"), "14"));
                    cm.AddUser(pRut, Integer.parseInt(pRut), req.getParameter("pass1"));
                    umDiario.addUser(pRut, ps, pNombre, Integer.parseInt(pRut), pDig);
                    umNot.addUser(pRut, ps, pNombre, Integer.parseInt(pRut), pDig);
                    umRev.addUser(pRut, ps, pNombre, Integer.parseInt(pRut), pDig);
                    int sw = 0;
                    int sw2 = 0;
                    String app = "";
                    m_pass = "Usuario se Creo en :<br>";
                    for(int x = 1; x <= checks_largo; x++)
                    {
                        app = req.getParameter("checkbox_".concat(String.valueOf(String.valueOf(x))));
                        if(app != null)
                        {
                            if(cm.AddUserApp(Integer.parseInt(pRut), app))
                            {
                                if("sh".equals(app))
                                {
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Ser Humano<br>");
                                    sw = 1;
                                }
                                if("wfv".equals(app))
                                {
                                    if(cm.AddUserWorkFlow(Integer.parseInt(pRut), app, ConWf))
                                    {
                                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - WorkFlow Vacaciones<br>");
                                        sw = 1;
                                    }
                                } else
                                if("anex".equals(app))
                                {
                                    if(cm.AddUserAnexos(Integer.parseInt(pRut), app, ConDf))
                                    {
                                        um.agregarAccesos(pRut, "df_mant_anexo", "N");
                                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Anexos<br>");
                                        sw = 1;
                                    }
                                } else
                                if("vig".equals(app))
                                {
                                    if(cm.AddUserVigilantes(Integer.parseInt(pRut), app, ConDf))
                                    {
                                        um.agregarAccesos(pRut, "df_vigilante", "N");
                                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Vigilantes<br>");
                                        sw = 1;
                                    }
                                } else
                                if("cert".equals(app))
                                {
                                    if(cm.AddUserCert(Integer.parseInt(pRut), app, ConDf))
                                    {
                                        um.agregarAccesos(pRut, "df_certif", "N");
                                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Documentaci\363n<br>");
                                        sw = 1;
                                    }
                                } else
                                if("df".equals(app))
                                {
                                    if(cm.AddUserDataFolder(Integer.parseInt(pRut), app, ConDf))
                                    {
                                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - DataFolder<br>");
                                        sw = 1;
                                    }
                                } else
                                if("pub".equals(app))
                                {
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Ser Humano Publicaciones<br>");
                                    sw = 1;
                                } else
                                if("adm".equals(app))
                                {
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Ser Humano Administrador<br>");
                                    sw = 1;
                                } else
                                if("mis".equals(app))
                                {
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - RRHH. MIS<br>");
                                    sw = 1;
                                } else
                                if("pub_dia".equals(app))
                                {
                                    umDiario.setTipo(pRut, "A");
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Publicador Diario Mural<br>");
                                    sw = 1;
                                } else
                                if("pub_not".equals(app))
                                {
                                    umNot.setTipo(pRut, "A");
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Publicador Noticias<br>");
                                    sw = 1;
                                } else
                                if("pub_rev".equals(app))
                                {
                                    umRev.setTipo(pRut, "A");
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Publicador Revista Inst.<br>");
                                    sw = 1;
                                } else
                                if("adm_benef".equals(app))
                                {
                                    umBenef.setTipo(pRut, "A");
                                    m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Publicador Beneficios<br>");
                                    sw = 1;
                                }
                            }
                        } 
                        
                    }

                    if(sw == 1)
                        modelRoot.put("addok", m_pass);
                    if(sw2 == 1)
                        modelRoot.put("delok", m_elim);
                }
                if(ps != null && ps_ant != null && !ps.equals(ps_ant) && !"".equals(ps_ant))
                {
                    m_pass = "La Clave se actualiz\363 en :<br>";
                    if(cm.UpdateUser(pRut, ps))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - SerHumano<br>");
                    if(cm.UpdateUserAnexos(Integer.parseInt(pRut), "anex", ConDf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Anexos<br>");
                    if(cm.UpdateUserVigilantes(Integer.parseInt(pRut), "vig", ConDf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Vigilantes<br>");
                    if(cm.UpdateUserCert(Integer.parseInt(pRut), "cert", ConDf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Documentaci\363n<br>");
                    if(cm.UpdateUserWorkFlow(Integer.parseInt(pRut), "wfv", ConWf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - WorkFlow Vacaciones<br>");
                    if(cm.UpdateUserDatafolder(Integer.parseInt(pRut), "df", ConDf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - DataFolder<br>");
                    if(cm.UpdateUserDatafolder(Integer.parseInt(pRut), "mis", ConDf))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - RRHH. MIS<br>");
                    if(umDiario.updateUserPassword(pRut, ps))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Diario Mural<br>");
                    if(umNot.updateUserPassword(pRut, ps))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Noticias<br>");
                    if(umRev.updateUserPassword(pRut, ps))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Revista Inst.<br>");
                    if(umBenef.updateUserPassword(pRut, ps))
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Beneficios Inst.<br>");
                    modelRoot.put("passok", m_pass);
                }
                FreemakerTool tool = new FreemakerTool();
                
                modelRoot.put("app", tool.getListData(cm.getApp(pRut)));
                modelRoot.put("user", pRut);
                modelRoot.put("dig", cm.getDigRut(pRut, ConDf));
                modelRoot.put("nombre", cm.getNombregRut(pRut, ConDf));
                Consulta consul = cm.getDatosUser(pRut);
                if(consul.next())
                {
                    modelRoot.put("pass", consul.getString("password_usuario"));
                    modelRoot.put("fecha", consul.getString("passw_ult_cambio"));
                    modelRoot.put("nuevo", "no");
                }
                consul.close();
            }
            super.retTemplate(resp,"admin/cap.htm",modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost Cap");
    }

    private Usuario user;
    private ResourceBundle proper;
}