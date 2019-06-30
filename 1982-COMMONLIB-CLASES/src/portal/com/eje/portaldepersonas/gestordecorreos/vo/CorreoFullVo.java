package portal.com.eje.portaldepersonas.gestordecorreos.vo;

import java.util.Date;

import portal.com.eje.portal.vo.vo.Vo;

public class CorreoFullVo extends Vo {
	private int id_correo;
	private Date fecha_recepcion;
	private Date fecha_envio;
	private Date fecha_eliminacion;
	private int rut_remitente;
	private String titulo;
	private String nombres;

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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

}
