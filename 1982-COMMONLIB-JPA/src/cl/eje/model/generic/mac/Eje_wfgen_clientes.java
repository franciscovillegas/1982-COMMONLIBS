package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_cliente", numerica = false, isForeignKey = false) }, tableName = "eje_wfgen_clientes")
public class Eje_wfgen_clientes extends Vo {
	private int	id_cliente;
	private String cliente;
	private int vigente;
	
	public int getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public int getVigente() {
		return vigente;
	}
	public void setVigente(int vigente) {
		this.vigente = vigente;
	}
	
	
}
