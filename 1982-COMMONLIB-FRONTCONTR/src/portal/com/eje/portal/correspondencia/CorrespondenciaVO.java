package portal.com.eje.portal.correspondencia;

import java.util.Date;

import javax.mail.Message.RecipientType;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;

public class CorrespondenciaVO implements IVoDestinatario {
	private int rut;
	private String mail;
	private String nombre;
	private Date fecUpdate;

	public CorrespondenciaVO(int rut,String nombre, String mail) {
		this(rut, nombre, mail, null);
	}
	
	public CorrespondenciaVO(int rut,String nombre, String mail,  Date fecUpdate) {
		super();
		this.rut = rut;
		this.mail = mail;
		this.nombre = nombre;
		this.fecUpdate = fecUpdate;
	}

	@Override
	public Integer getRut() {
		return this.rut;
	}

	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public String getEmail() {
		return this.mail;
	}

	@Override
	public RecipientType getTipo() {
		return RecipientType.TO;
	}

 
	public boolean isValidMail() {
		return true;
	}

 
	public java.sql.Date getFechaActualizacion() {
		// TODO Auto-generated method stub
		return null;
	}
 

}
