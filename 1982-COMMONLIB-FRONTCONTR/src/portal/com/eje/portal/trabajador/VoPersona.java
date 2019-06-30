package portal.com.eje.portal.trabajador;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import freemarker.template.SimpleHash;
import portal.com.eje.portal.correspondencia.CorrespondenciaMailLocator;

public class VoPersona implements IConsultaDataRow {
	
	private int intIdPersona;
	private String strPersona;
	private String strNombres;
	private String strApellidoPaterno;
	private String strApellidoMaterno;
	private String strCargo;
	private String strUnidad;
	private String strEMail;
	private String strFoto;
	
	private boolean existOnBD;
	
	public VoPersona(int intIdPersona, String strPersona, String strEMail, String strFoto) {
		super();
		this.intIdPersona = intIdPersona;
		this.strPersona = strPersona;
		this.strEMail = strEMail;
		this.strFoto = strFoto;
	}

	public VoPersona(int intIdPersona) {
		load(intIdPersona);
		this.existOnBD = true;
	}

	
	public VoPersona(Connection conn, int intIdPersona) {
		load(conn, intIdPersona);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdPersona) {
		return load(null, intIdPersona);
	}

	public boolean load(Connection conn, int intIdPersona) {
		
		this.intIdPersona = intIdPersona;

		TrabajadorDataFiltro df = new TrabajadorDataFiltro();
		df.setDataPersonal(true);
		df.setDataContacto(true);
		df.setDataContactoMail(true);
		df.setDataUnidad(true);
		df.setDataCargo(true);
		df.setPathBacks("../../../");
		ConsultaData dtaPersona = TrabajadorDataLocator.getInstance().getDataFiltrada(df, this.intIdPersona);
		while (dtaPersona!=null && dtaPersona.next()) {
			this.strPersona = dtaPersona.getString("nombre");
			this.strNombres = dtaPersona.getString("nombres");
			this.strApellidoPaterno = dtaPersona.getString("ape_paterno");
			this.strApellidoMaterno = dtaPersona.getString("ape_materno");
//			this.strEMail = dtaPersona.getString("mail");
			this.strEMail = CorrespondenciaMailLocator.getInstance().getCorreo(intIdPersona, null);
			this.strFoto = dtaPersona.getString("imagen_path");
			this.strCargo = dtaPersona.getString("cargo_desc");
			this.strUnidad = dtaPersona.getString("unidad_desc");
		}
		
		return true;
	}
	
	
	public int getId() {
		return intIdPersona;
	}
	
	public String getNombre() {
		return strPersona;
	}

	public String getNombres() {
		return strNombres;
	}
	
	public String getApellidoPaterno() {
		return strApellidoPaterno;
	}

	public String getApellidoMaterno() {
		return strApellidoMaterno;
	}

	public String getEMail() {
		return strEMail;
	}
	
	public String getFoto() {
		return strFoto;
	}

	public SimpleHash toHash() {
		
		SimpleHash modelRoot = new 	SimpleHash();

		modelRoot.put("id_persona", String.valueOf(this.intIdPersona));
		modelRoot.put("persona", this.strPersona);
		modelRoot.put("nombres", this.strNombres);
		modelRoot.put("apellido_paterno", this.strApellidoPaterno);
		modelRoot.put("apellido_materno", this.strApellidoMaterno);
		modelRoot.put("email", this.strEMail);
		modelRoot.put("cargo", this.strCargo);
		modelRoot.put("unidad", this.strUnidad);

		return modelRoot;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		data.put("id_persona", new Field(this.intIdPersona));
		data.put("persona", new Field(this.strPersona));
		data.put("nombres", new Field(this.strNombres));
		data.put("apellido_paterno", new Field(this.strApellidoPaterno));
		data.put("apellido_materno", new Field(this.strApellidoMaterno));
		data.put("email", new Field(this.strEMail));
		data.put("cargo", new Field(this.strCargo));
		data.put("unidad", new Field(this.strUnidad));
		
//		data.put("fecha_tope", new Field(sdf.format(this.dteFecha)));
		
		return data;
		
	}

}
