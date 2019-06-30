package cl.ejedigital.web.fileupload.ifaces;

import java.util.HashMap;
import java.util.List;


public interface IParametrosProcess {

	public void readParametros();
	public HashMap getParametros();
	public List getFiles();
	public String getRelativePath();
	
}
