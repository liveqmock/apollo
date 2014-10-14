/**
 * FrontServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cn.com.youtong.apollo.webservice.generated;

public class FrontServiceServiceLocator extends org.apache.axis.client.Service implements cn.com.youtong.apollo.webservice.generated.FrontServiceService {

    // Use to get a proxy class for FrontService
    private java.lang.String FrontService_address = "http://localhost:8080/apollo/services/FrontService";

	public void setFrontServiceAddress( String addr )
	{
		this.FrontService_address = addr;
	}

    public java.lang.String getFrontServiceAddress() {
        return FrontService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FrontServiceWSDDServiceName = "FrontService";

    public java.lang.String getFrontServiceWSDDServiceName() {
        return FrontServiceWSDDServiceName;
    }

    public void setFrontServiceWSDDServiceName(java.lang.String name) {
        FrontServiceWSDDServiceName = name;
    }

    public cn.com.youtong.apollo.webservice.generated.FrontService getFrontService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FrontService_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getFrontService(endpoint);
    }

    public cn.com.youtong.apollo.webservice.generated.FrontService getFrontService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.com.youtong.apollo.webservice.generated.FrontServiceSoapBindingStub _stub = new cn.com.youtong.apollo.webservice.generated.FrontServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getFrontServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.com.youtong.apollo.webservice.generated.FrontService.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.com.youtong.apollo.webservice.generated.FrontServiceSoapBindingStub _stub = new cn.com.youtong.apollo.webservice.generated.FrontServiceSoapBindingStub(new java.net.URL(FrontService_address), this);
                _stub.setPortName(getFrontServiceWSDDServiceName());
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
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.apollo.youtong.com.cn", "FrontServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("FrontService"));
        }
        return ports.iterator();
    }

}
