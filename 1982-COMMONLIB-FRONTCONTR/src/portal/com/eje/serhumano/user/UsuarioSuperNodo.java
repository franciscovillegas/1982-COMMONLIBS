package portal.com.eje.serhumano.user;

import portal.com.eje.serhumano.admin.arbol.Nodo;

public class UsuarioSuperNodo {
	private static Nodo SuperNodo = null;
	
	private UsuarioSuperNodo() {
		
	}
	
	
	public static Nodo getSuperNodo() {
		return SuperNodo;
	}
	
	public static void setSuperNodo(Nodo nodo) {
		SuperNodo = nodo;
	}
}
