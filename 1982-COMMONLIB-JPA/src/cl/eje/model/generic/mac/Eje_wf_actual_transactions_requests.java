package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { 
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_request", isForeignKey = false, numerica = true) }, 
		tableName = "eje_wf_actual_transactions_requests")
@TableReferences(value = { @TableReference(field = "actual_transaccion_id", 
							fk = @ForeignKeyReference(fk = "actual_transaccion_id", otherTableField = "actual_transaccion_id"), voClass = Eje_wf_actual_transactions.class) })
public class Eje_wf_actual_transactions_requests extends Vo {
	private int	id_corr_request;
	private int	actual_transaccion_id;
	private String param;
	private String value;
	
	public int getId_corr_request() {
		return id_corr_request;
	}
	public void setId_corr_request(int id_corr_request) {
		this.id_corr_request = id_corr_request;
	}
	public int getActual_transaccion_id() {
		return actual_transaccion_id;
	}
	public void setActual_transaccion_id(int actual_transaccion_id) {
		this.actual_transaccion_id = actual_transaccion_id;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	

}
