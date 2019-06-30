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
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Admtiempo extends MyHttpServlet
{

    public Admtiempo()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Administracion del tiempo");
        Consulta consul = new Consulta(conexion);
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Administraci\363n del Tiempo", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_adm_tiempo", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Administraci\363n del Tiempo", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String sql = "SELECT a.rut AS rut, a.digito_ver, a.nombre AS nombre, a.anexo, a.unidad, a.e_mail, a.cargo AS cargo, a.sueldo AS sueldo, a.bruto_regular AS tothaber, b.valhoras AS valor, b.num_horas AS nhoras,b.num_minutos AS nmin, a.id_foto AS foto FROM view_ges_InfoRut a LEFT OUTER JOIN eje_ges_sobretiempos b ON a.rut = b.rut WHERE (a.rut = " + strRut + ") ORDER BY b.periodo desc";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Administraci\363n del Tiempo", "Informaci\363n no disponible...");
            } else
            {
                String cifra = "";
                int x = 0;
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("sbase", tool.setFormatNumber(valida.validarDato(consul.getString("sueldo"), "0")));
                modelRoot.put("thaber", tool.setFormatNumber(valida.validarDato(consul.getString("tothaber"), "0")));
                try
                {
                    float sol = (100F * consul.getFloat("valor")) / consul.getFloat("tothaber");
                    cifra = String.valueOf(sol);
                    x = cifra.indexOf(".");
                    cifra = cifra.substring(0, x + 2);
                    modelRoot.put("impacto", cifra);
                }
                catch(ArithmeticException e)
                {
                    OutMessage.OutMessagePrint("Excepcion, enc.: " + e.getMessage());
                }
                modelRoot.put("hrsex", tool.setFormatNumber(valida.validarDato(consul.getString("nhoras"), "0")));
                modelRoot.put("minex", tool.setFormatNumber(valida.validarDato(consul.getString("nmin"), "0")));
                modelRoot.put("mtoactual", tool.setFormatNumber(valida.validarDato(consul.getString("valor"), "0")));
                modelRoot.put("foto", consul.getString("foto"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("rut", strRut);
                Tools _tmp = tool;
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("unidad", consul.getString("unidad"));
                Consulta deudas = new Consulta(conexion);
                sql = "SELECT distinct b.periodo,b.total_haberes AS tothaber,b.valhoras AS valor,b.num_horas AS nhoras,b.num_minutos AS nmin,b.tipo AS tip,eje_ges_trab_haberes_historia.monto AS sueldo,eje_ges_periodo.peri_ano,eje_ges_periodo.peri_mes FROM eje_ges_sobretiempos b LEFT OUTER JOIN eje_ges_periodo ON b.periodo = eje_ges_periodo.peri_id LEFT OUTER JOIN eje_ges_trab_haberes_historia ON b.rut = eje_ges_trab_haberes_historia.rut AND b.compania = eje_ges_trab_haberes_historia.empresa AND b.periodo = eje_ges_trab_haberes_historia.periodo WHERE (eje_ges_trab_haberes_historia.haber = '1003') AND (b.rut = " + strRut + ") " + "ORDER BY b.periodo desc";
                OutMessage.OutMessagePrint("--> " + sql);
                deudas.exec(sql);
                OutMessage.OutMessagePrint("--> val " + consul.getString("valor"));
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; deudas.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("periodo", deudas.getString("periodo"));
                    simplehash1.put("ano", deudas.getString("peri_ano"));
                    simplehash1.put("mes", deudas.getString("peri_mes"));
                    simplehash1.put("nhrs", tool.setFormatNumber(deudas.getInt("nhoras")));
                    simplehash1.put("nmin", tool.setFormatNumber(deudas.getInt("nmin")));
                    simplehash1.put("mto", tool.setFormatNumber(deudas.getInt("valor")));
                    simplehash1.put("tipo", valida.validarDato(deudas.getString("tip"), "N/R"));
                    simplehash1.put("tothab", tool.setFormatNumber(deudas.getInt("tothaber")));
                    simplehash1.put("sbases", tool.setFormatNumber(deudas.getInt("sueldo")));
                    try
                    {
                        float sol = (100F * deudas.getFloat("valor")) / deudas.getFloat("tothaber");
                        cifra = String.valueOf(sol);
                        x = cifra.indexOf(".");
                        cifra = cifra.substring(0, x + 3);
                        simplehash1.put("efec", cifra);
                    }
                    catch(ArithmeticException e)
                    {
                        OutMessage.OutMessagePrint("Excepcion, det.: " + e.getMessage());
                    }
                }

                modelRoot.put("horas", simplelist);
                deudas.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/CartolaHorasExtras.htm",modelRoot);
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