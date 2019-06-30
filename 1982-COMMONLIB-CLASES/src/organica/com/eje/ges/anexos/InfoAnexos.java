package organica.com.eje.ges.anexos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class InfoAnexos extends MyHttpServlet
{

    public InfoAnexos()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    private void devolverPaginaBusqueda(HttpServletResponse resp, boolean tienePermiso)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("puede_mant", tienePermiso);
        super.retTemplate(resp,"Gestion/Anexos!main_anexos.htm",modelRoot);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\n-------->Entro a Generar Listados de anexos`para exportar");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String paramInfo = req.getParameter("info");
            System.out.println("Cual info: ".concat(String.valueOf(String.valueOf(paramInfo))));
            String registro = "";
            String rut = "";
            String nombre = "";
            String idCargo = "";
            String cargo = "";
            String idCco = "";
            String cco = "";
            String anexo = "";
            String fono = "";
            String fax = "";
            String path = "";
            Consulta consul = new Consulta(conexion);
            Consulta anexos = new Consulta(conexion);
            Consulta fonos = new Consulta(conexion);
            Consulta faxes = new Consulta(conexion);
            SimpleHash modelRoot = null;
            modelRoot = new SimpleHash();
            proper = ResourceBundle.getBundle("DataFolder");
            path = proper.getString("path.export.anexos");
            if("1".equals(paramInfo))
            {
                File archivo_rut = new File(String.valueOf(String.valueOf(path)).concat("/Listador.txt"));
                System.out.println("////****Ruta del Listado de Anexos rut:".concat(String.valueOf(String.valueOf(archivo_rut.getAbsolutePath()))));
                FileWriter salida = new FileWriter(archivo_rut);
                rut = "";
                nombre = "";
                idCargo = "";
                cargo = "";
                idCco = "";
                cco = "";
                anexo = "";
                fono = "";
                fax = "";
                String cabecera = "";
                cabecera = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(cabecera)))).append("RUT").append(";").append("NOMBRE").append(";").append("ID_CARGO").append(";").append("DESC_CARGO").append(";").append("COD_CCO").append(";").append("DESC_CCO").append(";").append("ANEXO").append(";").append("FONO").append(";").append("FAX").append("\n")));
                System.out.println("****CABECERA :".concat(String.valueOf(String.valueOf(cabecera))));
                salida.write(cabecera);
                String strMsg = "";
                String sql = "SELECT rut,digito_ver,ape_paterno,ape_materno,nombres,id_cargo, cargo, id_unidad, unidad FROM view_ges_rut_all ORDER BY view_ges_rut_all.rut ASC";
                consul.exec(sql);
                for(; consul.next(); salida.flush())
                {
                    anexo = " ";
                    fono = " ";
                    fax = " ";
                    rut = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consul.getString("rut"))))).append("-").append(consul.getString("digito_ver"))));
                    nombre = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consul.getString("ape_paterno").trim())))).append(" ").append(consul.getString("ape_materno").trim()).append(" ").append(consul.getString("nombres"))));
                    idCargo = consul.getString("id_cargo");
                    cargo = consul.getString("cargo");
                    idCco = consul.getString("id_unidad");
                    cco = consul.getString("unidad");
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.anexo as anexo, trab.anexo as id, trab.unidad, unid.publico FROM eje_ges_trabajadores_anexos trab INNER JOIN eje_ges_unidad_anexo unid ON eje_ges_trab.anexo = unid.anexo AND  trab.unidad = unid.unidad WHERE (trab.rut = ")).append(consul.getString("rut")).append(") ").append("and (trab.anexo<>'0')")));
                    anexos.exec(sql);
                    while(anexos.next()) 
                        anexo = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(anexo)))).append(anexos.getString("anexo")).append(" ")));
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.fono as fono, trab.fono as id, trab.unidad, unid.publico FROM eje_ges_trabajadores_fono trab INNER JOIN eje_ges_unidad_fono unid ON trab.fono = unid.fono AND  trab.unidad = unid.unidad WHERE (trab.rut = ")).append(consul.getString("rut")).append(") ").append("and (trab.fono<>'0')")));
                    fonos.exec(sql);
                    while(fonos.next()) 
                        fono = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fono)))).append(fonos.getString("fono")).append(" ")));
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT fax, fax as id, unidad FROM eje_ges_trabajadores_fax WHERE (rut = ")).append(consul.getString("rut")).append(") ").append("and (fax<>'0')")));
                    faxes.exec(sql);
                    while(faxes.next()) 
                        fax = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fax)))).append(faxes.getString("fax")).append(" ")));
                    registro = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(rut)))).append(";").append(nombre).append(";").append(idCargo).append(";").append(cargo).append(";").append(idCco).append(";").append(cco).append(";").append(anexo).append(";").append(fono).append(";").append(fax).append("\n")));
                    if(consul.getString("rut").equals("10024389"))
                        System.out.println("=========>Linea: ".concat(String.valueOf(String.valueOf(registro))));
                    salida.write(registro);
                }

                System.out.println("---------->Fin Listado por Rut<--------Ahora por Unidad-----");
            } else
            if("2".equals(paramInfo))
            {
                File archivo_unidad = new File(String.valueOf(String.valueOf(path)).concat("/ListadoU.txt"));
                System.out.println(String.valueOf(String.valueOf((new StringBuilder("////****Ruta del Listado de Anexos Unidades:")).append(archivo_unidad.getAbsolutePath()).append("/").append(archivo_unidad.getName()))));
                FileWriter salida2 = new FileWriter(archivo_unidad);
                String id_unidad = "";
                String unidad = "";
                String id_region = "";
                String region = "";
                anexo = "";
                fono = "";
                fax = "";
                int cont = 0;
                String cabecera = "";
                cabecera = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(cabecera)))).append("ID_CCO").append(";").append("CCOSTO").append(";").append("ID_REGION").append(";").append("DESC_REGION").append(";").append("ANEXO").append(";").append("FONO").append(";").append("FAX").append("\n")));
                System.out.println("****CABECERA UNIDADES:".concat(String.valueOf(String.valueOf(cabecera))));
                salida2.write(cabecera);
                String uni_anterior = "";
                String sql = "SELECT DISTINCT unid.unid_id AS id_unidad, unid.unid_desc AS unidad,  unid.unid_region AS id_region, reg.descrip AS region FROM eje_ges_unidades unid INNER JOIN   eje_ges_regiones reg ON   unid.unid_region = reg.region WHERE (unid.vigente = 'S') order by unidad";
                consul.exec(sql);
                while(consul.next()) 
                {
                    anexo = " ";
                    fono = " ";
                    fax = " ";
                    id_unidad = consul.getString("id_unidad");
                    unidad = consul.getString("unidad");
                    id_region = consul.getString("id_region");
                    region = consul.getString("region");
                    if(region != null)
                        region = region.trim();
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 15 unidad, empresa, anexo, anexo as id, publico FROM eje_ges_unidad_anexo  WHERE (unidad = '")).append(id_unidad).append("') ").append("and (anexo<>'0')")));
                    anexos.exec(sql);
                    while(anexos.next()) 
                        anexo = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(anexo)))).append(anexos.getString("anexo")).append(" ")));
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, empresa, fono, fono as id, publico FROM eje_ges_unidad_fono WHERE (unidad = '")).append(id_unidad).append("') ").append("and (fono<>'0')")));
                    fonos.exec(sql);
                    while(fonos.next()) 
                        fono = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fono)))).append(fonos.getString("fono")).append(" ")));
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, empresa, fax, fax as id FROM eje_ges_unidad_fax WHERE (unidad = '")).append(id_unidad).append("') ").append("and (fax<>'0')")));
                    faxes.exec(sql);
                    while(faxes.next()) 
                        fax = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(fax)))).append(faxes.getString("fax")).append(" ")));
                    if(uni_anterior.equals(id_unidad))
                        id_unidad = " ";
                    registro = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(id_unidad)))).append(";").append(unidad).append(";").append(id_region).append(";").append(region).append(";").append(anexo).append(";").append(fono).append(";").append(fax).append("\n")));
                    salida2.write(registro);
                    salida2.flush();
                    uni_anterior = consul.getString("id_unidad");
                }
            }
            consul.close();
            anexos.close();
            fonos.close();
            faxes.close();
            modelRoot.put("cerrar_sesion", req.getParameter("CS") != null);
            modelRoot.put("info", paramInfo);
            super.retTemplate(resp,"Gestion/Anexos/vent_export.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    ResourceBundle proper;
}