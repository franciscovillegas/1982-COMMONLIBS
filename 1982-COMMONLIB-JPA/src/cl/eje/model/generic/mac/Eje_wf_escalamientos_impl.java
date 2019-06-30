package cl.eje.model.generic.mac;

import java.util.Collection;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_impl", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_impl")
@TableReferences({ 
	@TableReference(field = "voPersonas"			, fk = @ForeignKeyReference(fk = "id_escalamiento_impl", otherTableField = "id_escalamiento_impl"), voClass = Eje_wf_escalamientos_personas.class), 
	@TableReference(field = "voPersonaExcepcion"	, fk = @ForeignKeyReference(fk = "id_escalamiento_impl", otherTableField = "id_escalamiento_impl"), voClass = Eje_wf_escalamientos_persona_excepcion.class),
	@TableReference(field = "voUnidadExcepcion"		, fk = @ForeignKeyReference(fk = "id_escalamiento_impl", otherTableField = "id_escalamiento_impl"), voClass = Eje_wf_escalamientos_unidad_excepcion.class),
	@TableReference(field = "voCargos"				, fk = @ForeignKeyReference(fk = "id_escalamiento_impl", otherTableField = "id_escalamiento_impl"), voClass = Eje_wf_escalamientos_secuencia_cargos.class),
	@TableReference(field = "voClase"				, fk = @ForeignKeyReference(fk = "id_esclamiento_clase", otherTableField = "id_esclamiento_clase"), voClass = Eje_wf_escalamientos_clases.class),
	@TableReference(field = "voAccionAdicional"		, fk = @ForeignKeyReference(fk = "id_escalamiento_accion_adicional", otherTableField = "id_escalamiento_accion_adicional"), voClass = Eje_wf_escalamientos_accion_adicional.class)
})
public class Eje_wf_escalamientos_impl  extends Vo {
	private Collection<Eje_wf_escalamientos_personas> voPersonas;
	private Collection<Eje_wf_escalamientos_persona_excepcion> voPersonaExcepcion;
	private Collection<Eje_wf_escalamientos_unidad_excepcion> voUnidadExcepcion;
	private Collection<Eje_wf_escalamientos_secuencia_cargos> voCargos;
	private Collection<Eje_wf_escalamientos_clases> voClase;
	private Collection<Eje_wf_escalamientos_accion_adicional> voAccionAdicional;
	
	private Integer id_escalamiento_impl;
	private Integer id_escalamiento;
	private String nombre;
	private String clase;
	private String path_sencha_conf;
	private String path_sencha_confmail;
	private Integer width;
	private Integer height;
	private boolean mantener_asignacion_anterior;
	private boolean asigna_rut_discreto;
	private boolean asciende_busca_jefe;
	private Integer jefe_final_derivacion;
	private boolean asciende_busca_cargo;
	private boolean vigente_clase_asigna;
	private Integer id_esclamiento_clase;
	private boolean vigente_clase_evento_adicional;
	private Integer id_escalamiento_accion_adicional;
	private boolean vigente;
	
	public Collection<Eje_wf_escalamientos_personas> getVoPersonas() {
		return voPersonas;
	}
	public void setVoPersonas(Collection<Eje_wf_escalamientos_personas> voPersonas) {
		this.voPersonas = voPersonas;
	}
	public Collection<Eje_wf_escalamientos_persona_excepcion> getVoPersonaExcepcion() {
		return voPersonaExcepcion;
	}
	public void setVoPersonaExcepcion(Collection<Eje_wf_escalamientos_persona_excepcion> voPersonaExcepcion) {
		this.voPersonaExcepcion = voPersonaExcepcion;
	}
	public Collection<Eje_wf_escalamientos_unidad_excepcion> getVoUnidadExcepcion() {
		return voUnidadExcepcion;
	}
	public void setVoUnidadExcepcion(Collection<Eje_wf_escalamientos_unidad_excepcion> voUnidadExcepcion) {
		this.voUnidadExcepcion = voUnidadExcepcion;
	}
	public Collection<Eje_wf_escalamientos_secuencia_cargos> getVoCargos() {
		return voCargos;
	}
	public void setVoCargos(Collection<Eje_wf_escalamientos_secuencia_cargos> voCargos) {
		this.voCargos = voCargos;
	}
	public Collection<Eje_wf_escalamientos_clases> getVoClase() {
		return voClase;
	}
	public void setVoClase(Collection<Eje_wf_escalamientos_clases> voClase) {
		this.voClase = voClase;
	}
	public Collection<Eje_wf_escalamientos_accion_adicional> getVoAccionAdicional() {
		return voAccionAdicional;
	}
	public void setVoAccionAdicional(Collection<Eje_wf_escalamientos_accion_adicional> voAccionAdicional) {
		this.voAccionAdicional = voAccionAdicional;
	}
	public Integer getId_escalamiento_impl() {
		return id_escalamiento_impl;
	}
	public void setId_escalamiento_impl(Integer id_escalamiento_impl) {
		this.id_escalamiento_impl = id_escalamiento_impl;
	}
	public Integer getId_escalamiento() {
		return id_escalamiento;
	}
	public void setId_escalamiento(Integer id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getPath_sencha_conf() {
		return path_sencha_conf;
	}
	public void setPath_sencha_conf(String path_sencha_conf) {
		this.path_sencha_conf = path_sencha_conf;
	}
	public String getPath_sencha_confmail() {
		return path_sencha_confmail;
	}
	public void setPath_sencha_confmail(String path_sencha_confmail) {
		this.path_sencha_confmail = path_sencha_confmail;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public boolean isMantener_asignacion_anterior() {
		return mantener_asignacion_anterior;
	}
	public void setMantener_asignacion_anterior(boolean mantener_asignacion_anterior) {
		this.mantener_asignacion_anterior = mantener_asignacion_anterior;
	}
	public boolean isAsigna_rut_discreto() {
		return asigna_rut_discreto;
	}
	public void setAsigna_rut_discreto(boolean asigna_rut_discreto) {
		this.asigna_rut_discreto = asigna_rut_discreto;
	}
	public boolean isAsciende_busca_jefe() {
		return asciende_busca_jefe;
	}
	public void setAsciende_busca_jefe(boolean asciende_busca_jefe) {
		this.asciende_busca_jefe = asciende_busca_jefe;
	}
	public Integer getJefe_final_derivacion() {
		return jefe_final_derivacion;
	}
	public void setJefe_final_derivacion(Integer jefe_final_derivacion) {
		this.jefe_final_derivacion = jefe_final_derivacion;
	}
	public boolean isAsciende_busca_cargo() {
		return asciende_busca_cargo;
	}
	public void setAsciende_busca_cargo(boolean asciende_busca_cargo) {
		this.asciende_busca_cargo = asciende_busca_cargo;
	}
	public boolean isVigente_clase_asigna() {
		return vigente_clase_asigna;
	}
	public void setVigente_clase_asigna(boolean vigente_clase_asigna) {
		this.vigente_clase_asigna = vigente_clase_asigna;
	}
	public Integer getId_esclamiento_clase() {
		return id_esclamiento_clase;
	}
	public void setId_esclamiento_clase(Integer id_esclamiento_clase) {
		this.id_esclamiento_clase = id_esclamiento_clase;
	}
	public boolean isVigente_clase_evento_adicional() {
		return vigente_clase_evento_adicional;
	}
	public void setVigente_clase_evento_adicional(boolean vigente_clase_evento_adicional) {
		this.vigente_clase_evento_adicional = vigente_clase_evento_adicional;
	}
	public Integer getId_escalamiento_accion_adicional() {
		return id_escalamiento_accion_adicional;
	}
	public void setId_escalamiento_accion_adicional(Integer id_escalamiento_accion_adicional) {
		this.id_escalamiento_accion_adicional = id_escalamiento_accion_adicional;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
	 
		
}
