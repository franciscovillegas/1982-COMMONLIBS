package portal.com.eje.serhumano.admin.configdf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.EncargadoUnidad;
import portal.com.eje.serhumano.datosdf.Periodo;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Unidades;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class AsignaUniRel extends MyHttpServlet
{

    public AsignaUniRel()
    {
        antiguo = null;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
            Mantiene(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
            GeneraDatos(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        Usuario user = SessionMgr.rescatarUsuario(req);
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        Unidades x = new Unidades();
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            Consulta combo = new Consulta(Conexion);
            SimpleHash simplehash1 = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            String Emp = null;
            String unidad = null;
            String tipo = null;
            consulta = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa order by id";
            combo.exec(consulta);
            OutMessage.OutMessagePrint("sql empresas");
            for(; combo.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", combo.getString("id"));
                simplehash1.put("desc", combo.getString("empresa"));
            }

            modelRoot.put("empresas", simplelist);
            consulta = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_sh_unidades WHERE vigente='S'  ORDER BY unid_empresa, unid_desc";
            combo.exec(consulta);
            OutMessage.OutMessagePrint("sql empresas");
            simplelist = new SimpleList();
            for(; combo.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("empresa", combo.getString("empresa"));
                simplehash1.put("id", combo.getString("unid_id"));
                simplehash1.put("desc", combo.getString("unid_desc"));
            }

            modelRoot.put("unidades", simplelist);
            if(req.getParameter("empresa") != null && !req.getParameter("empresa").equals(""))
            {
                Emp = req.getParameter("empresa");
                unidad = req.getParameter("unidad");
            } else
            {
                combo.exec("select emp_rel,uni_rel,tipo_rel from eje_ges_usuario where rut_usuario = ".concat(String.valueOf(String.valueOf(req.getParameter("rut")))));
                if(combo.next())
                {
                    String paso = combo.getString("emp_rel");
                    if(paso != null && !"".equals(paso.trim()))
                    {
                        unidad = combo.getString("uni_rel");
                        Emp = paso;
                        tipo = combo.getString("tipo_rel");
                        OutMessage.OutMessagePrint("tiene unidad/relativa");
                    }
                }
            }
            modelRoot.put("cod_uni", unidad);
            modelRoot.put("cod_empresa", Emp);
            modelRoot.put("tipo", tipo);
            combo.close();
            if(req.getParameter("rut") != null)
                modelRoot.put("rutuser", req.getParameter("rut"));
            if(req.getParameter("accion") != null)
                modelRoot.put("accion", req.getParameter("accion"));
            if(unidad != null)
            {
                EncargadoUnidad e = new EncargadoUnidad(Conexion, unidad, Emp);
                datosRut r = new datosRut(Conexion, e.RutEnc);
                modelRoot.put("rut_resp", valida.validarDato(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(r.Rut))))).append("-").append(r.Digito_Ver))), ""));
                modelRoot.put("rut_resp_r", valida.validarDato(r.Rut, "N"));
                antiguo = r.Rut;
                modelRoot.put("nombre_enc", valida.validarDato(e.NombreEnc, "No existe Encargado"));
                modelRoot.put("mision", valida.validarDato(e.MisionUni, "No se ha derterminado la Mision"));
                modelRoot.put("foto_enc", r.Foto);
                modelRoot.put("unidad_des", x.getNombreUnidad(Conexion, user.getEmpresa(), unidad));
                modelRoot.put("cargo_enc", valida.validarDato(r.Cargo, "No existe Cargo"));
            }
            if(req.getParameter("rut") != null && !"".equals(req.getParameter("rut")))
            {
                datosRut r = new datosRut(Conexion, req.getParameter("rut"));
                modelRoot.put("rut", valida.validarDato(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(r.Rut))))).append("-").append(r.Digito_Ver))), ""));
                modelRoot.put("rut_r", valida.validarDato(r.Rut, "N"));
                modelRoot.put("nombre", valida.validarDato(r.Nombres, ""));
                modelRoot.put("foto", r.Foto);
                modelRoot.put("cargo", valida.validarDato(r.Cargo, ""));
            }
            modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
            super.retTemplate(resp,"admin/configdf/Asignar_UniRel/Asigna.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void Mantiene(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        Usuario user = SessionMgr.rescatarUsuario(req);
        String consulta = null;
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Periodo peri = new Periodo(Conexion);
            String unidad = req.getParameter("unidad");
            String responsable = req.getParameter("rut_r");
            String mision = req.getParameter("mision");
            String empresa = "";
            if(req.getParameter("empresa") != null && !req.getParameter("empresa").equals(""))
                empresa = req.getParameter("empresa").toString();
            String accion = req.getParameter("accion");
            if("M".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET mision = '")).append(mision).append("', ").append("rut_encargado = ").append(responsable).append(",").append("periodo= ").append(peri.getPeriodo()).append(" ").append("WHERE (unid_id = '").append(unidad).append("') ").append("and (rut_encargado=").append(antiguo).append(") ").append("and (unid_empresa='").append(empresa).append("')")));
            else
            if("A".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_encargado  (unid_empresa, unid_id, periodo, rut_encargado, mision)VALUES ('")).append(empresa).append("', '").append(unidad).append("', ").append(peri.getPeriodo()).append(", ").append(responsable).append(", '").append(mision).append("')")));
            OutMessage.OutMessagePrint("---->Actualizar U/E: ".concat(String.valueOf(String.valueOf(consulta))));
            info.insert(consulta);
            info.close();
            GeneraDatos(req, resp, Conexion);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"admin/configdf/Asignar_UniRel/mensaje.htm",modelRoot);
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

    private String antiguo;
}