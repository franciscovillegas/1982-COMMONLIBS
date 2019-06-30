package organica.com.eje.ges.gestion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class DatosContrato extends MyHttpServlet
{

    public DatosContrato()
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
        GeneraDatos(req, resp);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        String digito = req.getParameter("dig");
        if(rut == null || rut.equals(""))
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Datos Contractuales", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_exp_dat_cont", rut))
        {
            mensaje.devolverPaginaMensage(resp, "Datos Contractuales", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            int anos = 0;
            SimpleHash modelRoot = new SimpleHash();
            String dato = null;
            OutMessage.OutMessagePrint("---->RUT: " + rut);
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            consulta = "select digito_ver,id_foto,e_mail,anexo,nombre,id_unidad, unidad,area,cargo,ccosto,conv_laboral,sindicato,renta_reg_mensual,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto,ubic_fisica,division,categ_cargo,tipo_poder,tip_contrato,term,fecha_ingreso,antiguedad,xcorp,xunidad,xcargo,inghold,fec_unidad,ingcargo, segsalcom,id_centro_trabajo from view_ges_InfoRut where rut = " + rut;
            OutMessage.OutMessagePrint("---->consulta: " + consulta);
            info.exec(consulta);
            info.next();
            digito = info.getString("digito_ver");
            modelRoot.put("foto", info.getString("id_foto"));
            modelRoot.put("rut", rut);
            modelRoot.put("rut2", Tools.setFormatNumber(rut) + "-" + digito);
            modelRoot.put("mail", valida.validarDato(info.getString("e_mail"), "NR"));
            modelRoot.put("anexo", valida.validarDato(info.getString("anexo"), "NR"));
            modelRoot.put("nombre", info.getString("nombre"));
            modelRoot.put("unidad", info.getString("unidad"));
            modelRoot.put("id_unidad", info.getString("id_unidad"));
            modelRoot.put("area", info.getString("area"));
            modelRoot.put("cargo", info.getString("cargo"));
            modelRoot.put("cco", valida.validarDato(info.getString("ccosto"), "NR"));
            modelRoot.put("id_cco", valida.validarDato(info.getString("id_centro_trabajo"), "NR"));
            modelRoot.put("conv_laboral", info.getString("conv_laboral"));
            modelRoot.put("sindicato", info.getString("sindicato"));
            if(control.tienePermiso("df_exp_remu", rut))
                modelRoot.put("renta", tool.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
            modelRoot.put("rut_supervisor", Tools.setFormatNumber(info.getString("rut_supdirecto")) + "-" + info.getString("dig_supdirecto"));
            modelRoot.put("supervisor", info.getString("nom_supdirecto"));
            modelRoot.put("cargo_supervisor", info.getString("cargo_supdirecto"));
            modelRoot.put("ubicacion", info.getString("ubic_fisica"));
            modelRoot.put("division", info.getString("division"));
            modelRoot.put("categoria", info.getString("categ_cargo"));
            modelRoot.put("tipo_poder", info.getString("tipo_poder"));
            modelRoot.put("tipo_contrato", info.getString("tip_contrato"));
            modelRoot.put("termino", valida.validarFecha(info.getValor("term")));
            modelRoot.put("fec_ingreso", valida.validarFecha(info.getValor("fecha_ingreso")));
            dato = valida.validarDato(info.getString("antiguedad"), "0");
            OutMessage.OutMessagePrint("------>Antiguedad= " + info.getValor("antiguedad"));
            anos = Integer.parseInt(dato) / 12;
            if(anos < 0)
                anos *= -1;
            modelRoot.put("xempre", String.valueOf(anos));
            dato = valida.validarDato(info.getString("xcorp"), "0");
            anos = Integer.parseInt(dato) / 12;
            if(anos < 0)
                anos *= -1;
            modelRoot.put("xcorp", String.valueOf(anos));
            
            if( info.getValor("inghold") != null 
            	&& info.getValor("fecha_ingreso") != null
            	&& !valida.validarFecha(info.getValor("fecha_ingreso")).equals(valida.validarFecha(info.getValor("inghold"))) ) {
            	
            	modelRoot.put("ingre_corp", valida.validarFecha(info.getValor("inghold")));
            }
            else {
            	modelRoot.put("ingre_corp",  "n/a");
            }
            
            modelRoot.put("fec_unidad", valida.validarFecha(info.getValor("fec_unidad")));
            dato = valida.validarDato(info.getString("xunidad"), "0");
            anos = Integer.parseInt(dato) / 12;
            if(anos < 0)
                anos *= -1;
            modelRoot.put("xunidad", String.valueOf(anos));
            modelRoot.put("ingre_cargo", valida.validarFecha(info.getValor("ingcargo")));
            dato = valida.validarDato(info.getString("xcargo"), "0");
            anos = Integer.parseInt(dato) / 12;
            if(anos < 0)
                anos *= -1;
            modelRoot.put("xcargo", String.valueOf(anos));
            modelRoot.put("segsalcom", valida.validarDato(info.getString("segsalcom")));
            info.close();
            modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
            super.retTemplate(resp,"Gestion/InfoUsuario/DatosContrato.htm",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}