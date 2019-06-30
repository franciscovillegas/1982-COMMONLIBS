package intranet.com.eje.qsmcom.manager;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import intranet.com.eje.qsmcom.estructuras.DetalleComisiones;
import intranet.com.eje.qsmcom.estructuras.ParametrosObligatorios;
import intranet.com.eje.qsmcom.estructuras.Periodo;
import intranet.com.eje.qsmcom.estructuras.Rol;
import intranet.com.eje.qsmcom.estructuras.Trabajador;
import intranet.com.eje.qsmcom.interfaces.IMensajeUsuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import portal.com.eje.datos.Consulta;
import portal.com.eje.datos.Managers;
import portal.com.eje.serhumano.menu.Tools;
import portal.com.eje.tools.ArrayFactory;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.Varios;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.service.GatewayJndi;
import cl.eje.qsmcom.service.GatewayManager;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;


public class QSMComManager extends Managers {

	public static Permiso	PUEDE_LOGEAR				= new Permiso("cexterno");
	public static Permiso	PUEDE_ENTRAR_PORTAL			= new Permiso("sh");
	public static Permiso	PUEDE_ACCEDER_SISTEMA_SQM	= new Permiso("qsmcom_eje");
	public static String	PASSWORD_MAESTRO			= "_1_43ER.";
	
	public QSMComManager(Connection conex) {
		super(conex);

	}

	synchronized public int getInstanciaTrabajadorJerarquia(String rut) {
		Periodo periodo = getPeriodoValido();
		int newId = getMaxIdTrabajadorJerarquia() + 1;
		String sql = "insert into eje_qsmcom_trackeo_trabajador(id_subida, cronometro_seg , rut, fecha, periodo, tipo_proceso , vigente) ";
		sql += " values (?,0,?,getdate(),?,?,?)";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, newId);
			pst.setString(2, rut);
			pst.setInt(3, periodo.getPeriodo());
			pst.setInt(4, periodo.getIdTipo());
			pst.setInt(5, 1);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}

	synchronized public int getInstanciaTrabajadorComisiones(String rut, int idFile) {
		Periodo periodo = getPeriodoValido();
		int newId = -1;
		try {
				newId = getMaxIdTrabajadorComision() + 1;				
				String sql = "insert into eje_qsmcom_trackeo_dato (id_subida, tiempo_proceso, rut, fecha, periodo , tipo_proceso, subida_numero, id_file) ";
				sql += " values (?,0,?,getdate(),?,?,1,?)";
				try {
					PreparedStatement pst = super.conexion.prepareStatement(sql);
					pst.setInt(1, newId);
					pst.setString(2, rut);
					pst.setInt(3, periodo.getPeriodo());
					pst.setInt(4, periodo.getIdTipo());
					pst.setInt(5, idFile);
					pst.executeUpdate();
					pst.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return newId;
	}
	
	public String getIdSubidaFromTicket(String idTicket) {
		String idSubida = "-1";
		String sql = " SELECT id_subida FROM EJE_QSMCOM_TRACKEO_TICKET where id_ticket =  ?"; 
		ResultSet rs = null;
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setString(1, idTicket);
			rs = pst.executeQuery();
			if(rs.next()) {
				idSubida = rs.getString("id_subida");
			}
			rs.close();
			pst.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return idSubida;
	}
	
	synchronized public Timestamp getInstanciaTrabajadorDetComisiones(String rut, String nombreArchivo, String periodo, int idFile) {
		Timestamp date = new Timestamp(Varios.getInstanceCalendar().getTimeInMillis());
		try {
			String sql = "insert eje_qsmcom_detalle_com (fecha_subida , rut , nombre_archivo , correctos , incorrectos, periodo, id_file ) ";
			sql += " values (?,?,?,?,?,?,?)";
			try {
				PreparedStatement pst = super.conexion.prepareStatement(sql);
				pst.setTimestamp(1, date);
				pst.setString(2, rut);
				pst.setString(3, nombreArchivo);
				pst.setInt(4, 0);
				pst.setInt(5, 0);
				pst.setString(6, periodo);
				pst.setInt(7, idFile);
				pst.executeUpdate();
				pst.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Guarda el nombre de las columnas del archivo de QSM Detalle Comisión *
	 * */
	public void addNombreColumnasDetComisiones(IMensajeUsuario msg, Timestamp dateKey, ArrayList nombres) {
		for (int i = 0; i < nombres.size(); i++) {
			addNombreColumnaDetComisionesSimple(msg, dateKey, i, nombres.get(i).toString());
		}
	}

	/**
	 * Guarda el nombre de la columna del archivo de QSM Detalle Comisión,
	 * funciona junto con addNombreColumnasDetComisiones(Date dateKey,ArrayList
	 * nombres)
	 * 
	 * @see addNombreColumnasDetComisiones()
	 * 
	 * */
	private void addNombreColumnaDetComisionesSimple(IMensajeUsuario msg, Timestamp dateKey, int posX, String nombre) {
		try {
			String sql = "INSERT INTO eje_qsmcom_detalle_com_columnas(fecha_subida, pos_x , nombre) VALUES (?,?,?)";
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setTimestamp(1, dateKey);
			pst.setInt(2, posX);
			pst.setString(3, nombre);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			msg.addMsg("(SQLException)Error al guardar el dato : " + posX, IMensajeUsuario.ERROR);
			e.printStackTrace();
		}
		catch (Exception e) {
			msg.addMsg("(Exception)Error al guardar el dato : " + posX, IMensajeUsuario.ERROR);
			e.printStackTrace();
		}
	}

	public ResultSet getReporte_estadoSolicitudes() throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select r.id_req ,r.fecha_req as fecha_creacion, r.fecha_fin_req as fecha_finaliza, ");
		sql.append(" 	r.rut_responsable as rut_genero_ticket, ");
		sql.append(" 	ext.nombres as nombre_genero_ticket, s.descrip as estado, ");
		sql.append(" 	prod.producto as producto, ");
		sql.append(" 	event.evento as evento, ");
		sql.append(" 	suceso.suceso as suceso, ");
		sql.append(" 	case r.id_status ");
		sql.append(" 		when 3 then '' ");
		sql.append(" 		when 1 then resp.nombres ");
		sql.append(" 	end  as quien_tiene_el_ticket ");
		sql.append(" from eje_wf_requerimientos r left outer join eje_wf_status s on r.id_status = s.id_status ");
		sql.append(" 	left outer join eje_wf_tripleta_producto prod on prod.id_producto = r.id_producto ");
		sql.append(" 	left outer join eje_wf_tripleta_evento event on event.id_evento = r.id_evento and event.id_producto = r.id_producto ");
		sql.append(" 	left outer join eje_wf_tripleta_suceso suceso on suceso.id_suceso = r.id_suceso and suceso.id_evento = r.id_evento and suceso.id_producto = r.id_producto ");
		sql.append(" 	left outer join eje_wf_usuario_ext ext  on ext.rut = r.rut_responsable ");
		sql.append(" 	left outer join eje_wf_usuario_ext resp on resp.rut = r.rut_rol ");
		sql.append(" where r.id_producto in (").append(ParametrosObligatorios.IDPRO_COMISION_APROBADA).append(",").append(ParametrosObligatorios.IDPRO_COMISION_RECHAZADA).append(")");
		sql.append(" order by r.id_req ");
		PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
		return pst.executeQuery();
	}
	
	public ResultSet getReporte_estadoSolicitudesVSTrabajador() throws SQLException {	
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select r.id_req ,r.fecha_req as fecha_creacion, r.fecha_fin_req as fecha_finaliza, ");
		sql.append(" 	r.rut_responsable as rut_genero_ticket, ");
		sql.append(" 	ext.nombres as nombre_genero_ticket, s.descrip as estado, ");
		sql.append(" 	prod.producto as producto, ");
		sql.append(" 	event.evento as evento, ");
		sql.append(" 	suceso.suceso as suceso, ");
		sql.append(" 	case r.id_status ");
		sql.append(" 		when 3 then '' ");
		sql.append(" 		when 1 then resp.nombres ");
		sql.append(" 	end  as quien_tiene_el_ticket ");
		sql.append(" from eje_wf_requerimientos r left outer join eje_wf_status s on r.id_status = s.id_status ");
		sql.append(" 	left outer join eje_wf_tripleta_producto prod on prod.id_producto = r.id_producto ");
		sql.append(" 	left outer join eje_wf_tripleta_evento event on event.id_evento = r.id_evento and event.id_producto = r.id_producto ");
		sql.append(" 	left outer join eje_wf_tripleta_suceso suceso on suceso.id_suceso = r.id_suceso and suceso.id_evento = r.id_evento and suceso.id_producto = r.id_producto ");
		sql.append(" 	left outer join "+GatewayManager.getInstance().jndi(GatewayJndi.portal)+"..eje_ges_trabajador ext  on ext.rut = r.rut_responsable ");
		sql.append(" 	left outer join "+GatewayManager.getInstance().jndi(GatewayJndi.portal)+"..eje_ges_trabajador resp on resp.rut = r.rut_rol ");
		sql.append(" where r.id_producto in (").append(ParametrosObligatorios.IDPRO_COMISION_APROBADA).append(",").append(ParametrosObligatorios.IDPRO_COMISION_RECHAZADA).append(")");
		sql.append(" order by r.id_req ");
		PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
		return pst.executeQuery();
	}
	
	public ResultSet getReporte_maestroPersonal() throws SQLException {
		String sql = " select t.rut as [RUT EJE] , ";
		sql += " 	(select top 1 DV from eje_wf_usuario_ext where rut = t.rut ) as [DIG_VER],";
		sql += " 	(select top 1 '=\"'+clave+'\"' from eje_wf_usuario_ext where rut = t.rut ) as [CLAVE],";
		sql += "    t.nombre as [NOMBRE EJE], t.email AS [E-MAIL EJE] , ";
		sql += " 	t.direccion AS [DIRECCION PARTICULAR] , t.telefono AS [TELEFONO] , ";
		sql += " 	t.banco AS [BANCO], t.cuenta AS [NUM CUENTA] , t.tipo_cuenta AS [TIPO CUENTA], ";
		sql += " 	t2.rut AS [RUT SUP] , t2.nombre AS [NOM SUP] , t2.email AS [E-MAIL SUP]";
		sql += " from eje_qsmcom_trabajador t ";
		sql += " 	left outer join eje_qsmcom_relacion_sup s on s.id_subida = t.id_subida and s.rut_trabajador = t.rut ";
		sql += " 	left outer join eje_qsmcom_trabajador t2  on t2.id_subida = t.id_subida and t2.rut = s.rut_supervisor ";
		sql += " where t.id_subida in (select max(id_subida) from eje_qsmcom_trabajador) ";
		sql += " order by  s.rut_supervisor ";
		PreparedStatement pst = super.conexion.prepareStatement(sql);
		return pst.executeQuery();
	}
	
	public ResultSet getReporte_detalleEstadisticoConMultiplesFilas() throws SQLException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select d.id_subida,d.fecha as [Fecha Subida Archivo Prod.], ");
		sql.append(" substring(convert(varchar,p.periodo),1,4) as año, "); 
		sql.append(" case substring(convert(varchar,p.periodo),5,2) "); 
		sql.append(" 	when '01' then 'enero' "); 
		sql.append(" 	when '02' then 'febrero' "); 
		sql.append(" 	when '03' then 'marzo' "); 
		sql.append(" 	when '04' then 'abril' "); 
		sql.append(" 	when '05' then 'mayo' "); 
		sql.append(" 	when '06' then 'junio' "); 
		sql.append(" 	when '07' then 'julio' "); 
		sql.append(" 	when '08' then 'agosto'  ");
		sql.append(" 	when '09' then 'septiembre' "); 
		sql.append(" 	when '10' then 'octubre' "); 
		sql.append(" 	when '11' then 'noviembre' "); 
		sql.append(" 	when '12' then 'diciembre' "); 
		sql.append(" end as mes, "); 
		sql.append(" case p.tipo_proceso "); 
		sql.append(" 	when 1 then 'Proceso' "); 
		sql.append(" 	when 2 then 'Finalizacion Proceso' "); 
		sql.append(" end as tipoProceso, "); 
		sql.append(" dato.rut AS [Rut Ejecutivo], ");
		sql.append(" case isnull(REQ.id_status,0) ");
		sql.append(" 	when 0 	then 'No decidió' ");
		sql.append(" 	when 1 	then 'Pendiente' ");
		sql.append(" 	when 3	then 'finalizado' ");
		sql.append(" end as estado ");
		sql.append(" FROM EJE_QSMCOM_TRACKEO_DATO  D "); 
		sql.append(" INNER JOIN eje_qsmcom_periodo P on P.tipo_proceso = D.tipo_proceso "); 
		sql.append(" INNER JOIN (SELECT DISTINCT id_subida,CAMPO9 as rut "); 
		sql.append(" 			 FROM EJE_QSMCOM_DATOFULL subdato ) AS dato on dato.id_subida = D.id_subida "); 
		sql.append(" LEFT OUTER JOIN EJE_QSMCOM_TRACKEO_TICKET TT ON tt.tipo_proceso = D.tipo_proceso "); 
		sql.append(" 												AND tt.id_subida = D.id_subida ");
		sql.append(" 												AND tt.rut = dato.rut ");
		sql.append(" LEFT OUTER JOIN eje_wf_requerimientos REQ ON REQ.rut_responsable = TT.rut and tt.id_ticket = req.id_req ");
		sql.append(" order by   d.fecha  \n");
		PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
		return pst.executeQuery();
	}
	
	public boolean esValido(Rol rol) {
		boolean ok = false;
		String sql = " select * from eje_wf_roles where id_rol = ? and status_rol = 1 ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, rol.getIdRol());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				ok = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	public String puedoSubirLasComisiones() {
		Periodo periodo = getPeriodoValido();
		if (periodo == null) {
			return "No existe un periodo vigente.";
		} 
		else if(periodo.getIdTipo() == 2) {
			if(getCantDeSubidasDeComisionesParaEstePeriodo() > 1) {
				return "Solo se permite subir el archivo una vez en la etapa de finalización";
			}
		}
		return "";
	}

	private int difDias(Date ini, Date fin) {
		return cantDias(ini.getTime()) - cantDias(fin.getTime());
	}

	private int cantDias(long millis) {
		int dias = (int) (millis / (1000 * 60 * 60 * 24));
		return dias;
	}
	
	private int getMaxIdTrabajadorJerarquia() {
		int newId = -1;
		String sql = " select isnull(max(id_subida),0) newid from eje_qsmcom_trackeo_trabajador ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				newId = rs.getInt("newid");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}

	private int getMaxIdTrabajadorComision() {
		int newId = -1;
		String sql = " select isnull(max(id_subida),0) newid from eje_qsmcom_trackeo_dato ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				newId = rs.getInt("newid");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}

	public boolean deleteTrabajadoresJerarquia() {
		boolean ok = true;
		int idSubida = getIdSubidaValida_trabajador();
		String sql = " delete from eje_qsmcom_relacion_sup where id_subida in (?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, idSubida);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		sql = " delete from eje_qsmcom_trabajador where id_subida in (?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, idSubida);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		sql = " delete from eje_qsmcom_trackeo_trabajador where id_subida in (?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, idSubida);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}

	public boolean deleteBloqueoSubidaTrabajadoresComisiones() {
		boolean ok = false;
		String sql = " update eje_qsmcom_trackeo_ticket set valido = 0";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean deleteHistorialMismoPeriodoFullDato(  int idSubida) {
		boolean ok = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM EJE_QSMCOM_DATOFULL ");
		sql.append(" WHERE id_subida in (SELECT id_subida ");
		sql.append(" 					 FROM EJE_QSMCOM_TRACKEO_DATO ");
		sql.append(" 					 WHERE periodo  = ").append(getPeriodoValido().getPeriodo());
		sql.append(" 							and id_subida not in (").append(idSubida).append("))");
		sql.append(" 	and not campo").append(DetalleComisiones.CAMPORUTEJE);
		sql.append(" 			 in (select rut from EJE_QSMCOM_TRACKEO_TICKET where id_subida = EJE_QSMCOM_DATOFULL.id_subida)");
		sql.append(" 	and not campo").append(DetalleComisiones.CAMPORUTSUP);
		sql.append(" 			 in (select rut from EJE_QSMCOM_TRACKEO_TICKET where id_subida = EJE_QSMCOM_DATOFULL.id_subida) ");
		sql.append(" 	and not campo").append(DetalleComisiones.CAMPORUTZONAL);
		sql.append(" 			 in (select rut from EJE_QSMCOM_TRACKEO_TICKET where id_subida = EJE_QSMCOM_DATOFULL.id_subida) ");
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean deleteHistorialMismoPeriodoFullDetComisiones(Timestamp fecSubida) {
		boolean ok = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM EJE_QSMCOM_DETALLE_COM_DATOFULL "); 
		sql.append(" WHERE fecha_subida in (SELECT fecha_subida ");
		sql.append(" 						FROM EJE_QSMCOM_DETALLE_COM ");
		sql.append(" 					 WHERE periodo  =  ? ");
		sql.append(" 							and fecha_subida not in (?))");
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
			pst.setInt(1, getPeriodoValido().getPeriodo());
			pst.setTimestamp(2, fecSubida);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}
	
	public Periodo getPeriodoValido() {
		return getPeriodoValido(TipoCarga.calculo);
	}
	
	public Periodo getPeriodoValido(TipoCarga p) {
		String sql = " select top 1 periodo, tipo_proceso, fecha_ini , fecha_fin , (select nombre from eje_qsmcom_subperiodo where tipo_proceso = eje_qsmcom_periodo.tipo_proceso ) as tipo_desc, isnull(delay_acepta,0) as delay from eje_qsmcom_periodo where convert(varchar,fecha_ini,112) <= convert(varchar,getdate(),112) and convert(varchar,fecha_fin,112) >= convert(varchar,getdate(),112) and tipo_carga = ? order by tipo_proceso desc ";
		Periodo periodo = null;
		try {
			
			Object[] params = {p.getId()};
			ConsultaData data = ConsultaTool.getInstance().getData("mac", sql, params);
			Calendar calIni = Varios.getInstanceCalendar();
			Calendar calFin = Varios.getInstanceCalendar();
			if (data.next()) {
				calIni.setTimeInMillis(data.getDateJava("fecha_ini").getTime());
				calFin.setTimeInMillis(data.getDateJava("fecha_fin").getTime());
				periodo = new Periodo(data.getString("tipo_desc"), calFin, calIni, data.getInt("tipo_proceso"),
									 data.getInt("periodo"));
				periodo.setDelay(data.getInt("delay"));
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return periodo;
	}

	public Periodo getNextPeriodoValido() {
		String sql = " select top 1 periodo, tipo_proceso, fecha_ini , fecha_fin , 	 (select nombre from eje_qsmcom_subperiodo where tipo_proceso = eje_qsmcom_periodo.tipo_proceso ) as tipo_desc,   	 isnull(delay_acepta,0) as delay from eje_qsmcom_periodo where convert(varchar,fecha_ini,112) >= convert(varchar,getdate(),112) and tipo_carga = 2  order by fecha_ini ";
		Periodo periodo = null;
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			Calendar calIni = Varios.getInstanceCalendar();
			Calendar calFin = Varios.getInstanceCalendar();
			if (rs.next()) {
				calIni.setTimeInMillis(rs.getTimestamp("fecha_ini").getTime());
				calFin.setTimeInMillis(rs.getTimestamp("fecha_fin").getTime());
				periodo = new Periodo(rs.getString("tipo_desc"), calFin, calIni, rs.getInt("tipo_proceso"),
										rs.getInt("periodo"));
				periodo.setDelay(rs.getInt("delay"));
			}
			else {
				return null;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return periodo;
	}

	public Periodo getPeriodoValido(String idTicket) {
		String sql = " select top 1 periodo, tipo_proceso, fecha_ini , fecha_fin , (select nombre from eje_qsmcom_subperiodo where tipo_proceso = eje_qsmcom_periodo.tipo_proceso ) as tipo_desc, isnull(delay_acepta,0) as delay_acepta from eje_qsmcom_periodo where convert(varchar , fecha_ini , 112 ) <= ? and  convert(varchar , fecha_fin , 112) >= ? order by tipo_proceso desc ";
		Periodo periodo = null;
		try {
			Timestamp fechaticket = getFechaTicket(Integer.parseInt(idTicket));
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setTimestamp(1, fechaticket);
			pst.setTimestamp(2, fechaticket);
			ResultSet rs = pst.executeQuery();
			Calendar calIni = Varios.getInstanceCalendar();
			Calendar calFin = Varios.getInstanceCalendar();
			if (rs.next()) {
				calIni.setTimeInMillis(rs.getTimestamp("fecha_ini").getTime());
				calFin.setTimeInMillis(rs.getTimestamp("fecha_fin").getTime());
				periodo = new Periodo(rs.getString("tipo_desc"), calFin, calIni, rs.getInt("tipo_proceso"),
										rs.getInt("periodo"));
				periodo.setDelay(rs.getInt("delay_acepta"));
			}
			else {
				return null;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return periodo;
	}

	public String getPeriodoValido_str() {
		Periodo objPeriodo = getPeriodoValido();
		
		if(objPeriodo != null) {
			int periodo = objPeriodo.getPeriodo();
			int aaa = (int) periodo / 100;
			int mes = periodo % 100;
			return Tools.RescataMes(mes) + " del " + aaa + "  (" + objPeriodo.getTipoDesc() + ")";
		}
		else {
			return " - - - ";
		}
		
	}

	public String getPeriodoValido_str(String idTicket) {
		Periodo objPeriodo = getPeriodoValido(idTicket);
		try {
			if (objPeriodo != null) {
				int aaa = objPeriodo.getPeriodo() / 100;
				int mes = objPeriodo.getPeriodo() % 100;
				return Tools.RescataMes(mes) + " del " + aaa + "  (" + objPeriodo.getTipoDesc() + ")";
			}
			else {
				System.out.println("ERROR!!!! el ticket se creo en un periodo no vigente ");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean addTrabajador(Trabajador t) {
		boolean ok = true;
		int idSubida = getIdSubidaValida_trabajador();
		addUsuarioExterno(t);
		String sql = " insert into eje_qsmcom_trabajador(rut,digito_ver, nombre, email, direccion , telefono , id_subida, banco , cuenta, tipo_cuenta) ";
		sql += " values (? ,? ,? ,? , ? ,? ,? ,? ,? ,?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, t.getRut());
			pst.setString(2, t.getDig());
			pst.setString(3, t.getNombre());
			pst.setString(4, t.getMail());
			pst.setString(5, t.getDireccion());
			pst.setString(6, t.getTelefono());
			pst.setInt(7, idSubida);
			pst.setString(8, t.getBanco());
			pst.setString(9, t.getCuenta());
			pst.setString(10, t.getTipoCuenta());
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	public boolean addNombreColumnas(int posX, String nombre) {
		boolean ok = true;
		int idSubida = getIdSubidaValida_comision();
		String sql = " insert into eje_qsmcom_columna (id_subida, pos_x , nombre_col) values(?,?,?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, idSubida);
			pst.setInt(2, posX);
			pst.setString(3, nombre);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	public boolean addValorCeldaDetComisiones(Timestamp keyTime, int posX, int posY, String valor) throws SQLException {
		boolean ok = true;
		String sql = " insert into eje_qsmcom_detalle_com_dato ";
		sql += " (fila , fecha_subida , pos_x , valor ) ";
		sql += " values (?,?,?,?) ";
		PreparedStatement pst = super.conexion.prepareStatement(sql);
		pst.setInt(1, posY);
		pst.setTimestamp(2, keyTime);
		pst.setInt(3, posX);
		pst.setString(4, valor);
		pst.executeUpdate();
		pst.close();
		return ok;
	}

	public boolean addValorFullDetComisiones(Timestamp keyTime, int fila, String[] valores) throws SQLException {
		boolean ok = true;
		StringBuffer sql = new StringBuffer(" insert into eje_qsmcom_detalle_com_datofull ");
		sql.append(" (fila , fecha_subida , ");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" campo" + i);
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			else {
				sql.append(") \n ");
			}
		}
		sql.append(" values (?,?, ");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" ?");
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			else {
				sql.append(" \n ");
			}
		}
		sql.append(" ) ");
		PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
		pst.setInt(1, fila);
		pst.setTimestamp(2, keyTime);
		for (int i = 0; i < valores.length; i++) {
			pst.setString(i + 3, valores[i]);
		}
		pst.executeUpdate();
		pst.close();
		return ok;
	}

	public boolean addValorFullDato(int idSubida, int fila, String[] valores) throws SQLException {
		boolean ok = true;
		StringBuffer sql = new StringBuffer(" insert into EJE_QSMCOM_DATOFULL ");
		sql.append(" (fila , id_subida , ");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" campo" + i);
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			else {
				sql.append(") \n ");
			}
		}
		sql.append(" values (?,?, ");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" ?");
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			else {
				sql.append(" \n ");
			}
		}
		sql.append(" ) ");
		PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
		pst.setInt(1, fila);
		pst.setInt(2, idSubida);
		for (int i = 0; i < valores.length; i++) {
			pst.setString(i + 3, valores[i]);
		}
		pst.executeUpdate();
		pst.close();
		return ok;
	}

	public boolean transponerDetComisiones(Timestamp keyTime) {
		boolean ok = true;
		StringBuffer sql = new StringBuffer(
											"INSERT INTO EJE_QSMCOM_DETALLE_COM_DATO (FILA, FECHA_SUBIDA , POS_X , VALOR) ");
		try {
			for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
				sql.append("SELECT FILA,FECHA_SUBIDA,"
														+ i
														+ " AS POS_X,CAMPO"
														+ i
														+ " AS VALOR FROM EJE_QSMCOM_DETALLE_COM_DATOFULL WHERE FECHA_SUBIDA = ? \n");
				if (i + 1 < DetalleComisiones.CANTCAMPOS) {
					sql.append(" UNION \n ");
				}
			}
			PreparedStatement pst;
			pst = super.conexion.prepareStatement(sql.toString());
			for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
				pst.setTimestamp((i + 1), keyTime);
			}
			pst.executeUpdate();
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}

	public Timestamp getFechaTicket(int ticket) {
		Timestamp time = null;
		String sql = " select fecha_req from eje_wf_requerimientos where id_req =  ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, ticket);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				time = rs.getTimestamp("fecha_req");
				Calendar cal = Varios.getInstanceCalendar();
				cal.setTimeInMillis(time.getTime());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				time = new Timestamp(cal.getTimeInMillis());
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return time;
	}

	public int getIdSubidaValida_trabajador() {
		int idSubida = -1;
		String sql = " select max(id_subida) as id_subida from eje_qsmcom_trackeo_trabajador where periodo = ? and tipo_proceso = ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			Periodo periodo = getPeriodoValido();
			pst.setInt(1, periodo.getPeriodo());
			pst.setInt(2, periodo.getIdTipo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				idSubida = rs.getInt("id_subida");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			idSubida = -1;
		}
		return idSubida;
	}

	public int getIdSubidaValida_comision() {
		int idSubida = -1;
		String sql = " select max(id_subida) as id_subida from eje_qsmcom_trackeo_dato where periodo = ? and tipo_proceso = ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			Periodo periodo = getPeriodoValido();
			pst.setInt(1, periodo.getPeriodo());
			pst.setInt(2, periodo.getIdTipo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				idSubida = rs.getInt("id_subida");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			idSubida = -1;
		}
		catch (Exception e) {
			idSubida = -1;
		}
		return idSubida;
	}
	
	public Consulta getHistorialProduccion(String top) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT top ").append(top);
		sql.append(" 	d.id_subida, convert(varchar(16),d.fecha,120) as fecha, d.periodo, d.tipo_proceso, sub.nombre as proceso_desc, ");
		sql.append(" 	d.id_file, unic.name_original, unic.name_unic, ");
		sql.append(" 	proceso.fecha_ini, proceso.fecha_fin, proceso.correctos, proceso.malos, ");
		sql.append(" 	(select count(id_corr) from eje_qsmcom_proceso_log lg where proceso.id_proceso = lg.id_proceso ) as cantLog ");
		sql.append(" FROM eje_qsmcom_trackeo_dato d ");
		sql.append(" 	LEFT OUTER JOIN eje_qsmcom_periodo p on p.periodo = d.periodo and p.tipo_proceso = d.tipo_proceso ");
		sql.append(" 	LEFT OUTER JOIN eje_qsmcom_subperiodo sub on sub.tipo_proceso = d.tipo_proceso ");
		sql.append(" 	LEFT OUTER JOIN portal_credichile..eje_files_unico unic on unic.id_file = d.id_file ");
		sql.append(" 	LEFT OUTER JOIN eje_qsmcom_proceso proceso on proceso.id_subida =d.id_subida "); 
		sql.append(" ORDER BY d.periodo DESC "); 
		Consulta consulta = new Consulta(this.conexion);
		consulta.exec(sql.toString());
		return consulta;
	}
	
	public Consulta getHistorialDetComisiones(String top) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT top ").append(top);
		sql.append(" 	convert(varchar(16),d.fecha_subida,120) as fecha, d.periodo, "); 
		sql.append(" 	d.id_file, unic.name_original, unic.name_unic, ");
		sql.append(" 	proceso.fecha_ini, proceso.fecha_fin, proceso.correctos, proceso.malos, ");
		sql.append(" 	(select count(id_corr) from eje_qsmcom_proceso_log lg where proceso.id_proceso = lg.id_proceso ) as cantLog ");
		sql.append(" FROM eje_qsmcom_detalle_com d ");
		sql.append(" 	LEFT OUTER JOIN portal_credichile..eje_files_unico unic on unic.id_file = d.id_file ");
		sql.append(" 	LEFT OUTER JOIN eje_qsmcom_proceso proceso on proceso.fecha_subida =d.fecha_subida "); 
		sql.append(" ORDER BY d.periodo DESC "); 
		Consulta consulta = new Consulta(this.conexion);
		consulta.exec(sql.toString());
		return consulta;
	}

	public boolean addTrabajadorJerarquia(Trabajador eje, Trabajador sup) throws SQLException {
		boolean ok = true;
		int idSubida = getIdSubidaValida_trabajador();
		String sql = " insert into eje_qsmcom_relacion_sup (rut_trabajador, rut_supervisor, id_subida) values (?, ? ,?) ";
		PreparedStatement pst = super.conexion.prepareStatement(sql);
		pst.setInt(1, eje.getRut());
		pst.setInt(2, sup.getRut());
		pst.setInt(3, idSubida);
		pst.executeUpdate();
		pst.close();
		return ok;
	}
	
	public boolean tieneProduccion(int rut) {
		StringBuffer sql = new StringBuffer(" SELECT TOP 1 * ");
		sql.append(" FROM EJE_QSMCOM_DATOFULL ");
		sql.append(" WHERE ");
		sql.append("	 id_subida in (?)");
		sql.append("	 and  ( campo").append(DetalleComisiones.CAMPORUTEJE).append(" = ? ");
		sql.append(" 	 	  or campo").append(DetalleComisiones.CAMPORUTSUP).append(" = ? ");
		sql.append(" 	 	  or campo").append(DetalleComisiones.CAMPORUTZONAL).append(" = ? ) ");
		ResultSet rs = null;
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
			pst.setInt(1, getIdSubidaValida_comision());
			pst.setInt(2, rut);
			pst.setInt(3, rut);
			pst.setInt(4, rut);
			rs = pst.executeQuery();
			if(rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addUsuarioExterno(Trabajador eje) {
		boolean ok = true;
		String sql = " if(not exists(select 1 from eje_wf_usuario_ext where rut = ?)) ";
		sql += " insert into eje_wf_usuario_ext (rut,dv, nombres, ape_pat, ape_mat, clave) values (?,?,?,?,?,?) ";
		Validar valida = new Validar();
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, eje.getRut());
			pst.setInt(2, eje.getRut());
			pst.setString(3, eje.getDig());
			pst.setString(4, valida.validarDato(eje.getNombre(), ""));
			pst.setString(5, valida.validarDato(eje.getApe_pat(), ""));
			pst.setString(6, valida.validarDato(eje.getApe_mat(), ""));
			pst.setString(7, String.valueOf(eje.getRut()));
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	public void addMail(int rut, String mail) throws SQLException {
		String sql = " delete from eje_wf_usuario_correo where rut = ? ";
		PreparedStatement pst = super.conexion.prepareStatement(sql);
		pst.setInt(1, rut);
		pst.executeUpdate();
		pst.close();
		sql = " insert into eje_wf_usuario_correo (rut, correo , valido) values ( ? , ? , ? ) ";
		pst = super.conexion.prepareStatement(sql);
		pst.setInt(1, rut);
		pst.setString(2, mail);
		pst.setInt(3, 1);
		pst.executeUpdate();
		pst.close();
	}

	public Trabajador getTrabajadorSubido(int rut) throws Exception {
		return getTrabajadorSubido(rut, true);
	}

	public Trabajador getTrabajadorSubido(int rut, boolean preguntarSoloEnElPeriodoValido) throws Exception {
		Trabajador t = null;
		String sql = " select top 1 rut, digito_ver , nombre ,email, direccion , telefono , banco , cuenta, tipo_cuenta ";
		sql += " from eje_qsmcom_trabajador ";
		sql += " where rut = ? ";
		if (preguntarSoloEnElPeriodoValido) {
			sql += " and id_subida = ? ";
		}
		sql += " order by id_subida desc ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, rut);
			if (preguntarSoloEnElPeriodoValido) {
				pst.setInt(2, getIdSubidaValida_trabajador());
			}
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				String rutdig = String.valueOf(rut) + rs.getString("digito_ver");
				String nombre = rs.getString("nombre");
				String email = rs.getString("email");
				String direccion = rs.getString("direccion");
				String telefono = rs.getString("telefono");
				String tipoCuenta = rs.getString("tipo_cuenta");
				ArrayList roles = getRoles(rut);
				t = new Trabajador(rutdig, nombre, email, direccion, telefono, roles);
				t.setBanco(rs.getString("banco"));
				t.setCuenta(rs.getString("cuenta"));
				t.setTipoCuenta(tipoCuenta);
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}

	public Trabajador getTrabajadorExterno(int rut) throws Exception {
		Trabajador t = null;
		String sql = " select ue.rut, ue.dv as digito_ver, ue.nombres , ue.clave , c.correo as mail from eje_wf_usuario_ext ue left outer join eje_wf_usuario_correo c on c.rut = ue.rut  where ue.rut = ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, rut);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				String rutdig = String.valueOf(rut) + rs.getString("digito_ver");
				String nombre = rs.getString("nombres");
				String email = rs.getString("mail");
				ArrayList roles = getRoles(rut);
				t = new Trabajador(rutdig, nombre, email, "", "", roles);
				t.setClave(rs.getString("clave"));
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}


	private void delUsuarioExterno(int rut) throws SQLException {
		String sql = " delete from eje_wf_usuario_ext where rut = ? ";
		PreparedStatement pst = super.conexion.prepareStatement(sql);
		pst.setInt(1, rut);
		pst.executeUpdate();
		pst.close();
	}

	public void updateDatosTrabajadorExt(int rut, String campo, String valor) {
		String sql = " update eje_wf_usuario_ext set " + campo + "= ? where rut = ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, valor);
			pst.setInt(2, rut);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateDatosTrabajador(int rut, String campo, String valor) {
		String sql = " update eje_qsmcom_trabajador set " + campo + "= ? where rut = ? and id_subida = ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, valor);
			pst.setInt(2, rut);
			pst.setInt(3, getIdSubidaValida_trabajador());
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList getRoles(int rut) {
		ArrayList roles = new ArrayList();
		String sql = " select distinct ru.id_rol, r.desc_rol as nombre , r.glosa_rol as descripcion  from eje_wf_tupla_rol_usuario ru inner join eje_wf_roles r on r.id_rol = ru.id_rol where ru.rut = ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, rut);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre"), rs.getString("descripcion")));
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return roles;
	}

	public int getRutResponsable(int idTicket) {
		int rut = -1;
		String sql = " select rut_responsable from eje_wf_requerimientos where id_req =  ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, idTicket);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				rut = rs.getInt("rut_responsable");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return rut;
	}

	public boolean yaGereneroTicketParaPeriodo(int periodo, TipoCarga c, int rut) {
		boolean ok = false;
		int indice = ManagerQSM.getInstance().getLastSubidaPorRut_PeriodoActual(c,rut);
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ");
		sql.append(" FROM eje_qsmcom_trackeo_ticket t "); 
		sql.append("  INNER JOIN eje_qsmcom_periodo p on p.periodo = t.periodo");
		sql.append(" WHERE t.periodo = ? and t.rut = ? and p.tipo_carga = ? and id_subida = ? and t.valido = 1 ");

		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql.toString());
			pst.setInt(1, periodo);
			pst.setInt(2, rut);
			pst.setInt(3, c.getId());
			pst.setInt(4, indice);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				ok = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	public boolean yaSubioTrabajadoresParaEstePeriodo() {
		boolean ok = false;
		String sql = " select 1 from eje_qsmcom_trackeo_trabajador where tipo_proceso = ? and periodo = ? ";
		Periodo periodo = getPeriodoValido();
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, periodo.getIdTipo());
			pst.setInt(2, periodo.getPeriodo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				ok = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	public boolean yaSubioComisionesParaEstePeriodo() {
		boolean ok = false;
		String sql = " select 1 from eje_qsmcom_trackeo_dato where tipo_proceso = ? and periodo = ? ";
		Periodo periodo = getPeriodoValido();
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, periodo.getIdTipo());
			pst.setInt(2, periodo.getPeriodo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				ok = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	public int getCantDeSubidasDeComisionesParaEstePeriodo() {
		int cant = 0;
		String sql = " select COUNT(*) as cantidad from eje_qsmcom_trackeo_dato where tipo_proceso = ? and periodo = ? ";
		Periodo periodo = getPeriodoValido();
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, periodo.getIdTipo());
			pst.setInt(2, periodo.getPeriodo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return cant;
	}

	public int getCantMaximaDeDiasDeDelayParaEstePeriodo() {
		int cant = -1;
		String sql = " select delay_acepta from eje_qsmcom_periodo where periodo = ? and tipo_proceso = 1 ";
		Periodo periodo = getPeriodoValido();
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setInt(1, periodo.getPeriodo());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("delay_acepta");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return cant;
	}

	public void addTrackeoTicketParaEstePeriodo(int rut, int idreq, TipoCarga c) {
		int idSubida = -1; 
		String sql = " insert into eje_qsmcom_trackeo_ticket (periodo, tipo_proceso , tipo_carga, rut, id_subida , id_ticket , valido) values (?,?,?,?,?,?,1) ";
		
		
		int idTipoProceso = -1;
		int idTipoCarga   = -1;
		int periodo = -1;
		
		Periodo p =  ManagerQSM.getInstance().getPeriodoValido(c);
		if(p != null) {
			idSubida = ManagerQSM.getInstance().getLastSubidaPorRut(p.getPeriodo(), rut);
		
			ConsultaData data = ManagerQSM.getInstance().getProceso_FromIdSubida(idSubida);
			if(data.next()) {
				periodo	= data.getInt("id_periodo");
				idTipoProceso =  data.getInt("idTipoProceso");
				idTipoCarga =  data.getInt("idTipoCarga");
			}
			
			try {
				PreparedStatement pst = super.conexion.prepareStatement(sql);
				pst.setInt(1, periodo);
				pst.setInt(2, idTipoProceso);
				pst.setInt(3, idTipoCarga);
				pst.setInt(4, rut);
				pst.setInt(5, idSubida);
				pst.setInt(6, idreq);
				pst.executeUpdate();
				pst.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public String comentariosRequerimientos(String requerimiento, Connection con) {
		String tiene = "";
		try {
			Consulta consultatmp = new Consulta(con);
			String sql = "select convert(varchar(10),actual_transaction_datetime,103)+ ' ' + convert(varchar(5),actual_transaction_datetime,114) fecha, "
													+
													"comentario from eje_wf_actual_transactions where id_req = "
													+ requerimiento + " and comentario is not null " +
													"order by actual_transaccion_id";
			consultatmp.exec(sql);
			while (consultatmp.next()) {
				tiene += consultatmp.getString("fecha") + " : " + consultatmp.getString("comentario") + "<br>";
			}
			consultatmp.close();
		}
		catch (Exception e) {
			System.out.println("<-----No se pudo saber si el usuario tiene requerimientos \n" + e);
		}
		return tiene;
	}
	
	public HashMap getNombreColumnas(int idSubida) {
		String sql = " select pos_x, nombre_col from EJE_QSMCOM_COLUMNA where id_subida = ? ";
		HashMap map = new HashMap();
		try {
			PreparedStatement pst = conexion.prepareStatement(sql);
			pst.setInt(1, idSubida);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				map.put(rs.getString("pos_x"), rs.getString("nombre_col"));
			}
			rs.close();
			pst.close();
		}
		catch (Exception e) {
			System.out.println("<-----No se pudo saber si el usuario tiene requerimientos \n" + e);
		}
		return map;
	}
	
	public HashMap getNombreColumnasDetalleComision(String periodo) {
		String sql = " select POS_X, NOMBRE from EJE_QSMCOM_DETALLE_COM_COLUMNAS ";
		sql += " where fecha_subida in (select max(fecha_subida) ";
		sql += " 						from EJE_QSMCOM_DETALLE_COM ";
		sql += "						where periodo = ? ) ";
		HashMap map = new HashMap();
		try {
			PreparedStatement pst = conexion.prepareStatement(sql);
			if(periodo == null ) {
				pst.setString(1, getLastPeriodoDetalleComisiones());
			}
			else {
				pst.setString(1, periodo);
			}
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				map.put(rs.getString("POS_X"), rs.getString("NOMBRE"));
			}
			rs.close();
			pst.close();
		}
		catch (Exception e) {
			System.out.println("<-----No se pudo saber si el usuario tiene requerimientos \n" + e);
		}
		return map;
	}
	
	public SimpleList getPeriodosDisponiblesOseleccionadoUF(String defPeriodo, boolean fueLoaded) {
		SimpleList lista = new SimpleList();
		if (!fueLoaded) {
			Calendar cal = Varios.getInstanceCalendar();
			SimpleHash hash = null;
			cal.add(Calendar.MONTH, -5);
			for (int i = 0; i < 12; i++) {
				String mes = Varios.rellenaCadena(String.valueOf(cal.get(Calendar.MONTH) + 1), '0', 2);
				String aaa = String.valueOf(cal.get(Calendar.YEAR));
				if (getPeriodoUF(aaa + mes) == null) {
					hash = new SimpleHash();
					hash.put("id", aaa + mes);
					hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
					lista.add(hash);
				}
				cal.add(Calendar.MONTH, 1);
			}
		}
		else {
			SimpleHash hash = new SimpleHash();
			String aaa = defPeriodo.substring(0, 4);
			String mes = defPeriodo.substring(4, 6);
			hash.put("id", aaa + mes);
			hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
			lista.add(hash);
		}
		return lista;
	}

	public SimpleList getPeriodosDisponiblesOseleccionado(String defPeriodo, boolean fueLoaded) {
		SimpleList lista = new SimpleList();
		if (!fueLoaded) {
			Calendar cal = Varios.getInstanceCalendar();
			SimpleHash hash = null;
			for (int i = 0; i < 12; i++) {
				String mes = Varios.rellenaCadena(String.valueOf(cal.get(Calendar.MONTH) + 1), '0', 2);
				String aaa = String.valueOf(cal.get(Calendar.YEAR));
				if (!yaExisteElPeriodo(aaa + mes)) {
					hash = new SimpleHash();
					hash.put("id", aaa + mes);
					hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
					lista.add(hash);
				}
				cal.add(Calendar.MONTH, 1);
			}
		}
		else {
			SimpleHash hash = new SimpleHash();
			String aaa = defPeriodo.substring(0, 4);
			String mes = defPeriodo.substring(4, 6);
			hash.put("id", aaa + mes);
			hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
			lista.add(hash);
		}
		return lista;
	}

	public SimpleList getPeriodoSeleccionadoDetalle(String pFecInicio, String pFecFin,
													String rFecInicio, String rFecFin, String delay_acepta) {
		SimpleList lista = new SimpleList();
		SimpleHash hash = null;
		hash = new SimpleHash();
		hash.put("fecInicioProceso", pFecInicio);
		hash.put("fecTerminoProceso", pFecFin);
		hash.put("diasExtension", delay_acepta == null ? "0" : delay_acepta);
		hash.put("fecInicioReProceso", rFecInicio);
		hash.put("fecTerminoReProceso", rFecFin);
		lista.add(hash);
		return lista;
	}

	public ResultSet getTrabajadoresFullDetalleComisionesRsTodos(PortalManager mgr, int rutTrabaja, boolean premios) {
		String nexo = premios ? "=" : "<>";
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct com.periodo ");
		sql.append(" FROM eje_qsmcom_detalle_com com ");
		sql.append(" WHERE NOT com.periodo IS NULL ");
		sql.append(" 	and com.fecha_subida in ");
		sql.append(" 	(select f.fecha_subida ");
		sql.append(" 	 from eje_qsmcom_detalle_com_datofull f ");
		sql.append(" 	 where ( f.campo" + DetalleComisiones.CAMPORUTEJE + " = '" + rutTrabaja + "'  \n");
		sql.append(" 	 		 OR f.campo" + DetalleComisiones.CAMPORUTSUP + " = '" + rutTrabaja + "'  \n");
		sql.append(" 	 		 OR f.campo" + DetalleComisiones.CAMPORUTZONAL + " = '" + rutTrabaja + "' ) \n");
		sql.append(" 		    and  f.fecha_subida in (select max(com2.fecha_subida) ");
		sql.append(" 							 		from eje_qsmcom_detalle_com com2 ");
		sql.append(" 									where com2.periodo = com.periodo)) order by com.periodo desc");
		PreparedStatement pst;
		ResultSet rs = null;
		try {
			pst = conexion.prepareStatement(sql.toString());
			rs = pst.executeQuery();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ArrayList getTrabajadoresFullDetalleComisionesArrayList(PortalManager mgr, int rutTrabaja, String periodo) {
		ResultSet rs = getTrabajadoresFullDetalleComisionesRs(mgr, rutTrabaja, periodo);
		ArrayList lista = new ArrayList();
		HashMap map = null;
		try {
			while (rs.next()) {
				map = new HashMap();
				for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
					map.put(String.valueOf(i), rs.getString(i + 1));
				}
				lista.add(map);
			}
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public ArrayList getTrabajadoresFullProduccionArrayList(PortalManager mgr, int rutTrabaja) {
		return getTrabajadoresFullProduccionArrayList(mgr, rutTrabaja, -1);
	}
	
	public ArrayList getTrabajadoresFullProduccionArrayList(PortalManager mgr, int rutTrabaja, int idSubida) {
		ResultSet rs = getTrabajadoresFullProduccionRs(mgr, rutTrabaja, idSubida);
		ArrayList lista = new ArrayList();
		HashMap map = null;
		try {
			while (rs.next()) {
				map = new HashMap();
				for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
					map.put(String.valueOf(i), rs.getString(i + 1));
				}
				lista.add(map);
			}
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public ResultSet getTrabajadoresFullProduccionRs(PortalManager mgr, int rutTrabaja , int idSubida) {
		StringBuffer sql = new StringBuffer();
		ArrayFactory array = new ArrayFactory();
		sql.append(" SELECT \n");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" campo" + i);
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			sql.append(" \n ");
		}
		sql.append(" FROM EJE_QSMCOM_DATOFULL \n");
		if(idSubida == -1) {
			getRutsDescendientesFromExcelProduccion(rutTrabaja, String.valueOf(getIdSubidaValida_comision()),array);
			sql.append(" WHERE id_subida IN ( ").append( getIdSubidaValida_comision() ).append(" ) \n");
		}
		else {
			getRutsDescendientesFromExcelProduccion(rutTrabaja, String.valueOf(idSubida),array);
			sql.append(" WHERE id_subida IN ( ").append( idSubida ).append(" ) \n");
		}
		sql.append("  AND campo").append(DetalleComisiones.CAMPORUTEJE).append(" in "+array.getArrayString()+"");
		sql.append("  ORDER BY ");
		sql.append("  		    campo").append(DetalleComisiones.CAMPONOMBREZONAL);
		sql.append("  		   ,campo").append(DetalleComisiones.CAMPONOMBRESUP);
		sql.append("  		   ,campo").append(DetalleComisiones.CAMPONOMEJE);
		sql.append("  		   ,campo").append(DetalleComisiones.CAMPOTIPO_PRODUCTO);
		PreparedStatement pst;
		ResultSet rs = null;
		try {
			pst = conexion.prepareStatement(sql.toString());
			rs = pst.executeQuery();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet getTrabajadoresFullDetalleComisionesRs(PortalManager mgr, int rutTrabaja, String periodo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT \n");
		for (int i = 0; i < DetalleComisiones.CANTCAMPOS; i++) {
			sql.append(" campo" + i);
			if ((i + 1) < DetalleComisiones.CANTCAMPOS) {
				sql.append(", ");
			}
			sql.append(" \n ");
		}
		sql.append(" FROM eje_qsmcom_detalle_com_datofull \n");
		sql.append(" WHERE fecha_subida IN (SELECT max(ds.fecha_subida) \n");
		sql.append(" 						FROM eje_qsmcom_detalle_com ds \n");
		sql.append(" 						WHERE ds.periodo in ( \n");
		if (periodo == null) {
			sql.append(getLastPeriodoDetalleComisiones() + " \n ");
			periodo = getLastPeriodoDetalleComisiones();
		}
		else {
			sql.append(periodo).append(" \n");
		}
		sql.append("  ) ) \n");
		sql.append("  AND campo").append(DetalleComisiones.CAMPORUTEJE).append(" in ('"+rutTrabaja+"')");
		PreparedStatement pst;
		ResultSet rs = null;
		try {
			pst = conexion.prepareStatement(sql.toString());
			rs = pst.executeQuery();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean esDelTipoCom(int rut, int tipoRutColumna) {
		boolean siEs = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ");		
		sql.append(" FROM EJE_QSMCOM_DATOFULL \n");
		sql.append(" WHERE id_subida IN (SELECT max(ds.id_subida) \n");
		sql.append(" 						FROM EJE_QSMCOM_DATOFULL ds ) \n");
		sql.append(" 	 AND CONVERT(VARCHAR,campo").append(tipoRutColumna).append(") = ('").append(rut).append("') ");
		try {
			PreparedStatement pst = conexion.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				siEs = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return siEs;
	}
	
	public boolean esDelTipo(int rut, String periodo, int tipoRutColumna) {
		boolean siEs = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ");		
		sql.append(" FROM eje_qsmcom_detalle_com_datofull \n");
		sql.append(" WHERE fecha_subida IN (SELECT max(ds.fecha_subida) \n");
		sql.append(" 						FROM eje_qsmcom_detalle_com ds \n");
		sql.append(" 						WHERE ds.periodo in ( \n");
		if (periodo == null) {
			sql.append(getLastPeriodoDetalleComisiones() + " \n ");
		}
		else {
			sql.append(periodo).append(" \n");
		}
		sql.append(" ))  AND CONVERT(VARCHAR,campo").append(tipoRutColumna).append(") = ('").append(rut).append("') ");
		try {
			PreparedStatement pst = conexion.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				siEs = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return siEs;
	}
	
	public HashMap getJefeSupervisor(String periodo, int rut) {
		return getJefeDetComision(periodo,rut,
		               DetalleComisiones.CAMPORUTSUP,
		               DetalleComisiones.CAMPODIGSUP,
		               DetalleComisiones.CAMPONOMBRESUP,
		               DetalleComisiones.CAMPORUTEJE);
	}
	
	public HashMap getJefeZonal(String periodo, int rut) {
		HashMap hash = getJefeDetComision(periodo,rut,
			               DetalleComisiones.CAMPORUTZONAL,
			               DetalleComisiones.CAMPODIGZONAL,
			               DetalleComisiones.CAMPONOMBREZONAL,
			               DetalleComisiones.CAMPORUTEJE);
		if(hash.size() == 0) {
			hash = getJefeDetComision(periodo,rut,
               DetalleComisiones.CAMPORUTZONAL,
               DetalleComisiones.CAMPODIGZONAL,
               DetalleComisiones.CAMPONOMBREZONAL,
               DetalleComisiones.CAMPORUTSUP);
		}
		return hash;
	}
	
	public HashMap getJefeDetComision(String periodo, int rut, int campoGetRut, int campoGetDV, int campoGetNombre, int campoBusca) {
		HashMap map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT campo").append(campoGetRut).append(" as rut ");
		sql.append(" 		,campo").append(campoGetDV).append(" as dv ");
		sql.append(" 		,campo").append(campoGetNombre).append(" as nombre ");
		sql.append(" FROM eje_qsmcom_detalle_com_datofull \n");
		sql.append(" WHERE fecha_subida IN (SELECT max(ds.fecha_subida) \n");
		sql.append(" 						FROM eje_qsmcom_detalle_com ds \n");
		sql.append(" 						WHERE ds.periodo in ( \n");
		if (periodo == null) {
			sql.append(getLastPeriodoDetalleComisiones() + " \n ");
		}
		else {
			sql.append(periodo).append(" \n");
		}
		sql.append(" ))  AND CONVERT(VARCHAR,campo").append(campoBusca).append(") = ('").append(rut).append("') ");
		try {
			PreparedStatement pst = conexion.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				 map.put("rut"		, rs.getString("rut"));
				 map.put("dv"		, rs.getString("dv"));
				 map.put("nombre"	, rs.getString("nombre"));
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public HashMap getJefeProduccion(int idSubida, int rut, int campoGetRut, int campoGetDV, int campoGetNombre, int campoBusca) {
		HashMap map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT campo").append(campoGetRut).append(" as rut ");
		sql.append(" 		,campo").append(campoGetDV).append(" as dv ");
		sql.append(" 		,campo").append(campoGetNombre).append(" as nombre ");
		sql.append(" FROM EJE_QSMCOM_DATOFULL \n");
		sql.append(" WHERE id_subida IN (").append(idSubida).append(") ");
		sql.append("       AND CONVERT(VARCHAR,campo").append(campoBusca).append(") = ('").append(rut).append("') ");
		try {
			PreparedStatement pst = conexion.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				 map.put("rut"		, rs.getString("rut"));
				 map.put("dv"		, rs.getString("dv"));
				 map.put("nombre"	, rs.getString("nombre"));
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public StringBuffer getRutsDescendientesFromExcelProduccion(int rut, String idSubida) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT CAMPO").append(DetalleComisiones.CAMPORUTEJE).append(" as RUT \n ");
		sql.append(" FROM EJE_QSMCOM_DATOFULL \n ");
		if (idSubida == null) {
			sql.append(" WHERE id_subida IN ("+getIdSubidaValida_comision()+") ");
		}
		else {
			sql.append(" WHERE id_subida IN ("+idSubida+")");
		}
		sql.append(" AND  ( ");
		sql.append(" 		   CAMPO").append(DetalleComisiones.CAMPORUTEJE).append("='").append(rut).append("'");
		sql.append(" 		OR CAMPO").append(DetalleComisiones.CAMPORUTZONAL).append("='").append(rut).append("'");
		sql.append(" 		OR CAMPO").append(DetalleComisiones.CAMPORUTSUP).append("='").append(rut).append("'");
		sql.append(" 	  ) ");
		return sql;
	}
	
	public void getRutsDescendientesFromExcelProduccion(int rut, String idSubida, ArrayFactory array) {
		try {
			PreparedStatement pst = conexion.prepareStatement(getRutsDescendientesFromExcelProduccion(rut, idSubida).toString());
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				array.add(rs.getString("rut"));
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SimpleList getPeriodoSeleccionadoDetalle(String periodo) {
		SimpleList lista = new SimpleList();
		String sql = " select periodo, tipo_proceso , fecha_ini , fecha_fin, isnull(delay_acepta,0) as delay_acepta from eje_qsmcom_periodo where periodo = ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			ResultSet rs = pst.executeQuery();
			SimpleHash hash = new SimpleHash();
			while (rs.next()) {
				if (1 == rs.getInt("tipo_proceso")) {
					hash.put("fecInicioProceso", getStringFromStamp(rs.getTimestamp("fecha_ini")));
					hash.put("fecTerminoProceso", getStringFromStamp(rs.getTimestamp("fecha_fin")));
					hash.put("diasExtension", rs.getString("delay_acepta"));
				}
				else if (2 == rs.getInt("tipo_proceso")) {
					hash.put("fecInicioReProceso", getStringFromStamp(rs.getTimestamp("fecha_ini")));
					hash.put("fecTerminoReProceso", getStringFromStamp(rs.getTimestamp("fecha_fin")));
				}
			}
			rs.close();
			pst.close();
			lista.add(hash);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	private String getStringFromStamp(Timestamp time) {
		Calendar cal = Varios.getInstanceCalendar();
		cal.setTimeInMillis(time.getTime());
		String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String aaa = String.valueOf(cal.get(Calendar.YEAR));
		mes = Varios.rellenaCadena(mes, '0', 2);
		dia = Varios.rellenaCadena(dia, '0', 2);
		return dia + "/" + mes + "/" + aaa;
	}

	public SimpleList getPeriodosCreados(String periodoSelected) {
		SimpleList lista = new SimpleList();
		String sql = " select distinct periodo from eje_qsmcom_periodo order by periodo desc";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			SimpleHash hash = new SimpleHash();
			hash.put("id", "-1");
			hash.put("nombre", "&#60;&#60;Crear nuevo periodo&#62;&#62;");
			lista.add(hash);
			while (rs.next()) {
				hash = new SimpleHash();
				String aaa = rs.getString("periodo").substring(0, 4);
				String mes = rs.getString("periodo").substring(4, 6);
				hash.put("id", rs.getString("periodo"));
				hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
				if (rs.getString("periodo") != null && rs.getString("periodo").equals(periodoSelected)) {
					hash.put("props", "selected=\"selected\"");
				}
				lista.add(hash);
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public SimpleList getPeriodosUFCreados(String periodoSelected) {
		SimpleList lista = new SimpleList();
		String sql = " select param as periodo, valor from eje_qsmcom_parametros where id_tipo = 1 order by param desc";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			SimpleHash hash = new SimpleHash();
			hash.put("id", "-1");
			hash.put("nombre", "&#60;&#60;Crear nuevo periodo&#62;&#62;");
			lista.add(hash);
			while (rs.next()) {
				hash = new SimpleHash();
				String aaa = rs.getString("periodo").substring(0, 4);
				String mes = rs.getString("periodo").substring(4, 6);
				hash.put("id", rs.getString("periodo"));
				hash.put("nombre", aaa + " " + Varios.getMes(Integer.parseInt(mes)));
				if (rs.getString("periodo") != null && rs.getString("periodo").equals(periodoSelected)) {
					hash.put("props", "selected=\"selected\"");
				}
				lista.add(hash);
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public String getPeriodoUF(String periodo) {
		String uf = null;
		String sql = " select valor from eje_qsmcom_parametros where id_tipo = 1 and param = ?  ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				uf = rs.getString("valor");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return uf;
	}

	public String getLastPeriodoDetalleComisiones() {
		String lastPeriodo = null;
		String sql = " select max(periodo) as periodo from EJE_QSMCOM_DETALLE_COM  ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				lastPeriodo = rs.getString("periodo");
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lastPeriodo;
	}

	public boolean yaExisteElPeriodo(String periodo) {
		boolean existe = false;
		String sql = " select 1 from eje_qsmcom_periodo where periodo = ?  ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				existe = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return existe;
	}

	public boolean existeElArchivo(String archivo) {
		boolean existe = false;
		String sql = " select 1 from eje_wf_files_up where ltrim(rtrim(nombre_archivo )) = ?  ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, archivo.trim());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				existe = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return existe;
	}

	public boolean yaExistePeriodo_ValidaFuera(String fecha1, String fecha2, String periodoExcluyente) {
		boolean correcto = false;
		String sql = " select 1 from eje_qsmcom_periodo where fecha_ini > ? and fecha_fin < ? and periodo <> ? ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			Calendar calIni = Varios.getCalendarFromString(fecha1);
			Calendar calFin = Varios.getCalendarFromString(fecha2);
			pst.setTimestamp(1, new Timestamp(calIni.getTimeInMillis()));
			pst.setTimestamp(2, new Timestamp(calFin.getTimeInMillis()));
			pst.setString(3, periodoExcluyente);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				correcto = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean yaExistePeriodo_ValidaEntre(String fecha, String periodoExcluyente) {
		boolean correcto = false;
		String sql = " select 1 from eje_qsmcom_periodo where fecha_ini <= ? and fecha_fin >= ? and periodo <> ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			Calendar cal = Varios.getCalendarFromString(fecha);
			pst.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
			pst.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
			pst.setString(3, periodoExcluyente);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				correcto = true;
			}
			rs.close();
			pst.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean addNewPeriodoValidoUF(String periodo, String valor) {
		try {
			conexion.setAutoCommit(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		boolean correcto = true;
		String sql = " insert into eje_qsmcom_parametros (id_tipo,param,valor) values (1,?,?) ";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			pst.setString(2, valor);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			correcto = false;
			e.printStackTrace();
		}
		try {
			if (correcto) {
				conexion.commit();
			}
			else {
				conexion.rollback();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conexion.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean addNewPeriodoValido(String periodo, String pFecIni, String pFecFin, String rFecIni, String rFecFin,
											String delay) {
		try {
			conexion.setAutoCommit(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		boolean correcto = true;
		String sql = " insert into eje_qsmcom_periodo (periodo , tipo_proceso ,fecha_ini , fecha_fin, delay_acepta) ";
		sql += "	values (?,?,?,?,?)";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			pst.setInt(2, 1);
			pst.setTimestamp(3, new Timestamp(Varios.getCalendarFromString(pFecIni).getTimeInMillis()));
			pst.setTimestamp(4, new Timestamp(Varios.getCalendarFromString(pFecFin).getTimeInMillis()));
			pst.setString(5, delay);
			pst.executeUpdate();
			pst.close();
			sql = " insert into eje_qsmcom_periodo (periodo , tipo_proceso ,fecha_ini , fecha_fin) ";
			sql += "	values (?,?,?,?)";
			pst = super.conexion.prepareStatement(sql);
			pst.setString(1, periodo);
			pst.setInt(2, 2);
			pst.setTimestamp(3, new Timestamp(Varios.getCalendarFromString(rFecIni).getTimeInMillis()));
			pst.setTimestamp(4, new Timestamp(Varios.getCalendarFromString(rFecFin).getTimeInMillis()));
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			correcto = false;
			e.printStackTrace();
		}
		try {
			if (correcto) {
				conexion.commit();
			}
			else {
				conexion.rollback();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conexion.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean updPeriodoValidoUF(String periodo, String valor) {
		try {
			conexion.setAutoCommit(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		boolean correcto = true;
		String sql = " update eje_qsmcom_parametros set valor = ? ";
		sql += "	where id_tipo = 1 and param = ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setString(1, valor);
			pst.setString(2, periodo);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			correcto = false;
			e.printStackTrace();
		}
		try {
			if (correcto) {
				conexion.commit();
			}
			else {
				conexion.rollback();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conexion.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean updPeriodoValido(String periodo, String pFecIni, String pFecFin, String rFecIni, String rFecFin,
											String delay) {
		try {
			conexion.setAutoCommit(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		boolean correcto = true;
		String sql = " update eje_qsmcom_periodo set fecha_ini = ? , fecha_fin = ?, delay_acepta = ? ";
		sql += "	where periodo = ? and tipo_proceso = ?";
		try {
			PreparedStatement pst = super.conexion.prepareStatement(sql);
			pst.setTimestamp(1, new Timestamp(Varios.getCalendarFromString(pFecIni).getTimeInMillis()));
			pst.setTimestamp(2, new Timestamp(Varios.getCalendarFromString(pFecFin).getTimeInMillis()));
			pst.setString(3, delay);
			pst.setString(4, periodo);
			pst.setInt(5, 1);
			pst.executeUpdate();
			pst.close();
			sql = " update eje_qsmcom_periodo set fecha_ini = ? , fecha_fin = ? ";
			sql += "where periodo = ? and tipo_proceso = ?";
			pst = super.conexion.prepareStatement(sql);
			pst.setTimestamp(1, new Timestamp(Varios.getCalendarFromString(rFecIni).getTimeInMillis()));
			pst.setTimestamp(2, new Timestamp(Varios.getCalendarFromString(rFecFin).getTimeInMillis()));
			pst.setString(3, periodo);
			pst.setInt(4, 2);
			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e) {
			correcto = false;
			e.printStackTrace();
		}
		catch (Exception e) {
			correcto = false;
			e.printStackTrace();
		}
		try {
			if (correcto) {
				conexion.commit();
			}
			else {
				conexion.rollback();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conexion.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return correcto;
	}

}

class Permiso {
	private String	idPermiso;

	Permiso(String idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getIdPermiso() {
		return idPermiso;
	}

}

enum EnumTipoPeriodo {
	COMISIONES
}