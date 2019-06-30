package portal.com.eje.portal.parametro;

import portal.com.eje.portal.EModulos;

public interface IParametroListener {
	
	public void onNewParametro(EModulos modulo, String key);
	
	public void onNewParametroValue(int idCliente, ParametroKey key, ParametroValue value);
	
	public void onEditParametroValue(int idCliente, ParametroKey key, ParametroValue newValue);
	
	public void onDeleteParametroValue(int idCliente, ParametroKey key, String valueKey);
	
}
