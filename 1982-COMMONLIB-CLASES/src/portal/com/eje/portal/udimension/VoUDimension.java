package portal.com.eje.portal.udimension;

import java.util.List;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class VoUDimension implements IConsultaDataRow{

	private int intIdDimension;
	private String strNombre;
	private VoUTipoDimension TipoDimension;
	private String strCardinalidad;
	private String strCampoRef;
	private List<VoUDValor> valores;
	
	public VoUDimension(int intIdDimension, String strNombre, VoUTipoDimension TipoDimension, String strCardinalidad, String strCampoRef, List<VoUDValor> valores){
		super();
		this.intIdDimension = intIdDimension;
		this.strNombre = strNombre;
		this.TipoDimension = TipoDimension;
		this.strCardinalidad = strCardinalidad;
		this.strCampoRef = strCampoRef;
		this.valores = valores;
	}
	
	public VoUDimension(int intIdDimension, String strNombre, VoUTipoDimension TipoDimension, String strCardinalidad, String strCampoRef){
		this(intIdDimension, strNombre, TipoDimension, strCardinalidad, strCampoRef, null);
	}
	
	public int getIdDimension(){
		return intIdDimension;
	}
	
	public String getNombre(){
		return strNombre;
	}

	public VoUTipoDimension getTipo(){
		return TipoDimension;
	}
	
	public String getCardinalidad(){
		return strCardinalidad;
	}
	
	public String getCampoRef(){
		return strCampoRef;
	}
	
	public List<VoUDValor> getValores(){
		return valores;
	}
	
	public void setId(int intIdDimension) {
		this.intIdDimension = intIdDimension;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();

		data.put("id_dimension", new Field(this.getIdDimension()));
		data.put("nombre", new Field(this.getNombre()));
		if (this.getTipo()!=null) {
			data.put("id_tipodimension", new Field(this.getTipo().getIdDimension()));
			data.put("tipodimension", new Field(this.getTipo().getDescripcion()));
		}else {
			data.put("id_tipodimension", new Field(null));
			data.put("tipodimension", new Field(null));
		}
		data.put("camporef", new Field(this.getCampoRef()));
		
		return data;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder(strNombre + " ["+this.getTipo()+"] ");
		s.append("{");
		boolean first = true;
		
		for(VoUDValor vo : getValores()) {
			if(!first) {
				s.append(",");
			}
			
			s.append(vo);
			first = false;
		}
		
		s.append("}");
		
		return s.toString();
	}

}


