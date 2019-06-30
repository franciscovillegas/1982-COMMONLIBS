package mis.html;

import mis.pila.Pila;

public class HTMLiterator {
	private HTMLnode NodoCabezera=null;
	private HTMLtag tag=null;
	private Pila Encolados=null;
	private Pila EncoladosPasados=null;
	public static HTMLnode valor=null;
	
	public HTMLiterator(HTMLnode NodoCabezera_,HTMLtag tag_) {
		if(NodoCabezera_ == null || tag_ == null) {
			System.out.println("Contructor HTMLiterator, nodo cabezera o Tag es null");
		}
		else {
			NodoCabezera = NodoCabezera_;
			tag = tag_;
			Encolados = new Pila();
			EncoladosPasados = new Pila();
			ReBuscarTag(NodoCabezera);
			this.Siguiente();
		}
	}
	
	public void Siguiente() {
		if(valor != null) {
			EncoladosPasados.apilar(valor);
		}
		
		if(!Encolados.vacia()) {
			valor =(HTMLnode) Encolados.desApilar();
		}
		else {
			valor = null;
		}
	}
	
	public void atras() {
		if(valor != null) {
			Encolados.apilar(valor);
		}
		
		if(!EncoladosPasados.vacia()) {
			valor = (HTMLnode) EncoladosPasados.desApilar();
		}
		else {
			valor= null;
		}
	}
	
	private void ReBuscarTag(HTMLnode nodo) {
		HTMLnode Tmp=null;
		Pila pila = new Pila();
		if(nodo.GetTag().equals(tag) || tag.equals(HTMLtag.COMODIN)) {
			Encolados.apilar(nodo);
		}
		nodo.Hijos.volcar(pila);
		while(!pila.vacia()) {
			Tmp= (HTMLnode)pila.desApilar();
			ReBuscarTag(Tmp);
			nodo.Hijos.apilar(Tmp);
		}
	}
}