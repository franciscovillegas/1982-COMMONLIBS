package portal.com.eje.frontcontroller.ioutils;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.IFieldManipulator;
import cl.ejedigital.consultor.ManipulatorSqlServer2000;
import cl.ejedigital.consultor.output.ReservedWord;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;

public class IOUtilCsv extends IOUtil {

	public static IOUtilCsv getIntance() {
		return Weak.getInstance(IOUtilCsv.class);
	}
	
	public boolean retCSV(IIOClaseWebLight io, ConsultaData data, String fileName) {

		IFieldManipulator fManipulator = new ManipulatorSqlServer2000();
		
		StringBuilder strOut = new StringBuilder();
		
		List<String> nombres = data.getNombreColumnas();
		int x=0;
		for(String nombre : nombres) {
			if (x!=0) {
				strOut.append(";");
			}
			strOut.append(nombre);
			x++;
		}
		strOut.append("\n");
		
		DataList lista = data.getData();
		for( DataFields f : lista) {
			
			Set<String> set = f.keySet();
			int i = 0;
			for(String key : set) {
				if (i!=0) {
					strOut.append(";");
				}
				
				String strVal = null;
				if( f.get(key).getObject() instanceof ReservedWord) {
					strVal = ((ReservedWord)f.get(key).getObject()).toString();
				}
				else {
					strVal = fManipulator.getForcedString(f.get(key));
				}
				
				if(strVal == null || strVal.trim().length() == 0) {
					strVal = "";
				}
				
				strOut.append(strVal);
				i++;
			}
			strOut.append("\n");
		}
		
		return retCSV(io, strOut, fileName);
		 
	}
	
	private boolean retCSV(IIOClaseWebLight io, StringBuilder strOut, String fileName) {

    	boolean ok = false;
    	
    	io.getResp().setContentType("text/csv");
    	io.getResp().setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        try
        {
            OutputStream outputStream = io.getResp().getOutputStream();
            outputStream.write(strOut.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            
            ok = true;
            
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

    	return ok;
    }
}
