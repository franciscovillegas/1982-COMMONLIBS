package portal.com.eje.usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.WordUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.cache.Cache;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.eje_generico_util.ConfContext;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.tools.EnumTool;

public class UsuarioInfo extends AbsClaseWeb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5165179122818425652L;
	private String param = "UsuarioInfo";
	private String paramKey_contextoImagen = "contextoImagen";
	
	public UsuarioInfo(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		
		
		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), param , paramKey_contextoImagen , EModulos.modulo_webmatico.toString());
		} catch (PPMException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost() throws Exception {
		doGet();
	}

	/*
	 * Tiene el problema que si no estas logeado no devuelve un JSON, por ende no es
	 * recomendable que se utilize en sencha
	 * 
	 */
	@Override
	public void doGet() throws Exception {
		String methodOut = super.getIoClaseWeb().getParamString("methodOut", null);
		int rutUsuario = getIoClaseWeb().getUsuario().getRutIdInt();

		DataList info = getDatosUsuarioCached(rutUsuario);
		if (methodOut == null) {
			JSonDataOut out = new JSonDataOut(info);
			super.getIoClaseWeb().retTexto(out.getListData());
		} else if ("sencha".equals(methodOut)) {
			super.getIoClaseWeb().retSenchaJson(info, true);
		}

	}

	public DataList getDatosUsuario() {
		int rut = super.getIoClaseWeb().getUsuario().getRutIdInt();
		return getDatosUsuarioCached(rut);
	}
	
	public DataList getDatosUsuarioCached(int rut) {

		DataList list = null;
		try {
			Class[] def = { int.class };
			Object[] params = { rut };

			list = Cache.weak(this, "getDatosUsuario", def, params, DataList.class);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public DataList getDatosUsuario(int rut) {
		DataList data = new DataList();
		DataFields campos = new DataFields();

		campos.put("nombre", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getName().toLowerCase())));
		campos.put("empresa", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getEmpresa())));
		campos.put("cargoid", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getCargoId())));
		campos.put("cargodesc", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getCargoDesc().toLowerCase())));
		campos.put("unidadid", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getIdUnidad())));
		campos.put("unidaddesc", new Field(WordUtils.capitalizeFully(super.getIoClaseWeb().getUsuario().getUnidad().toLowerCase())));

		ConsultaData dtaJefatura = OrganicaLocator.getInstance().getJefeDelTrabajador(super.getIoClaseWeb().getUsuario().getRutIdInt());
		while (dtaJefatura != null && dtaJefatura.next()) {
			campos.put("id_jefatura", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("rut"))));
			campos.put("jefatura_nombre", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("nombre"))));
			campos.put("jefatura_idcargo", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("id_cargo"))));
			campos.put("jefatura_cargo", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("cargo"))));
			campos.put("jefatura_idunidad", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("id_unidad"))));
			campos.put("jefatura_unidad", new Field(WordUtils.capitalizeFully(dtaJefatura.getForcedString("unidad"))));
		}

		// deprecado pero no se elimina para mantener consistencia
		
		campos.put("foto_folder", getFotoTrabajador(""));
		
		campos.put("foto", getFotoTrabajador(new StringBuilder().append(super.getIoClaseWeb().getUsuario().getRutId()).append(".jpg").toString()));
		// deprecado pero no se elimina para mantener consistencia
		campos.put("fotonoexiste",  "../../../temporal/imgtrabajadores/sinfoto.jpg");

		/*@deprecado*/
		campos.put("usuarioImagen", super.getIoClaseWeb().getUsuario().getUsuarioImagen().getNameUnic());
		
		/*@deprecado*/
		campos.put("usuarioImagenNoExiste", "temporal/imgtrabajadores/sinfoto.jpg");

		campos.put("privilegios", new Field(getApps()));
		campos.put("perfiles", getMenuHome());
		campos.put("perfilesAtributos", getAtributos());
		campos.put("colaborador", esColaborador());

		campos.put("rut", new Field(super.getIoClaseWeb().getUsuario().getRutId()));
		campos.put("rut_enc", super.getIoClaseWeb().getUsuario().getRutIdEncypted());
		campos.put("usuario", super.getIoClaseWeb().getUsuario().getLoginUsuario());
		campos.put("digito_ver", Validar.getInstance().validarDato(super.getIoClaseWeb().getUsuario().getRut().getDig(), "0"));
		campos.put("clave", new Field(super.getIoClaseWeb().getUsuario().getPassWord()));
		campos.put("mail", new Field(super.getIoClaseWeb().getUsuario().getEmail()));
		campos.put("mailValidado", new Field(super.getIoClaseWeb().getUsuario().getEmailValidado()));
		campos.put("cantidadUnidadesDescendientes", new Field(super.getIoClaseWeb().getUsuario().getUnidadesDescendientes().length));
		campos.put("cantIngresos", new Field(super.getIoClaseWeb().getUsuario().getCantIngreso()));
		campos.put("claveUltCambio", new Field(getUltCambioClave()));
		campos.put("isJefeUnidad", super.getIoClaseWeb().getUsuario().isJefeUnidad());

		DataFields dataTracking = UsuarioInfoTracking.getInstance().getDataTracking();
		if (dataTracking != null) {
			campos.put("tracking_ingresos_siempre", dataTracking.get("ingresos_siempre"));
			campos.put("tracking_ingresos_hoy", dataTracking.get("ingresos_hoy"));
			campos.put("tracking_cumpl_hoy", dataTracking.get("cumpl_hoy"));
		}

		try {
			ResourceBundle proper = ResourceBundle.getBundle("mail");

			campos.put("adminRut", proper.getString("admin.rut"));
			campos.put("adminNombre", proper.getString("admin.nombre"));
			campos.put("adminMail", proper.getString("admin.mail"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		campos.put("privilegios_webmatico", new Field(getWebmaticoPrivilegios()));

		addPrivilegio_Modulo(campos, "modulos.btn.autoservicio");
		addPrivilegio_Modulo(campos, "modulos.btn.perfcargo");
		addPrivilegio_Modulo(campos, "modulos.btn.ept");
		addPrivilegio_Modulo(campos, "modulos.btn.gdd");
		addPrivilegio_Modulo(campos, "modulos.btn.vacaciones");
		addPrivilegio_Modulo(campos, "modulos.btn.capacitacion");
		addPrivilegio_Modulo(campos, "modulos.btn.movpersonal");
		addPrivilegio_Modulo(campos, "modulos.btn.ficha_actdatos");

		campos.put("pago", new Field(getFormaDePago()));

		data.add(campos);

		return data;
	}

	private void addPrivilegio_Modulo(DataFields input, String jsonKey) {

	}

	private String getFotoTrabajador(String nombreImg) {
		String img = null;
		ParametroValue pv = ParametroLocator.getInstance().getValor(param, paramKey_contextoImagen);
		if(pv != null) {
			
			EModulos modImg = EnumTool.getEnumByToString(EModulos.class,   pv.getValue(), EModulos.nodefinido);
			if(modImg != null && modImg != EModulos.nodefinido) {
				portal.com.eje.serhumano.user.Usuario u = getIoClaseWeb().getUsuario();
				
				img = ConfContext.getInstance().getConfContextUrl().getUrl(getIoClaseWeb(), modImg, 
																		new StringBuilder()
																		.append("/temporal/imgtrabajadores/").append(nombreImg).toString());
				
			 
			}
		}
		
		if(img == null) {
			img = "../../../temporal/imgtrabajadores/sinfoto.jpg"; 
		}
		
		
		return img;
	}
	
	private Field esColaborador() {
		PerfilUsuario perfilUsr = new PerfilUsuario(super.getIoClaseWeb().getUsuario().getRutId());

		List<PerfilUsuario> perfiles = perfilUsr.getPerfiles();
		DataList data = new DataList();
		Set<String> descSeleccionadas = new HashSet<String>();
		boolean valor = false;
		for (PerfilUsuario perfil : perfiles) {

			if (perfil.id == 9)
				valor = true;

		}

		return new Field(valor);
	}

	private IDataOut getAtributos() {

		PerfilUsuario perfilUsr = new PerfilUsuario(super.getIoClaseWeb().getUsuario().getRutId());

		List<PerfilUsuario> perfiles = perfilUsr.getPerfiles();
		DataList data = new DataList();
		Set<String> descSeleccionadas = new HashSet<String>();

		for (PerfilUsuario perfil : perfiles) {

			descSeleccionadas.addAll(perfil.descSeleccionadas);
		}

		// System.out.println("*** elementos seleccionados"+descSeleccionadas.size());

		data.addAll(PerfilUsuario.getAtributosSeleccionados(descSeleccionadas));

		// System.out.println("*** Elementos filtrados"+data.size());

		JSonDataOut out = new JSonDataOut(data);

		JSarrayDataOut outJS = new JSarrayDataOut(data);
		return outJS;
	}

	private IDataOut getMenuHome() {

		PerfilUsuario perfilUsr = new PerfilUsuario(super.getIoClaseWeb().getUsuario().getRutId());

		// List<PerfilUsuario> perfiles = perfilUsr.getPerfiles();
		// DataList data = new DataList();
		//
		//
		// for (PerfilUsuario perfil : perfiles) {
		// DataFields campos = new DataFields();
		//
		// campos.put("idPerfil", perfil.id);
		// campos.put("nomPerfil", perfil.nombre);
		// campos.put("descSeleccionadas", new
		// Field(perfil.getDescSeleccionaDataField()));
		//
		// data.add(campos);
		// }
		//
		// data.addAll(PerfilUsuario.getPerfilesQuePuedeVer(perfilUsr.getPerfiles()));
		//
		// JSonDataOut out = new JSonDataOut(data);
		//
		// DataList data2 = new DataList();
		// DataFields campos2 = new DataFields();
		// campos2.put("out", new Field(out));
		// data2.add(campos2);

		DataList data3 = new DataList();
		List<PerfilUsuario> perfiles = perfilUsr.getPerfiles();
		Collection<DataFields> cols = PerfilUsuario.getPerfilesQuePuedeVer(perfiles);
		data3.addAll(cols);
		JSarrayDataOut outJS = new JSarrayDataOut(data3);
		return outJS;
	}

	private IDataOut getWebmaticoPrivilegios() {
		List<Map<String, String>> lista = super.getIoClaseWeb().getUsuario().getPrivilegio_zonasWebmatico();
		DataList data = new DataList();

		for (Map<String, String> m : lista) {
			DataFields campos = new DataFields();

			Set<String> set = m.keySet();
			for (String s : set) {
				campos.put(s, new Field(m.get(s)));
			}

			data.add(campos);
		}

		JSonDataOut out = new JSonDataOut(data);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;
	}

	private IDataOut getFormaDePago() {

		StringBuffer strSQL = new StringBuffer();

		strSQL.append("select forma_pago, banco, cta_cte from eje_ges_trabajador where rut=").append(super.getIoClaseWeb().getUsuario().getRutId());

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSonDataOut out = new JSonDataOut(data);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;
	}

	private IDataOut getLinks() {

		List<String> lista = super.getIoClaseWeb().getUsuario().getApps();
		DataList data = new DataList();
		DataFields campos = null;

		for (String s : lista) {
			campos = new DataFields();
			campos.put("app", new Field(s));

			data.add(campos);
		}

		JSonDataOut out = new JSonDataOut(data);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;

	}

	private IDataOut getApps() {
		List<String> lista = super.getIoClaseWeb().getUsuario().getApps();
		DataList data = new DataList();
		DataFields campos = null;

		for (String s : lista) {
			campos = new DataFields();
			campos.put("app", new Field(s));

			data.add(campos);
		}

		JSonDataOut out = new JSonDataOut(data);

		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);

		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;
	}

	private String getUltCambioClave() {
		String sql = "select passw_ult_cambio from eje_ges_usuario  where rut_usuario = ? ";
		String retorno = null;
		Object[] params = { super.getIoClaseWeb().getUsuario().getRutId() };
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);

			if (data.next()) {
				Date date = data.getDateJava("passw_ult_cambio");
				if (date != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					retorno = format.format(date);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retorno;
	}

}
