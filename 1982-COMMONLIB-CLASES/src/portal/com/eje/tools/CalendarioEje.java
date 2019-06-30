package portal.com.eje.tools;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.vo.VoFestivo;

/**
 * Clase que representa las preguntas que se puedes responder a partir de la
 * tabla eje_ges_calendario
 * 
 * @author Pancho
 * @since 31-08-2018
 * 
 */
public class CalendarioEje {

	public static CalendarioEje getInstance() {
		return Util.getInstance(CalendarioEje.class);
	}

	/**
	 * Calcula una nueva fecha habil a partir de una fecha de inicio, dicho de otra
	 * forma, a la fecha de inicio se le suman los días de "diasAgregados" sean
	 * estos positivos o negativos y calcula la nueva fecha habil<br/>
	 * Si fechaInicio es un día festivo, se toma inicio el primera día habil hacia
	 * la dirección indicada por signo de diasAgregados<br/>
	 * 
	 * <br/>
	 * Este método responde las preguntas de: <br/>
	 * <ul>
	 * <li>"hasta @diasAgregados días hábiles a partir de @fechaInicio"</li>
	 * <li>"desde hace @diasAgregados días hábiles que puedes pedir licencias, osea
	 * desde @fechaInicio"</li>
	 * </ul>
	 * 
	 * 
	 * @author Pancho
	 * @since 31-08-2018
	 */
	public Date getFechaHabil(Date fechaInicio, int diasAgregados) {

		if (fechaInicio == null) {
			throw new NullPointerException("FechaInicio no puede ser null");
		}

		int fechaBase = cl.ejedigital.tool.validar.Validar.getInstance()
				.validarInt(cl.ejedigital.tool.misc.Formatear.getInstance().toDate(fechaInicio, "yyyyMMdd"), -1);
		int yearBase = fechaBase / 10000;
		String key = "getFechaHabil(" + fechaBase + "," + diasAgregados + ")";

		Calendar cal = (Calendar) WeakCacheLocator.getInstance(getClass()).get(key);

		if (cal == null) {
			cal = Calendar.getInstance();

			if (diasAgregados > 0) {
				cal = getFechaHabilInicio(fechaInicio, 1);
				for (int i = 0; i < diasAgregados; i++) {
					cal.add(Calendar.DAY_OF_MONTH, 1);

					cal = getFechaHabilInicio(cal.getTime(), 1);
				}

			} else if (diasAgregados < 0) {
				cal = getFechaHabilInicio(fechaInicio, -1);
				for (int i = 0; i > diasAgregados; i--) {
					cal.add(Calendar.DAY_OF_MONTH, -1);

					cal = getFechaHabilInicio(cal.getTime(), -1);
				}
			}

			WeakCacheLocator.getInstance(getClass()).put(key, cal);
		}

		return cal.getTime();
	}

	/**
	 * dirección > 0 si se busca el día habil siguiente (puede ser hoy) dirección <
	 * 0 si se busca el día habil hacia atrás (puede ser hoy)
	 * 
	 * @author Pancho
	 * @since 31-08-2018
	 */
	public Calendar getFechaHabilInicio(Date fechaInicio, int direccion) {
		if (fechaInicio == null) {
			throw new NullPointerException("FechaInicio no puede ser null");
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaInicio);

		Map<String, VoFestivo> festivos = getFestivos(fechaInicio);

		while (festivos.get(getAnsi(cal.getTime())) != null) {
			cal.add(Calendar.DAY_OF_MONTH, direccion > 0 ? 1 : -1);
		}

		return cal;
	}

	@SuppressWarnings("unchecked")
	private Map<String, VoFestivo> getFestivos(Date date) {
		int fechaBase = cl.ejedigital.tool.validar.Validar.getInstance()
				.validarInt(cl.ejedigital.tool.misc.Formatear.getInstance().toDate(date, "yyyyMMdd"), -1);
		int yearBase = fechaBase / 10000;
		String key = "getFestivos(" + fechaBase + ")";
		Map<String, VoFestivo> festivos = (Map<String, VoFestivo>) WeakCacheLocator.getInstance(getClass()).get(key);

		if (festivos == null) {
			festivos = new HashMap<String, VoFestivo>();

			StringBuilder sql = new StringBuilder();
			sql.append(" select fecha=convert(varchar(10), fecha, 112), festivo \n");
			sql.append(" from eje_ges_calendario \n");
			sql.append(
					" where year(fecha) = @@yearBase or year(fecha) = ((@@yearBase) +1 ) or year(fecha) = ((@@yearBase)-1) \n");
			sql.append(" and festivo = 1 \n");

			ConsultaData data = null;
			try {
				data = ConsultaTool.getInstance().getData("portal",
						sql.toString().replaceAll("@@yearBase", String.valueOf(yearBase)));
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (data != null) {
				Collection<VoFestivo> cols = VoTool.getInstance().buildVo(data, VoFestivo.class);
				festivos = VoTool.getInstance().getCollection(cols, HashMap.class, "fecha", String.class);

				Assert.isTrue(festivos != null, "No puede ser null");

				festivos.putAll(getFinesDeSemana(date));

				WeakCacheLocator.getInstance(getClass()).put(key, festivos);
			}
		}

		return festivos;

	}

	@SuppressWarnings("unchecked")
	private Map<String, VoFestivo> getFinesDeSemana(Date date) {
		int fechaBase = cl.ejedigital.tool.validar.Validar.getInstance()
				.validarInt(cl.ejedigital.tool.misc.Formatear.getInstance().toDate(date, "yyyyMMdd"), -1);
		int yearBase = fechaBase / 10000;
		String key = "getFinesDeSemana(" + fechaBase + ")";
		Map<String, VoFestivo> festivos = (Map<String, VoFestivo>) SoftCacheLocator.getInstance(getClass()).get(key);

		if (festivos == null) {
			festivos = new HashMap<String, VoFestivo>();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, yearBase - 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, 0);

			while (cal.get(Calendar.YEAR) == yearBase || cal.get(Calendar.YEAR) == (yearBase - 1)
					|| cal.get(Calendar.YEAR) == (yearBase + 1)) {

				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					String ansi = getAnsi(cal.getTime());
					VoFestivo vo = new VoFestivo();
					vo.setFecha(ansi);
					vo.setFestivo(1);

					festivos.put(ansi, vo);
				}

				cal.add(Calendar.DAY_OF_MONTH, 1);
			}

			SoftCacheLocator.getInstance(getClass()).put(key, festivos);
		}

		return festivos;
	}

	private String getAnsi(Date date) {
		return cl.ejedigital.tool.misc.Formatear.getInstance().toDate(date, "yyyyMMdd");
	}
}
