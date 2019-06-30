package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_responsable;
import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.trabajador.TrabajadorInfoLocator;
import portal.com.eje.portal.trabajador.util.EjeGesTrabajadorFields;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.serhumano.user.Usuario;

class Responsable {
	private final String JNDI = "portal";
	private final String CAMPO_RUT = "rut";
	private final String CAMPO_NOMBRES = "nombres";
	private final String CAMPO_NOMBRE = "nombre";
	private final String CAMPO_APE_PATERNO = "ape_paterno";
	private final String CAMPO_APE_MATERNO = "ape_materno";

	public static Responsable getIntance() {
		return Weak.getInstance(Responsable.class);
	}

	public Eje_doc_responsable getResponsable(TransactionConnection cons, Usuario u) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);
		Eje_doc_responsable r = CtrTGeneric.getInstance().getFromClass(conn, Eje_doc_responsable.class, Wheres.where(CAMPO_RUT, Wheres.EQUALS, u.getRutIdInt()).build());

		if (r == null) {
			r = new Eje_doc_responsable();

			ConsultaData data = TrabajadorInfoLocator.getInstance().getData(u.getRutIdInt(), EjeGesTrabajadorFields.getNombres());
			if (data != null && data.next()) {
				r.setRut(u.getRutIdInt());
				r.setNombre(data.getString(CAMPO_NOMBRE));
				r.setNombres(data.getString(CAMPO_NOMBRES));
				r.setApe_paterno(data.getString(CAMPO_APE_PATERNO));
				r.setApe_materno(data.getString(CAMPO_APE_MATERNO));

			}

			CtrTGeneric.getInstance().add(conn, r);
 
		}

		return r;
	}

}
