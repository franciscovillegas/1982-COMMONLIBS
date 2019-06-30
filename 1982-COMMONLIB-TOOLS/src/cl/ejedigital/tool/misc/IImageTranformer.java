package cl.ejedigital.tool.misc;

import java.awt.image.BufferedImage;
import java.io.File;

public interface IImageTranformer {

	public File getFile(String base64);
	
	public BufferedImage getBufferedImage(String base64);
	
	public BufferedImage redimensionarImagen(BufferedImage originalImage, double width, double heigth);
	
	public File redimensionarImagen(File file, double width, double heigth);
	
}
