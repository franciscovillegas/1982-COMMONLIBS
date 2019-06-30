package cl.ejedigital.tool.svn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.SVNFileUtil;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


public class SnvManipulator implements ISvnManipulator {
	
	private String url;
	SVNRepository repository = null;
	ISVNAuthenticationManager authManager = null;

	public SnvManipulator(String url) {
		super();
		this.url = url;
		try {
			this.repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(this.url));
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se crear Factory en base a URL de SVN" );
		}
	}

	public boolean connect(String usuario, String clave) {
		this.authManager = SVNWCUtil.createDefaultAuthenticationManager(usuario,clave);
		this.repository.setAuthenticationManager(authManager);
		try {
			this.repository.testConnection();
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se pudo conectar a SVN" );
			return false;
		}
		return true;
	}

	public boolean disconnect() {
		this.repository.closeSession();
		return true;
	}

	public boolean mkDir(String nameDirectory,String relativePath) {
		ISVNEditor editor;
		try {
			editor = this.repository.getCommitEditor("Crear directorio", null);
			editor.openRoot(-1);
			editor = cdDir(editor,relativePath,LatestRevision());
			editor.addDir(nameDirectory,null,LatestRevision());
			editor.closeEdit();
			return true;
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se pudo crear directorio" );
			return false;
		}
	}
	
	public boolean rmDir(String nameDirectory,String relativePath) {
		try {
			ISVNEditor editor = repository.getCommitEditor("Borrar directorio", null);
			editor.openRoot(-1);
			editor = cdDir(editor,relativePath,LatestRevision());
			editor.deleteEntry(nameDirectory,LatestRevision());
			editor.closeEdit();
			return true;
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se pudo borrar directorio" );
			return false;
		}
	}
	
	public ISVNEditor cdDir(ISVNEditor editor, String relativePath, long revision) {
		try {
			editor.openDir(relativePath, revision);
			return editor;
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se realizo operacion de cambio de directorio" );
			return editor;
		}
	}
	
	public boolean uploadFile(File file, String relativePath) {
		if(existsPath(relativePath + "/" + file.getName())) {
			return updateFile(file,relativePath);
		}
		else {
			return addFile(file,relativePath);
		}
	}
	
	public boolean addFile(File file, String relativePath) {
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        ISVNEditor editor;
		try {
			editor = this.repository.getCommitEditor("Subida a repositorio de archivo", null);
			editor.openRoot(-1);
			editor = cdDir(editor,relativePath,LatestRevision());
			editor.addFile(file.getName(), null, LatestRevision());
			editor.applyTextDelta(this.url + "/" + relativePath, null);
			InputStream is = SVNFileUtil.openFileForReading(file);
			String chksm = deltaGenerator.sendDelta(this.url + "/" + relativePath, is, editor, true);
			is.close();
			editor.textDeltaEnd(file.getName());
			editor.closeFile(file.getName(), chksm);
			editor.closeEdit();
			return true;
		} 
        catch (IOException e) {
        	System.out.println("Problema : Problemas con la manipulacion del archivo" );
			return false;
		}
		catch (SVNException e) {
			System.out.println("Problema : Problemas al tratar de agregar el archivo al SVN" );
			return false;
		}
	}
	
	public boolean updateFile(File file, String relativePath) {
		try {
			String filePath = relativePath + "/" + file.getName();
			ISVNEditor editor = this.repository.getCommitEditor("Modificacion archivo existente", null, true, null);
			editor.openRoot(-1);
			editor = cdDir(editor,relativePath,LatestRevision());
			editor.openFile(filePath, -1);
			editor.applyTextDelta(filePath, null);
			final SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
			InputStream is = SVNFileUtil.openFileForReading(file);
			final String checksum = deltaGenerator.sendDelta(filePath, is, editor, true);
			editor.closeFile(filePath, checksum);
			editor.closeDir(); 
			editor.closeEdit();
			return true;
		} 
		catch (SVNException e) {
			e.printStackTrace();
			System.out.println("Problema : Problemas al tratar de actualizar el archivo al SVN" );
			return false;
		}
	}
	
	public boolean uploadFiles(List<File> files, String relativePath) {
		boolean resultado=true;
		for (File file : files) { 
			resultado=uploadFile(file,relativePath);
		}
		return resultado;
	}
	
	public boolean deleteFile(String nameFile, String relativePath) {
		ISVNEditor editor;
		try {
			editor = repository.getCommitEditor("Eliminación archivo", null);
			editor.openRoot(-1);
			editor = cdDir(editor,relativePath,LatestRevision());
			editor.deleteEntry(nameFile,LatestRevision());
			editor.closeEdit();
			return true;
		} 
		catch (SVNException e) {
			System.out.println("Problema : Problemas al tratar de elimnar el archivo del SVN" );
			return false;
		}
	}

	public boolean deleteFiles(List<String> nameFiles, String relativePath) {
		boolean resultado=true;
		for (String nameFile : nameFiles) { 
			resultado=deleteFile(nameFile,relativePath);
		}
		return resultado;
	}
	
	public boolean exportFiles(String PathDest, String relativePath) {
		try {
			SVNClientManager ourClientManager = SVNClientManager.newInstance();
			ourClientManager.setAuthenticationManager(this.authManager);
			SVNUpdateClient updateClient = ourClientManager.getUpdateClient( );
			updateClient.setIgnoreExternals( false );
			String urlRepository = this.repository.getLocation() + "/" + relativePath; 
			SVNRepository repositoryLocation = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(urlRepository));
			updateClient.doExport( repositoryLocation.getLocation(), new File(PathDest),SVNRevision.create(LatestRevision()), SVNRevision.create(LatestRevision()),null, true, SVNDepth.INFINITY);
			return true;
		} 
		catch (SVNException e) {
			System.out.println("Problema : Problemas al exportar archivos al directorio" );
			return false;
		}
	}
	
	public boolean existsPath(String relativePath) {
		try {
			SVNURL dirUrl = SVNURL.parseURIEncoded(this.url);
			SVNURL fileUrl = dirUrl.appendPath(relativePath, false);
	        SVNRepository repositoryTemp = SVNRepositoryFactory.create(fileUrl);
	        repositoryTemp.setAuthenticationManager(this.authManager);
			SVNNodeKind fileKind = repositoryTemp.checkPath("",LatestRevision());
	        if (fileKind == SVNNodeKind.NONE) {
	        	return false;
			} 
	        else {
	        	return true;
	        }
		} 
		catch (SVNException e) {
			System.out.println("Problema : No se pudo verificar la ruta" );
			return false;
		}
	}
	
	public long LatestRevision() {
		//try { return this.repository.getLatestRevision();
		return -1;
		//} catch (SVNException e) { System.out.println("Problema : No pudo obtener la ultima revision" ); return 0; }
	}

}
