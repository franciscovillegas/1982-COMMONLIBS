package portal.com.eje.frontcontroller.ioutils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import cl.ejedigital.web.fileupload.FileService;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.MimeType;
import portal.com.eje.portal.factory.Weak;

public class IOUtilFile extends IOUtil {

	public static IOUtilFile getInstance() {
		return Weak.getInstance(IOUtilFile.class);
	}
	
	/**
	 * @since 02-agosto-2016
	 * @author Francisco
	 * 
	 * Importa por a través de la implementación de FileService el archivo que viene con el parámetro indicado.<br/>
	 * En lo mismo que ejecutar la siguientes lineas, suponiendo que le nombre del parámetro es <b>"file"</b> y el tipo de archivo que contiene es la <b>"imagen del trabajador"</b>: <br/>
	 * <br/>
	 * <i>
	 *  File f = io.getFile("file");<br/>
	 *	<br/>
	 *	FileService fs = new FileService(io.getServletContext());<br/>
	 *	Integer newID = fs.addFile(io.getUsuario().getRutIdInt(), f, "fotografía_trabajador")<br/>
	 *	<br/>
	 *	return newID;<br/>
	 *  </i>
	 *  <br/>
	 *  <b>Es importante mencionar que si no es posible crear la relación , sea por que no viene archivo o algún otro problema, el método retornará null.</b>
	 * */
	
	public Integer importParamFile(IIOClaseWebLight  io, String nomParam, String tipArchivoCargado) {
		File f = io.getFile(nomParam);
		Integer newID = null;
		
		if(f != null && f.exists()) {		
			FileService fs = new FileService(io.getServletContext());
			newID = fs.addFile(io.getUsuario().getRutIdInt(), f, tipArchivoCargado);
		}
		
		return newID;
	}
	/**
	 * Retorna un file, este puede ser visto en linea o en como archivo atachado
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 * */

	public void retFile(IIOClaseWebLight  io, File file, String fileName, boolean inline) {
		if(file == null || !file.exists()) {
			return;
		}
		
		PrintWriter out = null;
		ServletOutputStream out2 = null;
		InputStream is = null;
		
		try {
			HttpServletResponse resp = io.getResp();
			HttpServletRequest  req  = io.getReq();
	        resp.setContentType(MimeType.getInstance().getMine(file.getName()));      
	        String attachment = "attachment";
	        if(inline) {
	        	attachment= "inline";
	        }
	        resp.setHeader("Content-Disposition", attachment+";fileName=".concat(fileName)); 		
	        /*
	        out = getResp().getWriter();
	        
	        is = FileUtils.openInputStream(file);
	        char[] chars = IOUtils.toCharArray(is);
	        
	        out.write(chars);
	        */
	        
	        out2 = io.getResp().getOutputStream();
	        byte[] buffer = FileUtils.readFileToByteArray(file);
	        out2.write(buffer);
		}
		catch(Exception e) {
			System.out.println("Error->"+e);
		}
		finally {
			if(out != null) {
				out.flush();
				out.close();   
			}
			
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public FileService getFileService(IIOClaseWebLight  io) {
		return new FileService(io.getServletContext());
	}
	public void retFile(IOClaseWeb io, File file, boolean inline) {
		retFile(io, file, file.getName(), inline);
		
	}
}
