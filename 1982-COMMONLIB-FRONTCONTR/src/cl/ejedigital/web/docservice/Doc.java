package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_responsable;
import cl.eje.model.generic.portal.Eje_doc_version;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import cl.ejedigital.web.ejedocdoc.Documento;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.serhumano.user.Usuario;

class Doc {
	private final String JNDI = "portal";
	private final Version version = Version.getIntance();
	private final Pagina pagina = Pagina.getIntance();
	private final Responsable responsable = Responsable.getIntance();
	
	public static Doc getIntance() {
		return Weak.getInstance(Doc.class);
	}

	public Eje_doc_doc addDoc(TransactionConnection cons, Usuario u, String contenidoDelDoc, boolean isPublico) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);

		Eje_doc_responsable r = responsable.getResponsable(cons, u);
		
		Eje_doc_doc doc = new Eje_doc_doc();
		doc.setDoc_nombre(getNombreFromText(contenidoDelDoc));
		doc.setFecha_creo(ConsultaTool.getInstance().getNow(conn));
		doc.setRut_creo(r.getRut());
		doc.setModulo_creo(EModulos.getThisModulo().getContexo());
		doc.setId_estado(EnumDocEstado.PUBLICADA.getIdEstado());
		doc.setIs_publico(isPublico);
		
		DocListenersTool.getIntance().onPrepareSave(doc, u, contenidoDelDoc);

		CtrTGeneric.getInstance().add(conn, doc);
		Eje_doc_version versionVo = version.addVersion(cons, r, doc, contenidoDelDoc);
		
		DocListenersTool.getIntance().onSave(doc, u, contenidoDelDoc);
		
		return doc;
	}
	
	public Eje_doc_doc updDoc(TransactionConnection cons, Usuario u, Eje_doc_doc docToUpdate, String contenidoDelDoc) throws NullPointerException, SQLException, EjeDocNotExistException {
		Connection conn = cons.getConnection(JNDI);

		
		Eje_doc_doc doc = Documento.getIntance().getDoc(cons, docToUpdate.getId_doc());
		Eje_doc_responsable r = responsable.getResponsable(cons, u);
		
		doc.setId_estado(docToUpdate.getId_estado());
		doc.setIs_publico(docToUpdate.isIs_publico());

 
		String actualDocText = DocService.getTexto().getTexto(cons, doc);
		DocListenersTool.getIntance().onPrepareUpd(doc, u, actualDocText, contenidoDelDoc);
 
		CtrTGeneric.getInstance().upd(conn, doc);
	 
		Eje_doc_version versionVo = version.addVersion(cons, r, doc, contenidoDelDoc);	
		 

		DocListenersTool.getIntance().onSave(doc, u, contenidoDelDoc);
		
		return doc;
	}
	
	String getNombreFromText(String txt) {
		String nombre = "";
		if (txt != null) {
			if (txt.length() >= 100) {
				nombre = txt.substring(0, 100);
			} else {
				nombre = txt.substring(0, txt.length());
			}
		}

		return MyString.getInstance().quitaCEspeciales(nombre);
	}
}
