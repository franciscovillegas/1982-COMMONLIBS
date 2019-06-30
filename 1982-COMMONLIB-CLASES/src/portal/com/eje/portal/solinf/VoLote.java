package portal.com.eje.portal.solinf;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;

public class VoLote implements IConsultaDataRow {
	
	private int intIdLote;
	private EModulos modulo;
	private String strDescripcion;
	private String strEmisor;
	private String strUrl;
	private Date dteFecha;
	private int intIdSolicitante;
	private VoEstado estado;
	private List<VoSolicitud> solicitudes;
	
	private boolean existOnBD;
	
	public VoLote(int intIdLote, EModulos modulo, String strDescripcion, String strEmisor, String strUrl, Date dteFecha, int intIdSolicitante, VoEstado estado, List<VoSolicitud> solicitudes) {
		super();
		this.intIdLote = intIdLote;
		this.modulo = modulo;
		this.strDescripcion = strDescripcion;
		this.strEmisor = strEmisor;
		this.strUrl = strUrl;
		this.dteFecha = dteFecha;
		this.intIdSolicitante = intIdSolicitante;
		this.estado=estado;
		this.solicitudes = solicitudes;
	}

	public VoLote(int intIdLote) {
		load(intIdLote);
		this.existOnBD = true;
	}

	public VoLote(Connection conn, int intIdLote) {
		load(conn, intIdLote);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdLote) {
		return load(null, intIdLote);
	}

	public boolean load(Connection conn, int intIdLote) {
		
		this.intIdLote = intIdLote;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select l.id_modulo, l.descripcion, l.emisor, l.url, l.fecha, l.id_solicitante, l.id_estado \n") 
		.append("from eje_solinf_lote l \n");
		if (intIdLote!=0) {
			strSQL.append("where l.id_Lote=? \n");
		}else {
			strSQL.append("where l.id_Lote<>? \n");
		}
		
		Object[] params = {intIdLote};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while(data != null && data.next()) {
				this.modulo = EModulos.getModuloById(data.getInt("id_modulo"));
				this.strDescripcion = data.getString("descripcion");
				this.strEmisor = data.getString("emisor");
				this.strUrl = data.getString("url");
				this.dteFecha = data.getDateJava("fecha");
				this.intIdSolicitante = data.getInt("id_solicitante");
				this.estado = new VoEstado(data.getInt("id_estado"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public int getId() {
		return intIdLote;
	}
	
	public EModulos getModulo() {
		return modulo;
	}
	
	public String getDescripcion() {
		return strDescripcion;
	}
	
	public String getEmisor() {
		return strEmisor;
	}
	
	public String getUrl() {
		return strUrl;
	}
	
	public Date getFecha() {
		return dteFecha;
	}
	
	public int getSolicitante() {
		return intIdSolicitante;
	}
	
	public VoEstado getEstado() {
		return estado;
	}
	
	public List<VoSolicitud> getSolicitudes(){
		return solicitudes;
	}

	public void setIdLote(int intIdLote) {
		this.intIdLote = intIdLote;
	}
	
	public void setEstado(VoEstado estado) {
		this.estado = estado;
	}
	
	public void setSolicitudes(List<VoSolicitud> solicitudes) {
		this.solicitudes = solicitudes;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		data.put("id_lote", new Field(this.intIdLote));
		data.put("id_modulo", new Field(this.modulo.getId()));
		data.put("descipcion", new Field(this.strDescripcion));
		data.put("emisor", new Field(this.strEmisor));
		data.put("url", new Field(this.strUrl));
		data.put("fecha_tope", new Field(sdf.format(this.dteFecha)));
		data.put("id_solicitante", new Field(this.intIdSolicitante));
		data.put("id_estado", new Field(this.estado.getId()));
		data.put("estado", new Field(this.estado.getDescripcion()));
		data.put("estado_icono", new Field(this.estado.getIcono()));
		
		return data;
		
	}
	
}
