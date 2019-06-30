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
//            GrupoFam_Manager

public class S_GrupoFamiliar extends MyHttpServlet
{

    public S_GrupoFamiliar()
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
            GeneraDatos(req, resp, Conexion);
            insTracking(req, "Grupo Familiar".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio GeneraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutId();
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Grupo Familiar", "Tiempo de Sesi\363n expirado...");
        } else
        {
            GrupoFam_Manager grupofam = new GrupoFam_Manager(conexion);
            Consulta consul = grupofam.GetCabecera(strRut);
            if(!consul.next())
            {
                super.mensaje.devolverPaginaMensage(resp, "Grupo Familiar", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", strRut);
                modelRoot.put("rut2", String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(strRut))))).append("-").append(consul.getString("digito_ver"))));
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
                System.out.println(" -------->" + user.getEmpresa());
                Consulta consul2 = grupofam.GetDetalleCargas(strRut, user.getEmpresa());
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
                consul.close();
                consul2.close();
                super.retTemplate(resp,"misdatos/grupo_fam.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("\n**** Fin GeneraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private Tools tool;
}