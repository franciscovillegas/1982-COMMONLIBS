package cl.ejedigital.web.docservice.listeners;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.docservice.IDocListener;
import cl.ejedigital.web.docservice.IPageListener;
import cl.ejedigital.web.docservice.ITagListener;
import cl.ejedigital.web.docservice.error.DocNotContainUpdateException;
import cl.ejedigital.web.docservice.error.ExceedMaximunNumberPage;
import cl.ejedigital.web.docservice.error.TagNameException;
import portal.com.eje.serhumano.user.Usuario;

public class DefaultDocListener implements IDocListener, IPageListener, ITagListener {
	private final int MAX_PAGES_NUMBER = 50;
	
	@Override
	public boolean onPrepareSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc) {
		if(doc == null) {
			throw new NullPointerException("El doc no puede ser null");
		}
		if(contenidoDelDoc == null) {
			throw new NullPointerException("Se debe guardar algo, no puede ser null el contenido");
		}
		if(contenidoDelDoc.equals("")) {
			throw new NullPointerException("Se debe guardar algo, el contenido no puede ser vacío.");
		}
		return true;
	}

	@Override
	public boolean onSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc) {
		if(doc == null) {
			throw new NullPointerException("El doc no puede ser null");
		}
		 
		return true;
	}
	
	@Override
	public boolean onPrepareUpd(Eje_doc_doc doc, Usuario u, String contenidoDelDocAnterior, String nuevoContenido) {
		if(contenidoDelDocAnterior.equals(nuevoContenido)) {
			throw new DocNotContainUpdateException("El documento no contiene modificaciones ");
		}
		return false;
	}

	@Override
	public boolean onReadPage(int pageNumber, String textoPagina) {
		if(pageNumber > this.MAX_PAGES_NUMBER) {
			throw new ExceedMaximunNumberPage("Se ha excedido la cantidad máxima de páginas por documento : "+this.MAX_PAGES_NUMBER);
		}
		
		return true;
 
	}

	@Override
	public String onNormalizeTag(String beforeNormalize) {
		if(beforeNormalize == null) {
			throw new NullPointerException("No puede ser null el nombre del tag");
		}
		if(beforeNormalize.trim().equals("")) {
			throw new TagNameException("El nombre del tag debe más largo ");
		}
		
		return beforeNormalize.trim();
	}


}
