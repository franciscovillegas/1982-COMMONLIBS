package portal.com.eje.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Main_formatImg {

	public static void main(String[] args) {
		String dir = "C:\\Users\\Pancho\\Google Drive\\((01-EJEDIGITAL))\\027-Banco Estado\\fotos";
		File f = new File(dir);
		
		Map exts = new HashMap();
		
		List<String> extsImgs = new ArrayList<String>();
		extsImgs.add("jpg");
		extsImgs.add("jpeg");
		extsImgs.add("png");
		extsImgs.add("gif");
		
		if(f.exists()) {
			File[] files = f.listFiles();
			
			for(File file : files) {
				String fName = file.getName();
				
				try {
					String name = fName.substring(0,  fName.lastIndexOf("."));
					String ext = (fName.substring(fName.lastIndexOf(".") + 1, fName.length())).toLowerCase();
					
					exts.put(ext, "true");
					
					if(extsImgs.indexOf(ext) != -1) {
						/*SI ES IMAGEN*/
						
						if(name.indexOf("-") != -1) {
							name = name.substring(0,  fName.lastIndexOf("-"));
						}
						
						File dest = new File(dir + "\\ok\\"+name+"."+ext);
						System.out.println(fName + ">>" + dest.getName());
						
						FileUtils.copyFile(file, dest);
					}
				
				 
					
				} catch (IOException e) {
					System.out.println("Error en el archivo :"+fName);
				}
				
				
			}
		}
		
		Set<String> set = exts.keySet();
		List li = new ArrayList(set);
		
		for(Object o : li) {
			System.out.println(o);
		}
	}

}
