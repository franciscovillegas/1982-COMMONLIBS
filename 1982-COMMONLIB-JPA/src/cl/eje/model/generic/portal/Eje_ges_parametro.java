package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", tableName = "eje_ges_parametro", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "cod_param", numerica = true, isForeignKey = false)})
public class Eje_ges_parametro extends Vo {
	private int	cod_param;
	private int	peri_id;
	private String tipo;
	private String descrip;
	private String vigente;
	private String codext1;
	public int getCod_param() {
		return cod_param;
	}
	public void setCod_param(int cod_param) {
		this.cod_param = cod_param;
	}
	public int getPeri_id() {
		return peri_id;
	}
	public void setPeri_id(int peri_id) {
		this.peri_id = peri_id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getVigente() {
		return vigente;
	}
	public void setVigente(String vigente) {
		this.vigente = vigente;
	}
	public String getCodext1() {
		return codext1;
	}
	public void setCodext1(String codext1) {
		this.codext1 = codext1;
	}
	
	
	
	
}
