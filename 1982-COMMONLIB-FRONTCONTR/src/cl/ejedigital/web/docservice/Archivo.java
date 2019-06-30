package cl.ejedigital.web.docservice;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_files;
import cl.ejedigital.web.fileupload.FileService;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.serhumano.user.Usuario;

class Archivo {
	private final String JNDI = "portal";
	private final FileService fileService = new FileService();
	
	public static Archivo getIntance() {
		return Weak.getInstance(Archivo.class);
	}

	public Eje_doc_files addFile(TransactionConnection cons, Usuario u, Eje_doc_doc docVo, File file) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);

		int id_file = fileService.addFile(u.getRutIdInt(), file, "Eje_doc_file");

		Eje_doc_files fileVo = new Eje_doc_files();
		fileVo.setId_doc(docVo.getId_doc());
		fileVo.setId_file(id_file);

		CtrTGeneric.getInstance().add(conn, fileVo);
		return fileVo;
	}

}
