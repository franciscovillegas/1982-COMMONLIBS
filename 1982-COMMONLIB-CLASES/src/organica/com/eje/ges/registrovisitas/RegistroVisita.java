package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class RegistroVisita extends MyHttpServlet
{

    public RegistroVisita()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            CargaPagina(req, resp, Conexion);
        else
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            GeneraCambios(req, resp, Conexion);
        else
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", Conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a Mostrar Data de RegistroVisita");
        user = Usuario.rescatarUsuario(req);
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss a");
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        SimpleList simplelist2 = new SimpleList();
        SimpleList datos = new SimpleList();
        Validar valida = new Validar();
        String sql = "";
        Consulta consul = new Consulta(conexion);
        Consulta busca = new Consulta(conexion);
        Consulta revisa = new Consulta(conexion);
        String rut = req.getParameter("rut");
        String nom_c = req.getParameter("nom");
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        String anexo = req.getParameter("anexo");
        String rut_v = req.getParameter("rutv");
        String nom_v = req.getParameter("nomv");
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_trabajadores.nombre AS contacto,eje_ges_visita.fecha, eje_ges_visita.rut_visita,eje_ges_visita.termino FROM eje_ges_visita INNER JOIN eje_ges_trabajador ON eje_ges_visita.rut_contacto = eje_ges_trabajadores.rut WHERE (eje_ges_visita.rut_visita = '")).append(rut_v).append("') AND (eje_ges_visita.termino IS NULL)")));
        revisa.exec(sql);
        if(revisa.next())
        {
            modelRoot.put("contacto", revisa.getString("contacto"));
            modelRoot.put("hora", horaFormat.format(revisa.getValor("fecha")));
            modelRoot.put("fecha", valida.validarFecha(revisa.getValor("fecha")));
            modelRoot.put("mensaje", "1");
        } else
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_trabajadores_anexos.rut,eje_ges_anexo_rel.anexo_rel,eje_ges_trabajadores_anexos1.rut AS rut_rel,eje_ges_trabajadores.nombre,eje_ges_trabajadores.digito_ver AS dig_rel FROM eje_ges_anexo_rel INNER JOIN eje_ges_trabajadores_anexos ON eje_ges_anexo_rel.unidad = eje_ges_trabajadores_anexos.unidad AND eje_ges_anexo_rel.anexo = eje_ges_trabajadores_anexos.anexo INNER JOIN eje_ges_trabajadores_anexos eje_ges_trabajadores_anexos1 ON eje_ges_anexo_rel.unidad_rel = eje_ges_trabajadores_anexos1.unidad AND eje_ges_anexo_rel.anexo_rel = eje_ges_trabajadores_anexos1.anexo INNER JOIN eje_ges_trabajador ON eje_ges_trabajadores_anexos1.rut = eje_ges_trabajadores.rut WHERE (eje_ges_trabajadores_anexos.rut = ")).append(rut).append(")")));
            OutMessage.OutMessagePrint("=======>Anexos relacionados\n ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            SimpleHash simplehash1;
            for(; consul.next(); simplelist2.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("a_rel", consul.getString("anexo_rel"));
                simplehash1.put("nombre", consul.getString("nombre"));
                simplehash1.put("rut", consul.getString("rut_rel"));
            }

            modelRoot.put("anexos", simplelist2);
            sql = "SELECT grup_id AS id, grup_desc AS grupo FROM eje_ges_visita_motivo_grupo";
            consul.exec(sql);
            String layer = "";
            for(; consul.next(); OutMessage.OutMessagePrint("****************************"))
            {
                simplehash1 = new SimpleHash();
                layer = consul.getString("id");
                simplehash1.put("id", consul.getString("id"));
                simplehash1.put("grupo", consul.getString("grupo"));
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT mot_id, mot_grupo, mot_desc FROM eje_ges_visita_motivo WHERE (mot_grupo = '")).append(consul.getString("id")).append("')")));
                OutMessage.OutMessagePrint("------>motivos;".concat(String.valueOf(String.valueOf(sql))));
                busca.exec(sql);
                datos = new SimpleList();
                SimpleHash simplehash2;
                for(; busca.next(); datos.add(simplehash2))
                {
                    simplehash2 = new SimpleHash();
                    simplehash2.put("grup", consul.getString("id"));
                    OutMessage.OutMessagePrint("-->motivo: ".concat(String.valueOf(String.valueOf(busca.getString("mot_id")))));
                    simplehash2.put("m_id", busca.getString("mot_id"));
                    simplehash2.put("m_desc", busca.getString("mot_desc"));
                }

                simplehash1.put("datos", datos);
                simplelist.add(simplehash1);
            }

            modelRoot.put("detalle", simplelist);
            modelRoot.put("lay", layer);
            modelRoot.put("rut_v", rut_v);
            if(nom_v.length() > 20)
            {
                modelRoot.put("nom_v", nom_v.substring(0, 20));
                OutMessage.OutMessagePrint("-->Cortar Nombre");
            } else
            {
                modelRoot.put("nom_v", nom_v);
            }
            modelRoot.put("rut_c", rut);
            if(nom_c.length() > 20)
            {
                modelRoot.put("nom_c", nom_c.substring(0, 20));
                OutMessage.OutMessagePrint("-->Cortar Nombre");
            } else
            {
                modelRoot.put("nom_c", nom_c);
            }
            modelRoot.put("unidad", unidad);
            modelRoot.put("anexo", anexo);
            modelRoot.put("empresa", empresa);
            consul.close();
            busca.close();
        }
        revisa.close();
        super.retTemplate(resp,"Gestion/RegistroVisitas/registro.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost Busca Contacto");
    }

    public void GeneraCambios(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al a insertar RegistroVisita");
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        Consulta consul = new Consulta(conexion);
        Consulta inserta = new Consulta(conexion);
        String rut = req.getParameter("rut_c");
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        String responsable = req.getParameter("responsable");
        String rut_v = req.getParameter("rut_v");
        String nom_v = req.getParameter("nom_v");
        String motivo = req.getParameter("motivo");
        String emp_v = req.getParameter("empre_v");
        String obs = req.getParameter("obs");
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("========>Empre Visita: ")).append(emp_v).append("\nObservacion: ").append(obs))));
        int corr = 0;
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT MAX(corr) AS num FROM eje_ges_visita WHERE (rut_contacto = ")).append(rut).append(")")));
        consul.exec(sql);
        if(consul.next())
        {
            corr = consul.getInt("num") + 1;
            if(responsable.equals("0"))
            {
                OutMessage.OutMessagePrint("*****respondio el mismo contacto******");
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita (rut_contacto, rut_visita, nom_visita, motivo_id, fecha, unidad,corr, empresa,rut_responsable,empre_visita,obs_visita) VALUES (")).append(rut).append(", '").append(rut_v).append("', '").append(nom_v).append("','").append(motivo).append("', GETDATE(), '").append(unidad).append("', ").append(corr).append(", '").append(empresa).append("',").append(rut).append(",'").append(emp_v).append("','").append(obs).append("')")));
            } else
            {
                OutMessage.OutMessagePrint("*****respondio otra persona******");
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita (rut_contacto, rut_visita, nom_visita, motivo_id, fecha, unidad,corr, empresa,rut_responsable,empre_visita,obs_visita) VALUES (")).append(rut).append(", '").append(rut_v).append("', '").append(nom_v).append("','").append(motivo).append("', GETDATE(), '").append(unidad).append("', ").append(corr).append(", '").append(empresa).append("',").append(responsable).append(",'").append(emp_v).append("','").append(obs).append("')")));
            }
        } else
        if(responsable.equals("0"))
        {
            OutMessage.OutMessagePrint("*****respondio el mismo contacto******");
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita (rut_contacto, rut_visita, nom_visita, motivo_id, fecha, unidad,corr, empresa,rut_responsable,empre_visita,obs_visita) VALUES (")).append(rut).append(", '").append(rut_v).append("', '").append(nom_v).append("','").append(motivo).append("', GETDATE(), '").append(unidad).append("',1, '").append(empresa).append("',").append(rut).append(",'").append(emp_v).append("','").append(obs).append("')")));
        } else
        {
            OutMessage.OutMessagePrint("*****respondio otra persona******");
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita (rut_contacto, rut_visita, nom_visita, motivo_id, fecha, unidad,corr, empresa,rut_responsable,empre_visita,obs_visita) VALUES (")).append(rut).append(", '").append(rut_v).append("', '").append(nom_v).append("','").append(motivo).append("', GETDATE(), '").append(unidad).append("',1, '").append(empresa).append("',").append(responsable).append(",'").append(emp_v).append("','").append(obs).append("')")));
        }
        OutMessage.OutMessagePrint("insertar tabla visitas;".concat(String.valueOf(String.valueOf(sql))));
        inserta.insert(sql);
        modelRoot.put("rut_v", rut_v);
        consul.close();
        inserta.close();
        super.retTemplate(resp,"Gestion/RegistroVisitas/exito.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost Busca Contacto");
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