package portal.com.eje.cap.parametros;

import java.util.Collection;

import cl.eje.model.generic.cap.Eje_cap_parametro;
import portal.com.eje.cap.parametros.ifaces.ICapParamNemoteable;
import portal.com.eje.portal.factory.Util;

public class CapParametro {

	public enum CapParametroGrupo {
		cursos(50),
		areas(6),
		areasTematicas(7),
		competencias(8),
		areaCurso(11),
		calculos(12),
		costos(13),
		sence(14),
		vigencia(16),
		estado(17),
		estadoPlanificacion(18),
		acciones(19), //parece q que se ocupa para el workflow
		tiposDeCursos(9),
		origenes(10),
		regiones(20),
		comuna(21),
		ciudad(22),
		tipoJor(23),
		tipoAlcance(24),
		estadoAcciones(25),
		meses(26),
		genero(28),
		profesionrelator(29),
		estadorelator(30),
		ORIGEN_PRESUPUESTARIO(33),
		VIGENCIA_GENERICA(40);
		
		private final Integer id;
		
		private CapParametroGrupo(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
	
	public enum CapParametroSubgrupo {
		Defecto(1);
		
		private Integer id;
		
		CapParametroSubgrupo(int id) {
			this.id= id;
		}
		
		public Integer getId() {
			return this.id;
		}
	}
	
	public enum CapParametroDefaultValue {
		VIGENTE("vigente"),
		NO_VIGENTE("no vigente");
		
		private String value;

		private CapParametroDefaultValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	protected CapParametro() {
		
	}
	
	public static CapParametro getInstance() {
	 	return Util.getInstance(CapParametro.class);
	}
	
	public boolean addParametro(CapParametroGrupo grupo, CapParametroSubgrupo sGrupo, String param) {
		return CapParametroTool.getInstance().addParametro(grupo, sGrupo, param);
	}
	
	public static Collection<Eje_cap_parametro> getParametros(CapParametroGrupo grupo, String value, boolean vigente) {
		return CapParametroToolCached.getInstance().getParametros(grupo, value, vigente);
	}

	public static Integer getIdParametroVigente() {
		return CapParametroToolCached.getInstance().getIdParametroVigente();
	}
	
	public static Integer getIdParametroNoVigente() {
		 return CapParametroToolCached.getInstance().getIdParametroNoVigente();
	}

	public static Integer getIdParametro(CapParametroGrupo grupo, String nemo)  {
		 return CapParametroToolCached.getInstance().getIdParametro(grupo, nemo);
	 
	}
	
	public static Integer getIdParametro(CapParametroGrupo grupo, ICapParamNemoteable nemo)  {
		 return CapParametroToolCached.getInstance().getIdParametro(grupo, nemo.getNemotenico());
	 
	}
}
