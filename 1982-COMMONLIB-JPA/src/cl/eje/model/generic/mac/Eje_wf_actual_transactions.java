package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "actual_transaccion_id", isForeignKey = false, numerica = true) }, tableName = "eje_wf_actual_transactions")
public class Eje_wf_actual_transactions extends Vo {
	private int actual_transaccion_id;
	private int event_outcome_code;
	private int transaction_id;
	private int process_id;
	private int id_evento;
	private Date actual_transaction_datetime;
	private String actual_transaction_details;
	private Date fecha_ingreso_req;
	private Date fecha_term_req;
	private Date fecha_apertura;
	private Date fecha_derivacion;
	private Date fecha_recepcion;
	private int id_req;
	private int id_rol_antes;
	private String nombre_rol_antes;
	private int id_rol_despues;
	private String nombre_rol_despues;
	private String comentario;
	private Date permanencia;
	private int rut_rol_antes;
	private String responsable_anterior;
	private int rut_rol_despues;
	private String responsable_actual;
	private int id_motivo_rechazo;
	private int id_estado;
	private String connect_wf_context;
	private int connect_wf_id_req_remoto;
	private int connect_wf_actual_transaccion_id_remoto;
	private String connect_wf_direccion;
	private String context;
	
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public int getActual_transaccion_id() {
		return actual_transaccion_id;
	}
	public void setActual_transaccion_id(int actual_transaccion_id) {
		this.actual_transaccion_id = actual_transaccion_id;
	}
	public int getEvent_outcome_code() {
		return event_outcome_code;
	}
	public void setEvent_outcome_code(int event_outcome_code) {
		this.event_outcome_code = event_outcome_code;
	}
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}
	public int getProcess_id() {
		return process_id;
	}
	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}
	public int getId_evento() {
		return id_evento;
	}
	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}
	public Date getActual_transaction_datetime() {
		return actual_transaction_datetime;
	}
	public void setActual_transaction_datetime(Date actual_transaction_datetime) {
		this.actual_transaction_datetime = actual_transaction_datetime;
	}
	public String getActual_transaction_details() {
		return actual_transaction_details;
	}
	public void setActual_transaction_details(String actual_transaction_details) {
		this.actual_transaction_details = actual_transaction_details;
	}
	public Date getFecha_ingreso_req() {
		return fecha_ingreso_req;
	}
	public void setFecha_ingreso_req(Date fecha_ingreso_req) {
		this.fecha_ingreso_req = fecha_ingreso_req;
	}
	public Date getFecha_term_req() {
		return fecha_term_req;
	}
	public void setFecha_term_req(Date fecha_term_req) {
		this.fecha_term_req = fecha_term_req;
	}
	public Date getFecha_apertura() {
		return fecha_apertura;
	}
	public void setFecha_apertura(Date fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}
	public Date getFecha_derivacion() {
		return fecha_derivacion;
	}
	public void setFecha_derivacion(Date fecha_derivacion) {
		this.fecha_derivacion = fecha_derivacion;
	}
	public Date getFecha_recepcion() {
		return fecha_recepcion;
	}
	public void setFecha_recepcion(Date fecha_recepcion) {
		this.fecha_recepcion = fecha_recepcion;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public int getId_rol_antes() {
		return id_rol_antes;
	}
	public void setId_rol_antes(int id_rol_antes) {
		this.id_rol_antes = id_rol_antes;
	}
	public String getNombre_rol_antes() {
		return nombre_rol_antes;
	}
	public void setNombre_rol_antes(String nombre_rol_antes) {
		this.nombre_rol_antes = nombre_rol_antes;
	}
	public int getId_rol_despues() {
		return id_rol_despues;
	}
	public void setId_rol_despues(int id_rol_despues) {
		this.id_rol_despues = id_rol_despues;
	}
	public String getNombre_rol_despues() {
		return nombre_rol_despues;
	}
	public void setNombre_rol_despues(String nombre_rol_despues) {
		this.nombre_rol_despues = nombre_rol_despues;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Date getPermanencia() {
		return permanencia;
	}
	public void setPermanencia(Date permanencia) {
		this.permanencia = permanencia;
	}
	public int getRut_rol_antes() {
		return rut_rol_antes;
	}
	public void setRut_rol_antes(int rut_rol_antes) {
		this.rut_rol_antes = rut_rol_antes;
	}
	public String getResponsable_anterior() {
		return responsable_anterior;
	}
	public void setResponsable_anterior(String responsable_anterior) {
		this.responsable_anterior = responsable_anterior;
	}
	public int getRut_rol_despues() {
		return rut_rol_despues;
	}
	public void setRut_rol_despues(int rut_rol_despues) {
		this.rut_rol_despues = rut_rol_despues;
	}
	public String getResponsable_actual() {
		return responsable_actual;
	}
	public void setResponsable_actual(String responsable_actual) {
		this.responsable_actual = responsable_actual;
	}
	public int getId_motivo_rechazo() {
		return id_motivo_rechazo;
	}
	public void setId_motivo_rechazo(int id_motivo_rechazo) {
		this.id_motivo_rechazo = id_motivo_rechazo;
	}
	public int getId_estado() {
		return id_estado;
	}
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	public String getConnect_wf_context() {
		return connect_wf_context;
	}
	public void setConnect_wf_context(String connect_wf_context) {
		this.connect_wf_context = connect_wf_context;
	}
	public int getConnect_wf_id_req_remoto() {
		return connect_wf_id_req_remoto;
	}
	public void setConnect_wf_id_req_remoto(int connect_wf_id_req_remoto) {
		this.connect_wf_id_req_remoto = connect_wf_id_req_remoto;
	}
	public int getConnect_wf_actual_transaccion_id_remoto() {
		return connect_wf_actual_transaccion_id_remoto;
	}
	public void setConnect_wf_actual_transaccion_id_remoto(int connect_wf_actual_transaccion_id_remoto) {
		this.connect_wf_actual_transaccion_id_remoto = connect_wf_actual_transaccion_id_remoto;
	}
	public String getConnect_wf_direccion() {
		return connect_wf_direccion;
	}
	public void setConnect_wf_direccion(String connect_wf_direccion) {
		this.connect_wf_direccion = connect_wf_direccion;
	}
 
	
	
	
	
}
