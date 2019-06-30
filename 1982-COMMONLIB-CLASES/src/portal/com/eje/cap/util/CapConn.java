package portal.com.eje.cap.util;

import java.sql.Connection;

import cl.eje.jndi.cap.CapJndi;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.transactions.TransactionConnection;

public class CapConn {


	public static CapConn getInstance() {
		return Util.getInstance(CapConn.class);
	}
	
	public static Connection getCapConn(TransactionConnection cons) {
		return cons.getConnection(CapJndi.jndiCap);
	}
 
}
