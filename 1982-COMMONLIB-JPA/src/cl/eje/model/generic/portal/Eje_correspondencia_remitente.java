package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_remitente", isForeignKey = false, numerica = true) }, tableName = "eje_correspondencia_remitente")
public class Eje_correspondencia_remitente extends Vo {
	private int id_remitente;
	private String nombre;
	private String contexto;
	private int id_modulo;
	private String mail;

	public int getId_remitente() {
		return id_remitente;
	}

	public void setId_remitente(int id_remitente) {
		this.id_remitente = id_remitente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public int getId_modulo() {
		return id_modulo;
	}

	public void setId_modulo(int id_modulo) {
		this.id_modulo = id_modulo;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
