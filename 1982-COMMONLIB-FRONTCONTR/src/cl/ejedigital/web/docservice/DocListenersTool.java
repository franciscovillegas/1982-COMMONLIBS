package cl.ejedigital.web.docservice;

import java.util.List;

import cl.eje.model.generic.portal.Eje_doc_doc;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;

class DocListenersTool implements IDocListener, IPageListener, ITagListener {
	private final String paquete = "cl.ejedigital.web.docservice.listeners";
	private List<IDocListener> docListeners = null;
	private List<IPageListener> pageListeners = null;
	private List<ITagListener> tagListeners = null;
	
	public static DocListenersTool getIntance() {
		return Weak.getInstance(DocListenersTool.class);
	}

	private DocListenersTool() {
		docListeners = PaqueteFactory.getInstance().getObjects(paquete, IDocListener.class);
		pageListeners = PaqueteFactory.getInstance().getObjects(paquete, IPageListener.class);
		tagListeners = PaqueteFactory.getInstance().getObjects(paquete, ITagListener.class);
	}
	
	public void addListener(IDocListener docListener) {
		if(docListener != null) {
			docListeners.add(docListener);	
		}
	}
 
	
	public void addListener(IPageListener pageListener) {
		if(pageListener != null) {
			pageListeners.add(pageListener);	
		}
	}

	@Override
	public boolean onPrepareSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc) {
		for(IDocListener listener : docListeners) {
			listener.onPrepareSave(doc, u, contenidoDelDoc);
		}
		
		return true;
	}
	
	@Override
	public boolean onPrepareUpd(Eje_doc_doc doc, Usuario u, String contenidoDelDocAnterior, String contenidoDelDocNuevo) {
		for(IDocListener listener : docListeners) {
			listener.onPrepareUpd(doc, u, contenidoDelDocAnterior, contenidoDelDocNuevo);
		}
		
		return true;
	}

	@Override
	public boolean onSave(Eje_doc_doc doc, Usuario u, String contenidoDelDoc) {
		for(IDocListener listener : docListeners) {
			listener.onPrepareSave(doc, u, contenidoDelDoc);
		}
		
		return true;
	}

	@Override
	public boolean onReadPage(int pageNumber, String textoPagina) {
		for(IPageListener listener : pageListeners) {
			listener.onReadPage(pageNumber, textoPagina);
		}
		
		return true;
	}

	@Override
	public String onNormalizeTag(String beforeNormalize) {
		for(ITagListener listener : tagListeners) {
			beforeNormalize = listener.onNormalizeTag(beforeNormalize);
		}
		
		return beforeNormalize;
	}
}
