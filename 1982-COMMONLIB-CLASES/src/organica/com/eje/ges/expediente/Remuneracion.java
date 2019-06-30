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

public class Remuneracion extends MyHttpServlet
{

    public Remuneracion()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Remuneracion");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Remuneraci\363n", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_remu", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Remuneraci\363n", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, digito_ver,nombre,anexo,e_mail, cargo, fecha_ingreso, id_foto, dias_trab, total_haberes, tot_descuento, liquido, rango_desde, rango_hasta, rango_concepto, rango_situacion, sueldo, forma_pago, bono_gestion, movilizacion, bono_zona, asig_caja, bono_antig, rta_variable, traslado, bruto_regular, neto_regular, bono_anual,  bruto_zona, neto_zona, bruto_promedio, neto_promedio, bruto_total, neto_total FROM view_ges_InfoRut  WHERE (rut = " + strRut + ")";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Remuneraci\363n", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", consul.getString("rut"));
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("sueldo", tool.setFormatNumber(valida.validarDato(consul.getString("sueldo"), "0")));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("bono_ges", tool.setFormatNumber(consul.getInt("bono_gestion")));
                modelRoot.put("movili", tool.setFormatNumber(consul.getInt("movilizacion")));
                modelRoot.put("bono_zona", tool.setFormatNumber(consul.getInt("bono_zona")));
                modelRoot.put("asig_caja", tool.setFormatNumber(consul.getInt("asig_caja")));
                modelRoot.put("bono_antig", tool.setFormatNumber(consul.getInt("bono_antig")));
                modelRoot.put("rta_var", tool.setFormatNumber(consul.getInt("rta_variable")));
                modelRoot.put("traslado", tool.setFormatNumber(consul.getInt("traslado")));
                modelRoot.put("bruto_regular", tool.setFormatNumber(consul.getInt("bruto_regular")));
                modelRoot.put("neto_regular", tool.setFormatNumber(consul.getInt("neto_regular")));
                modelRoot.put("bruto_zona", tool.setFormatNumber(consul.getInt("bruto_zona")));
                modelRoot.put("neto_zona", tool.setFormatNumber(consul.getInt("neto_zona")));
                modelRoot.put("bruto_prom", tool.setFormatNumber(consul.getInt("bruto_promedio")));
                modelRoot.put("neto_prom", tool.setFormatNumber(consul.getInt("neto_promedio")));
                modelRoot.put("bruto_total", tool.setFormatNumber(consul.getInt("bruto_total")));
                modelRoot.put("neto_total", tool.setFormatNumber(consul.getInt("neto_total")));
                modelRoot.put("bono_anual", tool.setFormatNumber(consul.getInt("bono_anual")));
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT periodo,corr, benef_rut, benef_nom, monto, moneda FROM eje_ges_retencion_judicial_historia WHERE (rut = " + strRut + ") ";
                OutMessage.OutMessagePrint("--> " + sql);
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("num", valida.validarDato(consul2.getString("corr")));
                    simplehash1.put("benef_rut", valida.validarDato(consul2.getString("benef_rut")));
                    simplehash1.put("benef_nom", valida.validarDato(consul2.getString("benef_nom")));
                    simplehash1.put("monto", valida.validarDato(consul2.getValor("monto")));
                    simplehash1.put("moneda", valida.validarDato(consul2.getString("moneda")));
                }

                modelRoot.put("retencion", simplelist);
                consul2.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/Remuneracion.htm",modelRoot);
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