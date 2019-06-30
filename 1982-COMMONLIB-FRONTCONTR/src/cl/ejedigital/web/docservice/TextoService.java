package cl.ejedigital.web.docservice;

import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_version;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import cl.ejedigital.web.ejedocdoc.Documento;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;

class TextoService implements ITextoService {

	public static ITextoService getIntance() {
		return Weak.getInstance(TextoService.class);
	}
	
	/* (non-Javadoc)
	 * @see cl.ejedigital.web.docservice.ITextoService#getTexto(portal.com.eje.portal.transactions.TransactionConnection, cl.eje.model.generic.portal.Eje_doc_doc)
	 */
	@Override
	public String getTexto(TransactionConnection cons, Eje_doc_doc docToGet) throws NullPointerException, SQLException, EjeDocNotExistException {
		if(cons == null || docToGet == null) {
			throw new EjeDocNotExistException("No existe porque los parámetros son nullos ");
		}
		
		return getTexto(cons, docToGet.getId_doc());
	}
	
	/* (non-Javadoc)
	 * @see cl.ejedigital.web.docservice.ITextoService#getTexto(portal.com.eje.portal.transactions.TransactionConnection, java.lang.Integer)
	 */
	@Override
	public String getTexto(TransactionConnection cons,Integer id_doc) throws NullPointerException, SQLException, EjeDocNotExistException {
		
		Eje_doc_doc doc = Documento.getIntance().getDoc(cons, id_doc);
		if(doc == null) {
			throw new EjeDocNotExistException("doc no existe "+id_doc);
		}
		
		Eje_doc_version version = Version.getIntance().getLastVersion(cons, doc);
		return Pagina.getIntance().getTexto(cons, version);
	}
	
	 
}
