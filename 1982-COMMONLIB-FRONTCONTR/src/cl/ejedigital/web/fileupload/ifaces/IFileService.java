package cl.ejedigital.web.fileupload.ifaces;

import java.io.File;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;
import cl.ejedigital.web.fileupload.vo.EjeFilesUnico;


public interface IFileService {

	int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile);
	
	/**
	 * @deprecated
	 * @since 2015-DIC-17
	 * @author Francisco
	 * 
	 * Ya no es necesario tener previamente los tipos, cada uno podrá crearlos en cualquier momento pasando una cadena de caracteres como tipo
	 * 
	 * */
	int addFile(int rutSubida, File cfile, EjeFileUnicoTipo tipoFile, IReplication replica);
	
	int addFile(int rutSubida, File cfile, Object tipoFile, IReplication replica);

	public boolean existeFile(String nombreUnico);

	public File getFile(int idFile);
	
	public File getFile(int idFile, String relativePath);
	
	public EjeFilesUnico getFileUnico(int idFile) ;
	
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile) ;
}
