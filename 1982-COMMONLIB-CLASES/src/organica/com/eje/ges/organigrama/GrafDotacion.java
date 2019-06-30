package organica.com.eje.ges.organigrama;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class GrafDotacion extends MyHttpServlet
{

    public GrafDotacion()
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
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            valida = new Validar();
            String unidad = req.getParameter("unidad");
            OutMessage.OutMessagePrint("Unidad : ".concat(String.valueOf(String.valueOf(req.getParameter("unidad")))));
            String paramEmpresa = user.getEmpresa();
            UE = new EncargadoUnidad();
            UE.DotacionTotalUnidad(Conexion, unidad, paramEmpresa);
            modelRoot.put("ddr", (new Integer(UE.TotalUniReal)).toString());
            modelRoot.put("dda", (new Integer(UE.TotalUniAut)).toString());
            modelRoot.put("ddd", (new Integer(UE.TotalUniDif)).toString());
            UE.DotacionTotalUnidadRama(Conexion, unidad, user.getEmpresa());
            modelRoot.put("dir", Tools.setFormatNumber((new Integer(UE.TotalUniRealR - UE.TotalUniReal)).toString()));
            modelRoot.put("dia", Tools.setFormatNumber((new Integer(UE.TotalUniAutR - UE.TotalUniAut)).toString()));
            modelRoot.put("did", (new Integer(UE.TotalUniAutR - UE.TotalUniAut - (UE.TotalUniRealR - UE.TotalUniReal))).toString());
            Consulta consul_unid = new Consulta(Conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(paramEmpresa).append("') AND (unid_id = '").append(unidad).append("')")));
            consul_unid.exec(sql);
            if(consul_unid.next())
                modelRoot.put("unid_desc", consul_unid.getString("unid_desc"));
            consul_unid.close();
            super.retTemplate(resp,"GrafDotacion.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private EncargadoUnidad UE;
    private Validar valida;
    private Mensaje mensaje;
}