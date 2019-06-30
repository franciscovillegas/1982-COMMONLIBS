package cl.ejedigital.tool.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


/**
 * Clase singleton que permite manipular de forma sencilla los objetos FILE. 
 * Se asegura que los métodos puedan ser utilzados tanto en windows como en Linux
 * 
 * @see java.io.File
 * */
public class MyFile {
	private static MyFile instance;
	
	private MyFile() {
		
	}
	
	public static MyFile getInstance() {
		if(instance == null) {
			synchronized (MyFile.class) {
				if(instance == null) {
					instance = new MyFile();
				}
			}
		}
		
		return instance;
	}
	
	public boolean writeFile(File fileSave, String txt) {
		InputStream in=null;
		try {
			in = IOUtils.toInputStream(txt, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writeFile(fileSave, in);
	}
	
	public boolean writeFile(File fileSave, InputStream in) {
		boolean ok = false;
		OutputStream out= null;
		
		try {
			File dirs = new File(fileSave.getParent());
			
			if(!dirs.exists()) {
				dirs.mkdirs();
			}
			
			if(dirs.exists()) {
				out = new FileOutputStream(fileSave);

				byte buf[] = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				
				ok = true;
			}
			else {
				ok = false;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (SecurityException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if( in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ok;
	}

	/**
	 * Se debe pasar el charset
	 * @deprecated
	 * 
	 * */
	public StringBuilder readFile(File file) throws IOException {
		return readFile(file, "UTF-8");
	}
	
	public StringBuilder readFile(File file, String charSet) throws IOException {
		FileInputStream in = new FileInputStream(file);
		InputStreamReader r = new InputStreamReader(in, charSet);
		String text = IOUtils.toString(r);

		return new StringBuilder(text);
	}

	/**
	 * Retorna el nombre de un archivo sin la extensión, si no tiene retorna le mismo name
	 *
	 * @author Pancho
	 * @since 23-05-2028
	 * */
	public String getFileWithOutExt(String xmlName) {
		if(xmlName != null) {
			if(xmlName.indexOf(".") != -1) {
				return xmlName.substring(0, xmlName.lastIndexOf("."));
			}
		}
		return xmlName;
	}
	
	/**
	 * Retorna el nombre de un archivo sin la extensión, si no tiene retorna null
	 *
	 * @author Pancho
	 * @since 23-05-2028
	 * */
	public String getFileWithOutExt(File file) {
		if(file != null && !file.isDirectory()) {
			 
			if(file.getName().indexOf(".") != -1) {
				return file.getName().substring(0, file.getName().lastIndexOf("."));
			}
			else {
				return file.getName();
			}
 
		}
		return null;
	}
	
	public String getExt(File file) {
		if(file != null && !file.isDirectory()) {
			 
			if(file.getName().indexOf(".") != -1) {
				return file.getName().substring(file.getName().lastIndexOf("."),file.getName().length());
			}
 
		}
		return null;
	}
	
	public boolean delete(List<File> files) {
		if(files != null) {
			for(File f : files) {
				f.delete();
			}
		}
		
		return true;
	}
	
	public boolean deleteOnExit(List<File> files) {
		if(files != null) {
			for(File f : files) {
				f.deleteOnExit();
			}
		}
		
		return true;
	}
	
	public boolean forceDeleteOnExit(List<File> files) {
		if(files != null) {
			for(File f : files) {
				try {
					FileUtils.forceDeleteOnExit(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public File changeFolder(File origin,File pathOrigin, File pathDest) {
		try {
			String canOrigin = origin.getCanonicalPath().replace(File.separator, ";");
			String canPathOrigin = pathOrigin.getCanonicalPath().replace(File.separator, ";");
			String canPathDest = pathDest.getCanonicalPath().replace(File.separator, ";");
			
			String finalString = canOrigin.replaceAll(canPathOrigin, canPathDest);
			;
			return new File(finalString.replace(";", File.separator));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
