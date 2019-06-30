package portal.com.eje.tools;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;


/**
 *  * @nota Clase que permite agregar valores y al final devuelve un arreglo sql, con los valores sin
 * repetirlos
 * 
 * @ejemplo retorna algo como (2,4,5,6,7,8,9,0,2) ó ('a','b','c','d')
 * @deprecated
 * @since 2016-11-30
 * @author Pancho 
 * 
 * ocupar la clase de cl.ejedigital.tool.strings.ArrayFactory
 * 
 * */

public class ArrayFactory implements Collection<String>{
	private Stack<String> pila;
	
	public ArrayFactory(){
		pila = new Stack<String>();
	}
	
	public void add(int valor){
		if(!pila.contains(String.valueOf(valor))) {
			pila.add(String.valueOf(valor));
		}
	}
	
	public boolean add(String valor){
		pila.add(valor);
		return true;
	}
	
	public String getArrayInteger() {
		return getArraySql(true);
	}
	
	public String getArrayString(){
		return getArraySql(false);
	}
	
	public void vacia() {
		pila.removeAllElements();
	}
	
	
	private String getArraySql(boolean retornaNumeros){
		String array = "";
		String valGet = "";
		for(int i=0;i< pila.size();i++) {
			if(retornaNumeros) {
				valGet = (String)pila.get(i);
			}
			else {
				valGet = "'" + (String)pila.get(i) + "'"; 
			}
			
			array += valGet + ",";
		}
		
		if(array.length() != 0) {
			array = array.substring(0, array.length()-1);
		}
		else {
			if(retornaNumeros) {
				array = "-12345678";
			}
			else {
				array = "'-12345678'";
			}
		}
		
		return "(" + array + ")";
	}
	
	public int getCount() {
		return size();
	}

	public int size() {
		return pila.size();
	}

	public boolean isEmpty() {
		return this.size() == 0;
	}

	public boolean contains(Object o) {
		return pila.contains(o);
	}

	public Iterator<String> iterator() {
		return pila.iterator();
	}

	public Object[] toArray() {
		return pila.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return pila.toArray(a);
	}

	public boolean remove(Object o) {
		return remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return containsAll(pila);
	}

	public boolean addAll(Collection<? extends String> c) {
		return addAll(pila);
	}

	public boolean removeAll(Collection<?> c) {
		return removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return retainAll(pila);
	}

	public void clear() {
		pila.clear();
		
	}
}
