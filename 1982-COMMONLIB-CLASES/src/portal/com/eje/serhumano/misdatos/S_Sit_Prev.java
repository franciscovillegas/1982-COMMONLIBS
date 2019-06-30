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

public class S_Sit_Prev extends MyHttpServlet
{

    public S_Sit_Prev()
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
            insTracking(req, "Situación Previsional".intern(), null);
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
            super.mensaje.devolverPaginaSinSesion(resp, "Situaci\363n Previsional", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String Ncargas = null;
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Validar valida = new Validar();
            Prev_Manager dataprev = new Prev_Manager(Conexion);
            Consulta cargas = dataprev.GetCargas(rut);
            cargas.next();
            Ncargas = cargas.getString("cargas");
            cargas.close();
            Consulta datos = dataprev.GetDataPrevisional(rut);
            datos.next();
            modelRoot.put("foto", datos.getString("id_foto"));
            modelRoot.put("nombre", valida.validarDato(datos.getString("nombre"), "NR"));
            modelRoot.put("cargo", valida.validarDato(datos.getString("cargo"), "NR"));
            modelRoot.put("afp", valida.validarDato(datos.getString("afp"), "NR"));
            modelRoot.put("fec_afilia", valida.validarFecha(datos.getValor("fec_afi_sist")));
            modelRoot.put("ingre_afp", valida.validarFecha(datos.getValor("fec_ing_afp")));
            modelRoot.put("cotizacion", valida.validarDato(datos.getString("cot_afp"), "NR"));
            modelRoot.put("monto_adicional", valida.validarDato(datos.getString("cot_adic"), "NR"));
            modelRoot.put("moneda_adicional", valida.validarDato(datos.getString("mo_cot_adic"), ""));
            modelRoot.put("monto_volunt", valida.validarDato(datos.getString("ah_volunt"), "NR"));
            modelRoot.put("moneda_volunt", valida.validarDato(datos.getString("mo_ah_volunt"), ""));
            modelRoot.put("jubilado", valida.validarDato(datos.getString("jubilado"), ""));
            modelRoot.put("monto_deposito", valida.validarDato(datos.getString("dep_conven"), "NR"));
            modelRoot.put("moneda_deposito", valida.validarDato(datos.getString("mon_dep_conven"), "NR"));
            modelRoot.put("afp_historica", valida.validarDato(datos.getString("afp_histo"), "NR"));
            modelRoot.put("isapre", valida.validarDato(datos.getString("isapre"), "NR"));
            modelRoot.put("ing_isap", valida.validarFecha(datos.getValor("fec_ing_isap")));
            modelRoot.put("plan_sal", valida.validarDato(datos.getString("plan_salud"), "NR"));
            modelRoot.put("fec_consa", valida.validarFecha(datos.getValor("fec_con_salud")));
            modelRoot.put("fec_venc", valida.validarFecha(datos.getValor("venc_salud")));
            modelRoot.put("mon_plan", valida.validarDato(datos.getString("mon_salud"), "NR"));
            modelRoot.put("cotisap", valida.validarDato(datos.getString("cot_salud"), "NR"));
            modelRoot.put("adicsal", valida.validarDato(datos.getString("adic_salud"), "NR"));
            modelRoot.put("mone_adicsal", valida.validarDato("", "NR"));
            modelRoot.put("isap_histo", valida.validarDato(datos.getString("isap_histo"), "NR"));
            modelRoot.put("camb_plan", valida.validarDato(datos.getString("cambio_plan"), "NR"));
            modelRoot.put("mail", valida.validarDato(datos.getString("e_mail"), "NR"));
            modelRoot.put("anexo", valida.validarDato(datos.getString("anexo"), "NR"));
            modelRoot.put("rut2", rut);
            modelRoot.put("rut", rut);
            modelRoot.put("unidad", valida.validarDato(datos.getString("unidad"), "NR"));
            modelRoot.put("numcargas", valida.validarDato(Ncargas, "0"));
            modelRoot.put("cargas", simplelist);
            datos.close();
            super.retTemplate(resp,"misdatos/situacion_prev.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private Tools tool;
}