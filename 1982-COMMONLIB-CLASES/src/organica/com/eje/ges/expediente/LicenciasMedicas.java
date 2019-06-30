package organica.com.eje.ges.expediente;

import java.io.IOException;
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
import freemarker.template.SimpleList;

public class LicenciasMedicas extends MyHttpServlet
{

    public LicenciasMedicas()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
    	user = Usuario.rescatarUsuario(req);
        portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de LicenciasMedicas");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        String strCuales = req.getParameter("cuales");
        String strAgrupar = req.getParameter("agrupar");
        if(strCuales == null)
            strCuales = "T";
        if(strAgrupar == null)
            strAgrupar = "G";
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Licencias M\351dicas", "Tiempo de Sesi\363n expirado...");
        else
        if(!userPortal.tieneApp(PermisoPortal.BOTON_GESTION))
        {
            mensaje.devolverPaginaMensage(resp, "Licencias M\351dicas", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto,anexo,e_mail, id_unidad, unidad,area,division FROM view_ges_InfoRut  WHERE (rut = " + strRut + ")";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Licencias M\351dicas", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", strRut);
                modelRoot.put("anexo", consul.getString("anexo"));
                modelRoot.put("mail", consul.getString("e_mail"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("id_unidad", consul.getString("id_unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                Consulta licencias = new Consulta(conexion);
                sql = "SELECT * FROM eje_ges_licencias_medicas WHERE (rut = " + strRut + ") ";
                if(strCuales.equalsIgnoreCase("F"))
                {
                    String strFechaDesde = req.getParameter("MesD") + "/" + req.getParameter("DiaD") + "/" + req.getParameter("AnoD");
                    String strFechaHasta = req.getParameter("MesH") + "/" + req.getParameter("DiaH") + "/" + req.getParameter("AnoH");
                    sql = sql + " and (fecha_inicio >= '" + strFechaDesde + "' and fecha_inicio <= '" + strFechaHasta + "')";
                }
                sql = sql + " order by fecha_inicio desc";
                if(strAgrupar.equalsIgnoreCase("G"))
                {
                    sql = sql + " ,grupo_enfermedad";
                    modelRoot.put("opcion", "G");
                } else
                {
                    sql = sql + " , profesional";
                    modelRoot.put("opcion", "P");
                }
                OutMessage.OutMessagePrint("--> " + sql);
                licencias.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; licencias.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("inicio", valida.validarFecha(licencias.getValor("fecha_inicio")));
                    simplehash1.put("termino", valida.validarFecha(licencias.getValor("fecha_termino")));
                    simplehash1.put("tipo_licencia", licencias.getString("tipo_licencia"));
                    simplehash1.put("diagnostico", licencias.getString("diagnostico"));
                    simplehash1.put("dias", licencias.getString("dias"));
                    simplehash1.put("grupo", licencias.getString("grupo_enfermedad"));
                    simplehash1.put("rut_profesional", licencias.getString("rut_profesional"));
                    simplehash1.put("profesional", licencias.getString("profesional"));
                    simplehash1.put("espe_profesional", licencias.getString("espe_profesional"));
                }

                modelRoot.put("licencias", simplelist);
                licencias.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/DetalleLicencias.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}