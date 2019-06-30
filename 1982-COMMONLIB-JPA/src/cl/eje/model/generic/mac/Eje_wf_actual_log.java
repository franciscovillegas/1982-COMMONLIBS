package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "actual_log_id", isForeignKey = false, numerica = true) }, tableName = "eje_wf_actual_log")
public class Eje_wf_actual_log extends Vo {
	private int actual_log_id;
	private int level_int;
	private Date fecha_log;
	private int id_req;
	private int rut_session;
	private String proc_responsable;
	private String log_str;
	public int getActual_log_id() {
		return actual_log_id;
	}
	public void setActual_log_id(int actual_log_id) {
		this.actual_log_id = actual_log_id;
	}
	public int getLevel_int() {
		return level_int;
	}
	public void setLevel_int(int level_int) {
		this.level_int = level_int;
	}
	public Date getFecha_log() {
		return fecha_log;
	}
	public void setFecha_log(Date fecha_log) {
		this.fecha_log = fecha_log;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public int getRut_session() {
		return rut_session;
	}
	public void setRut_session(int rut_session) {
		this.rut_session = rut_session;
	}
	public String getProc_responsable() {
		return proc_responsable;
	}
	public void setProc_responsable(String proc_responsable) {
		this.proc_responsable = proc_responsable;
	}
	public String getLog_str() {
		return log_str;
	}
	public void setLog_str(String log_str) {
		this.log_str = log_str;
	}
	 
	
	

}
