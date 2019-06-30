package portal.com.eje.serhumano.admin.arbol.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.admin.arbol.MiArbolTM;
import portal.com.eje.serhumano.admin.arbol.Nodo;
import portal.com.eje.serhumano.admin.arbol.Organica;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.ControlAccesoTM;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.UsuarioSuperNodo;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

public class LoadTree extends MyHttpServlet
{

    public LoadTree()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        if(req.getParameter("reload") != null ||UsuarioSuperNodo.getSuperNodo() == null)
        {
            OutMessage.OutMessagePrint("Recargar Arbol...");
            Organica.main(new String[1]);
        }
        user = SessionMgr.rescatarUsuario(req);
        if(user.esValido())
        {
            Connection Conexion = super.connMgr.getConnection("portal");
            ControlAcceso control = new ControlAcceso(user);
            String html = null;
            SimpleHash modelRoot = new SimpleHash();
            html = req.getParameter("pag");
            String HTM = "arbol/";
            if(req.getParameter("arbol") != null)
            {
                if(!req.getParameter("arbol").equals(""))
                    HTM = String.valueOf(HTM) + String.valueOf(req.getParameter("arbol"));
            } else
            {
                HTM = "arbol/miarbol.htm";
            }
            MiArbolTM arboTm = new MiArbolTM(getTemplate(HTM), user, req);
            int pos = 1;
            if(req.getParameter("pos") != null)
                pos = Integer.parseInt(req.getParameter("pos"));
            System.err.println("****>(Busqueda Organica) param Posicion(donde comenzar): ".concat(String.valueOf(String.valueOf(String.valueOf(pos)))));
            String formulario = "";
            if(req.getParameter("nameform") != null)
                formulario = req.getParameter("nameform");
            System.err.println("******Formulario: ".concat(String.valueOf(String.valueOf(formulario))));
            int itera = 1;
            int posFinal = 0;
            String paramBusEmp = req.getParameter("be");
            String paramBusUni = req.getParameter("bu");
            String paramBusRut = req.getParameter("br");
            String paramBusNom = req.getParameter("bn");
            String paramBusPat = req.getParameter("bp");
            String paramBusMat = req.getParameter("bm");
            System.out.println(String.valueOf(String.valueOf((new StringBuilder("Rut: ")).append(paramBusRut).append("\nNombre: ").append(paramBusNom).append("\nPaterno: ").append(paramBusPat).append("\nMaterno: ").append(paramBusMat))));
            if(paramBusEmp == null && paramBusUni == null && paramBusRut == null && paramBusNom == null && paramBusPat == null && paramBusMat == null)
                modelRoot.put("inicio", "1");
            if(!"".equals(paramBusRut))
            {
                modelRoot.put("br", paramBusRut);
                Consulta consul = new Consulta(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 1 unid_id, empresa_trab FROM view_busqueda  WHERE convert(varchar,rut) + '-' + digito = '")).append(paramBusRut).append("'")));
                OutMessage.OutMessagePrint("Buscar unid/rut --> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                if(consul.next())
                {
                    paramBusUni = consul.getString("unid_id");
                    paramBusEmp = consul.getString("empresa_trab");
                }
                consul.close();
            } else
            if(!"".equals(paramBusNom))
            {
                modelRoot.put("bn", paramBusNom);
                Consulta consul = new Consulta(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_busqueda  WHERE (nombres LIKE '%")).append(paramBusNom).append("%') ")));
                if(!"".equals(paramBusPat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (ape_paterno LIKE '%").append(paramBusPat).append("%')")));
                    modelRoot.put("bp", paramBusPat);
                }
                if(!"".equals(paramBusMat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (ape_materno LIKE '%").append(paramBusMat).append("%')")));
                    modelRoot.put("bm", paramBusMat);
                }
                OutMessage.OutMessagePrint("Buscar Nombre --> ".concat(String.valueOf(String.valueOf(sql))));
                OutMessage.OutMessagePrint("--->Pos(a buscar)= ".concat(String.valueOf(String.valueOf(pos))));
                consul.exec(sql);
                do
                {
                    if(!consul.next())
                        break;
                    OutMessage.OutMessagePrint("****>Iteracion: ".concat(String.valueOf(String.valueOf(itera))));
                    if(itera == pos)
                    {
                        paramBusUni = consul.getString("unid_id");
                        paramBusEmp = consul.getString("empresa_trab");
                        itera++;
                        break;
                    }
                    OutMessage.OutMessagePrint("****>Posicion NO encontrada");
                    itera++;
                } while(true);
                posFinal = itera;
                if(posFinal > Registros(sql, Conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            } else
            if(!"".equals(paramBusPat))
            {
                modelRoot.put("bp", paramBusPat);
                Consulta consul = new Consulta(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_busqueda  WHERE (ape_paterno LIKE '%")).append(paramBusPat).append("%') ")));
                if(!"".equals(paramBusMat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (ape_materno LIKE '%").append(paramBusMat).append("%')")));
                    modelRoot.put("bm", paramBusMat);
                }
                OutMessage.OutMessagePrint("Buscar Paterno --> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                do
                {
                    if(!consul.next())
                        break;
                    if(itera == pos)
                    {
                        paramBusUni = consul.getString("unid_id");
                        paramBusEmp = consul.getString("empresa_trab");
                        itera++;
                        break;
                    }
                    OutMessage.OutMessagePrint("****>Posicion NO encontrada");
                    itera++;
                } while(true);
                posFinal = itera;
                if(posFinal > Registros(sql, Conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            } else
            if(!"".equals(paramBusMat))
            {
                modelRoot.put("bm", paramBusMat);
                Consulta consul = new Consulta(Conexion);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_busqueda  WHERE (ape_materno LIKE '%")).append(paramBusMat).append("%') ")));
                OutMessage.OutMessagePrint("Buscar Materno --> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                do
                {
                    if(!consul.next())
                        break;
                    if(itera == pos)
                    {
                        paramBusUni = consul.getString("unid_id");
                        paramBusEmp = consul.getString("empresa_trab");
                        itera++;
                        break;
                    }
                    itera++;
                } while(true);
                posFinal = itera;
                if(posFinal > Registros(sql, Conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            }
            if(paramBusUni != null)
            {
                System.err.println(String.valueOf(String.valueOf((new StringBuilder("******Unidad: ")).append(paramBusUni).append("  --->empresa: ").append(paramBusEmp))));
                if(paramBusEmp == null)
                {
                    String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa as empresa_trab,hijo as unid_id FROM VIEW_arbol WHERE (hijo = '")).append(paramBusUni).append("') order by empresa")));
                    OutMessage.OutMessagePrint("Buscar Unidad Recursivo --> ".concat(String.valueOf(String.valueOf(sql))));
                    Consulta consul = new Consulta(Conexion);
                    consul.exec(sql);
                    do
                    {
                        if(!consul.next())
                            break;
                        if(itera == pos)
                        {
                            System.err.println("****>Posicion encontrada");
                            paramBusUni = consul.getString("unid_id");
                            paramBusEmp = consul.getString("empresa_trab");
                            itera++;
                            break;
                        }
                        itera++;
                    } while(true);
                    posFinal = itera;
                    if(posFinal > Registros(sql, Conexion))
                        modelRoot.put("fin", "1");
                    consul.close();
                }
                System.err.println(String.valueOf(String.valueOf((new StringBuilder("******(2)Unidad: ")).append(paramBusUni).append("  --->empresa: ").append(paramBusEmp))));
                if(paramBusEmp == null)
                    paramBusEmp = "";
                arboTm.setRamaNodo(new Nodo("", paramBusUni, "", paramBusEmp));
                modelRoot.put("be", paramBusEmp);
                modelRoot.put("bu", paramBusUni);
            }
            if(req.getParameter("tc") != null)
            {
                OutMessage.OutMessagePrint("cargar combos");
                Consulta consul = new Consulta(Conexion);
                String sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM vista_unidades_dep WHERE (vigente = 'S') ";
                sql = String.valueOf(String.valueOf(sql)).concat(" order by unid_desc");
                consul.exec(sql);
                modelRoot.put("unidades", consul.getSimpleList());
                consul.close();
            }
            modelRoot.put("TraeArbol", arboTm);
            modelRoot.put("Control", new ControlAccesoTM(control));
            System.err.println("****>Posicion para comenzar la sgte busqueda: ".concat(String.valueOf(String.valueOf(posFinal))));
            modelRoot.put("pos", String.valueOf(posFinal));
            modelRoot.put("nameform", formulario);
            modelRoot.put("Param", new GetParam(req));
            super.connMgr.freeConnection("portal", Conexion);
            super.retTemplate(resp,html,modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
    }

    private int Registros(String query, Connection Conexion)
    {
        int x = 0;
        Consulta info = new Consulta(Conexion);
        info.exec(query);
        while(info.next()) 
            x++;
        return x;
    }

    private Usuario user;
}