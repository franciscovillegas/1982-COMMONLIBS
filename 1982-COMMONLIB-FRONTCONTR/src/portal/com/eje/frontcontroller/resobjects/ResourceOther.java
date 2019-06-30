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

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.Cache.CachePortalTypes;
import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.validar.Validar;


public class ResourceOther implements IResourceManipulate {
	private Cache otherCache;
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private IResourceListener	resourceListener;
	
	public ResourceOther() {
		otherCache = CacheLocator.getInstance(CachePortalTypes.CACHEOTHER);
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
		ServletOutputStream out = null;
		String otherPath = "/" + ResourceTool.getInstance().getObject(req);
		FileOther objOther =  (FileOther) otherCache.get(otherPath);
		//ByteArrayOutputStream bufOther = (ByteArrayOutputStream) otherCache.get(otherPath);		
		
		if(objOther == null) {
			File otherFile = null;

			try {
				otherFile = ResourceMapping.getInstance().getFile(req, otherPath);

			}
			catch (FileNotFoundException e) {
				if(resourceListener != null) {
					resourceListener.fileNotFound(otherPath);
				}
				throw e;
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}

			FileInputStream in = null;
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			
			try {
				in = new FileInputStream(otherFile);
				
				for(int i = in.read(); i > -1; i = in.read()) {
					buffer.write(i);
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
			objOther.setContentType(MimeType.getInstance().getMine(otherFile.getName()));
			objOther.setNameFile(otherFile.getName());
			objOther.setOut(buffer);
			
			otherCache.put(otherPath,objOther);
		}
			
		return objOther;
	}
	
	public ByteArrayOutputStream getStream(String pathObjOther) throws ServletException, IOException {
		//ByteArrayOutputStream bufImage = (ByteArrayOutputStream) imageCache.get(imagePath);
	
		FileOther objOther = getObjOther(pathObjOther);
		ByteArrayOutputStream out = new ByteArrayOutputStream();;
		out.write(objOther.getOut().toByteArray());

		return out;
		
		
	}
 

	
}
