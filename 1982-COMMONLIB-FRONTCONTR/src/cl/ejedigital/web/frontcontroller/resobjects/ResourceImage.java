package cl.ejedigital.web.frontcontroller.resobjects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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

import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.web.CacheWebTypes;
/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ResourceImage implements IResourceManipulate {

	private Cache				imageCache;
	private HttpServletResponse	resp;
	private HttpServletRequest	req;
	private IResourceListener	resourceListener;

	public ResourceImage() {
		imageCache = CacheLocator.getInstance(CacheWebTypes.CACHEIMAGE);
	}

	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}

	public void init(HttpServletRequest req, HttpServletResponse resp) {
		this.resp = resp;
		this.req = req;
	}

	public ServletOutputStream getWriter() throws IOException, ServletException {
		ServletOutputStream out = null;
		String imagePath = "/" + req.getQueryString();
		ByteArrayOutputStream bufImage = (ByteArrayOutputStream) imageCache.get(imagePath);

		if(bufImage == null) {
			File imgFile = null;

			try {
				imgFile = ResourceMapping.getInstance().getFile(imagePath);

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
			bufImage = new ByteArrayOutputStream();
			
			try {
				in = new FileInputStream(imgFile);
				
				for(int i = in.read(); i > -1; i = in.read()) {
					bufImage.write(i);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if(in != null) {
					in.close();
				}
			}
			
			imageCache.put(imagePath,bufImage);
		}
		
		resp.setContentType("image/png");
		out = resp.getOutputStream();
		out.write(bufImage.toByteArray());

		return out;
	}

	public BufferedImage bufferImage(Image image, int type) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),image.getHeight(null),type);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image,null,null);
		waitForImage(bufferedImage);
		return bufferedImage;
	}

	private void waitForImage(BufferedImage bufferedImage) {
		final ImageLoadStatus imageLoadStatus = new ImageLoadStatus();
		bufferedImage.getHeight(new ImageObserver() {

			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				if(infoflags == ALLBITS) {
					imageLoadStatus.heightDone = true;
					return true;
				}
				return false;
			}
		});
		bufferedImage.getWidth(new ImageObserver() {

			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				if(infoflags == ALLBITS) {
					imageLoadStatus.widthDone = true;
					return true;
				}
				return false;
			}
		});
		while(!imageLoadStatus.widthDone && !imageLoadStatus.heightDone) {
			try {
				Thread.sleep(300);
			}
			catch (InterruptedException e) {

			}
		}
	}

	class ImageLoadStatus {

		public boolean	widthDone	= false;
		public boolean	heightDone	= false;
	}

}
