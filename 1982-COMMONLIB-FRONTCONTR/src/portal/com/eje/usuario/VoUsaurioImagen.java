package portal.com.eje.usuario;

import java.io.Serializable;


public class VoUsaurioImagen implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 957584315491353087L;
	
	
	private String nameUnic;
	private String nameOriginal;
	private int idFile;
	
	
	public VoUsaurioImagen() {
		nameUnic = "temporal/imgtrabajadores/sinfoto.jpg";
		nameOriginal = "imagenTrabajador.jpg";
		int idFile = -1;
	}
	
	/**
	 * Retorna el PATH de la imagen, la ubicación relativa el sitio en donde encontrar a la imagen.<br/>
	 * 
	 * */
	public String getNameUnic() {
		return nameUnic;
	}
	
	public void setNameUnic(String nameUnic) {
		this.nameUnic = nameUnic;
	}
	
	public String getNameOriginal() {
		return nameOriginal;
	}
	
	public void setNameOriginal(String nameOriginal) {
		this.nameOriginal = nameOriginal;
	}

	
	public int getIdFile() {
		return idFile;
	}

	
	public void setIdFile(int idFile) {
		this.idFile = idFile;
	}


}
