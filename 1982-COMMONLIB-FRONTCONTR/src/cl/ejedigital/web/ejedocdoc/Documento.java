package cl.ejedigital.web.ejedocdoc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_files;
import cl.eje.model.generic.portal.Eje_files_unico;
import cl.ejedigital.consultor.ISenchaPage;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.AbsVoPersiste.EnumReferenceMethod;
import portal.com.eje.portal.vo.util.Wheres;

public class Documento {
	private final String JNDI = "portal";
	private final String ID_DOC = "id_doc";
	
	public static Documento getIntance() {
		return Weak.getInstance(Documento.class);
	}

	public Collection<Eje_files_unico> getFiles(TransactionConnection cons, int id_doc, ISenchaPage page) throws NullPointerException, SQLException {
		Connection portal = cons.getPortal();
		
		List<Class> classToLoad = new ArrayList<>();
		classToLoad.add(Eje_doc_files.class);
		classToLoad.add(Eje_files_unico.class);
		
		Collection<Eje_doc_doc> vos = CtrTGeneric.getInstance().getAllFromClass(portal, Eje_doc_doc.class, Wheres.where(ID_DOC, Wheres.EQUALS, id_doc).build(), classToLoad, EnumReferenceMethod.EAGER);
		
		List<Eje_files_unico> files = new ArrayList<>();
		for(Eje_doc_doc d : vos) {
			if(d != null && d.getEje_doc_files() != null) { 
				for(Eje_doc_files f : d.getEje_doc_files() ) {
					if(f.getEje_files_unico() != null) {
						files.addAll(f.getEje_files_unico());
					}
				}
			}
		}

		return files;
	}
	
	public Eje_doc_doc getDoc(TransactionConnection cons, Integer id_doc) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);
		
		Eje_doc_doc doc = CtrTGeneric.getInstance().getFromClass(conn, Eje_doc_doc.class, Wheres.where(ID_DOC, Wheres.EQUALS, id_doc).build());
		return doc;
	}
}
