package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_rol", isForeignKey = true, numerica = true) }, tableName = "eje_wf_tupla_rol_estado")
public class Eje_wf_tupla_rol_estado extends Vo {
	private int id_producto;
	private int id_evento;
	private int id_suceso;
	private int id_rol;
	private int id_estado_ingresa;
	private int id_estado_deriva;
	private int id_estado_aprueba;
	private int id_estado_rechaza;
	private int id_estado_cierra;
	private int id_estado_informado;
	private int id_estado_receptor_workflow;

	public int getId_producto() {
		return id_producto;
	}

	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}

	public int getId_evento() {
		return id_evento;
	}

	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}

	public int getId_suceso() {
		return id_suceso;
	}

	public void setId_suceso(int id_suceso) {
		this.id_suceso = id_suceso;
	}

	public int getId_rol() {
		return id_rol;
	}

	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}

	public int getId_estado_ingresa() {
		return id_estado_ingresa;
	}

	public void setId_estado_ingresa(int id_estado_ingresa) {
		this.id_estado_ingresa = id_estado_ingresa;
	}

	public int getId_estado_deriva() {
		return id_estado_deriva;
	}

	public void setId_estado_deriva(int id_estado_deriva) {
		this.id_estado_deriva = id_estado_deriva;
	}

	public int getId_estado_aprueba() {
		return id_estado_aprueba;
	}

	public void setId_estado_aprueba(int id_estado_aprueba) {
		this.id_estado_aprueba = id_estado_aprueba;
	}

	public int getId_estado_rechaza() {
		return id_estado_rechaza;
	}

	public void setId_estado_rechaza(int id_estado_rechaza) {
		this.id_estado_rechaza = id_estado_rechaza;
	}

	public int getId_estado_cierra() {
		return id_estado_cierra;
	}

	public void setId_estado_cierra(int id_estado_cierra) {
		this.id_estado_cierra = id_estado_cierra;
	}

	public int getId_estado_informado() {
		return id_estado_informado;
	}

	public void setId_estado_informado(int id_estado_informado) {
		this.id_estado_informado = id_estado_informado;
	}

	public int getId_estado_receptor_workflow() {
		return id_estado_receptor_workflow;
	}

	public void setId_estado_receptor_workflow(int id_estado_receptor_workflow) {
		this.id_estado_receptor_workflow = id_estado_receptor_workflow;
	}

}
