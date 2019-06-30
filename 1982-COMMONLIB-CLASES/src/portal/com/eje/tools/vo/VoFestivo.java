package portal.com.eje.tools.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class VoFestivo extends Vo {
	private String fecha;
	private int festivo;
	
	public VoFestivo() {
		
	}
	
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getFestivo() {
		return festivo;
	}
	public void setFestivo(int festivo) {
		this.festivo = festivo;
	}
	
	
	
}