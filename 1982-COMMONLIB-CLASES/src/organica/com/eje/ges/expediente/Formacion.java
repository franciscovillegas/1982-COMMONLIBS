package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Formacion extends MyHttpServlet
{

    public Formacion()
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
        OutMessage.OutMessagePrint("\n************Entro al doPost de Formacion");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null)
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
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,nombre,cargo,digito_ver,anexo,e_mail,fecha_ingreso,id_foto,unidad,area,division  FROM view_ges_InfoRut  WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Formaci\363n", "Informaci\363n no disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", strRut);
                modelRoot.put("rut2", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ti.descrip AS titulo, tipo.descrip AS tipo, inst.descrip AS instituto, fp.fecha FROM eje_ges_formacion_profesional fp LEFT OUTER JOIN eje_ges_tipo_titulos tipo ON fp.tipo = tipo.tipo LEFT OUTER JOIN eje_ges_institutos inst ON fp.instituto = inst.instituto LEFT OUTER JOIN eje_ges_titulos ti ON fp.titulo = ti.titulo WHERE (rut = ")).append(strRut).append(")").append(" ORDER BY orden")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
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
                }

                modelRoot.put("profesional", simplelist);
                consul2.close();
                Consulta consul3 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT beca,periodo,carrera,sem_realizados,sem_pendientes,sem_totales,por_finan,monto_trabajador,monto_empresa,monto_total,estado_beca,resultado,org_instructor FROM eje_ges_curso_externo WHERE (rut = ")).append(strRut).append(") ORDER BY periodo")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul3.exec(sql);
                simplelist = new SimpleList();
                for(; consul3.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("beca", consul3.getString("beca"));
                    simplehash1.put("periodo", consul3.getString("periodo"));
                    simplehash1.put("carrera", consul3.getString("carrera"));
                    simplehash1.put("sem_real", consul3.getString("sem_realizados"));
                    simplehash1.put("sem_pend", consul3.getString("sem_pendientes"));
                    simplehash1.put("sem_totales", consul3.getString("sem_totales"));
                    simplehash1.put("finan", consul3.getString("por_finan"));
                    simplehash1.put("monto_trab", consul3.getString("monto_trabajador"));
                    simplehash1.put("monto_emp", consul3.getString("monto_empresa"));
                    simplehash1.put("monto_total", consul3.getString("monto_total"));
                    simplehash1.put("beca_estado", consul3.getString("estado_beca"));
                    simplehash1.put("resultado", consul3.getString("resultado"));
                    simplehash1.put("organismo", consul3.getString("org_instructor"));
                }

                modelRoot.put("perfeccionamiento_ext", simplelist);
                consul3.close();
                Consulta consul4 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT curso,fecha_inicio,fecha_termino,organismo,valor,asistencia,nota_aprob,resul_texto,horas FROM eje_ges_capacitaciones WHERE (rut = ")).append(strRut).append(") ORDER BY periodo")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul4.exec(sql);
                simplelist = new SimpleList();
                for(; consul4.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("curso", consul4.getString("curso"));
                    simplehash1.put("fecha_inicio", valida.validarFecha(consul4.getValor("fecha_inicio")));
                    simplehash1.put("fecha_termino", valida.validarFecha(consul4.getValor("fecha_termino")));
                    simplehash1.put("organismo", consul4.getString("organismo"));
                    simplehash1.put("valor", consul4.getString("valor"));
                    simplehash1.put("asistencia", consul4.getString("asistencia"));
                    simplehash1.put("nota_aprob", consul4.getString("nota_aprob"));
                    simplehash1.put("concepto_aprob", consul4.getString("resul_texto"));
                    simplehash1.put("horas", consul4.getString("horas"));
                }

                modelRoot.put("cursos", simplelist);
                consul4.close();
                super.retTemplate(resp,"Gestion/Expediente/Formacion.htm",modelRoot);
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