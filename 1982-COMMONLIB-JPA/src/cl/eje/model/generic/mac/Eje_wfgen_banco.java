package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_banco", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "id_banco", numerica = true, isForeignKey = false)})
public class Eje_wfgen_banco extends Vo {
	private int id_banco;
	private String banco;
	
	public int getId_banco() {
		return id_banco;
	}
	public void setId_banco(int id_banco) {
		this.id_banco = id_banco;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	
	

}
