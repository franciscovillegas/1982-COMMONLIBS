package portal.com.eje.portal.solinf;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;

public class VoTipoDocumento implements IConsultaDataRow {
	
	private int intIdSolDoc;
	private int intIdTipoDocto;
	private EModulos modulo;
	private String strNombre;
	private boolean bolVigente;
	private int intIdArchivo;
	private String strArchivoNombre;
	private String strArchivoExtension;
	private VoEstado estado;
	
	private boolean existOnBD;
	
	public VoTipoDocumento(int intIdSolDoc, int intIdTipoDocto, String strNombre, EModulos modulo, boolean bolVigente, VoEstado estado, int intIdArchivo, String strArchivoNombre, String strArchivoExtension) {
		super();
		this.intIdSolDoc = intIdSolDoc;
		this.intIdTipoDocto = intIdTipoDocto;
		this.strNombre = strNombre;
		this.modulo = modulo;
		this.bolVigente = bolVigente;
		this.estado = estado;
		this.intIdArchivo = intIdArchivo;
		this.strArchivoNombre = strArchivoNombre;
		this.strArchivoExtension = strArchivoExtension;
	}

	public VoTipoDocumento(int intIdTipoDocto, String strNombre, EModulos modulo, boolean bolVigente, VoEstado estado, int intIdArchivo, String strArchivoNombre, String strArchivoExtension) {
		this(0, intIdTipoDocto, strNombre, modulo, bolVigente, estado, intIdArchivo, strArchivoNombre, strArchivoExtension);
	}

	public VoTipoDocumento() {
		this(0, 0, null, null, true, null, 0, null, null);
	}

	public VoTipoDocumento(int intIdTipoDocto) {
		load(intIdTipoDocto);
		this.existOnBD = true;
	}

	public VoTipoDocumento(Connection conn, int intIdTipoDocto) {
		load(conn, intIdTipoDocto);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdTipoDocto) {
		return load(null, intIdTipoDocto);
	}

	public boolean load(Connection conn, int intIdTipoDocto) {
		
		this.intIdTipoDocto = intIdTipoDocto;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select id_modulo, tipodocto, vigente \n") 
		.append("from eje_solinf_tipodocto \n") 
		.append("where id_tipodocto=? \n");
		
		Object[] params = {intIdTipoDocto};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while(data != null && data.next()) {
				this.strNombre = data.getString("tipodocto");
				this.bolVigente = (data.getInt("vigente")==1);
				this.modulo = EModulos.getModuloById(data.getInt("id_modulo"));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public int getId() {
		return intIdTipoDocto;
	}
	public String getNombre() {
		return strNombre;
	}
	public int getIdSolDoc() {
		return intIdSolDoc;
	}
	public EModulos getModulo() {
		return modulo;
	}
	public boolean getVigente() {
		return bolVigente;
	}
	public VoEstado getEstado() {
		return estado;
	}
	public int getIdArchivo() {
		return  intIdArchivo;
	}
	public String getArchivoNombre() {
		return strArchivoNombre;
	}
	public String getArchivoExtension() {
		return strArchivoExtension;
	}
	public void setIdSolDoc(int intIdSolDoc) {
		this.intIdSolDoc = intIdSolDoc;
	}
	public void setIdArchivo(int intIdArchivo) {
		this.intIdArchivo = intIdArchivo;
	}
	public void setArchivoNombre(String strArchivoNombre) {
		this.strArchivoNombre = strArchivoNombre;
	}
	public void setArchivoExtension(String strArchivoExtension) {
		this.strArchivoExtension = strArchivoExtension;
	}
	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		data.put("id_soldoc", new Field(this.intIdSolDoc));
		data.put("id_tipodocto", new Field(this.intIdTipoDocto));
		data.put("tipodocto", new Field(this.strNombre));
		if (this.modulo!=null) {
			data.put("id_modulo", new Field(this.modulo.getId()));
		}else {
			data.put("id_modulo", null);
		}
		data.put("id_archivo", new Field(this.intIdArchivo));
		data.put("archivo_nombre", new Field(this.strArchivoNombre));
		data.put("archivo_extension", new Field(this.strArchivoExtension));
		data.put("vigente", new Field(this.bolVigente));
		
		return data;
		
	}
	
}
