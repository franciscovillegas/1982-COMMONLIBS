/**
 * ConectorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.ws;

public interface ConectorService extends javax.xml.rpc.Service {
    public java.lang.String getConectorAddress();

    public cl.eje.generico.ws.Conector getConector() throws javax.xml.rpc.ServiceException;

    public cl.eje.generico.ws.Conector getConector(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
