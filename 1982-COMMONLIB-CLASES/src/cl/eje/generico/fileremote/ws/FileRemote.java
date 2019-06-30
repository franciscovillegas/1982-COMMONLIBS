package cl.eje.generico.fileremote.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import cl.eje.generico.RemoteBase;
import cl.eje.generico.fileremote.ws.injectar.Factura;
import cl.eje.generico.fileremote.ws.injectar.UtilFacturas;
import cl.eje.generico.ws.SQLremoteAnalizerToken;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.consultor.ISenchaPage;

/**
 * Clase que administra el Objeto File de forma remota
 * 
 * @author Pancho
 * @since 2017-07-20
 * @see java.io.File
 * */
public class FileRemote extends RemoteBase implements IConsultaDataRow  {
 
	private File remoteFile;
	
	private FileRemote(String urlSW, File remoteFile) throws RemoteException {
		super(urlSW);
		
		this.remoteFile = remoteFile;
	}

	/*A MODO DE PRUEBA*/
	public static void main(String[] args)  {
		try {
			//SQLremoteAnalizer sql = new SQLremoteAnalizer("http://200.54.81.237:7007/wsistsnd/");
			//ConsultaData data = sql.getData("sp_tables");
			//data.printTableOverConsole();
			
//			FileRemote remote = FileRemote.createNewRemoteFile("http://200.54.81.237:7007/wsistsnd/");
//			remote.goTo("\\\\Istfinanzas\\ist\\DteEnvio\\DocRecibidos");
//			
//			ConsultaDataPage page = new ConsultaDataPage(null, 50, 0, 5);
//			
//			 
//			FileRemote[] fr = remote.listFilesPagined(page);
//			for(FileRemote f : fr) {
//				System.out.println(f.getAbsolutePath());
//			}
			
		
			UtilFacturas utilFacturas = new UtilFacturas();
			Set<Factura> facturaAProcesar = utilFacturas.obtenerFacturasAProcesar();
			
			for(Factura factura : facturaAProcesar) {
				
				System.out.println("nombre: " + factura.obtenerNombreDesdeBd() + " rutProveedor: " + factura.rutProveedor);
				factura.asignarFactura();
			}
			
			
//			Importar_byFileRemote importar_byFileRemote = new Importar_byFileRemote();
//			
//			importar_byFileRemote.obtenerFacturasWSDeIST();
			
			
			
			
			/*
			FileRemote remote = FileRemote.createNewRemoteFile("http://200.54.81.237:7007/wsistsnd/");
			remote.goTo("\\\\Istfinanzas\\ist\\DteEnvio\\DocRecibidos");
			int i = 0;
 			FileRemote[] files = remote.listFiles();
			for(FileRemote file : files) {
				System.out.println("\t"+file.getCanonicalPath());
				if(file.isDirectory()) {
					FileRemote[] hijos = file.listFiles();
					
					for(FileRemote hijo : hijos) {
						System.out.println("\t\t"+hijo.getCanonicalPath());
						
						if(hijo.isFile()) {
							if(i++ < 20) {
								hijo.printRemoteFile(System.out);	
							}
							else {
								return;
							}
							
						}
					}
				}
			}
			*/
			 
			
			 
			
		}  catch (Exception e) {
			System.out.println("No se pudo cargar todos los datos");
			e.printStackTrace();
		} 
	}
	
	
	public static FileRemote createNewRemoteFile(String urlRemote) throws RemoteException {
		RemoteBase rb = new RemoteBase(urlRemote); 
		
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) rb.getProxy(ConectorFileRemoteProxy.class);
		String s = cf.createNewRemoteFile(SQLremoteAnalizerToken.getInstance().createToken());
	
		try {
			File file = (File) rb.getObject(s);
			return new FileRemote(urlRemote, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean isDirectory() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.isDirectory(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		Boolean ok = (Boolean) getObject(resultado);
		return ok;
	}
	
	public boolean isFile() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.isFile(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		Boolean ok = (Boolean) getObject(resultado);
		return ok;
	}
	
	public boolean exist() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.exists(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		Boolean ok = (Boolean) getObject(resultado);
		return ok;
	}
	
	/**
	 * Listado de archivos es muy extenso, por ende se cambió a un método que pueda hacer lo mismo pero de forma páginada.
	 * @deprecated
	 * */
	public FileRemote[] listFiles() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.listFiles(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		File[] lista = (File[]) getObject(resultado);
		FileRemote[] listaRemota = new FileRemote[0];
		
		if(lista != null) {
			listaRemota = new FileRemote[lista.length];
			int i=0;
			for(File f : lista) {
				listaRemota[i] = new FileRemote(this.urlSW, f);
				i++;
			}
		}
		
		return listaRemota;
	}
	
	
	
	
	public FileRemote[] listFilesPagined(ISenchaPage page) throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		String resultado = cf.listFilesPagined(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile), getXml(page));
		
		File[] files = (File[]) getObject(resultado);
		FileRemote[] filesRemotes = null;
		
		if(files != null) {
			filesRemotes = new FileRemote[files.length];
			int i = 0;
			
			for(File f : files) {
				filesRemotes[i++] = new FileRemote(urlSW, f);
			}
		}
		
		return filesRemotes;
	}
	
	public Set<String> filepath() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.listFiles(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		File[] lista = (File[]) getObject(resultado);
		Set<String> listaRemota = new HashSet<String>();
		
		if(lista != null) {
			int i=0;
			for(File f : lista) {
				listaRemota.add(f.getPath());
				i++;
			}
		}
		
		return listaRemota;
	}
	
	public void printListFiles(PrintStream pm) throws Exception {
		FileRemote[] lista = listFiles();
		
		for(FileRemote fr : lista) {
			pm.println(fr);
		}
	}
	
	public FileRemote[] listRootDirectories() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		
		String resultado = cf.listRoots(SQLremoteAnalizerToken.getInstance().createToken());
		
		File[] files = (File[])getObject(resultado);
		FileRemote[] filesRemotes = null;
		
		if(files != null) {
			filesRemotes = new FileRemote[files.length];
			int i = 0;
			
			for(File f : files) {
				filesRemotes[i++] = new FileRemote(urlSW, f);
			}
		}
		
		return filesRemotes;
	}
	
	public void goTo(String remotePath) throws RemoteException {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		this.remoteFile = new File(remotePath);
	}
	
	
	public long getFreeSpace() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		 
		String resultado = cf.getFreeSpace(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		
		Long freeSpace = (Long)getObject(resultado);
		return freeSpace;
	}
	
	public void printRemoteFile(PrintStream p) throws Exception {
		List<String> lineas = readLines();
		for(String l: lineas) {
			p.println(l);
		}
	}
	
	public void importRemoteFile(File localDestination) throws IOException, Exception {
		if(localDestination == null) {
			throw new Exception("No existe la ruta :"+localDestination);	
		}
		else if(!isFile()) {
			throw new Exception("No es un archivo:"+localDestination);
		}
		else  {
			FileOutputStream fo = new FileOutputStream(localDestination);
			byte[] bytes = readBytes();
			IOUtils.write(bytes, fo);	
		}
	}
	
	public String getContent() throws IOException, Exception {
		if(!isFile()) {
			throw new Exception("No es un archivo:"+this.remoteFile);
		}
		else  {
			 
			byte[] bytes = readBytes();
			String str = new String(bytes, "UTF-8");
			return str;
		}
	}
	
	public List<String> readLines() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		String resultado = cf.readLines(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		 
		List<String> lines = (List<String>)getObject(resultado);
		return lines;
	}
	
	public byte[] readBytes() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		String resultado = cf.readBytes(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		
		byte[] bytes = (byte[])getObject(resultado);
		return bytes;
	}
	
	public FileRemote getParentFile() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		String resultado = cf.getParentFile(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		
		File file = (File)getObject(resultado);
		return new FileRemote(urlSW, file);
	}
	
	public String getAbsolutePath() {
		return this.remoteFile.getAbsolutePath();
	}
	
	public String getCanonicalPath() throws IOException {
		return this.remoteFile.getCanonicalPath();
	}
	
	public boolean delete() throws Exception {
		ConectorFileRemoteProxy cf = (ConectorFileRemoteProxy) getProxy(ConectorFileRemoteProxy.class);
		String resultado = cf.delete(SQLremoteAnalizerToken.getInstance().createToken(), getXml(remoteFile));
		
		Boolean boo = (Boolean)getObject(resultado);
		return boo;
	}
	
	@Override
	public String toString() {
		return String.valueOf(remoteFile);
	}

	@Override
	public DataFields toDataField() {
		DataFields df = new DataFields();
		df.put("file_name", this.remoteFile.getName());
		df.put("file_path", this.remoteFile.getPath());
		
		
		return df;
	}
	
	public String getName() {
		return this.remoteFile.getName();
	}
	
	public String getPath() {
		return this.remoteFile.getPath();
	}
	
}
