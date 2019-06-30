package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ManUnidades extends MyHttpServlet
{

    public ManUnidades()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doGet(req, resp);
        connMgr.freeConnection("portal", Conexion);
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
        insTracking(req, "Mantenerdor Unidades".intern(),null );
        String consulta = "";
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        ControlAcceso control = new ControlAcceso(user);
        String id_unidad = req.getParameter("unidad");
        String unidad = req.getParameter("descrip");
        String modo = req.getParameter("modo");
        String id_empresa = req.getParameter("empresa");
        if(!user.esValido())
            devolverPaginaMensage(resp, "Mantener Unidades", "Tiempo de Sesi\363n expirado...");
        else
        if(control.tienePermiso("df_mant_unidad"))
        {
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("unidad: ")).append(id_unidad).append("\n").append("desc:   ").append(unidad).append("\n").append("modo:   ").append(modo).append("\n").append("empresa:").append(id_empresa))));
            Consulta combo = new Consulta(Conexion);
            consulta = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa";
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
            Consulta valores = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_unidades.area, eje_ges_jerarquia.compania,eje_ges_jerarquia.nodo_id, eje_ges_jerarquia.nodo_padre,eje_ges_jerarquia.nodo_nivel, eje_ges_jerarquia.nodo_imagen,eje_ges_jerarquia.nodo_corr,eje_ges_jerarquia.nodo_hijos FROM eje_ges_unidades INNER JOIN eje_ges_jerarquia ON eje_ges_unidades.unid_empresa = eje_ges_jerarquia.compania AND eje_ges_unidades.unid_id = eje_ges_jerarquia.nodo_id LEFT OUTER JOIN eje_ges_areas ON eje_ges_unidades.area = eje_ges_areas.area_id AND eje_ges_unidades.unid_empresa = eje_ges_areas.area_empresa WHERE (eje_ges_jerarquia.nodo_id = '")).append(id_unidad).append("') AND ").append("(eje_ges_jerarquia.compania = '").append(id_empresa).append("')")));
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
                sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidades WHERE (unid_empresa = '")).append(valores.getString("compania")).append("') ").append("AND (unid_id = '").append(id_unidad).append("')")));
                OutMessage.OutMessagePrint("**eliminando nodo (tabla Unidades);".concat(String.valueOf(String.valueOf(sql))));
                elimina.insert(sql);
                sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidad_rama WHERE (empresa = '")).append(valores.getString("compania")).append("') ").append("AND (unidad = '").append(id_unidad).append("')")));
                OutMessage.OutMessagePrint("**eliminando nodo (tabla Unidad Rama);".concat(String.valueOf(String.valueOf(sql))));
                elimina.insert(sql);
                sql = String.valueOf(String.valueOf(new StringBuilder("select max(peri_id) as max from eje_ges_periodo")));
                elimina.exec(sql);
                elimina.next();
                int max = elimina.getInt("max");
                sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_indic_bae_dotacion WHERE (periodo = ")).append(max).append(" AND empresa = '").append(valores.getString("compania")).append("') ").append("AND (unidad = '").append(id_unidad).append("')")));
                OutMessage.OutMessagePrint("**eliminando nodo (tabla indic_bae_dotacion);".concat(String.valueOf(String.valueOf(sql))));
                elimina.insert(sql);
                Usuario.SuperNodo = null;
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
        Consulta hijos = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id FROM eje_ges_jerarquia WHERE (nodo_padre = '")).append(unid).append("')").append("AND (compania = '").append(emp).append("')")));
        OutMessage.OutMessagePrint("**Rescatar hijos;".concat(String.valueOf(String.valueOf(sql))));
        hijos.exec(sql);
        Consulta elimina = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_jerarquia WHERE (nodo_id = '")).append(unid).append("') ").append("AND (compania = '").append(emp).append("')")));
        OutMessage.OutMessagePrint("**eliminando nodo(Tabla Jerarquia);".concat(String.valueOf(String.valueOf(sql))));
        elimina.insert(sql);
// -------
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidades WHERE (unid_empresa = '")).append(emp).append("') ").append("AND (unid_id = '").append(unid).append("')")));
        OutMessage.OutMessagePrint("**eliminando nodo (tabla Unidades);".concat(String.valueOf(String.valueOf(sql))));
        elimina.insert(sql);
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_unidad_rama WHERE (empresa = '")).append(emp).append("') ").append("AND (unidad = '").append(unid).append("')")));
        OutMessage.OutMessagePrint("**eliminando nodo (tabla Unidad Rama);".concat(String.valueOf(String.valueOf(sql))));
        elimina.insert(sql);
// --------
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
                modelRoot.put("carga", "1");
            }
            if(mo.equals("1"))
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
        SimpleHash modelRoot = new SimpleHash();
        String modo = req.getParameter("modo");
        String descrip = req.getParameter("descrip");
        String unidad = req.getParameter("id_unidad");
        String comp_nueva = req.getParameter("empresa");
        String area_nueva = req.getParameter("area");
        String ant_unidad = req.getParameter("unidad");
        String comp_ant = req.getParameter("emp_ant");
        String dot_aut_dir = req.getParameter("dot_aut_dir");
        String dot_aut_ind = req.getParameter("dot_aut_ind");
        OutMessage.OutMessagePrint("modo= ".concat(String.valueOf(String.valueOf(modo))));
        if(modo.equals("3"))
        {
            Consulta datos1 = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_id = '")).append(unidad).append("', unid_desc = '").append(descrip).append("',").append("unid_empresa = '").append(comp_nueva).append("',area = '").append(area_nueva).append("' ").append("WHERE (unid_id = '").append(ant_unidad).append("') ").append("AND (unid_empresa = '").append(comp_ant).append("')")));
            OutMessage.OutMessagePrint("-->update unidad: ".concat(String.valueOf(String.valueOf(consulta))));
            datos1.insert(consulta);
            datos1.close();
            Consulta datos2 = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET compania = '")).append(comp_nueva).append("', nodo_id = '").append(unidad).append("' ").append("WHERE (compania = '").append(comp_ant).append("') AND (nodo_id = '").append(ant_unidad).append("')")));
            OutMessage.OutMessagePrint("-->update jerarquia: ".concat(String.valueOf(String.valueOf(consulta))));
            datos2.insert(consulta);
            datos2.close();
            Periodo peri = new Periodo(Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_nueva, unidad, "U", dot_aut_dir, Conexion);
            actualizaDotAut(peri.getPeriodo(), comp_nueva, unidad, "R", dot_aut_ind, Conexion);
            Usuario.SuperNodo = null;
            RefrescaDatos(unidad, descrip, comp_nueva, modo, req, resp, Conexion);
        }
        if(modo.equals("1"))
            if(!Existe(comp_ant, unidad, Conexion))
            {
                Consulta datos1 = new Consulta(Conexion);
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidades (unid_empresa, unid_id, unid_desc, area,vigente) VALUES ('")).append(comp_ant).append("', '").append(unidad).append("', '").append(descrip).append("',").append(area_nueva).append(",'S'").append(")")));
                OutMessage.OutMessagePrint("Paso 0:Insert unidad: ".concat(String.valueOf(String.valueOf(consulta))));
                datos1.insert(consulta);
                datos1.close();
                Consulta buscar = new Consulta(Conexion);
                int maximo = 0;
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT MAX(nodo_corr) AS maximo FROM eje_ges_jerarquia GROUP BY nodo_padre, compania HAVING (nodo_padre = '")).append(ant_unidad).append("') ").append("AND (compania = '").append(comp_ant).append("')")));
                buscar.exec(consulta);
                if(buscar.next())
                {
                    OutMessage.OutMessagePrint("paso 1:buscar maximo corr(el nodo es padre);".concat(String.valueOf(String.valueOf(consulta))));
                    maximo = buscar.getInt("maximo");
                } else
                {
                    consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET nodo_hijos = 1 where (nodo_id = '")).append(ant_unidad).append("') ").append("AND (compania = '").append(comp_ant).append("')")));
                    buscar.insert(consulta);
                    consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT nodo_corr AS corr FROM eje_ges_jerarquia where (nodo_id = '")).append(ant_unidad).append("') ").append("AND (compania = '").append(comp_ant).append("')")));
                    OutMessage.OutMessagePrint("paso 1:buscar maximo corr(el nodo no es padre);".concat(String.valueOf(String.valueOf(consulta))));
                    buscar.exec(consulta);
                    buscar.next();
                    maximo = buscar.getInt("corr");
                }
                consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET nodo_corr = nodo_corr + 1 WHERE (nodo_corr > ")).append(maximo).append(") ").append("AND (compania = '").append(comp_ant).append("')")));
                OutMessage.OutMessagePrint("Paso 2:reasignar correlativos;".concat(String.valueOf(String.valueOf(consulta))));
                buscar.insert(consulta);
                int nivel = RescataNivelPadre(ant_unidad, Conexion) + 1;
                buscar.close();
                Consulta datos2 = new Consulta(Conexion);
                maximo++;
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_jerarquia (compania, nodo_id, nodo_padre, nodo_nivel, nodo_imagen,nodo_corr, nodo_hijos) VALUES ('")).append(comp_ant).append("', '").append(unidad).append("', '").append(ant_unidad).append("', ").append(nivel).append(", NULL, ").append(maximo).append(", 0)")));
                OutMessage.OutMessagePrint("Paso 3:-->Insert Jerarquia: ".concat(String.valueOf(String.valueOf(consulta))));
                datos2.insert(consulta);
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_rama (empresa, unidad, tipo, uni_rama) VALUES ('")).append(comp_ant).append("', '").append(unidad).append("', 'U','0')")));
                datos2.insert(consulta);
                OutMessage.OutMessagePrint("Paso 4:Insert Unidad Rama: ".concat(String.valueOf(String.valueOf(consulta))));
                datos2.close();
                Periodo peri = new Periodo(Conexion);
                actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "U", dot_aut_dir, Conexion);
                actualizaDotAut(peri.getPeriodo(), comp_ant, unidad, "R", dot_aut_ind, Conexion);
                Usuario.SuperNodo = null;
                RefrescaDatos(unidad, descrip, comp_ant, modo, req, resp, Conexion);
            } else
            {
                super.retTemplate(resp,"Gestion/MantUnidades/mensaje.htm",modelRoot);
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