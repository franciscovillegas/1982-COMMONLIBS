package portal.com.eje.tools.jqtree;

import java.util.ArrayList;

import portal.com.eje.tools.escapeJavascript;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;

public class JqNodos extends ArrayList<JqNodo> implements IJqContent {
	
	public IDataOut getJqValue() {
		DataList lista = new DataList();
		
		synchronized (this) {
			for(JqNodo nodo : this) {
				DataFields data = new DataFields();
				data.put("value",new Field(nodo.getJqValue()));
				lista.add(data);
			}
		}
		
		JSarrayDataOut out = new JSarrayDataOut(lista);
		out.setEscape(new escapeJavascript());
		return out;
		
	}
}
