package portal.com.eje.serhumano.uploadimg;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class UploadingImagesUploadServlet extends MyHttpServlet
    implements Servlet
{

    public UploadingImagesUploadServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] Entrando...");
        try
        {
            SimpleHash modelRoot = new SimpleHash();
            SimpleList errors = null;
            SimpleList messages = new SimpleList();
            modelRoot.put("errors", errors);
            modelRoot.put("messages", messages);
            user = SessionMgr.rescatarUsuario(request);
            if(!user.esValido())
            {
                super.mensaje.devolverPaginaSinSesion(response, "ERROR de Sesi&oacute;n", "Su sesi&oacute;n ha expirado o no se ha logeado.");
                return;
            }
            FileUpload fu = new FileUpload();
            fu.setSizeMax(0x100000L);
            OutMessage.OutMessagePrint("Ponemos el tama\361o m\341ximo");
            DefaultFileItemFactory defaultFileItemFactory = new DefaultFileItemFactory();
            defaultFileItemFactory.setSizeThreshold(4096);
            String realPath = getServletContext().getRealPath("/");
            String workerImagesPath = realPath.concat("temporal").concat(File.separator).concat("imgtrabajadores").concat(File.separator);
            OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] realPath: " + realPath);
            OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] workerImagesPath: " + workerImagesPath);
            defaultFileItemFactory.setRepository(new File(workerImagesPath));
            fu.setFileItemFactory(defaultFileItemFactory);
            List fileItems = fu.parseRequest(request);
            if(fileItems == null)
            {
                OutMessage.OutMessagePrint("La lista es nula");
                return;
            }
            OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet]fileItems.size() =" + fileItems.size());
            Iterator i = fileItems.iterator();
            boolean existImageFiles = false;
            while(i.hasNext()) 
            {
                FileItem fileItem = (FileItem)i.next();
                String fileName = fileItem.getName();
                if(fileName == null || "".equals(fileName.trim()))
                    continue;
                existImageFiles = true;
                OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] fileName: " + fileName);
                fileName = fileName.toLowerCase();
                int index = fileName.indexOf(".");
                if(index >= 0 && fileName.indexOf(".jpg") != index || fileName.indexOf("-") >= 0) {
                	if(errors == null) {  errors = new SimpleList(); }
                    errors.add("Archivo " + fileName + " no cumple con el formato para el nombre.");
                }
                if(fileName.indexOf(".jpg") < 0 || fileItem.getContentType().indexOf("image") < 0) {
                	if(errors == null) {  errors = new SimpleList(); }
                    errors.add("Archivo " + fileName + " no es un archivo jpg.");
                }
                java.awt.image.BufferedImage image = ImageIO.read(fileItem.getInputStream());
                OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] size: " + fileItem.getSize());
                if(fileItem.getSize() > 0x11800L) {
                	if(errors == null) {  errors = new SimpleList(); }
                    errors.add("Tama\361o del archivo " + fileName + " es " + fileItem.getSize() / 1024L + " Kb, lo cual excede el m\341ximo.");
                }
                OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] height: " + image.getHeight());
                OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] width: " + image.getWidth());
                if(image.getHeight() != 81 || image.getWidth() != 62) {
                	if(errors == null) {  errors = new SimpleList(); }
                    errors.add("Dimensiones de la imagen " + fileName + " no son correctas.");
                }
                if(!(errors == null))
                    break;
                int pos = fileName.lastIndexOf("\\") + 1;
                fileName = fileName.substring(pos);
                File imageFile = new File(workerImagesPath + fileName);
                fileItem.write(imageFile);
                fileItem.getOutputStream().close();
            }
            if(!existImageFiles) {
            	if(errors == null) {  errors = new SimpleList(); }
                errors.add("No selecciono archivos para la subida.");
            }
            if(errors == null)
                messages.add("<b><font color='red'>Archivo(s) subido(s) Exitosamente</font></b>");
            super.retTemplate(response,"upload_img/upload_images.htm",modelRoot);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ServletException(ex.toString(), ex);
        }
        OutMessage.OutMessagePrint("[UploadingImagesUploadServlet][doGet] Saliendo...");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private Usuario user;
}