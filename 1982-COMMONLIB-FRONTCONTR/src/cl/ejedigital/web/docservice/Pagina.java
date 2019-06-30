package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_pagina;
import cl.eje.model.generic.portal.Eje_doc_version;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.Sort;
import cl.ejedigital.web.docservice.error.ExceedMaximunNumberPage;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.tools.sortcollection.VoSort;

/**
 * @author Pancho
 * @since 26-06-2019
 * */
class Pagina {
	private final String JNDI = "portal";
	private final int MAX_PAGINA_SIZE = 8000;
	private final Palabra palabra = Palabra.getIntance();
	public final String CAMPO_PAG = "pag";
	
	
	public static Pagina getIntance() {
		return Weak.getInstance(Pagina.class);
	}

	public List<Eje_doc_pagina> addPaginas(TransactionConnection cons, Eje_doc_doc doc, Eje_doc_version version, String contenidoDelDoc) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);

		List<Eje_doc_pagina> paginas = new LinkedList<>();
		int pag = 0;
		for (String contenido : privateBuildPaginasFromContenido(cons, doc, version, contenidoDelDoc)) {
			Eje_doc_pagina pagina = new Eje_doc_pagina();
			pagina.setId_ver(version.getId_ver());
			pagina.setPag(pag++);
			pagina.setContenido(contenido);

			paginas.add(pagina);
		}
		
		
		for(Eje_doc_pagina pagina: paginas) {
			CtrTGeneric.getInstance().add(conn, pagina);
		}
		
		palabra.referenciaPalabras(cons, paginas);

		return paginas;
	}
	
	private List<String> privateBuildPaginasFromContenido(TransactionConnection cons, Eje_doc_doc doc, Eje_doc_version version, String contenidoDelDoc) {
		List<String> paginas = new LinkedList<>();
		
		int qPaginas = (contenidoDelDoc.length() / MAX_PAGINA_SIZE) + 1;
		for(int i=0 ; i<qPaginas;i++) {
			int start = (i * MAX_PAGINA_SIZE);
			int end = start + MAX_PAGINA_SIZE;
			
			String pagina = null;
			if(contenidoDelDoc.length() >= end) {
				pagina = contenidoDelDoc.substring(start, end);
			}
			else {
				pagina = contenidoDelDoc.substring(start, contenidoDelDoc.length());
			}
			
			paginas.add(pagina);
			
			DocListenersTool.getIntance().onReadPage(i+1, pagina);
		}
		
		
		
		return paginas;
	}

	public Collection<Eje_doc_pagina> getPaginas(TransactionConnection cons, Eje_doc_version version) throws NullPointerException, SQLException {
		return CtrTGeneric.getInstance().getAllFromClass(cons.getPortal(), 
				Eje_doc_pagina.class, 
				Wheres.where(Version.CAMPO_ID_VER, Wheres.EQUALS, version.getId_ver()).build(),
				Sort.sort(CAMPO_PAG, Order.Ascendente));
	}
	
	@SuppressWarnings("rawtypes")
	public String getTexto(TransactionConnection cons, Eje_doc_version version) throws NullPointerException, SQLException {
		StringBuilder str= new StringBuilder();
		
		Collection<Eje_doc_pagina> paginas = getPaginas(cons, version);
		if(paginas != null && !paginas.isEmpty()) {
			VoSort.getInstance().sortByMethodValue((List) paginas, CAMPO_PAG, Integer.class, Order.Ascendente);
			
			for(Eje_doc_pagina p : paginas) {
				str.append(p.getContenido());
			}
		}
		
		
		return str.toString();
	}
}
