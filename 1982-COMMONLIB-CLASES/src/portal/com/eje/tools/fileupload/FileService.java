package portal.com.eje.tools.fileupload;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;

import javax.servlet.ServletContext;

import portal.com.eje.tools.consola.Consola;
import portal.com.eje.tools.fileupload.ifaces.IFileService;
import portal.com.eje.tools.fileupload.ifaces.IReplication;
import portal.com.eje.tools.fileupload.vo.EjeFileUnicoTipo;
import portal.com.eje.tools.fileupload.vo.EjeFilesUnico;
import portal.com.eje.tools.fileupload.vo.EstadisticaReplicacion;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.strings.MyString;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
public class FileService implements IFileService {
	private FileManager fm;
	private ServletContext sContext;
	private String relativePath;
	
	public FileService(ServletContext sContext) {
		super();
		
		Consola.getInstance().printInfo(" FileService: Construyendo ") ;
		
		this.fm = new FileManager();
		this.sContext = sContext;
		this.relativePath = "Files".concat(File.separator).concat("filesUnicos");
		
		
		Consola.getInstance().printInfo(" FileService: Termino de contruccion") ;
	}

	public File getFile(int idFile, String relativePath) {
		Consola.getInstance().printInfo(" getFile ("+idFile+","+relativePath+")");
		
		String pathBase = sContext.getRealPath(relativePath);
		EjeFilesUnico unic = getFileUnico(idFile);
		File archivonuevo		= new File(pathBase+File.separator+unic.getNameUnic());
		
		return archivonuevo;
	}
	
	public File getFile(int idFile){
		return this.getFile(idFile,this.relativePath);
	}
	
	
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile){
		return fm.getFiles(tipoFile);		
	}
	
	public int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile) {
		return this.addFile(rutSubida,cfile,tipoFile,null);
	}
	
	public boolean updateGlosa(int idArchivo, String glosa) {
		return this.fm.updateGlosa(idArchivo, glosa);
	}
	
	public boolean delRegFile(int idArchivo) {
		return this.fm.delFile(idArchivo);
	}
	
	public int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replication){
		boolean existe = false;
		int newId = -1;
		Consola.getInstance().printInfo(" FileService: addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replication)");
		
		try {
		
			String nombreUnico;
			String nameOriginal = cfile.getName();
			
			int pos = nameOriginal.lastIndexOf(".") + 1;
			String extension = nameOriginal.substring(pos);
			
			do {
				MyString my = new MyString();
				nombreUnico = my.getRandomString("abcdefghijklmnopqrstuvwxyz0123456789_",40);
				existe = existeFile( nombreUnico);
				
			 } while ( existe == true );
			
			
			String pathBase = cfile.getParent();
			File archivoACambiar 	= new File(pathBase+File.separator+nameOriginal);
			File archivonuevo		= new File(pathBase+File.separator+nombreUnico+"."+extension);
			
			Consola.getInstance().printInfo(" FileService: nombreOriginal ->"+archivoACambiar);
			Consola.getInstance().printInfo(" FileService: nombreUnico ->"+archivonuevo);
			
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
			
			archivoACambiar.renameTo(archivonuevo);
			
			Consola.getInstance().printInfo(" FileService: Agregando archivo a la BD ");
			newId = fm.insertFile( new EjeFilesUnico(tipoFile, rutSubida, nameOriginal,archivonuevo.getName(), bytes, machineName));
			Consola.getInstance().printInfo(" FileService: archivo  agregado");
		
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
	
	private void replica(IReplication replication, File file) {
		try {
		
			EstadisticaReplicacion est = replication.replica(file);
			
			if(est.getCantError() != 0) {
				ICorreoBuilder correo = new BuilderCorreoEstadistica(est);
				ICorreoProcess proc  = new CorreoProcessPropertie(correo);
				CorreoDispatcher.getInstance().sendMail(proc);
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


	


}