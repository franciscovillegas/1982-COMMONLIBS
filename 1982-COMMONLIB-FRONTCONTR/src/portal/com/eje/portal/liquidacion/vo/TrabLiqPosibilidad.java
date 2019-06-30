package portal.com.eje.portal.liquidacion.vo;

import java.io.File;
import java.util.Date;

import cl.eje.model.generic.portal.Eje_ges_certif_histo_liquidacion_cabecera;
import cl.eje.model.generic.portal.Eje_ges_ficha;
import cl.eje.model.generic.portal.Eje_ges_trabajador;
import cl.eje.model.generic.portal.Eje_ges_trabajador_historia;
import cl.ejedigital.tool.misc.Formatear;
import portal.com.eje.portal.vo.vo.Vo;

public class TrabLiqPosibilidad extends Vo {
	private int rut;
	private File file_to_create;
	private String cargo;
	private String ccosto;
	private int periodo;
	private int dias_licencias;
	private int dias_trabajados;
	private Date fecha_traspaso_inc;
	private int empresa;
	private Eje_ges_ficha eje_ges_ficha;
	private Eje_ges_trabajador eje_ges_trabajador;
	private Eje_ges_trabajador_historia eje_ges_trabajador_historia;
	private Eje_ges_certif_histo_liquidacion_cabecera eje_ges_certif_histo_liquidacion_cabecera;
	
	public File getFile_to_create() {
		return file_to_create;
	}
	public void setFile_to_create(File file_to_create) {
		this.file_to_create = file_to_create;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getCcosto() {
		return ccosto;
	}
	public void setCcosto(String ccosto) {
		this.ccosto = ccosto;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public int getDias_licencias() {
		return dias_licencias;
	}
	public void setDias_licencias(int dias_licencias) {
		this.dias_licencias = dias_licencias;
	}
	public int getDias_trabajados() {
		return dias_trabajados;
	}
	public void setDias_trabajados(int dias_trabajados) {
		this.dias_trabajados = dias_trabajados;
	}
	public Date getFecha_traspaso_inc() {
		return fecha_traspaso_inc;
	}
	public void setFecha_traspaso_inc(Date fecha_traspaso_inc) {
		this.fecha_traspaso_inc = fecha_traspaso_inc;
	}
	public int getEmpresa() {
		return empresa;
	}
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	 
	
	public Eje_ges_ficha getEje_ges_ficha() {
		return eje_ges_ficha;
	}
	public void setEje_ges_ficha(Eje_ges_ficha eje_ges_ficha) {
		this.eje_ges_ficha = eje_ges_ficha;
	}
	public Eje_ges_trabajador getEje_ges_trabajador() {
		return eje_ges_trabajador;
	}
	public void setEje_ges_trabajador(Eje_ges_trabajador eje_ges_trabajador) {
		this.eje_ges_trabajador = eje_ges_trabajador;
	}
	public Eje_ges_trabajador_historia getEje_ges_trabajador_historia() {
		return eje_ges_trabajador_historia;
	}
	public void setEje_ges_trabajador_historia(Eje_ges_trabajador_historia eje_ges_trabajador_historia) {
		this.eje_ges_trabajador_historia = eje_ges_trabajador_historia;
	}
	public Eje_ges_certif_histo_liquidacion_cabecera getEje_ges_certif_histo_liquidacion_cabecera() {
		return eje_ges_certif_histo_liquidacion_cabecera;
	}
	public void setEje_ges_certif_histo_liquidacion_cabecera(Eje_ges_certif_histo_liquidacion_cabecera eje_ges_certif_histo_liquidacion_cabecera) {
		this.eje_ges_certif_histo_liquidacion_cabecera = eje_ges_certif_histo_liquidacion_cabecera;
	}
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("rut:").append(this.getRut());
		str.append(",periodo:").append(this.getPeriodo());
		str.append(",fecha_traspaso_inc:").append(Formatear.getInstance().toDate(getFecha_traspaso_inc(), "dd-MM-yyyy HH:mm:ss.SSS"));
		str.append(",cargo:").append(this.getCargo());
		str.append(",wp_cod_empresa:").append(this.getEmpresa());
		str.append(",ccosto:").append(this.getCcosto());
		str.append(",d_licencias:").append(this.getDias_licencias());
		str.append(",d_trabaj:").append(this.getDias_trabajados());
		
		return str.toString();
	}
	
	
}
