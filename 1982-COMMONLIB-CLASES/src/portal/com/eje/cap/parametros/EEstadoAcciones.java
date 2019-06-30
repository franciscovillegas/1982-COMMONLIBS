package portal.com.eje.cap.parametros;

import java.lang.reflect.Field;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.cap.parametros.CapParametro;
import portal.com.eje.cap.parametros.CapParametro.CapParametroGrupo;
import portal.com.eje.cap.parametros.ifaces.ICapParamNemoteable;
import portal.com.eje.tools.EnumTool;

public enum EEstadoAcciones implements ICapParamNemoteable {
	
	/*
	ABIERTA("abierta"),
	NODEFINIDA("nodefinida")
	*/
	Planificada("planificada"),
	CierreDeProfesor("cierre_de_profesor"),
	CierrePorEstimacionDeCosto("cierre_por_est_costo"),
	CierreFinal("cierre_final"),
	Eliminado("eliminado"),
	AprobadaParaEjecucion("aprobada_para_ejecucion");
	
	private String nemo;
	
	EEstadoAcciones(String nemo) {
		this.nemo = nemo;
	}
	
	public String getNemo() {
		return nemo;
	}
	
	/**
	 * Obtiene el valor desde la BD
	 * @author Pancho
	 * @since 23-05-2019
	 * */
	public int getId() {
		return CapParametro.getIdParametro(CapParametroGrupo.estadoAcciones, nemo);
	}
	
	public static EEstadoAcciones getFromAccion(int idAccion) {
		return CapParametroToolCached.getInstance().getEstadoFromIdAccion(idAccion);
	}
	
	public static EEstadoAcciones getFromIdParametro(int idParametro) {
		
		EEstadoAcciones retorno = null;
		
		for(EEstadoAcciones estado : EnumTool.getListEnums(EEstadoAcciones.class)) {
			if(idParametro == CapParametroToolCached.getInstance().getIdParametro(CapParametroGrupo.estadoAcciones, estado.getNemo())) {
				retorno = estado;
				break;
			}
		}
		
		return retorno;
	}

	@Override
	public String getNemotenico() {
		return this.nemo;
	}
}
