package cl.ejedigital.web.docservice;

import cl.eje.model.generic.portal.Eje_doc_doc;
import portal.com.eje.serhumano.user.Usuario;

public interface IDocListener {

	/**
	 * Cuando se Prepara el doc para ser guardado
	 * */
	boolean onPrepareSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc);
	
 
	boolean onPrepareUpd(Eje_doc_doc doc, Usuario u, String contenidoDelDocAnterior, String nuevoContenido);
	
	/**
	 * Cuando el doc ya está en la bd
	 * */
	boolean onSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc);
	
}
