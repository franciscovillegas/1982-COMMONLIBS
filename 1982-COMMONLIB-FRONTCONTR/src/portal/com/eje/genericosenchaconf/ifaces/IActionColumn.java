package portal.com.eje.genericosenchaconf.ifaces;

import portal.com.eje.genericconf.ifaces.IButton;

public interface IActionColumn extends IButton {


	/**
	 * Al momento de realizar correctamente la acción se refrescara el store que crea esta grid
	 * @author Pancho
	 * */

	public boolean isAccionOnSuccessRefreshThisStore();

	/**
	 * Al momento de realizar correctamente la acción se refrescara el store que crea esta grid
	 * @author Pancho
	 * */
	public void setAccionOnSuccessRefreshThisStore(boolean accionOnSuccessRefreshThisStore) ;


	public boolean isAccionOnSuccessRefreshRelativesStores();

	public void setAccionOnSuccessRefreshRelativesStores(boolean accionOnSuccessRefreshRelativesStores);

	public boolean isAccionOnSuccessDeleteThisRecord();

	public void setAccionOnSuccessDeleteThisRecord(boolean accionOnSuccessDeleteThisRecord);
	
}
