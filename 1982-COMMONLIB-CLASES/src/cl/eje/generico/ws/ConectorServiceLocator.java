/**
 * ConectorServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cl.eje.generico.ws;

public class ConectorServiceLocator extends org.apache.axis.client.Service implements cl.eje.generico.ws.ConectorService {

    public ConectorServiceLocator() {
    }


    public ConectorServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConectorServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Conector
    private java.lang.String Conector_address = "http://localhost:8070/lucky/sw/services/Conector";

    public java.lang.String getConectorAddress() {
        return Conector_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConectorWSDDServiceName = "Conector";

    public java.lang.String getConectorWSDDServiceName() {
        return ConectorWSDDServiceName;
    }

    public void setConectorWSDDServiceName(java.lang.String name) {
        ConectorWSDDServiceName = name;
    }

    public cl.eje.generico.ws.Conector getConector(String url) throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
         try {
             endpoint = new java.net.URL(url + "/services/Conector");
         }
         catch (java.net.MalformedURLException e) {
             throw new javax.xml.rpc.ServiceException(e);
         }
         return getConector(endpoint);
     }
    
    public cl.eje.generico.ws.Conector getConector() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Conector_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConector(endpoint);
    }

    public cl.eje.generico.ws.Conector getConector(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cl.eje.generico.ws.ConectorSoapBindingStub _stub = new cl.eje.generico.ws.ConectorSoapBindingStub(portAddress, this);
            _stub.setPortName(getConectorWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConectorEndpointAddress(java.lang.String address) {
        Conector_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cl.eje.generico.ws.Conector.class.isAssignableFrom(serviceEndpointInterface)) {
                cl.eje.generico.ws.ConectorSoapBindingStub _stub = new cl.eje.generico.ws.ConectorSoapBindingStub(new java.net.URL(Conector_address), this);
                _stub.setPortName(getConectorWSDDServiceName());
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
        if ("Conector".equals(inputPortName)) {
            return getConector();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.generico.eje.cl", "ConectorService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.generico.eje.cl", "Conector"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Conector".equals(portName)) {
            setConectorEndpointAddress(address);
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
