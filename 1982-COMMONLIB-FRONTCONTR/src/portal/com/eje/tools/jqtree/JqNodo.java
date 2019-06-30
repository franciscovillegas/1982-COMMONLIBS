package portal.com.eje.tools.jqtree;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.tools.escapeJavascript;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;

public class JqNodo implements IJqContent {
	private String nombre;
	private List<JqNodo> jqHijos;
	private String idNodo;
	
	public JqNodo(String nombre, String idNodo) {
		jqHijos = new ArrayList<JqNodo>();
		this.idNodo = idNodo;
		this.nombre = nombre;
	}
	
	public JqNodo(String nombre) {
		jqHijos = new ArrayList<JqNodo>();
		this.idNodo = String.valueOf(Math.random());
		this.nombre = nombre;
	}

	public String getIdNodo() {
		return idNodo;
	}

	public void addHijo(JqNodo n) {
		jqHijos.add(n);	
	}
	
	public String getNombre() {
		return nombre;
	}



	public String toString() {
		StringBuilder str = new StringBuilder();
		return str.append(nombre).append("[key:").append(idNodo).append("] [hijos:").append(jqHijos.size())
				.append("]").toString();
	}

	public IDataOut getJqValue() {

		DataList lista = new DataList();
		DataFields data = new DataFields();
		
		data.put("label", new Field(nombre));
		data.put("id", new Field(idNodo));
		
		if (jqHijos.size() > 0) {
			DataList listaHijos = new DataList();
			
			for(JqNodo nodo : jqHijos) {
				DataFields dataHijos = new DataFields();
				dataHijos.put("hijos", new Field(nodo.getJqValue()));
				listaHijos.add(dataHijos);
			}

			JSarrayDataOut outArray = new JSarrayDataOut(listaHijos);
			outArray.setEscape(new escapeJavascript());
			data.put("children", new Field(outArray));
		}

		lista.add(data);
		JSonDataOut out = new JSonDataOut(lista);
		out.setEscape(new escapeJavascript());
		return out;
	}

}
