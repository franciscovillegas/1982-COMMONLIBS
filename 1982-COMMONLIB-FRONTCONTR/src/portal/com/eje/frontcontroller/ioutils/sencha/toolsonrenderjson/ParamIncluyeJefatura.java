package portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.sencha.util.AbsOutComponent;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.trabajador.TrabajadorInfoLocator;

public class ParamIncluyeJefatura extends AbsOutComponent {
	public final String PARAM_JEFATURA = "incluye_jefatura";

	public static ParamIncluyeJefatura getIntance() {
		return Weak.getInstance(ParamIncluyeJefatura.class);
	}

	@Override
	public void setUtilidad(ConsultaData dataToReturn, IOClaseWeb io) {
		if (io.getParamBoolean(PARAM_JEFATURA)) {
			TrabajadorInfoLocator.getTool().addJefatura(dataToReturn);
		}
	}
}
