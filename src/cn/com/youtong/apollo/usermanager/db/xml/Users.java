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
public class Users implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _userList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Users() {
        super();
        _userList = new ArrayList();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Users()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUser
    **/
    public void addUser(User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(vUser);
    } //-- void addUser(User) 

    /**
     * 
     * 
     * @param index
     * @param vUser
    **/
    public void addUser(int index, User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(index, vUser);
    } //-- void addUser(int, User) 

    /**
    **/
    public void clearUser()
    {
        _userList.clear();
    } //-- void clearUser() 

    /**
    **/
    public java.util.Enumeration enumerateUser()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_userList.iterator());
    } //-- java.util.Enumeration enumerateUser() 

    /**
     * 
     * 
     * @param index
    **/
    public User getUser(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (User) _userList.get(index);
    } //-- User getUser(int) 

    /**
    **/
    public User[] getUser()
    {
        int size = _userList.size();
        User[] mArray = new User[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (User) _userList.get(index);
        }
        return mArray;
    } //-- User[] getUser() 

    /**
    **/
    public int getUserCount()
    {
        return _userList.size();
    } //-- int getUserCount() 

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
     * @param vUser
    **/
    public boolean removeUser(User vUser)
    {
        boolean removed = _userList.remove(vUser);
        return removed;
    } //-- boolean removeUser(User) 

    /**
     * 
     * 
     * @param index
     * @param vUser
    **/
    public void setUser(int index, User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _userList.set(index, vUser);
    } //-- void setUser(int, User) 

    /**
     * 
     * 
     * @param userArray
    **/
    public void setUser(User[] userArray)
    {
        //-- copy array
        _userList.clear();
        for (int i = 0; i < userArray.length; i++) {
            _userList.add(userArray[i]);
        }
    } //-- void setUser(User) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.usermanager.db.xml.Users unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Users) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Users.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Users unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
