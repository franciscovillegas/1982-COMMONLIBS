package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "dia_semana_int", isForeignKey = true, numerica = true) }, tableName = "eje_ges_horariohabil")
public class Eje_ges_horariohabil extends Vo {
	private int	dia_semana_int;
	private String dia_semana_desc;
	private Date horario_entrada;
	private Date horario_salida;
	private boolean festivo;
	
	public int getDia_semana_int() {
		return dia_semana_int;
	}
	public void setDia_semana_int(int dia_semana_int) {
		this.dia_semana_int = dia_semana_int;
	}
	public String getDia_semana_desc() {
		return dia_semana_desc;
	}
	public void setDia_semana_desc(String dia_semana_desc) {
		this.dia_semana_desc = dia_semana_desc;
	}
	public Date getHorario_entrada() {
		return horario_entrada;
	}
	public void setHorario_entrada(Date horario_entrada) {
		this.horario_entrada = horario_entrada;
	}
	public Date getHorario_salida() {
		return horario_salida;
	}
	public void setHorario_salida(Date horario_salida) {
		this.horario_salida = horario_salida;
	}
	public boolean isFestivo() {
		return festivo;
	}
	public void setFestivo(boolean festivo) {
		this.festivo = festivo;
	}
	
	
}
