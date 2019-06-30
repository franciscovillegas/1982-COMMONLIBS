package portal.com.eje.portal.liquidacion;

import portal.com.eje.portal.vo.vo.Vo;

public class VoPeriodoMensual extends Vo {
	private int peri_id;
	private int peri_ano;
	private int peri_mes;
	private Double peri_utm;
	private Double peri_uf;
	public int getPeri_id() {
		return peri_id;
	}
	public void setPeri_id(int peri_id) {
		this.peri_id = peri_id;
	}
	public int getPeri_ano() {
		return peri_ano;
	}
	public void setPeri_ano(int peri_ano) {
		this.peri_ano = peri_ano;
	}
	public int getPeri_mes() {
		return peri_mes;
	}
	public void setPeri_mes(int peri_mes) {
		this.peri_mes = peri_mes;
	}
	public Double getPeri_utm() {
		return peri_utm;
	}
	public void setPeri_utm(Double peri_utm) {
		this.peri_utm = peri_utm;
	}
	public Double getPeri_uf() {
		return peri_uf;
	}
	public void setPeri_uf(Double peri_uf) {
		this.peri_uf = peri_uf;
	}
	
	
	@Override
	public String toString() {
		return new StringBuilder().append(peri_id)
				.append("  utm:").append(this.peri_utm)
				.append("  uf:").append(peri_uf).toString();
	}
	
}
