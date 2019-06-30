package portal.com.eje.portal.eje_generico_util.vo;

import java.util.Date;

import portal.com.eje.portal.vo.vo.Vo;

public class VoBandejaIntranet extends Vo {
	private String id_mensaje;
	private String mensaje;
	private int rut_receptor;
	private String nombre_receptor;
	private int rut_remitente;
	private String nombre_remitente;
	private String titulo;
	private Date fecha_envio;
	private Date fecha_recepcion_primeravez;
	private Date fecha_nomostrrar_bydestinatario;
	private String url;
	private String mensaje_full;
	private boolean es_url;
	private boolean es_mensaje;
	private String data_adicional;

	public String getId_mensaje() {
		return id_mensaje;
	}

	public void setId_mensaje(String id_mensaje) {
		this.id_mensaje = id_mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getRut_receptor() {
		return rut_receptor;
	}

	public void setRut_receptor(int rut_receptor) {
		this.rut_receptor = rut_receptor;
	}

	public String getNombre_receptor() {
		return nombre_receptor;
	}

	public void setNombre_receptor(String nombre_receptor) {
		this.nombre_receptor = nombre_receptor;
	}

	public int getRut_remitente() {
		return rut_remitente;
	}

	public void setRut_remitente(int rut_remitente) {
		this.rut_remitente = rut_remitente;
	}

	public String getNombre_remitente() {
		return nombre_remitente;
	}

	public void setNombre_remitente(String nombre_remitente) {
		this.nombre_remitente = nombre_remitente;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getFecha_envio() {
		return fecha_envio;
	}

	public void setFecha_envio(Date fecha_envio) {
		this.fecha_envio = fecha_envio;
	}

	public Date getFecha_recepcion_primeravez() {
		return fecha_recepcion_primeravez;
	}

	public void setFecha_recepcion_primeravez(Date fecha_recepcion_primeravez) {
		this.fecha_recepcion_primeravez = fecha_recepcion_primeravez;
	}

	public Date getFecha_nomostrrar_bydestinatario() {
		return fecha_nomostrrar_bydestinatario;
	}

	public void setFecha_nomostrrar_bydestinatario(Date fecha_nomostrrar_bydestinatario) {
		this.fecha_nomostrrar_bydestinatario = fecha_nomostrrar_bydestinatario;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMensaje_full() {
		return mensaje_full;
	}

	public void setMensaje_full(String mensaje_full) {
		this.mensaje_full = mensaje_full;
	}

	public boolean isEs_url() {
		return es_url;
	}

	public void setEs_url(boolean es_url) {
		this.es_url = es_url;
	}

	public boolean isEs_mensaje() {
		return es_mensaje;
	}

	public void setEs_mensaje(boolean es_mensaje) {
		this.es_mensaje = es_mensaje;
	}

	public String getData_adicional() {
		return data_adicional;
	}

	public void setData_adicional(String data_adicional) {
		this.data_adicional = data_adicional;
	}

}
