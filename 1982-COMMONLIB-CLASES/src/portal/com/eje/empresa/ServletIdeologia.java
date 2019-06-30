package portal.com.eje.empresa;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;

import organica.tools.Validar;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaPreparada;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;


public class ServletIdeologia extends MyHttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Usuario user = SessionMgr.rescatarUsuario(req);
		
		try {
			if(user.esValido()) {
				Validar val = new Validar();
				/*mod save load*/
				String withSave =  val.validarDato(req.getParameter("save"),"false");
				String accion =  val.validarDato(req.getParameter("accion"),"load");
				String seccion =  val.validarDato(req.getParameter("seccion"),"none");
				String htm	  =  val.validarDato(req.getParameter("htm"),"empresa/ideologiaMain.htm");
				
				if("Mision".equals(seccion)){
					insTracking(req, "Misión".intern(), null);
				}
				else if("Vision".equals(seccion)){
					insTracking(req, "Visión".intern(), null);
				}
				else{
					if("Valores".equals(seccion)){
						insTracking(req, "Valores".intern(), null);
					}
					else if("Politicas".equals(seccion)){
						insTracking(req, "Pol&iacute;ticas".intern(), null);
					}
					else{
						if("Modelo".equals(seccion)){
							insTracking(req, "Modelo".intern(), null);
						}
					}
				}
				
				if("load".equals(accion)) {
					htm = "empresa/ideologia"+seccion+".htm";
				}
				
				if("true".equals(withSave)) {
					 if(existeTexto(seccion)) {
						 updateTexto(seccion,val.validarDato(req.getParameter("texto"),""));
					 }
					 else {
						 insertTexto(seccion,val.validarDato(req.getParameter("texto"),""));
					 }
				}
				
				SimpleHash modelRoot = new SimpleHash();
				modelRoot.put("texto",getTexto(seccion));
				modelRoot.put("accion",accion);
				modelRoot.put("seccion",seccion);
				
				super.retTemplate(resp,htm,modelRoot);			
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private boolean updateTexto(String key,String texto) throws SQLException {
		String strConsulta = " UPDATE eje_generico_pages SET  TEXTO = ? WHERE key_page = ?";
		String[] params = {texto, key};
		
		return ConsultaTool.getInstance().update("portal",strConsulta,params) > 0;
	}
	
	private boolean insertTexto(String key,String texto) throws SQLException {
		String strConsulta = " INSERT INTO eje_generico_pages (key_page, TEXTO) values (?,?)";
		String[] params = {key, texto };
				
		return ConsultaTool.getInstance().insert("portal",strConsulta,params);
	}
	
	private boolean existeTexto(String key) throws SQLException {
		String strConsulta = " SELECT 1 FROM eje_generico_pages WHERE key_page = ? ";
		String[] params = { key};
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal",strConsulta,params);
		
		if(data.next()) {
			return true;
		}
		
		return false;
	}
	
	private String getTexto(String key) throws SQLException {
		String strConsulta = " SELECT texto FROM eje_generico_pages WHERE key_page = ? ";
		String[] params = {key};		
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal",strConsulta,params);
		
		if(data.next()) {
			return data.getClobAsString("texto");
		}
		
		return "";		
	}
	
	
}
