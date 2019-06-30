package portal.com.eje.serhumano.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.enums.EnumDoAccion;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ifaces.AbsLoginPeople;
import portal.com.eje.serhumano.user.masterbean.SessionMasterBean;
import portal.com.eje.serhumano.user.utils.S_StartSessionDispatcher;

public class S_StartSession extends MyHttpServlet {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public S_StartSession() {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
    	

    	sendToStartSession(req, resp, EnumDoAccion.doPost);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

    	sendToStartSession(req, resp, EnumDoAccion.doGet);
    }
    
    private void sendToStartSession(HttpServletRequest req, HttpServletResponse resp, EnumDoAccion accion) throws ServletException, IOException {
    	
    	AbsLoginPeople loginPeople = SessionMasterBean.getMasterBean();
    	if(loginPeople == null) {
    		S_StartSessionDispatcher.getInstance().iniciaDespachoNormal(req, resp, accion);	
    	}
    	else {
    		
    		S_StartSessionDispatcher.getInstance().iniciaDespachoComponente(loginPeople, this, req, resp, accion);
    	}
    	
		
    	
    }
    
   
    
    private StringBuilder appendAllParameters(HttpServletRequest req, HttpServletResponse resp, StringBuilder lastServlet) {
    	IOClaseWeb io = new IOClaseWeb(this, req, resp);
    	Map<String, List<String>> map = io.getMapParams();
    	
    	Set<String> set = map.keySet();
    	for(String s : set) {
    		if(lastServlet.length() > 0) {
    			lastServlet.append("&");
			}
    		
    		lastServlet.append(s).append("=").append( getValues(map.get(s)));
    	}
    	
    	return lastServlet;
    }
    
    private String getValues(List<String> lista) {
    	StringBuilder str = new StringBuilder();
    	
    	if(lista != null) {
    		for(String s : lista) {
    			if(str.length() > 0) {
    				str.append(",");
    			}
    			str.append(s);
    		}
    	}
    	
    	return str.toString();
    }
}