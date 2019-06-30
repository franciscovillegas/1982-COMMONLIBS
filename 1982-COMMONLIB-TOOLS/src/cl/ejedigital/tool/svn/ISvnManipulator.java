package cl.ejedigital.tool.svn;

import java.io.File;
import java.util.List;
import org.tmatesoft.svn.core.io.ISVNEditor;


public interface ISvnManipulator {

	public boolean connect(String usuario, String clave);
	
	public boolean disconnect();
	
	public boolean mkDir(String nameDirectory, String relativePath);
	
	public boolean rmDir(String nameDirectory, String relativePath);
	
	public ISVNEditor cdDir(ISVNEditor editor, String relativePath, long revision);
	
	public boolean uploadFile(File file, String relativePath);
	
	public boolean uploadFiles(List <File> files, String relativePath);
	
	public boolean addFile(File file, String relativePath);
	
	public boolean updateFile(File file, String relativePath);
	
	public boolean deleteFile(String nameFile, String relativePath);
	
	public boolean deleteFiles(List <String> nameFiles, String relativePath);
	
	public boolean exportFiles(String PathDest, String relativePath);
	
	public boolean existsPath(String relativePath);
	
	public long LatestRevision();
	
}
