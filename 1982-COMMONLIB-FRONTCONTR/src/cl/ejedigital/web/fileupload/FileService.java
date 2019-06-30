package cl.ejedigital.web.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.consola.Consola;
import cl.ejedigital.web.fileupload.ifaces.IFileService;
import cl.ejedigital.web.fileupload.ifaces.IReplication;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;
import cl.ejedigital.web.fileupload.vo.EjeFilesUnico;
import cl.ejedigital.web.fileupload.vo.EstadisticaReplicacion;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet_onInitServer;


public class FileService implements IFileService {
	private FileManager fm;
	private ServletContext sContext;
	private String relativePath;
	private boolean deleteOriginal;
	
	public void setDeleteOriginal(boolean deleteOriginal) {
		this.deleteOriginal = deleteOriginal;
	}


	/**
	 * Ejecutado desde una aplicacion, se guarda el path en el campo FULL_HHD_PATH de la tabla EJE_FILES_UNICO
	 * */
	public FileService() {
		this(null);
	}

	/**
	 * Inicialmente FileService solo servía cuando era ejecutado desde WEB <br/><br/>
	 * <b>Actualización 2018-04-04 Francisco</b><br/>
	 * Ahora puede ser ocupado desde una Aplicación, y el archivo no se cambiará de ruta ni se generará un nombre único, sin embargo,
	 * el archivo se igual forma tendrá un número generado por BD único para ser referenciado a través de este. <br/><br/>
	 * 
	 * <ul>
	 * 	<li>Guarda el <b>relative webcontent path</b> en el campo <b>relative_path_to_webcontent</b> de la tabla <b>EJE_FILES_UNICO</b> </li>
	 * 	<li>Si se le pasa ServletContent entonces es web.</li>
	 * </ul>
	 * */
	public FileService(ServletContext sContext) {
		super();
		
		deleteOriginal = true;
		
		Consola.getInstance().printInfo(" FileService: Construyendo "+(sContext != null? "(2018-04-04 WEB MODE)" : "(2018-04-04 APLICATION MODE)")) ;
		
		this.fm = new FileManager();
		this.sContext = sContext;
		this.relativePath = File.separator+"temporal".concat(File.separator).concat("filesUnicos");

		
		Consola.getInstance().printInfo(" FileService: Termino de contruccion") ;
	}

	public File getFile(int idFile, String relativePath) {
		/**/
		
		String pathBase = null;
		
		File folderClasses = new File(this.getClass().getResource("/").getFile());
		String fullPath = folderClasses.getAbsolutePath();
 
		EjeFilesUnico unic = getFileUnico(idFile);
		File archivonuevo		= null;
		
		if(fullPath.indexOf("WEB-INF") != -1  && false) {
			/*2018-04-04 FRANCISCO
			 * Anterior a la fecha, para aquellos paths dentro de WEB-INF,
			  creo que no se ocupa !!! no sirve para nada*/
			
			/*2018-04-04 Anterior a la fecha, para aquellos paths dentro de WEB-INF*/
			folderClasses = new File( fullPath.substring(0,fullPath.indexOf("WEB-INF")).replaceAll("%20"," ") + File.separator + relativePath);
			
			if(folderClasses.exists() ) {
				pathBase = folderClasses.getAbsolutePath();
			}
			
			archivonuevo = new File(pathBase+File.separator+unic.getNameUnic());
			
			if(sContext == null) {
				Consola.getInstance().printInfo(" getFile ("+idFile+","+pathBase+")");
			}
		}
		else if(unic.isIsweb()) {
			/*ES WEB*/
			Consola.getInstance().printInfo(" getFile ("+idFile+","+relativePath+")");
			
			archivonuevo = new File(ContextInfo.getInstance().getRealPath(unic.getRelativePathToWebcontent())+File.separator+unic.getNameUnic());
			 
		}
		else {
			/*FUE GUARDADO POR APLICACIÓN*/
			archivonuevo = new File(unic.getFullHddPath()+File.separator+unic.getNameUnic());
		}
		
		
		
		return archivonuevo;
	}
	
	public File getFile(int idFile){
		return this.getFile(idFile,this.relativePath);
	}
	
	
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile){
		return fm.getFiles(tipoFile);		
	}
	
	public int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile) {
		return this.addFile(rutSubida,cfile,tipoFile,new Replication());
	}
	
	public int addFile(int rutSubida, File cfile, String tipoFile) {
		return this.addFile(rutSubida,cfile,tipoFile,new Replication());
	}
	
	
	public boolean updateGlosa(int idArchivo, String glosa) {
		return this.fm.updateGlosa(idArchivo, glosa);
	}
	
	public boolean delRegFile(int idArchivo) {
		return this.fm.delFile(idArchivo);
	}
	
	public int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replica) {
		return addFilePrivate(rutSubida, cfile, tipoFile, replica);
	}

	public int addFile(int rutSubida, File cfile, Object tipoFile, IReplication replica) {
		return addFilePrivate(rutSubida, cfile, tipoFile, replica);
	}
	
	public int addFilePrivate(int rutSubida, File cfile, Object tipoFile, IReplication replication){
		//boolean existe = false;
		int newId = -1;
		
		if(sContext != null) {
			Consola.getInstance().printInfo(" FileService (2018-04-04 WEB MODE): addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replication)");
		}
		else {
			Consola.getInstance().printInfo(" FileService (2018-04-04 APLICATION MODE): addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replication)");
		}
		
		try {
			MyString my = Util.getInstance(MyString.class);
			
			 
			String nameOriginal = cfile.getName();
			
		 
			File archivonuevo		= FileService.getFileUnic(cfile);
			
			File archivoACambiar = cfile;
			
			
			
			Consola.getInstance().printInfo(" FileService: nombreOriginal ->"+archivoACambiar);
			
			if(sContext != null) {
				Consola.getInstance().printInfo(" FileService: nombreUnico ->"+archivonuevo);
			}
			
			long bytes = 0;		
			if(cfile.exists()) {
				bytes = cfile.length();
			}
			
			String machineName = null;
			try {
				machineName = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException e) {
				Consola.getInstance().printWarning("No se pudo reconocer el nombre de la maquina ");
			}
			
			if(!archivonuevo.getParentFile().exists()) {
				archivonuevo.getParentFile().mkdirs();
			}
			
			if(ContextInfo.getInstance().getServletContextName() != null && deleteOriginal) {
				/*SOLO BORRARÁ CUANDO ES WEB*/
				Consola.getInstance().printInfo("Borrando Original ->"+archivoACambiar); 
				archivoACambiar.renameTo(archivonuevo);
			}
			else {
				/*copia, mantiene el original NO SIRVE*/
				if(!archivoACambiar.getAbsoluteFile().equals(archivonuevo.getAbsoluteFile())) {
					archivoACambiar.renameTo(archivonuevo);
				}
			}
			
			/* 20-06-2018 
			 * Pancho se cambiá el nombre para que se cuando se hace POST ajax sobre el archivo se vea correctamente */
			nameOriginal = my.convertFromUTF8(nameOriginal);
			
			Consola.getInstance().printInfo(" FileService: Agregando archivo a la BD ");
			if(ContextInfo.getInstance().getServletContextName() != null) {
				/*ES WEB*/
				String pathFolder = null;
 
				String pathFile = archivonuevo.getParentFile().getCanonicalPath();
				String pathWecontent = ContextInfo.getInstance().getRealPath("/");
				pathFolder = pathFile.substring(pathWecontent.length(), pathFile.length() );
				
				newId = fm.insertFile( new EjeFilesUnico(null, tipoFile, null, rutSubida, nameOriginal, 
														 archivonuevo.getName(), bytes, machineName, null, null,
														 pathFolder, true ));
			}
			else {
				/*ES APLICATION*/
				String pathFolder = null;
				 
				pathFolder = archivonuevo.getParentFile().getCanonicalPath();
				 
				
				newId = fm.insertFile( new EjeFilesUnico(null, tipoFile, null, rutSubida, nameOriginal, 
														 archivonuevo.getName(), bytes, machineName, null, pathFolder,
														  null , false ));
			}
			
			Consola.getInstance().printInfo(" FileService: archivo  agregado, AO:"+archivoACambiar.exists()+", AN:"+archivonuevo.exists());
		
			if(replication != null) {
				replica(replication,archivonuevo);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Consola.getInstance().printError(e);
		}
		
		if( fm.existeIdFile(newId) ) {
			return newId;
		}
		else {
			return -1;
		}
	}
	
	private static String getPeriodoFolder() {
		return String.valueOf( (Calendar.getInstance().get(Calendar.YEAR) * 100) +	(Calendar.getInstance().get(Calendar.MONTH)+1));
	}
	
	private void replica(IReplication replication, File file) {
		try {
		
			EstadisticaReplicacion est = replication.replica(file);
			
			if(est.getCantError() != 0) {
//				ICorreoBuilder correo = new BuilderCorreoEstadistica(est);
//				ICorreoProcess process = new CorreoProcessDefault(correo);
//				CorreoDispatcher.getInstance().sendMail(process);
			}
		} catch (Exception e){
			Consola.getInstance().printError(e);
		}
	}

	
	public boolean existeFile(String nombreUnico){
		return fm.existeNombreUnico(nombreUnico);
	}
	
	public EjeFilesUnico getFileUnico(int idFile) {
		try {
			return fm.getFile(idFile);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * Retorna los archivos cargados en una carpeta temporal, el primer File de la lista es la carpeta y el resto son los archivos 
	 * @author Pancho
	 * @since 26-06-2018
	 * */
	public List<File> getTemporalFiles(List<Integer> idFiles) {
		
		
		List<File> filesTemporales = new ArrayList<File>();
		
		
		
		String pathBase = null;
		
		if(MyHttpServlet_onInitServer.getInstance(null).existContext()) {
			pathBase = ContextInfo.getInstance().getRealPath("temporal");
		}
		else {
 
			pathBase = ContextInfo.getInstance().getRealPath("temporal");
			 
		}
		
		if(idFiles != null && idFiles.size() > 0) {
			MyString myString = Util.getInstance(MyString.class);
			String tmpName = myString.quitaCEspeciales(String.valueOf(Math.random()));
			
			File temporalFolder = new File( pathBase
					.concat(File.separator).concat("filesUnicos")
					.concat(File.separator).concat("tmp_").concat(tmpName));
			filesTemporales.add(temporalFolder);
			
			System.out.println("Tmp folder:"+temporalFolder);

			try {
				for(Integer idFile : idFiles) {
					File file = getFile(idFile);
					EjeFilesUnico eje =  getFileUnico(idFile);
					File fileDest = new File(temporalFolder.getCanonicalPath()+File.separator+eje.getNameOriginal());
					FileUtils.copyFile(file, fileDest);
					
					filesTemporales.add(fileDest);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return filesTemporales;
	}
 
	public static File getFileUnic(File cfile) {
		MyString my = new MyString();
		
		
		String nombreUnico = null;
		String pathBase = ContextInfo.getInstance().getRealPath("temporal");
		File fileCan = null;
		
		String periodoFolder = getPeriodoFolder();
		
		String nameOriginal = cfile.getName();
		
		int pos = nameOriginal.lastIndexOf(".") + 1;
		String extension = nameOriginal.substring(pos);
		
		boolean conPeriodoFolder = true;
		
		if(!conPeriodoFolder) {
			periodoFolder = "";
		}
		
		boolean first = true;
		nombreUnico =  FileService.toSHA1(cfile);
		
		do {
			if(first) {
				fileCan = new File(new StringBuilder()
						.append(pathBase)
						.append(File.separator).append("filesUnicos")
						.append(File.separator).append(periodoFolder)
						.append(File.separator).append(nombreUnico)
						.append(".").append(extension).toString());	
				first = false;
			}
			else {
				fileCan = new File(new StringBuilder()
						.append(pathBase)
						.append(File.separator).append("filesUnicos")
						.append(File.separator).append(periodoFolder)
						.append(File.separator).append(nombreUnico).append("_colision_").append(my.getRandomString(MyString.cadena_alfanumerica, 20))
						.append(".").append(extension).toString());	
			}
			
		 } while ( fileCan.exists());
		
		return fileCan;
	}
	
	public static String toSHA1(File f) {
		byte[] bytes = null;
		InputStream targetStream = null;
		try {
			targetStream = new FileInputStream(f);
			
			bytes = IOUtils.toByteArray(targetStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (targetStream != null) {
				try {
					targetStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return toSHA1(bytes);
	}
	
	public static String toSHA1(byte[] convertme) {
	    final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    byte[] buf = md.digest(convertme);
	    char[] chars = new char[2 * buf.length];
	    for (int i = 0; i < buf.length; ++i) {
	        chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
	        chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
	    }
	    return new String(chars);
	}

}