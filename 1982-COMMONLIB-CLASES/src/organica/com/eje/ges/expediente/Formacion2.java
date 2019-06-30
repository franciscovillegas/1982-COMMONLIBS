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

public class Formacion2 extends MyHttpServlet
{

    public Formacion2()
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
        OutMessage.OutMessagePrint("\n************Entro al doPost de Formacion2");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Formaci\363n", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_form", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Formaci\363n", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut,nombre,cargo,digito_ver,anexo,e_mail,fecha_ingreso,id_foto,id_unidad, unidad,area,division  FROM view_ges_InfoRut  WHERE (rut = " + strRut + ")";
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Formaci\363n", "Informaci\363n no disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", strRut);
                modelRoot.put("rut2", Tools.setFormatNumber(consul.getString("rut")) + "-" + consul.getString("digito_ver"));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("id_unidad", consul.getString("id_unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT ti.descrip AS titulo, tipo.descrip AS tipo, inst.descrip AS instituto, fp.fecha, fp.estado FROM eje_ges_formacion_profesional fp LEFT OUTER JOIN eje_ges_tipo_titulos tipo ON fp.tipo = tipo.tipo LEFT OUTER JOIN eje_ges_institutos inst ON fp.instituto = inst.instituto LEFT OUTER JOIN eje_ges_titulos ti ON fp.titulo = ti.titulo WHERE (rut = " + strRut + ")" + " ORDER BY orden ";
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("tipo", valida.validarDato(consul2.getString("tipo")));
                    simplehash1.put("titulo", valida.validarDato(consul2.getString("titulo")));
                    simplehash1.put("fecha", valida.validarFecha(consul2.getValor("fecha")));
                    simplehash1.put("organismo", valida.validarDato(consul2.getString("instituto")));
                    simplehash1.put("estado", valida.validarDato(consul2.getString("estado")));
                }

                modelRoot.put("profesional", simplelist);
                consul2.close();
                Consulta consul4 = new Consulta(conexion);
                sql = "SELECT curso,fecha_inicio,fecha_termino,organismo,valor,asistencia,nota_aprob,resul_texto,horas FROM eje_ges_capacitaciones WHERE (rut = " + strRut + ") ORDER BY periodo desc";
                consul4.exec(sql);
                simplelist = new SimpleList();
                for(; consul4.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("curso", consul4.getString("curso"));
                    simplehash1.put("fecha_inicio", valida.validarFecha(consul4.getValor("fecha_inicio")));
                    simplehash1.put("fecha_termino", valida.validarFecha(consul4.getValor("fecha_termino")));
                    simplehash1.put("organismo", valida.validarDato(consul4.getString("organismo")));
                    simplehash1.put("valor", valida.validarDato(consul4.getString("valor")));
                    simplehash1.put("asistencia", valida.validarDato(consul4.getString("asistencia")));
                    simplehash1.put("nota_aprob", valida.validarDato(consul4.getString("nota_aprob")));
                    simplehash1.put("concepto_aprob", valida.validarDato(consul4.getString("resul_texto")));
                    simplehash1.put("horas", valida.validarDato(consul4.getString("horas")));
                }

                modelRoot.put("cursos", simplelist);
                consul4.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/Formacion2.htm",modelRoot);;
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