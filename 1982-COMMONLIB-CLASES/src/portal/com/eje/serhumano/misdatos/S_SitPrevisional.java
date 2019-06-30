package portal.com.eje.serhumano.misdatos;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.Periodo;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.FormatoNumero;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.misdatos:
//            Prev_Manager

public class S_SitPrevisional extends MyHttpServlet
{

    public S_SitPrevisional()
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

        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Situaci\363n Previsional", "Tiempo de Sesi\363n expirado...");
        } else
        {
            GregorianCalendar Fecha = new GregorianCalendar();
            int dia = Fecha.get(5);
            int ano = Fecha.get(1);
            int mes = Fecha.get(2) + 1;
            if(rut == null)
                rut = user.getRutId();
            SimpleHash modelRoot = new SimpleHash();
            SimpleList periodos = new SimpleList();
            SimpleList listaafp = new SimpleList();
            SimpleList listasalud = new SimpleList();
            Validar valida = new Validar();
            datosRut dataRut = new datosRut(Conexion, user.getRutId());
            modelRoot.put("empresa", dataRut.EmpresaDescrip);
            modelRoot.put("rut_trab", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(user.getRutId())))))).append("-").append(dataRut.Digito_Ver))));
            modelRoot.put("nombre", dataRut.Nombres);
            modelRoot.put("ccosto", dataRut.Unidad);
            modelRoot.put("ingreso", dataRut.FecIngreso);
            Prev_Manager dataprev = new Prev_Manager(Conexion);
            Consulta datos2 = null;
            Consulta datos = dataprev.GetPeriodos(dataRut.Rut, dataRut.Empresa, dataRut.Id_Unidad);
            Periodo peri = null;
            SimpleHash simplehash1;
            for(; datos.next(); periodos.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                System.err.println("!!!Iterando Periodos!!!");
                peri = new Periodo(Conexion, datos.getString("periodo"));
                simplehash1.put("peri", peri.getPeriodoPalabrasSimple());
                datos2 = dataprev.GetDataFondoPension(dataRut.Rut, dataRut.Empresa, dataRut.Id_Unidad, datos.getString("periodo"));
                listaafp = new SimpleList();
                if(datos2.next())
                {
                    SimpleHash simplehash2 = new SimpleHash();
                    simplehash2.put("afp", valida.validarDato(datos2.getString("glosa_descuento")));
                    simplehash2.put("mon1", valida.validarDato(datos2.getString("val_descuento")));
                    listaafp.add(simplehash2);
                }
                datos2 = dataprev.GetDataSalud(dataRut.Rut, dataRut.Empresa, dataRut.Id_Unidad, datos.getString("periodo"));
                listasalud = new SimpleList();
                if(datos2.next())
                {
                    SimpleHash simplehash3 = new SimpleHash();
                    simplehash3.put("sal", valida.validarDato(datos2.getString("glosa_descuento")));
                    simplehash3.put("mon2", valida.validarDato(datos2.getString("val_descuento")));
                    listasalud.add(simplehash3);
                }
                simplehash1.put("listasalud", listasalud);
                simplehash1.put("listaafp", listaafp);
            }

            modelRoot.put("datos", periodos);
            String strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("hoy", strFechaHoy);
            modelRoot.put("FNum", new FormatoNumero());
            datos.close();
            datos2.close();
            super.retTemplate(resp,"misdatos/sit_prev.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private Tools tool;
}