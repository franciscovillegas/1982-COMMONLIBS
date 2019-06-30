package portal.com.eje.portal.tipoparrafo.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class voTPParametro implements IConsultaDataRow {
	
	private int intId;
	private Double dblIdRef;
	private String strNombre;
	private String strUso;
	private boolean bolVigente;
	List<voTPParametroValor> valores;
	
	public voTPParametro(int intId, String strNombre, String strUso, boolean bolVigente) {
		super();
		this.intId = intId;
		this.strUso = strUso;
		this.strNombre = strNombre;
		this.bolVigente = bolVigente;
	}

	public voTPParametro(int intId) {
		load(null, intId);
	}

	public voTPParametro(Connection conn, int intId) {
		load(conn, intId);
	}
	
	public boolean load(Connection conn, int intId) {
		
		this.intId = intId;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select p.id_tipoparrafo, p.id_parrafo, p.Nombre, p.vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo_parrafo p \n");
		strSQL.append("where p.id_parrafo=? \n");
		
		Object[] params = {intId};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while (data!=null && data.next()) {
				this.strNombre = data.getString("Nombre");
				this.bolVigente = (data.getInt("vigente"))==1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
		
	}
	
	public int getId() {
		return intId;
	}
	public Double getIdRef() {
		return dblIdRef;
	}
	public String getNombre() {
		return strNombre;
	}
	public String getUso() {
		return strUso;
	}
	public boolean getVigente() {
		return bolVigente;
	}
	public int getVigenteInt() {
		int intVigente=0;
		if (bolVigente) {
			intVigente=1;
		}
		return intVigente;
	}
	public List<voTPParametroValor> getValores(){
		return valores;
	}

	public void setId(int intId) {
		this.intId = intId;
	}
	public void setIdRef(Double dblIdRef) {
		this.dblIdRef = dblIdRef;
	}
	public void setNombre(String strNombre) {
		this.strNombre = strNombre;
	}
	public void setValores(List<voTPParametroValor> valores) {
		this.valores = valores;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_tpparametro", this.intId);
		data.put("tpparametro", this.strNombre);
		data.put("vigente", this.bolVigente);
		
		return data;
		
	}
	
}
