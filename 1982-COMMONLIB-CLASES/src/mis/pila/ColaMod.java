package mis.pila;

public class ColaMod {   
	public Pila pilaCola;
    private Pila pilaCola2;
    
	public ColaMod() {
		pilaCola=new Pila();
		pilaCola2=new Pila();
	}
	
	public void insertar(Object dato) {
  	   pilaCola.eliminar();		//se reinica la pila,para q no guarde los valores del anterior orden
  	   pilaCola2.apilar(dato);
       NodoP actual=pilaCola2.tope; 
       while(actual!=null) {	//se invierte la pila 	
    	   pilaCola.apilar(actual.getElemento());
    	   actual=actual.siguiente;
       }
	}
  
	public void sacar(){
  	  	 pilaCola.desApilar();
	}
  
	public void imprimir(){
  	  	 pilaCola.imprimir();
	}
 
	public static void main (String [] args) {	
	  	ColaMod p=new ColaMod();
		p.insertar("1");
		p.insertar("2");
		p.insertar("3");
		p.insertar("4");
		p.imprimir();
		p.sacar();
		p.imprimir();
  }
}