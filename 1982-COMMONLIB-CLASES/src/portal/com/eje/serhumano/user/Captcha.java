package portal.com.eje.serhumano.user;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;

public class Captcha {
	public static Captcha instance;
	
	private Captcha() {
		
	}
	
	public static Captcha getInstance() {
		if(instance == null) {
			synchronized (Captcha.class) {
				if(instance == null) {
					instance = new Captcha();
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
	    

}
