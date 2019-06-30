package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idcorr", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_cliente_cadena")
public class Eje_wfgen_cliente_cadena extends Vo {
	private int idcorr;
	private int idcliente;
	private int idcadena;

	public int getIdcorr() {
		return idcorr;
	}

	public void setIdcorr(int idcorr) {
		this.idcorr = idcorr;
	}

	public int getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
	}

	public int getIdcadena() {
		return idcadena;
	}

	public void setIdcadena(int idcadena) {
		this.idcadena = idcadena;
	}

}
