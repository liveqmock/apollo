/**
 * FrontService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cn.com.youtong.apollo.webservice.generated;

public interface FrontService extends java.rmi.Remote {
    public cn.com.youtong.apollo.webservice.generated.ServiceResult uploadData(java.lang.String data, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadData(java.lang.String taskID, java.lang.String unitID, java.util.Calendar date, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadAllData(java.lang.String taskID, java.util.Calendar date, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadDataByTree(java.lang.String taskID, java.lang.String unitID, java.util.Calendar date, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult publishTask(java.lang.String definition, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadTask(java.lang.String id, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult deleteTask(java.lang.String taskID, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult publishDictionary(java.lang.String content, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadDictionary(java.lang.String id, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult deleteDictionary(java.lang.String id, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult publishScriptSuit(java.lang.String taskID, java.lang.String script, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult downloadScriptSuit(java.lang.String taskID, java.lang.String suitName, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public cn.com.youtong.apollo.webservice.generated.ServiceResult deleteScriptSuit(java.lang.String taskID, java.lang.String suitName, java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
}
