package portal.com.eje.frontcontroller.resobjects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.Cache.CachePortalTypes;

public class ResourceImage implements IResourceManipulate {

	private Cache				imageCache;
	private HttpServletResponse	resp;
	private HttpServletRequest	req;
	private IResourceListener	resourceListener;

	public ResourceImage() {
		imageCache = CacheLocator.getInstance(CachePortalTypes.CACHEIMAGE);
	}

	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}

	public void init(MyHttpServlet myhttp, HttpServletRequest req, HttpServletResponse resp) {
		this.resp = resp;
		this.req = req;
	}

	public ServletOutputStream getWriter() throws IOException, ServletException {
		String imagePath = "/" + ResourceTool.getInstance().getObject(req);

		ServletOutputStream out = resp.getOutputStream();
		ByteArrayOutputStream bufImage = getStream(imagePath);
		
		FileOther objOther = getObjOther(imagePath);
		resp.setContentType(objOther.getContentType());
		String contentDisposition = Validar.getInstance().validarDato(req.getParameter("cdisposition") , "inline");	
		resp.setHeader( "Content-Disposition" , contentDisposition.concat(";fileName=".concat(objOther.getNameFile())) );
		
	 
			out.write(bufImage.toByteArray());	
		 
		
		return out;
	}
	
	public FileOther getObjOther(String imagePath) throws ServletException, IOException {
		File imgFile = null;

		FileOther objOther =  (FileOther) imageCache.get(imagePath);
		
		if(objOther == null) {
			
			try {
				imgFile = ResourceMapping.getInstance().getFile(req, imagePath);
	
			}
			catch (FileNotFoundException e) {
				if(resourceListener != null) {
					resourceListener.fileNotFound(imagePath);
				}
				throw e;
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}
	
			FileInputStream in = null;
			ByteArrayOutputStream bufImage = new ByteArrayOutputStream();
			
			try {
				in = new FileInputStream(imgFile);
				
				for(int i = in.read(); i > -1; i = in.read()) {
					bufImage.write(i);
				}
			}
			catch (IOException e) {
				throw e;
			}
			finally {
				if(in != null) {
					in.close();
				}
			}
			
			objOther = new FileOther();
			objOther.setContentType(MimeType.getInstance().getMine(imgFile.getName()));
			objOther.setNameFile(imgFile.getName());
			objOther.setOut(bufImage);
			
			imageCache.put(imagePath,objOther);
		}
		
		return objOther;
	}
	
	public ByteArrayOutputStream getStream(String imagePath) throws ServletException, IOException {
		//ByteArrayOutputStream bufImage = (ByteArrayOutputStream) imageCache.get(imagePath);
	
		FileOther objOther = getObjOther(imagePath);
		ByteArrayOutputStream out = new ByteArrayOutputStream();;
		out.write(objOther.getOut().toByteArray());

		return out;
	}

}
