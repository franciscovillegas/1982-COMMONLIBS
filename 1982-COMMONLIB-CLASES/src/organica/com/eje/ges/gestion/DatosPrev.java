package organica.com.eje.ges.gestion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class DatosPrev extends MyHttpServlet
{

    public DatosPrev()
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
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
    	user = Usuario.rescatarUsuario(req);
        portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
        
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null || rut.equals(""))
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Datos Previsionales", "Tiempo de Sesi\363n expirado...");
        else
        if(!userPortal.tieneApp(PermisoPortal.BOTON_GESTION))
        {
            mensaje.devolverPaginaMensage(resp, "Datos Previsionales", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            String Ncargas = null;
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Consulta detalle = new Consulta(Conexion);
            Validar valida = new Validar();
            Consulta cargas = new Consulta(Conexion);
            consulta = "select count(nombre) as cargas from eje_ges_grupo_familiar where rut=" + rut;
            cargas.exec(consulta);
            cargas.next();
            Ncargas = cargas.getString("cargas");
            cargas.close();
            Consulta datos = new Consulta(Conexion);
            consulta = "select nombre,afp,fec_afi_sist,fec_ing_afp,cargo,cot_afp,cot_adic,mo_cot_adic,ah_volunt,mo_ah_volunt,jubilado,dep_conven,mon_dep_conven,afp_histo,id_foto,isapre,fec_ing_isap,plan_salud,fec_con_salud,venc_salud,mon_salud,cot_salud,adic_salud,isap_histo,cambio_plan,e_mail,anexo,unidad from view_ges_InfoRut where rut=" + rut;
            datos.exec(consulta);
            datos.next();
            modelRoot.put("foto", datos.getString("id_foto"));
            modelRoot.put("nombre", valida.validarDato(datos.getString("nombre"), "NR"));
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
            modelRoot.put("rut2", tool.setFormatNumber(rut));
            modelRoot.put("rut", rut);
            modelRoot.put("unidad", valida.validarDato(datos.getString("unidad"), "NR"));
            modelRoot.put("numcargas", valida.validarDato(Ncargas, "0"));
            modelRoot.put("cargas", simplelist);
            datos.close();
            detalle.close();
            modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
            super.retTemplate(resp,"Gestion/InfoUsuario/DatosPrev.htm",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}