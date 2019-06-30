package portal.com.eje.portal.appcontext.enums;

public enum EnumBeansConfigurationType {
	SINGLE(0),
	MUTISELECT(50);
	
	private int idtype; 
	EnumBeansConfigurationType(int idtype) {
		this.idtype = idtype;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.idtype);
	}
	
	public int getIdtype() {
		return this.idtype;
	}
	
	public String getIdtypeStr() {
		return this.toString();
	}
}
