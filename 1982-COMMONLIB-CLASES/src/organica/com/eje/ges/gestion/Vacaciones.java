package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
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

public class Vacaciones extends MyHttpServlet
{

    public Vacaciones()
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
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null || rut.equals(""))
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Vacaciones", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_exp_vaca", rut))
        {
            mensaje.devolverPaginaMensage(resp, "Vacaciones", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            SimpleHash modelRoot = new SimpleHash();
            OutMessage.OutMessagePrint("Entro a Mostrar datos!!!!");
            Consulta vacaciones = new Consulta(Conexion);
            Validar valida = new Validar();
            Consulta info = new Consulta(Conexion);
            String sql = "SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto,anexo,e_mail,unidad,area,division,hoy,fecha_ingreso,recono,fecrecono FROM view_ges_InfoRut  WHERE (rut = '" + rut + "')";
            OutMessage.OutMessagePrint("consulta: " + sql);
            info.exec(sql);
            info.next();
            modelRoot.put("rut2", Tools.setFormatNumber(info.getString("rut")) + "-" + info.getString("digito_ver"));
            modelRoot.put("rut", info.getString("rut"));
            modelRoot.put("hoy", valida.validarFecha(info.getValor("hoy")));
            modelRoot.put("nombre", info.getString("nombre"));
            modelRoot.put("cargo", info.getString("cargo"));
            modelRoot.put("fec_ingreso", valida.validarFecha(info.getValor("fecha_ingreso")));
            modelRoot.put("anos", valida.validarDato(info.getString("recono"), "0"));
            modelRoot.put("fec", valida.validarFecha(info.getValor("fecrecono")));
            modelRoot.put("anexo", valida.validarDato(info.getString("anexo"), "NR"));
            modelRoot.put("mail", valida.validarDato(info.getString("e_mail"), "NR"));
            modelRoot.put("foto", info.getString("id_foto"));
            info.close();
            consulta = "select pen_normal,pen_progresivo,pen_convenio,der_normal,der_progresivo,der_convenio,uti_normal,uti_progresivo,uti_convenio,pen_normal,pen_progresivo,pen_convenio,prog_vendido,periodo,bono_normal,bono_pen_normal,bono_convenio,bono_pen_convenio from eje_ges_vacaciones where rut = " + rut;
            OutMessage.OutMessagePrint(consulta);
            vacaciones.exec(consulta);
            SimpleList simplelist = new SimpleList();
            int nsaldo = 0;
            int nsaldo_N = 0;
            int nsaldo_P = 0;
            int nsaldo_C = 0;
            SimpleHash simplehash1;
            for(; vacaciones.next(); simplelist.add(simplehash1))
            {
                int nsaldo_per = 0;
                nsaldo_per = vacaciones.getInt("pen_normal") + vacaciones.getInt("pen_progresivo") + vacaciones.getInt("pen_convenio");
                nsaldo += nsaldo_per;
                nsaldo_N += vacaciones.getInt("pen_normal");
                nsaldo_P += vacaciones.getInt("pen_progresivo");
                nsaldo_C += vacaciones.getInt("pen_convenio");
                simplehash1 = new SimpleHash();
                simplehash1.put("pe", vacaciones.getString("periodo"));
                simplehash1.put("dn", valida.validarDato(vacaciones.getString("der_normal"), "0"));
                simplehash1.put("dp", valida.validarDato(vacaciones.getString("der_progresivo"), "0"));
                simplehash1.put("dc", valida.validarDato(vacaciones.getString("der_convenio"), "0"));
                simplehash1.put("un", valida.validarDato(vacaciones.getString("uti_normal"), "0"));
                simplehash1.put("up", valida.validarDato(vacaciones.getString("uti_progresivo"), "0"));
                simplehash1.put("uc", valida.validarDato(vacaciones.getString("uti_convenio"), "0"));
                simplehash1.put("pn", valida.validarDato(vacaciones.getString("pen_normal"), "0"));
                simplehash1.put("pp", valida.validarDato(vacaciones.getString("pen_progresivo"), "0"));
                simplehash1.put("pc", valida.validarDato(vacaciones.getString("pen_convenio"), "0"));
                simplehash1.put("s", (new Integer(nsaldo_per)).toString());
                simplehash1.put("dpv", valida.validarDato(vacaciones.getString("prog_vendido"), "0"));
                simplehash1.put("bn", valida.validarDato(vacaciones.getString("bono_normal"), "0"));
                simplehash1.put("mn", valida.validarDato(vacaciones.getString("bono_pen_normal"), "0"));
                simplehash1.put("bc", valida.validarDato(vacaciones.getString("bono_convenio"), "0"));
                simplehash1.put("mc", valida.validarDato(vacaciones.getString("bono_pen_convenio"), "0"));
            }

            modelRoot.put("nsaldo", Tools.setFormatNumber(nsaldo));
            modelRoot.put("nsaldo_n", Tools.setFormatNumber(nsaldo_N));
            modelRoot.put("nsaldo_p", Tools.setFormatNumber(nsaldo_P));
            modelRoot.put("nsaldo_c", Tools.setFormatNumber(nsaldo_C));
            modelRoot.put("vacaciones", simplelist);
            consulta = "SELECT dia,monto, provision FROM eje_ges_provision_vacaciones WHERE rut = " + rut;
            vacaciones.exec(consulta);
            if(vacaciones.next())
                modelRoot.put("prov", valida.validarDato(Tools.setFormatNumber((vacaciones.getInt("dia") * vacaciones.getInt("monto")) / 30), "0"));
            else
                modelRoot.put("prov", "0");
            vacaciones.close();
            modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
            super.retTemplate(resp,"Gestion/InfoUsuario/Vacaciones.htm",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg)
    {
        try
        {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}