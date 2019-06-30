package portal.com.eje.tools.sence;

import java.util.Collection;

import org.springframework.util.Assert;

import portal.com.eje.portal.vo.VoMathTool;
import portal.com.eje.tools.sence.vo.SencePersonaTramoVo;

public class SenceCursoPersona {
	private Collection<SencePersonaTramoVo> personas;
	private int costoEmpresa;
	private int costoSence;
	private int costoCurso;
	
	public SenceCursoPersona(Collection<SencePersonaTramoVo> personas, int costoCurso) {
		super();
		this.personas = personas;
		this.costoCurso = costoCurso;
		
		Assert.notNull(personas, "Las personas no pueden ser null");
		calculaTotales();
	}
	
	private void calculaTotales() {
		int costoSence = 0;
		
		for(SencePersonaTramoVo p : personas) {
			costoSence += (p.getTramo().getFactorAporteSence() * costoCurso);
		}
		
		this.costoSence = costoSence;
		this.costoEmpresa = costoCurso - costoSence;
	}

	public Collection<SencePersonaTramoVo> getPersonas() {
		return personas;
	}

	public int getCostoEmpresa() {
		return costoEmpresa;
	}

	public int getCostoSence() {
		return costoSence;
	}

	public int getCostoCurso() {
		return costoCurso;
	}
	
	
}
