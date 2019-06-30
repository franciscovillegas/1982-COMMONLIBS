package organica.com.eje.ges.gestion.mantencion;

import java.io.IOException;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package organica.com.eje.ges.gestion.mantencion:
//            InsertaHojaVida, Anotaciones, MakeObs

public class MantenedorHojaVidaDF extends MyHttpServlet
{

    public MantenedorHojaVidaDF()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Mantenedor");
        OutMessage.OutMessagePrint("CargaPagina");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        OutMessage.OutMessagePrint("usuario leido");
        Mant = new InsertaHojaVida();
        OutMessage.OutMessagePrint("template");
        String consulta = null;
        GregorianCalendar Fecha = new GregorianCalendar();
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        java.util.Date date2 = Fecha.getTime();
        String r = user.getRutConsultado();
        OutMessage.OutMessagePrint("rut de la sesion : ".concat(String.valueOf(String.valueOf(r))));
        if(!user.esValido())
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        } else
        {
            modelRoot.put("fecha", valida.validarFecha(date2));
            Consulta info = new Consulta(conexion);
            consulta = "SELECT * from eje_ges_hoja_vida_tipoobs ";
            info.exec(consulta);
            modelRoot.put("tiposobs", info.getSimpleList());
            if(req.getParameter("buscar") != null && req.getParameter("rut") != null)
                if(control.tienePermisoJerarquico(conexion, "df_exp_remu", req.getParameter("rut")))
                {
                    userRut = new Rut(conexion, req.getParameter("rut").toString());
                    if(userRut.valido)
                    {
                        modelRoot.put("rut", req.getParameter("rut"));
                        modelRoot.put("rutf", String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(userRut.Rut))))).append("-").append(userRut.Digito_Ver)));
                        modelRoot.put("nombre", userRut.Nombres);
                        modelRoot.put("cargo", userRut.Cargo);
                        modelRoot.put("anexo", userRut.Anexo);
                        modelRoot.put("email", userRut.Email);
                        modelRoot.put("mail", userRut.Mail);
                        modelRoot.put("ubicacion", userRut.UbicFisica);
                        modelRoot.put("unidad", userRut.Unidad);
                        modelRoot.put("foto", userRut.Foto);
                        notas = new Anotaciones();
                        modelRoot.put("anotaciones", notas.getAnotacionesAll(conexion, req.getParameter("rut").toString(), userRut.Empresa));
                    } else
                    {
                        modelRoot.put("mensaje", "Rut NO existe, intente nuevamente");
                    }
                } else
                {
                    modelRoot.put("mensaje", "Usted No esta autorizado a asignar anotaciones al empleado");
                }
            if(req.getParameter("graba") != null)
                if(!req.getParameter("rut1").trim().equals(user.getRutUsuario()))
                {
                    Mant = new InsertaHojaVida();
                    if(Mant.InsertaHojaVida(conexion, valida.validarFecha(date2, "mm/dd/yyyy"), user.getEmpresa(), req.getParameter("rut1"), user.getRutUsuario(), req.getParameter("tipo"), req))
                    {
                        userRut = new Rut(conexion, req.getParameter("rut1").toString());
                        modelRoot.put("rut", req.getParameter("rut1"));
                        modelRoot.put("rutf", String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(userRut.Rut))))).append("-").append(userRut.Digito_Ver)));
                        modelRoot.put("nombre", userRut.Nombres);
                        modelRoot.put("cargo", userRut.Cargo);
                        modelRoot.put("anexo", userRut.Anexo);
                        modelRoot.put("email", userRut.Email);
                        modelRoot.put("mail", userRut.Mail);
                        modelRoot.put("ubicacion", userRut.UbicFisica);
                        modelRoot.put("unidad", userRut.Unidad);
                        modelRoot.put("foto", userRut.Foto);
                        notas = new Anotaciones();
                        modelRoot.put("anotaciones", notas.getAnotacionesAll(conexion, req.getParameter("rut1").toString(), userRut.Empresa));
                        modelRoot.put("mensaje", "La Operaci\363n se ralizo con \351xito");
                    } else
                    {
                        modelRoot.put("mensaje", "La Operaci\363n Fallo, intente nuevamente");
                    }
                } else
                {
                    modelRoot.put("mensaje", "La Operaci\363n Fallo, Usted es el Supervisor");
                }
            info.close();
            super.retTemplate(resp,"Gestion/hojavida/mantencion/mante_hojavida.htm",modelRoot);
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
        super.retTemplate(resp,"Gestion/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private InsertaHojaVida Mant;
    private Rut userRut;
    private Tools tool;
    private Anotaciones notas;
    private MakeObs obs;
}