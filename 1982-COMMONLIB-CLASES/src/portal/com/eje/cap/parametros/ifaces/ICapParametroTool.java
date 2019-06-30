package portal.com.eje.cap.parametros.ifaces;

import java.sql.SQLException;
import java.util.Collection;

import cl.eje.model.generic.cap.Eje_cap_parametro;
import portal.com.eje.cap.parametros.CapParametro.CapParametroGrupo;
import portal.com.eje.cap.parametros.CapParametro.CapParametroSubgrupo;
import portal.com.eje.cap.parametros.EEstadoAcciones;

public interface ICapParametroTool {

	boolean addParametro(CapParametroGrupo grupo, CapParametroSubgrupo sGrupo, String param);

	Collection<Eje_cap_parametro> getParametros(CapParametroGrupo grupo, String value, boolean vigente);

	Integer getIdParametroVigente();

	Integer getIdParametroNoVigente();

	Integer getIdParametro(CapParametroGrupo grupo, String nemo) throws SQLException;

	EEstadoAcciones getEstadoFromIdAccion(int idAccion);

}