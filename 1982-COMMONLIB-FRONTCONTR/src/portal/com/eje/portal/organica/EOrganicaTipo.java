package portal.com.eje.portal.organica;

public enum EOrganicaTipo {
	porUnidades(Organica.class),
	porDimension(OrganicaPorDimension.class);
	
	private Class cImpl;
	EOrganicaTipo(Class cImpl) {
		this.cImpl = cImpl;
	}
	
	public Class getClassImpl() {
		return this.cImpl;
	}
	
}
