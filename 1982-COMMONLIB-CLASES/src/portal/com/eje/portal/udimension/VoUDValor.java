package portal.com.eje.portal.udimension;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class VoUDValor implements IConsultaDataRow {

	int intDitemId;
	String strDitemDesc;
	String strCodRef;
	boolean bolActivo;
	
 
	public VoUDValor(int intDitemId, String strDitemDesc) { 
		this(intDitemId, strDitemDesc, true);
	}
	
	/**
	 * No sirve a al fecha (23-08-2018) el booleano en el valor ya que no está modelado en la bd
	 * @author Pancho
	 * 
	 * */
	public VoUDValor(int intDitemId, String strDitemDesc, boolean bolActivo) {
		this(intDitemId, strDitemDesc, null, bolActivo);
	}
	/**
	 * pancho: El campo strCodRef solo se utiliza para las dimensiones, la dimensión en si le puedes poner una referencia codref que ayuda a que se llame por ese string y no por el id.
	 * No sirve el strCodRef en el valor de una unidad.<br/>
	 * 
	 *
 	  @deprecated
 	  @since 23-08-2018
	 * */
	public VoUDValor(int intDitemId, String strDitemDesc, String strCodRef, boolean bolActivo) {
		this.intDitemId = intDitemId;
		this.strDitemDesc = strDitemDesc;
		this.strCodRef = strCodRef;
		this.bolActivo = bolActivo;
	}
	
	public int getDitemId(){
		return intDitemId; 
	}
	
	public String getDitemDesc(){
		return strDitemDesc; 
	}

	public boolean getActivo(){
		return bolActivo; 
	}

	public String getCodRef(){
		return strCodRef; 
	}
	
	public void setId(int intDitemId) {
		this.intDitemId = intDitemId;
	}

	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();

		data.put("ditem_id", new Field(this.getDitemId()));
		data.put("ditem_desc", new Field(this.getDitemDesc()));
		data.put("codref", new Field(this.getCodRef()));
		data.put("activo", new Field(this.getActivo()));
		
		return data;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return strDitemDesc;
	}

}
