package cl.eje.generico.ws;

public class ConectorProxy implements cl.eje.generico.ws.Conector {
  private String _endpoint = null;
  private cl.eje.generico.ws.Conector conector = null;
  
  public ConectorProxy(String urlSW, boolean use) {
	    _initConectorProxy(urlSW);
  }
  
  public ConectorProxy() {
    _initConectorProxy();
  }
  
  public ConectorProxy(String endpoint) {
    _endpoint = endpoint;
    _initConectorProxy();
  }
  
  private void _initConectorProxy(String urlSW) {
	    try {
	      conector = (new cl.eje.generico.ws.ConectorServiceLocator()).getConector(urlSW);
	      if (conector != null) {
	        if (_endpoint != null)
	          ((javax.xml.rpc.Stub)conector)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	        else
	          _endpoint = (String)((javax.xml.rpc.Stub)conector)._getProperty("javax.xml.rpc.service.endpoint.address");
	      }
	      
	    }
	    catch (javax.xml.rpc.ServiceException serviceException) {}
	  }
  
  private void _initConectorProxy() {
    try {
      conector = (new cl.eje.generico.ws.ConectorServiceLocator()).getConector();
      if (conector != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)conector)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)conector)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (conector != null)
      ((javax.xml.rpc.Stub)conector)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cl.eje.generico.ws.Conector getConector() {
    if (conector == null)
      _initConectorProxy();
    return conector;
  }
  
  public java.lang.String update(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException{
    if (conector == null)
      _initConectorProxy();
    return conector.update(token, query);
  }
  
  public java.lang.String delete(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException{
    if (conector == null)
      _initConectorProxy();
    return conector.delete(token, query);
  }
  
  public java.lang.String insert(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException{
    if (conector == null)
      _initConectorProxy();
    return conector.insert(token, query);
  }
  
  public java.lang.String getData(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException{
    if (conector == null)
      _initConectorProxy();
    return conector.getData(token, query);
  }
  
  
}