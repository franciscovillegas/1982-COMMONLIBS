package cl.ejedigital.web.datos;

public enum Order {

	Ascendente, Descendente;

	public static Order fromString(String valor) {
		if (valor != null && valor.toLowerCase().indexOf("asc") != -1) {
			return Ascendente;
		} else if (valor != null && valor.toLowerCase().indexOf("desc") != -1) {
			return Descendente;
		} else {
			return Descendente;
		}
	}

}
