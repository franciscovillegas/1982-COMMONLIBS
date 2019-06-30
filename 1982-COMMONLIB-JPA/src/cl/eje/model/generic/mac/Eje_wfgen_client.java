package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = false, field = "id", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_client")
public class Eje_wfgen_client extends Vo {
	private int id;
	private String name;
	private String description;
	private String versionappmovil;
	private String urlapkappmovil;
	private String unic_cod_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersionappmovil() {
		return versionappmovil;
	}

	public void setVersionappmovil(String versionappmovil) {
		this.versionappmovil = versionappmovil;
	}

	public String getUrlapkappmovil() {
		return urlapkappmovil;
	}

	public void setUrlapkappmovil(String urlapkappmovil) {
		this.urlapkappmovil = urlapkappmovil;
	}

	public String getUnic_cod_name() {
		return unic_cod_name;
	}

	public void setUnic_cod_name(String unic_cod_name) {
		this.unic_cod_name = unic_cod_name;
	}

}
