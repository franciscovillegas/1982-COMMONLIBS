/**
 * Conector.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.ws;

public interface Conector extends java.rmi.Remote {
    public java.lang.String update(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException;
    public java.lang.String delete(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException;
    public java.lang.String insert(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException;
    public java.lang.String getData(java.lang.String token, java.lang.String query) throws java.rmi.RemoteException;
}
