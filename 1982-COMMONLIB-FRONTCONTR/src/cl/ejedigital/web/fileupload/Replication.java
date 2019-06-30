package cl.ejedigital.web.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cl.ejedigital.web.consola.Consola;
import cl.ejedigital.web.fileupload.ifaces.IReplication;
import cl.ejedigital.web.fileupload.vo.EstadisticaReplicacion;

class Replication implements IReplication {

	private int				cantArchivosCopiados;
	private int				cantArchivosConError;
	private ResourceBundle	proper;
	private final String	properName	= "replication";

	public Replication() {
		proper = null;

		try {
			proper = ResourceBundle.getBundle(properName);
		}
		catch (java.lang.NullPointerException e) {
			System.out.println("[Replicacion de copia]Necesita el archivo \"" + properName
				+ ".properties\" para configurar ReplicationService");
			//e.printStackTrace();
		}
		catch (java.util.MissingResourceException e) {
			System.out.println("[Replicacion de copia] Necesita el archivo \"" + properName
				+ ".properties\" para configurar ReplicationService");
			//e.printStackTrace();
		}

	}

	public EstadisticaReplicacion replica(File fileOrigen) {
		EstadisticaReplicacion est = new EstadisticaReplicacion();
		if(proper == null) return est;
		
		Consola.getInstance().printInfo(" Replicacion: Iniciando replicacion de ->" + fileOrigen);
		
		
		try {
			List rutas = getRutas();
			

			Consola.getInstance().printInfo(" Replicacion:  Copiando archivo :" + fileOrigen);
			est.addMsg(" Replicacion: Copiando archivo :" + fileOrigen);

			for(int i = 0; i < rutas.size(); i++) {
				File fileDestino = new File((String) rutas.get(i));

				if(!fileDestino.isDirectory()) {
					est.addError();
					Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getPath() + "\" No es un directorio");
					est.addMsg("\"" + fileDestino.getPath() + "\" No es un directorio");

				}
				else if(!fileDestino.exists()) {
					est.addError();
					Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getAbsoluteFile() + "\" No existe el directorio ");
					est.addMsg("\"" + fileDestino.getAbsoluteFile() + "\" No existe el directorio ");

				}
				else if(!fileDestino.canWrite()) {
					est.addError();
					Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getAbsoluteFile() + "\" No se puede escribir en el directorio ");
					est.addMsg("\"" + fileDestino.getAbsoluteFile() + "\" No se puede escribir en el directorio ");

				}
				else {

					try {
						fileCopy(fileOrigen,new File(fileDestino,fileOrigen.getName()));
						Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getPath() + "\" ok ");
						est.addMsg(" copia en -> \"" + fileDestino.getPath() + "\" ok ");
						est.addOk();
					}
					catch (FileNotFoundException e) {
						Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getPath() + "\"\n" + e);
						est.addMsg("\"" + fileDestino.getPath() + "\"\n" + e);
						est.addError();

					}
					catch (IOException e) {
						Consola.getInstance().printInfo(" Replicacion: \"" + fileDestino.getPath() + "\"\n" + e);
						est.addMsg("\"" + fileDestino.getPath() + "\"\n" + e);
						est.addError();
					}
				}

			}
		}
		catch (Exception e) {
			Consola.getInstance().printInfo(" Replicacion: Ha ocurrido un error al replicar ");
			Consola.getInstance().printError(e);

		}
		Consola.getInstance().printInfo(" Replicacion: Replicacion finalizada ");

		return est;
	}

	public int getCantArchivosCopiados() {
		return cantArchivosCopiados;
	}

	public int getCantArchivosConError() {
		return cantArchivosConError;
	}

	public List getRutas() {
		ArrayList lista = new ArrayList();
		
		try {
			if(proper != null) {
				String msgOut = (" Replicacion: getRutas retorno ->");
				String value = proper.getString("replication.paths");
				String[] paths = value.split("\\;");
	
				
				for(int i = 0; i < paths.length; i++) {
					if(lista.indexOf(paths[i]) == -1) {
						lista.add(paths[i]);
						msgOut += paths[i]  + ",";
					}
				}
				
				Consola.getInstance().printInfo(msgOut);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Consola.getInstance().printInfo(" Replicacion: Error al intentar capturar las rutas de replicacion ");
		}

		return lista;
	}

	private void fileCopy(File f1, File f2) throws FileNotFoundException, IOException {

		InputStream in = new FileInputStream(f1);

		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while((len = in.read(buf)) > 0) {
			out.write(buf,0,len);
		}
		in.close();
		out.close();

	}

}
