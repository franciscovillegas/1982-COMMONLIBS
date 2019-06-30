package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Buscar_por_Concepto extends MyHttpServlet
{

    public Buscar_por_Concepto()
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
        ArmaConsulta(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void ArmaConsulta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            control = new ControlAcceso(user);
            int cual = Integer.parseInt(req.getParameter("Operacion"));
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("\n--> Inicio Buscar_por_Concepto (cual: ")).append(cual).append(")"))));
            switch(cual)
            {
            case 1: // '\001'
                PagIngreso(req, resp, Conexion);
                break;

            case 2: // '\002'
                DespResultado(req, resp, Conexion);
                break;

            case 3: // '\003'
                DespDatos(req, resp, Conexion);
                break;
            }
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("--> Termino");
    }

    private void PagIngreso(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda por Filtro", "Tiempo de Sesi\363n expirado...");
        } else
        {
            Consulta Conceptos = new Consulta(Conexion);
            Consulta Comodin = new Consulta(Conexion);
            Consulta Valores = new Consulta(Conexion);
            consulta = "select codconcepto as cod,descripcion as des from eje_ges_conceptos order by orden";
            OutMessage.OutMessagePrint("consulta concepto : ---> ".concat(String.valueOf(String.valueOf(consulta))));
            Conceptos.exec(consulta);
            modelRoot.put("conceptos", Conceptos.getSimpleList());
            FiltroUnidad fu = new FiltroUnidad(user);
            OutMessage.OutMessagePrint("parametro concepto : ---> ".concat(String.valueOf(String.valueOf(req.getParameter("concepto")))));
            if(req.getParameter("concepto") != null && !req.getParameter("concepto").trim().equals(""))
            {
                modelRoot.put("conc", req.getParameter("concepto").toString());
                if("105".equals(req.getParameter("concepto").trim()))
                {
                    String sql = "SELECT DISTINCT unid_id AS cod, unid_desc AS des FROM vista_ges_unidades_dep WHERE (vigente = 'S') ";
                    if(!control.tienePermiso("df_jer_comp"))
                        sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
                    sql = String.valueOf(String.valueOf(sql)).concat(" order by unid_desc");
                    OutMessage.OutMessagePrint("consulta valores: ---> ".concat(String.valueOf(String.valueOf(sql))));
                    Valores.exec(sql);
                } else
                {
                    consulta = "select * from eje_ges_conceptos where codconcepto=".concat(String.valueOf(String.valueOf(req.getParameter("concepto").toString())));
                    Comodin.exec(consulta);
                    Comodin.next();
                    if(Comodin.getString("tabla").equals("cargos"))
                    {
                        OutMessage.OutMessagePrint("Tabla Cargos Vigentes: ---> ".concat(String.valueOf(String.valueOf(Comodin.getString("tabla")))));
                        consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct  ")).append(Comodin.getString("campocodigo")).append(" as cod ,").append(Comodin.getString("campodesc")).append(" as des,vigente from eje_ges_").append(Comodin.getString("tabla")).append(" where (vigente = 'S') ").append(" order by ").append(Comodin.getString("campodesc"))));
                    } else
                    {
                        consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct  ")).append(Comodin.getString("campocodigo")).append(" as cod ,").append(Comodin.getString("campodesc")).append(" as des from eje_ges_").append(Comodin.getString("tabla")).append(" order by ").append(Comodin.getString("campodesc"))));
                    }
                    OutMessage.OutMessagePrint("consulta valores: ---> ".concat(String.valueOf(String.valueOf(consulta))));
                    Valores.exec(consulta);
                }
                modelRoot.put("valores", Valores.getSimpleList());
            }
        }
        super.retTemplate(resp,"Gestion/Buscar/form_busqueda_concepto.html",modelRoot);
    }

    private void DespDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String consulta = null;
        String rut = null;
        SimpleHash modelRoot = new SimpleHash();
        if(req.getParameter("rut") != "")
        {
            rut = req.getParameter("rut");
            Consulta Buscar = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("Select rut,digito_ver,nombre,sueldo,CONVERT(varchar,fecha_nacim,6) as fecha_nacim,domicilio,id_foto as foto from view_ges_InfoRut where rut ='")).append(rut).append("'")));
            Buscar.exec(consulta);
            modelRoot.put("varios", Buscar.getSimpleList());
        } else
        {
            modelRoot.put("mensaje", "Debe un ingresar el nombre para consultar");
        }
        super.retTemplate(resp,"Gestion/Buscar/Desp_Datos.html",modelRoot);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String consulta = null;
        String Bus = null;
        String Bus2 = "";
        String Boton = "";
        String valordesc = "";
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda por Filtro", "Tiempo de Sesi\363n expirado...");
        } else
        {
            Consulta Comodin = new Consulta(Conexion);
            Consulta Buscar = new Consulta(Conexion);
            Consulta Valores = new Consulta(Conexion);
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("FNum", new FormatoNumero());
            Consulta Conceptos = new Consulta(Conexion);
            consulta = "select codconcepto as cod,descripcion as des from eje_ges_conceptos order by orden";
            OutMessage.OutMessagePrint("consulta concepto : ---> ".concat(String.valueOf(String.valueOf(consulta))));
            Conceptos.exec(consulta);
            modelRoot.put("conceptos", Conceptos.getSimpleList());
            FiltroUnidad fu = new FiltroUnidad(user);
            String emp = fu.getUnidadRel().getEmpresa();
            String uni = fu.getUnidadRel().getUnidad();
            OutMessage.OutMessagePrint("parametro concepto : ---> ".concat(String.valueOf(String.valueOf(req.getParameter("concepto")))));
            if(req.getParameter("concepto") != null && !req.getParameter("concepto").trim().equals(""))
            {
                modelRoot.put("conc", req.getParameter("concepto").toString());
                if("105".equals(req.getParameter("concepto").trim()))
                {
                    String sql = "SELECT DISTINCT unid_id AS cod, unid_desc AS des FROM vista_ges_unidades_dep WHERE (vigente = 'S')";
                    if(!control.tienePermiso("df_jer_comp"))
                        sql = String.valueOf(sql) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and ( ")).append(fu.getFiltro()).append(" )"))));
                    sql = String.valueOf(String.valueOf(sql)).concat(" order by unid_desc");
                    Valores.exec(sql);
                } else
                {
                    consulta = "select * from eje_ges_conceptos where codconcepto=".concat(String.valueOf(String.valueOf(req.getParameter("concepto").toString())));
                    Comodin.exec(consulta);
                    Comodin.next();
                    consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct  ")).append(Comodin.getString("campocodigo")).append(" as cod ,").append(Comodin.getString("campodesc")).append(" as des from eje_ges_").append(Comodin.getString("tabla")).append(" order by ").append(Comodin.getString("campodesc"))));
                    OutMessage.OutMessagePrint("consulta valores: ---> ".concat(String.valueOf(String.valueOf(consulta))));
                    Valores.exec(consulta);
                }
                modelRoot.put("valores", Valores.getSimpleList());
                if(!req.getParameter("valor").trim().equals(""))
                {
                    consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct ")).append(Comodin.getString("campocodigo")).append(" as cod ,").append(Comodin.getString("campodesc")).append(" as des from eje_ges_").append(Comodin.getString("tabla")).append(" where ").append(Comodin.getString("campocodigo")).append("='").append(req.getParameter("valor").toString()).append("'")));
                    OutMessage.OutMessagePrint("consulta valores: ---> ".concat(String.valueOf(String.valueOf(consulta))));
                    Valores.exec(consulta);
                    Valores.next();
                    valordesc = Valores.getString("des");
                }
            }
            Bus = "SELECT DISTINCT rut, digito as digito_ver, nombres, ape_paterno, ape_materno FROM view_ges_busqueda WHERE (1=1)";
            if(!control.tienePermiso("df_jer_comp"))
                Bus = String.valueOf(Bus) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" AND ( ")).append(fu.getFiltro()).append(" ) "))));
            String operador = "";
            String campo = "";
            String valor = "";
            if(!req.getParameter("operador").trim().equals(""))
            {
                OutMessage.OutMessagePrint("operador  : ".concat(String.valueOf(String.valueOf(req.getParameter("operador").trim()))));
                operador = req.getParameter("operador").trim();
            }
            if(!req.getParameter("concepto").trim().equals(""))
            {
                OutMessage.OutMessagePrint("concepto  : ".concat(String.valueOf(String.valueOf(req.getParameter("concepto").trim()))));
                consulta = "select campo_vista as c from eje_ges_conceptos where codconcepto=".concat(String.valueOf(String.valueOf(req.getParameter("concepto").toString())));
                Comodin.exec(consulta);
                Comodin.next();
                campo = Comodin.getString("c");
            }
            if(!req.getParameter("valor").trim().equals(""))
            {
                OutMessage.OutMessagePrint("concepto  : ".concat(String.valueOf(String.valueOf(req.getParameter("concepto").trim()))));
                valor = req.getParameter("valor").trim().toString();
                modelRoot.put("val", valor);
            }
            if(operador != "LIKE")
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ")).append(campo).append(" ").append(operador).append("'").append(valor).append("' ")));
            else
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ")).append(campo).append(" ").append(operador).append("'%").append(valor).append("%' ")));
            String sel1 = "";
            String sel2 = "";
            String sel3 = "";
            String sel4 = "";
            if(!req.getParameter("sel1").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel1  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel1").trim()))));
                sel1 = req.getParameter("sel1").trim();
                if(sel1.trim().equals("Rut"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                if(sel1.trim().equals("Nombre"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                if(sel1.trim().equals("A. Paterno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                if(sel1.trim().equals("A. Materno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
            }
            if(!req.getParameter("sel2").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel2 : ".concat(String.valueOf(String.valueOf(req.getParameter("sel2").trim()))));
                if(!req.getParameter("sel2").trim().equals(sel1))
                {
                    sel2 = req.getParameter("sel2").trim();
                    if(sel2.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel2.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel2.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel2.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(!req.getParameter("sel3").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel3  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel3").trim()))));
                if(!req.getParameter("sel3").trim().equals(sel1) && !req.getParameter("sel3").trim().equals(sel2))
                {
                    sel3 = req.getParameter("sel4").trim();
                    if(sel3.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel3.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel3.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel3.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(!req.getParameter("sel4").trim().equals(""))
            {
                OutMessage.OutMessagePrint("sel4  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel4").trim()))));
                if(!req.getParameter("sel4").trim().equals(sel1) && !req.getParameter("sel4").trim().equals(sel2) && !req.getParameter("sel4").trim().equals(sel3))
                {
                    sel4 = req.getParameter("sel4").trim();
                    if(sel4.trim().equals("Rut"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                    if(sel4.trim().equals("Nombre"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                    if(sel4.trim().equals("A. Paterno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                    if(sel4.trim().equals("A. Materno"))
                        Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
                }
            }
            if(!Boton.trim().equals(""))
            {
                Boton = Boton.substring(0, Boton.length() - 1);
                Boton = " ORDER BY ".concat(String.valueOf(String.valueOf(Boton)));
            }
            consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Bus)))).append(Bus2).append(Boton)));
            OutMessage.OutMessagePrint("SELECT : ".concat(String.valueOf(String.valueOf(consulta))));
            Buscar.exec(consulta);
            modelRoot.put("varios", Buscar.getSimpleList());
            modelRoot.put("Busqueda", String.valueOf(String.valueOf((new StringBuilder("Buscar Empleados que cumplan con lo Siguiente: ")).append(campo).append(" que sea ").append(req.getParameter("operador").toString()).append(" ").append(valordesc))));
        }
        super.retTemplate(resp,"Gestion/Buscar/Desp_busqueda_concepto.html",modelRoot);
    }

    private Usuario user;
    private ControlAcceso control;
    private Mensaje mensaje;
}