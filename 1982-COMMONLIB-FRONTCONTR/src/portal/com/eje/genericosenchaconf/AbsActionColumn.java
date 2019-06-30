package portal.com.eje.genericosenchaconf;

import portal.com.eje.genericconf.AbsButton;
import portal.com.eje.genericosenchaconf.ifaces.IActionColumn;

public class AbsActionColumn extends AbsButton implements IActionColumn {
	private boolean accionOnSuccessRefreshThisStore;
	private boolean accionOnSuccessRefreshRelativesStores;
	private boolean accionOnSuccessDeleteThisRecord;
	
	
	public AbsActionColumn() {
		// TODO Auto-generated constructor stub
	}


	public boolean isAccionOnSuccessRefreshThisStore() {
		return accionOnSuccessRefreshThisStore;
	}


	public void setAccionOnSuccessRefreshThisStore(boolean accionOnSuccessRefreshThisStore) {
		this.accionOnSuccessRefreshThisStore = accionOnSuccessRefreshThisStore;
	}


	public boolean isAccionOnSuccessRefreshRelativesStores() {
		return accionOnSuccessRefreshRelativesStores;
	}


	public void setAccionOnSuccessRefreshRelativesStores(boolean accionOnSuccessRefreshRelativesStores) {
		this.accionOnSuccessRefreshRelativesStores = accionOnSuccessRefreshRelativesStores;
	}


	public boolean isAccionOnSuccessDeleteThisRecord() {
		return accionOnSuccessDeleteThisRecord;
	}


	public void setAccionOnSuccessDeleteThisRecord(boolean accionOnSuccessDeleteThisRecord) {
		this.accionOnSuccessDeleteThisRecord = accionOnSuccessDeleteThisRecord;
	}

	 
	
	
}
