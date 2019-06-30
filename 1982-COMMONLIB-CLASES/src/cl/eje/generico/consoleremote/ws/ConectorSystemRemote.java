/**
 * ConectorSystemRemote.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.consoleremote.ws;

public interface ConectorSystemRemote extends java.rmi.Remote {
    public java.lang.String stop(java.lang.String token, java.lang.String processXml) throws java.rmi.RemoteException;
    public java.lang.String exec(java.lang.String token, java.lang.String arregloStringParams) throws java.rmi.RemoteException;
    public java.lang.String getConsole(java.lang.String token, java.lang.String processXml) throws java.rmi.RemoteException;
}
