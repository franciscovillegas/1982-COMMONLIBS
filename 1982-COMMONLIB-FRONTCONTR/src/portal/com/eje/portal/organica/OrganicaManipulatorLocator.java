package portal.com.eje.portal.organica;

import portal.com.eje.portal.organica.ifaces.IOrganicaManipulator;

public class OrganicaManipulatorLocator {

	public IOrganicaManipulator getManipulator() {
		return OrganicaManipulatorCtr.getInstance();
	}
	
}
