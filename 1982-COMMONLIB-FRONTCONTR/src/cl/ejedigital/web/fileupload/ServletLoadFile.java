package cl.ejedigital.web.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.MyHttpServlet;
import cl.ejedigital.web.fileupload.vo.EjeFilesUnico;


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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

			int largoBuffer = 1024;
			
			int idFile = Validar.getInstance().validarInt(request.getParameter("idfile"));
			
			FileService fs = new FileService(getServletContext());
			EjeFilesUnico u = fs.getFileUnico(idFile);
			
			String realPath = this.getServletContext().getRealPath("/");
			String workPath = "temportal"+File.separator+"filesUnicos"+File.separator;
			
			File archivo = new File(realPath+workPath+u.getNameUnic());
			
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename=\""+u.getNameOriginal()+"");
		
			try {
				javax.servlet.ServletOutputStream out = response.getOutputStream();
				
				if(archivo.exists()) {
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
	            e.printStackTrace();
		    } 
	}
	
}