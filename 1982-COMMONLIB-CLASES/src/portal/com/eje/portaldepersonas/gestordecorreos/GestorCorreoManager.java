package portal.com.eje.portaldepersonas.gestordecorreos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.tool.strings.SqlLike;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portaldepersonas.gestordecorreos.enums.EnumGestorCorreosBandejas;
import portal.com.eje.tools.ConsultaDataEncrypter;

public class GestorCorreoManager {

	public static GestorCorreoManager getInstance() {
		return Mng.getInstance(GestorCorreoManager.class);
	}

	public CtrGestorCorreo getCtr() {
		return CtrGestorCorreo.getCtr();
	}

	public ConsultaData getCorreos(Integer rut, EnumGestorCorreosBandejas bandeja, String query, Integer rutRemitente, boolean encriptar, boolean getContenidoMail, ISenchaPage page) throws SQLException {
		return getCorreos(rut, null, bandeja, query, rutRemitente, encriptar, getContenidoMail, page);
	}

	public ConsultaData getCorreos(Integer rut, Integer idCorreo, EnumGestorCorreosBandejas bandeja, String query, Integer rutRemitente, boolean encriptar, boolean getContenidoMail, ISenchaPage page) throws SQLException {

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select 					");
		sql.appendLine(" 	be.id_correo, 			");
		sql.appendLine(" 	be.fecha_recepcion, 	");
		sql.appendLine(" 	be.fecha_envio, 		");
		sql.appendLine(" 	be.fecha_papelera, 	");
		sql.appendLine(" 	be.fecha_eliminacion, 	");

		sql.appendLine(" 	be.rut_remitente, 		");

		sql.appendLine(" 	be.titulo, 				");
		sql.appendLine(" 	be.favorito, 			");

		if (getContenidoMail) {
			sql.appendLine(" 	be.mensaje_full, 		");
		}

		sql.appendLine(" 	r.nombres,  			");
		sql.appendLine(" 	r.e_mail  				");

		sql.appendLine(" from eje_ges_bandeja_correo_entrada be ");
		sql.appendLine("	inner join eje_ges_bandeja_correo_remitente r on be.rut_remitente = r.rut_remitente 	");
		sql.appendLine(" where 1=1 ");

		List<Object> params = new ArrayList<Object>();
		if (rut != null) {
			sql.appendLine(" and be.rut_receptor = ? ");
			params.add(rut);
		}

		if (rutRemitente != null) {
			sql.appendLine(" and r.rut_remitente = ? ");
			params.add(rutRemitente);
		}

		if (idCorreo != null) {
			sql.appendLine(" and be.id_correo = ? ");
			params.add(idCorreo);
		} else {

			switch (bandeja) {
			case FAVORITOS:
				sql.appendLine(" and be.favorito = 1 ");
				sql.appendLine(" and be.fecha_papelera is null ");
				break;
			case PAPELERA:
				sql.appendLine(" and not be.fecha_papelera is null ");
				break;
			default:
				sql.appendLine(" and be.fecha_papelera is null ");
				break;
			}

		}

		sql.appendLine(" and be.fecha_eliminacion is null ");

		if (query != null && !"".equals(query)) {
			sql.appendLine(" and (be.titulo like (?)					");
			sql.appendLine(" 		or r.nombres like (?)				");
			sql.appendLine(" 		or r.ape_paterno like (?) 			");
			sql.appendLine("		or r.ape_materno like (?))			");

			query = SqlLike.buildLikeParam(query);
			params.add(query);
			params.add(query);
			params.add(query);
			params.add(query);
		}
		
		if(page != null ) {
			page.addSort("fecha_envio", "desc");
		}
		
		ConsultaData data = ConsultaTool.getInstance().getDataPagged("portal", sql, params.toArray(), page, "id_correo");

		if (encriptar) {
			ConsultaDataEncrypter.getInstance().encrypt(data, new String[] { "id_correo" });
		}

		return data;
	}

	/**
	 * es la misma consulta que getCorreos pero agrupada por remitentes
	 * 
	 * @author Pancho
	 * @since 11-10-2018
	 * @see #getCorreos(Integer, Integer, Boolean, Boolean, String, boolean)
	 */
	public ConsultaData getRemitentes(Integer rut, EnumGestorCorreosBandejas bandeja, String query, boolean encriptar) throws SQLException {

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select 					");

		sql.appendLine(" 	r.rut_remitente, 	");
		sql.appendLine(" 	r.nombres,			");
		sql.appendLine(" 	r.ape_paterno,		");
		sql.appendLine(" 	r.ape_materno,		");
		sql.appendLine(" 	r.foto_original,	");
		sql.appendLine(" 	r.foto_pequena,		");
		sql.appendLine(" 	r.e_mail ");

		sql.appendLine(" from eje_ges_bandeja_correo_entrada be ");
		sql.appendLine("	inner join eje_ges_bandeja_correo_remitente r on be.rut_remitente = r.rut_remitente 	");
		sql.appendLine(" where 1=1 ");

		List<Object> params = new ArrayList<Object>();
		if (rut != null) {
			sql.appendLine(" and be.rut_receptor = ? ");
			params.add(rut);
		}

		switch (bandeja) {
		case FAVORITOS:
			sql.appendLine(" and be.favorito = 1 ");
			break;
		case PAPELERA:
			sql.appendLine(" and not be.fecha_papelera is null ");
			break;
		default:
			sql.appendLine(" and be.fecha_papelera is null ");
			break;
		}

		sql.appendLine(" and be.fecha_eliminacion is null ");

		if (query != null) {
			sql.appendLine(" and (be.titulo like (?)					");
			sql.appendLine(" 		or r.nombres like (?)				");
			sql.appendLine(" 		or r.ape_paterno like (?) 			");
			sql.appendLine("		or r.ape_materno like (?))			");

			query = "%" + Formatear.getInstance().tTrim(query) + "%";
			params.add(query);
			params.add(query);
			params.add(query);
			params.add(query);
		}

		sql.appendLine(" group by  r.rut_remitente,  r.nombres,	 r.ape_paterno, r.ape_materno, r.foto_original,	 r.foto_pequena, r.e_mail ");

		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params.toArray());

		if (encriptar) {
			ConsultaDataEncrypter.getInstance().encrypt(data, new String[] { "rut_remitente" });
		}

		return data;
	}

	public ConsultaData getSinLeer(Integer rut) throws SQLException {
		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select 						");
		sql.appendLine(" 	tipo='bandeja_entrada',		");
		sql.appendLine(" 	q=count(*)					");
		sql.appendLine(" from eje_ges_bandeja_correo_entrada be ");
		sql.appendLine(" where 1=1 ");
		sql.appendLine(" and be.rut_receptor = @rut_receptor ");
		sql.appendLine(" and be.fecha_recepcion is null "); // que no ha recibido

		// sql.appendLine(" and isnull(be.favorito,0) = 0 "); -- da lo mismo fav
		sql.appendLine(" and be.fecha_papelera is null "); // que no esta en papelera

		sql.appendLine(" and be.fecha_eliminacion is null "); // ni eliminado
		sql.appendLine(" union ");
		sql.appendLine(" select 						");
		sql.appendLine(" 	tipo='favoritos',		");
		sql.appendLine(" 	q=count(*)					");
		sql.appendLine(" from eje_ges_bandeja_correo_entrada be ");
		sql.appendLine(" where 1=1 ");
		sql.appendLine(" and be.rut_receptor = @rut_receptor ");
		sql.appendLine(" and be.fecha_recepcion is null ");

		sql.appendLine(" and be.favorito = 1 ");
		sql.appendLine(" and be.fecha_papelera is null ");
		sql.appendLine(" and be.fecha_eliminacion is null ");

		String sqlFinal = sql.toString().replaceAll("@rut_receptor", String.valueOf(rut));
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlFinal);
		return data;
	}

	public boolean markUnMarkFavorito(int idCorreo) throws SQLException {
		String sql = "update eje_ges_bandeja_correo_entrada set favorito = case when isnull(favorito,0) = 0 then 1 else 0 end where id_correo = ? ";

		Object[] params = { idCorreo };
		return ConsultaTool.getInstance().update("portal", sql, params) > 0;
	}

	public ConsultaData getArchivosArjuntos(int idCorreo) throws SQLException {
		String sql = "select id_adjunto,id_correo,id_file from eje_ges_bandeja_archivo_adjuntos where id_correo = ? ";
		Object[] params = { idCorreo };
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);

		ConsultaDataEncrypter.getInstance().encrypt(data, new String[] { "id_adjunto", "id_correo", "id_file" });

		return data;
	}

	public ConsultaData getCorreo(int idCorreo) throws SQLException {
		return getCorreos(null, idCorreo, null, null, null, true, true, null);
	}

	public boolean markReaded(int rut, int idCorreo) throws SQLException {
		String sql = " update eje_ges_bandeja_correo_entrada set fecha_recepcion = getdate() where id_correo = ? and rut_receptor = ? ";
		Object[] params = { idCorreo, rut };
		return ConsultaTool.getInstance().update("portal", sql, params) > 0;
	}

	public boolean markPapelera(int idCorreo) throws SQLException {
		SqlBuilder sql = new SqlBuilder();

		sql.appendLine(" if(exists(select top 1 1 from eje_ges_bandeja_correo_entrada where fecha_papelera is null and id_correo = @id_correo )) ");
		sql.appendLine("  begin ");
		sql.appendLine(" 		update eje_ges_bandeja_correo_entrada ");
		sql.appendLine(" 			set ");
		sql.appendLine(" 	  	fecha_papelera = getdate() ");
		sql.appendLine(" 		where  id_correo = @id_correo ");
		sql.appendLine("  end ");
		sql.appendLine(" else");
		sql.appendLine("  begin ");
		sql.appendLine(" 		update eje_ges_bandeja_correo_entrada ");
		sql.appendLine(" 			set ");
		sql.appendLine(" 	  	fecha_eliminacion = getdate() ");
		sql.appendLine(" 		where  id_correo = @id_correo ");
		sql.appendLine("  end ");

		String sqlFinal = sql.toString().replaceAll("@id_correo", String.valueOf(idCorreo));

		return ConsultaTool.getInstance().update("portal", sqlFinal) > 0;
	}

	public boolean setBandeja(List<String> lista, EnumGestorCorreosBandejas bandeja) throws SQLException {

		ArrayFactory af = new ArrayFactory();
		af.addAll(lista);

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" update eje_ges_bandeja_correo_entrada ");

		switch (bandeja) {
		case ENTRADA:
			sql.appendLine(" set fecha_papelera = null, fecha_eliminacion = null ");
			break;
		case FAVORITOS:
			sql.appendLine(" set favorito = 1, fecha_papelera = null, fecha_eliminacion = null ");
			break;
		case PAPELERA:
			sql.appendLine(" set fecha_papelera = getdate() ");
			break;
		case ELIMINADOS:
			sql.appendLine(" set fecha_eliminacion = getdate() ");
			break;
		default:
			break;
		}

		sql.appendLine(" where id_correo in ");
		sql.appendLine(af.getArrayInteger());

		return ConsultaTool.getInstance().update("portal", sql) > 0;
	}

	public boolean markReaded(List<String> lista, boolean readed) throws SQLException {
		ArrayFactory af = new ArrayFactory();
		af.addAll(lista);

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" update eje_ges_bandeja_correo_entrada ");

		if (readed) {
			sql.appendLine(" set fecha_recepcion = getdate() ");
		} else {
			sql.appendLine(" set fecha_recepcion = null ");
		}

		sql.appendLine(" where id_correo in ");
		sql.appendLine(af.getArrayInteger());

		return ConsultaTool.getInstance().update("portal", sql) > 0;
	}

	public ConsultaData getCountSinLeer(int rut) throws SQLException {
		ConsultaData data = getSinLeer(rut);

		ConsultaData dataRetorno = ConsultaTool.getInstance().newConsultaData(new String[] { "q" });

		int q = 0;
		if (data != null) {
			data.setMode(ConsultaDataMode.CONVERSION);

			while (data.next()) {
				q += data.getInt("q");
			}
		}

		DataFields df = new DataFields();
		df.put("q", q);

		dataRetorno.add(df);

		return dataRetorno;

	}

}
