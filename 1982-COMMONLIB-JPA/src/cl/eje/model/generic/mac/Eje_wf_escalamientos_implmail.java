package cl.eje.model.generic.mac;

import java.util.Collection;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_mail", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_implmail")
@TableReferences({ 
	@TableReference(field = "eje_wf_escalamientos_mail_dest"				, fk = @ForeignKeyReference(fk = "id_escalamiento_mail", otherTableField = "id_escalamiento_mail"), voClass = Eje_wf_escalamientos_mail_dest.class),
	@TableReference(field = "enviar_correo_nuevas_asignaciones_idmail_list"	, fk = @ForeignKeyReference(fk = "enviar_correo_nuevas_asignaciones_idmail"	, otherTableField = "id_mail"), voClass = Eje_wf_tupla_mail.class), 
	@TableReference(field = "enviar_correo_al_rol_actual_idmail_list"		, fk = @ForeignKeyReference(fk = "enviar_correo_al_rol_actual_idmail"		, otherTableField = "id_mail"), voClass = Eje_wf_tupla_mail.class),
	@TableReference(field = "enviar_correo_responsable_idmail_list"			, fk = @ForeignKeyReference(fk = "enviar_correo_responsable_idmail"			, otherTableField = "id_mail"), voClass = Eje_wf_tupla_mail.class),
	@TableReference(field = "enviar_correo_a_jefe_idmail_list"				, fk = @ForeignKeyReference(fk = "enviar_correo_a_jefe_idmail"				, otherTableField = "id_mail"), voClass = Eje_wf_tupla_mail.class),
	@TableReference(field = "enviar_correo_a_otras_idmail_list"				, fk = @ForeignKeyReference(fk = "enviar_correo_a_otras_idmail"				, otherTableField = "id_mail"), voClass = Eje_wf_tupla_mail.class) })
public class Eje_wf_escalamientos_implmail extends Vo {
	private Collection<Eje_wf_escalamientos_mail_dest> eje_wf_escalamientos_mail_dest;
	private Eje_wf_tupla_mail	enviar_correo_nuevas_asignaciones_idmail_list;
	private Eje_wf_tupla_mail	enviar_correo_al_rol_actual_idmail_list;
	private Eje_wf_tupla_mail	enviar_correo_responsable_idmail_list;
	private Eje_wf_tupla_mail	enviar_correo_a_jefe_idmail_list;
	private Eje_wf_tupla_mail	enviar_correo_a_otras_idmail_list;
	
	private int id_escalamiento_mail;
	private int id_escalamiento;
	private String clase;
	private boolean mail;
	private boolean e_mail;
	private boolean enviar_correo_nuevas_asignaciones;
	private boolean cancelar_si_asignacion_es_la_misma;
	private int enviar_correo_nuevas_asignaciones_idmail;
	private boolean enviar_correo_al_rol_actual;
	private int enviar_correo_al_rol_actual_idmail;
	private boolean enviar_correo_responsable;
	private int enviar_correo_responsable_idmail;
	private boolean enviar_correo_a_jefe;
	private int enviar_correo_a_jefe_idmail;
	private boolean enviar_correo_a_otras;
	private int enviar_correo_a_otras_idmail;
	
	
	public boolean isCancelar_si_asignacion_es_la_misma() {
		return cancelar_si_asignacion_es_la_misma;
	}
	public void setCancelar_si_asignacion_es_la_misma(boolean cancelar_si_asignacion_es_la_misma) {
		this.cancelar_si_asignacion_es_la_misma = cancelar_si_asignacion_es_la_misma;
	}
	public Collection<Eje_wf_escalamientos_mail_dest> getEje_wf_escalamientos_mail_dest() {
		return eje_wf_escalamientos_mail_dest;
	}
	public void setEje_wf_escalamientos_mail_dest(
			Collection<Eje_wf_escalamientos_mail_dest> eje_wf_escalamientos_mail_dest) {
		this.eje_wf_escalamientos_mail_dest = eje_wf_escalamientos_mail_dest;
	}
	public Eje_wf_tupla_mail getEnviar_correo_nuevas_asignaciones_idmail_list() {
		return enviar_correo_nuevas_asignaciones_idmail_list;
	}
	public void setEnviar_correo_nuevas_asignaciones_idmail_list(
			Eje_wf_tupla_mail enviar_correo_nuevas_asignaciones_idmail_list) {
		this.enviar_correo_nuevas_asignaciones_idmail_list = enviar_correo_nuevas_asignaciones_idmail_list;
	}
	public Eje_wf_tupla_mail getEnviar_correo_al_rol_actual_idmail_list() {
		return enviar_correo_al_rol_actual_idmail_list;
	}
	public void setEnviar_correo_al_rol_actual_idmail_list(Eje_wf_tupla_mail enviar_correo_al_rol_actual_idmail_list) {
		this.enviar_correo_al_rol_actual_idmail_list = enviar_correo_al_rol_actual_idmail_list;
	}
	public Eje_wf_tupla_mail getEnviar_correo_responsable_idmail_list() {
		return enviar_correo_responsable_idmail_list;
	}
	public void setEnviar_correo_responsable_idmail_list(Eje_wf_tupla_mail enviar_correo_responsable_idmail_list) {
		this.enviar_correo_responsable_idmail_list = enviar_correo_responsable_idmail_list;
	}
	public Eje_wf_tupla_mail getEnviar_correo_a_jefe_idmail_list() {
		return enviar_correo_a_jefe_idmail_list;
	}
	public void setEnviar_correo_a_jefe_idmail_list(Eje_wf_tupla_mail enviar_correo_a_jefe_idmail_list) {
		this.enviar_correo_a_jefe_idmail_list = enviar_correo_a_jefe_idmail_list;
	}
	public Eje_wf_tupla_mail getEnviar_correo_a_otras_idmail_list() {
		return enviar_correo_a_otras_idmail_list;
	}
	public void setEnviar_correo_a_otras_idmail_list(Eje_wf_tupla_mail enviar_correo_a_otras_idmail_list) {
		this.enviar_correo_a_otras_idmail_list = enviar_correo_a_otras_idmail_list;
	}
	public int getId_escalamiento_mail() {
		return id_escalamiento_mail;
	}
	public void setId_escalamiento_mail(int id_escalamiento_mail) {
		this.id_escalamiento_mail = id_escalamiento_mail;
	}
	public int getId_escalamiento() {
		return id_escalamiento;
	}
	public void setId_escalamiento(int id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public boolean isMail() {
		return mail;
	}
	public void setMail(boolean mail) {
		this.mail = mail;
	}
	public boolean isE_mail() {
		return e_mail;
	}
	public void setE_mail(boolean e_mail) {
		this.e_mail = e_mail;
	}
	public boolean isEnviar_correo_nuevas_asignaciones() {
		return enviar_correo_nuevas_asignaciones;
	}
	public void setEnviar_correo_nuevas_asignaciones(boolean enviar_correo_nuevas_asignaciones) {
		this.enviar_correo_nuevas_asignaciones = enviar_correo_nuevas_asignaciones;
	}
	public int getEnviar_correo_nuevas_asignaciones_idmail() {
		return enviar_correo_nuevas_asignaciones_idmail;
	}
	public void setEnviar_correo_nuevas_asignaciones_idmail(int enviar_correo_nuevas_asignaciones_idmail) {
		this.enviar_correo_nuevas_asignaciones_idmail = enviar_correo_nuevas_asignaciones_idmail;
	}
	public boolean isEnviar_correo_al_rol_actual() {
		return enviar_correo_al_rol_actual;
	}
	public void setEnviar_correo_al_rol_actual(boolean enviar_correo_al_rol_actual) {
		this.enviar_correo_al_rol_actual = enviar_correo_al_rol_actual;
	}
	public int getEnviar_correo_al_rol_actual_idmail() {
		return enviar_correo_al_rol_actual_idmail;
	}
	public void setEnviar_correo_al_rol_actual_idmail(int enviar_correo_al_rol_actual_idmail) {
		this.enviar_correo_al_rol_actual_idmail = enviar_correo_al_rol_actual_idmail;
	}
	public boolean isEnviar_correo_responsable() {
		return enviar_correo_responsable;
	}
	public void setEnviar_correo_responsable(boolean enviar_correo_responsable) {
		this.enviar_correo_responsable = enviar_correo_responsable;
	}
	public int getEnviar_correo_responsable_idmail() {
		return enviar_correo_responsable_idmail;
	}
	public void setEnviar_correo_responsable_idmail(int enviar_correo_responsable_idmail) {
		this.enviar_correo_responsable_idmail = enviar_correo_responsable_idmail;
	}
	public boolean isEnviar_correo_a_jefe() {
		return enviar_correo_a_jefe;
	}
	public void setEnviar_correo_a_jefe(boolean enviar_correo_a_jefe) {
		this.enviar_correo_a_jefe = enviar_correo_a_jefe;
	}
	public int getEnviar_correo_a_jefe_idmail() {
		return enviar_correo_a_jefe_idmail;
	}
	public void setEnviar_correo_a_jefe_idmail(int enviar_correo_a_jefe_idmail) {
		this.enviar_correo_a_jefe_idmail = enviar_correo_a_jefe_idmail;
	}
	public boolean isEnviar_correo_a_otras() {
		return enviar_correo_a_otras;
	}
	public void setEnviar_correo_a_otras(boolean enviar_correo_a_otras) {
		this.enviar_correo_a_otras = enviar_correo_a_otras;
	}
	public int getEnviar_correo_a_otras_idmail() {
		return enviar_correo_a_otras_idmail;
	}
	public void setEnviar_correo_a_otras_idmail(int enviar_correo_a_otras_idmail) {
		this.enviar_correo_a_otras_idmail = enviar_correo_a_otras_idmail;
	}
	
	
	 

}
