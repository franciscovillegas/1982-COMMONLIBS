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

public class DatosPersonales extends MyHttpServlet
{

    public DatosPersonales()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Historial");
        user = Usuario.rescatarUsuario(req);
        String cant_ingresos = String.valueOf(user.getCantIngresos());
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Historial", "Tiempo de Sesi\363n expirado...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto, anexo, e_mail  FROM view_ges_InfoRut  WHERE (rut = " + strRut + ")";
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Historial", "Informaci\363n no Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("rut", strRut);
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("anexo", consul.getString("anexo"));
                modelRoot.put("mail", consul.getString("e_mail"));
                modelRoot.put("cant_ingresos", cant_ingresos);
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT empresa,desde,hasta,cargo FROM view_experiencia_laboral WHERE (rut = " + strRut + ")" + " ORDER BY desde desc ";
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("empresa", valida.validarDato(consul2.getString("empresa")));
                    simplehash1.put("desde", valida.validarDato(consul2.getString("desde")));
                    simplehash1.put("hasta", valida.validarDato(consul2.getString("hasta")));
                    simplehash1.put("cargo", valida.validarDato(consul2.getString("cargo")));
                }

                modelRoot.put("experiencia", simplelist);
                consul2.close();
                Consulta consul3 = new Consulta(conexion);
                sql = "SELECT fecha,tipo,motivo,empresa_orig,empresa_dest,cargo_orig,cargo_dest,unidad_orig,unidad_dest,permanencia, renta_ant, renta_act FROM view_movimiento_historico WHERE (rut = " + strRut + ") ORDER BY fecha desc";
                consul3.exec(sql);
                simplelist = new SimpleList();
                for(; consul3.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("fecha", valida.validarFecha(consul3.getValor("fecha")));
                    simplehash1.put("tipo", valida.validarDato(consul3.getString("tipo")));
                    simplehash1.put("motivo", valida.validarDato(consul3.getString("motivo")));
                    simplehash1.put("emp_orig", valida.validarDato(consul3.getString("empresa_orig")));
                    simplehash1.put("emp_dest", valida.validarDato(consul3.getString("empresa_dest")));
                    simplehash1.put("cargo_orig", valida.validarDato(consul3.getString("cargo_orig")));
                    simplehash1.put("cargo_dest", valida.validarDato(consul3.getString("cargo_dest")));
                    simplehash1.put("unidad_orig", valida.validarDato(consul3.getString("unidad_orig")));
                    simplehash1.put("unidad_dest", valida.validarDato(consul3.getString("unidad_dest")));
                    simplehash1.put("permanencia", valida.validarDato(consul3.getString("permanencia")));
                    simplehash1.put("renta_ant", tool.setFormatNumber(valida.validarDato(consul3.getString("renta_ant"), "0")));
                    simplehash1.put("renta_act", tool.setFormatNumber(valida.validarDato(consul3.getString("renta_act"), "0")));
                }

                modelRoot.put("movimientos", simplelist);
                consul3.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/Historial.htm",modelRoot);
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