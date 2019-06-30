package portal.com.eje.cap.parametros.ifaces;

import java.util.Collection;

import cl.eje.model.generic.cap.Eje_cap_parametro;
import portal.com.eje.cap.parametros.CapParametro.CapParametroGrupo;
import portal.com.eje.cap.parametros.EEstadoAcciones;

public interface ICapParametroToolCached {

	Integer getIdParametro(CapParametroGrupo grupo, String nemo);

	Integer getIdParametroNoVigente();

	Collection<Eje_cap_parametro> getParametros(CapParametroGrupo grupo, String value, boolean vigente);

	Integer getIdParametroVigente();

	EEstadoAcciones getEstadoFromIdAccion(int idAccion);

}