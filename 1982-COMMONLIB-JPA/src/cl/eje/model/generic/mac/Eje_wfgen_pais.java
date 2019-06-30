package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_pais", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "id_pais", numerica = true, isForeignKey = false)})
public class Eje_wfgen_pais extends Vo {
	private int id_pais;
	private int id_continente;
	private String nombre;
	private String iso2;
	private String iso3;
	private String cia;
	private String codtelefonico;
	private String codinternet;
	private String icono;
	
	public int getId_pais() {
		return id_pais;
	}
	public void setId_pais(int id_pais) {
		this.id_pais = id_pais;
	}
	public int getId_continente() {
		return id_continente;
	}
	public void setId_continente(int id_continente) {
		this.id_continente = id_continente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIso2() {
		return iso2;
	}
	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}
	public String getIso3() {
		return iso3;
	}
	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}
	public String getCia() {
		return cia;
	}
	public void setCia(String cia) {
		this.cia = cia;
	}
	public String getCodtelefonico() {
		return codtelefonico;
	}
	public void setCodtelefonico(String codtelefonico) {
		this.codtelefonico = codtelefonico;
	}
	public String getCodinternet() {
		return codinternet;
	}
	public void setCodinternet(String codinternet) {
		this.codinternet = codinternet;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	
	
}
