package portal.com.eje.serhumano.user;

import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerDefault;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.misc.Cronometro;
import freemarker.template.SimpleHash;

public class SessionMgr extends AbsClaseWebInsegura {	
	private Cronometro cro = null;
    private int cantVeces = 0;
    
    public SessionMgr(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		cro =  new Cronometro();
		cro.start();
	}
    
    public SessionMgr() {
    	super(null);
    	cro =  new Cronometro();
		cro.start();
    }

    public static SessionMgr getInstance() {
    	return Util.getInstance(SessionMgr.class);
    }
    
    
	public static void setTimeout() {
        try {
        	ResourceBundle proper = ResourceBundle.getBundle("db");
            timeout = Integer.parseInt(proper.getString("timeout.session"));
    	}
    	catch(Exception e) {
    		timeout = 1800;
    	}
    }

	public Usuario rescatarUsuario_i(HttpSession sesion) {
		 Usuario user;
	        if(sesion == null)
	        {
	        	printPorConsola("rescatarUsuario --> Session nula");
	            user = new Usuario(sesion, new JndiManagerDefault());
	        } else
	        {
	            try
	            {
	                user = (Usuario)sesion.getAttribute(sesion.getId());
	            }
	            catch(Exception e)
	            {
	            	printPorConsola("rescatarUsuario Exception --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
	                user = new Usuario(sesion , new JndiManagerDefault());
	            }
	            if(user == null)
	            {
	            	printPorConsola("rescatarUsuario --> Usuario nulo");
	                user = new Usuario(sesion , new JndiManagerDefault());
	            }
	        }
	        	        
	        printPorConsola("rescatarUsuario Usuario --> ".concat(String.valueOf(user.toString())));
     
	        
	        return user;
	}
	
	private void printPorConsola(String texto) {
		cantVeces++;
		
		if(cro.GetMilliseconds() >= 60000) {
			StringBuilder str = new StringBuilder("@@ ["+texto+"]");

			
			if(cantVeces > 1) {
				str.append("(").append(cantVeces + " veces").append(")");
			}
			//System.out.println(str.toString());
			cro.start();
			cantVeces = 0;
		}
	}
	
    public static Usuario rescatarUsuario(HttpSession sesion) {
       return SessionMgr.getInstance().rescatarUsuario_i(sesion);
    }

    public static Usuario rescatarUsuario(HttpServletRequest req)
    {
        return rescatarUsuario(req.getSession(false));
    }

    public static void guardarUsuario(HttpServletRequest req, Usuario usuario)
    {
    	HttpSession miSesion = req.getSession(true);
        miSesion.setAttribute(miSesion.getId(), usuario);
        miSesion.setMaxInactiveInterval(timeout);
        //System.out.println("guardarUsuario Usuario --> ".concat(String.valueOf(String.valueOf(usuario.toString()))));
    }

    /**
     * @author Pancho
 
     * */
    public static String guardarUsuarioOrganica(HttpServletRequest req, organica.com.eje.ges.usuario.Usuario usuario)
    {
        HttpSession miSesion = req.getSession(true);
        miSesion.setAttribute(miSesion.getId()+"o", usuario);
        miSesion.setMaxInactiveInterval(timeout);

        //System.out.println("guardarUsuario Usuario --> ".concat(String.valueOf(String.valueOf(usuario.toString()))));
        
        return miSesion.getId();
    }
    
    private static int timeout;

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		
		
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCalendar.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("select".equals(accion)) {
			if("liveSeconds".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(getLeftTime()));
			}
			else if("jsonLiveSeconds".equals(thing)) {
				long left =  getLeftTime();
				
				DataList lista = new DataList();
				DataFields campos = new DataFields();
				
				long resto = left % 1000;
				left =  (long) Math.floor(left / 1000);
				campos.put("milliseconds", new Field(resto));
				
				resto = left % 60;
				left =  (long) Math.floor(left / 60);
				campos.put("seconds", new Field(resto));
				
				resto = left % 60;
				left =  (long) Math.floor(left / 60);
				campos.put("minutes", new Field(resto));
				
				resto = left % 24;
				left =  (long) Math.floor(left / 24);
				campos.put("hours", new Field(resto));
				campos.put("days", new Field(0));
				campos.put("months", new Field(0));
				
				lista.add(campos);
				
				JSonDataOut out = new JSonDataOut(lista);
				super.getIoClaseWeb().retTexto(out.getListData());
				
			}
		}
		
		
	}
	
	public String getIdSession(HttpSession sesion) {
		if(sesion != null) {
			return sesion.getId();
		}
		
		return null;
	}
	
	private long getLeftTime() {
		  HttpSession miSesion = super.getIoClaseWeb().getReq().getSession(false);
		  long left = 0;
		  
		  if(miSesion != null) {
			  long lastAccess = miSesion.getLastAccessedTime() ;
			  long now = Calendar.getInstance().getTimeInMillis();
			  long max = miSesion.getMaxInactiveInterval() * 1000;
			  
			  left = max - (now - lastAccess);
			  if(left < 0) {
				  left = 0;
			  }
		  }
		
		  
		  return left;
	}
}