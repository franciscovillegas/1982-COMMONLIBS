package cl.eje.generico.fileremote.ws.injectar;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class UtilFacturas {

	public Set<Factura> obtenerFacturasAProcesar() {

		StringBuilder sql = new StringBuilder();
		Set<Factura> facturasAIngresarAlSistema = new HashSet<Factura>();

		sql.append(" SELECT wf.id_corr, wf.rut_emisor, wf.folio, wf.tipo_dte, wf.precio_con_iva, wf.file_name  \n");
		sql.append(" FROM eje_siifactura_por_insertar_al_wf wf  \n");

		
//		  sql.append(" SELECT wf.id_corr, wf.rut_emisor, wf.folio, wf.tipo_dte, wf.precio_con_iva, wf.file_name  \n");
//		  sql.append(" FROM eje_siifactura_por_insertar_al_wf wf LEFT JOIN eje_siifactura_import im ON wf.file_name = im.file_name WHERE im.estado_factura = 15 \n");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (data != null) {

			while (data != null && data.next()) {

				String rutEmisor = data.getForcedString("rut_emisor");
				int id = data.getInt("id_corr");
				int folio = data.getInt("folio");
				int tipoDte = data.getInt("tipo_dte");
				int monto = data.getInt("precio_con_iva");
				String nombre = data.getForcedString("file_name");

				Factura factura = new Factura(id, rutEmisor, folio, tipoDte, monto, nombre);
				facturasAIngresarAlSistema.add(factura);

			}
		}return facturasAIngresarAlSistema;

	}

	public Factura obtenerFacturaXNombre(String nombreFactura) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select id_corr, rut_emisor, folio, tipo_dte,  precio_con_iva, file_name \n");
		sql.append(" from eje_siifactura_por_insertar_al_wf WHERE file_name = ? \n");

		ConsultaData data = null;
		Object[] params = { nombreFactura };
		Factura factura = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (data != null) {

			if (data != null && data.next()) {

				String rutEmisor = data.getForcedString("rut_emisor");
				String nombre = data.getForcedString("file_name");
				int id = data.getInt("id_corr");
				int folio = data.getInt("folio");
				int tipoDte = data.getInt("tipo_dte");
				int monto = data.getInt("precio_con_iva");

				factura = new Factura(id, rutEmisor, folio, tipoDte, monto, nombre);

			}
		}
		return factura;
	}

}
