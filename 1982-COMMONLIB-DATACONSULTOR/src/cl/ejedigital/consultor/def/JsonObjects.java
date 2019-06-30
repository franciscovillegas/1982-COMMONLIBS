package cl.ejedigital.consultor.def;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.JSarrayDataOut;

public class JsonObjects extends JSarrayDataOut implements List<JsonObject> {
 	
	public JsonObjects() {
		super((DataList)null);
		
 
		super.data  = new DataList();
	}
	/**
	
	private static final long serialVersionUID = 2722497274020202772L;

	public IDataOut getValues() {
		DataList lista = new DataList();
		DataFields fields = null;
		
		 
		for(Object o : this) {
			fields = new DataFields();
			fields.put("value", ((JsonObject)o).getValues());
			lista.add(fields);
		}
		
		lista.add(fields);	
		
		JSarrayDataOut out = new JSarrayDataOut(lista);		
		return out;
	}
 * 
	 */

	public int size() {
		// TODO Auto-generated method stub
		return this.data.size();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.data.isEmpty();
	}

	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return this.data.contains(o);
	}

	public Iterator<JsonObject> iterator() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return this.data.toArray();
	}

	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return this.data.toArray(a);
	}

	public boolean add(JsonObject e) {
		// TODO Auto-generated method stub
		DataFields fields = new DataFields();
		fields.put("value", e);
		return this.data.add(fields);
 
	}
	
	public JsonObjects add(String e) {
		// TODO Auto-generated method stub
		DataFields fields = new DataFields();
		fields.put("value", e);
		this.data.add(fields);
		return this;
	}
	
	public JsonObjects add(Double e) {
		// TODO Auto-generated method stub
		DataFields fields = new DataFields();
		fields.put("value", e);
		this.data.add(fields);
		return this;
	}
	
	public JsonObjects add(Double e, Double e2) {
		// TODO Auto-generated method stub
		DataFields fields = new DataFields();
		
		JsonObjects object = new JsonObjects();
		object.add(e);
		object.add(e2);
		
		fields.put("value", object);
		this.data.add(fields);
		return this;
	}
	
	public JsonObjects add(Integer e) {
		// TODO Auto-generated method stub
		DataFields fields = new DataFields();
		fields.put("value", e);
		this.data.add(fields);
		return this;
	}

	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return this.data.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.data.containsAll(c);
	}

	public boolean addAll(Collection<? extends JsonObject> c) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public boolean addAll(int index, Collection<? extends JsonObject> c) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.data.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.data.retainAll(c);
	}

	public void clear() {
		this.data.clear();
		
	}

	public JsonObject get(int index) {
		throw new NotImplementedException();
	}

	public JsonObject set(int index, JsonObject element) {
		// TODO Auto-generated method stub
		return new JsonObject(this.data.set(index, element.getDataFields()));
	}

	public void add(int index, JsonObject element) {
		this.data.add(index, element.getDataFields());
		
	}

	public JsonObject remove(int index) {
		// TODO Auto-generated method stub
		return new JsonObject(this.data.remove(index));
	}

	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return this.data.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return this.data.lastIndexOf(o);
	}

	public ListIterator<JsonObject> listIterator() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public ListIterator<JsonObject> listIterator(int index) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public List<JsonObject> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
}
