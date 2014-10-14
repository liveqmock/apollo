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
public class Role implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _userRights;

    private boolean _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    private java.lang.String _memo;


      //----------------/
     //- Constructors -/
    //----------------/

    public Role() {
        super();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Role()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag() 

    /**
     * Returns the value of field 'flag'.
     * 
     * @return the value of field 'flag'.
    **/
    public boolean getFlag()
    {
        return this._flag;
    } //-- boolean getFlag() 

    /**
     * Returns the value of field 'memo'.
     * 
     * @return the value of field 'memo'.
    **/
    public java.lang.String getMemo()
    {
        return this._memo;
    } //-- java.lang.String getMemo() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'userRights'.
     * 
     * @return the value of field 'userRights'.
    **/
    public java.lang.String getUserRights()
    {
        return this._userRights;
    } //-- java.lang.String getUserRights() 

    /**
    **/
    public boolean hasFlag()
    {
        return this._has_flag;
    } //-- boolean hasFlag() 

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
     * Sets the value of field 'flag'.
     * 
     * @param flag the value of field 'flag'.
    **/
    public void setFlag(boolean flag)
    {
        this._flag = flag;
        this._has_flag = true;
    } //-- void setFlag(boolean) 

    /**
     * Sets the value of field 'memo'.
     * 
     * @param memo the value of field 'memo'.
    **/
    public void setMemo(java.lang.String memo)
    {
        this._memo = memo;
    } //-- void setMemo(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'userRights'.
     * 
     * @param userRights the value of field 'userRights'.
    **/
    public void setUserRights(java.lang.String userRights)
    {
        this._userRights = userRights;
    } //-- void setUserRights(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.usermanager.db.xml.Role unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Role) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Role.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Role unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
