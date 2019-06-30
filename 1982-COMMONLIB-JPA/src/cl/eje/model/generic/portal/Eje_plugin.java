package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_plugin", isForeignKey = false, numerica = true) }, tableName = "eje_plugin")
public class Eje_plugin extends Vo {
	private int id_plugin;
	private String nombre;
	private String singleton_key;
	private boolean activado;
	private Date fecha_cambio_activacion_flag;
	private String path_jar;
	private boolean is_seems_ok;
	private String version;
	private double version_especificacion;
	private int tiempo_activacion_ms;
	private int tiempo_desactivacion_ms;
	private String url_componente;
	private boolean tiene_actualizacion;
	private Date fecha_actualizacion;
	
	public int getId_plugin() {
		return id_plugin;
	}
	public void setId_plugin(int id_plugin) {
		this.id_plugin = id_plugin;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSingleton_key() {
		return singleton_key;
	}
	public void setSingleton_key(String singleton_key) {
		this.singleton_key = singleton_key;
	}
	public boolean isActivado() {
		return activado;
	}
	public void setActivado(boolean activado) {
		this.activado = activado;
	}
	public Date getFecha_cambio_activacion_flag() {
		return fecha_cambio_activacion_flag;
	}
	public void setFecha_cambio_activacion_flag(Date fecha_cambio_activacion_flag) {
		this.fecha_cambio_activacion_flag = fecha_cambio_activacion_flag;
	}
	public String getPath_jar() {
		return path_jar;
	}
	public void setPath_jar(String path_jar) {
		this.path_jar = path_jar;
	}
	public boolean isIs_seems_ok() {
		return is_seems_ok;
	}
	public void setIs_seems_ok(boolean is_seems_ok) {
		this.is_seems_ok = is_seems_ok;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public double getVersion_especificacion() {
		return version_especificacion;
	}
	public void setVersion_especificacion(double version_especificacion) {
		this.version_especificacion = version_especificacion;
	}
	public int getTiempo_activacion_ms() {
		return tiempo_activacion_ms;
	}
	public void setTiempo_activacion_ms(int tiempo_activacion_ms) {
		this.tiempo_activacion_ms = tiempo_activacion_ms;
	}
	public int getTiempo_desactivacion_ms() {
		return tiempo_desactivacion_ms;
	}
	public void setTiempo_desactivacion_ms(int tiempo_desactivacion_ms) {
		this.tiempo_desactivacion_ms = tiempo_desactivacion_ms;
	}
	public String getUrl_componente() {
		return url_componente;
	}
	public void setUrl_componente(String url_componente) {
		this.url_componente = url_componente;
	}
	public boolean isTiene_actualizacion() {
		return tiene_actualizacion;
	}
	public void setTiene_actualizacion(boolean tiene_actualizacion) {
		this.tiene_actualizacion = tiene_actualizacion;
	}
	public Date getFecha_actualizacion() {
		return fecha_actualizacion;
	}
	public void setFecha_actualizacion(Date fecha_actualizacion) {
		this.fecha_actualizacion = fecha_actualizacion;
	}
	
	
	
	
}
