/**
 * ConectorSystemRemoteService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.consoleremote.ws;

public interface ConectorSystemRemoteService extends javax.xml.rpc.Service {
    public java.lang.String getConectorSystemRemoteAddress();

    public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote() throws javax.xml.rpc.ServiceException;

    public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
