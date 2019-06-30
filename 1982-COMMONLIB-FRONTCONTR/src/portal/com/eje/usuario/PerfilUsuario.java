package portal.com.eje.usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.serhumano.user.UsuarioPerfil;

public class PerfilUsuario {

	Integer id;
	String nombre, rut, rutUsuario;
	Set<String> descSeleccionadas;

	public PerfilUsuario(String rutUsuario) {
		this.rutUsuario = rutUsuario;
	}

	private PerfilUsuario() {
		this.rutUsuario = rutUsuario;
	}

	public List<PerfilUsuario> getPerfiles() {

		return obtenerUsuarioPerfil();

	}

	private List<PerfilUsuario> obtenerUsuarioPerfil() {

		StringBuilder str = new StringBuilder();
		str.append(" SELECT distinct p.id,p.nombre,r.rut ");
		str.append(
				"  FROM eje_generico_perfiles_webmatico p LEFT OUTER JOIN eje_generico_perfiles_rut_webmatico r ON p.id=r.perfil  ");
		str.append(" WHERE r.rut = ? ");
		String[] params = { rutUsuario };
		List<PerfilUsuario> perfil = new ArrayList<PerfilUsuario>();

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", str.toString(), params);
			
			if(data != null && data.size() > 0 ) {
				while (data.next()) {
					PerfilUsuario perfilUsuario = new PerfilUsuario();
	
					perfilUsuario.id = data.getInt("id");
					perfilUsuario.nombre = String.valueOf(data.getString("nombre"));
					perfilUsuario.rut = String.valueOf(data.getInt("rut"));
					perfilUsuario.descSeleccionadas = obtenerDescripcionPerfil(perfilUsuario.id);
					perfil.add(perfilUsuario);
				}
			}
			else {
				str = new StringBuilder();
				str.append(" SELECT distinct p.id,p.nombre,rut= ").append(rutUsuario);
				str.append("  FROM eje_generico_perfiles_webmatico p  ");
				str.append(" WHERE p.is_default = 1 ");

				data = ConsultaTool.getInstance().getData("portal", str.toString());
				
				while (data.next()) {
					PerfilUsuario perfilUsuario = new PerfilUsuario();
	
					perfilUsuario.id = data.getInt("id");
					perfilUsuario.nombre = String.valueOf(data.getString("nombre"));
					perfilUsuario.rut = String.valueOf(data.getInt("rut"));
					perfilUsuario.descSeleccionadas = obtenerDescripcionPerfil(perfilUsuario.id);
					perfil.add(perfilUsuario);
				}
					
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error al extraer datos");
		}

		return perfil;
	}

	public IDataOut getDescSeleccionaDataField() {

		DataFields campos = new DataFields();
		DataList descripcionesSelec = new DataList();

		for (String descSeleccionada : descSeleccionadas) {

			campos.put(descSeleccionada, descSeleccionada);
		}

		descripcionesSelec.add(campos);
		JSonDataOut out = new JSonDataOut(descripcionesSelec);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;

	}

	private Set<String> obtenerDescripcionPerfil(int idPerfil) {

		StringBuilder str = new StringBuilder();
		str.append(" SELECT app_id ");
		str.append(" FROM eje_generico_webmatico_ejegesuserapp ");
		str.append(" WHERE id_perfil = ? ");
		String[] params = { Integer.toString(idPerfil) };
		Set<String> descPerfil = new HashSet<String>();

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", str.toString(), params);
			while (data.next()) {
				String nomDescripPerfil = String.valueOf(data.getString("app_id"));

				descPerfil.add(nomDescripPerfil);
			}
		} catch (SQLException e) {
			System.out.println("Error al extraer datos");
		}

		return descPerfil;

	}

	public static Collection<DataFields> getPerfilesQuePuedeVer(List<PerfilUsuario> perfiles) {

		Collection<DataFields> menuOpciones = new ArrayList<DataFields>();
		String menuNombre = "out";

		Set<OpcionMenuHome> opciones = OpcionMenuHome.getMenuPorPerfil(perfiles);
		for (OpcionMenuHome menu : opciones) {
			DataList descripcionesSelec = new DataList();
			IDataOut menujsn = getMenuJson(menu);
			DataFields camposMenuAmostrar = new DataFields();

			camposMenuAmostrar.put("menu", (new Field(menujsn)));
			menuNombre = menu.name();
			descripcionesSelec.add(camposMenuAmostrar);

			JSarrayDataOut suboutJS = new JSarrayDataOut(descripcionesSelec);

			DataList data2 = new DataList();
			DataFields campos2 = new DataFields();
			campos2.put(menuNombre, new Field(suboutJS));

			menuOpciones.add(campos2);
		}

		return menuOpciones;

		// data2.add(campos2);
		//
		// JSarrayDataOut outJS = new JSarrayDataOut(data2);
		// return outJS;

	}

	private static IDataOut getMenuJson(OpcionMenuHome menu) {

		DataList descripcionesSelec = new DataList();
		DataFields campos = new DataFields();

		campos.put("menuhref", menu.contenidoHref);
		campos.put("titulohref", menu.textoEtiqueta);
		campos.put("codigo", menu.name());
		descripcionesSelec.add(campos);

		JSarrayDataOut suboutJS = new JSarrayDataOut(descripcionesSelec);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put(menu.name(), new Field(suboutJS));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;

	}

	public static Collection<DataFields>   getAtributosSeleccionados(Set<String> descSeleccionadas2) {

		Collection<DataFields> menuOpciones = new ArrayList<DataFields>();
		String menuNombre ="out";
		
		for (OpcionMenuHome menu : OpcionMenuHome.getMenuPorAtributo(descSeleccionadas2)) {

			DataList descripcionesSelec = new DataList();
			IDataOut menujsn = getMenuJson(menu);
			DataFields camposMenuAmostrar = new DataFields();

			camposMenuAmostrar.put("menu", (new Field(menujsn)));
			menuNombre = menu.name();
			descripcionesSelec.add(camposMenuAmostrar);

			JSarrayDataOut suboutJS = new JSarrayDataOut(descripcionesSelec);

			DataList data2 = new DataList();
			DataFields campos2 = new DataFields();
			campos2.put(menuNombre, new Field(suboutJS));

			menuOpciones.add(campos2);
		}

		return menuOpciones;
		
		

	}

	public String toString() {
		return "["+id + " " + nombre + "]";
	}
}
