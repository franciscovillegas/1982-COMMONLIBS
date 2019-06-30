package loginsw;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSonDataOut;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;



/**
 * @deprecated
 * @see EjeI?modulo=ejeb_generico_login&thing=CaptchaCode&accion=get
 * */
public class Captcha extends AbsClaseWebInsegura {

	public Captcha(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		DataList lista = new DataList();
		DataFields fields = new DataFields();
		
		fields.put("key", new Field(getPassLineEnconded()));
		
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		super.getIoClaseWeb().retTexto(out.getListData());
	}


	
	private String getPassLineEnconded() {
        Encrypter stringEncrypter = new Encrypter();
        String randomLetters = new String("");
        for (int i = 0; i < Config.getPropertyInt(Config.MAX_NUMBER); i++) {
            randomLetters += (char) (65 + (Math.random() * 24));
        }
        randomLetters = randomLetters.replaceAll("I","X");
        randomLetters = randomLetters.replaceAll("Q","Z");

        String passlineNormal = randomLetters + "." + super.getIoClaseWeb().getReq().getSession().getId();
        String passlineValueEncoded = stringEncrypter.encrypt(passlineNormal);
        passlineValueEncoded  = Base64Coder.encode(passlineValueEncoded );
        return passlineValueEncoded;
    }

}
