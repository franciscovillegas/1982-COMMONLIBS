package portal.com.eje.frontcontroller.ioutils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;

public class IOUtilImage extends IOUtil {

	
	public static IOUtilImage getIntance() {
		return Weak.getInstance(IOUtilImage.class);
	}
	/**
	 * Importa una imagen y redimensiona proporcionalmente según width<br/>
	 * si w != null && h != null && forcePoportions == true then proporciona a w <br/>
	 * si w != null && h != null && forcePoportions == false then proporciona a w y h<br/>
	 * @throws IOException 
	 * */
	public Integer importParamImagen(IIOClaseWebLight io, String paramName, String descripcion, Integer w, Integer h, boolean forceProportions) throws IOException {
		int idFile = io.getUtil(IOUtilFile.class).importParamFile(io, paramName, descripcion);
		File file = io.getUtil(IOUtilFile.class).getFileService(io).getFile(idFile);
		
		BufferedImage buffered = null;
		//boolean forceProportions = true;
		int fW = -1;
		int fH = -1; 
		
		if(file.exists()) {
			String ext = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()).toLowerCase();
			List<String> extensionesPemitidas = new ArrayList<String>();
			extensionesPemitidas.add(".jpeg");
			extensionesPemitidas.add(".jpg");
			extensionesPemitidas.add(".bmp");
			extensionesPemitidas.add(".gif");
			
			if(extensionesPemitidas.indexOf(ext) != -1) {
				buffered = ImageIO.read(file);	
			}
			else {
				throw new IOException("Solo se permiten imagenes :"+ext);
			}
			 
		}
		else {
			 throw new IOException("No existe el archivo ");
		}
			
		int oW = buffered.getWidth();
		int oH = buffered.getHeight();
		 
		if(w == null && h == null) {
			fW = oW;
			fH = oH;
		}
		else if(w != null && h == null ) {
			fW = w;
			fH = obtenerH(oW, oH, w);
		}
		else if(w == null && h != null ) {
			fW = obtenerW(oW, oH, h);
			fH = h;
		}
		else  if(w != null && h != null ){
			
			int tmpH = obtenerH(oW, oH, w);
			int tmpW = obtenerW(oW, oH, h);
			
			if(!forceProportions) {
				fW = tmpW;
				fH = tmpH;
			}
			else {
				int wDecremental = w;
				while(tmpH > h) {
					wDecremental--;
					tmpH = obtenerH(oW, oH, wDecremental);
				}
				
				fW = wDecremental;
				fH = tmpH;
			}
		}
		
		BufferedImage bi = resize(buffered, fW, fH);
		 
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			ByteArrayOutputStream bufImage = new ByteArrayOutputStream();
			
			ImageIO.write( bi, "jpg", bufImage );
			bufImage.flush();
			byte[] imageInByte = bufImage.toByteArray();
			
			//io.getResp().setHeader( "Content-Disposition" ,"inline;fileName=".concat(f.getName()));
			
			out.write(imageInByte);
			
			
		} catch (IOException e) {
			throw e;
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return idFile;
	}	
	
	public BufferedImage resize(BufferedImage img, int newW, int newH) {  
	    int w = img.getWidth();  
	    int h = img.getHeight();  
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;  
	}  
	
	private int obtenerH(int originalW, int originalH, int newW) {
		return (int)(new Double(newW) * new Double(originalH) / new Double(originalW));
	}
	
	private int obtenerW(int originalW, int originalH, int newH) {
	    return (int)(new Double(newH)  * new Double(originalW) / new Double(originalH));
	}
}
