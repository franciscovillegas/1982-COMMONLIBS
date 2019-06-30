package portal.com.eje.portal.transactions;

import java.util.HashMap;
import java.util.Map;

 

public class TransactionTool {
	private Map<String,Object> params;
	private TransactionConnection conns;
	private Exception e;
	private boolean ok;
	private Map<String,Object> dataRetorno;
	
	public TransactionTool(boolean restringeCommint){
		
		restringeCommint = false;
		
		this.params = new HashMap<String,Object>();
		this.conns = new TransactionConnection(restringeCommint);
		this.dataRetorno = new HashMap<String, Object>();
	}
	
	public void returnData(String s, Object o) {
		this.dataRetorno.put(s, o);
	}
	
	public Object getData(String s) {
		return dataRetorno.get(s);
	}
	
	public Map<String,Object> putParam(String key, Object value) {
		 this.params.put(key, value);
		 return this.params;
	}
		
	public boolean doTransaction(ITransaction t) {
		this.e = null;
		
		ok = true;
		try {
			ok = t.transaction(conns, params, this);
		}
		catch(Exception e) {
			this.e = e;
			ok = false;
		} catch (Throwable e1) {
			this.e = e;
			ok = false;
		}
		finally { 
			conns.tryFreeConnections(ok);
		}
		
		return ok;
	}
	
	public Exception getException() {
		return e;
	}
	
	public boolean getOK() {
		return ok;
	}
	
	public boolean isOK() {
		return ok;
	}

	public void printExceptionIfExist() {
		if(this.e != null) {
			e.printStackTrace();
		}
		
	}
	 
}
