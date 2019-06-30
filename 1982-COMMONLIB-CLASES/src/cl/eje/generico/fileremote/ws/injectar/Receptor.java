package cl.eje.generico.fileremote.ws.injectar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.web.datos.ConsultaTool;

public class Receptor {

	public ConsultaData obtenerReceptores() {

		String nombre = "";
		List<String> cols = new ArrayList<String>();
		cols.add("id_persona");
		cols.add("nombre");
		cols.add("id_localidad");
		cols.add("RUT");
		cols.add("vigente");

		ConsultaData dataFinal = new ConsultaData(cols);
		StringBuilder sql = new StringBuilder(" select * \r\n" + "	from eje_wf_persona p \r\n"
				+ "	inner join eje_wf_tupla_rol_usuario ru on ru.rut=p.id_persona \r\n" + "	where  ru.id_rol = 145 ");

		boolean ok = false;
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString());
			while (data != null && data.next()) {

				DataFields fields = new DataFields();

				fields.put("id_persona", data.getInt("id_persona"));
				fields.put("nombre", data.getString("nombre"));
				fields.put("id_localidad", data.getInt("id_localidad"));
				fields.put("RUT", data.getInt("RUT"));
				fields.put("vigente", data.getInt("vigente"));

				dataFinal.add(fields);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataFinal;

	}

	public ConsultaData obtenerReceptoresXRegion(int idRegion) {

		String nombre = "";

		List<String> cols = new ArrayList<String>();

		cols.add("id_persona");
		cols.add("nombre");
		cols.add("id_localidad");
		cols.add("RUT");
		cols.add("vigente");

		ConsultaData dataFinal = new ConsultaData(cols);
		StringBuilder sql = new StringBuilder(" select * \r\n" + "	from eje_wf_persona p \r\n"
				+ "	inner join eje_wf_tupla_rol_usuario ru on ru.rut=p.id_persona \r\n"
				+ "	where id_localidad = ? and ru.id_rol = 145 ");

		Object[] params = { idRegion };
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);

			while (data != null && data.next()) {

				DataFields fields = new DataFields();
				fields.put("id_persona", data.getInt("id_persona"));
				fields.put("nombre", data.getString("nombre"));
				fields.put("id_localidad", data.getInt("id_localidad"));
				fields.put("RUT", data.getInt("RUT"));
				fields.put("vigente", data.getInt("vigente"));

				dataFinal.add(fields);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataFinal;

	}

	public ConsultaData buscarXNombre(String nombre) {

		nombre = "%" + nombre + "%";

		List<String> cols = new ArrayList<String>();

		cols.add("id_persona");
		cols.add("nombre");
		cols.add("id_localidad");
		cols.add("RUT");
		cols.add("vigente");

		ConsultaData dataFinal = new ConsultaData(cols);
		StringBuilder sql = new StringBuilder(" select * \r\n" + "	from eje_wf_persona p \r\n"
				+ "	inner join eje_wf_tupla_rol_usuario ru on ru.rut=p.id_persona \r\n"
				+ "	where nombre like ? and ru.id_rol = 145 ");

		Object[] params = { nombre };
		ConsultaData data;

		try {
			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);

			while (data != null && data.next()) {

				DataFields fields = new DataFields();

				fields.put("id_persona", data.getInt("id_persona"));
				fields.put("nombre", data.getString("nombre"));
				fields.put("id_localidad", data.getInt("id_localidad"));
				fields.put("RUT", data.getInt("RUT"));
				fields.put("vigente", data.getInt("vigente"));

				dataFinal.add(fields);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataFinal;

	}

}
