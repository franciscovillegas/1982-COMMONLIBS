package cl.ejedigital.web.fileupload.vo;

public class EjeFileUnicoTipo {
	public static EjeFileUnicoTipo DESCONOCIDO = new EjeFileUnicoTipo(0, "Desconocido","Desconocido");
	public static EjeFileUnicoTipo METAS = new EjeFileUnicoTipo(1, "Metas","Archivo de Metas");
	public static EjeFileUnicoTipo CUMPLIMIENTOS = new EjeFileUnicoTipo(2,"Cumplimientos", "Archivo de Cumplimientos");
	
	public static EjeFileUnicoTipo TIPO1 = new EjeFileUnicoTipo(11,"Tipo 1", "Tipo 1");
	public static EjeFileUnicoTipo TIPO2 = new EjeFileUnicoTipo(12,"Tipo 2", "Tipo 2");
	public static EjeFileUnicoTipo TIPO3 = new EjeFileUnicoTipo(13,"Tipo 3", "Tipo 3");
	public static EjeFileUnicoTipo TIPO4 = new EjeFileUnicoTipo(14,"Tipo 4", "Tipo 4");
	public static EjeFileUnicoTipo TIPO5 = new EjeFileUnicoTipo(15,"Tipo 5", "Tipo 5");
	
	public static EjeFileUnicoTipo CAJACHILE_AGENCIEROS = new EjeFileUnicoTipo(100,"CajaChile_Agenciero", "Archivo de agencieros para caja chile");

	
	private int id;
	private String nombre;
	private String desc;

	protected EjeFileUnicoTipo(int id, String nombre, String desc) {
		this.id = id;
		this.nombre = nombre;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDesc() {
		return desc;
	}
	
	public static EjeFileUnicoTipo getEjeFileUnicoTipo(int id){
		switch (id) {
		case 0:
			return DESCONOCIDO;
		case 1:
			return METAS;
		case 2:
			return CUMPLIMIENTOS;
		default:
			return DESCONOCIDO;
		}
		
	}

}
