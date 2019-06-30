package cl.ejedigital.tool.strings;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;


/**
 *  * @nota Clase que permite agregar valores y al final devuelve un arreglo sql, con los valores sin
 * repetirlos
 * 
 * @ejemplo retorna algo como (2,4,5,6,7,8,9,0,2) ó ('a','b','c','d')
 * 
 * */

public class ArrayFactory implements Collection {
	private Stack pila;
	
	public ArrayFactory(){
		pila = new Stack();
	}
	
	@Override
	public boolean add(Object valor) {
		if(!pila.contains(valor)) {
			return pila.add(valor);
		}
		
		return false;
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
		StringBuilder array = new StringBuilder();
		
		for(int i=0;i< pila.size();i++) {
			if(!"".equals(array.toString())) {
				array.append(",");
			}
			if(retornaNumeros) {
				array.append(pila.get(i));
			}
			else {
				array.append("'").append(pila.get(i)).append("'");
			}
		}
		
		if(pila.size() == 0) {
			if(retornaNumeros) {
				array.append(-122);
			}
			else {
				array.append("'").append(-122).append("'");
			}
		}
	 
		
		return "(" + array.toString() + ")";
	}
	
	public int getCount() {
		return pila.size();
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

	public Iterator iterator() {
		return pila.iterator();
	}

	public Object[] toArray() {
		return pila.toArray();
	}

	public Object[] toArray(Object[] a) {
		return pila.toArray(a);
	}

	public boolean remove(Object o) {
		return pila.remove(o);
	}

	public boolean containsAll(Collection c) {
		return pila.containsAll(c);
	}

	public boolean addAll(Collection c) {
		return pila.addAll(c);
	}

	public boolean removeAll(Collection c) {
		return pila.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return pila.retainAll(pila);
	}

	public void clear() {
		pila.clear();
		
	}
 
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		if(size() > 0) {
			for(Object o : pila) {
				if(!"".equals(str.toString())){
					str.append(",");
				}
				str.append(String.valueOf(o));
			}
		}
		
		return "{"+str.toString()+"}";
	}

	public static <T>ArrayFactory populateFrom(Collection<T> integers) {
		ArrayFactory af = new ArrayFactory();
		
		if(integers != null) {
			af.addAll(integers);
		}
		
		return af;
	}

}
