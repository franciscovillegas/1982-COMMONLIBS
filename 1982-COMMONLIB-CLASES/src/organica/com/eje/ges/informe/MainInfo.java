package organica.com.eje.ges.informe;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.arbol.MiArbolTM;
import organica.arbol.Nodo;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MainInfo extends MyHttpServlet
{

    public MainInfo()
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
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de MainInfo");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        FiltroUnidad fu = new FiltroUnidad(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        } else
        {
            GregorianCalendar Fecha = new GregorianCalendar();
            int ano = Fecha.get(1);
            int mes = Fecha.get(2) + 1;
            SimpleHash modelRoot = new SimpleHash();
            String HTM = "Gestion/arbol/miarbol.htm";
            MiArbolTM arboTm = new MiArbolTM(getTemplate(HTM), user, req);
            modelRoot.put("TraeArbol", new MiArbolTM(getTemplate(HTM), user));
            modelRoot.put("Control", new ControlAccesoTM(control));
            int pos = 1;
            if(req.getParameter("pos") != null)
                pos = Integer.parseInt(req.getParameter("pos"));
            System.err.println("****>(Busqueda Organica) param Posicion(donde comenzar): ".concat(String.valueOf(String.valueOf(String.valueOf(pos)))));
            String formulario = "";
            if(req.getParameter("nameform") != null)
                formulario = req.getParameter("nameform");
            System.err.println("******Formulario: ".concat(String.valueOf(String.valueOf(formulario))));
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("------>Param Mes: ")).append(req.getParameter("peri_mes")).append("\n------>Param A\361o: ").append(req.getParameter("peri_ano")))));
            if(req.getParameter("peri_mes") != null)
                modelRoot.put("mes", req.getParameter("peri_mes"));
            else
                modelRoot.put("mes", String.valueOf(mes));
            if(req.getParameter("peri_ano") != null)
                modelRoot.put("ano", req.getParameter("peri_ano"));
            else
                modelRoot.put("ano", String.valueOf(ano));
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
            String sql;
            Consulta consul;
            if(!"".equals(paramBusRut))
            {
                modelRoot.put("br", paramBusRut);
                consul = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 1 unid_id, empresa_trab FROM view_ges_busqueda  WHERE convert(varchar,rut) + '-' + digito = '")).append(paramBusRut).append("'")));
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
                consul = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_ges_busqueda  WHERE (nombres LIKE '%")).append(paramBusNom).append("%') ")));
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
                if(posFinal > Registros(sql, conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            } else
            if(!"".equals(paramBusPat))
            {
                modelRoot.put("bp", paramBusPat);
                consul = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_ges_busqueda  WHERE (ape_paterno LIKE '%")).append(paramBusPat).append("%') ")));
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
                if(posFinal > Registros(sql, conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            } else
            if(!"".equals(paramBusMat))
            {
                modelRoot.put("bm", paramBusMat);
                consul = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct unid_id, empresa_trab FROM view_ges_busqueda  WHERE (ape_materno LIKE '%")).append(paramBusMat).append("%') ")));
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
                    OutMessage.OutMessagePrint("****>Posicion NO encontrada");
                    itera++;
                } while(true);
                posFinal = itera;
                if(posFinal > Registros(sql, conexion))
                    modelRoot.put("fin", "1");
                consul.close();
            }
            if(paramBusUni != null)
            {
                System.err.println(String.valueOf(String.valueOf((new StringBuilder("******Unidad: ")).append(paramBusUni).append("  --->empresa: ").append(paramBusEmp))));
                if(paramBusEmp == null)
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa as empresa_trab,hijo as unid_id FROM VIEW_GES_arbol WHERE (hijo = '")).append(paramBusUni).append("') order by empresa")));
                    OutMessage.OutMessagePrint("Buscar Unidad Recursivo --> ".concat(String.valueOf(String.valueOf(sql))));
                    consul = new Consulta(conexion);
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
                        OutMessage.OutMessagePrint("****>Posicion NO encontrada");
                        itera++;
                    } while(true);
                    posFinal = itera;
                    if(posFinal > Registros(sql, conexion))
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
            OutMessage.OutMessagePrint("cargar combos");
            consul = new Consulta(conexion);
            sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM vista_ges_unidades_dep WHERE (vigente = 'S') ";
            if(!control.tienePermiso("df_jer_comp"))
                sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
            sql = String.valueOf(String.valueOf(sql)).concat(" order by unid_desc");
            consul.exec(sql);
            modelRoot.put("unidades", consul.getSimpleList());
            consul.close();
            modelRoot.put("TraeArbol", arboTm);
            modelRoot.put("Control", new ControlAccesoTM(control));
            System.err.println("****>Posicion para comenzar la sgte busqueda: ".concat(String.valueOf(String.valueOf(posFinal))));
            modelRoot.put("pos", String.valueOf(posFinal));
            modelRoot.put("nameform", formulario);
            modelRoot.put("unidad", fu.getUnidadRel().getUnidad());
            modelRoot.put("empresa", fu.getUnidadRel().getEmpresa());
            Consulta consul2 = new Consulta(conexion);
            sql = "SELECT distinct cargo id , descrip des FROM eje_ges_cargos where vigente = 'S'  order by descrip";
            consul2.exec(sql);
            modelRoot.put("cargos", consul2.getSimpleList());
            consul2.close();
            Consulta consul3 = new Consulta(conexion);
            sql = "SELECT motivo_id id, motivo_desc des FROM eje_ges_mot_retiro";
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul3.exec(sql);
            modelRoot.put("motivos", consul3.getSimpleList());
            consul3.close();
            Consulta consul7 = new Consulta(conexion);
            sql = "SELECT codigo AS nivel FROM eje_ges_escala_salarial ORDER BY codigo";
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul7.exec(sql);
            modelRoot.put("niveles", consul7.getSimpleList());
            consul7.close();
            modelRoot.put("peri_remu", (new Periodo(conexion)).getPeriodo());
            String filtroAdic = "";
            if(user.veMultiplesRamas())
            {
                filtroAdic = String.valueOf(String.valueOf((new StringBuilder("(EXISTS ( SELECT unid_emp, unid_id FROM vista_ges_unidades_dep AS vu WHERE (")).append(fu.getFiltro()).append(") AND ((vu.unid_emp = info.unid_empresa) AND  (vu.unid_id = info.unid_id)))) AND ")));
                OutMessage.OutMessagePrint("--> Filtro Adicional\n".concat(String.valueOf(String.valueOf(filtroAdic))));
                modelRoot.put("filtro_adic", filtroAdic);
            }
            String puede_ver = "NADA";
            if(user.getPermisos().getPermiso("df_exp_remu") != null)
                puede_ver = user.getPermisos().getPermiso("df_exp_remu").getPuedeVer();
            modelRoot.put("puede_ver_remu", puede_ver);
            modelRoot.put("acceso1", "df_exp_remu");
            puede_ver = "NADA";
            if(user.getPermisos().getPermiso("df_exp_adm_tiempo") != null)
                puede_ver = user.getPermisos().getPermiso("df_exp_adm_tiempo").getPuedeVer();
            modelRoot.put("puede_ver_adm", puede_ver);
            modelRoot.put("acceso2", "df_exp_adm_tiempo");
            puede_ver = "NADA";
            if(user.getPermisos().getPermiso("df_exp_dat_pers") != null)
                puede_ver = user.getPermisos().getPermiso("df_exp_dat_pers").getPuedeVer();
            modelRoot.put("puede_ver_pers", puede_ver);
            modelRoot.put("acceso3", "df_exp_dat_pers");
            modelRoot.put("rut_user", user.getRutUsuario());
            super.retTemplate(resp,"Gestion/Informes/main_info_arbol.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"mensaje.htm",modelRoot);
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
    private Mensaje mensaje;
}