package cl.ejedigital.web.docservice;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_files;
import cl.eje.model.generic.portal.Eje_doc_tag;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.serhumano.user.Usuario;

public interface IDocService {

	Eje_doc_doc addDoc(TransactionConnection cons, Usuario u, String contenidoDelDoc, boolean isPublico) throws NullPointerException, SQLException;

	Eje_doc_doc addDoc(TransactionConnection cons, Usuario u, File f) throws NullPointerException, SQLException;

	Eje_doc_files addFile(TransactionConnection cons, Usuario u, Eje_doc_doc docVo, File file) throws NullPointerException, SQLException;

	Eje_doc_doc updDoc(TransactionConnection cons, Usuario usuario, Eje_doc_doc docToUpdate, String contenido) throws NullPointerException, SQLException, EjeDocNotExistException;

	Eje_doc_doc updDoc(TransactionConnection cons, Usuario usuario, Eje_doc_doc docToUpdate) throws NullPointerException, SQLException, EjeDocNotExistException;

	void addListener(IDocListener docListener);

	void addListener(IPageListener pageListener);

	/**
	 * Será true si se creó uno nuevo
	 * @throws SQLException 
	 * @throws NullPointerException 
	 * */
	boolean addTag(TransactionConnection cons, Usuario usuario, Eje_doc_doc docVo, String tag) throws NullPointerException, SQLException;
 

	Collection<TagSelected> getTags(TransactionConnection cons, Eje_doc_doc doc, String query, ISenchaPage page) throws SQLException;

}