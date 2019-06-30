package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idlocal", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_local")
public class Eje_wfgen_local extends Vo {
	private int idlocal;
	private String nemo;
	private String nombre;
	private String direccion;
	private String latitud;
	private String longitud;

	public int getIdlocal() {
		return idlocal;
	}

	public void setIdlocal(int idlocal) {
		this.idlocal = idlocal;
	}

	public String getNemo() {
		return nemo;
	}

	public void setNemo(String nemo) {
		this.nemo = nemo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

}
