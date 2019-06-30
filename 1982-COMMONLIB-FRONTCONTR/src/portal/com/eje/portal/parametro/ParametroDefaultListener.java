package portal.com.eje.portal.parametro;

import portal.com.eje.portal.EModulos;

public class ParametroDefaultListener implements IParametroListener {
	
	@Override
	public void onNewParametro(EModulos modulo, String key) {
		ParametroLocator.getInstance().clear();
	}

	@Override
	public void onNewParametroValue(int idCliente, ParametroKey key, ParametroValue value) {
		ParametroLocator.getInstance().clear();
	}

	@Override
	public void onEditParametroValue(int idCliente, ParametroKey key, ParametroValue newValue) {
		ParametroLocator.getInstance().clear();
	}

	@Override
	public void onDeleteParametroValue(int idCliente, ParametroKey key, String keyValue) {
		ParametroLocator.getInstance().clear();

	}

}
