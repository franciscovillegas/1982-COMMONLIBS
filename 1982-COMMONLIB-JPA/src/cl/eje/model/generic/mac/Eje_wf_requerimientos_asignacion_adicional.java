package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wf_requerimientos_asignacion_adicional", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_asignacion_adicional", numerica = true, isForeignKey = false) })
public class Eje_wf_requerimientos_asignacion_adicional extends Vo {
	private int id_asignacion_adicional;
	private int id_req;
	private Date fecha_rol;
	private int	rut_rol_antes;
	private String nombre_persona_antes;
	private int rut_rol_despues;
	private String nombre_persona_despues;
	private boolean mantener_asignacion_anterior;
	private int id_escalamiento;
	private int id_registro_mastro;

	
	
	
	public boolean isMantener_asignacion_anterior() {
		return mantener_asignacion_anterior;
	}
	public void setMantener_asignacion_anterior(boolean mantener_asignacion_anterior) {
		this.mantener_asignacion_anterior = mantener_asignacion_anterior;
	}
	public int getId_asignacion_adicional() {
		return id_asignacion_adicional;
	}
	public void setId_asignacion_adicional(int id_asignacion_adicional) {
		this.id_asignacion_adicional = id_asignacion_adicional;
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
	public int getRut_rol_antes() {
		return rut_rol_antes;
	}
	public void setRut_rol_antes(int rut_rol_antes) {
		this.rut_rol_antes = rut_rol_antes;
	}
	public String getNombre_persona_antes() {
		return nombre_persona_antes;
	}
	public void setNombre_persona_antes(String nombre_persona_antes) {
		this.nombre_persona_antes = nombre_persona_antes;
	}
	public int getRut_rol_despues() {
		return rut_rol_despues;
	}
	public void setRut_rol_despues(int rut_rol_despues) {
		this.rut_rol_despues = rut_rol_despues;
	}
	public String getNombre_persona_despues() {
		return nombre_persona_despues;
	}
	public void setNombre_persona_despues(String nombre_persona_despues) {
		this.nombre_persona_despues = nombre_persona_despues;
	}
	public int getId_escalamiento() {
		return id_escalamiento;
	}
	public void setId_escalamiento(int id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}
	public int getId_registro_mastro() {
		return id_registro_mastro;
	}
	public void setId_registro_mastro(int id_registro_mastro) {
		this.id_registro_mastro = id_registro_mastro;
	}

	
}
