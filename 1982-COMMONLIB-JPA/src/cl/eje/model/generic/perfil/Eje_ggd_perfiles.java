package cl.eje.model.generic.perfil;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;

@TableDefinition(jndi = "ggd", pks = { 
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_perfil", isForeignKey = false, numerica = true)
}, tableName = "Eje_ggd_perfiles")
public class Eje_ggd_perfiles {
	private int id_perfil;
	private int id_proceso;
	private String perfil;
	private int pond_auto;
	private int pond_superv;
	private int pond_subord;
	private int pond_pares;
	private int pond_global_competencia;
	private int pond_global_metas;
	private String modo_nota;
	private String personalizada_nota_competencias;
	private String personalizada_nota_metas;
	private String personalizada_nota_total;
	private String personalizada_nota_bono;
	private Boolean inc_bono;
	private String vigente;
	
	
	public int getId_perfil() {
		return id_perfil;
	}
	public void setId_perfil(int id_perfil) {
		this.id_perfil = id_perfil;
	}
	public int getId_proceso() {
		return id_proceso;
	}
	public void setId_proceso(int id_proceso) {
		this.id_proceso = id_proceso;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public int getPond_auto() {
		return pond_auto;
	}
	public void setPond_auto(int pond_auto) {
		this.pond_auto = pond_auto;
	}
	public int getPond_superv() {
		return pond_superv;
	}
	public void setPond_superv(int pond_superv) {
		this.pond_superv = pond_superv;
	}
	public int getPond_subord() {
		return pond_subord;
	}
	public void setPond_subord(int pond_subord) {
		this.pond_subord = pond_subord;
	}
	public int getPond_pares() {
		return pond_pares;
	}
	public void setPond_pares(int pond_pares) {
		this.pond_pares = pond_pares;
	}
	public int getPond_global_competencia() {
		return pond_global_competencia;
	}
	public void setPond_global_competencia(int pond_global_competencia) {
		this.pond_global_competencia = pond_global_competencia;
	}
	public int getPond_global_metas() {
		return pond_global_metas;
	}
	public void setPond_global_metas(int pond_global_metas) {
		this.pond_global_metas = pond_global_metas;
	}
	public String getModo_nota() {
		return modo_nota;
	}
	public void setModo_nota(String modo_nota) {
		this.modo_nota = modo_nota;
	}
	public String getPersonalizada_nota_competencias() {
		return personalizada_nota_competencias;
	}
	public void setPersonalizada_nota_competencias(String personalizada_nota_competencias) {
		this.personalizada_nota_competencias = personalizada_nota_competencias;
	}
	public String getPersonalizada_nota_metas() {
		return personalizada_nota_metas;
	}
	public void setPersonalizada_nota_metas(String personalizada_nota_metas) {
		this.personalizada_nota_metas = personalizada_nota_metas;
	}
	public String getPersonalizada_nota_total() {
		return personalizada_nota_total;
	}
	public void setPersonalizada_nota_total(String personalizada_nota_total) {
		this.personalizada_nota_total = personalizada_nota_total;
	}
	public String getPersonalizada_nota_bono() {
		return personalizada_nota_bono;
	}
	public void setPersonalizada_nota_bono(String personalizada_nota_bono) {
		this.personalizada_nota_bono = personalizada_nota_bono;
	}
	public Boolean getInc_bono() {
		return inc_bono;
	}
	public void setInc_bono(Boolean inc_bono) {
		this.inc_bono = inc_bono;
	}
	public String getVigente() {
		return vigente;
	}
	public void setVigente(String vigente) {
		this.vigente = vigente;
	}
	
	
	
	
}
