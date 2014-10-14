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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Permission implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _unitID;

    private java.lang.String _taskID;

    private java.lang.String _groupName;

    private int _permit;

    /**
     * keeps track of state for field: _permit
    **/
    private boolean _has_permit;


      //----------------/
     //- Constructors -/
    //----------------/

    public Permission() {
        super();
    } //-- cn.com.youtong.apollo.data.unitpermissions.xml.Permission()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'groupName'.
     * 
     * @return the value of field 'groupName'.
    **/
    public java.lang.String getGroupName()
    {
        return this._groupName;
    } //-- java.lang.String getGroupName() 

    /**
     * Returns the value of field 'permit'.
     * 
     * @return the value of field 'permit'.
    **/
    public int getPermit()
    {
        return this._permit;
    } //-- int getPermit() 

    /**
     * Returns the value of field 'taskID'.
     * 
     * @return the value of field 'taskID'.
    **/
    public java.lang.String getTaskID()
    {
        return this._taskID;
    } //-- java.lang.String getTaskID() 

    /**
     * Returns the value of field 'unitID'.
     * 
     * @return the value of field 'unitID'.
    **/
    public java.lang.String getUnitID()
    {
        return this._unitID;
    } //-- java.lang.String getUnitID() 

    /**
    **/
    public boolean hasPermit()
    {
        return this._has_permit;
    } //-- boolean hasPermit() 

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
     * Sets the value of field 'groupName'.
     * 
     * @param groupName the value of field 'groupName'.
    **/
    public void setGroupName(java.lang.String groupName)
    {
        this._groupName = groupName;
    } //-- void setGroupName(java.lang.String) 

    /**
     * Sets the value of field 'permit'.
     * 
     * @param permit the value of field 'permit'.
    **/
    public void setPermit(int permit)
    {
        this._permit = permit;
        this._has_permit = true;
    } //-- void setPermit(int) 

    /**
     * Sets the value of field 'taskID'.
     * 
     * @param taskID the value of field 'taskID'.
    **/
    public void setTaskID(java.lang.String taskID)
    {
        this._taskID = taskID;
    } //-- void setTaskID(java.lang.String) 

    /**
     * Sets the value of field 'unitID'.
     * 
     * @param unitID the value of field 'unitID'.
    **/
    public void setUnitID(java.lang.String unitID)
    {
        this._unitID = unitID;
    } //-- void setUnitID(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.Permission unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.Permission) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.Permission.class, reader);
    } //-- cn.com.youtong.apollo.data.unitpermissions.xml.Permission unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
