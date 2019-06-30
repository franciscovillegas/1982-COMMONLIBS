package cl.eje.generico.consoleremote.ws;

public class ConectorSystemRemoteProxy implements cl.eje.generico.consoleremote.ws.ConectorSystemRemote {
  private String _endpoint = null;
  private cl.eje.generico.consoleremote.ws.ConectorSystemRemote conectorSystemRemote = null;
  
  public ConectorSystemRemoteProxy(String urlSW, Boolean use) {
    _initConectorSystemRemoteProxy(urlSW);
  }
  
  public ConectorSystemRemoteProxy() {
	    _initConectorSystemRemoteProxy();
  }
  
  public ConectorSystemRemoteProxy(String endpoint) {
    _endpoint = endpoint;
    _initConectorSystemRemoteProxy();
  }
  
  private void _initConectorSystemRemoteProxy() {
    try {
      conectorSystemRemote = (new cl.eje.generico.consoleremote.ws.ConectorSystemRemoteServiceLocator()).getConectorSystemRemote();
      if (conectorSystemRemote != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)conectorSystemRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)conectorSystemRemote)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  private void _initConectorSystemRemoteProxy(String urlSW) {
	    try {
	      conectorSystemRemote = (new cl.eje.generico.consoleremote.ws.ConectorSystemRemoteServiceLocator()).getConectorSystemRemote(urlSW);
	      if (conectorSystemRemote != null) {
	        if (_endpoint != null)
	          ((javax.xml.rpc.Stub)conectorSystemRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	        else
	          _endpoint = (String)((javax.xml.rpc.Stub)conectorSystemRemote)._getProperty("javax.xml.rpc.service.endpoint.address");
	      }
	      
	    }
	    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (conectorSystemRemote != null)
      ((javax.xml.rpc.Stub)conectorSystemRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote() {
    if (conectorSystemRemote == null)
      _initConectorSystemRemoteProxy();
    return conectorSystemRemote;
  }
  
  public java.lang.String stop(java.lang.String token, java.lang.String processXml) throws java.rmi.RemoteException{
    if (conectorSystemRemote == null)
      _initConectorSystemRemoteProxy();
    return conectorSystemRemote.stop(token, processXml);
  }
  
  public java.lang.String exec(java.lang.String token, java.lang.String arregloStringParams) throws java.rmi.RemoteException{
    if (conectorSystemRemote == null)
      _initConectorSystemRemoteProxy();
    return conectorSystemRemote.exec(token, arregloStringParams);
  }
  
  public java.lang.String getConsole(java.lang.String token, java.lang.String processXml) throws java.rmi.RemoteException{
    if (conectorSystemRemote == null)
      _initConectorSystemRemoteProxy();
    return conectorSystemRemote.getConsole(token, processXml);
  }
  
  
}