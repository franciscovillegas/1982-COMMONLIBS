package portal.com.eje.tools;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;
import cl.ejedigital.tool.validar.Validar;

public class CaptchaTool {
	private static CaptchaTool instance;
	
	public static CaptchaTool  getInstance() {
		if(instance == null) {
			synchronized (CaptchaTool.class) {
				if(instance == null) {
					instance = new CaptchaTool();
				}
			}
		}
		
		return instance;
	}
	
	
	
	 public String getPassLineEnconded(HttpServletRequest req) {
        Encrypter stringEncrypter = new Encrypter();
        String randomLetters = new String("");
        for (int i = 0; i < Config.getPropertyInt(Config.MAX_NUMBER); i++) {
            randomLetters += (char) (65 + (Math.random() * 24));
        }
        randomLetters = randomLetters.replaceAll("I","X");
        randomLetters = randomLetters.replaceAll("Q","Z");

        String passlineNormal = randomLetters + "." + req.getSession().getId();
        String passlineValueEncoded = stringEncrypter.encrypt(passlineNormal);
        passlineValueEncoded  = Base64Coder.encode(passlineValueEncoded );
        return passlineValueEncoded;
    }
    
    public boolean validaCodigo(HttpServletRequest req) {
    	Validar val = new Validar();
	    String passline = val.validarDato(req.getParameter("passline"),"");
	    String passlineEncoded =  val.validarDato(req.getParameter("passline_enc"),"");
    	boolean pageOut = false;
	    Encrypter stringEncrypter = new Encrypter();
	    boolean securityLettersPass = true;
	    try {
		    if ((passline!=null) || (passlineEncoded !=null)) {
		        String passlineDecoded = Base64Coder.decode(passlineEncoded);
		        String passlineCheck = stringEncrypter.decrypt(passlineDecoded);
		        //String passlineString = passlineCheck;
		        passlineCheck = passlineCheck.substring(0, Config.getPropertyInt(Config.MAX_NUMBER));
		        //String sessionId = passlineString.substring(passlineString.indexOf(".")+1, passlineString.length());
		        //if (!sessionId.equals(req.getSession().getId())) {
		        //	securityLettersPass = false;
		        //}
		        if (!passline.toUpperCase().equals(passlineCheck.toUpperCase())) {
		        	securityLettersPass = false;
		        }
		        if (securityLettersPass) {
		        	pageOut = true;
		        }
		    }
	    } catch (Exception e) {
	    	
	    }
    	return pageOut;
    }
	
	
}
