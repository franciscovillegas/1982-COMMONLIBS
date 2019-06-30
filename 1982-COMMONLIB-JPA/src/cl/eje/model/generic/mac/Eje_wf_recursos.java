package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "cod_recurso", isForeignKey = false, numerica = true) }, tableName = "eje_wf_recursos")
public class Eje_wf_recursos extends Vo {

	private int cod_recurso;
	private String nombre;
	private String descripcion;
	private String path;
	private int id_tiporecurso;
	private String tipo_recurso;
	private int externo;

	public int getCod_recurso() {
		return cod_recurso;
	}

	public void setCod_recurso(int cod_recurso) {
		this.cod_recurso = cod_recurso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getId_tiporecurso() {
		return id_tiporecurso;
	}

	public void setId_tiporecurso(int id_tiporecurso) {
		this.id_tiporecurso = id_tiporecurso;
	}

	public String getTipo_recurso() {
		return tipo_recurso;
	}

	public void setTipo_recurso(String tipo_recurso) {
		this.tipo_recurso = tipo_recurso;
	}

	public int getExterno() {
		return externo;
	}

	public void setExterno(int externo) {
		this.externo = externo;
	}

}
