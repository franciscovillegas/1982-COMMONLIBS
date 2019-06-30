package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", 
pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "secuencia", numerica = true, isForeignKey = false),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_rol", numerica = true, isForeignKey = false),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_producto", numerica = true, isForeignKey = false),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_evento", numerica = true, isForeignKey = false),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_suceso", numerica = true, isForeignKey = false)}, tableName = "eje_wf_tiempo_escalamiento")
public class Eje_wf_tiempo_escalamiento extends Vo {
	private int	id_producto;
	private int	id_evento;
	private int	id_suceso;
	private int	id_rol;
	private int	secuencia;
	private int	tiempo_espera;
	private int	id_tipoescalamiento;
	private int	cod_recurso;
	private int	id_persona;
	private int	nivel;
	
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
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public int getTiempo_espera() {
		return tiempo_espera;
	}
	public void setTiempo_espera(int tiempo_espera) {
		this.tiempo_espera = tiempo_espera;
	}
	public int getId_tipoescalamiento() {
		return id_tipoescalamiento;
	}
	public void setId_tipoescalamiento(int id_tipoescalamiento) {
		this.id_tipoescalamiento = id_tipoescalamiento;
	}
	public int getCod_recurso() {
		return cod_recurso;
	}
	public void setCod_recurso(int cod_recurso) {
		this.cod_recurso = cod_recurso;
	}
	public int getId_persona() {
		return id_persona;
	}
	public void setId_persona(int id_persona) {
		this.id_persona = id_persona;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	

}
