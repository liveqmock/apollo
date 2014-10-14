/**
 * FrontServiceServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cn.com.youtong.apollo.webservice.generated;

public class FrontServiceServiceTestCase extends junit.framework.TestCase {
    public FrontServiceServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1FrontServiceUploadData() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.uploadData(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test2FrontServiceDownloadData() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadData(new java.lang.String(), new java.lang.String(), java.util.Calendar.getInstance(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test3FrontServiceDownloadAllData() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadAllData(new java.lang.String(), java.util.Calendar.getInstance(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test4FrontServiceDownloadDataByTree() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadDataByTree(new java.lang.String(), new java.lang.String(), java.util.Calendar.getInstance(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test5FrontServicePublishTask() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.publishTask(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test6FrontServiceDownloadTask() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadTask(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test7FrontServiceDeleteTask() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.deleteTask(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test8FrontServicePublishDictionary() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.publishDictionary(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test9FrontServiceDownloadDictionary() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadDictionary(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test10FrontServiceDeleteDictionary() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.deleteDictionary(new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test11FrontServicePublishScriptSuit() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.publishScriptSuit(new java.lang.String(), new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test12FrontServiceDownloadScriptSuit() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.downloadScriptSuit(new java.lang.String(), new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test13FrontServiceDeleteScriptSuit() {
        cn.com.youtong.apollo.webservice.generated.FrontService binding;
        try {
            binding = new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator().getFrontService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            cn.com.youtong.apollo.webservice.generated.ServiceResult value = null;
            value = binding.deleteScriptSuit(new java.lang.String(), new java.lang.String(), new java.lang.String(), new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

}
