package portal.com.eje.portal.roldetecterconfig;

import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.transactions.TransactionConnection;

public class VoRolDetecterConfig implements IConsultaDataRow {
	
	private int intIdRolDetecter;
	private int intCodRecurso;
	private String strNombre;
	private String strDescripcion;
	private String strPath;
	private boolean bolActivo;
	private List<VoRDCMiembro> miembros;
	private boolean existOnBD;
	
	
	public VoRolDetecterConfig(int intIdRolDetecter, String strNombre, String strDescripcion, boolean bolActivo) {
		super();
		this.intIdRolDetecter = intIdRolDetecter;
		this.strNombre = strNombre;
		this.strDescripcion = strDescripcion;
		this.bolActivo = bolActivo;
		this.existOnBD = false;
	}
	
	/**
	 * No tene Connection ni TransactionConnection
	 
	public VoRolDetecterConfig(int intIdRolDetecter) {
		load(intIdRolDetecter);
		this.existOnBD = true;
	}
	 */
	
	public VoRolDetecterConfig(TransactionConnection cons, int intIdRolDetecter) {
		load(cons, intIdRolDetecter, false);
		this.existOnBD = true;
	}
	
	/**
	 * No tene Connection ni TransactionConnection
	 *  
	 * 
	public VoRolDetecterConfig(int intIdRolDetecter, int intCodRecurso) {
		load(intIdRolDetecter, intCodRecurso);
		this.existOnBD = true;
	}
    */
	
	public VoRolDetecterConfig(TransactionConnection cons, int intIdRolDetecter, int intCodRecurso, boolean bolConMiembros) {
		load(cons, intIdRolDetecter, intCodRecurso, bolConMiembros);
		this.existOnBD = true;
	}
	
	public VoRolDetecterConfig(TransactionConnection cons, int intIdRolDetecter, int intCodRecurso) {
		load(cons, intIdRolDetecter, intCodRecurso, false);
		this.existOnBD = true;
	}
	
	/**
	 * No tene Connection ni TransactionConnection
	public boolean load(int intIdRolDetecter) {
		return load(null, intIdRolDetecter, false);
	}
	 * */
	
	/**
	 * No tene Connection ni TransactionConnection
	public boolean load(int intIdRolDetecter, int intCodRecurso) {
		return load(null, intIdRolDetecter, intCodRecurso, false);
	}
	 * */
	
	/**
	 * No tene Connection ni TransactionConnection

	public boolean load(int intIdRolDetecter, int intCodRecurso, boolean bolConMiembros) {
		return load(null, intIdRolDetecter, intCodRecurso, bolConMiembros);
	}
	* */
	
	public boolean load(TransactionConnection cons, int intIdRolDetecter, int intCodRecurso, boolean bolConMiembros) {
		 
		if (intIdRolDetecter==0){
			intIdRolDetecter = loadByCodRecurso(cons, intCodRecurso);
		}
		
		return load(cons, intIdRolDetecter, bolConMiembros);
	}
	
	/**
	 * No tene Connection ni TransactionConnection
	int loadByCodRecurso(int intCodRecruso) {
		return loadByCodRecurso(null, intCodRecruso);
	}
	 * */
	
	/**
	 * return id_roldetecter
	 * */
	int loadByCodRecurso(TransactionConnection cons, int intCodRecruso) {
		 
		StringBuilder strSQL = new StringBuilder();
		
		int intIdRolDetecter = 0;
		
		strSQL.append("select id_roldetecter \n") 
		.append("from eje_wf_roldetecter \n")
		.append("where cod_recurso=").append(intCodRecruso);
		
		try {
			ConsultaData data  = ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString());
 
			while(data != null && data.next()) {
				intIdRolDetecter = data.getInt("id_roldetecter");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return intIdRolDetecter;
	}
	
	
	public boolean load(TransactionConnection cons, int intIdRolDetecter, boolean bolConMiembros) {
		if( cons  == null ) {
			throw new NullPointerException();
		}
		
		this.intIdRolDetecter = intIdRolDetecter;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select cod_recurso, roldetecter, descripcion, vigente \n") 
		.append("from eje_wf_roldetecter \n")
		.append("where id_roldetecter=").append(intIdRolDetecter);
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString());
			 
			while(data != null && data.next()) {
				this.intIdRolDetecter = intIdRolDetecter;
				this.strNombre = data.getForcedString("roldetecter");
				this.strDescripcion = data.getForcedString("descripcion");
				this.bolActivo = "1".equals(data.getForcedString("vigente"));
				if (bolConMiembros) {
					this.miembros = RolDetecterConfigLocator.getInstance().getMiembros(cons, intIdRolDetecter);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public int getId() {
		return intIdRolDetecter;
	}

	public void setId(int intIdRolDetecter) {
		this.intIdRolDetecter = intIdRolDetecter;
	}

	public int getCodRecurso() {
		return intCodRecurso;
	}

	public void setCodRecurso(int intCodRecurso) {
		this.intCodRecurso = intCodRecurso;
	}
	
	public void setPath(String strPath) {
		this.strPath = strPath;
	}

	public String getNombre() {
		return strNombre;
	}

	public String getDescripcion() {
		return strDescripcion;
	}

	public boolean getActivo() {
		return bolActivo;
	}

	public String getPath() {
		return strPath;
	}
	
	public boolean isExistOnBD() {
		return existOnBD;
	}
	
	public List<VoRDCMiembro> getMiembros(){
		return miembros;
	}

	@Override
	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		data.put("id_roldetecter", new Field(this.intIdRolDetecter));
		data.put("cod_recurso", new Field(this.intCodRecurso));
		data.put("roldetecter", new Field(this.strNombre));
		data.put("descripcion", new Field(this.strDescripcion));
		data.put("path", new Field(this.strPath));
		data.put("vigente", new Field(this.bolActivo));
				
		return data;
	}
	
	public String toString() {
		return getNombre();
	}
}
