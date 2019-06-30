package cl.eje.generico.fileremote.ws.injectar;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class Factura {

	static int CANTIDAD_DIAS_BUSCAR_FACT_ANTERIORES = -120;
	public int id;
	public String rutProveedor;
	public String nombre;
	String rutSinDV = "";
	String idLocalidad = "";
	int folio;
	int dte;
	int monto;
	BigDecimal cuentaCorriente = null;
	Integer banco = null;
	int formaPago = 0;
	int condpago = 0;
	// String servicio;
	// int localidad;
	int asignadoRutRol = -1;

	public Factura(int id, String rutEmisor, int folio, int tipoDTE, int monto, String nombre) {

		this.id = id;
		this.rutProveedor = rutEmisor; // obtenerRut(rutEmisor);
		this.folio = folio;
		dte = tipoDTE;
		this.monto = monto;
		this.nombre = nombre;

	}

	private int parseRutSinDV(String rutEmisor) {

		String[] rutDv = rutEmisor.split("-");
		int rut = Integer.parseInt(rutDv[0]);

		return rut;
	}

	public String obtenerNombreDesdeBd() {

		String nombre = "";

		StringBuilder sql = new StringBuilder("SELECT * FROM  eje_wfgen_proveedor").append("  WHERE nif = ?");

		Object[] params = { rutProveedor };
		boolean ok = false;
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);

			if (data != null && data.next()) {
				nombre = data.getForcedString("nombre_fantasia");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nombre;

	}

	public int obtenerIdProveedorDesdeBd() {

		int idProveedor = -1;

		StringBuilder sql = new StringBuilder("SELECT * FROM  eje_wfgen_proveedor").append("  WHERE nif = ?");

		Object[] params = { rutProveedor };
		boolean ok = false;
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);

			if (data != null && data.next()) {
				idProveedor = data.getInt("id_proveedor");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idProveedor;

	}

	static int count = 0;

	public boolean asignarFactura() {

		actualizarEstadoFactura(EstadosDeFactura.NO_PROCESADOS);
		boolean ok = false;
		// System.out.println("folio factura : " + folio);
		if (existeFacturaEnWF()) {

			actualizarEstadoFactura(EstadosDeFactura.DUPLICADO);
			eliminarDeListaAlWf();

		} else if (asignarPorServicio() && rutSinDV.length() > 0 && idLocalidad.length() > 0) {

			asignadoRutRol = obtenerAsignadoRutRol();
			if (insertInsertIntoWorkflow()) {

				actualizarEstadoFactura(EstadosDeFactura.PROCESADOS_REGLA1);
				eliminarFacturaDeListaAProcesar();
				ok = true;
			}

		} else if (asignarPorFecha(CANTIDAD_DIAS_BUSCAR_FACT_ANTERIORES) && rutSinDV.length() > 0
				&& idLocalidad.length() > 0) {

			asignadoRutRol = obtenerAsignadoRutRol();
			if (insertInsertIntoWorkflow()) {

				actualizarEstadoFactura(EstadosDeFactura.PROCESADOS_REGLA2);
				eliminarFacturaDeListaAProcesar();
				ok = true;
			}

		}
		count++;

		return ok;
	}

	public boolean asignarFactura(int idReceptor) {

		actualizarEstadoFactura(EstadosDeFactura.NO_PROCESADOS);
		boolean ok = false;
		// System.out.println("folio factura : " + folio);
		if (existeFacturaEnWF()) {

			actualizarEstadoFactura(EstadosDeFactura.DUPLICADO);
			eliminarDeListaAlWf();

		} else {
			asignadoRutRol = obtenerAsignadoRutRol(idReceptor);
			if (asignadoRutRol > 0) {
				// TODO: Definir datos de proveedor de la factura a insertar
				if (insertInsertIntoWorkflow()) {
					actualizarEstadoFactura(EstadosDeFactura.PROCESADOS_MANUAL);
					eliminarFacturaDeListaAProcesar();
					ok = true;
				}
			}
		}
		return ok;
	}

	private void obtenerBancoyCuentaCorriente() {

		StringBuilder sql = new StringBuilder("SELECT a.id_banco, a.cuentacorriente, a.id_formapago, a.id_condpago ")
				.append(" FROM eje_wffact_documentos a, eje_wfgen_proveedor b "
						+ " WHERE a.id_proveedor=b.id_proveedor AND b.id_portal = ? ORDER BY a.id_req DESC ");

		int rutEmi = parseRutSinDV(rutProveedor);

		Object[] params = { rutEmi };
		ConsultaData data;
		try {

			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			if (data != null && data.next()) {

				condpago = data.getInt("id_condpago");
				formaPago = data.getInt("id_formapago");
				banco = data.getInt("id_banco");
				cuentaCorriente = data.getBigDecimal("cuentacorriente");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean asignarPorFecha(int dias) {

		Date fechaActual = new Date();
		if (dias > 0)
			dias = dias * -1;

		int idProveedor = obtenerIdProveedorDesdeBd();

		if (idProveedor == -1) {
			actualizarEstadoFactura(EstadosDeFactura.PROVEEDOR_NO_EXISTE);
			return false;
		}

		if (!proveedorTieneFacturaAnterior()) {
			actualizarEstadoFactura(EstadosDeFactura.NO_TIENE_FACTURA_ANTERIOR);
			return false;
		}
		Date fecIni = sumarRestarDiasFecha(fechaActual, dias);
		Date fecEnBD = new Date();
		StringBuilder sql = new StringBuilder(
				"SELECT TOP 1 a.id_localidad, b.id_portal, fecha_req FROM  eje_wffact_documentos a INNER JOIN eje_wfgen_proveedor b ON a.id_proveedor=b.id_proveedor INNER JOIN eje_wf_actual_transactions c  ON c.id_req=a.id_req INNER JOIN eje_wf_requerimientos d ON d.id_req=a.id_req")
						.append("  WHERE d.id_evento = 102 AND \r\n"
								+ "  b.[vigencia] = 1 AND c.ID_ROL_antes = 145 AND \r\n"
								+ " b.id_portal = ? GROUP BY  a.id_req, a.id_localidad, b.id_portal, fecha_req ORDER BY a.id_req  DESC ");

		int rutEmi = parseRutSinDV(rutProveedor);

		Object[] params = { rutEmi };
		boolean ok = false;
		ConsultaData data;
		try {

			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			ok = data.size() > 0;
			if (data != null && data.next()) {

				rutSinDV = data.getForcedString("id_portal");
				idLocalidad = data.getForcedString("id_localidad");
				fecEnBD = data.getDateJava("fecha_req");
				if (fecEnBD.compareTo(fecIni) < 0) {
					// fecEnBD es antes que fecIni
					ok = false;
					actualizarEstadoFactura(EstadosDeFactura.FACTURA_CON_FECHA_ANTERIOR_ALPLAZO);

				}

				System.out.println(
						"emisor encontrado por factura anterior : " + rutSinDV + " localidad : " + idLocalidad);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}

	private boolean proveedorTieneFacturaAnterior() {

		StringBuilder sql = new StringBuilder(
				"SELECT TOP 1 a.id_req FROM eje_wffact_documentos a INNER JOIN eje_wfgen_proveedor b ON a.id_proveedor=b.id_proveedor")
						.append("  WHERE nif = ?");

		Object[] params = { rutProveedor };
		boolean ok = false;
		ConsultaData data;
		try {

			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			if (data != null && data.next()) {
				int valId_req = data.getInt("id_req");
				ok = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok;

	}

	private void esFiltroPorFecha(Date fecIni) {

		int rutEmi = parseRutSinDV(rutProveedor);
		Object[] params = { rutEmi, fecIni };
		StringBuilder sql = new StringBuilder(
				"SELECT TOP 1 a.id_localidad, b.id_portal FROM  eje_wffact_documentos a INNER JOIN eje_wfgen_proveedor b ON a.id_proveedor=b.id_proveedor INNER JOIN eje_wf_actual_transactions c  ON c.id_req=a.id_req INNER JOIN eje_wf_requerimientos d ON d.id_req=a.id_req")
						.append("  WHERE d.id_evento = 102 AND \r\n"
								+ "  b.[vigencia] = 1 AND c.ID_ROL_antes = 145 AND \r\n"
								+ " b.id_portal = ?  AND fecha_req >= ?  GROUP BY  a.id_req, a.id_localidad, b.id_portal ORDER BY a.id_req  DESC ");

	}

	public Date sumarRestarDiasFecha(Date fecha, int dias) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
	}

	private boolean eliminarFacturaDeListaAProcesar() {

		boolean ok = eliminarDeListaAlWf();
		if (ok)
			marcarFacturaComoProcesada();
		return ok;
	}

	private boolean eliminarDeListaAlWf() {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" DELETE FROM eje_siifactura_por_insertar_al_wf WHERE id_corr = ?  ");

		Object[] params = { id };

		boolean ok = false;
		try {
			System.out.println(" borrar de la lista por insertar id : " + id);
			// System.out.println("sql : " + strConsulta.toString());

			ok = ConsultaTool.getInstance().update("portal", strConsulta.toString(), params) > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	private void marcarFacturaComoProcesada() {

		StringBuilder sql = new StringBuilder();

		String tabla = "eje_siifactura_import";
		sql.append(" UPDATE ").append(tabla);
		sql.append(" SET enviada_al_wf = ? , fecha_inyeccion_en_wf = getdate() ");
		sql.append(" WHERE folio = ? AND rut_emisor = ? AND tipo_dte = ? ");

		boolean facturaProcesada = true;
		Object[] params = { facturaProcesada, folio, rutProveedor, dte };

		try {
			ConsultaTool.getInstance().insert("portal", sql.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean asignarPorServicio() {

		StringBuilder sql = new StringBuilder("SELECT * FROM  eje_siifactura_servicios_basicos")
				.append("  WHERE rut_Proveedor = ?");

		Object[] params = { parseRutSinDV(rutProveedor) };
		boolean ok = false;
		ConsultaData data;
		try {
			// System.out.println("asignar servicio");
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			ok = data.size() > 0;
			if (data != null && data.next()) {
				rutSinDV = data.getForcedString("id_servicio");
				idLocalidad = data.getForcedString("id_sona");

				System.out.println("encontrada por servicio, idLocalidad: " + idLocalidad);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok;
	}

	HashMap<String, Integer> obtenerInfoFacturas() {

		StringBuilder sql = new StringBuilder("SELECT * FROM  eje_siifactura_import").append("  WHERE file_name = ?");

		// System.out.println(" nombre: " + nombre);
		Object[] params = { nombre };
		ConsultaData data;
		HashMap<String, Integer> datosFactura = new HashMap<String, Integer>();
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			if (data != null && data.next()) {

				datosFactura.put("tipo_dte", new Integer(data.getForcedString("tipo_dte")));
				datosFactura.put("precio_con_iva", data.getInt("precio_con_iva"));
				datosFactura.put("precio_sin_iva", data.getInt("precio_sin_iva"));
				datosFactura.put("iva", data.getInt("iva"));

			} else {
				eliminarDeListaAlWf();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datosFactura;
	}

	private String getNodeText(Document doc, String tagName) {
		NodeList lista = doc.getElementsByTagName(tagName);
		if (lista != null && lista.getLength() > 0) {
			return lista.item(0).getTextContent();
		}
		return null;
	}

	public boolean insertInsertIntoWorkflow() {

		boolean ok = false;
		int conIVA = 0;
		int sinIVA = 0;
		int iva = 0;
		int tipoDte = 0;

		HashMap<String, Integer> infoFactura = obtenerInfoFacturas();

		if (infoFactura.isEmpty()) {
			return false;
		}
		conIVA = infoFactura.get("precio_con_iva");
		sinIVA = infoFactura.get("precio_sin_iva");
		iva = infoFactura.get("iva");
		tipoDte = infoFactura.get("tipo_dte");

		int localidad = new Integer(idLocalidad);
		int asignadoRol = 145;
		int asignadoProducto = 74;
		int asignadoEvento = 102;
		int asignadoIssue = 170;
		int idProveedor = obtenerIdProveedorDesdeBd();

		int asignadoEstado = 1;

		double idReq = 0;
		if (idProveedor == -1) {
			actualizarEstadoFactura(EstadosDeFactura.PROVEEDOR_NO_EXISTE);
			return false;
		}
		if (asignadoRutRol == -1) {
			actualizarEstadoFactura(EstadosDeFactura.NO_SE_PUDO_ASIGNAR_FACTURA);
			return false;
		}

		{
			String sql = "INSERT INTO EJE_WF_REQUERIMIENTOS ";
			sql += "        (nombre_cliente, id_evento, id_suceso, id_producto, cod_emp_propios, cod_planta, fecha_req, ";
			sql += "         id_status, id_rol, id_rol_antes, rut_rol, rut_rol_antes, fecha_rol, ";
			sql += "		 rut_responsable, es_historico, tipo_cierre) ";
			sql += " VALUES ( ?, ?, ?, ?, ?, ?, getdate(), ";
			sql += " 		  ?, ?, ?, ?, ?, getdate(), ";
			sql += "		  ?, ?, ?) ";

			Object[] params = { "RECEPTOR AUTOMÁTICO", asignadoEvento, asignadoIssue, asignadoProducto, 430, 1,
					asignadoEstado, asignadoRol, asignadoRol, asignadoRutRol, 0, asignadoRutRol, "N", null };

			try {
				if (tipoDte == 30 || tipoDte == 33 || tipoDte == 32 || tipoDte == 34 || tipoDte == 60 || tipoDte == 61)
					idReq = ConsultaTool.getInstance().insertIdentity("wf", sql, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		{
			// ID_OFICINA es igual al ID_LOCALIDAD
			String sqlDoc = "INSERT INTO eje_wffact_documentos (ID_REQ, ID_TIPODOCTO, ID_TIPODOCTORECEPTOR, ID_PROVEEDOR, NUMDOCTO, ";
			sqlDoc += " FECHAEMISION, FECHAVENCIMIENTO, ID_OFICINA, ID_CONDPAGO, ID_FORMAPAGO, ";
			sqlDoc += " ID_BANCO, CUENTACORRIENTE, ID_LOCALIDAD, ID_CCOSTO, ID_CODIMP, ";
			sqlDoc += " ID_ESTADO  ) ";
			sqlDoc += "  VALUES (? , ? , ? , ? , ? , ";
			sqlDoc += " 		 getdate() , dateadd(day, 45, getdate()), ?, ?, ?, ";
			sqlDoc += " 		 ? , ?, ?, ?, ? , ";
			sqlDoc += " 		 ? ) ";

			obtenerBancoyCuentaCorriente();
			ArrayList<Integer> idTipodct = obtenerIDTipodocto();
			int idTipo = 0;
			if (idTipodct.size() > 0)
				idTipo = idTipodct.get(0);

			Object[] params = { idReq, idTipo, idTipo, idProveedor, folio, localidad, condpago, formaPago, banco,
					cuentaCorriente, localidad, null, 100, 50 };

			if (tipoDte == 30 || tipoDte == 33 || tipoDte == 32 || tipoDte == 34 || tipoDte == 60 || tipoDte == 61) {
				String sqlValores = " INSERT INTO eje_wffact_documentos_valores (id_req, id_tipovalor, secuencia, porcentaje, monto ) VALUES (?, ?, ?, ?, ?)";
				Object[] params1 = { idReq, 1, 1, 0, sinIVA };
				Object[] params2 = { idReq, 2, 2, 0, sinIVA };
				Object[] params3 = { idReq, 4, 4, 0, iva };
				Object[] params4 = { idReq, 8, 5, 0, monto };

				try {

					ConsultaTool.getInstance().insert("wf", sqlDoc, params);

					ConsultaTool.getInstance().insert("wf", sqlValores, params2);
					ConsultaTool.getInstance().insert("wf", sqlValores, params3);
					ConsultaTool.getInstance().insert("wf", sqlValores, params4);
					ConsultaTool.getInstance().insert("wf", sqlValores, params1);
					ok = true;

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (tipoDte == 32 || tipoDte == 34 || tipoDte == 60 || tipoDte == 61) {

				String sqlValores = " INSERT INTO eje_wffact_documentos_valores (id_req, id_tipovalor, secuencia, porcentaje, monto ) VALUES (?, ?, ?, ?, ?)";
				Object[] params1 = { idReq, 1, 1, 0, sinIVA };
				Object[] params2 = { idReq, 2, 3, 0, sinIVA };
				Object[] params4 = { idReq, 8, 5, 0, monto };
				ok = true;
				try {

					ConsultaTool.getInstance().insert("wf", sqlDoc, params);

					ConsultaTool.getInstance().insert("wf", sqlValores, params2);
					ConsultaTool.getInstance().insert("wf", sqlValores, params4);
					ConsultaTool.getInstance().insert("wf", sqlValores, params1);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				actualizarEstadoFactura(EstadosDeFactura.NO_HABLITADA_PARA_PROCESAR);
				return false;
			}
			return ok;
			/******** Codigos Valores *************************/
			/* 1 NETO */
			/* 2 AFECTO */
			/* 3 EXENTO */
			/* 4 IVA */
			/* 5 ILA */
			/* 6 IABA */
			/* 7 ESPEC */
			/* 8 TOTAL */

			// [mac_ist_pagoprov_v2].[dbo].[eje_wffact_docconf_tipodocto]
			/******************** Codigos tipo factura **************/
			/* Factura 30 */
			/* Factura Electrónica 33 */
			/* Nota de Crédito 60 */
			/* Nota de Crédito Electrónica 61 */
			/* Nota de Crédito Exenta 60 */
			/* Nota de Crédito Exenta Electrónica 61 */
			/* Nota de Débito Electrónica 56 */
			/* Factura Exenta Electrónica 34 */
		}
	}

	private boolean actualizarEstadoFactura(EstadosDeFactura estadoFactura) {

		boolean ok = false;
		StringBuilder sql = new StringBuilder();

		sql.append(" UPDATE eje_siifactura_import 					\n");
		sql.append(" SET 											\n");
		sql.append(" estado_factura	= " + estadoFactura.codigo + ", \n");
		sql.append(" fecha_inyeccion_en_wf = getdate()				\n");
		sql.append(" WHERE 											\n");
		sql.append(" folio = ? AND rut_emisor = ? AND tipo_dte = ?	\n");

		try {
			Object[] params = { folio, rutProveedor, dte };
			ok = ConsultaTool.getInstance().update("portal", sql.toString(), params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ok;
	}

	int obtenerAsignadoRutRol() {

		int localidad = new Integer(idLocalidad);

		int asignadoEvento = 102;
		int idRolAntes = 145;

		int asignadoRutRol = -1;

		// StringBuilder sql = new StringBuilder(
		// "SELECT TOP 1 a.RUT_ROL_ANTES FROM eje_wf_requerimientos a, eje_wfgen_persona
		// b ")
		// .append(" WHERE a.id_evento = ? and\r\n" + " a.rut_rol_antes= b.[id_persona]
		// and \r\n"
		// + " b.[vigente]=1 and \r\n"
		// + " a.ID_ROL_antes = ? and id_localidad = ? and ru.id_rol= ? ORDER BY id_req
		// DESC");

		StringBuilder sql = new StringBuilder(" SELECT TOP 1 rut FROM eje_wf_persona p ")
				.append(" inner join eje_wf_tupla_rol_usuario ru on ru.rut=p.id_persona "
						+ " where p.id_localidad= ? and ru.id_rol= ? " + " ORDER BY NEWID()");
		Object[] params = { localidad, idRolAntes };
		boolean ok = false;
		ConsultaData data;
		try {

			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			ok = data != null && data.size() > 0;

			if (data != null && data.next()) {

				asignadoRutRol = data.getInt("rut");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return asignadoRutRol;

	}

	int obtenerAsignadoRutRol(int idReceptor) {

		int asignadoRutRol = -1;

		StringBuilder sql = new StringBuilder(" SELECT TOP 1 rut, id_localidad FROM eje_wf_persona p ")
				.append(" inner join eje_wf_tupla_rol_usuario ru on ru.rut=p.id_persona "
						+ " where p.id_persona = ? AND ru.id_rol=145" + " ORDER BY NEWID()");
		Object[] params = { idReceptor };
		boolean ok = false;
		ConsultaData data;
		try {

			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			ok = data != null && data.size() > 0;

			if (data != null && data.next()) {

				asignadoRutRol = data.getInt("rut");
				Integer localidad = data.getInt("id_localidad");
				idLocalidad = localidad.toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return asignadoRutRol;

	}

	boolean existeFacturaEnWF() {

		int idProveedor = obtenerIdProveedorDesdeBd();

		StringBuilder sql = new StringBuilder(" SELECT * FROM  eje_wffact_documentos docs ")
				.append("  WHERE numdocto = ? AND "
						+ " id_tipodocto IN( SELECT id_tipodocto FROM eje_wffact_docconf_tipodocto WHERE tipodoctocontab = ? )"
						+ "  AND id_proveedor = ? ");

		Object[] params = { folio, dte, idProveedor };
		boolean ok = false;
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			ok = data != null && data.size() > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok;
	}

	ArrayList<Integer> obtenerIDTipodocto() {

		ArrayList<Integer> idTipodcto = new ArrayList<Integer>();
		StringBuilder sql = new StringBuilder(
				" SELECT id_tipodocto FROM eje_wffact_docconf_tipodocto WHERE tipodoctocontab = ? ORDER BY descrip_receptor DESC");
		Object[] params = { dte };
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
			while (data != null && data.next()) {

				idTipodcto.add(data.getInt("id_tipodocto"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idTipodcto;

	}

}
