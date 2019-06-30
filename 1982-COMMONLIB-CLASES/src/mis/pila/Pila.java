package mis.pila;

import java.io.Serializable;

public class Pila implements Serializable {
	private int CantObjetos;
    public NodoP tope;   
    
	public Pila() {
		tope=null;
		CantObjetos=0;
	}
	
	public boolean vacia () {
	 	return tope == null;
	}
	
	public void apilar(Object dato) {
		     NodoP p=new NodoP(dato);
		     p.datos=dato;
		     p.siguiente=tope; 
		     tope=p;
		     CantObjetos++;
	}
	
    public Object desApilar() {
    	     Object dat;   
    	     NodoP p=tope;
    	     dat=p.datos;  
		     tope=tope.siguiente;
		     p=null;
		     CantObjetos--;
		     return dat;
    }	 
    
	public void eliminar() {
		while(tope!=null) {
			desApilar();
		}
	}	   
    
	public void imprimir() {
	 	if (vacia()) {
			System.out.println("La pila esta vacia...");
		}
		else {
			NodoP Actual=tope;
			do { 
				System.out.print(Actual.getElemento().toString()+" ");	
				Actual=Actual.siguiente;
			}while(Actual != null);
			System.out.println();//imprime un salto de linea
		}
	 } 		   
    
    public void volcar(Pila PilaDest) {
    	Object Obj;
    	while (!this.vacia()) {
    		Obj = this.desApilar();
    		PilaDest.apilar(Obj);
    	}
    }
    
    public int Count() {
    	return CantObjetos;
    }

	public static void main(String[]args) {
		Pila p=new Pila();
		p.apilar("1");
		p.apilar("2");
		p.apilar("3");
		p.apilar("4");
		p.imprimir();
		p.desApilar();
		p.imprimir();
    }
}		