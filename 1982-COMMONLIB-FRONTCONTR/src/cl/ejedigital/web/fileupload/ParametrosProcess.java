package cl.ejedigital.web.fileupload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.tool.validar.ifaces.IValidarDato;
import cl.ejedigital.web.consola.Consola;
import cl.ejedigital.web.fileupload.ifaces.IFileService;
import cl.ejedigital.web.fileupload.ifaces.IParametrosProcess;
import cl.ejedigital.web.fileupload.ifaces.IReplication;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;

public class ParametrosProcess implements IParametrosProcess {

	private ServletContext		sContext;
	private HttpServletRequest	req;
	private HashMap				valores;
	private List				files;
	private final String		RELATIVEPATHSAVE;
	private IValidarDato		validar;
	private IFileService		fileService;
	private int 				rutResponsable;
	
	public ParametrosProcess(HttpServletRequest req, ServletContext sContext, IFileService fileService, int rutResponsable) {
		Consola.getInstance().printInfo(" Leyendo parametros ["+req.getRequestURI()+"]");
		
		this.valores = new HashMap();
		this.files = new ArrayList();
		this.validar = new Validar();
		this.fileService = fileService;
		this.RELATIVEPATHSAVE = getRelativePath();
		this.req = req;
		this.sContext = sContext;
		this.rutResponsable = rutResponsable;
		
		Consola.getInstance().printInfo(" Leyendo parametros en path ->"+this.RELATIVEPATHSAVE);
		readParametros();
		
		Consola.getInstance().printInfo(" Parametros leidos ["+valores+"]");
		Consola.getInstance().printInfo(" Archivos obtenidos ["+files+"]");
	}
	
	public ParametrosProcess(HttpServletRequest req, ServletContext sContext, IFileService fileService) {
		this(req,sContext,fileService,-1);
	}

	public void readParametros() {
		RequestContext reqContext = new ServletRequestContext(req);

		if(ServletFileUpload.isMultipartContent(reqContext)) {
			Consola.getInstance().printInfo(" es con MULTIPARTCONTENT");
			initConMultipart(validar);

		}
		else {
			Consola.getInstance().printInfo(" NO es con MULTIPARTCONTENT");
			initSinMultipart(validar);

		}

	}

	public String getRelativePath() {
		String path = null;
		
		try {
			ResourceBundle proper = ResourceBundle.getBundle("upload");
			path = proper.getString("unic.relativepath");
		} catch (MissingResourceException e) {
			
		}
		
		if(path == null) {
			path = File.separator + "temporal" + File.separator + "filesUnicos";
		} 
		
		return path;
	}

	private void initConMultipart(IValidarDato validar) {
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

		try {
			List items = uploadHandler.parseRequest(req);
			Iterator iter = items.iterator();

			while(iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if(item.isFormField() && item.getFieldName() != null) {
					Consola.getInstance().printInfo(" NAME:"+item.getFieldName()+" -> " + item.getString());
					valores.put(item.getFieldName(),item.getString());
				}
				else {
					if(item.getName() != null && !"".equals(item.getName())) {
						File tmp = new File(item.getName());

						String ruta = sContext.getRealPath(RELATIVEPATHSAVE);
						File tosave = new File(ruta,tmp.getName());

						item.write(tosave);
						String[] array = item.getFieldName().split("_");
						int idTipo = 0;

						if(array.length == 2) {
							idTipo = validar.validarInt(array[1],0);
						}
						else {
							idTipo = 0;
						}
						Consola.getInstance().printInfo(" NAME:"+item.getFieldName()+" VAL:" + tosave +" -> IDTIPO:"+idTipo);
						
						IReplication replica = new Replication();
						int newId = this.fileService.addFile(rutResponsable,tosave,EjeFileUnicoTipo.getEjeFileUnicoTipo(idTipo),replica);						

						files.add(new Integer(newId));	
					}
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSinMultipart(IValidarDato validar) {
		valores.putAll(req.getParameterMap());
	}

	public HashMap getParametros() {
		return valores;
	}

	public List getFiles() {
		return files;
	}
}
