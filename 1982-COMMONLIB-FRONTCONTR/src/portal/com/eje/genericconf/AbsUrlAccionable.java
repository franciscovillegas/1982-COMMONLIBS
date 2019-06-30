package portal.com.eje.genericconf;

import org.apache.commons.lang.NotImplementedException;

import cl.eje.helper.AZonePage;
import cl.eje.helper.AZoneUtil;
import cl.eje.helper.EnumAccion;
import cl.eje.view.sencha.eje_generico_bootstrap.LoadButtonOnBootstrapPage;
import cl.eje.view.servlets.page.ServletPage;
import portal.com.eje.genericconf.ifaces.IUrlAccionable;

public class AbsUrlAccionable extends AbsAyudable implements IUrlAccionable {
	private String accionUrl;
	private String accionWaitMsg;
	private String accionOnSuccessMsg;
	private String accionOnFailureMsg;

	private boolean accionConfirmed;
	private String accionConfirmTitle;
	private String accionConfirmMsg;

	protected AbsUrlAccionable() {
		accionConfirmTitle = "Confirmación";
		accionConfirmMsg = null;
		accionWaitMsg = "Esperando respuesta del servidor.";
		accionOnSuccessMsg = "Acción realizada correctamente";
		accionOnFailureMsg = "Ha ocurrido un error inesperado";
	}

	public String getAccionUrl() {
		return accionUrl;
	}

	public void setAccionUrl(String accionUrl) {
		this.accionUrl = accionUrl;
	}
	
	public void setAccionUrlButtonBootstrapProcesor() {
		setAccionUrl(AZonePage.buildUrlFolderFormat(ServletPage.class, LoadButtonOnBootstrapPage.class));
	}
	

	public String getAccionWaitMsg() {
		return accionWaitMsg;
	}

	public void setAccionWaitMsg(String accionWaitMsg) {
		this.accionWaitMsg = accionWaitMsg;
	}

	public String getAccionOnSuccessMsg() {
		return accionOnSuccessMsg;
	}

	public void setAccionOnSuccessMsg(String accionOnSuccessMsg) {
		this.accionOnSuccessMsg = accionOnSuccessMsg;
	}

	public String getAccionOnFailureMsg() {
		return accionOnFailureMsg;
	}

	public void setAccionOnFailureMsg(String accionOnFailureMsg) {
		this.accionOnFailureMsg = accionOnFailureMsg;
	}

	/**
	 * Si getAccionConfirmMsg != null y getAccionConfirmMsg.lenth() > 0 se entiende que accionConfirmed == tue
	 * */
	public boolean isAccionConfirmed() {
		return getAccionConfirmMsg() != null;
	}

	/**
	 * Si getAccionConfirmMsg != null y getAccionConfirmMsg.lenth() > 0 se entiende que accionConfirmed == tue
	 * @deprecated
	 * */
	public void setAccionConfirmed(boolean accionConfirmed) {
		throw new NotImplementedException();
	}

	public String getAccionConfirmTitle() {
		return accionConfirmTitle;
	}

	public void setAccionConfirmTitle(String accionConfirmTitle) {
		this.accionConfirmTitle = accionConfirmTitle;
	}

	public String getAccionConfirmMsg() {
		return accionConfirmMsg;
	}

	public void setAccionConfirmMsg(String accionConfirmMsg) {
		this.accionConfirmMsg = accionConfirmMsg;
	}

}
