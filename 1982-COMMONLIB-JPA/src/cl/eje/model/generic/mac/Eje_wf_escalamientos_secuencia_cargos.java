package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_cargos", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_secuencia_cargos")
public class Eje_wf_escalamientos_secuencia_cargos extends Vo {
	
	private int	id_escalamiento_impl;
	private int	id_escalamiento_cargos;
	private String descrip;
	private int	horas;
	private boolean	vigente;
	
	public int getId_escalamiento_impl() {
		return id_escalamiento_impl;
	}
	public void setId_escalamiento_impl(int id_escalamiento_impl) {
		this.id_escalamiento_impl = id_escalamiento_impl;
	}
	public int getId_escalamiento_cargos() {
		return id_escalamiento_cargos;
	}
	public void setId_escalamiento_cargos(int id_escalamiento_cargos) {
		this.id_escalamiento_cargos = id_escalamiento_cargos;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public int getHoras() {
		return horas;
	}
	public void setHoras(int horas) {
		this.horas = horas;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
	 
	
	
}
