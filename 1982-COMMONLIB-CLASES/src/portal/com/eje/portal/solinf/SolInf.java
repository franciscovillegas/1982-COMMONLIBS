package portal.com.eje.portal.solinf;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.SendEmail;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;

public class SolInf implements ISolInf {

	private static ISolInf instance;
	private Cronometro cronometro;
	private int secondToRefresh = 60;
	
	public static ISolInf getInstance() {
		if(instance == null) {
			synchronized (SolInf.class) {
				if(instance == null) {
					instance = new SolInf();
				}
			}
		}
		
		return instance;
	}
	
	@Override
	public List<VoTipoDocumento> getTiposDeDocumento() {
		return getTiposDeDocumento(EModulos.getThisModulo());
	}

	@Override
	public List<VoTipoDocumento> getTiposDeDocumento(int intIdSolicitud) {
		return getTiposDeDocumento(EModulos.getThisModulo(), intIdSolicitud);
	}
	
	@Override
	public List<VoTipoDocumento> getTiposDeDocumento(EModulos modulo) {
		return getTiposDeDocumento(modulo, 0);
	}

	private List<VoTipoDocumento> getTiposDeDocumento(EModulos modulo, int intIdSolicitud) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<VoTipoDocumento> tiposdocumento = new ArrayList<VoTipoDocumento>();
		
		if (intIdSolicitud!=0) {
			strSQL.append("select td.id_tipodocto, td.id_modulo, td.vigente, std.id_soldoc, tipodocto=coalesce(coalesce(std.tipodocto, td.tipodocto), std.archivo_nombre), id_archivo=coalesce(std.id_archivo, 0), std.archivo_nombre, std.archivo_extension, std.fecha \n");
			strSQL.append("from eje_solinf_lote_solicitud_tipdocto std \n");
			strSQL.append("left join eje_solinf_tipodocto td on td.id_modulo=").append(modulo.getId()).append(" and std.id_tipodocto=td.id_tipodocto \n");
			strSQL.append("where std.id_solicitud=").append(intIdSolicitud).append(" \n");
			strSQL.append("order by coalesce(td.id_tipodocto, 999), coalesce(coalesce(std.tipodocto, td.tipodocto), std.archivo_nombre) \n");
		}else{
			strSQL.append("select id_soldoc=0, td.id_modulo, td.id_tipodocto, td.tipodocto, id_archivo=0, archivo_nombre=null, archivo_extension=null, fecha=null, td.vigente ");
			strSQL.append("from eje_solinf_tipodocto td \n");
			strSQL.append("where td.id_modulo=").append(modulo.getId()).append(" \n");
			strSQL.append("order by td.tipodocto \n");
		}

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				EModulos emodulo = EModulos.getModuloById(data.getInt("id_modulo"));
				tiposdocumento.add(new VoTipoDocumento(data.getInt("id_soldoc"), data.getInt("id_tipodocto"), data.getString("tipodocto"), emodulo, (data.getInt("vigente")==1), null,  data.getInt("id_archivo"), data.getString("archivo_nombre"), data.getString("archivo_extension")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tiposdocumento;
	}
	
	@Override
	public boolean putTiposDeDocumento(List<VoTipoDocumento> tiposdocto) {

		boolean ok = true;
		
		StringBuilder strSQL = new StringBuilder();
		
		try {
			strSQL.append("declare @data table (id_tipodocto int) \n\n");
			for (VoTipoDocumento tipodocto: tiposdocto) {
				if (tipodocto.getId()!=0) {
					strSQL.append("insert into @data values(").append(tipodocto.getId()).append(") \n");
				}
			}
			strSQL.append("\n")
			.append("delete t \n")
			.append("from eje_solinf_tipodocto t \n")
			.append("left join @data x on x.id_tipodocto=t.id_tipodocto \n")
			.append("where x.id_tipodocto is null \n")
			.append("\n");
			
			ConsultaTool.getInstance().update("portal", strSQL.toString());
			
			for (VoTipoDocumento tipodocto: tiposdocto) {
				if (ok) {
					strSQL = new StringBuilder();
					if (tipodocto.getId()!=0) {
						strSQL.append("update eje_solinf_tipodocto set tipodocto=?, vigente=? where id_tipodocto=?");
						Object[] params = {tipodocto.getNombre(), tipodocto.getVigente(), tipodocto.getId()};
						ConsultaTool.getInstance().update("portal", strSQL.toString(), params);
					}else {
						strSQL.append("insert into eje_solinf_tipodocto (id_modulo, tipodocto, vigente) values (?, ?, ?)");
						Object[] params = {EModulos.getThisModulo().getId(), tipodocto.getNombre(), tipodocto.getVigente()};
						ConsultaTool.getInstance().insert("portal", strSQL.toString(), params);
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	@Override
	public VoLote getLoteBySolicitud(VoSolicitud solicitud) {
		return getLoteBySolicitud(null, solicitud);
	}

	@Override
	public VoLote getLoteBySolicitud(Connection conn, VoSolicitud solicitud) {
		
		StringBuilder strSQL = new StringBuilder();
		
		int intIdLote = 0;
		
		strSQL.append("select id_lote from eje_solinf_lote_solicitud where id_solicitud=? ");
		
		Object[] params = {solicitud.getId()};
		
		ConsultaData data = null;
		try {
			if (conn!=null) {
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString(), params);
			}else {
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (data!=null && data.next()) {
			intIdLote = data.getInt("id_lote");
		}

		return new VoLote(intIdLote);

	}
	
	@Override
	public boolean putLote(IOClaseWeb io, Connection conn, VoLote lote) {

		boolean ok = true;
		
		StringBuilder strSQL = new StringBuilder();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		//Crear Lote
		strSQL.append("insert into eje_solinf_lote (id_modulo, descripcion, fecha, id_solicitante, id_estado) values (?,?,?,?,?) \n")
		.append("select id_lote=@@identity");
		
		Object[] params = {
			lote.getModulo().getId(),
			lote.getDescripcion(),
			lote.getFecha(),
			lote.getSolicitante(),
			lote.getEstado().getId()
		};
		
		try {
			
			ConsultaData data = ConsultaTool.getInstance().getData(conn, strSQL.toString(), params);
			while (data!=null && data.next()) {
				lote.setIdLote(Validar.getInstance().validarInt(data.getForcedString("id_lote")));
			}
			
			//Agregar Solicitudes
			for (VoSolicitud solicitud: lote.getSolicitudes()) {
				strSQL = new StringBuilder();
				strSQL.append("insert into eje_solinf_lote_solicitud (id_lote, id_persona, email, detalle, fecha_tope, id_estado) values (?,?,?,?,?,?) ")
				.append("select id_solicitud=@@identity");
				Object[] sparams = {
						lote.getId(),
						solicitud.getPersona().getId(),
						solicitud.getEMail(),
						solicitud.getDetalle(),
						solicitud.getFecha(),
						solicitud.getEstado().getId()
					};
				ConsultaData dtaSoliciutd = ConsultaTool.getInstance().getData(conn, strSQL.toString(), sparams);
				while (dtaSoliciutd!=null && dtaSoliciutd.next()) {
					solicitud.setId(Validar.getInstance().validarInt(dtaSoliciutd.getForcedString("id_solicitud")));
				}
				
				SolInfMail.getInstance().sendMailSolicitud(io, EEstado.solicitado, lote, solicitud);
				
				//Agregar documentos tipos de solicitados
				for (VoTipoDocumento tipodocumento: solicitud.getTiposDocto()) {
					strSQL = new StringBuilder();
					strSQL.append("insert into eje_solinf_lote_solicitud_tipdocto (id_solicitud, id_tipodocto, tipodocto, id_estado) values (?,?,?,?) ");
					Object[] tparams = {
							solicitud.getId(),
							tipodocumento.getId(),
							tipodocumento.getNombre(),
							tipodocumento.getEstado().getId()
						};
					ConsultaTool.getInstance().insert(conn, strSQL.toString(), tparams);
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	@Override
	public List<VoSolicitud> getSolicitudes(Connection conn, VoLote lote) {

		List<VoSolicitud> solicitudes = new ArrayList<VoSolicitud>();

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select id_solicitud from eje_solinf_lote_solicitud where id_lote=? order by id_solicitud");
		
		Object[] params = {lote.getId()};
		
		try {
			ConsultaData data = null;
			if (conn==null) {
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			}else {
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString(), params);
			}
			while (data!=null && data.next()) {
				solicitudes.add(new VoSolicitud(data.getInt("id_solicitud")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return solicitudes;
		
	}

	@Override
	public boolean updSolicitud(IOClaseWeb io, VoSolicitud solicitud) {
		return updSolicitud(io, null, solicitud);
	}
	
	@Override
	public boolean updSolicitud(IOClaseWeb io, Connection conn, VoSolicitud solicitud) {

		boolean ok = false;
		
		StringBuilder strSQL = new StringBuilder();
		SolInfMail sinfMail = new SolInfMail();
		
		strSQL.append("update eje_solinf_lote_solicitud set id_informador=?, respuesta=?, fecha_respuesta=getdate(), id_estado=? \n")
		.append("where id_solicitud=? ");
		
		Object[] params = {
				solicitud.getInformador().getId(),
				solicitud.getRespuesta(),
				solicitud.getEstado().getId(),
				solicitud.getId()
		};
		
		try {
			if (conn!=null) {
				ConsultaTool.getInstance().update(conn, strSQL.toString(), params);
			}else {
				ConsultaTool.getInstance().update("portal", strSQL.toString(), params);
			}
			
			for(VoTipoDocumento tipodocto: solicitud.getTiposDocto()) {
				strSQL = new StringBuilder();
				List<Object> ptd = new ArrayList<Object>();
				
				ptd.add(tipodocto.getIdArchivo());
				ptd.add(tipodocto.getArchivoNombre());
				ptd.add(tipodocto.getArchivoExtension());
				ptd.add(EEstado.enviado.getValor());
				
				if (tipodocto.getIdSolDoc()==0) {
					strSQL.append("insert into eje_solinf_lote_solicitud_tipdocto (id_archivo, archivo_nombre, archivo_extension, fecha, id_estado, id_solicitud) values (?,?,?,getDate(),?,?) \n");
					ptd.add(solicitud.getId());
				}else {
					strSQL.append("update eje_solinf_lote_solicitud_tipdocto set id_archivo=?, archivo_nombre=?, archivo_extension=?, fecha=getDate(), id_estado=? \n");
					strSQL.append("where id_soldoc=? ");
					ptd.add(tipodocto.getIdSolDoc());
				}

				if (conn!=null) {
					ConsultaTool.getInstance().update(conn, strSQL.toString(), ptd.toArray());
				}else {
					ConsultaTool.getInstance().update("portal", strSQL.toString(), ptd.toArray());
				}

			}
			
			VoLote lote = getLoteBySolicitud(conn, solicitud);
			VoEstado estado = getLoteEstado(conn, lote, true);
			lote.setEstado(estado);
			
			ok = updLote(conn, lote);
			if (ok) {
				ok = sinfMail.sendMailRespuesta(io, EEstado.enviado, lote, solicitud);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}
	
	private VoEstado getLoteEstado(Connection conn, VoLote lote, boolean bolSegunSolicitudes) {

		StringBuilder strSQL = new StringBuilder();
		
		VoEstado estado = null;
		
		if (bolSegunSolicitudes) {
			strSQL.append("select id_estado from eje_solinf_lote_solicitud where id_lote=? ");
		}else {
			strSQL.append("select id_estado from eje_solinf_lote where id_lote=? ");
		}
		Object[] params = {lote.getId()};
		
		ConsultaData data = null;
		
		try {
			if (conn==null) {
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			}else {
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString(), params);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (data!=null && data.next()) {
			if (estado==null) {
				estado = new VoEstado(data.getInt("id_estado"));
			}
			if (data.getInt("id_estado")!=estado.getId()) {
				estado = new VoEstado(EEstado.enproceso);
			}
		}
		
		return estado;

	}

	@Override
	public boolean updLote(VoLote lote) {
		return updLote(null, lote);
	}

	@Override
	public boolean updLote(Connection conn, VoLote lote) {

		boolean ok = false;

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("update eje_solinf_lote set id_estado=? \n")
		.append("where id_lote=? ");
		
		Object[] params = {
				lote.getEstado().getId(),
				lote.getId()
		};
		
		try {
			if (conn!=null) {
				ConsultaTool.getInstance().update(conn, strSQL.toString(), params);
			}else {
				ConsultaTool.getInstance().update("portal", strSQL.toString(), params);
			}
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
	}


	
	

}
