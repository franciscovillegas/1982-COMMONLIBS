package cl.ejedigital.web.docservice;

import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import portal.com.eje.portal.transactions.TransactionConnection;

public interface ITextoService {

	String getTexto(TransactionConnection cons, Eje_doc_doc docToGet) throws NullPointerException, SQLException, EjeDocNotExistException;

	String getTexto(TransactionConnection cons, Integer id_doc) throws NullPointerException, SQLException, EjeDocNotExistException;

}