package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableReferences({
		@TableReference(field = "eje_wfgen_postulante", voClass = Eje_wfgen_postulante.class, fk = @ForeignKeyReference(fk = "id_postulante", otherTableField = "id_postulante")),
		@TableReference(field = "eje_wf_requerimientos", voClass = Eje_wf_requerimientos.class, fk = @ForeignKeyReference(fk = "id_req", otherTableField = "id_req")), })
@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_req", numerica = true, isForeignKey = true) }, tableName = "eje_wf_data_contratacion")
public class Eje_wf_data_contratacion extends Vo {
	private Eje_wfgen_postulante eje_wfgen_postulante;
	private Eje_wf_requerimientos eje_wf_requerimientos;

	private int id_req;
	private int id_motivodotacion;
	private int id_postulante;
	private Date fecha_inicio;
	private Date fecha_termino;
	private Date fecha_ingreso;
	private Date primer_vencimiento;
	private Date segundo_vencimiento;
	private Integer id_tipocontrato;
	private String observacion;
	private int id_tipodecobertura;
	private String especificacion;
	private int id_periodoremuneracion;
	private int id_turnolaboral;
	private int id_horariolaboral;
	private Double monto;
	private String perfil;
	private String perfil_especificacion;
	private String comision;
	private String sustento;
	private Integer id_reemplazo;
	private Integer id_empresa;
	private Integer id_lote;
	private Integer id_contrato_afirmar;
	private Integer id_contrato_firmado;
	private String cliente;
	private String ncotizacion;
	private String id_unidad;
	private String unidad;
	private String cargo_desc;
	private boolean rol_privado;
	private int idcorr_ruta;

	public Eje_wf_requerimientos getEje_wf_requerimientos() {
		return eje_wf_requerimientos;
	}

	public void setEje_wf_requerimientos(Eje_wf_requerimientos eje_wf_requerimientos) {
		this.eje_wf_requerimientos = eje_wf_requerimientos;
	}

	public Eje_wfgen_postulante getEje_wfgen_postulante() {
		return eje_wfgen_postulante;
	}

	public void setEje_wfgen_postulante(Eje_wfgen_postulante eje_wfgen_postulante) {
		this.eje_wfgen_postulante = eje_wfgen_postulante;
	}

	public int getId_req() {
		return id_req;
	}

	public void setId_req(int id_req) {
		this.id_req = id_req;
	}

	public int getId_motivodotacion() {
		return id_motivodotacion;
	}

	public void setId_motivodotacion(int id_motivodotacion) {
		this.id_motivodotacion = id_motivodotacion;
	}

	public int getId_postulante() {
		return id_postulante;
	}

	public void setId_postulante(int id_postulante) {
		this.id_postulante = id_postulante;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_termino() {
		return fecha_termino;
	}

	public void setFecha_termino(Date fecha_termino) {
		this.fecha_termino = fecha_termino;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public Date getPrimer_vencimiento() {
		return primer_vencimiento;
	}

	public void setPrimer_vencimiento(Date primer_vencimiento) {
		this.primer_vencimiento = primer_vencimiento;
	}

	public Date getSegundo_vencimiento() {
		return segundo_vencimiento;
	}

	public void setSegundo_vencimiento(Date segundo_vencimiento) {
		this.segundo_vencimiento = segundo_vencimiento;
	}

	public Integer getId_tipocontrato() {
		return id_tipocontrato;
	}

	public void setId_tipocontrato(Integer id_tipocontrato) {
		this.id_tipocontrato = id_tipocontrato;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public int getId_tipodecobertura() {
		return id_tipodecobertura;
	}

	public void setId_tipodecobertura(int id_tipodecobertura) {
		this.id_tipodecobertura = id_tipodecobertura;
	}

	public String getEspecificacion() {
		return especificacion;
	}

	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}

	public int getId_periodoremuneracion() {
		return id_periodoremuneracion;
	}

	public void setId_periodoremuneracion(int id_periodoremuneracion) {
		this.id_periodoremuneracion = id_periodoremuneracion;
	}

	public int getId_turnolaboral() {
		return id_turnolaboral;
	}

	public void setId_turnolaboral(int id_turnolaboral) {
		this.id_turnolaboral = id_turnolaboral;
	}

	public int getId_horariolaboral() {
		return id_horariolaboral;
	}

	public void setId_horariolaboral(int id_horariolaboral) {
		this.id_horariolaboral = id_horariolaboral;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPerfil_especificacion() {
		return perfil_especificacion;
	}

	public void setPerfil_especificacion(String perfil_especificacion) {
		this.perfil_especificacion = perfil_especificacion;
	}

	public String getComision() {
		return comision;
	}

	public void setComision(String comision) {
		this.comision = comision;
	}

	public String getSustento() {
		return sustento;
	}

	public void setSustento(String sustento) {
		this.sustento = sustento;
	}

	public Integer getId_reemplazo() {
		return id_reemplazo;
	}

	public void setId_reemplazo(Integer id_reemplazo) {
		this.id_reemplazo = id_reemplazo;
	}

	public Integer getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(Integer id_empresa) {
		this.id_empresa = id_empresa;
	}

	public Integer getId_lote() {
		return id_lote;
	}

	public void setId_lote(Integer id_lote) {
		this.id_lote = id_lote;
	}

	public Integer getId_contrato_afirmar() {
		return id_contrato_afirmar;
	}

	public void setId_contrato_afirmar(Integer id_contrato_afirmar) {
		this.id_contrato_afirmar = id_contrato_afirmar;
	}

	public Integer getId_contrato_firmado() {
		return id_contrato_firmado;
	}

	public void setId_contrato_firmado(Integer id_contrato_firmado) {
		this.id_contrato_firmado = id_contrato_firmado;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNcotizacion() {
		return ncotizacion;
	}

	public void setNcotizacion(String ncotizacion) {
		this.ncotizacion = ncotizacion;
	}

	public String getId_unidad() {
		return id_unidad;
	}

	public void setId_unidad(String id_unidad) {
		this.id_unidad = id_unidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getCargo_desc() {
		return cargo_desc;
	}

	public void setCargo_desc(String cargo_desc) {
		this.cargo_desc = cargo_desc;
	}

	public boolean isRol_privado() {
		return rol_privado;
	}

	public void setRol_privado(boolean rol_privado) {
		this.rol_privado = rol_privado;
	}

	public int getIdcorr_ruta() {
		return idcorr_ruta;
	}

	public void setIdcorr_ruta(int idcorr_ruta) {
		this.idcorr_ruta = idcorr_ruta;
	}

}
