package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.bci.traspaso.GeneraUnidadRama;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ManUnidades2 extends MyHttpServlet
{

    public ManUnidades2()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        String valor = req.getParameter("id_unidad");
        OutMessage.OutMessagePrint("*valor: ".concat(String.valueOf(String.valueOf(valor))));
        if(Conexion != null)
        {
            if(Usuario.rescatarUsuario(req).esValido())
            {
                if(valor == null)
                {
                    CargaPagina(req, resp, Conexion);
                } else
                {
                    OutMessage.OutMessagePrint("*******Actualizar....criterios***********");
                    GeneraDatos(req, resp, Conexion);
                }
            } else
            {
                devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
            }
        } else
        {
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = "";
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        ControlAcceso control = new ControlAcceso(user);
        String id_unidad = req.getParameter("unidad");
        String unidad = req.getParameter("descrip");
        String modo = req.getParameter("modo");
        String id_empresa = req.getParameter("empresa");
        if(control.tienePermiso("df_mant_unidad"))
        {
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("unidad: ")).append(id_unidad).append("\n").append("desc:   ").append(unidad).append("\n").append("modo:   ").append(modo).append("\n").append("empresa:").append(id_empresa))));
            Consulta combo = new Consulta(Conexion);
            consulta = "SELECT empresa AS id, descrip AS empresa FROM eje_ges_empresa";
            combo.exec(consulta);
            OutMessage.OutMessagePrint("sql empresas");
            SimpleHash simplehash1;
            for(; combo.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", combo.getString("id"));
                simplehash1.put("desc", combo.getString("empresa"));
            }

            modelRoot.put("empresas", simplelist);
            consulta = "SELECT area_empresa as empresa , area_id as id , area_desc as descrip FROM eje_ges_areas  ORDER BY empresa, descrip";
            combo.exec(consulta);
            modelRoot.put("areas", combo.getSimpleList());
            consulta = "SELECT div_empresa AS empresa, div_id AS id, div_desc AS descrip FROM eje_ges_division  ORDER BY empresa, descrip";
            combo.exec(consulta);
            modelRoot.put("divisiones", combo.getSimpleList());
            OutMessage.SetTraza();
            OutMessage.traza = "";
            if("1".equals(OutMessage.verunidades))
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_id, unid_desc,unid_empresa FROM view_unidad_no_jerarquia WHERE (unid_empresa = '")).append(id_empresa).append("')").append(" ORDER BY unid_desc")));
            else
            if("2".equals(OutMessage.verunidades))
                consulta = "SELECT unid_id, unid_desc FROM eje_ges_unidades WHERE (vigente = 'S') GROUP BY unid_id, unid_desc ORDER BY unid_desc";
            combo.exec(consulta);
            modelRoot.put("unidades_no_jer", combo.getSimpleList());
            combo.close();
            Consulta valores = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_unidades.area, eje_ges_jerarquia.compania,eje_ges_jerarquia.nodo_id, eje_ges_jerarquia.nodo_padre,eje_ges_jerarquia.nodo_nivel, eje_ges_jerarquia.nodo_imagen,eje_ges_jerarquia.nodo_corr,eje_ges_jerarquia.nodo_hijos FROM eje_ges_unidades INNER JOIN eje_ges_jerarquia ON eje_ges_unidades.unid_empresa = eje_ges_jerarquia.compania AND eje_ges_unidades.unid_id = eje_ges_jerarquia.nodo_id WHERE (eje_ges_jerarquia.nodo_id = '")).append(id_unidad).append("') AND ").append("(eje_ges_jerarquia.compania = '").append(id_empresa).append("')")));
            valores.exec(consulta);
            valores.next();
            OutMessage.OutMessagePrint("sql unidad");
            if(modo.equals("3"))
            {
                modelRoot.put("modo", modo);
                modelRoot.put("unidad", id_unidad);
                modelRoot.put("glosa", unidad);
                modelRoot.put("corr", valores.getString("nodo_corr"));
                modelRoot.put("padre", valores.getString("nodo_padre"));
                modelRoot.put("emp", valores.getString("compania"));
                modelRoot.put("ar", valores.getString("area"));
                Periodo peri = new Periodo(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_rama AS tipo, dot_aut FROM eje_ges_indic_bae_dotacion WHERE (empresa = '")).append(id_empresa).append("') AND (unidad = '").append(id_unidad).append("') AND").append(" (periodo = ").append(peri.getPeriodo()).append(")")));
                Consulta consul_dot = new Consulta(Conexion);
                OutMessage.OutMessagePrint("rescata dot. aut --> ".concat(String.valueOf(String.valueOf(sql))));
                consul_dot.exec(sql);
                do
                {
                    if(!consul_dot.next())
                        break;
                    if("U".equals(consul_dot.getString("tipo")))
                        modelRoot.put("dot_aut_dir", consul_dot.getString("dot_aut"));
                    else
                    if("R".equals(consul_dot.getString("tipo")))
                        modelRoot.put("dot_aut_ind", consul_dot.getString("dot_aut"));
                } while(true);
                consul_dot.close();
            }
            if(modo.equals("1"))
            {
                OutMessage.OutMessagePrint("Agregar Unidad*****");
                modelRoot.put("modo", modo);
                modelRoot.put("padre", valores.getString("nodo_padre"));
                modelRoot.put("unidad", id_unidad);
                modelRoot.put("emp", id_empresa);
                modelRoot.put("ar", valores.getString("area"));
            }
            if(modo.equals("2"))
            {
                OutMessage.OutMessagePrint("Eliminar Unidad*****");
                Consulta hijos = new Consulta(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id FROM eje_ges_jerarquia WHERE (nodo_padre = '")).append(id_unidad).append("') ").append("AND (compania = '").append(valores.getString("compania")).append("')")));
                OutMessage.OutMessagePrint("**Rescatar hijos;".concat(String.valueOf(String.valueOf(sql))));
                hijos.exec(sql);
                Consulta elimina = new Consulta(Conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_jerarquia WHERE (nodo_id = '")).append(id_unidad).append("') ").append("AND (compania = '").append(valores.getString("compania")).append("')")));
                OutMessage.OutMessagePrint("**eliminando nodo (tabla Jerarquia);".concat(String.valueOf(String.valueOf(sql))));
                elimina.insert(sql);
                for(; hijos.next(); Elimina_Nodo(hijos.getString("compania"), hijos.getString("nodo_id"), Conexion));
                hijos.close();
                (new GeneraUnidadRama(Conexion)).Run();
                modelRoot.put("carga", "1");
            }
            valores.close();
        } else
        {
            modelRoot.put("permiso", "1");
        }
        super.retTemplate(resp,"Gestion/MantUnidades/EditaUnidad.htm",modelRoot);
    }

    private void Elimina_Nodo(String emp, String unid, Connection Conexion)
    {
        String sql = "";
        Consulta elimina = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_jerarquia WHERE (nodo_id = '")).append(unid).append("') ").append("AND (compania = '").append(emp).append("')")));
        OutMessage.OutMessagePrint("**eliminando nodo(Tabla Jerarquia);".concat(String.valueOf(String.valueOf(sql))));
        elimina.insert(sql);
        elimina.close();
        Consulta hijos = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id FROM eje_ges_jerarquia WHERE (nodo_padre = '")).append(unid).append("')").append("AND (compania = '").append(emp).append("')")));
        OutMessage.OutMessagePrint("**Rescatar hijos;".concat(String.valueOf(String.valueOf(sql))));
        hijos.exec(sql);
        for(; hijos.next(); Elimina_Nodo(hijos.getString("compania"), hijos.getString("nodo_id"), Conexion));
        hijos.close();
    }

    public void RefrescaDatos(String unid, String des, String compa, String mo, HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = "";
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        ControlAcceso control = new ControlAcceso(user);
        OutMessage.OutMessagePrint("entro a refrescar datos**********");
        if(control.tienePermiso("df_mant_unidad"))
        {
            Consulta combo = new Consulta(Conexion);
            consulta = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa";
            combo.exec(consulta);
            SimpleHash simplehash1;
            for(; combo.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", combo.getString("id"));
                simplehash1.put("desc", combo.getString("empresa"));
            }

            modelRoot.put("empresas", simplelist);
            consulta = "SELECT area_empresa as empresa , area_id as id , area_desc as descrip FROM eje_ges_areas ORDER BY empresa, descrip";
            combo.exec(consulta);
            modelRoot.put("areas", combo.getSimpleList());
            consulta = "SELECT div_empresa AS empresa, div_id AS id, div_desc AS descrip FROM eje_ges_division ORDER BY empresa, descrip";
            combo.exec(consulta);
            modelRoot.put("divisiones", combo.getSimpleList());
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_id, unid_desc FROM view_unidad_no_jerarquia WHERE (unid_empresa = '")).append(compa).append("')").append(" ORDER BY unid_desc")));
            combo.exec(consulta);
            modelRoot.put("unidades_no_jer", combo.getSimpleList());
            combo.close();
            if(mo.equals("3"))
            {
                Consulta valores = new Consulta(Conexion);
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_unidades.area, eje_ges_jerarquia.compania,eje_ges_jerarquia.nodo_id, eje_ges_jerarquia.nodo_padre,eje_ges_jerarquia.nodo_nivel, eje_ges_jerarquia.nodo_imagen,eje_ges_jerarquia.nodo_corr,eje_ges_jerarquia.nodo_hijos FROM eje_ges_unidades INNER JOIN eje_ges_jerarquia ON eje_ges_unidades.unid_empresa = eje_ges_jerarquia.compania AND eje_ges_unidades.unid_id = eje_ges_jerarquia.nodo_id LEFT OUTER JOIN eje_ges_areas ON eje_ges_unidades.area = eje_ges_areas.area_id AND eje_ges_unidades.unid_empresa = eje_ges_areas.area_empresa WHERE (eje_ges_jerarquia.nodo_id = '")).append(unid).append("') AND ").append("(eje_ges_jerarquia.compania = '").append(compa).append("')")));
                valores.exec(consulta);
                valores.next();
                modelRoot.put("modo", mo);
                modelRoot.put("unidad", unid);
                modelRoot.put("glosa", des);
                modelRoot.put("corr", valores.getString("nodo_corr"));
                modelRoot.put("padre", valores.getString("nodo_padre"));
                modelRoot.put("emp", valores.getString("compania"));
                modelRoot.put("ar", valores.getString("area"));
                valores.close();
                Periodo peri = new Periodo(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_rama AS tipo, dot_aut FROM eje_ges_indic_bae_dotacion WHERE (empresa = '")).append(compa).append("') AND (unidad = '").append(unid).append("') AND").append(" (periodo = ").append(peri.getPeriodo()).append(")")));
                Consulta consul_dot = new Consulta(Conexion);
                OutMessage.OutMessagePrint("rescata dot. aut --> ".concat(String.valueOf(String.valueOf(sql))));
                consul_dot.exec(sql);
                do
                {
                    if(!consul_dot.next())
                        break;
                    if("U".equals(consul_dot.getString("tipo")))
                        modelRoot.put("dot_aut_dir", consul_dot.getString("dot_aut"));
                    else
                    if("R".equals(consul_dot.getString("tipo")))
                        modelRoot.put("dot_aut_ind", consul_dot.getString("dot_aut"));
                } while(true);
                consul_dot.close();
            }
            modelRoot.put("carga", "1");
        } else
        {
            modelRoot.put("permiso", "1");
        }
        super.retTemplate(resp,"Gestion/MantUnidades/EditaUnidad.htm",modelRoot);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        OutMessage.OutMessagePrint("---->Mantener Unidad*********");
        String consulta = null;
        String modo = req.getParameter("modo");
        String descrip = req.getParameter("descrip");
        String unidad = req.getParameter("id_unidad");
        String area_nueva = req.getParameter("area");
        String ant_unidad = req.getParameter("unidad");
        String comp_ant = req.getParameter("emp_ant");
        String dot_aut_dir = req.getParameter("dot_aut_dir");
        String dot_aut_ind = req.getParameter("dot_aut_ind");
        OutMessage.OutMessagePrint("modo= ".concat(String.valueOf(String.valueOf(modo))));
        if(modo.equals("3"))
        {
            Consulta datos1 = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_desc = '")).append(descrip).append("',").append("area = '").append(area_nueva).append("' ").append("WHERE (unid_id = '").append(ant_unidad).append("') ").append("AND (unid_empresa = '").append(comp_ant).append("')")));
            OutMessage.OutMessagePrint("-->update unidad: ".concat(String.valueOf(String.valueOf(consulta))));
            datos1.insert(consulta);
            datos1.close();
            Periodo peri = new Periodo(Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "U", dot_aut_dir, Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "R", dot_aut_ind, Conexion);
            (new GeneraUnidadRama(Conexion)).Run();
            RefrescaDatos(unidad, descrip, comp_ant, modo, req, resp, Conexion);
        }
        if(modo.equals("1"))
        {
            Consulta datos2 = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_jerarquia (compania, nodo_id, nodo_padre, nodo_nivel, nodo_imagen,nodo_corr, nodo_hijos) VALUES ('")).append(comp_ant).append("', '").append(unidad).append("', '").append(ant_unidad).append("', null, NULL, null, null)")));
            OutMessage.OutMessagePrint("-->Insert Jerarquia: ".concat(String.valueOf(String.valueOf(consulta))));
            datos2.insert(consulta);
            datos2.close();
            Periodo peri = new Periodo(Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "U", dot_aut_dir, Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "R", dot_aut_ind, Conexion);
            (new GeneraUnidadRama(Conexion)).Run();
            RefrescaDatos(unidad, descrip, comp_ant, modo, req, resp, Conexion);
        }
    }

    private void actualizaDotAut(String periodo, String empresa, String unidad, String tipo, String valor, Connection Conexion)
    {
        Consulta act_dot = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_indic_bae_dotacion SET dot_aut = ")).append(valor).append(" WHERE (uni_rama = '").append(tipo).append("') AND (periodo = ").append(periodo).append(") AND").append(" (unidad = '").append(unidad).append("') AND (empresa = '").append(empresa).append("')")));
        OutMessage.OutMessagePrint("ActDotAut U --> ".concat(String.valueOf(String.valueOf(sql))));
        if(!act_dot.insert(sql))
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_indic_bae_dotacion (dot_aut, uni_rama, periodo, unidad, empresa) VALUES (")).append(valor).append(", '").append(tipo).append("', ").append(periodo).append(", '").append(unidad).append("', '").append(empresa).append("')")));
            OutMessage.OutMessagePrint("ActDotAut I --> ".concat(String.valueOf(String.valueOf(sql))));
            act_dot.insert(sql);
        }
        act_dot.close();
    }

    private boolean Existe(String empresa, String unidad, Connection Conexion)
    {
        Consulta busqueda = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_jerarquia where (nodo_id = '")).append(unidad).append("') ").append("AND (compania = '").append(empresa).append("')")));
        busqueda.exec(sql);
        return busqueda.next();
    }

    private int RescataNivelPadre(String padre, Connection Conexion)
    {
        String consulta = null;
        int niv = 0;
        Consulta nivel = new Consulta(Conexion);
        consulta = String.valueOf(String.valueOf((new StringBuilder("select nodo_nivel from eje_ges_jerarquia where nodo_id='")).append(padre).append("'")));
        nivel.exec(consulta);
        if(nivel.next())
            niv = nivel.getInt("nodo_nivel");
        nivel.close();
        return niv;
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/MantUnidades/mensaje.htm",modelRoot);
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
    private Mensaje mensaje;
}