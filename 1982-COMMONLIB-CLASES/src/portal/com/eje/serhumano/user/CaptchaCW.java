package portal.com.eje.serhumano.user;

import java.util.HashMap;
import java.util.Map;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class CaptchaCW extends AbsClaseWebInsegura{

	public CaptchaCW(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		Map map = new HashMap<String,String>();
		map.put("key",Captcha.getInstance().getPassLineEnconded(super.getIoClaseWeb().getReq()));
		
		super.getIoClaseWeb().retSenchaJson(map, true);
		
	}

	@Override
	public void doGet() throws Exception {
		doPost();
		
	}



}
