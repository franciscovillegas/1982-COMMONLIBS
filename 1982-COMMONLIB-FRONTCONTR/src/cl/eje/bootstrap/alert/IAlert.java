package cl.eje.bootstrap.alert;

import cl.eje.bootstrap.ifacetemplatesetter.ITemplateSetter;

public interface IAlert extends ITemplateSetter  {


	public EnumAlertType getAlertType() ;

	public void setAlertType(EnumAlertType alertType);
}