package cl.ejedigital.web.docservice;

import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.ejedigital.web.docservice.IDocIntegrity;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import cl.ejedigital.web.ejedocdoc.Documento;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;

class DocIntegrity implements IDocIntegrity {

	public static DocIntegrity getIntance() {
		return Weak.getInstance(DocIntegrity.class);
	}

	public boolean existDoc(TransactionConnection cons, Integer id_doc) throws NullPointerException, SQLException, EjeDocNotExistException {
		Eje_doc_doc doc = Documento.getIntance().getDoc(cons, id_doc);
		if (doc == null) {
			throw new EjeDocNotExistException("No existe el documento :" + id_doc);
		}
		return true;
	}

 
}
