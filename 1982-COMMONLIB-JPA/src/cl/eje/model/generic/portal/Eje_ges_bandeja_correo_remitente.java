package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "rut_remitente", isForeignKey = true, numerica = true) }, tableName = "eje_ges_bandeja_correo_remitente")
public class Eje_ges_bandeja_correo_remitente extends Vo {
	private int	rut_remitente;
	private String nombres;
	private String ape_paterno;
	private String ape_materno;
	private String foto_original;
	private String foto_pequena;
	
	public int getRut_remitente() {
		return rut_remitente;
	}
	public void setRut_remitente(int rut_remitente) {
		this.rut_remitente = rut_remitente;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApe_paterno() {
		return ape_paterno;
	}
	public void setApe_paterno(String ape_paterno) {
		this.ape_paterno = ape_paterno;
	}
	public String getApe_materno() {
		return ape_materno;
	}
	public void setApe_materno(String ape_materno) {
		this.ape_materno = ape_materno;
	}
	public String getFoto_original() {
		return foto_original;
	}
	public void setFoto_original(String foto_original) {
		this.foto_original = foto_original;
	}
	public String getFoto_pequena() {
		return foto_pequena;
	}
	public void setFoto_pequena(String foto_pequena) {
		this.foto_pequena = foto_pequena;
	}
	
	
	
	
}
