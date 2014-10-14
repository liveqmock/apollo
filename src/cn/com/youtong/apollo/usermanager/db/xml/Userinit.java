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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Userinit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private Users _users;

    private Groups _groups;

    private Roles _roles;


      //----------------/
     //- Constructors -/
    //----------------/

    public Userinit() {
        super();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Userinit()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'groups'.
     * 
     * @return the value of field 'groups'.
    **/
    public Groups getGroups()
    {
        return this._groups;
    } //-- Groups getGroups() 

    /**
     * Returns the value of field 'roles'.
     * 
     * @return the value of field 'roles'.
    **/
    public Roles getRoles()
    {
        return this._roles;
    } //-- Roles getRoles() 

    /**
     * Returns the value of field 'users'.
     * 
     * @return the value of field 'users'.
    **/
    public Users getUsers()
    {
        return this._users;
    } //-- Users getUsers() 

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
     * Sets the value of field 'groups'.
     * 
     * @param groups the value of field 'groups'.
    **/
    public void setGroups(Groups groups)
    {
        this._groups = groups;
    } //-- void setGroups(Groups) 

    /**
     * Sets the value of field 'roles'.
     * 
     * @param roles the value of field 'roles'.
    **/
    public void setRoles(Roles roles)
    {
        this._roles = roles;
    } //-- void setRoles(Roles) 

    /**
     * Sets the value of field 'users'.
     * 
     * @param users the value of field 'users'.
    **/
    public void setUsers(Users users)
    {
        this._users = users;
    } //-- void setUsers(Users) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.usermanager.db.xml.Userinit unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Userinit) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Userinit.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Userinit unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
