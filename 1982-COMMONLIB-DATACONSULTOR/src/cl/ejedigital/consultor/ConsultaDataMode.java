package cl.ejedigital.consultor;

public enum ConsultaDataMode {
	NOCONVERSION(ManipulatorSqlServer2000.class),
	CONVERSION(ManipulatorSqlServerV2018.class);
	private Class<? extends IFieldManipulator> clase;
	
	ConsultaDataMode(Class<? extends IFieldManipulator> clase) {
		this.clase = clase;
	}
	
	public Class<? extends IFieldManipulator> getImplementation() {
		return this.clase;
	}
}
