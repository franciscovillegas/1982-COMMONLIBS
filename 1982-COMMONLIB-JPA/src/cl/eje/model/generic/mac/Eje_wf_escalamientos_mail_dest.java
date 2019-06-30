package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_dest", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_mail_dest")
public class Eje_wf_escalamientos_mail_dest extends Vo {
	private int id_escalamiento_dest;
	private int id_escalamiento_mail;
	private int rut;
	private String nombre;
	private String mail;
	private String e_mail;
	private int send_type;
	public int getId_escalamiento_dest() {
		return id_escalamiento_dest;
	}
	public void setId_escalamiento_dest(int id_escalamiento_dest) {
		this.id_escalamiento_dest = id_escalamiento_dest;
	}
	public int getId_escalamiento_mail() {
		return id_escalamiento_mail;
	}
	public void setId_escalamiento_mail(int id_escalamiento_mail) {
		this.id_escalamiento_mail = id_escalamiento_mail;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public int getSend_type() {
		return send_type;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	} 
	
	
	
}
