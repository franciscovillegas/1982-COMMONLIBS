package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { 
			@PrimaryKeyDefinition(autoIncremental = false, field = "id_producto", isForeignKey = true, numerica = true),
			@PrimaryKeyDefinition(autoIncremental = false, field = "id_evento", isForeignKey = true, numerica = true),
			@PrimaryKeyDefinition(autoIncremental = false, field = "id_suceso", isForeignKey = true, numerica = true),
			@PrimaryKeyDefinition(autoIncremental = false, field = "id_rol", isForeignKey = true, numerica = true),
			@PrimaryKeyDefinition(autoIncremental = false, field = "cod_perfil", isForeignKey = true, numerica = true) 
			}, tableName = "eje_wf_tupla_rol_perfil")
public class Eje_wf_tupla_rol_perfil extends Vo {
	private int	id_producto;
	private int	id_evento;
	private int	id_suceso;
	private int	id_rol;
	private int	cod_perfil;
	
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
	public int getCod_perfil() {
		return cod_perfil;
	}
	public void setCod_perfil(int cod_perfil) {
		this.cod_perfil = cod_perfil;
	}
	
	
	
}
