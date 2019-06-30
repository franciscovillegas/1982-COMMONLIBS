package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idcorr_rutas", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_rutas")
public class Eje_wfgen_rutas extends Vo {
	private int idcorr_rutas;
	private int idcorr_ruta;
	private int idcliente;
	private int idcadena;
	private int idlocal;
	private boolean activo;

	public int getIdcorr_rutas() {
		return idcorr_rutas;
	}

	public void setIdcorr_rutas(int idcorr_rutas) {
		this.idcorr_rutas = idcorr_rutas;
	}

	public int getIdcorr_ruta() {
		return idcorr_ruta;
	}

	public void setIdcorr_ruta(int idcorr_ruta) {
		this.idcorr_ruta = idcorr_ruta;
	}

	public int getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
	}

	public int getIdcadena() {
		return idcadena;
	}

	public void setIdcadena(int idcadena) {
		this.idcadena = idcadena;
	}

	public int getIdlocal() {
		return idlocal;
	}

	public void setIdlocal(int idlocal) {
		this.idlocal = idlocal;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}