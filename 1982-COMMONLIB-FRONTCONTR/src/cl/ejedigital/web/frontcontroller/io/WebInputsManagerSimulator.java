package cl.ejedigital.web.frontcontroller.io;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class WebInputsManagerSimulator extends WebInputsManager {

	public WebInputsManagerSimulator(String realPathToSave) throws Exception {
		super(new HashMap<String,List<String>>(), new HashMap<String, List<File>>());
		
		this.realPathToSave = realPathToSave;
	}
	 
}