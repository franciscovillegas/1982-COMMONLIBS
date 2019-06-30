/**
 * ConectorFileRemoteService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.fileremote.ws;

public interface ConectorFileRemoteService extends javax.xml.rpc.Service {
    public java.lang.String getConectorFileRemoteAddress();

    public cl.eje.generico.fileremote.ws.ConectorFileRemote getConectorFileRemote() throws javax.xml.rpc.ServiceException;

    public cl.eje.generico.fileremote.ws.ConectorFileRemote getConectorFileRemote(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
