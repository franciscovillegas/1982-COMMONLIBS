package portal.com.eje.tools;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.web.fileupload.FileService;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class UploadFile extends AbsClaseWeb {

	public UploadFile(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		Map<String, List<File>> files = super.getIoClaseWeb().getMapFiles();
		DataList listaIds = new DataList();
		
		if(files != null) {
			FileService fs = new FileService(super.getIoClaseWeb().getServletContext());
			
			Set<String> set = files.keySet();
			for(String key : set) {
				List<File> lista = files.get(key);
				DataFields fields = new DataFields();
				int pos = 0;
				
				for(File f : lista) {
					int id = fs.addFile(super.getIoClaseWeb().getUsuario().getRutIdInt(),f,EjeFileUnicoTipo.DESCONOCIDO);
					
					
					if( pos == 0) {
						fields.put(key, new Field(id));	
					}
					else {
						fields.put(key + "_" + pos, new Field(id));
					}
					pos++;
				}
			}
		}
		
		super.getIoClaseWeb().retJson(listaIds);
	}

	

}
