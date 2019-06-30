package portal.com.eje.serhumano.directorio.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;

import portal.com.eje.serhumano.directorio.mgr.ManagerTrabajador;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import cl.ejedigital.consultor.ConsultaData;
import freemarker.template.SimpleHash;

public class FichaDirectorio extends MyHttpServlet
{
    private Usuario user;
    
    public FichaDirectorio()
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
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        
        Integer strRut = Integer.valueOf(req.getParameter("rut"));
        String htm= req.getParameter("htm");
        
		ConsultaData datosTrabajadorData = ManagerTrabajador.getInstance().getTrabajadores(strRut);

		if (datosTrabajadorData.next()){
			modelRoot.put("rut", String.valueOf(datosTrabajadorData.getInt("rut")) );
			modelRoot.put("nombre", datosTrabajadorData.getString("nombre") );
			modelRoot.put("ape_paterno", datosTrabajadorData.getString("ape_paterno") );
			modelRoot.put("ape_materno", datosTrabajadorData.getString("ape_materno") );
			modelRoot.put("desc_cargo", datosTrabajadorData.getString("desc_cargo") );
			modelRoot.put("desc_unidad", datosTrabajadorData.getString("desc_unidad") );
			modelRoot.put("telefono", datosTrabajadorData.getString("telefono") );
			modelRoot.put("celular", datosTrabajadorData.getString("celular") );
			modelRoot.put("anexo", datosTrabajadorData.getString("anexo") );
			modelRoot.put("mail", datosTrabajadorData.getString("mail") );
		}

        super.retTemplate(resp,htm,modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", Conexion);
    }

}