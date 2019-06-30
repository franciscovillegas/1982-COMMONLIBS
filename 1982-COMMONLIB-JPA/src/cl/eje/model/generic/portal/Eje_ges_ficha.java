package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "periodo", isForeignKey = true, numerica = true),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_empresa", isForeignKey = true, numerica = true),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_ficha", isForeignKey = true, numerica = true) }, tableName = "eje_ges_ficha")
public class Eje_ges_ficha extends Vo {
	private Integer periodo;
	private String id_empresa;
	private Integer id_ficha;
	private Integer id_persona;
	private Integer id_tabtrab;
	private Integer id_tabtrabh;
	private Integer id_tabliq;

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public String getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(String id_empresa) {
		this.id_empresa = id_empresa;
	}

	public Integer getId_ficha() {
		return id_ficha;
	}

	public void setId_ficha(Integer id_ficha) {
		this.id_ficha = id_ficha;
	}

	public Integer getId_persona() {
		return id_persona;
	}

	public void setId_persona(Integer id_persona) {
		this.id_persona = id_persona;
	}

	public Integer getId_tabtrab() {
		return id_tabtrab;
	}

	public void setId_tabtrab(Integer id_tabtrab) {
		this.id_tabtrab = id_tabtrab;
	}

	public Integer getId_tabtrabh() {
		return id_tabtrabh;
	}

	public void setId_tabtrabh(Integer id_tabtrabh) {
		this.id_tabtrabh = id_tabtrabh;
	}

	public Integer getId_tabliq() {
		return id_tabliq;
	}

	public void setId_tabliq(Integer id_tabliq) {
		this.id_tabliq = id_tabliq;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new StringBuilder()
					.append("id_persona:").append(this.getId_persona())
					.append("periodo:").append(this.getPeriodo())
					.append("id_ficha:").append(this.getId_ficha())
					.toString();
	}

}

