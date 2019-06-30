package portal.com.eje.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.misc.SoftCacheLocator;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;

/**
 * Retorna captchas codes y los almacena
 * 
 * @since 12-10-2018
 * @author Pancho
 * */
public class Catpcha {
 
	
	public static Catpcha getInstance() {
		return Static.getInstance(Catpcha.class);
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
        
        getMap().put(passlineValueEncoded, Calendar.getInstance().getTime());
        
        return passlineValueEncoded;
    }
    
    private Map<String,Date> getMap() {
    	HashMap<String,Date> map = (HashMap<String,Date>) SoftCacheLocator.getInstance(this.getClass()).get("map");
    	if(map == null) {
    		map = new HashMap<String,Date>();
    	}
    	
    	return map;
    }
}
