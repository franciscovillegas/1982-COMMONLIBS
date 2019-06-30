package cl.ejedigital.web.docservice;

import java.sql.SQLException;

import cl.ejedigital.web.docservice.error.EjeDocNotExistException;
import portal.com.eje.portal.transactions.TransactionConnection;

public interface IDocIntegrity {

	public boolean existDoc(TransactionConnection cons, Integer id_doc) throws NullPointerException, SQLException, EjeDocNotExistException;
}