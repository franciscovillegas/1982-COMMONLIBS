package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_correo", isForeignKey = false, numerica = true) }, tableName = "eje_ges_bandeja_correo_entrada")
public class Eje_ges_bandeja_correo_entrada extends Vo {
	private int	id_correo;
	private Date fecha_recepcion;
	private Date fecha_envio;
	private Date fecha_eliminacion;
	private int	rut_remitente;
	private String mensaje;
	private String mensaje_full;
	private String titulo;
	private String data_adicional;
	private boolean favorito;
	
	public int getId_correo() {
		return id_correo;
	}
	public void setId_correo(int id_correo) {
		this.id_correo = id_correo;
	}
	public Date getFecha_recepcion() {
		return fecha_recepcion;
	}
	public void setFecha_recepcion(Date fecha_recepcion) {
		this.fecha_recepcion = fecha_recepcion;
	}
	public Date getFecha_envio() {
		return fecha_envio;
	}
	public void setFecha_envio(Date fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
	public Date getFecha_eliminacion() {
		return fecha_eliminacion;
	}
	public void setFecha_eliminacion(Date fecha_eliminacion) {
		this.fecha_eliminacion = fecha_eliminacion;
	}
	public int getRut_remitente() {
		return rut_remitente;
	}
	public void setRut_remitente(int rut_remitente) {
		this.rut_remitente = rut_remitente;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getMensaje_full() {
		return mensaje_full;
	}
	public void setMensaje_full(String mensaje_full) {
		this.mensaje_full = mensaje_full;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getData_adicional() {
		return data_adicional;
	}
	public void setData_adicional(String data_adicional) {
		this.data_adicional = data_adicional;
	}
	public boolean isFavorito() {
		return favorito;
	}
	public void setFavorito(boolean favorito) {
		this.favorito = favorito;
	}
	
	
	
}
