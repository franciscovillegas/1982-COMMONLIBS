package cl.eje.generico.fileremote.ws;

public class ConectorFileRemoteProxy implements cl.eje.generico.fileremote.ws.ConectorFileRemote {
  private String _endpoint = null;
  private cl.eje.generico.fileremote.ws.ConectorFileRemote conectorFileRemote = null;
  
  public ConectorFileRemoteProxy(String urlSW, Boolean use) {
    _initConectorFileRemoteProxy(urlSW);
  }
  
  public ConectorFileRemoteProxy() {
	    _initConectorFileRemoteProxy();
	  }
  
  public ConectorFileRemoteProxy(String endpoint) {
    _endpoint = endpoint;
    _initConectorFileRemoteProxy();
  }
  
  private void _initConectorFileRemoteProxy() {
	    try {
	      conectorFileRemote = (new cl.eje.generico.fileremote.ws.ConectorFileRemoteServiceLocator()).getConectorFileRemote();
	      if (conectorFileRemote != null) {
	        if (_endpoint != null)
	          ((javax.xml.rpc.Stub)conectorFileRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	        else
	          _endpoint = (String)((javax.xml.rpc.Stub)conectorFileRemote)._getProperty("javax.xml.rpc.service.endpoint.address");
	      }
	      
	    }
	    catch (javax.xml.rpc.ServiceException serviceException) {}
	  }
  
  private void _initConectorFileRemoteProxy(String urlSW) {
    try {
      conectorFileRemote = (new cl.eje.generico.fileremote.ws.ConectorFileRemoteServiceLocator()).getConectorFileRemote(urlSW);
      if (conectorFileRemote != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)conectorFileRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)conectorFileRemote)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (conectorFileRemote != null)
      ((javax.xml.rpc.Stub)conectorFileRemote)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cl.eje.generico.fileremote.ws.ConectorFileRemote getConectorFileRemote() {
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote;
  }
  
  public void main(java.lang.String[] args) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    conectorFileRemote.main(args);
  }
  
  public java.lang.String toString(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.toString(token, fileEnc);
  }
  
  public java.lang.String hashCode(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.hashCode(token, fileEnc);
  }
  
  public java.lang.String getName(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getName(token, fileEnc);
  }
  
  public java.lang.String length(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.length(token, fileEnc);
  }
  
  public java.lang.String getCanonicalPath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getCanonicalPath(token, fileEnc);
  }
  
  public java.lang.String getParent(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getParent(token, fileEnc);
  }
  
  public java.lang.String isAbsolute(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.isAbsolute(token, fileEnc);
  }
  
  public java.lang.String delete(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.delete(token, fileEnc);
  }
  
  public java.lang.String setReadOnly(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.setReadOnly(token, fileEnc);
  }
  
  public java.lang.String list(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.list(token, fileEnc);
  }
  
  public java.lang.String canExecute(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.canExecute(token, fileEnc);
  }
  
  public java.lang.String canRead(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.canRead(token, fileEnc);
  }
  
  public java.lang.String canWrite(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.canWrite(token, fileEnc);
  }
  
  public java.lang.String createNewFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.createNewFile(token, fileEnc);
  }
  
  public java.lang.String deleteOnExit(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.deleteOnExit(token, fileEnc);
  }
  
  public java.lang.String exists(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.exists(token, fileEnc);
  }
  
  public java.lang.String getAbsoluteFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getAbsoluteFile(token, fileEnc);
  }
  
  public java.lang.String getAbsolutePath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getAbsolutePath(token, fileEnc);
  }
  
  public java.lang.String getCanonicalFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getCanonicalFile(token, fileEnc);
  }
  
  public java.lang.String getFreeSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getFreeSpace(token, fileEnc);
  }
  
  public java.lang.String getParentFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getParentFile(token, fileEnc);
  }
  
  public java.lang.String getPath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getPath(token, fileEnc);
  }
  
  public java.lang.String getTotalSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getTotalSpace(token, fileEnc);
  }
  
  public java.lang.String getUsableSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.getUsableSpace(token, fileEnc);
  }
  
  public java.lang.String isDirectory(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.isDirectory(token, fileEnc);
  }
  
  public java.lang.String isFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.isFile(token, fileEnc);
  }
  
  public java.lang.String isHidden(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.isHidden(token, fileEnc);
  }
  
  public java.lang.String lastModified(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.lastModified(token, fileEnc);
  }
  
  public java.lang.String listFiles(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.listFiles(token, fileEnc);
  }
  
  public java.lang.String listRoots(java.lang.String token) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.listRoots(token);
  }
  
  public java.lang.String mkdir(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.mkdir(token, fileEnc);
  }
  
  public java.lang.String mkdirs(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.mkdirs(token, fileEnc);
  }
  
  public java.lang.String renameTo(java.lang.String token, java.lang.String fileEnc, java.lang.String pathDest) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.renameTo(token, fileEnc, pathDest);
  }
  
  public java.lang.String setExecutable(java.lang.String token, java.lang.String fileEnc, boolean executable) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.setExecutable(token, fileEnc, executable);
  }
  
  public java.lang.String setLastModified(java.lang.String token, java.lang.String fileEnc, long time) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.setLastModified(token, fileEnc, time);
  }
  
  public java.lang.String setReadable(java.lang.String token, java.lang.String fileEnc, boolean readable) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.setReadable(token, fileEnc, readable);
  }
  
  public java.lang.String setWritable(java.lang.String token, java.lang.String fileEnc, boolean writable) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.setWritable(token, fileEnc, writable);
  }
  
  public java.lang.String toURI(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.toURI(token, fileEnc);
  }
  
  public java.lang.String toURL(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.toURL(token, fileEnc);
  }
  
  public java.lang.String readBytes(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.readBytes(token, fileEnc);
  }
  
  public java.lang.String newFile(java.lang.String token) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.newFile(token);
  }
  
  public java.lang.String createNewRemoteFile(java.lang.String token) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.createNewRemoteFile(token);
  }
  
  public java.lang.String readLines(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.readLines(token, fileEnc);
  }
  
  public java.lang.String newFileFromPath(java.lang.String token, java.lang.String pathFile) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.newFileFromPath(token, pathFile);
  }
  
  public java.lang.String listFilesPagined(java.lang.String token, java.lang.String fileEnc, java.lang.String consultaDataPage) throws java.rmi.RemoteException{
    if (conectorFileRemote == null)
      _initConectorFileRemoteProxy();
    return conectorFileRemote.listFilesPagined(token, fileEnc, consultaDataPage);
  }
  
  
}