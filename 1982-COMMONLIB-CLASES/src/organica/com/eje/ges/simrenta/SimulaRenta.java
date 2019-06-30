package organica.com.eje.ges.simrenta;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.datos.ConexionODBC;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class SimulaRenta extends MyHttpServlet
{

    public SimulaRenta()
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
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de SimulaRenta");
        user = Usuario.rescatarUsuario(req);
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Simulador de Renta", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_sim_rta", strRut))
            mensaje.devolverPaginaMensage(resp, "Simulador de Renta", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, "", user, conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        String paramRutTrab = req.getParameter("rut");
        OutMessage.OutMessagePrint("RutTrab --> ".concat(String.valueOf(String.valueOf(paramRutTrab))));
        String html = req.getParameter("htm");
        if(html == null)
            html = "SimulaRenta.htm";
        OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("FFecha", new FormatoFecha());
        modelRoot.put("FNum", new FormatoNumero());
        try
        {
            String sql = "{call df_sim_situacionactual(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            ConexionODBC con = new ConexionODBC();
            Connection conex = con.Conecta();
            CallableStatement stm = conex.prepareCall(sql);
            stm.setInt(1, Integer.parseInt(paramRutTrab));
            stm.registerOutParameter(2, 12);
            stm.registerOutParameter(3, 12);
            stm.registerOutParameter(4, 12);
            stm.registerOutParameter(5, 12);
            stm.registerOutParameter(6, 12);
            stm.registerOutParameter(7, 12);
            stm.registerOutParameter(8, 12);
            stm.registerOutParameter(9, 12);
            stm.registerOutParameter(10, 12);
            stm.registerOutParameter(11, 12);
            stm.registerOutParameter(12, 12);
            stm.registerOutParameter(13, 12);
            stm.registerOutParameter(14, 12);
            stm.registerOutParameter(15, 12);
            stm.registerOutParameter(16, 12);
            stm.registerOutParameter(17, 12);
            stm.registerOutParameter(18, 12);
            stm.registerOutParameter(19, 12);
            stm.registerOutParameter(20, 12);
            stm.execute();
            for(int x = 2; x <= 20; x++)
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("param ")).append(x).append(": ").append(stm.getString(x)))));

            modelRoot.put("sueldo", stm.getString(2));
            modelRoot.put("gestion", stm.getString(3));
            modelRoot.put("movi", stm.getString(4));
            modelRoot.put("b_reg", stm.getString(5));
            modelRoot.put("n_reg", stm.getString(6));
            modelRoot.put("zona", stm.getString(7));
            modelRoot.put("b_zona", stm.getString(8));
            modelRoot.put("n_zona", stm.getString(9));
            modelRoot.put("b_anual", stm.getString(10));
            modelRoot.put("caja", stm.getString(11));
            modelRoot.put("b_antig", stm.getString(12));
            modelRoot.put("rta_var", stm.getString(13));
            modelRoot.put("b_prom", stm.getString(14));
            modelRoot.put("n_prom", stm.getString(15));
            modelRoot.put("tras", stm.getString(16));
            modelRoot.put("b_total", stm.getString(17));
            modelRoot.put("n_total", stm.getString(18));
            modelRoot.put("cod_remu", stm.getString(19));
            modelRoot.put("pre", stm.getString(20));
            String nivel = "";
            modelRoot.put("nivel", nivel);
            stm.close();
            conex.close();
        }
        catch(SQLException e)
        {
            OutMessage.OutMessagePrint("SQLException --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        Rut userRut = new Rut(conexion, paramRutTrab);
        modelRoot.put("rut", paramRutTrab);
        modelRoot.put("nombre", userRut.Nombres);
        modelRoot.put("cargo", userRut.Cargo);
        modelRoot.put("unidad_desc", userRut.Unidad);
        modelRoot.put("id_cargo", userRut.Id_Cargo);
        modelRoot.put("id_unidad", userRut.Id_Unidad);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("email", userRut.Email);
        modelRoot.put("mail", userRut.Mail);
        modelRoot.put("empresa", userRut.Empresa);
        modelRoot.put("empresa_descrip", userRut.EmpresaDescrip);
        Consulta consul = new Consulta(conexion);
        String consulta = "SELECT empresa AS id, descrip AS empresa FROM eje_ges_empresa order by id";
        consul.exec(consulta);
        modelRoot.put("empresas", consul.getSimpleList());
        consulta = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_ges_unidades WHERE vigente='S'  ORDER BY unid_empresa, unid_desc";
        consul.exec(consulta);
        modelRoot.put("unidades", consul.getSimpleList());
        consulta = "SELECT empresa, cargo AS id, descrip FROM eje_ges_cargos WHERE vigente='S'  ORDER BY empresa, descrip";
        consul.exec(consulta);
        modelRoot.put("cargos", consul.getSimpleList());
        consulta = "SELECT ec.cargo, ec.estamento, es.nivel FROM eje_ges_escala_salarial es INNER JOIN eje_ges_estamentos_cargos ec ON  es.familia_cargo = ec.estamento ORDER BY ec.cargo, ec.estamento, es.nivel";
        consul.exec(consulta);
        modelRoot.put("niveles", consul.getSimpleList());
        consul.close();
        super.retTemplate(resp,"Gestion/SimRenta/" + html,modelRoot);
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}