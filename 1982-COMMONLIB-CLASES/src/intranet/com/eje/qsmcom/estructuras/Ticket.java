package intranet.com.eje.qsmcom.estructuras;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

public class Ticket {
	private String id;
	private String rol;
	private String rut;
	private String rutstr;
	private String producto;
	private String email;
	private String evento;
	private String suceso;
	private String nombreArchivo;
	private String comentario;
	private String accion;
	private boolean tieneArchivo;
	private String glosa;
	private boolean correctamenteCargado;
	private File tosave;
	private String tipo;
	private String pagSelected;
	
	public Ticket(HttpServletRequest request, String pathImageSave) {
		rut = "";
		producto = "";
		email = "";
		evento = "";
		suceso = "";
		nombreArchivo = "";
		tieneArchivo = false;
		glosa = "";
		correctamenteCargado = true;
		
		try {
			boolean isMultipart = FileUpload.isMultipartContent(request);
			
			if(isMultipart) {
				cargaMultipart(request, pathImageSave);
			} else {
				cargaNormal(request, pathImageSave);
			}
		} catch (Exception e) {
			correctamenteCargado = false;
			e.printStackTrace();
		}
	}
	
	
	private void cargaMultipart(HttpServletRequest request,  String pathImageSave ) throws Exception {
		DiskFileUpload upload = new DiskFileUpload();	
		List items = upload.parseRequest(request);			    
		File cfile = new File("");
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				if (item.getFieldName().equals("rut")) {
					rut = item.getString();		            	
				} else if (item.getFieldName().equals("producto")) {
					producto = item.getString();		            	
				} else if (item.getFieldName().equals("email")) {
					email = item.getString();		            	
				} else if (item.getFieldName().equals("evento")) {
					evento = item.getString();		            	
				} else if (item.getFieldName().equals("suceso")) {
					suceso = item.getString();		            	
				} else if (item.getFieldName().equals("mensaje")) {
					glosa = item.getString();		            	
				} else if (item.getFieldName().equals("rutstr")) {
					rutstr = item.getString();		            	
				} else if (item.getFieldName().equals("id")) {
					id = item.getString();		            	
				} else if (item.getFieldName().equals("comentario")) {
					comentario = item.getString();		            	
				} else if (item.getFieldName().equals("accion")) {
					accion = item.getString();		            	
				} else if (item.getFieldName().equals("rol")) {
					rol = item.getString();		            	
				} else if (item.getFieldName().equals("tipo")) {
					tipo = item.getString();		            	
				} else if (item.getFieldName().equals("pagselecionada")) {
					pagSelected = item.getString();		            	
				}
				
			}
			else {
				if (item.getFieldName() != null && !"".equals(item.getName())) {
					cfile=new File(item.getName());
			    		
		    	    File tosave=new File(pathImageSave + File.separator + cfile.getName());
	    			System.out.println("Directiorio: " + tosave);
	    			
	    			int num = 1;
	    			while(tosave.exists()) {
	    				tosave = new File(pathImageSave + File.separator + 
	    						cfile.getName().substring(0, cfile.getName().lastIndexOf(".")) + "("+num+")" +  
	    						cfile.getName().substring(cfile.getName().lastIndexOf("."),cfile.getName().length() ));
	    				num++;
	    			}
		    			
					item.write(tosave);
					nombreArchivo = tosave.getName();
					tieneArchivo = true;
				}	 
				else {
					System.out.println("requerimiento sin archivo");			    			
				}
			}
		}
		
	}
	
	private void cargaNormal( HttpServletRequest request,  String pathImageSave  ) {
		rut = request.getParameter("rut");
		producto = request.getParameter("producto");
		email = request.getParameter("email");
		evento = request.getParameter("evento");
		suceso = request.getParameter("suceso");
		glosa = request.getParameter("glosa");
		rutstr = request.getParameter("rutstr");
		id = request.getParameter("id");
		comentario = request.getParameter("comentario");
		accion = request.getParameter("accion");
		rol = request.getParameter("rol");
		tipo = request.getParameter("tipo");		            	
		pagSelected = request.getParameter("pagselecionada");
		
	}
		
	
	public String getRut() {
		return rut;
	}

	public String getProducto() {
		return producto;
	}

	public String getEmail() {
		return email;
	}

	public String getEvento() {
		return evento;
	}

	public String getSuceso() {
		return suceso;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public boolean isConArchivo() {
		return tieneArchivo;
	}

	public String getGlosa() {
		return glosa;
	}

	public boolean isCorrectamenteCargado() {
		return correctamenteCargado;
	}

	public File getFile() {
		return tosave;
	}

	public String getRutstr() {
		return rutstr;
	}

	public String getId() {
		return id;
	}

	public String getComentario() {
		return comentario;
	}

	public String getAccion() {
		return accion;
	}


	public String getRol() {
		return rol;
	}


	public String getTipo() {
		return tipo;
	}


	public int getPagSelected() {
		try {
			return Integer.parseInt(pagSelected);
		} catch (Exception e) {
			return 1;
		}
		
	}
	
	
	
	
}
