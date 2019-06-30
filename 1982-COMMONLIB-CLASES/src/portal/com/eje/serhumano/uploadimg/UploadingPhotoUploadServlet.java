package portal.com.eje.serhumano.uploadimg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.output.DeferredFileOutputStream;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class UploadingPhotoUploadServlet extends MyHttpServlet implements javax.servlet.Servlet {
	private Usuario user;

	public UploadingPhotoUploadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String MPREFIX = "[UploadingPhotoUploadServlet][doGet]";
		OutMessage.OutMessagePrint(MPREFIX + " Entrando...");
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "no-cache");
		
			SimpleHash modelRoot = new SimpleHash();
			SimpleList errors = new SimpleList();
			SimpleList messages = new SimpleList();
			modelRoot.put("errors", errors);
			modelRoot.put("messages", messages);
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}       
			FileUpload fu = new FileUpload();
			fu.setSizeMax(1024 * 1024 * 1); // 1 MegaBytes
			DefaultFileItemFactory defaultFileItemFactory = new DefaultFileItemFactory();
			defaultFileItemFactory.setSizeThreshold(4096);
			String realPath = this.getServletContext().getRealPath("/");
			String workerImagesPath = realPath + "temporal/imgtrabajadores/";
			defaultFileItemFactory.setRepository(new File(workerImagesPath));
			fu.setFileItemFactory(defaultFileItemFactory);
			List fileItems = fu.parseRequest(request);
			if (fileItems == null) {
				return;
			}
			Iterator i = fileItems.iterator();
			boolean existImageFiles = false;
			while (i.hasNext()) {
				FileItem fileItem = (FileItem) i.next();
				String fileName = fileItem.getName();                                    
				if (fileName != null && !"".equals(fileName.trim())) {
					existImageFiles = true;
					fileName = fileName.toLowerCase();
					int index = fileName.indexOf(".");
					if ( (index>=0) && (fileName.indexOf(".jpg")!=index) || fileName.indexOf("-")>=0) {
						errors.add("Archivo "+ fileName + " no cumple con el formato para el nombre.");
					}	
					if ( (! (fileName.indexOf(".jpg")>=0 ))|| (!(fileItem.getContentType().indexOf("image")>=0)) ) {              
						errors.add("Archivo "+ fileName + " no es un archivo jpg.");
					}          
					DeferredFileOutputStream deferredFileOutputStream = (DeferredFileOutputStream)fileItem.getOutputStream();
					BufferedImage image = ImageIO.read(new File( deferredFileOutputStream.getFile().getAbsolutePath() ) );
					if ( fileItem.getSize() > (70*1024)) {
						errors.add("Tamaño del archivo "+ fileName + " es " + fileItem.getSize()/1024 + " Kb, lo cual excede el máximo.");
					}
					if ( !(image.getHeight()==250 && image.getWidth()==250 )) {
						errors.add("Dimensiones de la imagen "+ fileName + " no son correctas.");
					}
					if ( errors.isEmpty()){
						File imageFile = new File(workerImagesPath + fileName);
						fileItem.write(imageFile);
						fileItem.getOutputStream().close();
					}
					else {
						break;
					}
				}               
			}
			if ( !existImageFiles ){
				errors.add("No selecciono archivos para la subida.");
			}
			if ( errors.isEmpty()){
				messages.add("Archivo(s) subido(s) Exitosamente");
			}
			
			IOClaseWeb io = new IOClaseWeb(this, request, response);
			
			super.retTemplate(io, "upload_img/upload_photo.htm", modelRoot);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.toString(), ex);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
}