package portal.com.eje.portal.trabajador;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPaged;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.tools.ArrayFactory;
import portal.com.eje.usuario.UsuarioImagenManager;
import portal.com.eje.usuario.VoUsaurioImagen;

public class TrabajadorData implements ITrabajadorData {

 
	public ConsultaData getDataPersonal(int rut) {
		
		TrabajadorDataFiltro filtro = new TrabajadorDataFiltro();
		filtro.setDataPersonal(true);
		filtro.setDataContacto(true);
		
		return getDataFiltrada(filtro, rut);
	}

	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, int rut) {
		ArrayFactory array = new ArrayFactory();
		array.add(rut);
		return getDataFiltrada(filtro, array);
	}
	
	public ConsultaDataPaged getDataFiltrada(TrabajadorDataFiltro filtro) {
		ISenchaPage page = filtro.getPage();
		
		String sqlPageTop	= "";
		String sqlPageWhere	= "";
		
		
		if(page == null) {
			System.out.println("@@@@ Error, al no tener filtro la clase trabajará en exceso, consumiendo muchos más recursos de los debiera, se recomiento encarecidamente que se agregue la página a mostrar.("+this.getClass()+")" );
		}
		else {
			sqlPageTop 		= " top "+page.getLimit();
			sqlPageWhere	= " not t.rut in (select top "+page.getLimit() * (page.getPage() - 1 )+" t2.rut from eje_ges_trabajador t2 "+getOrderBy(filtro.getPage(), "t2")+") ";
			
			if(filtro.getWord() != null) {
				String like = "'%"+StringEscapeUtils.escapeSql(filtro.getWord()).replaceAll(" ", "\\%")+"%'";
				like = like.replaceAll("\\%\\%", "\\%");
				sqlPageWhere += " and (nombre like ("+like+") or domicilio like ("+like+")) ";
			}
			
		}
		
		/*
		 * Aquí debería haber cache.
		 * */
		String sql = "select "+sqlPageTop+" t.rut from eje_ges_trabajador t ";
		
		if(page != null) {
			sql+=" where "+sqlPageWhere+" "+getOrderBy(filtro.getPage(), "t");
		}
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			ArrayFactory factory = new ArrayFactory();
			
			while(data != null && data.next()) {
				factory.add(data.getInt("rut"));
			}
			
			ConsultaData subDataRetorno = getDataFiltrada(filtro, factory);
			ConsultaDataPaged dataRetorno = new ConsultaDataPaged(subDataRetorno, page, getTotalSize());
			
			return dataRetorno;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getOrderBy(ISenchaPage page, String alias) {
		alias = Validar.getInstance().validarDato(alias, "");
		String orderBy		= "";
		
		if(page != null) {
			ConsultaData sorts = page.getSorts();
			
			if(sorts != null) {
				sorts.toStart();
				while(sorts.next()) {
					if(orderBy.length() > 0) {
						orderBy+=",";
					}
					orderBy+=" "+alias+"."+sorts.getString("property")+" "+sorts.getString("direction");
				}
				
				if(orderBy.length() > 0) {
					orderBy= " ORDER BY " + orderBy;
				}
			}
		}
		
		return orderBy;
	}
	
	private int getTotalSize() {
		String sql = "SELECT cantidad=count(rut) FROM EJE_GES_TRABAJADOR ";
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			if(data != null && data.next()) {
				return data.getInt("cantidad");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

 
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, cl.ejedigital.tool.strings.ArrayFactory ruts) {
		ArrayFactory rutsComoDebeSer = new ArrayFactory();
		
		Object[] array = ruts.toArray();
		
		for(Object o : array) {
			rutsComoDebeSer.add(String.valueOf(o));
		}
		
		return getDataFiltrada(filtro, rutsComoDebeSer);
	}
	
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, ArrayFactory ruts) {
		
		if(ruts == null || ruts.size() > 500) {
			throw new RuntimeException("No pueden cargarse más de 100 registros simultaneamente, dicho de otra forma, no es posible tener páginas con más de 100 personas.");
		}
		
		ConsultaData data = null;
		
		if(ruts != null) {
		
			StringBuilder sql = new StringBuilder();
			sql.append(" select id_persona=t.rut, ");
			
			if(filtro.isDataResumen()) {
				sql.append("	resumen='', ");
			}
			
			sql.append(" 		imagen_path='',rut, digito_ver, ");
			
			if(filtro.isDataPersonal()) {
				sql.append(" 	nombre=ltrim(rtrim(t.nombre)), ");
				sql.append(" 	nombres=ltrim(rtrim(t.nombres)), ");
				sql.append(" 	ape_paterno=ltrim(rtrim(t.ape_paterno)), ");
				sql.append(" 	ape_materno=ltrim(rtrim(t.ape_materno)), ");
				sql.append(" 	t.fecha_nacim, fecha_nacim_120=convert(varchar, t.fecha_nacim, 120), ");
				sql.append(" 	pais=ltrim(rtrim(t.pais)),  ");
			}
			
			if(filtro.isDataContacto() || filtro.isDataContactoFono() || filtro.isDataContactoCelular() || filtro.isDataContactoMail() || filtro.isDataContactoAnexo()) {
				if(filtro.isDataContactoFono()) {
					sql.append(" 	t.telefono, ");
				}
				
				if(filtro.isDataContactoCelular()) {
					sql.append(" 	t.celular, ");
				}
				
				if(filtro.isDataContactoMail()) {
					sql.append(" 	mail=(select top 1 cr.mail from eje_ges_trabajador cr where cr.rut = t.rut),  ");
				}
				
				if(filtro.isDataContactoAnexo()) {
					sql.append(" 	t.anexo,  ");
				}
			}
			
			if(filtro.isDataDomicilio()) {
				sql.append(" 	domicilio=ltrim(rtrim(t.domicilio)), ");
				sql.append(" 	comuna=ltrim(rtrim(t.comuna)), ");
				sql.append(" 	ciudad=ltrim(rtrim(t.ciudad)), ");
			}
			
			if(filtro.isDataUnidad()) {
				sql.append(" 	t.unidad,  ");
				sql.append(" 	unidad_desc=(select top 1 ltrim(rtrim(unid_desc)) from eje_ges_unidades where unid_id = t.unidad ),  ");
			}
			
			if(filtro.isDataEmpresa()) {
				sql.append(" 	t.wp_cod_empresa,  ");
				sql.append(" 	empresa=(Select top 1 descrip from eje_ges_empresa where empresa = t.wp_cod_empresa ),  ");
			}
			
			if(filtro.isDataCargo()) {
				sql.append(" 	t.cargo,  ");
				sql.append(" 	cargo_desc=(select top 1 ltrim(rtrim(descrip)) from eje_ges_cargos c where c.cargo = t.cargo and c.empresa = t.wp_cod_empresa and c.vigente = 'S'),  ");
			}
			
			if(filtro.isDataContratacion()) {
				sql.append(" 	t.fecha_ingreso, fecha_ingreso_120=convert(varchar, t.fecha_ingreso, 120), ");
				sql.append(" 	t.fecha_corporacion, fecha_corporacion_120=convert(varchar, t.fecha_corporacion, 120), ");
				sql.append(" 	t.fec_ing_hold, fec_ing_hold_120=convert(varchar, t.fec_ing_hold, 120), ");
			}

			if(filtro.isDataJefatura()) {
				sql.append("    rut_jefe='',");
				sql.append("    nombre_jefe='',");
				sql.append("    unidad_jefe='',");
				sql.append("    unidad_id_jefe='',");
				sql.append("    cargo_jefe='',");
				sql.append("    cargo_id_jefe='',");
			}
			
			if(filtro.isDataFormaPago()) {
				sql.append("    forma_pago=rtrim(ltrim(t.forma_pago)), banco=rtrim(ltrim(t.banco)), cta_cte=rtrim(ltrim(t.cta_cte)), ");
			}
			
			sql.append(" now_date=convert(varchar(10), getdate(), 112) ");
			sql.append(" from  eje_ges_trabajador t ");
			sql.append(" where t.rut in ").append(ruts.getArrayInteger()).append("\n");
			sql.append( getOrderBy(filtro.getPage(), "t"));
	
			try {
				 data = ConsultaTool.getInstance().getData("portal", sql.toString());
				 if(data != null) {
					 if(filtro.isCamelCaseActivate()) {

						 List<String> columnas = data.getNombreColumnas();
						 while(data.next()) {
							 for(String c: columnas) {
								 if(data.getField(c).getObject() instanceof String) {
									 data.getActualData().put(c, WordUtils.capitalizeFully(data.getForcedString(c)));
								 }
							 }
						 } 
						 
					 }
					 
					 data.toStart();
					 while(data.next()) {
						 putFoto(data, filtro);
						 
						 if(filtro.isDataJefatura()) {
							 putJefaturaData(data);
						 }
						 
						 if(filtro.isDataResumen()) { 
							 putResumen(data,filtro);	
						 }
					 }
					 data.toStart();
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		else {
			throw new InvalidParameterException("Deben venir ruts y actualmente no vienen o es el objeto contenedor es null");
		}
		
		return data;
	}
	
	private void putResumen(ConsultaData data, TrabajadorDataFiltro filtro) {
		 StringBuilder resumen = new StringBuilder();
		 if(filtro.isDataPersonal()) {
			 resumen.append("Rut\t\t:"+data.getInt("rut")).append("-").append(data.getString("digito_ver")).append("<br/>\n");
			 resumen.append("Nombre\t\t:").append( data.getString("nombre")).append("<br/>\n");
			 resumen.append("Fecha de nacimiento\t\t:").append( Formatear.getInstance().toLargeDate(data.getDateJava("fecha_nacim"))).append("<br/>\n");
			 resumen.append("Nacionalidad\t\t:").append( data.getString("pais")).append("<br/>\n");		
		 }
		 
		 if(filtro.isDataContacto()) {
			 resumen.append(resumen.toString().length() > 0 ? "<hr/>\n" : "");
			 resumen.append("Teléfono\t\t:").append( data.getString("telefono")).append("<br/>\n");
			 resumen.append("Celular\t\t:").append( data.getString("celular")).append("<br/>\n");	 
			 resumen.append("Email\t\t:").append( data.getString("mail")).append("<br/>\n");

		}
			
		if(filtro.isDataDomicilio()) {
			resumen.append(resumen.toString().length() > 0 ? "<hr/>\n" : "");
			resumen.append("Domicilio\t\t:").append( data.getString("domicilio")).append("<br/>\n");
			resumen.append("Ciudad\t\t:").append( data.getString("ciudad")).append("<br/>\n");
			resumen.append("Comuna\t\t:").append( data.getString("comuna")).append("<br/>\n");
		}
		
		
		if(filtro.isDataEmpresa() || filtro.isDataCargo()) {
			resumen.append(resumen.toString().length() > 0 ? "<hr/>\n" : "");
			if(filtro.isDataEmpresa()) {
				resumen.append("Empresa:").append(data.getString("empresa")).append("<br/>\n");
			}
			
			if(filtro.isDataCargo()) {
				resumen.append("Cargo:").append(data.getString("cargo_desc")).append("[").append(data.getString("cargo")).append("]").append("<br/>\n");
			}
		}
		
		if(filtro.isDataUnidad()) {
			 resumen.append(resumen.toString().length() > 0 ? "<hr/>\n" : "");
			 resumen.append("Unidad Actual:").append(data.getString("unidad_desc")).append("[").append(data.getString("unidad")).append("]").append("<br/>\n");
		}
		 
		if(filtro.isDataContratacion()) {
			resumen.append(resumen.toString().length() > 0 ? "<hr/>\n" : "");
			resumen.append("Fecha Ingreso:").append(Formatear.getInstance().toLargeDate(data.getDateJava("fecha_ingreso"))).append("<br/>\n");
			 
			 if(data.getDateJava("fecha_ingreso") != null && data.getDateJava("fecha_corporacion") != null && 
				data.getDateJava("fecha_ingreso").getTime() != data.getDateJava("fecha_corporacion").getTime() ) {
				resumen.append("Fecha Reconocimiento:").append(Formatear.getInstance().toLargeDate(data.getDateJava("fecha_corporacion"))).append("<br/>\n");
			 }
		}
		

		 
		 data.getActualData().put("resumen", resumen.toString());
	}
	
	private void putFoto(ConsultaData data, TrabajadorDataFiltro filtro) {
		UsuarioImagenManager ui = new UsuarioImagenManager();
		VoUsaurioImagen voImagen = ui.getImagen(data.getInt("rut"));
		data.getActualData().put("imagen_path", filtro.getPathBacks() + voImagen.getNameUnic());
	}
	
	private void putJefaturaData(ConsultaData data) {
		
		ConsultaData dataJefe = OrganicaLocator.getInstance().getJefeDelTrabajador(data.getInt("rut"));
		if(dataJefe != null && dataJefe.next()) {
			data.getActualData().put("rut_jefe", dataJefe.getInt("rut"));
			data.getActualData().put("unidad_id_jefe", dataJefe.getString("unid_id"));
			data.getActualData().put("unidad_jefe", WordUtils.capitalizeFully(dataJefe.getString("unid_desc")));
			data.getActualData().put("cargo_jefe", WordUtils.capitalizeFully(dataJefe.getString("cargo")));
			data.getActualData().put("cargo_id_jefe", dataJefe.getString("id_cargo"));
			data.getActualData().put("nombre_jefe", WordUtils.capitalizeFully(dataJefe.getString("nombre")));
		}
	}

	@Override
	public boolean createOrUpdatePassword(int rut, String clave) {
		boolean ok = true;
		if(!existPassword(rut)) {
			ok &= addPassword(rut, String.valueOf(rut), clave);
		}
		else {
			ok &= updPassword(rut, String.valueOf(rut), clave);
		}
		
		if(!existPrivilegio(rut, "sh")) {
			ok &= addPrivilegio(rut, "sh");
		}
		
		return ok;
	}
	
	private boolean existPassword(int rut) {
		String sql = "select * from eje_ges_usuario where rut_usuario = ? ";
		Object[] params = {rut};
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data != null && data.next();
	}

	private boolean addPassword(int rut, String usuario, String clave) {
		boolean ok = false;
		
		String sql = " insert into eje_ges_usuario (login_usuario,password_usuario,rut_usuario,wp_cod_empresa,wp_cod_planta,md5) ";
		sql+= " values (?,?,?,?,?,?)";
		Object[] params = {usuario, clave, rut, -1, -1 , "N"};
		
		ConsultaData data = null;
		try {
			ConsultaTool.getInstance().insert("portal", sql, params);
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	private boolean updPassword(int rut, String usuario, String clave) {
		boolean ok = false;
		
		String sql = " update eje_ges_usuario ";
		sql+= " set password_usuario=? ";
		sql+= " where rut_usuario = ? and login_usuario = ?";
		Object[] params = {clave, rut, usuario};
		
		try {
			ConsultaTool.getInstance().update("portal", sql, params);
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	private boolean existPrivilegio(int rut, String apps) {
		boolean ok = false;
		
		String sql = " select * from eje_ges_user_app where app_id = ? and rut_usuario = ? ";
		Object[] params = {apps, rut};
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql, params);
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data != null && data.next();
	}
	
	private boolean addPrivilegio(int rut, String appID) {
		boolean ok = true;
		String sql = " insert into eje_ges_user_app (app_id, rut_usuario, vigente , wp_cod_empresa, wp_cod_planta) ";
		sql += " values (?, ?, ? , ?, ?) ";
		
		Object[] params = {appID, rut, 1, -1 , -1};
		
		try {
			ConsultaTool.getInstance().insert("portal", sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
}
