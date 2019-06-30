package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_mail", numerica = true, isForeignKey = false) }, tableName = "eje_wf_tupla_mail")
public class Eje_wf_tupla_mail extends Vo {
	private int id_mail;
	private int id_producto;
	private int id_evento;
	private int id_suceso;
	private String nombre;
	private int send_to_mail;
	private int send_to_email;
	private String contenido;
	
	public int getId_mail() {
		return id_mail;
	}
	public void setId_mail(int id_mail) {
		this.id_mail = id_mail;
	}
	public int getId_producto() {
		return id_producto;
	}
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}
	public int getId_evento() {
		return id_evento;
	}
	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}
	public int getId_suceso() {
		return id_suceso;
	}
	public void setId_suceso(int id_suceso) {
		this.id_suceso = id_suceso;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getSend_to_mail() {
		return send_to_mail;
	}
	public void setSend_to_mail(int send_to_mail) {
		this.send_to_mail = send_to_mail;
	}
	public int getSend_to_email() {
		return send_to_email;
	}
	public void setSend_to_email(int send_to_email) {
		this.send_to_email = send_to_email;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	 
	
	
}
