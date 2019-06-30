package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "cod", isForeignKey = false, numerica = true) }, tableName = "eje_ges_calendario")
public class Eje_ges_calendario extends Vo {
	private int cod;
	private int codano;
	private int codmes;
	private int coddia;
	private String festivo;
	private int dia;
	private Date fecha;
	private String santo;
	
	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}
	public int getCodano() {
		return codano;
	}
	public void setCodano(int codano) {
		this.codano = codano;
	}
	public int getCodmes() {
		return codmes;
	}
	public void setCodmes(int codmes) {
		this.codmes = codmes;
	}
	public int getCoddia() {
		return coddia;
	}
	public void setCoddia(int coddia) {
		this.coddia = coddia;
	}
	public String getFestivo() {
		return festivo;
	}
	public void setFestivo(String festivo) {
		this.festivo = festivo;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getSanto() {
		return santo;
	}
	public void setSanto(String santo) {
		this.santo = santo;
	}
	
	
	
}
