package portal.com.eje.portaldepersonas.gestordecorreos.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class CorreoVo extends Vo {
	private int id_correo;
	private String remitente;
	private String resumen;
	private String datos_contacto;

	public int getId_correo() {
		return id_correo;
	}

	public void setId_correo(int id_correo) {
		this.id_correo = id_correo;
	}

	public String getRemitente() {
		return remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getDatos_contacto() {
		return datos_contacto;
	}

	public void setDatos_contacto(String datos_contacto) {
		this.datos_contacto = datos_contacto;
	}

}
