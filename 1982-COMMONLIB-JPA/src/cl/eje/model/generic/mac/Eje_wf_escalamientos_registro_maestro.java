package cl.eje.model.generic.mac;

import java.util.Date;

import cl.eje.model.generic.portal.Eje_ges_trabajador;
import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_registro_mastro", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_registro_maestro")
@TableReferences({
	@TableReference(field = "vo_rut_despues_escalamiento", fk = @ForeignKeyReference(fk = "rut_despues_escalamiento", otherTableField = "rut"), voClass = Eje_ges_trabajador.class)
})
public class Eje_wf_escalamientos_registro_maestro extends Vo {
	private Eje_ges_trabajador vo_rut_despues_escalamiento;
	
	private int id_registro_mastro;
	private int id_escalamiento;
	private int id_req;
	private Date fecha_rol;
	private int id_rol;
	private int rut;
	private int rut_despues_escalamiento;
	private boolean ult_reg_req;
	private int escalamiento_numero;
	private String accion;
	private boolean con_error;
	
	
	public Eje_ges_trabajador getVo_rut_despues_escalamiento() {
		return vo_rut_despues_escalamiento;
	}
	public void setVo_rut_despues_escalamiento(Eje_ges_trabajador vo_rut_despues_escalamiento) {
		this.vo_rut_despues_escalamiento = vo_rut_despues_escalamiento;
	}
	
	public int getEscalamiento_numero() {
		return escalamiento_numero;
	}
	public void setEscalamiento_numero(int escalamiento_numero) {
		this.escalamiento_numero = escalamiento_numero;
	}
	public int getId_registro_mastro() {
		return id_registro_mastro;
	}
	public void setId_registro_mastro(int id_registro_mastro) {
		this.id_registro_mastro = id_registro_mastro;
	}
	public int getId_escalamiento() {
		return id_escalamiento;
	}
	public void setId_escalamiento(int id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public Date getFecha_rol() {
		return fecha_rol;
	}
	public void setFecha_rol(Date fecha_rol) {
		this.fecha_rol = fecha_rol;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public int getRut_despues_escalamiento() {
		return rut_despues_escalamiento;
	}
	public void setRut_despues_escalamiento(int rut_despues_escalamiento) {
		this.rut_despues_escalamiento = rut_despues_escalamiento;
	}
	public boolean isUlt_reg_req() {
		return ult_reg_req;
	}
	public void setUlt_reg_req(boolean ult_reg_req) {
		this.ult_reg_req = ult_reg_req;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public boolean isCon_error() {
		return con_error;
	}
	public void setCon_error(boolean con_error) {
		this.con_error = con_error;
	}
	
	
	
	
}
