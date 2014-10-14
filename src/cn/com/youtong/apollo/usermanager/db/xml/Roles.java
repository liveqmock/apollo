/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.usermanager.db.xml;

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
public class Roles implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _roleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Roles() {
        super();
        _roleList = new ArrayList();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Roles()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vRole
    **/
    public void addRole(Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(vRole);
    } //-- void addRole(Role) 

    /**
     * 
     * 
     * @param index
     * @param vRole
    **/
    public void addRole(int index, Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(index, vRole);
    } //-- void addRole(int, Role) 

    /**
    **/
    public void clearRole()
    {
        _roleList.clear();
    } //-- void clearRole() 

    /**
    **/
    public java.util.Enumeration enumerateRole()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_roleList.iterator());
    } //-- java.util.Enumeration enumerateRole() 

    /**
     * 
     * 
     * @param index
    **/
    public Role getRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Role) _roleList.get(index);
    } //-- Role getRole(int) 

    /**
    **/
    public Role[] getRole()
    {
        int size = _roleList.size();
        Role[] mArray = new Role[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Role) _roleList.get(index);
        }
        return mArray;
    } //-- Role[] getRole() 

    /**
    **/
    public int getRoleCount()
    {
        return _roleList.size();
    } //-- int getRoleCount() 

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
     * @param vRole
    **/
    public boolean removeRole(Role vRole)
    {
        boolean removed = _roleList.remove(vRole);
        return removed;
    } //-- boolean removeRole(Role) 

    /**
     * 
     * 
     * @param index
     * @param vRole
    **/
    public void setRole(int index, Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _roleList.set(index, vRole);
    } //-- void setRole(int, Role) 

    /**
     * 
     * 
     * @param roleArray
    **/
    public void setRole(Role[] roleArray)
    {
        //-- copy array
        _roleList.clear();
        for (int i = 0; i < roleArray.length; i++) {
            _roleList.add(roleArray[i]);
        }
    } //-- void setRole(Role) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.usermanager.db.xml.Roles unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Roles) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Roles.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Roles unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
