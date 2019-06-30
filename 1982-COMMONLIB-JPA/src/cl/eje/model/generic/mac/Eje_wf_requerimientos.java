package cl.eje.model.generic.mac;

import java.util.Collection;
import java.util.Date;

import cl.eje.model.generic.portal.Eje_ges_trabajador;
import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_req", numerica = true, isForeignKey = false) }, tableName = "eje_wf_requerimientos")
@TableReferences({
		@TableReference(field = "eje_wf_tripleta_producto", fk = @ForeignKeyReference(fk = "id_producto", otherTableField = "id_producto"), voClass = Eje_wf_tripleta_producto.class),
		@TableReference(field = "eje_wf_tripleta_evento", fk = @ForeignKeyReference(fk = "id_evento", otherTableField = "id_evento"), voClass = Eje_wf_tripleta_evento.class),
		@TableReference(field = "eje_wf_tripleta_evento", fk = @ForeignKeyReference(fk = "id_producto", otherTableField = "id_producto"), voClass = Eje_wf_tripleta_evento.class),
		@TableReference(field = "eje_wf_tripleta_suceso", fk = @ForeignKeyReference(fk = "id_suceso", otherTableField = "id_suceso"), voClass = Eje_wf_tripleta_suceso.class),
		@TableReference(field = "eje_wf_tripleta_suceso", fk = @ForeignKeyReference(fk = "id_evento", otherTableField = "id_evento"), voClass = Eje_wf_tripleta_suceso.class),
		@TableReference(field = "eje_wf_tripleta_suceso", fk = @ForeignKeyReference(fk = "id_producto", otherTableField = "id_producto"), voClass = Eje_wf_tripleta_suceso.class),

		@TableReference(field = "eje_wf_escalamientos_registro_maestro", fk = @ForeignKeyReference(fk = "id_req", otherTableField = "id_req"), voClass = Eje_wf_escalamientos_registro_maestro.class),
		@TableReference(field = "eje_ges_trabajador_rol_actual", fk = @ForeignKeyReference(fk = "rut_rol", otherTableField = "rut"), voClass = Eje_ges_trabajador.class),
		@TableReference(field = "eje_ges_trabajador_rol_antes", fk = @ForeignKeyReference(fk = "rut_rol_antes", otherTableField = "rut"), voClass = Eje_ges_trabajador.class),
		@TableReference(field = "eje_ges_trabajador_responsable", fk = @ForeignKeyReference(fk = "rut_responsable", otherTableField = "rut"), voClass = Eje_ges_trabajador.class),
		@TableReference(field = "eje_wf_data_contratacion", fk = @ForeignKeyReference(fk = "id_req", otherTableField = "id_req"), voClass = Eje_wf_data_contratacion.class),

})

public class Eje_wf_requerimientos extends Vo {
	private Eje_wf_tripleta_producto eje_wf_tripleta_producto;
	private Eje_wf_tripleta_evento eje_wf_tripleta_evento;
	private Eje_wf_tripleta_suceso eje_wf_tripleta_suceso;
	private Collection<Eje_wf_escalamientos_registro_maestro> eje_wf_escalamientos_registro_maestro;
	private Eje_ges_trabajador eje_ges_trabajador_rol_actual;
	private Eje_ges_trabajador eje_ges_trabajador_rol_antes;
	private Eje_ges_trabajador eje_ges_trabajador_responsable;
	private Eje_wf_data_contratacion eje_wf_data_contratacion;

	private int id_req;
	private String nombre_cliente;
	private String correo;
	private int id_evento;
	private int id_suceso;
	private int id_producto;
	private int cod_emp_propios;
	private int cod_planta;
	private int cod_emp_terceros;
	private String glosa;
	private String nombre_archivo;
	private String subida_excel;
	private Date fecha_req;
	private int id_status;
	private int id_rol;
	private String comentario;
	private int id_rol_antes;
	private Date fecha_fin_req;
	private int rut_rol;
	private int rut_rol_antes;
	private Date fecha_rol;
	private int rut_responsable;
	private String tipo_requerimiento;
	private String tipo_ausencia;
	private String es_historico;
	private int tipo_cierre;
	private String data_adic;
	private int id_estado;
	private String origen_sc;
	private String origen_iticket;
	private String origen_ioxml;
	private boolean escalamiento_desactivado;
	private Date escalamiento_desactivado_fecha_update;
	
	public Eje_wf_tripleta_producto getEje_wf_tripleta_producto() {
		return eje_wf_tripleta_producto;
	}
	public void setEje_wf_tripleta_producto(Eje_wf_tripleta_producto eje_wf_tripleta_producto) {
		this.eje_wf_tripleta_producto = eje_wf_tripleta_producto;
	}
	public Eje_wf_tripleta_evento getEje_wf_tripleta_evento() {
		return eje_wf_tripleta_evento;
	}
	public void setEje_wf_tripleta_evento(Eje_wf_tripleta_evento eje_wf_tripleta_evento) {
		this.eje_wf_tripleta_evento = eje_wf_tripleta_evento;
	}
	public Eje_wf_tripleta_suceso getEje_wf_tripleta_suceso() {
		return eje_wf_tripleta_suceso;
	}
	public void setEje_wf_tripleta_suceso(Eje_wf_tripleta_suceso eje_wf_tripleta_suceso) {
		this.eje_wf_tripleta_suceso = eje_wf_tripleta_suceso;
	}
	public Collection<Eje_wf_escalamientos_registro_maestro> getEje_wf_escalamientos_registro_maestro() {
		return eje_wf_escalamientos_registro_maestro;
	}
	public void setEje_wf_escalamientos_registro_maestro(
			Collection<Eje_wf_escalamientos_registro_maestro> eje_wf_escalamientos_registro_maestro) {
		this.eje_wf_escalamientos_registro_maestro = eje_wf_escalamientos_registro_maestro;
	}
	public Eje_ges_trabajador getEje_ges_trabajador_rol_actual() {
		return eje_ges_trabajador_rol_actual;
	}
	public void setEje_ges_trabajador_rol_actual(Eje_ges_trabajador eje_ges_trabajador_rol_actual) {
		this.eje_ges_trabajador_rol_actual = eje_ges_trabajador_rol_actual;
	}
	public Eje_ges_trabajador getEje_ges_trabajador_rol_antes() {
		return eje_ges_trabajador_rol_antes;
	}
	public void setEje_ges_trabajador_rol_antes(Eje_ges_trabajador eje_ges_trabajador_rol_antes) {
		this.eje_ges_trabajador_rol_antes = eje_ges_trabajador_rol_antes;
	}
	public Eje_ges_trabajador getEje_ges_trabajador_responsable() {
		return eje_ges_trabajador_responsable;
	}
	public void setEje_ges_trabajador_responsable(Eje_ges_trabajador eje_ges_trabajador_responsable) {
		this.eje_ges_trabajador_responsable = eje_ges_trabajador_responsable;
	}
	public Eje_wf_data_contratacion getEje_wf_data_contratacion() {
		return eje_wf_data_contratacion;
	}
	public void setEje_wf_data_contratacion(Eje_wf_data_contratacion eje_wf_data_contratacion) {
		this.eje_wf_data_contratacion = eje_wf_data_contratacion;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public String getNombre_cliente() {
		return nombre_cliente;
	}
	public void setNombre_cliente(String nombre_cliente) {
		this.nombre_cliente = nombre_cliente;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public int getId_evento() {
		return id_evento;
	}
	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}
	public int getId_suceso() {
		return id_suceso;
	}
	public void setId_suceso(int id_suceso) {
		this.id_suceso = id_suceso;
	}
	public int getId_producto() {
		return id_producto;
	}
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}
	public int getCod_emp_propios() {
		return cod_emp_propios;
	}
	public void setCod_emp_propios(int cod_emp_propios) {
		this.cod_emp_propios = cod_emp_propios;
	}
	public int getCod_planta() {
		return cod_planta;
	}
	public void setCod_planta(int cod_planta) {
		this.cod_planta = cod_planta;
	}
	public int getCod_emp_terceros() {
		return cod_emp_terceros;
	}
	public void setCod_emp_terceros(int cod_emp_terceros) {
		this.cod_emp_terceros = cod_emp_terceros;
	}
	public String getGlosa() {
		return glosa;
	}
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
	public String getNombre_archivo() {
		return nombre_archivo;
	}
	public void setNombre_archivo(String nombre_archivo) {
		this.nombre_archivo = nombre_archivo;
	}
	public String getSubida_excel() {
		return subida_excel;
	}
	public void setSubida_excel(String subida_excel) {
		this.subida_excel = subida_excel;
	}
	public Date getFecha_req() {
		return fecha_req;
	}
	public void setFecha_req(Date fecha_req) {
		this.fecha_req = fecha_req;
	}
	public int getId_status() {
		return id_status;
	}
	public void setId_status(int id_status) {
		this.id_status = id_status;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getId_rol_antes() {
		return id_rol_antes;
	}
	public void setId_rol_antes(int id_rol_antes) {
		this.id_rol_antes = id_rol_antes;
	}
	public Date getFecha_fin_req() {
		return fecha_fin_req;
	}
	public void setFecha_fin_req(Date fecha_fin_req) {
		this.fecha_fin_req = fecha_fin_req;
	}
	public int getRut_rol() {
		return rut_rol;
	}
	public void setRut_rol(int rut_rol) {
		this.rut_rol = rut_rol;
	}
	public int getRut_rol_antes() {
		return rut_rol_antes;
	}
	public void setRut_rol_antes(int rut_rol_antes) {
		this.rut_rol_antes = rut_rol_antes;
	}
	public Date getFecha_rol() {
		return fecha_rol;
	}
	public void setFecha_rol(Date fecha_rol) {
		this.fecha_rol = fecha_rol;
	}
	public int getRut_responsable() {
		return rut_responsable;
	}
	public void setRut_responsable(int rut_responsable) {
		this.rut_responsable = rut_responsable;
	}
	public String getTipo_requerimiento() {
		return tipo_requerimiento;
	}
	public void setTipo_requerimiento(String tipo_requerimiento) {
		this.tipo_requerimiento = tipo_requerimiento;
	}
	public String getTipo_ausencia() {
		return tipo_ausencia;
	}
	public void setTipo_ausencia(String tipo_ausencia) {
		this.tipo_ausencia = tipo_ausencia;
	}
	public String getEs_historico() {
		return es_historico;
	}
	public void setEs_historico(String es_historico) {
		this.es_historico = es_historico;
	}
	public int getTipo_cierre() {
		return tipo_cierre;
	}
	public void setTipo_cierre(int tipo_cierre) {
		this.tipo_cierre = tipo_cierre;
	}
	public String getData_adic() {
		return data_adic;
	}
	public void setData_adic(String data_adic) {
		this.data_adic = data_adic;
	}
	public int getId_estado() {
		return id_estado;
	}
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	public String getOrigen_sc() {
		return origen_sc;
	}
	public void setOrigen_sc(String origen_sc) {
		this.origen_sc = origen_sc;
	}
	public String getOrigen_iticket() {
		return origen_iticket;
	}
	public void setOrigen_iticket(String origen_iticket) {
		this.origen_iticket = origen_iticket;
	}
	public String getOrigen_ioxml() {
		return origen_ioxml;
	}
	public void setOrigen_ioxml(String origen_ioxml) {
		this.origen_ioxml = origen_ioxml;
	}
	public boolean isEscalamiento_desactivado() {
		return escalamiento_desactivado;
	}
	public void setEscalamiento_desactivado(boolean escalamiento_desactivado) {
		this.escalamiento_desactivado = escalamiento_desactivado;
	}
	public Date getEscalamiento_desactivado_fecha_update() {
		return escalamiento_desactivado_fecha_update;
	}
	public void setEscalamiento_desactivado_fecha_update(Date escalamiento_desactivado_fecha_update) {
		this.escalamiento_desactivado_fecha_update = escalamiento_desactivado_fecha_update;
	}

	
}
