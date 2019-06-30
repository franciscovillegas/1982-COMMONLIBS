package portal.com.eje.portal.roldetecterconfig;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class VoRDCParametro implements IConsultaDataRow {
	
	private int intIdRDParam;
	private String strNombre;
	private String strReferencia;
	private boolean bolActivo;
	
	public VoRDCParametro(int intIdRDParam, String strNombre, String strReferencia, boolean bolActivo) {
		super();
		this.intIdRDParam = intIdRDParam;
		this.strNombre = strNombre;
		this.strReferencia = strReferencia;
		this.bolActivo = bolActivo;
	}
	
	public VoRDCParametro.Parametro getParametro() {
		return VoRDCParametro.Parametro.fromID(this.intIdRDParam);
	}
	
	public int getIdParam() {
		return intIdRDParam;
	}

	public void setIdParam(int intIdRDParam) {
		this.intIdRDParam = intIdRDParam;
	}
	
	public String getNombre() {
		return strNombre;
	}

	public String getReferencia() {
		return strReferencia;
	}

	public boolean getActivo() {
		return bolActivo;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();

		data.put("id_rdparam", new Field(this.intIdRDParam));
		data.put("rdparam", new Field(this.strNombre));
		data.put("referencia", new Field(this.strReferencia));
		data.put("vigente", new Field(this.bolActivo));
		
		return data;

	}
	
	public enum TipoMiembro {
		 
		Todos(0, "Todos"),
		Filtro(1, "Filtro"),
		Responsable(2, "Responsable");

		private int id;
		private String nombre;
		
		TipoMiembro(int id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}
		
		public String getnombre() {
			return this.nombre;
		}
		
		public int getId() {
			return this.id;
		}
		
		public static TipoMiembro fromID(int id) {
			java.lang.reflect.Field[] fields = TipoMiembro.class.getDeclaredFields();
			for(java.lang.reflect.Field f : fields) {
				if(f.getType().isAssignableFrom(TipoMiembro.class)) {
					TipoMiembro fc = Enum.valueOf(TipoMiembro.class, f.getName());	
					if(fc != null && fc.getId() == id) {
						return fc;
					}
				}			
			}
			
			return Todos;
		}
		
	}
	
	public enum Ticket {
		 
		IdREq(1, "IdReq"),
		Solicitante(2, "Solicitante");

		private int id;
		private String nombre;
		
		Ticket(int id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}
		
		public String getnombre() {
			return this.nombre;
		}
		
		public int getId() {
			return this.id;
		}
		
		public static Ticket fromID(int id) {
			java.lang.reflect.Field[] fields = Ticket.class.getDeclaredFields();
			for(java.lang.reflect.Field f : fields) {
				if(f.getType().isAssignableFrom(Ticket.class)) {
					Ticket fc = Enum.valueOf(Ticket.class, f.getName());	
					if(fc != null && fc.getId() == id) {
						return fc;
					}
				}			
			}
			
			return IdREq;
		}
		
	}
	
	public enum Parametro {
		 
		SinDefinir(0, "Seleccione un parámetro"),
		Unidad(4, "Unidad"),
		Cargo(5, "Cargo"),
		Persona(6, "Persona"),
		Ticket(20, "Ticket");
		private int id;
		private String nombre;
		
		Parametro(int id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}
		
		public String getnombre() {
			return this.nombre;
		}
		
		public int getId() {
			return this.id;
		}
		
		public static Parametro fromID(int id) {
			java.lang.reflect.Field[] fields = Parametro.class.getDeclaredFields();
			for(java.lang.reflect.Field f : fields) {
				if(f.getType().isAssignableFrom(Parametro.class)) {
					Parametro fc = Enum.valueOf(Parametro.class, f.getName());	
					if(fc != null && fc.getId() == id) {
						return fc;
					}
				}			
			}
			
			return SinDefinir;
		}
		
	}
	
}
