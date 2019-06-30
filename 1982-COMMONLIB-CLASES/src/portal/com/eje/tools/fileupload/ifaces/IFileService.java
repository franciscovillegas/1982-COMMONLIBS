package portal.com.eje.tools.fileupload.ifaces;

import java.io.File;

import cl.ejedigital.consultor.ConsultaData;

import portal.com.eje.tools.fileupload.vo.EjeFileUnicoTipo;
import portal.com.eje.tools.fileupload.vo.EjeFilesUnico;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */

public interface IFileService {

	int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile);
	
	int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replica);

	public boolean existeFile(String nombreUnico);

	public File getFile(int idFile);
	
	public File getFile(int idFile, String relativePath);
	
	public EjeFilesUnico getFileUnico(int idFile) ;
	
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile) ;
}
