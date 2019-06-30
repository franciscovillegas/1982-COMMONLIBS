package repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.svn.SnvManipulator;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;

public class ViewFileRepositoryServlet extends MyHttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private static final String separadorSVN ="/";
	private static final String directoryDownload = separadorSVN + "Repository" + separadorSVN + "Download" + separadorSVN;
	private Usuario user;
	
	public ViewFileRepositoryServlet() {
		super();
	}   	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SimpleHash modelRoot = new SimpleHash();
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}
			else {
				System.out.println(user.getJndi());
//		    	MMA 20170111
//		    	Connection Conexion =  connMgr.getConnection(user.getJndi());
		    	Connection Conexion =  getConnMgr().getConnection(user.getJndi());
				RepositoryManager rmng = new RepositoryManager();
				String html = "repository/expediente-view.htm";
				String accion = request.getParameter("accion") == null ? "dis" : request.getParameter("accion");
				String rut = request.getParameter("rut") == null ? user.getRut().getRut() : request.getParameter("rut");
				if(accion.equals("dis")) {
					if(!rut.equals("0")) {
						ConsultaData data = rmng.GetFilesTrabajador(rut,Conexion);
						FreemakerTool free = new FreemakerTool();
						if(data != null) {
							modelRoot.put("archivosSize",  String.valueOf(data.size()) );
							modelRoot.put("archivos"     ,  free.getListData(data));
						}
					}
					modelRoot.put("accion", accion);
					modelRoot.put("rut", rut);
					
					IOClaseWeb io = new IOClaseWeb(this, request, response);
					io.retTemplate(html,modelRoot);
				}
				else if(accion.equals("export")) {
					String idfile = request.getParameter("idfile");
					exportFileSvn(idfile,rmng,Conexion);
					Map<?, ?> map_archivo = rmng.getfileSVN(idfile,Conexion);
					String archivo = map_archivo.get("archivo").toString();
					String fullPath = getServletContext().getRealPath(directoryDownload + rut + separadorSVN + archivo);
					File file = new File(fullPath);
			        response.setContentType(getContentType(archivo));
			        response.setHeader("Content-Disposition", "attachment; filename=" + archivo);
			        int length = (int) file.length();
			        if (length > Integer.MAX_VALUE) { }
			        byte[] bytes = new byte[length];
			        FileInputStream fin = new FileInputStream(file);
			        fin.read(bytes);
			        ServletOutputStream os = response.getOutputStream();
			        os.write(bytes);
			        os.flush();
				}
//		    	MMA 20170111
//		        connMgr.freeConnection(user.getJndi(), Conexion);
		        getConnMgr().freeConnection(user.getJndi(), Conexion);
				//super.retTemplate(response,html,modelRoot);	
			}
		}
		catch (Exception ex) {      
			ex.printStackTrace();
			throw new ServletException(ex.toString(), ex);
		}
	}  	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  
	
	private boolean exportFileSvn(String idfile,RepositoryManager rmng,Connection con) {
		Map<?, ?> mp = rmng.getConfiguracionSVN(con);
		String url = mp.get("url").toString();
		String cliente = mp.get("cliente").toString();
		String user =  mp.get("usuario").toString();
		String pass =  mp.get("clave").toString();
		SnvManipulator sm = new SnvManipulator(url);
		sm.connect(user,pass);
		Map<?, ?> map_archivo = rmng.getfileSVN(idfile,con);
		String abrev = map_archivo.get("abrev").toString();
		String rut = map_archivo.get("rut").toString();
		String archivo = map_archivo.get("archivo").toString();
		String path_dest = getServletContext().getRealPath(directoryDownload + rut + File.separatorChar + archivo);
		String path_orig = cliente + separadorSVN + abrev + separadorSVN + rut + separadorSVN + archivo;
		boolean result = sm.exportFiles(path_dest, path_orig);
		sm.disconnect();
		return result;
	}
	
	String getContentType(String fileName) {
        String extension[] = { "txt", "htm","jpg","png","gif","pdf","doc","docx","xls","xlsx"}; 
        String mimeType[] = { "text/plain","text/html","image/jpg","image/jpg","image/gif","application/pdf","application/msword",
        		"application/msword","application/msexcel","application/x-msexcel",};
        String contentType = "text/html"; 
        int dotPosition = fileName.lastIndexOf('.');
        String fileExtension = fileName.substring(dotPosition + 1);
        for (int index = 0; index < mimeType.length; index++) {
            if (fileExtension.equalsIgnoreCase( extension[index])) {
                contentType = mimeType[index];
                break;
            }
        }
        return contentType;
    }
	
}