package cl.ejedigital.web.fileupload;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.tool.validar.ifaces.IValidarDato;
import cl.ejedigital.web.fileupload.ifaces.IFileService;
import cl.ejedigital.web.fileupload.ifaces.IParametrosGetter;
import cl.ejedigital.web.fileupload.ifaces.IParametrosProcess;


public class Parametros implements IParametrosGetter {
	private IParametrosProcess pProcess;
	private IValidarDato validar;
	
	public Parametros(HttpServletRequest req, ServletContext sContext, IFileService fService) {
		pProcess = new  ParametrosProcess(req,sContext, fService);
		validar = new Validar();
	}

	private String getSimpleValue(String paramName) {
		Object o = pProcess.getParametros().get(paramName);
		
		if(o instanceof String[]) {
			if(((String[])o).length == 1) {
				return ((String[])o)[0];
			}
			else {
				return null;
			}
		}
		else {
			return ((String)o);
		}
	}
	
	public String getParamString(String paramName, String valDefecto) {
		return 	validar.validarDato(getSimpleValue(paramName), valDefecto);
	}

	public int getParamInt(String paramName, int valDefecto) {
		return 	validar.validarInt(getSimpleValue(paramName), valDefecto);
	}

	public long getParamLong(String paramName, long valDefecto) {
		return 	validar.validarLong(getSimpleValue(paramName), valDefecto);
	}

	public double getParamDouble(String paramName, double valDefecto) {
		return 	validar.validarDouble(getSimpleValue(paramName), valDefecto);
	}

	public String[] getParamValues(String paramName, String valDefecto, int largo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getParamValues(String paramName, String valDefecto) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getFiles() {
		return pProcess.getFiles();
		
	}

	public String getRelativePath() {
		return pProcess.getRelativePath();
	}
}
