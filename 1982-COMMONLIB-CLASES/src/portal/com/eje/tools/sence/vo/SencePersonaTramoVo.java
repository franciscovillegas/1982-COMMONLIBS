package portal.com.eje.tools.sence.vo;

import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.sence.enums.EnumSenceTramo;

public class SencePersonaTramoVo extends Vo {
	private int rut;
	private int wp_cod_empresa;
	private int wp_cod_planta;
	private String nombre;
	private String ape_paterno;
	private String ape_materno;
	private double total_haberes;
	private EnumSenceTramo tramo;
	
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public int getWp_cod_empresa() {
		return wp_cod_empresa;
	}
	public void setWp_cod_empresa(int wp_cod_empresa) {
		this.wp_cod_empresa = wp_cod_empresa;
	}
	public int getWp_cod_planta() {
		return wp_cod_planta;
	}
	public void setWp_cod_planta(int wp_cod_planta) {
		this.wp_cod_planta = wp_cod_planta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApe_paterno() {
		return ape_paterno;
	}
	public void setApe_paterno(String ape_paterno) {
		this.ape_paterno = ape_paterno;
	}
	public String getApe_materno() {
		return ape_materno;
	}
	public void setApe_materno(String ape_materno) {
		this.ape_materno = ape_materno;
	}
	public double getTotal_haberes() {
		return total_haberes;
	}
	public void setTotal_haberes(double total_haberes) {
		this.total_haberes = total_haberes;
	}
	public EnumSenceTramo getTramo() {
		return tramo;
	}
	public void setTramo(EnumSenceTramo tramo) {
		this.tramo = tramo;
	}
	
	
	
}
