package portal.com.eje.portal.parametro;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;

public class ParametroValue implements IConsultaDataRow {
	private int pos;
	private int idCliente;
	private int idParam;
	private int idType;
	private String nemo;
	private String value;
	private String dataAdic1;
	private String dataAdic2;
	private String base64file;
	private String base64width;
	private String base64height;
	private String base64weight;

	public static ParametroValue buildParametro(String key,boolean valor) {
		return buildParametro(key, String.valueOf(valor));
	}
	
	public static ParametroValue buildParametro(String key,int valor) {
		return buildParametro(key, String.valueOf(valor));
	}
	
	public static ParametroValue buildParametro(String key,String valor) {
		ParametroValue pv = new ParametroValue();
		
		pv.nemo = key;
		pv.dataAdic1= key;
		pv.value = valor;
		pv.idType = ParametroType.tipo_String.getId();
		
		return pv;
	}
	
	public ParametroValue() {
		
	}
	
	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdParam() {
		return idParam;
	}

	public void setIdParam(int idParam) {
		this.idParam = idParam;
	}

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDataAdic1() {
		return dataAdic1;
	}
	
	public String getKey() {
		return dataAdic1;
	}

	public void setDataAdic1(String dataAdic1) {
		this.dataAdic1 = dataAdic1;
	}

	public String getDataAdic2() {
		return dataAdic2;
	}

	public void setDataAdic2(String dataAdic2) {
		this.dataAdic2 = dataAdic2;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getBase64file() {
		return base64file;
	}

	public void setBase64file(String base64file) {
		this.base64file = base64file;
	}

	public String getBase64width() {
		return base64width;
	}

	public void setBase64width(String base64width) {
		this.base64width = base64width;
	}

	public String getBase64height() {
		return base64height;
	}

	public void setBase64height(String base64height) {
		this.base64height = base64height;
	}

	public String getBase64weight() {
		return base64weight;
	}

	public void setBase64weight(String base64weight) {
		this.base64weight = base64weight;
	}

	public String getNemo() {
		return nemo;
	}

	public void setNemo(String nemo) {
		this.nemo = nemo;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getValue();
	}

	@Override
	public DataFields toDataField() {
		DataFields df = new DataFields();
		df.put("pos", pos);
		df.put("idcliente", idCliente);
		df.put("idparam", idParam);
		df.put("idtype", idType);
		df.put("nemo", nemo);
		df.put("value", value);
		df.put("dataadic1", dataAdic1);
		df.put("dataadic2", dataAdic2);
		df.put("base64file", base64file);
		df.put("base64width", base64width);
		df.put("base64height", base64height);
		df.put("base64weight", base64weight);
 
		return df;
	}
	
 

}
