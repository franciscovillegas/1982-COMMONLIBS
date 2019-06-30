/**
 * ConectorSystemRemoteServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.consoleremote.ws;

public class ConectorSystemRemoteServiceLocator extends org.apache.axis.client.Service implements cl.eje.generico.consoleremote.ws.ConectorSystemRemoteService {

    public ConectorSystemRemoteServiceLocator() {
    }


    public ConectorSystemRemoteServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConectorSystemRemoteServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ConectorSystemRemote
    private java.lang.String ConectorSystemRemote_address = "http://localhost:8090/genericoWS/services/ConectorSystemRemote";

    public java.lang.String getConectorSystemRemoteAddress() {
        return ConectorSystemRemote_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConectorSystemRemoteWSDDServiceName = "ConectorSystemRemote";

    public java.lang.String getConectorSystemRemoteWSDDServiceName() {
        return ConectorSystemRemoteWSDDServiceName;
    }

    public void setConectorSystemRemoteWSDDServiceName(java.lang.String name) {
        ConectorSystemRemoteWSDDServiceName = name;
    }

    public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote(String url) throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(url + "/services/ConectorSystemRemote");
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConectorSystemRemote(endpoint);
    }
    
    public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
         try {
             endpoint = new java.net.URL(ConectorSystemRemote_address);
         }
         catch (java.net.MalformedURLException e) {
             throw new javax.xml.rpc.ServiceException(e);
         }
         return getConectorSystemRemote(endpoint);
     }

    public cl.eje.generico.consoleremote.ws.ConectorSystemRemote getConectorSystemRemote(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cl.eje.generico.consoleremote.ws.ConectorSystemRemoteSoapBindingStub _stub = new cl.eje.generico.consoleremote.ws.ConectorSystemRemoteSoapBindingStub(portAddress, this);
            _stub.setPortName(getConectorSystemRemoteWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConectorSystemRemoteEndpointAddress(java.lang.String address) {
        ConectorSystemRemote_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cl.eje.generico.consoleremote.ws.ConectorSystemRemote.class.isAssignableFrom(serviceEndpointInterface)) {
                cl.eje.generico.consoleremote.ws.ConectorSystemRemoteSoapBindingStub _stub = new cl.eje.generico.consoleremote.ws.ConectorSystemRemoteSoapBindingStub(new java.net.URL(ConectorSystemRemote_address), this);
                _stub.setPortName(getConectorSystemRemoteWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ConectorSystemRemote".equals(inputPortName)) {
            return getConectorSystemRemote();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.consoleremote.generico.eje.cl", "ConectorSystemRemoteService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.consoleremote.generico.eje.cl", "ConectorSystemRemote"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ConectorSystemRemote".equals(portName)) {
            setConectorSystemRemoteEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
