package cl.ejedigital.tool.correo.vo;

import java.sql.Date;

import javax.mail.Message.RecipientType;



public class VoDestinatario implements IVoDestinatario {
	private String nombre;
	private String email;
	private RecipientType tipo;
	private Integer rut;
	
	/**
	 * @deprecated
	 * */
	public VoDestinatario() {
		
	}
	
	/**
	 * @deprecated
	 * @see cl.ejedigital.tool.correo.vo.VoDestinatarioPortal
	 * No se debe usar ya que el rut no es obligatorio en esta clase
	 * */
	public VoDestinatario(String nombre, String email, RecipientType tipo) {
		this(null,nombre, email, tipo);
	}
	
	public VoDestinatario(Integer rut, String nombre, String email, RecipientType tipo) {
		super();
		this.rut = rut;
		this.nombre = nombre;
		this.email = email;
		this.tipo = tipo;
	}

	
	public String getNombre() {
		if(nombre !=null ) {
			return nombre.trim();
		}
		
		return null;
	}
	
	public String getEmail() {
		if(email !=null ) {
			return email.trim();
		}
		
		return null;
	}
	
	public RecipientType getTipo() {
		return tipo;
	}

	public Integer getRut() {
		return this.rut;
	}
	
	public String toString() {
		return "[" + String.valueOf(tipo) + "]:"+ getNombre() + "<" + getEmail() + ">";
	}

	public boolean isValidMail() {
			
		if(this.email == null) {
			return false;
		}
		else if(this.email.lastIndexOf("@") != this.email.indexOf("@")) {
			return false;
		}
		else if( this.email.indexOf("@") == -1) {
			return false;
		}
		else {
			return true;
		}
		 
	}

	public Date getFechaActualizacion() {
		// TODO Auto-generated method stub
		return null;
	}
	 
}
