// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_BuscaJefe.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.Rut;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.verLicencias;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.verVacaciones;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_BuscaJefe extends MyHttpServlet
{

    public S_BuscaJefe()
    {
        vaca_desde = "";
        vaca_hasta = "";
        licencia_desde = "";
        licencia_hasta = "";
        listaresponsables = new SimpleList();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de BuscaJefe");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        Connection ConOrigen = super.connMgr.getConnection("portal");
        Connection ConDestino = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        if(ConOrigen == null || ConDestino == null)
        {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Busca Jefe".intern(), null);
            String empresa = "";
            empresa = user.getEmpresa();
            String tipo_sol = req.getParameter("tipo");
            String rut_jefe = SupDirecto(ConDestino, user.getRutId());
            Rut userRut = new Rut(ConDestino, rut_jefe);
            int nivel_jefe = userRut.Unidad_nivel;
            ParDisponible(ConDestino, ConOrigen, rut_jefe, empresa, nivel_jefe, req);
            modelRoot.put("tipo", tipo_sol);
            modelRoot.put("detalle", listaresponsables);
            listaresponsables = new SimpleList();
            super.retTemplate(resp,"misdatos/sol_vacaciones/Disponibles.htm",modelRoot);
        }
        super.connMgr.freeConnection("portal", ConDestino);
        super.connMgr.freeConnection("portal", ConOrigen);
        OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private String SupDirecto(Connection conn, String rut)
    {
        String valor = "";
        Consulta supdirecto = new Consulta(conn);
        if(conn != null)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,rut_supdirecto,dig_supdirecto,cargo_supdirecto, nom_supdirecto FROM eje_ges_supervisor WHERE (rut = ")).append(rut).append(")")));
            supdirecto.exec(sql);
            if(supdirecto.next())
                valor = supdirecto.getString("rut_supdirecto");
            else
                valor = "-1";
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
        supdirecto.close();
        return valor;
    }

    private void ParDisponible(Connection conDestino, Connection conOrigen, String rut, String empresa, int nivel_jefe, HttpServletRequest req)
    {
        String valor = "-1";
        String sql = "";
        String sql2 = "";
        String nombre = req.getParameter("nombre");
        String paterno = req.getParameter("paterno");
        String materno = req.getParameter("materno");
        System.out.println("---->Nivel del Jefe en ParDisponible: ".concat(String.valueOf(String.valueOf(nivel_jefe))));
        if(nivel_jefe > 0)
        {
            Consulta unidades = new Consulta(conDestino);
            Consulta responsables = new Consulta(conDestino);
            if(conDestino != null)
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id, nodo_nivel FROM eje_ges_workflow_jerarquia WHERE (compania = '")).append(empresa).append("') ").append("AND (nodo_nivel <= ").append(nivel_jefe).append(")")));
                OutMessage.OutMessagePrint("---->Buscando Unidades del nivel del Jefe:\n".concat(String.valueOf(String.valueOf(sql))));
                unidades.exec(sql);
                while(unidades.next()) 
                {
                    sql2 = String.valueOf(String.valueOf((new StringBuilder("SELECT empleados.emp_rut, empleados.emp_unidad,empleados.emp_cargo,empleados.emp_nombres,empleados.emp_paterno,empleados.emp_materno,eje_ges_workflow_usuario.user_rol FROM eje_ges_workflow_empleados empleados INNER JOIN eje_ges_workflow_usuario ON empleados.emp_rut = dbo.eje_ges_workflow_usuario.user_rut WHERE (empleados.emp_unidad = '")).append(unidades.getString("nodo_id")).append("') ").append("AND (eje_ges_workflow_usuario.user_rol = 4) ")));
                    if(!"".equals(nombre))
                        sql2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql2)))).append("AND (empleados.emp_nombres LIKE '%").append(nombre).append("%') ")));
                    if(!"".equals(paterno))
                        sql2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql2)))).append("AND (empleados.emp_paterno LIKE '%").append(paterno).append("%') ")));
                    if(!"".equals(materno))
                        sql2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql2)))).append("AND (empleados.emp_materno LIKE '%").append(materno).append("%') ")));
                    responsables.exec(sql2);
                    while(responsables.next()) 
                    {
                        SimpleHash simplehash1 = new SimpleHash();
                        if(Disponible(conOrigen, responsables.getString("emp_rut")))
                        {
                            valor = responsables.getString("emp_rut");
                            Rut datosRut = new Rut(conDestino, valor);
                            simplehash1.put("rut", valor);
                            simplehash1.put("nombre", datosRut.Nombre_completo);
                            simplehash1.put("unidad", datosRut.Unidad_desc);
                            simplehash1.put("cargo", datosRut.Cargo_desc);
                            listaresponsables.add(simplehash1);
                        }
                    }
                }
            } else
            {
                OutMessage.OutMessagePrint("en Class DestinatarioSol : Conexion es null ");
            }
            unidades.close();
        } else
        {
            valor = "ultimonivel";
        }
    }

    private String RecorreResponsables(Connection origen, Consulta rst)
    {
        String valor = "-1";
        System.err.println("***********Recorriendo Responsables");
        do
        {
            if(!rst.next())
                break;
            if(!Disponible(origen, rst.getString("emp_rut")))
                continue;
            valor = rst.getString("emp_rut");
            break;
        } while(true);
        return valor;
    }

    private boolean Disponible(Connection conOrigen, String rut)
    {
        boolean disponible = true;
        verVacaciones vacaciones = new verVacaciones(conOrigen, rut);
        if(vacaciones.deVacaciones())
        {
            OutMessage.OutMessagePrint("---->De Vacaciones");
            deVaca = true;
            vaca_desde = vacaciones.getInicioVaca();
            vaca_hasta = vacaciones.getFinVaca();
        } else
        {
            verLicencias licencias = new verLicencias(conOrigen, rut);
            if(licencias.conLicencia())
            {
                OutMessage.OutMessagePrint("---->con Licencia");
                conLicencia = true;
                licencia_desde = licencias.getInicioLicencia();
                licencia_hasta = licencias.getFinLicencia();
            }
        }
        if(deVaca || conLicencia)
            disponible = false;
        else
            disponible = true;
        deVaca = false;
        conLicencia = false;
        return disponible;
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("**********!!!Menu Left!!!***********");
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"WorkFlow/mensajelogin.html",modelRoot);
    }

    private Usuario user;
    private ResourceBundle proper;
    private Mensaje mensaje;
    private boolean deVaca;
    private String vaca_desde;
    private String vaca_hasta;
    private boolean conLicencia;
    private String licencia_desde;
    private String licencia_hasta;
    private SimpleList listaresponsables;
}