package organica.arbol.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.arbol.MiArbolTM;
import organica.arbol.Nodo;
import organica.arbol.Organica;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.servlet.GetParam;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class LoadTree extends MyHttpServlet
{

    public LoadTree()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
    	portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req); 
    	
        user = Usuario.rescatarUsuario(req);
        String unidad_administrativa = getUnidadAdm();

        if(req.getParameter("reload") != null || Usuario.SuperNodo == null)
        {
            OutMessage.OutMessagePrint("Recargar Arbol...");
            portal.com.eje.serhumano.user.Usuario user2 = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
            String[] privilegio = new String[1];
            privilegio[0] = "";
            if( user2.tieneApp("adm_cp") ) {
            	privilegio[0] = String.valueOf( (new StringBuilder("app_")).append("adm_cp") );
            }
            else if( user2.tieneApp("adm_gp") )
            	privilegio[0] = String.valueOf( (new StringBuilder("app_")).append("adm_gp") );
            Organica.main(privilegio);
        }
        if(user.esValido())
        {
//        	MMA 20170112
//    	    java.sql.Connection Conexion = connMgr.getConnection("portal");
    	    Connection Conexion = getConnMgr().getConnection("portal");
            FiltroUnidad fu = new FiltroUnidad(user);
            ControlAcceso control = new ControlAcceso(user);
            String html = null;
            resp.setContentType("text/html");
            resp.setHeader("Expires", "0");
            resp.setHeader("Pragma", "no-cache");
            SimpleHash modelRoot = new SimpleHash();
            html = String.valueOf(String.valueOf(req.getParameter("pag")));

            String HTM = "Gestion/arbol/";
            if(req.getParameter("arbol") != null)
            {
                if(!req.getParameter("arbol").equals(""))
                    HTM = String.valueOf(HTM) + String.valueOf(req.getParameter("arbol"));
            } else
            {
                HTM = "Gestion/arbol/miarbol.htm";
            }
            /* Rescata la empresa de uni_sup*/
            String empresa = "";
            String sqlEmp = "";
            Consulta conEmp = new Consulta(Conexion);
            sqlEmp = "SELECT empresa FROM eje_ges_uni_sup";
            conEmp.exec(sqlEmp);
            if(conEmp.next())
        	  empresa = conEmp.getString("empresa");
            conEmp.close();
            //************************************      
            
            MiArbolTM arboTm = new MiArbolTM(getTemplate(HTM), user, req, empresa);
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
                String sql = null;
            	  if( unidad_administrativa.equals("0") ) {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 1 unid_id, empresa_trab FROM view2_ges_busqueda_unidad_administrativa WHERE convert(varchar,rut) + '-' + digito = '")).append(paramBusRut).append("'")));
              	  }
              	  else {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 1 unid_id, empresa_trab FROM view2_ges_busqueda WHERE convert(varchar,rut) + '-' + digito = '")).append(paramBusRut).append("'")));
              	  }
                
                if(!control.tienePermiso("df_jer_comp"))
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
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
                String sql = null;
            	  if( unidad_administrativa.equals("0") ) {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda_unidad_administrativa  WHERE (nombres LIKE '%")).append(paramBusNom).append("%') ")));
              	  }
              	  else {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda  WHERE (nombres LIKE '%")).append(paramBusNom).append("%') ")));
              	  }
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
                if(!control.tienePermiso("df_jer_comp"))
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
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
            if(!"".equals(paramBusPat)) {
                modelRoot.put("bp", paramBusPat);
                Consulta consul = new Consulta(Conexion);
                String sql = "";
            	  if( unidad_administrativa.equals("0") ) {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda_unidad_administrativa WHERE (ape_paterno LIKE '%")).append(paramBusPat).append("%') ")));
              	  }
              	  else {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda  WHERE (ape_paterno LIKE '%")).append(paramBusPat).append("%') ")));
              	  }

                  if(!"".equals(paramBusMat))
                  {
                      sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (ape_materno LIKE '%").append(paramBusMat).append("%')")));
                      modelRoot.put("bm", paramBusMat);
                  }
                if(!control.tienePermiso("df_jer_comp"))
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
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
                String sql = "";
            	  if( unidad_administrativa.equals("0") ) {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda_unidad_administrativa WHERE (ape_materno LIKE '%")).append(paramBusMat).append("%') ")));
              	  }
              	  else {
                      sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view2_ges_busqueda  WHERE (ape_materno LIKE '%")).append(paramBusMat).append("%') ")));
              	  }

                if(!control.tienePermiso("df_jer_comp"))
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
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
                    String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa as empresa_trab,hijo as unid_id FROM VIEW_GES_arbol WHERE (hijo = '")).append(paramBusUni).append("') order by empresa")));
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
                String sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM vista_ges_unidades_dep WHERE (vigente = 'S') ";
                if(!control.tienePermiso("df_jer_comp"))
                    sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
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
            modelRoot.put("unidad", fu.getUnidadRel().getUnidad());
            modelRoot.put("empresa", fu.getUnidadRel().getEmpresa());
            modelRoot.put("Param", new GetParam(req));
//        	MMA 20170112
//          connMgr.freeConnection("portal", Conexion);
            getConnMgr().freeConnection("portal", Conexion);
            
            super.retTemplate(resp,html,modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
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

	private String getUnidadAdm() {
		String unidad_administrativa = "";
		ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
		try {
		  unidad_administrativa = proper.getString("portal.unidad_administrativa");
		}
		catch(MissingResourceException e) {
		        OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
		}
		return unidad_administrativa;
	}

    private Usuario user;
}