package portal.com.eje.portal.zip;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import cl.ejedigital.consultor.MyString;
import cl.ejedigital.tool.misc.MyFile;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import portal.com.eje.portal.factory.Util;
 

public class ZipTool {

	public static ZipTool getInstance() {
		return Util.getInstance(ZipTool.class);
	}

	/**
	 * Zipea una lista de archivos y retorna el file generado
	 * 
	 * @author Pancho
	 * @since 25-06-2018
	 * */
	public File zipFiles(List<File> filesToZip, File compressedFile, boolean deleteIfExistCompressedFile) throws IOException {
		List<File> files = zipFiles(filesToZip, compressedFile, deleteIfExistCompressedFile, null);
		
		if(files != null && files.size() == 1) {
			return files.get(0);
		}
		
		return null;
	}
	
	/**
	 * Comprime una lista de archivos en uno o varios zips dependiendo del máximo de MG
	 * @author Pancho
	 * @since 25-06-2018
	 * */
	public List<File> zipFiles(List<File> filesToZip, File compressedFile, boolean deleteIfExistCompressedFile, Integer maxMG) throws IOException {
		List<File> volumenes = new ArrayList<File>();
		
		if (filesToZip != null && compressedFile != null && filesToZip.size() > 0) {
			if (compressedFile.isDirectory()) {
				throw new IOException("Debe ser un archivo y no un directorio.");
			}
			

			compressedFile = new File(compressedFile .getParent() + File.separator + MyFile.getInstance().getFileWithOutExt(compressedFile.getName()));
		
			
			if (compressedFile.getName().indexOf(".zip") == -1) {
				compressedFile = new File(compressedFile.getCanonicalPath() + ".zip");
			}

			if (!compressedFile.getParentFile().exists()) {
				compressedFile.mkdirs();
			}

			
			
			
			try {
				if(deleteIfExistCompressedFile && compressedFile.exists()){
					compressedFile.delete();
				}
				
			 
				String nombreSinExt = MyFile.getInstance().getFileWithOutExt(compressedFile);
					
				if(maxMG != null) {
					long maxSize = (maxMG * 1024 * 1024);
					
					for(int i = 0 ; i< filesToZip.size() ;i++){
						List<File> filesToZipp = new ArrayList<File>();
						filesToZipp.add(filesToZip.get(i));
						
						compress(compressedFile, filesToZipp);						
					 						
						if(!volumenes.contains(compressedFile)) {
							volumenes.add(compressedFile);	
						}
						
						 
						boolean excededSize = compressedFile.length() > maxSize;
						
						if( excededSize ) {
 
							compressedFile = new File( compressedFile.getParentFile() + File.separator + nombreSinExt + "(" + volumenes.size() + ").zip" );
						}
					}
					
					List<File> volumenesCopia = new ArrayList<File>();
					int pos = 1;
					for(File f : volumenes) {
						File newFile = new File(f.getParentFile().getCanonicalPath() + File.separator + nombreSinExt + "("+(pos++)+" de "+volumenes.size()+").zip" );
						f.renameTo(newFile);
						volumenesCopia.add(newFile);
					}
					
					volumenes = volumenesCopia;
				}
				else {
					compress(compressedFile, filesToZip);
					
					volumenes.add(compressedFile);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return volumenes;
	}
	
	public File compress(File compressedFile, List<File> filesToZip) throws ZipException {
		ZipFile zipFile = new ZipFile(compressedFile);
		ZipParameters parameters = new ZipParameters();

		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

		ArrayList<File> listaDeArchivos = new ArrayList<File>();
		listaDeArchivos.addAll(filesToZip);
		
		zipFile.addFiles(listaDeArchivos, parameters);
		
		return compressedFile;
	}
	
	public List<File> compressDirectory(File compressedFile, File directory, int maxMG) throws ZipException {
		if(directory == null || !directory.isDirectory()) {
			throw new ZipException("No es un directorio legible :"+directory);
		}
		
		ZipFiles zip = new ZipFiles(maxMG);
		zip.zipDirectory(directory, compressedFile);
		
		
		return zip.getVolumenes();
	}
	
 
	private class ZipFiles {
	    
	    List<String> filesListInDir;
	    List<File> filesZipped;
	    private long fileMaxSize;
	    private MyString myString;
	    
	    
	    ZipFiles(int maxMG) {
	    	this.filesListInDir = new ArrayList<String>();
	    	this.filesZipped = new ArrayList<File>();
	    	this.fileMaxSize = maxMG * 1024 * 1024;
	    	myString = Util.getInstance(MyString.class);
	    }
	    
	    /**
	     * This method zips the directory
	     * @param dir
	     * @param zipDirName
	     */
	    public void zipDirectory(File dir, File zipFile) {
	        try {
	            populateFilesList(dir);
	            String originalName = MyFile.getInstance().getFileWithOutExt(zipFile);
	            //now zip files one by one
	            //create ZipOutputStream to write to the zip file
	            FileOutputStream fos = new FileOutputStream(zipFile);
	            ZipOutputStream zos = new ZipOutputStream(fos);

	            for(String filePath : filesListInDir){
	            	//File file = new File(filePath);
	            	
	            	if(zipFile.length() > this.fileMaxSize) {
	            		zipFile = new File(zipFile.getParent() + File.separator + originalName + "("+filesZipped.size()+").zip");
	            		zos.close();
	    	            fos.close();
	            		fos = new FileOutputStream(zipFile);
	     	            zos = new ZipOutputStream(fos);
	            	}
	            	
	            	if(!filesZipped.contains(zipFile)) {
	            		filesZipped.add(zipFile);
	            	}
 
	                 //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
	                 ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
	                 zos.putNextEntry(ze);
	                 //read the file and write to ZipOutputStream
	                 FileInputStream fis = new FileInputStream(filePath);
	                 byte[] buffer = new byte[1024];
	                 int len;
	                 while ((len = fis.read(buffer)) > 0) {
	                     zos.write(buffer, 0, len);
	                 }
	                 zos.closeEntry();
	                 fis.close();
	            }
	            
	            zos.close();
	            fos.close();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	     
	    
	    /**
	     * This method populates all the files in a directory to a List
	     * @param dir
	     * @throws IOException
	     */
	    private void populateFilesList(File dir) throws IOException {
	        File[] files = dir.listFiles();
	        for(File file : files){
	            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
	            else populateFilesList(file);
	        }
	    }

	    /**
	     * This method compresses the single file to zip format
	     * @param file
	     * @param zipFileName
	     */
	    public void zipSingleFile(File file, String zipFileName) {
	        try {
	            //create ZipOutputStream to write to the zip file
	            FileOutputStream fos = new FileOutputStream(zipFileName);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	            //add a new Zip Entry to the ZipOutputStream
	            ZipEntry ze = new ZipEntry(file.getName());
	            zos.putNextEntry(ze);
	            //read the file and write to ZipOutputStream
	            FileInputStream fis = new FileInputStream(file);
	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = fis.read(buffer)) > 0) {
	                zos.write(buffer, 0, len);
	            }
	            
	            //Close the zip entry to write to zip file
	            zos.closeEntry();
	            //Close resources
	            zos.close();
	            fis.close();
	            fos.close();
	            System.out.println(file.getCanonicalPath()+" is zipped to "+zipFileName);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }
	    
	    public List<File> getVolumenes() {
	    	return this.filesZipped;
	    }

	}

	public String compressToString(String str) {
		Base64Encoder encoder = new Base64Encoder();
		
		return encoder.encode(compress(str));
	}
	public byte[] compress(String str) {
		if ((str == null) || (str.length() == 0)) {
			return null;
		}
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(obj);
			gzip.write(str.getBytes("UTF-8"));
			gzip.flush();
			gzip.close();
			return obj.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String deCompress(final byte[] compressed) {
		final StringBuilder outStr = new StringBuilder();
		if ((compressed == null) || (compressed.length == 0)) {
			return "";
		}

		try {
			if (isCompressed(compressed)) {
				GZIPInputStream gis;
				gis = new GZIPInputStream(new ByteArrayInputStream(compressed));

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					outStr.append(line);
				}
			} else {
				outStr.append(compressed);
			}
			return outStr.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
	
	public String deCompress(String string) {
		Base64Encoder deco = new Base64Encoder();
		byte[] bytes;
		bytes = deco.decode(string);
		return deCompress(bytes);
 
	}
	
	public boolean isCompressed(final byte[] compressed) {
		return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
				&& (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
	}
}
