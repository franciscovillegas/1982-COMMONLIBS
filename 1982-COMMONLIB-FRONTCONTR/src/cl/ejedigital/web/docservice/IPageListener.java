package cl.ejedigital.web.docservice;

public interface IPageListener {

	/**
	 * Cuando se Prepara el doc para ser guardado
	 * */
	boolean onReadPage(int pageNumber, String textoPagina);
	
}
