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

public class GrupoFamiliar extends MyHttpServlet
{

    public GrupoFamiliar()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de GrupoFamiliar");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Grupo Familiar", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_grup_fam", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Grupo Familiar", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, nombre, cargo, fecha_ingreso,digito_ver,id_foto,afp,isapre,estado_civil,anexo,area,e_mail,id_unidad, unidad,division FROM view_ges_InfoRut WHERE (rut = " + strRut + ")";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Grupo Familiar", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", strRut);
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("afp", valida.validarDato(consul.getString("afp"), "NR"));
                modelRoot.put("isapre", valida.validarDato(consul.getString("isapre"), "NR"));
                modelRoot.put("estado", valida.validarDato(consul.getString("estado_civil"), "NR"));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("id_unidad", consul.getString("id_unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT secuencia,nombre,fecha_nacim,rut_carga,dv_carga,es_carga,es_carga_salud,num_matrim,actividad,fecha_inicio,fecha_termino,sexo,rtrim(parentesco) as parentesco,(DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = " + consul.getString("rut") + ")";
                OutMessage.OutMessagePrint("=====--> " + sql);
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("rut", strRut);
                    simplehash1.put("num", valida.validarDato(consul2.getString("numero")));
                    simplehash1.put("nombre", valida.validarDato(consul2.getString("nombre")));
                    simplehash1.put("fec_nac", valida.validarFecha(consul2.getValor("fecha_nacim")));
                    simplehash1.put("rut_carga", valida.validarDato(consul2.getString("rut_carga")));
                    simplehash1.put("dv_carga", valida.validarDato(consul2.getString("dv_carga")));
                    simplehash1.put("es_carga", valida.validarDato(consul2.getString("es_carga")));
                    simplehash1.put("es_carga_salud", valida.validarDato(consul2.getString("es_carga_salud")));
                    simplehash1.put("actividad", valida.validarDato(consul2.getString("actividad")));
                    simplehash1.put("fec_inicio", valida.validarFecha(consul2.getValor("fecha_inicio")));
                    simplehash1.put("fec_termino", valida.validarFecha(consul2.getValor("fecha_termino")));
                    simplehash1.put("edad", valida.validarDato(consul2.getString("edad")));
                    simplehash1.put("sexo", valida.validarDato(consul2.getString("sexo")));
                    simplehash1.put("parentesco", valida.validarDato(consul2.getString("parentesco")));
                }

                modelRoot.put("cargas", simplelist);
                consul2.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/GrupoFamiliar.htm",modelRoot);
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