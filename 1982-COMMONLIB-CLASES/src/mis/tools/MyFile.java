package mis.tools;

import java.io.File;

/**
 * No se debe utilizar, debido a que su  función no tiene nada que ver con el nombre del archivo.
 * 
 * @deprecated
 * @see cl.ejedigital.tool.misc.MyFile
 * */
public class MyFile extends File {
	private boolean esLinux = false;
	
	/**
	 * No se debe utilizar, debido a que su  función no tiene nada que ver con el nombre del archivo.
	 * 
	 * @deprecated
	 * @see cl.ejedigital.tool.misc.MyFile
	 * */
	
	public MyFile(String pathname) {
		super(pathname);
		
		if(System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
			esLinux = true;
		}

	}

	public File getAbsoluteFile() {
		 String str = super.getAbsoluteFile().toString();
		if(esLinux) {
			return new File(str.replace('\\', '/'));
		} else {
			return new File(str.replace('/','\\'));
		}
		
	}
	
}
