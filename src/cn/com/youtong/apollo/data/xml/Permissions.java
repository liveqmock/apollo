/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.data.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;


/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Permissions implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _permissionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Permissions() {
        super();
        _permissionList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.unitpermissions.xml.Permissions()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vPermission
    **/
    public void addPermission(Permission vPermission)
        throws java.lang.IndexOutOfBoundsException
    {
        _permissionList.add(vPermission);
    } //-- void addPermission(Permission) 

    /**
     * 
     * 
     * @param index
     * @param vPermission
    **/
    public void addPermission(int index, Permission vPermission)
        throws java.lang.IndexOutOfBoundsException
    {
        _permissionList.add(index, vPermission);
    } //-- void addPermission(int, Permission) 

    /**
    **/
    public void clearPermission()
    {
        _permissionList.clear();
    } //-- void clearPermission() 

    /**
    **/
    public java.util.Enumeration enumeratePermission()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_permissionList.iterator());
    } //-- java.util.Enumeration enumeratePermission() 

    /**
     * 
     * 
     * @param index
    **/
    public Permission getPermission(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _permissionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Permission) _permissionList.get(index);
    } //-- Permission getPermission(int) 

    /**
    **/
    public Permission[] getPermission()
    {
        int size = _permissionList.size();
        Permission[] mArray = new Permission[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Permission) _permissionList.get(index);
        }
        return mArray;
    } //-- Permission[] getPermission() 

    /**
    **/
    public int getPermissionCount()
    {
        return _permissionList.size();
    } //-- int getPermissionCount() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * 
     * 
     * @param vPermission
    **/
    public boolean removePermission(Permission vPermission)
    {
        boolean removed = _permissionList.remove(vPermission);
        return removed;
    } //-- boolean removePermission(Permission) 

    /**
     * 
     * 
     * @param index
     * @param vPermission
    **/
    public void setPermission(int index, Permission vPermission)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _permissionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _permissionList.set(index, vPermission);
    } //-- void setPermission(int, Permission) 

    /**
     * 
     * 
     * @param permissionArray
    **/
    public void setPermission(Permission[] permissionArray)
    {
        //-- copy array
        _permissionList.clear();
        for (int i = 0; i < permissionArray.length; i++) {
            _permissionList.add(permissionArray[i]);
        }
    } //-- void setPermission(Permission) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.Permissions unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.Permissions) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.Permissions.class, reader);
    } //-- cn.com.youtong.apollo.data.unitpermissions.xml.Permissions unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
