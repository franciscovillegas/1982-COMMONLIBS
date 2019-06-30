package cl.eje.qsmcom.managers;

import cl.eje.qsmcom.service.GatewayJndi;
import cl.eje.qsmcom.service.GatewayManager;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerReport {
	private static ManagerReport instance;
	
	private ManagerReport() {
	
	}
	
	public static ManagerReport getInstance() {
		if(instance == null) {
			synchronized (ManagerReport.class) {
				if(instance == null) {
					instance = new ManagerReport();
				}
			}
		}
		
		return instance;
	}
	
	
	public ConsultaData getReporteGeneral() {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT periodo.fecha_ini, periodo.fecha_fin, ");
		str.append("	sub.nombre as tipoEtapa, tipoCarga.glosa as tipoCarga, ");
		str.append("	periodo.glosa_periodo,dato.tiempo_proceso, ");
		str.append("	dato.id_subida as cargaId, ");
		str.append("	dato.fecha as cargaFecha, "); 
		str.append("	dato.id_plantilla, plantilla.nombre, ");
		str.append("	isnull(dato.cant_regs,0) as total_registros, ");
		str.append("	isnull(rutplantilla.cantidad,0) as total_ruts, ");
		str.append("	isnull(tticket.cantidad,0) as total_respondidos ");
		str.append(" FROM eje_qsmcom_subperiodo sub ");
		str.append(" inner join eje_qsmcom_periodo  periodo on sub.tipo_proceso = periodo.tipo_proceso ");
		str.append(" inner join eje_qsmcom_tipocarga tipoCarga on tipoCarga.tipo_carga = periodo.tipo_carga ");
		str.append(" inner join eje_qsmcom_trackeo_dato dato on dato.periodo = periodo.periodo ");
		str.append(" left outer join eje_qsmcom_plantilla plantilla on plantilla.id_plantilla = dato.id_plantilla ");
		str.append(" left outer join ( SELECT id_subida, COUNT(rut) as cantidad "); 
		str.append(" 				   FROM  eje_qsmcom_periodo_rut_plantilla "); 
		str.append(" 				   GROUP BY id_subida) as rutplantilla "); 
		str.append(" 				on rutplantilla.id_subida = dato.id_subida ");
		str.append(" left outer join (SELECT id_subida, count(rut) as cantidad ");
		str.append(" 				  FROM eje_qsmcom_trackeo_ticket "); 
		str.append(" 				  GROUP BY id_subida) as tticket on tticket.id_subida = dato.id_subida ");
		str.append(" ORDER BY dato.id_subida");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("mac", str.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return data;
	}
	
	public ConsultaData getReporteDetalle(int idSubida) {
		StringBuffer str = new StringBuffer();
		
		str.append(" select rp.rut, t.digito_ver, t.nombres, t.ape_paterno, t.ape_materno, isnull(convert(varchar,tt.id_ticket),'-') as id_ticket, ");
		str.append(" isnull(ty.producto,'-') as tipoTicket, "); 
		str.append(" isnull(convert(varchar,wf.fecha_fin_req,112),'-') as fechaFinRequerimiento ");
		str.append(" from eje_qsmcom_periodo_rut_plantilla rp "); 
		str.append(" left outer join ").append(GatewayManager.getInstance().jndi(GatewayJndi.portal)).append("..eje_ges_trabajador t on t.rut = rp.rut "); 
		str.append(" left outer join eje_qsmcom_trackeo_ticket tt on tt.id_subida = rp.id_subida  and tt.valido = 1 and rp.rut =tt.rut ");
		str.append(" left outer join eje_wf_requerimientos wf on wf.id_req = tt.id_ticket ");
		str.append(" left outer join eje_wf_tripleta_producto ty on ty.id_producto = wf.id_producto ");
		str.append(" where rp.id_subida = ? ");
		
		ConsultaData data = null;
		try {
			Object[] params = {idSubida};
			data = ConsultaTool.getInstance().getData("mac", str.toString(), params);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return data;
		

	}
	
	public ConsultaData getHistoriaTicket(int idTicket) {
		StringBuffer str = new StringBuffer();

		str.append("   SELECT a.actual_transaccion_id, a.actual_transaction_datetime as hora, a.actual_transaction_details AS accion, \n");
		str.append("   a.nombre_rol_antes , isnull(t.nombre,a.nombre_rol_despues) as  nombre_rol_despues,  a.comentario  \n");
		str.append("   FROM eje_wf_actual_transactions  a \n");
		str.append("   LEFT OUTER JOIN ").append(GatewayManager.getInstance().jndi(GatewayJndi.portal)).append("..eje_ges_trabajador t on t.rut = a.rut_rol_despues \n");
		str.append("   WHERE id_req = ?  	 \n");
		str.append("   		and ACTUAL_TRANSACTION_DETAILS not in ('Se ha ingresado el requerimiento.','Requerimiento recibido.','Requerimiento visualizado.')  ");
		
		ConsultaData data = null;
		try {
			Object[] params = {idTicket};
			data = ConsultaTool.getInstance().getData("mac", str.toString(), params);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return data;
	}

	public ConsultaData getTicket(String idTicket) {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT * ");
		str.append(" FROM eje_wf_requerimientos ");
		str.append(" WHERE id_req = ? ");
		
		ConsultaData data = null;
		try {
			Object[] params = {idTicket};
			data = ConsultaTool.getInstance().getData("mac", str.toString(), params);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return data;
		
	}
	
}
