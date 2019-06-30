package portal.com.eje.tools.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson.ParamEncrypted;
import portal.com.eje.frontcontroller.resobjects.MimeType;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.security.IEncrypter;
import portal.com.eje.usuario.UsuarioImagenManager;


public class ServletLoadFile extends MyHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServletLoadFile() {
		super();
		

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
	{
	    doGet(req, resp);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
 		String contentDisposition = request.getParameter("cd");
		
		/*PARAMS DE PARAMETROS*/
        String param  = request.getParameter("param");
        String key    = request.getParameter("key");
        String app	  = request.getParameter("app");
        /*END PARAMS DE PARAMETROS*/
        String enc    = request.getParameter("enc"); //si viene encryptrado
        
        if(request.getParameter("idfile") != null) {
        	/* idfile
        	 * Subido por FileService
        	 * */
        	String idFile = request.getParameter("idfile");
        	//idFile = URLDecoder.decode(idFile);
        	
        	if(enc != null && "true".equals(enc)) {
        		IEncrypter encrypter = ParamEncrypted.getInstance().getEncrypter(request);
        		idFile= encrypter.decrypt(idFile);
        	}
        	
        	int idFileInt = cl.ejedigital.tool.validar.Validar.getInstance().validarInt(idFile, -1);
        	returnFileUnic( idFileInt , contentDisposition, response);
        }
        else if(param != null && key != null ) {
        	returnImagenParametro(request, response);
        }
        else if( request.getParameter("cImagen") != null ) {
        	String cImagen= URLDecoder.decode(request.getParameter("cImagen")); // codigo desde UsuarioImagenManager
        	returnImagenUsuarioImagenManager(request, response);
        }
        else {
        	
        }
	}
	
	private void returnImagenUsuarioImagenManager(HttpServletRequest request, HttpServletResponse response) {
		String cImagen = request.getParameter("cImagen");
		
		UsuarioImagenManager um = new UsuarioImagenManager();
		
		try {
		 
			
			String imageName = "imagen.jpg";
			response.setContentType(MimeType.getInstance().getMine(imageName));
			response.setHeader("Content-Disposition", "inline ; filename=\""+imageName+"\"");
			
			try {
				byte[] bytes = um.getImagen(cImagen);
				IOUtils.write(bytes, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		 
		}
	}
	
	private void returnImagenParametro(HttpServletRequest request, HttpServletResponse response) {
		Validar val 	  = new Validar();
		String app	  = request.getParameter("app"); //OPCIONAL
		String param  	  = request.getParameter("param");
        String key    	  = request.getParameter("key");
        
        String cd 	  	  = val.validarDato( request.getParameter("cd") , "attachment");
        
        javax.servlet.ServletOutputStream out = null;
        
        try {
        	
        	if( app == null ) {
        		app = ParametroLocator.getInstance().getModuloContext();
        	}
        	
        	EModulos enumApp = EModulos.getModulo(app);	
        	
	        ParametroValue pv = ParametroLocator.getInstance().getValor(enumApp, param, key);
	        if(pv != null) {
	            String fileName   = val.validarDato( pv.getValue() , "noname.jpg");
		    
		        //response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", cd+"; filename=\""+fileName+"\"");
				
				out = response.getOutputStream();
				Base64Encoder b64 = new Base64Encoder();
				byte[] bytes = b64.decode(pv.getBase64file());
				 
				if(out != null) {
					out.write(bytes);
				}
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(out != null) {
					out.flush();
					out.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	
	private void returnFileUnic(int idFile, String contentDisposition, HttpServletResponse response) {
		int largoBuffer = 1024;
		
		File archivo = null;
		Connection connexion = DBConnectionManager.getInstance().getConnection("portal");
		try {
			cl.ejedigital.web.fileupload.FileService fs = new cl.ejedigital.web.fileupload.FileService(getServletContext());
			cl.ejedigital.web.fileupload.vo.EjeFilesUnico u = fs.getFileUnico(idFile);
			
			String realPath = this.getServletContext().getRealPath("/");
			String workPath = null;
			
			if(u.getRelativePathToWebcontent() != null) {
				workPath = u.getRelativePathToWebcontent();
				
				if(workPath != null && workPath.length() > 0 ) {
					if(!File.separator.equals(workPath.substring(workPath.length()-1, workPath.length()))) {
						workPath += File.separator;
					}
				}
			}
			else {
				workPath = "temporal".concat(File.separator)+"filesUnicos"+File.separator;
			}
			
			
			String fileName= "descarga";
			if( u != null) {
				fileName=u.getNameOriginal();
			}
			if(contentDisposition == null) {
				contentDisposition = "attachment";
			}
			if(idFile > 0) {
				archivo = new File(realPath+workPath+u.getNameUnic());
			}
			else {
				archivo = new File(realPath+workPath+"notFound.png");
			}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", contentDisposition+"; filename=\""+fileName+"\"");
			
			javax.servlet.ServletOutputStream out = response.getOutputStream();
			
			if(archivo != null && archivo.exists()) {
				FileInputStream is = new FileInputStream(archivo);
				
				
		        byte paso[] = new byte[largoBuffer];
		        
		        for(int largo = 0; (largo = is.read(paso)) != -1;)
		            out.write(paso, 0, largo);
		
		        is.close();
			}
			else {
				System.out.println("No existe el archivo \""+archivo+"\"");
			}
			
	        out.flush();
	        out.close();
	    } 
		catch (IOException e) {
			OutMessage.OutMessagePrint("Problemas con el archivo '" + archivo.getAbsolutePath() + "'");
            e.printStackTrace();
	    }
		finally {
			DBConnectionManager.getInstance().freeConnection("portal",connexion); 
		}
	}
	
	/**
	 * Implemetación de FileService
	 * */
	
	private void returnFileUnic(HttpServletRequest request, HttpServletResponse response) {
		Validar val = new Validar();
		String contentDisposition = request.getParameter("cd");
		int idFile = val.validarNum(request.getParameter("idfile"));
		
		returnFileUnic(idFile, contentDisposition, response);;
	}
	
}