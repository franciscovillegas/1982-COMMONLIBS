package repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import portal.com.eje.frontcontroller.resobjects.EscapeSencha;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.svn.SnvManipulator;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.frontcontroller.IOClaseWeb;

import com.google.gson.JsonObject;

import freemarker.template.SimpleHash;

public class FileRepositoryServlet extends MyHttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private static final String separadorSVN ="/";
	private static final String directoryDownload = separadorSVN + "Repository" + separadorSVN + "Download" + separadorSVN;
	private static final String directoryUpload = separadorSVN + "Repository" + separadorSVN + "Upload" + separadorSVN;
	private Usuario user;
	
	public FileRepositoryServlet() {
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
//		    	MMA 20170112
//			    java.sql.Connection Conexion = connMgr.getConnection(user.getJndi());
			    Connection Conexion = getConnMgr().getConnection(user.getJndi());
				RepositoryManager rmng = new RepositoryManager();
				String html = null;
				String accion = request.getParameter("accion");
				String rut = request.getParameter("trabajador") == null ? "0" : request.getParameter("trabajador");
				if(accion.equals("ruta")) {
					String strAbrev = request.getParameter("abrev");
					DataList result = new DataList();
					ConsultaData data = rmng.GetNameFilesTrabajador(user.getRutId(), strAbrev, Conexion);
					while(data!=null && data.next()) {
						String strUrl = data.getForcedString("archivo");
						DataFields fields = new DataFields();
						result.add(fields);
						fields.put("archivo", strUrl);
						JSonDataOut dataOutResponse = new JSonDataOut(result);
						dataOutResponse.setEscape(new EscapeSencha());
						retTexto(response, dataOutResponse.getListData());
//						retTexto(response, "{""archivo"": """+strUrl+"""}");
					}
				}else if(accion.equals("dis")) {
					html="repository/file-view.htm";
					modelRoot.put("trabajador", rmng.GetTrabajadores(Conexion) );
					if(!rut.equals("0")) {
						ConsultaData data = rmng.GetFilesTrabajador(rut,Conexion);
						FreemakerTool free = new FreemakerTool();
						modelRoot.put("existentes", free.getListData(data) );
						modelRoot.put("noexistentes", rmng.GetNoClasificadoTrabajador(rut,Conexion) );
					}
					modelRoot.put("accion", accion);
					modelRoot.put("rut", rut);
					modelRoot.put("nombre",rmng.GetNombreTrabajador(Conexion,rut));
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
				else if(accion.equals("new")) {
					html="repository/file-new.htm";
					modelRoot.put("trabajador", rmng.GetTrabajadores(Conexion) );
					if(!rut.equals("0")) {
						modelRoot.put("clasificacion", rmng.GetResumenClasificacionTrabajador(rut,Conexion) );
						modelRoot.put("nombre",rmng.GetNombreTrabajador(Conexion,rut));
					}
					modelRoot.put("accion", accion);
					modelRoot.put("rut", rut);
				}
				else if(accion.equals("newsave")) {
					html="repository/file-new.htm";
					modelRoot.put("trabajador", rmng.GetTrabajadores(Conexion) );
					if(!rut.equals("0")) {
						try {
					        List<FileItem> items2 = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
					        for (FileItem item : items2) {
					            if (item.isFormField()) {
					                System.out.println("Formulario : " + item.getFieldName() + " : " + item.getString());
					            } 
					            else {
					                boolean uf = uploadFile(rut,item);
					                boolean us = uploadFileToSVN(rut,item,rmng,Conexion);
					                if(uf && us) { 
					                	saveFileToBD(rut,item,rmng,Conexion);
					                }
					            }
					        }
					    } 
						catch (FileUploadException e) {
					        throw new ServletException("Cannot parse multipart request.", e);
					    }
						modelRoot.put("clasificacion", rmng.GetResumenClasificacionTrabajador(rut,Conexion) );
						modelRoot.put("nombre",rmng.GetNombreTrabajador(Conexion,rut));
					}
					modelRoot.put("accion", "new");
					modelRoot.put("rut", rut);
				}
				else if(accion.equals("del")) {
					modelRoot.put("trabajador", rmng.GetTrabajadores(Conexion) );
					if(!rut.equals("0")) {
						String archivos[] = request.getParameterValues("files") == null ? null : request.getParameterValues("files");
						if(archivos != null) {
							for(String valor: archivos){
								deleteFileSVN(valor,rmng,Conexion);
							}
						}
						ConsultaData data = rmng.GetFilesTrabajador(rut,Conexion);
						FreemakerTool free = new FreemakerTool();
						
						modelRoot.put("existentes", free.getListData(data) );
						modelRoot.put("nombre",rmng.GetNombreTrabajador(Conexion,rut));
					}
					html="repository/file-delete.htm";
					modelRoot.put("accion", accion);
					modelRoot.put("rut", rut);
				}
//		    	MMA 20170112
//		        connMgr.freeConnection(user.getJndi(), Conexion);
		        getConnMgr().freeConnection(user.getJndi(), Conexion);
				super.retTemplate(response,html,modelRoot);	
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
	
	private boolean uploadFile(String rut,FileItem item) {
		String fieldname = item.getFieldName();
		String filename = FilenameUtils.getName(item.getName());
		try {
			InputStream filecontent;
			filecontent = item.getInputStream();
			String pathDirectory = getServletContext().getRealPath(directoryUpload);
			new File(pathDirectory + rut).mkdirs();
			File file2 = new File(pathDirectory + rut + File.separatorChar + filename);
			OutputStream outputStream = new FileOutputStream(file2);
			IOUtils.copy(filecontent, outputStream);
			outputStream.close();
			System.out.println("Archivo subido con exito : " +fieldname + " : " + filename);
			return true;
		} 
		catch (IOException e) {
			System.out.println("Archivo no subido : " +fieldname + " : " + filename);
			return false;
		}
	}
	
	private boolean uploadFileToSVN(String rut,FileItem item,RepositoryManager rmng, Connection con) {
		Map<?, ?> mp = rmng.getConfiguracionSVN(con);
		String url = mp.get("url").toString();
		String cliente = mp.get("cliente").toString();
		String user =  mp.get("usuario").toString();
		String pass =  mp.get("clave").toString();
		String fieldname = item.getFieldName();
		String filename = FilenameUtils.getName(item.getName());
		String clasificacion = rmng.GetAbrevClasificacion(fieldname, con);
		SnvManipulator sm = new SnvManipulator(url);
		sm.connect(user,pass);
		String pathDirectory = getServletContext().getRealPath(directoryUpload);
		File file = new File( pathDirectory + rut + File.separatorChar + filename );
		if(sm.existsPath(cliente + separadorSVN + clasificacion)) {
			if(sm.existsPath(cliente + separadorSVN + clasificacion + separadorSVN + rut)) {
				sm.uploadFile(file, cliente + separadorSVN + clasificacion + separadorSVN + rut);
			}
			else {
				sm.mkDir(rut, cliente + separadorSVN + clasificacion);
				sm.uploadFile(file, cliente + separadorSVN + clasificacion +separadorSVN + rut);
			}
		}
		else {
			sm.mkDir(clasificacion, cliente);
			sm.mkDir(rut, cliente + separadorSVN + clasificacion);
			sm.uploadFile(file, cliente + separadorSVN + clasificacion + separadorSVN + rut);
		}
		sm.disconnect();
		file.delete();
		return true;
	}
	
	private boolean saveFileToBD(String rut,FileItem item,RepositoryManager rmng, Connection con) {
		return rmng.addFileToBD(item.getFieldName(),rut,FilenameUtils.getName(item.getName()),con);
	}
	
	private boolean deleteFileSVN(String idfile,RepositoryManager rmng,Connection con) {
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
		boolean delfileSvn = sm.deleteFile(archivo,  cliente + separadorSVN + abrev + separadorSVN + rut);
		boolean delfileBD = rmng.deleteFileBD(idfile,con);
		sm.disconnect();
		if(delfileSvn && delfileBD) {
			return true;
		}
		else {
			return false;
		}
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