package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_banco_tipocuenta", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "id_tipocuenta", numerica = true, isForeignKey = false)})
public class Eje_wfgen_banco_tipocuenta extends Vo {
	private int id_tipocuenta;
	private String cuenta;
	private String cod;
	
	public int getId_tipocuenta() {
		return id_tipocuenta;
	}
	public void setId_tipocuenta(int id_tipocuenta) {
		this.id_tipocuenta = id_tipocuenta;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	
 
	
	
	
}
