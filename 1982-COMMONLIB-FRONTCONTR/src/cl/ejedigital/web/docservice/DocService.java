package cl.ejedigital.web.docservice;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_files;
import cl.eje.model.generic.portal.Eje_doc_tag;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import cl.ejedigital.web.ejedocdoc.Documento;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.serhumano.user.Usuario;

/**
 * Al igual que FileService crea referencia a archivos subido a peoplemanager, esta clase crea referencias a documentos generados.<br/>
 * Ahora se podrá subir páginas html, o documentos extendos y referencias dicho documento, manual o página un numero único
 * 
 * @author Pancho
 * @since 25-06-2019
 * */
public class DocService implements IDocService {

	private final String JNDI = "portal";
	private final Doc doc = Doc.getIntance();
	private final Archivo archivo = Archivo.getIntance();
	private final DocListenersTool listeners = DocListenersTool.getIntance();
	
	public static IDocService getIntance() {
		return Weak.getInstance(DocService.class);
	}

	@Override
	public Eje_doc_doc addDoc(TransactionConnection cons, Usuario u, String contenidoDelDoc, boolean isPublico) throws NullPointerException, SQLException {
		if (contenidoDelDoc == null) {
			contenidoDelDoc = "";
		}

		Eje_doc_doc docVo = doc.addDoc(cons, u, contenidoDelDoc, isPublico);

		return docVo;
	}

	@Override
	public Eje_doc_doc addDoc(TransactionConnection cons, Usuario u, File f) throws NullPointerException, SQLException {
		Eje_doc_doc docVo = doc.addDoc(cons, u, "", false);
		archivo.addFile(cons, u, docVo, f);

		return docVo;
	}

	@Override
	public Eje_doc_files addFile(TransactionConnection cons, Usuario u, Eje_doc_doc docVo, File file) throws NullPointerException, SQLException {

		Eje_doc_files retorno = null;

		if (docVo != null) {
			docVo = Documento.getIntance().getDoc(cons, docVo.getId_doc());
			if (docVo != null) {
				retorno = archivo.addFile(cons, u, docVo, file);
			}

		}

		return retorno;
	}

	@Override
	public Eje_doc_doc updDoc(TransactionConnection cons, Usuario usuario, Eje_doc_doc docToUpdate, String contenido) throws NullPointerException, SQLException, EjeDocNotExistException {

		Eje_doc_doc docVo = doc.updDoc(cons, usuario, docToUpdate, contenido);
		return docVo;

	}

	@Override
	public Eje_doc_doc updDoc(TransactionConnection cons, Usuario usuario, Eje_doc_doc docToUpdate) throws NullPointerException, SQLException, EjeDocNotExistException {

		Eje_doc_doc docVo = doc.updDoc(cons, usuario, docToUpdate, null);
		return docVo;

	}
	
//	public String getTexto(TransactionConnection cons, Eje_doc_doc docToGet) throws NullPointerException, SQLException {
//		return TextoService.getIntance().getTexto(cons, docToGet);
//	}
	
	public static ITextoService getTexto() {
		return TextoService.getIntance();
	}
	
	public static IDocIntegrity getIntegrity() {
		return DocIntegrity.getIntance();
	}
	
	@Override
	public void addListener(IDocListener docListener) {
		listeners.addListener(docListener);
	}
	
	@Override
	public void addListener(IPageListener pageListener) {
		listeners.addListener(pageListener);
	}

	@Override
	public boolean addTag(TransactionConnection cons, Usuario usuario, Eje_doc_doc docVo, String tag) throws NullPointerException, SQLException {
		return Tag.getIntance().addTag(cons, docVo, tag);
	}
	
	@Override
	public Collection<TagSelected> getTags(TransactionConnection cons, Eje_doc_doc doc, String query , ISenchaPage page) throws SQLException {
		return Tag.getIntance().getTags(cons, doc, query, page);
	}
}
