package cl.ejedigital.web.freemaker.sethash;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cl.ejedigital.web.fmrender.FmRenderers;
import cl.ejedigital.web.freemaker.ifaces.IFreemakerSetHash;
import freemarker.template.SimpleHash;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;

public class SetHashAllFields implements IFreemakerSetHash{
	private Logger logger = Logger.getLogger(getClass());
	
	private SetHashAllFields() {
		
	}
	
	public static SetHashAllFields getInstance() {
		return Util.getInstance(SetHashAllFields.class);
	}
	
	@Override
	public <T> void setDataFromVo(SimpleHash modelRoot, T t, FmRenderers<T> rendes, int counter) {
		if(modelRoot == null || t == null) {
			return;
		}
		Map<String,String> toPut = new HashMap<>();
		
		Map<String,Method> metodos = VoTool.getInstance().getAllGetMethodsWithNoParameters_map(t.getClass());
		List<String> fields = new ArrayList<>( metodos.keySet() );
		
		for(Entry<String,Method> entry: metodos.entrySet()) {
			String field = entry.getKey();
			//los cambios que si estan en el vo
			Object o = VoTool.getInstance().getReturnFromMethod(t, entry.getValue());
			
			if(rendes != null)  {
				if( rendes.getRender(field) != null ) {
					o = rendes.getRender(field).getRenderer().renderer((T) t, field, counter);
				}
				else {
					o = ClaseConversor.getInstance().getObject(o, String.class);
				}
			}
			
			String value = ClaseConversor.getInstance().getObject(o , String.class);
			
			toPut.put(field, value);
		}
		
		if(rendes != null)  {
			for(String field : rendes.getRenderers().keySet()) {
				//los campos que no estan en el vo
				if(!fields.contains(field)) {
					Object o = rendes.getRender(field).getRenderer().renderer((T) t, field, counter);
					toPut.put(field, (String)o);
				}
			}
		}
		
		modelRoot.put("correlativo", String.valueOf(counter));
		logger.debug("correlativo >>> "+ String.valueOf(counter));
		for(Entry<String, String> entry : toPut.entrySet()) {
			modelRoot.put(entry.getKey(), entry.getValue());
			logger.debug(entry.getKey()+" >>> "+entry.getValue());
		}
		
	}
	
	@Override
	public <T> void setDataFromVo(SimpleHash modelRoot, T t) {
		setDataFromVo(modelRoot, t, null, 0);
		
	}

}
