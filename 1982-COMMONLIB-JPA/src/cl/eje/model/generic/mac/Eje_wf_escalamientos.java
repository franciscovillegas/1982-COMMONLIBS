package cl.eje.model.generic.mac;

import java.util.Collection;
import java.util.Date;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos")
@TableReferences({
		@TableReference(field = "eje_wf_escalamientos_impl", fk = @ForeignKeyReference(fk = "id_escalamiento", otherTableField = "id_escalamiento"), voClass = Eje_wf_escalamientos_impl.class),
		@TableReference(field = "eje_wf_escalamientos_implmail", fk = @ForeignKeyReference(fk = "id_escalamiento", otherTableField = "id_escalamiento"), voClass = Eje_wf_escalamientos_implmail.class),
		@TableReference(field = "eje_wf_escalamientos_registro_maestro", fk = @ForeignKeyReference(fk = "id_escalamiento", otherTableField = "id_escalamiento"), voClass = Eje_wf_escalamientos_registro_maestro.class) })
public class Eje_wf_escalamientos extends Vo {
	private Collection<Eje_wf_escalamientos_impl> eje_wf_escalamientos_impl;
	private Collection<Eje_wf_escalamientos_implmail> eje_wf_escalamientos_implmail;
	private Collection<Eje_wf_escalamientos_registro_maestro> eje_wf_escalamientos_registro_maestro;

	private int id_escalamiento;
	private int id_tipolimite;
	private int id_escalamiento_secuencia_id;
	private int id_escalamiento_padre;
	private String nombre;
	private Date fecha_update;
	private int id_rol;
	private double horas_antes;
	private int orden;
	private boolean es_horario_habil;
	private int cant_escalamientos;
	private boolean accion_es_asignacion;
	private boolean accion_es_envio_mail;
	private boolean con_iteraciones;
	private double iteraciones_despues_de_horas;
	private int nivel;
	private boolean vigente;

	public Collection<Eje_wf_escalamientos_impl> getEje_wf_escalamientos_impl() {
		return eje_wf_escalamientos_impl;
	}

	public void setEje_wf_escalamientos_impl(Collection<Eje_wf_escalamientos_impl> eje_wf_escalamientos_impl) {
		this.eje_wf_escalamientos_impl = eje_wf_escalamientos_impl;
	}

	public Collection<Eje_wf_escalamientos_implmail> getEje_wf_escalamientos_implmail() {
		return eje_wf_escalamientos_implmail;
	}

	public void setEje_wf_escalamientos_implmail(
			Collection<Eje_wf_escalamientos_implmail> eje_wf_escalamientos_implmail) {
		this.eje_wf_escalamientos_implmail = eje_wf_escalamientos_implmail;
	}

	public Collection<Eje_wf_escalamientos_registro_maestro> getEje_wf_escalamientos_registro_maestro() {
		return eje_wf_escalamientos_registro_maestro;
	}

	public void setEje_wf_escalamientos_registro_maestro(
			Collection<Eje_wf_escalamientos_registro_maestro> eje_wf_escalamientos_registro_maestro) {
		this.eje_wf_escalamientos_registro_maestro = eje_wf_escalamientos_registro_maestro;
	}

	public int getId_escalamiento() {
		return id_escalamiento;
	}

	public void setId_escalamiento(int id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}

	public int getId_tipolimite() {
		return id_tipolimite;
	}

	public void setId_tipolimite(int id_tipolimite) {
		this.id_tipolimite = id_tipolimite;
	}

	public int getId_escalamiento_secuencia_id() {
		return id_escalamiento_secuencia_id;
	}

	public void setId_escalamiento_secuencia_id(int id_escalamiento_secuencia_id) {
		this.id_escalamiento_secuencia_id = id_escalamiento_secuencia_id;
	}

	public int getId_escalamiento_padre() {
		return id_escalamiento_padre;
	}

	public void setId_escalamiento_padre(int id_escalamiento_padre) {
		this.id_escalamiento_padre = id_escalamiento_padre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}

	public int getId_rol() {
		return id_rol;
	}

	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}

	public double getHoras_antes() {
		return horas_antes;
	}

	public void setHoras_antes(double horas_antes) {
		this.horas_antes = horas_antes;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public boolean isEs_horario_habil() {
		return es_horario_habil;
	}

	public void setEs_horario_habil(boolean es_horario_habil) {
		this.es_horario_habil = es_horario_habil;
	}

	public int getCant_escalamientos() {
		return cant_escalamientos;
	}

	public void setCant_escalamientos(int cant_escalamientos) {
		this.cant_escalamientos = cant_escalamientos;
	}

	public boolean isAccion_es_asignacion() {
		return accion_es_asignacion;
	}

	public void setAccion_es_asignacion(boolean accion_es_asignacion) {
		this.accion_es_asignacion = accion_es_asignacion;
	}

	public boolean isAccion_es_envio_mail() {
		return accion_es_envio_mail;
	}

	public void setAccion_es_envio_mail(boolean accion_es_envio_mail) {
		this.accion_es_envio_mail = accion_es_envio_mail;
	}

	public boolean isVigente() {
		return vigente;
	}

	public boolean isCon_iteraciones() {
		return con_iteraciones;
	}

	public void setCon_iteraciones(boolean con_iteraciones) {
		this.con_iteraciones = con_iteraciones;
	}

	public double getIteraciones_despues_de_horas() {
		return iteraciones_despues_de_horas;
	}

	public void setIteraciones_despues_de_horas(double iteraciones_despues_de_horas) {
		this.iteraciones_despues_de_horas = iteraciones_despues_de_horas;
	}
	
	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}

}
