package portal.com.eje.serhumano.tracking;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;

public class trackingManager {
	
	public void insertTracking(String rut, String cod_empresa, String descripcion, String datos, HttpServletRequest req) {
		insertTracking(null, rut, cod_empresa, descripcion, datos, req);
	}

	public void insertTracking(String direcRel, String rut, String cod_empresa, String descripcion, String datos, HttpServletRequest req) {
		
		if(!("99999999".equals(rut))){
			try {
	
				
				datos = Validar.getInstance().cutString(datos, 100);
				String ip = req.getRemoteAddr() ;
				String browser = Validar.getInstance().cutString(req.getHeader("User-Agent"), 195);
				//Servlet que llama
				
				if(direcRel == null) {
					direcRel = req.getRequestURI().replaceAll(req.getContextPath(), "");
				}

				//Parametros
				String query = req.getQueryString();
				
				InetAddress address = InetAddress.getLocalHost();
				String server = address.getHostName();
				int intImpresa = Validar.getInstance().validarInt(cod_empresa, -1);
				
				String sql = "INSERT INTO eje_ges_tracking "
						.concat(" (rut, cod_empresa, fecha, hora, initdate , descripcion, ip, browser, direc_rel, query, datos,server,context) ")
						.concat("  VALUES (?,?, getdate() ,getdate() , getdate(),?,?,?,?,?,?,?,?) ");
	
				Object[] params = { 
								Validar.getInstance().validarInt(rut), 
								intImpresa, 
								Validar.getInstance().cutString(descripcion,100),
								Validar.getInstance().cutString(ip,15), 
								Validar.getInstance().cutString(browser,200),	
								Validar.getInstance().cutString(direcRel,100), 
								Validar.getInstance().cutString(query,150), 
								Validar.getInstance().cutString(datos,100), 
								Validar.getInstance().cutString(server,30),
								Validar.getInstance().cutString(req.getContextPath(),100),
				};
	
				ConsultaTool.getInstance().insert("portal", sql, params);
	
			} catch (UnknownHostException e) {
				e.getMessage();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("EL super usuario no es insertado en la tabla tracking");
		}
	}

}