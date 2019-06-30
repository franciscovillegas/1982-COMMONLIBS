package cl.eje.model.generic.cap;

import java.util.Date;

import cl.eje.jndi.cap.CapJndi;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = CapJndi.jndiCap, pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_accion", isForeignKey = true, numerica = true) }, tableName = "eje_cap_acciones")
public class Eje_cap_acciones extends Vo {
	private int id_accion;
	private String nombre;
	private int participantes;
	private int cupos;
	private Date f_inicio;
	private Date f_fin;
	private Date f_tope;
	private int mes_factura;
	private int id_curso;
	private String nombre_curso;
	private int id_horario;
	private int periodo;
	private int estado;
	private int costo_hora;
	private int costo_hora_sence;
	private int costo_presupuestado;
	private int costo_real;
	private int costo_temporal;
	private int horas;
	private int creditos;
	private int id_plan;
	private int id_plan_curso;
	private String asistencia;
	private int con_aviso;
	private int aviso_tiempo;
	private int aviso_cantidad;
	private int con_wf;
	private Date inicioevaluacion;
	private Date iniciocalidad;
	private Date iniciorelatoria;
	private int id_curso_origen;
	private int id_f_efectividad;
	private int id_f_calidad;
	private int id_f_relatoria;;
	private int f_efectividad_dias;
	private int f_calidad_dias;
	private int f_relatoria_dias;
	private int id_otec;
	private String ciudad;
	private String direccion;
	private String horario;
	public int getId_accion() {
		return id_accion;
	}
	public void setId_accion(int id_accion) {
		this.id_accion = id_accion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getParticipantes() {
		return participantes;
	}
	public void setParticipantes(int participantes) {
		this.participantes = participantes;
	}
	public int getCupos() {
		return cupos;
	}
	public void setCupos(int cupos) {
		this.cupos = cupos;
	}
	public Date getF_inicio() {
		return f_inicio;
	}
	public void setF_inicio(Date f_inicio) {
		this.f_inicio = f_inicio;
	}
	public Date getF_fin() {
		return f_fin;
	}
	public void setF_fin(Date f_fin) {
		this.f_fin = f_fin;
	}
	public Date getF_tope() {
		return f_tope;
	}
	public void setF_tope(Date f_tope) {
		this.f_tope = f_tope;
	}
	public int getMes_factura() {
		return mes_factura;
	}
	public void setMes_factura(int mes_factura) {
		this.mes_factura = mes_factura;
	}
	public int getId_curso() {
		return id_curso;
	}
	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}
	public String getNombre_curso() {
		return nombre_curso;
	}
	public void setNombre_curso(String nombre_curso) {
		this.nombre_curso = nombre_curso;
	}
	public int getId_horario() {
		return id_horario;
	}
	public void setId_horario(int id_horario) {
		this.id_horario = id_horario;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getCosto_hora() {
		return costo_hora;
	}
	public void setCosto_hora(int costo_hora) {
		this.costo_hora = costo_hora;
	}
	public int getCosto_hora_sence() {
		return costo_hora_sence;
	}
	public void setCosto_hora_sence(int costo_hora_sence) {
		this.costo_hora_sence = costo_hora_sence;
	}
	public int getCosto_presupuestado() {
		return costo_presupuestado;
	}
	public void setCosto_presupuestado(int costo_presupuestado) {
		this.costo_presupuestado = costo_presupuestado;
	}
	public int getCosto_real() {
		return costo_real;
	}
	public void setCosto_real(int costo_real) {
		this.costo_real = costo_real;
	}
	public int getCosto_temporal() {
		return costo_temporal;
	}
	public void setCosto_temporal(int costo_temporal) {
		this.costo_temporal = costo_temporal;
	}
	public int getHoras() {
		return horas;
	}
	public void setHoras(int horas) {
		this.horas = horas;
	}
	public int getCreditos() {
		return creditos;
	}
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	public int getId_plan() {
		return id_plan;
	}
	public void setId_plan(int id_plan) {
		this.id_plan = id_plan;
	}
	public int getId_plan_curso() {
		return id_plan_curso;
	}
	public void setId_plan_curso(int id_plan_curso) {
		this.id_plan_curso = id_plan_curso;
	}
	public String getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}
	public int getCon_aviso() {
		return con_aviso;
	}
	public void setCon_aviso(int con_aviso) {
		this.con_aviso = con_aviso;
	}
	public int getAviso_tiempo() {
		return aviso_tiempo;
	}
	public void setAviso_tiempo(int aviso_tiempo) {
		this.aviso_tiempo = aviso_tiempo;
	}
	public int getAviso_cantidad() {
		return aviso_cantidad;
	}
	public void setAviso_cantidad(int aviso_cantidad) {
		this.aviso_cantidad = aviso_cantidad;
	}
	public int getCon_wf() {
		return con_wf;
	}
	public void setCon_wf(int con_wf) {
		this.con_wf = con_wf;
	}
	public Date getInicioevaluacion() {
		return inicioevaluacion;
	}
	public void setInicioevaluacion(Date inicioevaluacion) {
		this.inicioevaluacion = inicioevaluacion;
	}
	public Date getIniciocalidad() {
		return iniciocalidad;
	}
	public void setIniciocalidad(Date iniciocalidad) {
		this.iniciocalidad = iniciocalidad;
	}
	public Date getIniciorelatoria() {
		return iniciorelatoria;
	}
	public void setIniciorelatoria(Date iniciorelatoria) {
		this.iniciorelatoria = iniciorelatoria;
	}
	public int getId_curso_origen() {
		return id_curso_origen;
	}
	public void setId_curso_origen(int id_curso_origen) {
		this.id_curso_origen = id_curso_origen;
	}
	public int getId_f_efectividad() {
		return id_f_efectividad;
	}
	public void setId_f_efectividad(int id_f_efectividad) {
		this.id_f_efectividad = id_f_efectividad;
	}
	public int getId_f_calidad() {
		return id_f_calidad;
	}
	public void setId_f_calidad(int id_f_calidad) {
		this.id_f_calidad = id_f_calidad;
	}
	public int getId_f_relatoria() {
		return id_f_relatoria;
	}
	public void setId_f_relatoria(int id_f_relatoria) {
		this.id_f_relatoria = id_f_relatoria;
	}
	public int getF_efectividad_dias() {
		return f_efectividad_dias;
	}
	public void setF_efectividad_dias(int f_efectividad_dias) {
		this.f_efectividad_dias = f_efectividad_dias;
	}
	public int getF_calidad_dias() {
		return f_calidad_dias;
	}
	public void setF_calidad_dias(int f_calidad_dias) {
		this.f_calidad_dias = f_calidad_dias;
	}
	public int getF_relatoria_dias() {
		return f_relatoria_dias;
	}
	public void setF_relatoria_dias(int f_relatoria_dias) {
		this.f_relatoria_dias = f_relatoria_dias;
	}
	public int getId_otec() {
		return id_otec;
	}
	public void setId_otec(int id_otec) {
		this.id_otec = id_otec;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	
	
	
}
