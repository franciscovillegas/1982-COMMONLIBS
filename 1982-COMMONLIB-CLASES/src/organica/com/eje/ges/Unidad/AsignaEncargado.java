package organica.com.eje.ges.Unidad;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.Indicador.Periodo;
import organica.com.eje.datos.Unidades;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class AsignaEncargado extends MyHttpServlet
{

    public AsignaEncargado()
    {
        antiguo = null;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
        {
            if("0".equals(req.getParameter("modo")))
                Mantiene(req, resp, Conexion);
            else
                ManFechaActualizacion(req, resp, Conexion);
        } else
        {
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
            GeneraDatos(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", Conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        Unidades x = new Unidades();
        if(user.esValido())
        {
            Validar valida = new Validar();
            Consulta combo = new Consulta(Conexion);
            SimpleHash simplehash1 = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            String Emp = "";
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
            consulta = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_ges_unidades WHERE vigente='S'  ORDER BY unid_empresa, unid_desc";
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
                modelRoot.put("cod_empresa", req.getParameter("empresa").toString());
                Emp = req.getParameter("empresa").toString();
                consulta = "SELECT descrip AS empresa FROM eje_ges_empresa where empresa=".concat(String.valueOf(String.valueOf(Emp)));
                combo.exec(consulta);
                combo.next();
                modelRoot.put("des_empresa", combo.getString("empresa"));
            }
            combo.close();
            String unidad = req.getParameter("unidad");
            modelRoot.put("cod_uni", unidad);
            if(unidad != null)
            {
                EncargadoUnidad e = new EncargadoUnidad(Conexion, unidad, Emp);
                Rut r = new Rut(Conexion, e.RutEnc);
                modelRoot.put("rut_resp", valida.validarDato(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(r.Rut))))).append("-").append(r.Digito_Ver))), ""));
                modelRoot.put("rut_resp_r", valida.validarDato(r.Rut, "N"));
                antiguo = r.Rut;
                modelRoot.put("nombre_enc", valida.validarDato(e.NombreEnc, "No existe Encargado"));
                modelRoot.put("mision", valida.validarDato(e.MisionUni, "No se ha derterminado la Mision"));
                modelRoot.put("foto_enc", r.Foto);
                modelRoot.put("unidad_des", x.getNombreUnidad(Conexion, user.getEmpresa(), unidad));
                modelRoot.put("cargo_enc", valida.validarDato(r.Cargo, "No existe Cargo"));
                modelRoot.put("rut_cambios", valida.validarDato(e.rutCambios, "S/I"));
                modelRoot.put("nom_cambios", valida.validarDato(e.NombreCambios, "S/I"));
                modelRoot.put("inicio", valida.validarDato(e.fec_inicio, "S/I"));
                modelRoot.put("actualiza", valida.validarDato(e.fec_actualiza, "S/I"));
            }
            if(req.getParameter("rut") != null && !"".equals(req.getParameter("rut")))
            {
                Rut r = new Rut(Conexion, req.getParameter("rut"));
                modelRoot.put("rut", valida.validarDato(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(r.Rut))))).append("-").append(r.Digito_Ver))), ""));
                modelRoot.put("rut_r", valida.validarDato(r.Rut, "N"));
                modelRoot.put("nombre", valida.validarDato(r.Nombres, ""));
                modelRoot.put("foto", r.Foto);
                modelRoot.put("cargo", valida.validarDato(r.Cargo, ""));
            }
            super.retTemplate(resp,"Gestion/AsignaEncargado/Asigna.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void Mantiene(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
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
            EncargadoUnidad e = new EncargadoUnidad();
            if("M".equalsIgnoreCase(accion))
            {
                if(antiguo.equals(responsable))
                {
                    consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET mision = '")).append(mision).append("', ").append("rut_encargado = ").append(responsable).append(",").append("periodo= ").append(peri.getPeriodo()).append(",").append("fec_actualiza=getdate() ").append("WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("') ").append("and (periodo=").append(peri.getPeriodo()).append(") ").append("and (rut_encargado=").append(antiguo).append(")")));
                    info.insert(consulta);
                    OutMessage.OutMessagePrint("---->Mismo EnCargado Unidad\n".concat(String.valueOf(String.valueOf(consulta))));
                } else
                {
                    e.setEncargadosInactivos(Conexion, user, empresa, unidad);
                    consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_encargado  (unid_empresa, unid_id, periodo, rut_encargado, mision,estado,fec_actualiza,rut_cambios) VALUES ('")).append(empresa).append("', '").append(unidad).append("', ").append(peri.getPeriodo()).append(", ").append(responsable).append(", '").append(mision).append("','1',getdate(),").append(user.getRutUsuario()).append(")")));
                    if(!info.insert(consulta))
                    {
                        consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET estado = '1',fec_ini='")).append(req.getParameter("inicio")).append("',").append("fec_actualiza=getdate(),").append("rut_cambios= ").append(user.getRutUsuario()).append(" WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("') ").append("and (periodo=").append(peri.getPeriodo()).append(") ").append("and (rut_encargado=").append(responsable).append(")")));
                        info.insert(consulta);
                        e = new EncargadoUnidad();
                        if(e.getFecInicio(Conexion, unidad, empresa) == null)
                        {
                            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET fec_ini = '")).append(req.getParameter("inicio")).append("',").append("fec_actualiza=getdate() ").append(" WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("') ").append("and (periodo=").append(peri.getPeriodo()).append(") ").append("and (rut_encargado=").append(responsable).append(")")));
                            info.insert(consulta);
                        }
                    }
                }
            } else
            if("A".equalsIgnoreCase(accion))
            {
                e.setEncargadosInactivos(Conexion, user, empresa, unidad);
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_encargado  (unid_empresa, unid_id, periodo, rut_encargado, mision,estado,fec_actualiza,rut_cambios) VALUES ('")).append(empresa).append("', '").append(unidad).append("', ").append(peri.getPeriodo()).append(", ").append(responsable).append(", '").append(mision).append("','1',getdate(),").append(user.getRutUsuario()).append(")")));
                info.insert(consulta);
                OutMessage.OutMessagePrint("---->(accion A)Nuevo EnCargado Unidad\n".concat(String.valueOf(String.valueOf(consulta))));
            }
            info.close();
            GeneraDatos(req, resp, Conexion);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void ManFechaActualizacion(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Periodo peri = new Periodo(Conexion);
            String unidad = req.getParameter("unidad");
            String inicio = req.getParameter("inicio");
            String empresa = "";
            if(req.getParameter("empresa") != null && !req.getParameter("empresa").equals(""))
                empresa = req.getParameter("empresa").toString();
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET fec_ini = '")).append(inicio).append("',fec_actualiza=getdate(),").append("rut_cambios= ").append(user.getRutUsuario()).append("WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("') ").append("and (periodo=").append(peri.getPeriodo()).append(") ").append("and (rut_encargado=").append(antiguo).append(")")));
            info.insert(consulta);
            OutMessage.OutMessagePrint("---->Update Fec Actualizacion\n".concat(String.valueOf(String.valueOf(consulta))));
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
        super.retTemplate(resp,"Gestion/AsignaEncargado/mensaje.htm",modelRoot);
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
    private String antiguo;
}