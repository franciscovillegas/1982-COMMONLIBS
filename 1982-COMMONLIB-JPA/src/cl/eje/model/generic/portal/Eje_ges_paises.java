package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "pais", isForeignKey = false, numerica = false) }, tableName = "eje_ges_paises")
public class Eje_ges_paises extends Vo {
	private String pais;
	private String descrip;
	private String nacionalidad;
	private String cod_m4;

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getCod_m4() {
		return cod_m4;
	}

	public void setCod_m4(String cod_m4) {
		this.cod_m4 = cod_m4;
	}

}
