package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idcorr", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_cadena_local")
public class Eje_wfgen_cadena_local extends Vo {
	private int idcorr;
	private int idcadena;
	private int idlocal;

	public int getIdcorr() {
		return idcorr;
	}

	public void setIdcorr(int idcorr) {
		this.idcorr = idcorr;
	}

	public int getIdcadena() {
		return idcadena;
	}

	public void setIdcadena(int idcadena) {
		this.idcadena = idcadena;
	}

	public int getIdlocal() {
		return idlocal;
	}

	public void setIdlocal(int idlocal) {
		this.idlocal = idlocal;
	}

}
