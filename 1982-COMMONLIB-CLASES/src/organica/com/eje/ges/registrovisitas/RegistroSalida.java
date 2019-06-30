package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class RegistroSalida extends MyHttpServlet
{

    public RegistroSalida()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        CargaPagina(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        GeneraCambios(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a listar las visitas activas");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss a");
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Validar valida = new Validar();
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rv as rut_visita, nv as nom_visita, rut, nombre, anexo, unidad, fecha,motivo, unid_id, empresa, fec, termino, horas FROM view_visitas_dia WHERE (termino IS NULL) order by rut_visita";
            OutMessage.OutMessagePrint("=======>Buscando\n ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("rutv", consul.getString("rut_visita"));
                simplehash1.put("nomv", consul.getString("nom_visita"));
                simplehash1.put("rutc", consul.getString("rut"));
                simplehash1.put("nomc", consul.getString("nombre"));
                simplehash1.put("mot", consul.getString("motivo"));
                simplehash1.put("fec", valida.validarFecha(consul.getString("fecha")));
                simplehash1.put("hora", horaFormat.format(consul.getValor("fecha")));
            }

            modelRoot.put("detalle", simplelist);
            consul.close();
            super.retTemplate(resp,"Gestion/RegistroVisitas/visitas.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost Busca Contacto");
    }

    public void GeneraCambios(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n*****    Entro al a Registrar Salida   *****");
        user = Usuario.rescatarUsuario(req);
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss a");
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        if(user.esValido())
        {
            Consulta consul = new Consulta(conexion);
            Consulta inserta = new Consulta(conexion);
            String valores[] = req.getParameterValues("valor");
            int largo = 0;
            int x = 0;
            if(valores == null)
                largo = 0;
            else
                largo = valores.length;
            if(largo != 0)
                for(x = 0; x < largo; x++)
                {
                    Vector par = new Vector();
                    par = Tools.separaLista(valores[x], "#");
                    String rut_v = (String)par.elementAt(0);
                    String rut_c = (String)par.elementAt(1);
                    String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_visita WHERE (rut_visita = '")).append(rut_v).append("') ").append("AND (rut_contacto= ").append(rut_c).append(") ").append("AND (termino IS NULL)")));
                    OutMessage.OutMessagePrint("buscando salida");
                    consul.exec(sql);
                    if(consul.next())
                        if(consul.getValor("termino") == null)
                        {
                            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_visita SET termino = GETDATE() WHERE (rut_visita = '")).append(rut_v).append("') ").append("AND (rut_contacto= ").append(rut_c).append(")")));
                            OutMessage.OutMessagePrint("Registrar Salida;".concat(String.valueOf(String.valueOf(sql))));
                            inserta.insert(sql);
                        } else
                        {
                            modelRoot.put("fecha", valida.validarFecha(consul.getString("termino")));
                            modelRoot.put("hora", horaFormat.format(consul.getValor("termino")));
                            modelRoot.put("mensaje", "Se registra una salida anterior.");
                        }
                }

            consul.close();
            inserta.close();
            super.retTemplate(resp,"Gestion/RegistroVisitas/exito.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de registro salida");
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/RegistroVisitas/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}