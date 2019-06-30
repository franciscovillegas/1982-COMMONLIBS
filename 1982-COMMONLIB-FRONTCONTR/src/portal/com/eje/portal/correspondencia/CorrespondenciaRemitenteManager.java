package portal.com.eje.portal.correspondencia;

import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

import org.springframework.util.Assert;

import cl.eje.model.generic.portal.Eje_correspondencia_remitente;
import cl.eje.model.generic.portal.Eje_generico_modulo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Wheres;

public class CorrespondenciaRemitenteManager {

	public static CorrespondenciaRemitenteManager getInstance() {
		return Mng.getInstance(CorrespondenciaRemitenteManager.class);
	}
	
	public boolean existe(EModulos modulos) throws SQLException {
		Assert.notNull(modulos);
		
		String sql = "select 1 from eje_correspondencia_remitente where id_modulo = ? ";
		Object[] params = {modulos.getId()};
		
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
		boolean existe = data != null && data.next();
		
		return existe;
	}
	
	public boolean createRemitente(EModulos modulos) throws NullPointerException, SQLException {
		Assert.notNull(modulos);
		
		Eje_generico_modulo mod = modulos.getGenericoModulo();
		
		Eje_correspondencia_remitente remitente = new Eje_correspondencia_remitente();
		remitente.setNombre(mod.getNombre());
		remitente.setContexto(modulos.getContexo());
		remitente.setId_modulo(modulos.getId());
		remitente.setMail(getDesde());
		
		CtrGeneric.getInstance().add(remitente);
		
		return true;
	}
	
	public Eje_correspondencia_remitente getRemitente(EModulos modulos) throws NullPointerException, SQLException {
		
		Collection<Eje_correspondencia_remitente> vos = CtrGeneric.getInstance().getAllFromClass(Eje_correspondencia_remitente.class, Wheres.where("id_modulo", "=", modulos.getId()).build());
		
		Eje_correspondencia_remitente obj = null;
		if(vos != null && vos.size() > 0) {
			obj = vos.iterator().next();
		}
		
		return obj;
	}
	
	private String getDesde() {
		try {
			ResourceBundle proper = ResourceBundle.getBundle("mail");
			return proper.getString("portal.maildesde");
		}
		catch(Exception e) {
			
		}
		
		return null;
	}
}
