package portal.com.eje.frontcontroller;

import java.util.HashMap;
import java.util.Map;

import portal.com.eje.frontcontroller.iface.ITransactionSimulator;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.transactions.ITransaction;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.transactions.TransactionTool;
import portal.com.eje.portal.vo.ClaseConversor;

/**
 * Simula una transación, para ello imprime en Texto las salidas al front y no en el ServletResponse, también simula otros contextos.
 * 
 * @author Pancho
 * @since 05-03-2019
 * */
public class IOSimulator {
	private IOClaseWeb io;
	private Map<String, String> gParams;
	private TransactionConnection cons;
	private EModulos moduloTransaction;
	private IOClaseWebSimulator ioInterno;
	private Object retorno;
	private Throwable lastError;
	
	public IOSimulator(IOClaseWeb io, Map<String, String> gParams) {
		this(io, gParams, io.getModulo());
		
		if(io == null || gParams == null ) {
			throw new NullPointerException();
		}
		
		this.cons = io.getTransactionConnection();
		this.moduloTransaction = io.getModulo();
	}
	
	public IOSimulator(IOClaseWeb io, Map<String, String> gParams, EModulos moduloTransaction) {
		super();
		
		if(io == null || gParams == null || moduloTransaction == null) {
			throw new NullPointerException();
		}
		
		this.io = io;
		this.gParams = gParams;
		
		this.cons = io.getTransactionConnection();
		this.moduloTransaction = moduloTransaction;
	}
	
	public IOSimulator(IOClaseWeb io, EModulos moduloTransaction) {
		this(io, new HashMap<String,String>(), moduloTransaction);
	}
	
	public IOSimulator(TransactionConnection cons, EModulos moduloTransaction) {
		if(cons == null || moduloTransaction == null) {
			throw new NullPointerException();
		}
		
		this.cons = cons;
		this.moduloTransaction = moduloTransaction;
	}
	
	public IOSimulator(EModulos moduloTransaction) {
		if(moduloTransaction == null) {
			throw new NullPointerException();
		}
		
		this.moduloTransaction = moduloTransaction;
	}

	/**
	 * Obtiene un ioSimulator <br/>
	 * Agrega los parámetros adicionales <br/>
	 * Utiliza la transacción existente o genera un nueva <br/>
	 * 
	 * @author Pancho
	 * @throws Throwable 
	 * @since 07-03-2019
	 * */
	public IOClaseWebResponse simuleTransaction(final ITransactionSimulator trasactionSimulator) throws RuntimeException  {

		IOClaseWebResponse response = null;
		
		try {
			if(this.cons != null) {
				response = simulaConTransaccion(this.cons, trasactionSimulator);
			}
			else {
				response = simulaNewTransaccion(cons, trasactionSimulator);
			}
		
		} catch (Exception e) {
			//e.printStackTrace();
		} catch (Throwable e) {
			//e.printStackTrace();
		}
		
		return response;
	}
	
	public IOClaseWebResponse simuleTransactionE(final ITransactionSimulator trasactionSimulator) throws Throwable  {

		IOClaseWebResponse response = null;
			 
		if(this.cons != null) {
			response = simulaConTransaccion(this.cons, trasactionSimulator);
		}
		else {
			response = simulaNewTransaccion(cons, trasactionSimulator);
		}

		return response;
	}
	
	private IOClaseWebResponse simulaNewTransaccion(TransactionConnection cons, final ITransactionSimulator trasactionSimulator) throws Throwable {
		IOClaseWebResponse response = null;
		
		TransactionTool tool = new TransactionTool(false);
		tool.doTransaction(new ITransaction() {
			
			@Override
			public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Throwable {
				
				tool.returnData("response", simulaConTransaccion(cons, trasactionSimulator) );
				
				return true;
			}
		});
		
		lastError = tool.getException();
		if(lastError != null) {
			throw lastError;
		}
		
		
		response = (IOClaseWebResponse) tool.getData("reponse");
		
		return response;
	}
	
	private IOClaseWebResponse simulaConTransaccion(TransactionConnection cons, final ITransactionSimulator trasactionSimulator) throws Throwable {
		EModulos moduloOriginal = cons.getContext();
		
		try {
			 
				if(io == null) {
					io = new IOClaseWeb(cons, null, null, null);
				}
				
				ioInterno = io.getSimulator(this.moduloTransaction);						
				ioInterno.addExtraParam(gParams);
				
				retorno = trasactionSimulator.simuleTransaction(ioInterno);
						
				 

		} catch (Exception e) {
			e.printStackTrace();
			lastError = e;
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			lastError = e;
			throw e;
		} finally {
			ioInterno.getTransactionConnection().setContext(moduloOriginal);
		}
		
		return getResponse();
	}

	/**
	 * Siempre retorna un objeto, nunca null
	 * */
	public IOClaseWebResponse getResponse() {
		return this.ioInterno != null && this.ioInterno.getResponse() != null ? this.ioInterno.getResponse() : new IOClaseWebResponse();
	}
	
	public Object getObject() {
		return this.retorno;
	}
	
	public <T> T getObject(Class<T> convert) {
		return (T) this.retorno;
	}
	
	public Throwable getLastError() {
		return this.lastError;
	}
	
	public boolean isOk() {
		return this.lastError == null;
	}
	
}
