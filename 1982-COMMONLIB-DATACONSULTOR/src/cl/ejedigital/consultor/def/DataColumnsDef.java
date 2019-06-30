package cl.ejedigital.consultor.def;

import java.util.LinkedList;
import java.util.List;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;


public class DataColumnsDef {
	private List<DataColumnDef> lista;
	
	public DataColumnsDef(List<String> nombreColumnas) {
		lista = new LinkedList<DataColumnDef>();
		
		for(String s : nombreColumnas) {
			lista.add(new DataColumnDef(s));
		}
	}

	public void addColumnDef(DataColumnDef def) {
		lista.add(def);
	}
	
	public DataColumnDef getColumnDef(String title) {
		for(DataColumnDef row : lista) {
			if(row.getsTitle().equals(title)) {
				return row;
			}
		}
		
		return null;
	}
	
	public int getColumnDefPos(String title) {
		int pos = 0;
		for(DataColumnDef row : lista) {
			if(row.getsTitle().equals(title)) {
				return pos;
			}
			pos++;
		}
		
		return 0;
	}
	
	public DataColumnDef getColumnDef(int pos) {
		return lista.get(pos);
	}
	
	public IDataOut toJarray() {
		DataList dList = new DataList();
		
		for(DataColumnDef row : lista) {
			DataFields fields = new DataFields();
			fields.put("value",new Field(row.toJson(lista.size())));
			
			dList.add(fields);
		}
		
		JSarrayDataOut out = new JSarrayDataOut(dList);
		return out;
	}

}
