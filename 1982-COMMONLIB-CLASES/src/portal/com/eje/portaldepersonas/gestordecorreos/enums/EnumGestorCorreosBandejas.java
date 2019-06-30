package portal.com.eje.portaldepersonas.gestordecorreos.enums;

public enum EnumGestorCorreosBandejas {
	ENTRADA,
	FAVORITOS,
	PAPELERA,
	ELIMINADOS;

	public static EnumGestorCorreosBandejas fromOpciones(boolean importantes, boolean papelera) {
		if(importantes) {
			return FAVORITOS;
		}
		else if(papelera) {
			return PAPELERA;
		}
		else {
			return ENTRADA;
		}
	}
}
