package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "banco", isForeignKey = false, numerica = false) }, tableName = "eje_ges_banco")
public class Eje_ges_banco extends Vo {
	private String banco;
	private String descrip;
	private String cod_m4;
	
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getCod_m4() {
		return cod_m4;
	}
	public void setCod_m4(String cod_m4) {
		this.cod_m4 = cod_m4;
	}
	
	
	
	
}
