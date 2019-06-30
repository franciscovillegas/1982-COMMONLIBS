package mis.pila;

import java.io.Serializable;

public class NodoP implements Serializable {
  	 protected Object datos;
     public NodoP siguiente;
     
	public  NodoP (Object valor) { 
	 	datos =valor;
	  	siguiente= null;
	 }
	
	 public Object getElemento() {
		 return datos;
	 }
	 
	 public NodoP getAnt() {
		 return siguiente; 
	 }
}
