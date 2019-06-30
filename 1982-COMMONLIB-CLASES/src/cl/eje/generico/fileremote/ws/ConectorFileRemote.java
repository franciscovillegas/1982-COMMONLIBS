/**
 * ConectorFileRemote.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.fileremote.ws;

public interface ConectorFileRemote extends java.rmi.Remote {
    public void main(java.lang.String[] args) throws java.rmi.RemoteException;
    public java.lang.String toString(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String hashCode(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getName(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String length(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getCanonicalPath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getParent(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String isAbsolute(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String delete(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String setReadOnly(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String list(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String canExecute(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String canRead(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String canWrite(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String createNewFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String deleteOnExit(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String exists(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getAbsoluteFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getAbsolutePath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getCanonicalFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getFreeSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getParentFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getPath(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getTotalSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String getUsableSpace(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String isDirectory(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String isFile(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String isHidden(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String lastModified(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String listFiles(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String listRoots(java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String mkdir(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String mkdirs(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String renameTo(java.lang.String token, java.lang.String fileEnc, java.lang.String pathDest) throws java.rmi.RemoteException;
    public java.lang.String setExecutable(java.lang.String token, java.lang.String fileEnc, boolean executable) throws java.rmi.RemoteException;
    public java.lang.String setLastModified(java.lang.String token, java.lang.String fileEnc, long time) throws java.rmi.RemoteException;
    public java.lang.String setReadable(java.lang.String token, java.lang.String fileEnc, boolean readable) throws java.rmi.RemoteException;
    public java.lang.String setWritable(java.lang.String token, java.lang.String fileEnc, boolean writable) throws java.rmi.RemoteException;
    public java.lang.String toURI(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String toURL(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String readBytes(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String newFile(java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String createNewRemoteFile(java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String readLines(java.lang.String token, java.lang.String fileEnc) throws java.rmi.RemoteException;
    public java.lang.String listFilesPagined(java.lang.String token, java.lang.String fileEnc, java.lang.String consultaDataPage) throws java.rmi.RemoteException;
    public java.lang.String newFileFromPath(java.lang.String token, java.lang.String pathFile) throws java.rmi.RemoteException;
}
