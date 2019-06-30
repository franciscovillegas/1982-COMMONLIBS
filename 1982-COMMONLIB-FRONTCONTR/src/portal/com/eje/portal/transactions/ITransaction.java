package portal.com.eje.portal.transactions;

import java.util.Map;

public interface ITransaction {

	public boolean transaction(TransactionConnection cons, Map<String,Object> paramsVars, TransactionTool tool) throws Throwable;
}
