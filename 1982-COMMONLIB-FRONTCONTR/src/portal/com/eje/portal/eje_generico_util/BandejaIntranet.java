package portal.com.eje.portal.eje_generico_util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.eje_generico_util.vo.VoBandejaIntranet;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoEncrypter;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.security.Encrypter;

public class BandejaIntranet {

	public static BandejaIntranet getInstance() {
		return Util.getInstance(BandejaIntranet.class);
	}

	/**
	 * Enntrega la bandeja de un usuario
	 * @author Pancho
	 * @throws SQLException 
	 * @since 20-09-2018
	 * */
	public Collection<VoBandejaIntranet> getMiBandeja(Usuario usuario) throws SQLException {
		return getMiBandeja(usuario, null);
	}
	 
	/**
	 * Enntrega la bandeja de un usuario
	 * @author Pancho
	 * @throws SQLException 
	 * @since 20-09-2018
	 * */
	public Collection<VoBandejaIntranet> getMiBandeja(Usuario usuario, String idMensajeEncripted) throws SQLException {
		Assert.notNull(usuario);
		
		Encrypter enc = new Encrypter();
		
		int idMensaje =	(idMensajeEncripted != null ? Validar.getInstance().validarInt(enc.decrypt(idMensajeEncripted), -1) : -1);
		
		Collection<VoBandejaIntranet> vos = null;
		 
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT \n");
		sql.append(" 	id_mensaje,mensaje,rut_receptor,nombre_receptor,rut_remitente,nombre_remitente, \n");
		sql.append(" 	titulo,fecha_envio,fecha_recepcion_primeravez,fecha_nomostrrar_bydestinatario, \n");
		sql.append(" 	url,mensaje_full,es_url,es_mensaje, data_adicional \n");
		sql.append(" FROM eje_ges_bandeja_entrada \n");
		sql.append(" WHERE isnull(fecha_nomostrrar_bydestinatario, dateadd(minute, 5, getdate())) >= getdate() \n");
		sql.append(" 		and rut_receptor = ? \n");
		 
		List<Object> params = new ArrayList<Object>();
		params.add(usuario.getRutIdInt());
		
		if(idMensaje > 0) {
			sql.append(" 		and id_mensaje = ? \n");
			
			params.add(idMensaje);
		}
		
		sql.append(" ORDER BY fecha_envio DESC \n");

		
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), params.toArray());

		vos = VoTool.getInstance().buildVo(data, VoBandejaIntranet.class);

		List<String> fieldsToEncrypt = new ArrayList<String>();
		fieldsToEncrypt.add("id_mensaje");

		vos= VoEncrypter.getInstance().encrypt(vos, VoBandejaIntranet.class, fieldsToEncrypt);

		return vos;
	}
	
	/**
	 * Marca como visto un mensaje
	 * @author Pancho
	 * @throws SQLException 
	 * @since 20-09-2018
	 * */
	public boolean markViewed(Usuario usuario, String idMensajeEncripted) throws SQLException {
		Assert.notNull(usuario);
		Assert.notNull(idMensajeEncripted);
		
		Encrypter encrypter = new Encrypter();
		int idMensaje = Validar.getInstance().validarInt(encrypter.decrypt(idMensajeEncripted),-1);
		boolean ok = false;
		
		if(idMensaje > 0) {
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE eje_ges_bandeja_entrada \n");
			sql.append(" set fecha_recepcion_primeravez = getdate() \n");
			sql.append(" WHERE id_mensaje = ? and rut_receptor = ? \n");
			
			Object[] params = {idMensaje, usuario.getRutIdInt()};
			
			ok = ConsultaTool.getInstance().update("portal", sql.toString(), params) > 0;
		}
		
		return ok;
	}

	public boolean markDeleted(Usuario usuario, String idMensajeEncripted) throws SQLException {
		Assert.notNull(usuario);
		Assert.notNull(idMensajeEncripted);
		
		Encrypter encrypter = new Encrypter();
		int idMensaje = Validar.getInstance().validarInt(encrypter.decrypt(idMensajeEncripted),-1);
		boolean ok = false;
		
		if(idMensaje > 0) {
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE eje_ges_bandeja_entrada \n");
			sql.append(" set fecha_nomostrrar_bydestinatario = getdate() \n");
			sql.append(" WHERE id_mensaje = ? and rut_receptor = ? \n");
			
			Object[] params = {idMensaje, usuario.getRutIdInt()};
			
			ok = ConsultaTool.getInstance().update("portal", sql.toString(), params) > 0;
		}
		
		return ok;
	}
}
