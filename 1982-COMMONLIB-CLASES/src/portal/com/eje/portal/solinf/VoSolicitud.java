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
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.trabajador.VoPersona;

public class VoSolicitud implements IConsultaDataRow {
	
	private int intIdSolicitud;
	private VoPersona solicitante;
	private Date dteFechaSolicitud;
	private VoPersona persona;
	private String strEMail;
	private String strDetalle;
	private VoPersona informador;
	private Date dteFechaRespuesta;
	private String strRespuesta;
	private Date dteFechaTope;
	private VoEstado estado;
	private int intDiasAvance;
	private int intDiasTotal;
	private int intPorcAvance;
	private int intNivelAvance;
	private List<VoTipoDocumento> tiposdocto;
	
	private boolean existOnBD;
	
//	public VoSolicitud(int intIdSolicitud, VoPersona solicitante, VoPersona persona, String strEMail, String strDetalle, Date dteFechaTope, VoEstado estado, int intDiasAvance, int intDiasTotal, int intPorcAvance, int intNivelAvance, List<VoTipoDocumento> tiposdocto) {
//		super();
//		this.intIdSolicitud = intIdSolicitud;
//		this.solicitante=solicitante;
//		this.persona = persona;
//		this.strEMail = strEMail;
//		this.strDetalle = strDetalle;
//		this.dteFechaTope = dteFechaTope;
//		this.estado = estado;
//		this.intDiasAvance = intDiasAvance;
//		this.intDiasTotal= intDiasTotal;
//		this.intDiasAvance = intPorcAvance;
//		this.intNivelAvance = intNivelAvance;
//		this.tiposdocto = tiposdocto;
//	}
	public VoSolicitud(int intIdSolicitud, VoPersona solicitante, VoPersona persona, String strEMail, String strDetalle, Date dteFechaTope, VoEstado estado, List<VoTipoDocumento> tiposdocto) {
		super();
		this.intIdSolicitud = intIdSolicitud;
		this.solicitante=solicitante;
		this.persona = persona;
		this.strEMail = strEMail;
		this.strDetalle = strDetalle;
		this.dteFechaTope = dteFechaTope;
		this.estado = estado;
		this.tiposdocto = tiposdocto;
		//		this(intIdSolicitud, solicitante, persona, strEMail, strDetalle, dteFechaTope, estado, 0, 0, 0, 0, tiposdocto);
	}

	public VoSolicitud(int intIdSolicitud) {
		load(intIdSolicitud);
		this.existOnBD = true;
	}

	public VoSolicitud(Connection conn, int intIdSolicitud) {
		load(conn, intIdSolicitud);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdSolicitud) {
		return load(null, intIdSolicitud);
	}

	public boolean load(Connection conn, int intIdSolicitud) {
		
		this.intIdSolicitud = intIdSolicitud;
		
		StringBuilder strSQL = new StringBuilder();

		strSQL.append("declare @porc_alerta int, @porc_critico int, @fecha_actual smalldatetime \n\n") 
		
		.append("set @porc_alerta = 70 \n") 
		.append("set @porc_critico = 90 \n") 
		.append("set @fecha_actual=convert(varchar, getdate(), 112) \n\n") 
		
		.append("select *, \n") 
		.append("nivel_avance=(case \n") 
		.append("	when fecha_respuesta is not null then 0 \n") 
		.append("	when porc_avance>=@porc_critico then 3 \n") 
		.append("	when porc_avance>=@porc_alerta then 2 \n") 
		.append("	else 1 end) \n") 
		.append("from ( \n") 
		.append("	select *, porc_avance=cast((case when dias_avance<=dias_total and dias_total>0 then round(dias_avance/dias_total*100, 0) else 100 end) as int) \n") 
		.append("	from ( \n") 
		.append("		select l.id_solicitante, fecha=l.fecha, ls.id_persona, ls.email, ls.detalle, ls.fecha_respuesta, ls.respuesta, ls.fecha_tope, ls.id_estado, \n") 
		.append("		dias_avance=cast(datediff(day, ls.fecha_tope, @fecha_actual) as numeric), \n") 
		.append("		dias_total=cast(datediff(day, l.fecha, ls.fecha_tope) as numeric) \n") 
		.append("		from eje_solinf_lote_solicitud ls \n") 
		.append("		inner join eje_solinf_lote l on l.id_lote=ls.id_lote \n") 
		.append("		where ls.id_solicitud=? \n") 
		.append("	) x \n") 
		.append(") y \n");
		
		Object[] params = {intIdSolicitud};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while(data != null && data.next()) {
				this.solicitante = new VoPersona(data.getInt("id_solicitante"));
				this.dteFechaSolicitud = data.getDateJava("fecha");
				this.persona = new VoPersona(data.getInt("id_persona"));
				this.strEMail = data.getString("email");
				this.strDetalle = data.getString("detalle");
				if (data.getForcedString("fecha_respuesta")!=null) {
					this.dteFechaRespuesta = data.getDateJava("fecha_respuesta");
				}
				this.strRespuesta = data.getString("respuesta");
				this.dteFechaTope = data.getDateJava("fecha_tope");
				this.estado = new VoEstado(data.getInt("id_estado"));
				this.intDiasAvance = Validar.getInstance().validarInt(data.getForcedString("dias_avance"));
				this.intDiasTotal = Validar.getInstance().validarInt(data.getForcedString("dias_total"));
				this.intPorcAvance = Validar.getInstance().validarInt(data.getForcedString("porc_avance"));
				this.intNivelAvance = Validar.getInstance().validarInt(data.getForcedString("nivel_avance"));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public int getId() {
		return intIdSolicitud;
	}
	
	public VoPersona getSolicitante() {
		return solicitante;
	}
	
	public VoPersona getPersona() {
		return persona;
	}
	
	public String getEMail() {
		return strEMail;
	}
	
	public String getDetalle() {
		return strDetalle;
	}
	
	public Date getFecha() {
		return dteFechaTope;
	}
	
	public VoEstado getEstado() {
		return estado;
	}
	
	public List<VoTipoDocumento> getTiposDocto(){
		return tiposdocto;
	}
	
	public VoPersona getInformador() {
		return informador;
	}
	
	public String getRespuesta() {
		return strRespuesta;
	}
	
	public void setId(int intIdSolicitud) {
		this.intIdSolicitud = intIdSolicitud;
	}
	
	public void setInformador(VoPersona informador) {
		this.informador =informador;
	}
	
	public void setRespuesta(String strRespuesta) {
		this.strRespuesta = strRespuesta;
	}
	public void setEstado(VoEstado estado) {
		this.estado = estado;
	}
	
	public void setTiposDocto(List<VoTipoDocumento> tiposdocto) {
		this.tiposdocto = tiposdocto;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		data.put("id_solicitud", new Field(this.intIdSolicitud));
		data.put("fecha_solicitud", new Field(sdf.format(this.dteFechaSolicitud)));
		data.put("id_solicitante", new Field(this.solicitante.getId()));
		data.put("solicitante", new Field(this.solicitante.getNombre()));
		data.put("solicitante_foto", new Field(this.solicitante.getFoto()));
		data.put("id_persona", new Field(this.persona.getId()));
		data.put("persona", new Field(this.persona.getNombre()));
		data.put("persona_foto", new Field(this.persona.getFoto()));
		data.put("email", new Field(this.strEMail));
		data.put("detalle", new Field(this.strDetalle));
		if (this.dteFechaRespuesta!=null) {
			data.put("fecha_respuesta", new Field(sdf.format(this.dteFechaRespuesta)));
		}else {
			data.put("fecha_respuesta", null);
		}
		data.put("respuesta", new Field(this.strRespuesta));
		data.put("fecha_tope", new Field(sdf.format(this.dteFechaTope)));
		data.put("id_estado", new Field(this.estado.getId()));
		data.put("estado", new Field(this.estado.getDescripcion()));
		data.put("estado_icono", new Field(this.estado.getIcono()));
		data.put("dias_avance", new Field(this.intDiasAvance));
		data.put("dias_total", new Field(this.intDiasTotal));
		data.put("porc_avance", new Field(this.intPorcAvance));
		data.put("nivel_avance", new Field(this.intNivelAvance));
		
		return data;
		
	}
	
}
