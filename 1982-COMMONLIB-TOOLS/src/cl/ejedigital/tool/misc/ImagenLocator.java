package cl.ejedigital.tool.misc;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import cl.ejedigital.tool.strings.MyString;
 

public class ImagenLocator implements IImageTranformer {
	private static IImageTranformer instance;
	
	private  ImagenLocator() {
		
	}
	
	public static IImageTranformer getInstance() {
		if(instance == null) {
			synchronized (ImagenLocator.class) {
				if(instance == null) {
					instance = new ImagenLocator();
				}
			}
		}
		
		return instance;
	}
	
	public BufferedImage getBufferedImage(String base64) {
		byte[] imageBytes = Base64.getDecoder().decode(base64);
        
		BufferedImage bufferedImg = null;
		try {
			bufferedImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bufferedImg;
	}
	
	public BufferedImage redimensionarImagen(BufferedImage originalImage, double width, double heigth) {
		 BufferedImage resizeImageJpg = null;
		 
		if(originalImage != null) {
	        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB
	                                               : originalImage.getType();
	        
	        double w = originalImage.getWidth();
	        double h = originalImage.getHeight();
	        
	        double porcW = width  * 100D / w;
	        double porcH = heigth * 100D / h;
	        
	        double porc = porcW > porcH ? porcW : porcH;
	        
	        int finalW = (int)(w * (porc / 100));
	        int finalH = (int)(h * (porc / 100));
	        
	        BufferedImage resizedImage = new BufferedImage((int)finalW,(int)finalH, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, (int)finalW, (int)finalH, null);
			g.dispose();
			
	       
		}
		
		return resizeImageJpg;
	}
 
	
	public File redimensionarImagen(File file, double width, double heigth) {
		 try {
			BufferedImage originalImage = ImageIO.read(file);
			redimensionarImagen(originalImage, width, heigth);
			ImageIO.write(originalImage, "jpg", file);
		
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 
		 return file;
	}

	
	/**
	 * String de imagen, debe venir completa, incluyendo el tipo de imagen.
	 * */
 
	public File getFile(String base64WithHeader) {
		String[] partes = base64WithHeader.split("\\,");
		String base64	   = partes[1];
		String extension   = partes[0];
		extension = extension.toLowerCase().split("\\/")[1].split("\\;")[0];
		
		MyString ms = new MyString();
		String nameFile = ms.getRandomString("abcdefghijklmnopqrstuvwxyz123456789", 20) + "." +extension;
		
		ImagenPath ip = new ImagenPath();
		File fileDest = new File(ip.getTemporalPath() + File.separator + nameFile);
		
		BufferedImage bufferedImg = ImagenLocator.getInstance().getBufferedImage(base64);
		try {
			ImageIO.write(bufferedImg, extension, fileDest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileDest; 
	}
	
}
