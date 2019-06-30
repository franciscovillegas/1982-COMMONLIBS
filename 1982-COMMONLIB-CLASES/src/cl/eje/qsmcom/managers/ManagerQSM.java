package cl.eje.qsmcom.managers;

import intranet.com.eje.qsmcom.estructuras.Periodo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import portal.com.eje.tools.Varios;
import cl.eje.qsmcom.service.GatewayJndi;
import cl.eje.qsmcom.service.GatewayManager;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.tool.ExcelTool;
import cl.eje.qsmcom.tool.TipoRegistro;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;

public class ManagerQSM {
	private static ManagerQSM instance;
	
	private ManagerQSM() {
	
	}
	
	public static ManagerQSM getInstance() {
		if(instance == null) {
			synchronized (ManagerQSM.class) {
				if(instance == null) {
					instance = new ManagerQSM();
				}
			}
		}
		
		return instance;
	}
	
	public ConsultaData getProceso_FromIdSubida(int idSubida) {	
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT p.tipo_proceso as idTipoProceso, p.periodo as id_periodo,  p.glosa_periodo, s.nombre, c.tipo_carga as idTipoCarga, c.glosa as  tipo_carga, convert(varchar,p.fecha_ini,103) as fecha_ini , convert(varchar,p.fecha_fin,103) as fecha_fin ");
		strConsulta.append(" FROM eje_qsmcom_trackeo_dato d inner join eje_qsmcom_periodo p on d.periodo = p.periodo ");
		strConsulta.append("	LEFT OUTER JOIN eje_qsmcom_subperiodo s on p.tipo_proceso = s.tipo_proceso");
		strConsulta.append(" 	LEFT OUTER JOIN eje_qsmcom_tipocarga c on c.tipo_carga = p.tipo_carga ");
		strConsulta.append(" WHERE d.id_subida = ? ");
		strConsulta.append(" ORDER BY d.id_subida ");
		
		Object[] params = {idSubida}; 
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getProceso(int idPeriodo) {	
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT p.tipo_proceso as idTipoProceso, p.periodo as id_periodo,  p.glosa_periodo, s.nombre, c.tipo_carga as idTipoCarga, c.glosa as  tipo_carga, convert(varchar,p.fecha_ini,103) as fecha_ini , convert(varchar,p.fecha_fin,103) as fecha_fin ");
		strConsulta.append(" FROM eje_qsmcom_periodo p LEFT OUTER JOIN eje_qsmcom_subperiodo s on p.tipo_proceso = s.tipo_proceso");
		strConsulta.append(" 	LEFT OUTER JOIN eje_qsmcom_tipocarga c on c.tipo_carga = p.tipo_carga ");
		strConsulta.append(" WHERE p.periodo = ? ");
		strConsulta.append(" ORDER BY p.fecha_ini DESC ");
		
		Object[] params = {idPeriodo}; 
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getProceso(Connection conn, int idPeriodo) {	
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT p.tipo_proceso as idTipoProceso, p.periodo as id_periodo,  p.glosa_periodo, s.nombre, c.tipo_carga as idTipoCarga, c.glosa as  tipo_carga, convert(varchar,p.fecha_ini,103) as fecha_ini , convert(varchar,p.fecha_fin,103) as fecha_fin ");
		strConsulta.append(" FROM eje_qsmcom_periodo p LEFT OUTER JOIN eje_qsmcom_subperiodo s on p.tipo_proceso = s.tipo_proceso");
		strConsulta.append(" 	LEFT OUTER JOIN eje_qsmcom_tipocarga c on c.tipo_carga = p.tipo_carga ");
		strConsulta.append(" WHERE p.periodo = ? ");
		strConsulta.append(" ORDER BY p.fecha_ini DESC ");
		
		Object[] params = {idPeriodo}; 
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData(conn,strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getProcesos() {
		return getProcesos(null);
	}
		
	public ConsultaData getProcesos(TipoCarga tipoCarga) {
		
		Object[] params = null; 
		
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT p.periodo as id_periodo,  p.glosa_periodo, s.nombre, c.glosa as  tipo_carga, convert(varchar,p.fecha_ini,103) as fecha_ini , convert(varchar,p.fecha_fin,103) as fecha_fin ");
		strConsulta.append(" FROM eje_qsmcom_periodo p LEFT OUTER JOIN eje_qsmcom_subperiodo s on p.tipo_proceso = s.tipo_proceso");
		strConsulta.append(" 	LEFT OUTER JOIN eje_qsmcom_tipocarga c on c.tipo_carga = p.tipo_carga ");
		
		if(tipoCarga != null) {
			strConsulta.append(" WHERE c.tipo_carga = ? ");
			
			params = new Object[1];
			params[0] = new Object();
			params[0] = tipoCarga.getId();
		}
		else {
			params = new Object[0];
		}
		strConsulta.append(" ORDER BY p.fecha_ini DESC ");
		
		ConsultaData data = null;
		
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getCargos() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT c.cargo, c.descrip, c.caracteristicas, '' as opciones, p.id_plantilla, p.nombre, ");
		strConsulta.append(" 	p.fecha_creacion, p.path_excel, p.path_htm, p.path_img ");
		strConsulta.append(" FROM eje_ges_cargos c left outer join eje_qsm_plantilla p on c.caracteristicas = p.id_plantilla "); 
		strConsulta.append(" WHERE c.vigente = 'S' ");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	private ConsultaData getLastProceso() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT TOP 1  p.periodo as id_periodo  ");
		strConsulta.append(" FROM eje_qsmcom_periodo p  ");
		strConsulta.append(" order by p.id_periodo desc ");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public boolean insertProceso(String glosa, Date fecIni, Date fecFin, int tipoProceso, int tipoCarga) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" INSERT INTO eje_qsmcom_periodo ( glosa_periodo, fecha_ini, fecha_fin, tipo_proceso, tipo_carga) ");
		strConsulta.append(" VALUES (?,?,?,?,?) ");
		
		Object[] params = {glosa,fecIni,fecFin,tipoProceso,tipoCarga};

		double indice = -1;
		try {
			indice = ConsultaTool.getInstance().insertIdentity("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return indice > 0;
	}
	
	public boolean isFechaProcesoViable(Date fecIni, Date fecFin, TipoCarga c) {
		
		StringBuffer str = new StringBuffer();
		str.append(" SELECT * \n"); 
		str.append(" FROM eje_qsmcom_periodo \n"); 
		str.append(" WHERE fecha_fin >= ? and fecha_fin <= ?  and tipo_carga = ?  \n");
		str.append(" 	UNION \n");
		str.append(" SELECT * \n"); 
		str.append(" FROM eje_qsmcom_periodo \n"); 
		str.append(" WHERE fecha_ini >= ?  and fecha_ini <= ? and tipo_carga = ?   \n");
		str.append(" 	UNION \n");
		str.append(" SELECT * \n"); 
		str.append(" FROM eje_qsmcom_periodo "); 
		str.append(" WHERE fecha_ini <= ?  and fecha_fin >= ? and tipo_carga = ?   \n");
		str.append(" 	UNION \n");
		str.append(" SELECT * \n"); 
		str.append(" FROM eje_qsmcom_periodo \n"); 
		str.append(" WHERE fecha_ini >= ?  and fecha_fin <= ? and tipo_carga = ?   \n");
		
		Object[] params = {fecIni,fecFin,c.getId(),
						   fecIni,fecFin,c.getId(),
						   fecIni,fecFin,c.getId(),
						   fecIni,fecFin,c.getId()};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return !data.next();
	}

	public boolean delProceso(int id) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" DELETE FROM eje_qsmcom_periodo WHERE periodo = ? ");
		
		Object[] params = {id};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("mac",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	


	public boolean deleteJefeUnidad(String id) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" DELETE eje_ges_unidad_encargado WHERE unid_id = ? ");
		
		Object[] params = {id};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean deleteJefeUnidad(String id, int rut) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" DELETE eje_ges_unidad_encargado WHERE unid_id = ? and rut_encargado = ? ");
		
		Object[] params = {id, rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean deleteJefeUnidad( int rut) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" DELETE eje_ges_unidad_encargado WHERE rut_encargado = ? ");
		
		Object[] params = {rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public boolean insertJefeUnidad(int rut, int rutLogin, String id) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" INSERT INTO eje_ges_unidad_encargado ");
		strConsulta.append(" (unid_empresa,unid_id,periodo,rut_encargado,mision,estado,fec_actualiza,rut_cambios,fec_ini,acc_org) ");
		strConsulta.append(" VALUES (?,?,YEAR( getdate()),?,?,?,getdate(),?,?,?) ");
		
		Object[] params = {0,id,rut,null,1,rutLogin,null,0};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public ConsultaData getTiposDeCarga() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("SELECT tipo_carga, glosa FROM eje_qsmcom_tipocarga");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getTiposDeProceso() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("SELECT  TIPO_PROCESO,NOMBRE, MES_FORMULA, DIA_START from eje_qsmcom_subperiodo");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public ConsultaData getPlantillas() {
		return getPlantilla(-1);
	}
	
	public ConsultaData getPlantilla_PeriodoActual(int rut) {
		int subidaActual = getLastSubidaPorRut_PeriodoActual(TipoCarga.calculo, rut);
		ConsultaData data = getSubida(subidaActual);
		
		if(data.next()){
			int idPlantilla = data.getInt("id_plantilla");
			return getPlantilla(idPlantilla);
		}
		
		return null;
	}
	
	public int getRutResponsable_IdReq(int idReq) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("select rut_responsable from eje_wf_requerimientos where ID_REQ = ?");
		Object[] params = {idReq};
		int rutResponsable = -1;
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
			if(data.next()) {
				rutResponsable = data.getInt("rut_responsable");
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rutResponsable;
	}
	
	public ConsultaData getPlantilla_PeriodoPasado(int idReq) {
		int periodo = getPeriodoValidoParaUnTicket(idReq);
		int rutResponsable = getRutResponsable_IdReq(idReq);
		
		int ultimaSubidaPeriodo = getLastSubidaPorRut(periodo,rutResponsable);
		ConsultaData data = getSubida(ultimaSubidaPeriodo);
		
		if(data.next()){
			int idPlantilla = data.getInt("id_plantilla");
			return getPlantilla(idPlantilla);
		}
		
		return null;
	}
	
	public ConsultaData getPlantilla(int idPlantilla) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT id_plantilla,nombre,fecha_creacion,path_excel,path_htm,path_img, cant_columnas, ");
		strConsulta.append(" 	con_detalle, cols_pk_cabecera, cols_pk_detalle, vigente,cant_columnas_detalle ");
		strConsulta.append(" FROM eje_qsmcom_plantilla ");
		strConsulta.append(" WHERE vigente = 1 ");
		
		Object[] params = null;
		if(idPlantilla > 0) {
			strConsulta.append(" AND id_plantilla = ? ");
			
			params = new Object[1];
			params[0] = idPlantilla;
		}
		else {
			params = new Object[0];
		}
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;		
	}
	
	public ConsultaData getPlantillasConCargosValidos() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT p.id_plantilla, p.nombre, p.fecha_creacion, p.path_excel, p.path_htm, p.path_img ");
		strConsulta.append(" FROM eje_qsmcom_plantilla p inner join ").append(GatewayManager.getInstance().jndi(GatewayJndi.portal)).append("..eje_ges_cargos c on c.caracteristicas = p.id_plantilla ");
		strConsulta.append(" WHERE c.vigente = 'S' and p.vigente = 1");
		strConsulta.append(" ORDER BY p.id_plantilla ");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public int addTrackeoDato(Connection conn , int rut, int periodo, int addFile, int idPlantilla) {
		int newId = getTrackeoDatoMaxId(conn) + 1;
		ConsultaData data = getProceso(conn, periodo);
		int tipoCarga = 0;
		int tipoProceso = 0; 
		
		if(data.next()) {
			tipoProceso = data.getInt("idTipoProceso");
			tipoCarga = data.getInt("idTipoCarga");
		}
		
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  INSERT INTO eje_qsmcom_trackeo_dato ");
		strConsulta.append("  (id_subida, tiempo_proceso,rut, fecha, subida_numero, periodo, tipo_proceso, tipo_carga, id_file, id_plantilla) ");
		strConsulta.append("  VALUES (?,?,?,GETDATE(),?,?,?,?,?,?) ");
		
		Object[] params = {newId,0,rut,-1,periodo,tipoProceso,tipoCarga,addFile,idPlantilla};
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert(conn,strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(ok) {
			return newId;
		}
		else {
			return -1;
		}
	}
	
	public int getTrackeoDatoMaxId(Connection conn) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  select isnull(MAX(id_subida),0) as id_subida from eje_qsmcom_trackeo_dato ");

		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
			if(data.next()) {
				return data.getInt("id_subida");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public ConsultaData getSubida(int newIdSubida) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  select * from eje_qsmcom_trackeo_dato where id_subida = ?");
		Object[] params = {newIdSubida};
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	
	public ConsultaData getSubidas(int idReq) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  select id_subida from  eje_qsmcom_periodo_rut_plantilla where rut = ? ");
		Object[] params = {idReq};
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public int getProcesoMaxId() {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  select isnull(MAX(id_proceso),0) as id_proceso from eje_qsmcom_proceso ");

		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
			if(data.next()) {
				return data.getInt("id_proceso");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int initProceso(int idSubida) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  insert into eje_qsmcom_proceso ");
		strConsulta.append("  	(id_proceso, fecha_ini, fecha_fin, correctos, malos, id_subida) ");
		strConsulta.append("  values ");
		strConsulta.append("    (?,GETDATE(),null,0,0,?)");
		
		int newId = getProcesoMaxId() + 1;
		Object[] params = {newId,idSubida};
		
		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().insert("mac",strConsulta.toString(),params);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(ok) {
			return newId;
		}
		else {
			return -1;
		}
	}
	
	public boolean endProceso(int idProceso, int correctos, int malos) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append("  UPDATE eje_qsmcom_proceso ");
		strConsulta.append("  	set fecha_fin = getdate(), correctos = ?, malos = ? ");
		strConsulta.append("  WHERE id_proceso = ?");
		
		Object[] params = {correctos,malos,idProceso};
		
		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().insert("mac",strConsulta.toString(),params);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public ConsultaData getTablasParaPlantilla(int idPlantilla, int registros, TipoRegistro tipo) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT id_tabla_registro, table_name, reg_count, id_plantilla ");
		strConsulta.append(" FROM eje_qsmcom_tabla_registro ");
		strConsulta.append(" WHERE id_plantilla = ? and reg_count <  ? and es_detalle = ?");
		strConsulta.append(" ORDER BY id_tabla_registro DESC ");

		ConsultaData data = null;
		Object[] params = {idPlantilla, registros, tipo == TipoRegistro.detalle ? 1 : 0};
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public boolean existeTablasParaPlantilla(String tablaName) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" select * from dbo.sysobjects where id = object_id(N'[dbo].[").append(tablaName).append("]') and OBJECTPROPERTY(id, N'IsUserTable') = 1 ");

		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data != null && data.next();
	}
	
	public ConsultaData getTablasParaPlantilla(String tablaName) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT * FROM eje_qsmcom_tabla_registro WHERE TABLE_NAME = ? ");

		ConsultaData data = null;
		Object[] params = {tablaName};
		
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	
	public String getTablaParaPlantilla(int idPlantilla, int maxRegistros, TipoRegistro tipo) {
		
		ConsultaData data = getTablasParaPlantilla(idPlantilla,maxRegistros, tipo);
		if(data.next()) {
			return data.getString("table_name");
		}
	
		return null;
	}
	
	public String getNewTableParaPlantilla(int idPlantilla) {
		MyString my = new MyString();
		StringBuffer newTableName = new StringBuffer();
		
		newTableName.append("__plantilla_".concat(my.rellenaCadena(String.valueOf(idPlantilla), '0', 5)).concat("__").concat(my.getRandomString("abcdefghijklmnopqrstuvwxyz_0123456789", 30)));	
		
		while(existeTablasParaPlantilla(newTableName.toString())) {
			newTableName = new StringBuffer();
			newTableName.append("__plantilla_".concat(my.rellenaCadena(String.valueOf(idPlantilla), '0', 5)).concat("__").concat(my.getRandomString("abcdefghijklmnopqrstuvwxyz_0123456789", 30)));	
		}
		
		return newTableName.toString();
	}
	
	public boolean createTablasParaPlantilla(int idPlantilla, String name) {
	
		ConsultaData data = ManagerQSM.getInstance().getPlantilla(idPlantilla);
		boolean ok = false;
		
		if(data.next()) {
			int cantColumnas = data.getInt("cant_columnas");
			int cantColumnasDetalle = data.getInt("cant_columnas_detalle");
			
			
			/* PRINCIPAL */
			StringBuffer strCreacion = new StringBuffer("CREATE TABLE \n");
			strCreacion.append("[dbo].[").append(name).append("] (");
			strCreacion.append("[fila]").append("   	 [int]  NULL, \n");
			strCreacion.append("[id_subida]").append("   [int]  NULL, \n");
			strCreacion.append("[id_req]").append("      [int]  NULL, \n");
			
			for(int i = 0; i < cantColumnas ;i++) {
				strCreacion.append("[").append(getColumnName(i)).append("]").append("   [varchar] (100) NULL,  \n");
			}
			

			String str = strCreacion.toString().substring(0, strCreacion.toString().lastIndexOf(",")).concat(")");
			
			try {
				 ConsultaTool.getInstance().update("mac", str) ;
				 ok = existeTablasParaPlantilla(name);
				
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}
			

			strCreacion = new StringBuffer("CREATE TABLE \n");
			strCreacion.append("[dbo].[").append(name).append("_dataname] (");
			strCreacion.append("[fila]").append("   	 [int]  NULL, \n");
			strCreacion.append("[id_subida]").append("   [int]  NULL, \n");
			strCreacion.append("[id_req]").append("      [int]  NULL, \n");
			
			for(int i = 0; i < cantColumnas ;i++) {
				strCreacion.append("[").append(getColumnName(i)).append("]").append("   [varchar] (100) NULL,  \n");
			}
			
			str = strCreacion.toString().substring(0, strCreacion.toString().lastIndexOf(",")).concat(")");
			
			try {
				 ConsultaTool.getInstance().update("mac", str) ;
				 ok = ok && existeTablasParaPlantilla(name);
				
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}
			
		}
		
		return ok;
	}
	
	private String getColumnName(int cantColumnas) {
		char[] pal1 = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		String columnaName = "";
		
		int posSup =  (cantColumnas) / pal1.length;
		int posInten =  (cantColumnas) % pal1.length;
		
		if(posSup > 0) {
			columnaName =String.valueOf(pal1[posSup-1]);
		}
		
		columnaName += String.valueOf(pal1[posInten]);;
		
		return columnaName;
	}
	
	public boolean registraTablasParaPlantilla(String tableName, int idPlantilla,TipoRegistro tipo) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" INSERT INTO eje_qsmcom_tabla_registro ");
		strConsulta.append("  (table_name, reg_count, id_plantilla,es_detalle) ");
		strConsulta.append(" VALUES (?,?,?,?)");
		
		Object[] params = {tableName,0,idPlantilla,tipo == TipoRegistro.detalle? 1 : 0};
		
		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().insert("mac",strConsulta.toString(),params);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public boolean updateTrackeoDato(int newIdSubida, String campo, Object value) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" UPDATE eje_qsmcom_trackeo_dato ");
		strConsulta.append(" SET  ").append(campo).append(" = ? ");
		strConsulta.append(" WHERE id_subida = ? ");
		
		Object[] params = {value,newIdSubida};
		
		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().update("mac",strConsulta.toString(),params) > 0;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
		
	}
	
	public boolean addRegistrosTablaRegistro(String tableName, int nuevosRegistros) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" UPDATE eje_qsmcom_tabla_registro ");
		strConsulta.append(" SET  reg_count = isnull(reg_count,0) + ").append(nuevosRegistros);
		strConsulta.append(" WHERE table_name = ? ");
		
		Object[] params = {tableName};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("mac",strConsulta.toString(),params) > 0;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	
	public boolean addTrackeoRegistro(String tableName, int idSubida) {
		String idTabla = ConsultaTool.getInstance().getFirsStringtValue(getTablasParaPlantilla(tableName),"id_tabla_registro");
		
		int idTablaInt = Validar.getInstance().validarInt(idTabla, -1);
		if(idTablaInt >= 0) {
			return addTrackeoRegistro(idTablaInt, idSubida);
		}
		
		return false;
	}
	public boolean addTrackeoRegistro(int idTableName, int idSubida) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" INSERT INTO eje_qsmcom_trackeo_registro ");
		strConsulta.append("  (id_subida, id_tabla_registro )");
		strConsulta.append(" VALUES (?,?) ");
		
		Object[] params = {idSubida,idTableName};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("mac",strConsulta.toString(),params);
		
		} catch (SQLException e) {
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
	
	
	public int getLastSubida_PeriodoRut(int periodo, int rut) {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT isnull(max(d.id_subida),-1) as id_subida ");
		str.append(" FROM eje_qsmcom_trackeo_dato d inner join eje_qsmcom_periodo_rut_plantilla p "); 
		str.append(" 	on p.id_subida = d.id_subida ");
		str.append(" WHERE d.periodo = ? and p.rut = ? ");
	
		ConsultaData data = null;
		
		try {
			Object[] params = {periodo,rut};
			data = ConsultaTool.getInstance().getData( "mac" , str.toString() , params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int idSubida = -1;
		
		if(data != null && data.next()) {
			idSubida = data.getInt("id_subida");
		}
		
		return idSubida;
	}
	
	/**
	 * Entrega el id de la última subida del actual periodo para un rut determinado, en el caso de no tener ninguna subida para el rut-periodo se retornará un -1
	 * 
	 * */
	
	public int getLastSubidaPorRut_PeriodoActual(TipoCarga t,int rut) {
		Periodo periodo = getPeriodoValido(t);
		
		if(periodo != null) {
			return getLastSubida_PeriodoRut(periodo.getPeriodo(), rut);
		}
		else {
			return 0;
		}
		
	}

	public int getLastSubidaPorRut(int periodo, int rut) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT max(p.id_subida) as id_subida ");
		strConsulta.append(" FROM eje_qsmcom_periodo_rut_plantilla P ");
		strConsulta.append(" INNER JOIN eje_qsmcom_trackeo_dato D on d.id_subida = p.id_subida ");
		strConsulta.append(" WHERE p.rut = ? and d.periodo = ? ");
		strConsulta.append(" ORDER BY d.id_subida DESC ");
		int maximo = -1;
		
		Object[] params = {rut, periodo};
		try {
			ConsultaData data  = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
			if(data.next()) {
				maximo = data.getInt("id_subida");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return maximo;
	}

	public boolean addRelationSubidaRut(String tableName, int idSubida) {
		ConsultaData data = getTablasParaPlantilla(tableName);
		int idPlantilla = 0;
		
		if(data.next()) {
			idPlantilla = data.getInt("id_plantilla");
		}
		
		data = getPlantilla(idPlantilla);
		
		
		boolean ok = false;
		if(data.next()) {
			ExcelTool ex = new ExcelTool();
			int posKey = data.getInt("cols_pk_cabecera");
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO eje_qsmcom_periodo_rut_plantilla (ID_SUBIDA,RUT)  ");
			sql.append(" SELECT ");
			sql.append(" id_subida, id_req ");
			sql.append(" FROM ").append(tableName);
			sql.append(" WHERE  id_subida = ?  ");
			sql.append(" GROUP BY  ");
			sql.append(" id_subida, id_req");
			
			Object[] params = {idSubida};
			try {
				ok = ConsultaTool.getInstance().insert("mac", sql.toString(), params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return ok;
	}

	public ConsultaData getTablaIdRegistroFromIdSubida(int idSubida) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" select * from eje_qsmcom_trackeo_registro where id_subida = ? ");
		
		Object[] params = {idSubida};
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public ConsultaData getTablaRegistro(int idTabla) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" select * from eje_qsmcom_tabla_registro where id_tabla_registro = ? ");
		
		Object[] params = {idTabla};
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public ConsultaData getRegistros(int idTabla, int idSubida) {
		ConsultaData data = getTablaRegistro(idTabla);
		String table = null;
		
		if(data.next()) {
			table = data.getString("table_name");
		
			StringBuffer strConsulta = new StringBuffer();
			strConsulta.append(" select * from ").append(table).append(" where id_subida = ? ");
			
			Object[] params = {idSubida};
			try {
				data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	
	public String getOrNewTableName(int idPlantilla, int cantRegMaximos) {
		ManagerQSM q = ManagerQSM.getInstance();
		String tablaName = q.getTablaParaPlantilla(idPlantilla, cantRegMaximos, TipoRegistro.normal );
		
		if(tablaName == null) {
			tablaName = q.getNewTableParaPlantilla(idPlantilla);
			boolean ok = q.createTablasParaPlantilla(idPlantilla, tablaName);
			
			if(ok) {
				ConsultaData dataPlantilla = ManagerQSM.getInstance().getPlantilla(idPlantilla);
				
				boolean conDetalle = false;
				if(dataPlantilla.next()) {
					conDetalle = dataPlantilla.getBoolean("con_detalle");
					q.registraTablasParaPlantilla(tablaName.concat("_dataname"), idPlantilla, TipoRegistro.detalle );
				}
				
				
				q.registraTablasParaPlantilla(tablaName, idPlantilla, TipoRegistro.normal);
				
			}
			else {
				tablaName = null;
			}
		}
		
		return tablaName;
	}
	
	public ConsultaData getRegistrosDetalle(int idTabla, int idSubida, int rut) {		
		return getRegistros(idTabla, idSubida, rut, TipoRegistro.detalle);
	}
	
	public ConsultaData getRegistros(int idTabla, int idSubida, int rut) {		
		return getRegistros(idTabla, idSubida, rut, TipoRegistro.normal);
	}
	
	public ConsultaData getRegistros(int idTabla, int idSubida, int rut, TipoRegistro t) {
		ConsultaData data = getTablaRegistro(idTabla);
		String tableName = null;
		int posKey = -1;
		int posKeyDetalle = -1;
		
		if(data.next()) {
			tableName = data.getString("table_name");
		
			data = getTablasParaPlantilla(tableName);
			int idPlantilla = 0;
			
			if(data.next()) {
				idPlantilla = data.getInt("id_plantilla");
			}
			
			data = getPlantilla(idPlantilla);
			
			boolean ok = false;
			if(data.next()) {
				posKey = data.getInt("cols_pk_cabecera");
				posKeyDetalle = data.getInt("cols_pk_detalle");
			}
		}
		
		if(posKey != -1) {
			StringBuffer strConsulta = new StringBuffer();
			ExcelTool tool = new ExcelTool();
				
			if(t == TipoRegistro.normal) {
				strConsulta.append(" select * from ").append(tableName)
						   .append(" where id_subida = ? and ").append("[").append(tool.getColumnName(posKey)).append("] = ?");
			}
			else {
				strConsulta.append(" select * from ").append(tableName).append("_detalle")
				   .append(" where id_subida = ? and ").append("[").append(tool.getColumnName(posKeyDetalle)).append("] = ?");
			}
			
			Object[] params = {idSubida, rut};
			try {
				data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
	
			} catch (SQLException e) {
				System.out.println(strConsulta.toString());
				e.printStackTrace();
			}
		}
		else {
			data = null;
		}
		
		return data;
	}
	
	public int getPeriodoValidoParaUnTicket(String idTicket) {
		return getPeriodoValidoParaUnTicket(Validar.getInstance().validarInt(idTicket,-1));
	}
	
	public int getPeriodoValidoParaUnTicket(int idTicket) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" select * from eje_qsmcom_trackeo_ticket t where id_ticket = ? ");
		
		Object[] params = {idTicket};
		int periodoValido = -1;
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("mac",strConsulta.toString(),params);
			
			if(data.next()) {
				periodoValido = data.getInt("periodo");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return periodoValido;
	}

	public String getPeriodoName(int periodoInt) {
		ConsultaData data = ManagerQSM.getInstance().getProceso( periodoInt );
		StringBuffer glosa = new StringBuffer();
		if(data.next()) {
			glosa.append(data.getString("glosa_periodo")).append("  (").append(data.getString("nombre")).append(")");
		}
		
		return glosa.toString();
	}
	
	public String getPeriodoName_FromIdReq(String idReq) {
		return getPeriodoName_FromIdReq(Validar.getInstance().validarInt(idReq, -1));
	}
	
	public String getPeriodoName_FromIdReq(int idReq) {
		ConsultaData data = ManagerQSM.getInstance().getProceso( getPeriodoValidoParaUnTicket(idReq) );
		StringBuffer glosa = new StringBuffer();
		if(data.next()) {
			glosa.append(data.getString("glosa_periodo")).append("  (").append(data.getString("nombre")).append(")");
		}
		
		return glosa.toString();
	}

	/**
	 * Entrega la última subida de un tipo de carga, no diferencia por periodo actual ni pasado.
	 * */
	
	public ConsultaData getLastSubidaTipoCarga(TipoCarga tipo) {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT TOP 1 td.id_subida, td.tiempo_proceso, td.rut, td.fecha, td.subida_numero, ");
		str.append(" 		td.periodo, td.tipo_proceso, td.tipo_carga, td.id_file, td.id_plantilla ");
		str.append(" from eje_qsmcom_trackeo_dato td "); 
		str.append(" inner join eje_qsmcom_periodo p 	on td.periodo = p.periodo "); 
		str.append(" inner join eje_qsmcom_tipocarga t	on t.tipo_carga = p.tipo_carga ");
		str.append(" inner join eje_qsmcom_subperiodo s	on s.tipo_proceso = p.tipo_proceso ");
		str.append(" WHERE t.tipo_carga = ? ");
		str.append(" ORDER BY td.id_subida DESC ");
		
		Object[] params = {tipo.getId()};
		
		ConsultaData data=null;
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	public ConsultaData getResumenLastSubidasFromPeriodos(TipoCarga tipo, int rut) {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT m.periodo, m.id_subida , d.id_plantilla, d.id_file , \n");
		str.append(" 	p.glosa_periodo, p.fecha_ini, p.fecha_fin, tsp.nombre, tc.glosa, ");
		str.append(" 	dt.rut , pl.nombre as nombre_plantilla, tc.glosa as glosatipocarga, tsp.nombre as glosatipoperiodo \n");
		str.append(" FROM \n");    
		str.append(" 	(SELECT p1.periodo, MAX(d1.id_subida ) AS id_subida  	\n");
		str.append(" 	 FROM eje_qsmcom_periodo p1  	 \n");
		str.append(" 	 INNER JOIN eje_qsmcom_trackeo_dato d1 on p1.periodo = d1.periodo  \n");
		str.append(" 	 INNER JOIN eje_qsmcom_periodo_rut_plantilla pla1 on pla1.id_subida = d1.id_subida \n");
		str.append(" 	WHERE pla1.rut = ").append(rut).append("\n");
		str.append(" 	 GROUP BY p1.periodo ) AS M \n"); 
		str.append(" INNER JOIN eje_qsmcom_periodo p ON p.periodo = m.periodo \n");
		str.append(" INNER JOIN eje_qsmcom_trackeo_dato d ON d.id_subida = m.id_subida \n");
		str.append(" INNER JOIN eje_qsmcom_periodo_rut_plantilla dt ON dt.id_subida = d.id_subida \n");
		str.append(" INNER JOIN eje_qsmcom_plantilla pl ON pl.id_plantilla = d.id_plantilla \n");
		str.append(" INNER JOIN eje_qsmcom_tipocarga tc on tc.tipo_carga = p.tipo_carga \n");
		str.append(" INNER JOIN eje_qsmcom_subperiodo tsp on tsp.tipo_proceso = p.tipo_proceso \n");
		str.append(" WHERE p.tipo_carga = ? ");
		str.append(" 		and  dt.rut = ? \n");	

		Object[] params = {tipo.getId(), rut};	
		
		ConsultaData data=null;
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}
	
	
	public ConsultaData getResumenLastSubidasFromPeriodos(TipoCarga tipo) {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT m.rut, t.digito_ver , t.nombres, t.ape_paterno, t.ape_materno,m.periodo, p.glosa_periodo, m.id_subida, m.id_plantilla, plant.nombre ");
		str.append(" FROM( ");
		str.append(" SELECT  dt.rut,d.periodo,  d.id_plantilla, max(d.id_subida) as id_subida ");
		str.append(" FROM eje_qsmcom_periodo_rut_plantilla dt inner join eje_qsmcom_trackeo_dato d ON d.id_subida = DT.id_subida "); 
		str.append(" group by dt.rut,d.periodo, d.id_plantilla) AS M ");
		str.append(" LEFT OUTER JOIN qsmcredichile..eje_ges_trabajador T on t.rut = m.rut ");
		str.append(" LEFT OUTER JOIN eje_qsmcom_periodo p	   ON p.periodo = m.periodo ");
		str.append(" LEFT OUTER JOIN eje_qsmcom_plantilla plant ON plant.id_plantilla = m.id_plantilla ");
		str.append(" WHERE m.periodo in (select top 1 periodo from eje_qsmcom_periodo where tipo_carga = ? order by periodo desc) ");
		str.append(" and m.rut > 100 -- SOLO PARA VERIFICAR RUT ");
		str.append(" ORDER BY  M.RUT ");
		
		Object[] params = {tipo.getId()};
		
		
		ConsultaData data=null;
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	
	public ConsultaData getRegistroCargasHistoricas() {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT d.id_subida,d.fecha , d.rut, ISNULL(t.nombre,'Desconocido') as nombre, ");
		str.append(" 	tc.glosa as tipoCarga, tsp.nombre as tipoProceso, ");
		str.append(" 	COUNT(dt.rut) as rutsCargados, d.cant_regs, d.tiempo_proceso as ms ");
		str.append(" FROM  eje_qsmcom_periodo p ");  
		str.append(" INNER JOIN eje_qsmcom_trackeo_dato d ON d.periodo = p.periodo ");
		str.append(" INNER JOIN eje_qsmcom_plantilla pl ON pl.id_plantilla = d.id_plantilla ");
		str.append(" INNER JOIN eje_qsmcom_tipocarga tc on tc.tipo_carga = p.tipo_carga ");
		str.append(" INNER JOIN eje_qsmcom_subperiodo tsp on tsp.tipo_proceso = p.tipo_proceso ");
		str.append(" INNER JOIN eje_qsmcom_periodo_rut_plantilla dt ON dt.id_subida = d.id_subida ");
		str.append(" LEFT OUTER JOIN ").append(GatewayManager.getInstance().jndi(GatewayJndi.portal)).append("..eje_ges_trabajador t on t.rut = d.rut ");
		str.append(" GROUP BY d.id_subida, d.fecha , d.rut, t.nombre,tc.glosa , tsp.nombre , d.cant_regs, d.tiempo_proceso ");
		str.append(" ORDER BY d.id_subida DESC ");
		
		
		ConsultaData data=null;
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	
	public ConsultaData getRegistroCargasHistoricas(int idPeriodo, int idPlantilla) {
		StringBuffer str = new StringBuffer();
		
		str.append(" SELECT d.id_subida,d.fecha , d.rut, ISNULL(t.nombre,'Desconocido') as nombre, \n");
		str.append(" 	tc.glosa as tipoCarga, tsp.nombre as tipoProceso, \n");
		str.append(" 	COUNT(dt.rut) as rutsCargados, d.cant_regs, d.tiempo_proceso as ms \n");
		str.append(" FROM  eje_qsmcom_periodo p \n");  
		str.append(" INNER JOIN eje_qsmcom_trackeo_dato d ON d.periodo = p.periodo \n");
		str.append(" INNER JOIN eje_qsmcom_plantilla pl ON pl.id_plantilla = d.id_plantilla \n");
		str.append(" INNER JOIN eje_qsmcom_tipocarga tc on tc.tipo_carga = p.tipo_carga \n");
		str.append(" INNER JOIN eje_qsmcom_subperiodo tsp on tsp.tipo_proceso = p.tipo_proceso \n");
		str.append(" INNER JOIN eje_qsmcom_periodo_rut_plantilla dt ON dt.id_subida = d.id_subida \n");
		str.append(" LEFT OUTER JOIN ").append(GatewayManager.getInstance().jndi(GatewayJndi.portal)).append("..eje_ges_trabajador t on t.rut = d.rut \n");
		str.append(" WHERE p.periodo = ? and pl.id_plantilla = ?  \n");
		str.append(" GROUP BY d.id_subida, d.fecha , d.rut, t.nombre,tc.glosa , tsp.nombre , d.cant_regs, d.tiempo_proceso \n");
		str.append(" ORDER BY d.id_subida DESC \n");
		
		
		ConsultaData data=null;
		Object[] params = {idPeriodo, idPlantilla};
		try {
			data = ConsultaTool.getInstance().getData("mac",str.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	
}


