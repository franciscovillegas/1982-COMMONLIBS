package portal.com.eje.portal.vo.iface;

public interface IVoValueChange {
	
	/**
	 * Se ejecuta despes del get del primer objeto
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	public Object changeAfterGet(String fieldName,Object get);
	
	/**
	 * Se ejecuta antes del set al nuevo objeto
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	public Object changeBeforeSetToCopy(String fieldName,Object before);
}
