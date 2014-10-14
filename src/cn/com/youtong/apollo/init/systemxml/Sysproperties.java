/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.init.systemxml;

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
public class Sysproperties implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _syspropertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sysproperties() {
        super();
        _syspropertyList = new ArrayList();
    } //-- cn.com.youtong.apollo.init.systemxml.Sysproperties()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSysproperty
    **/
    public void addSysproperty(Sysproperty vSysproperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _syspropertyList.add(vSysproperty);
    } //-- void addSysproperty(Sysproperty) 

    /**
     * 
     * 
     * @param index
     * @param vSysproperty
    **/
    public void addSysproperty(int index, Sysproperty vSysproperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _syspropertyList.add(index, vSysproperty);
    } //-- void addSysproperty(int, Sysproperty) 

    /**
    **/
    public void clearSysproperty()
    {
        _syspropertyList.clear();
    } //-- void clearSysproperty() 

    /**
    **/
    public java.util.Enumeration enumerateSysproperty()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_syspropertyList.iterator());
    } //-- java.util.Enumeration enumerateSysproperty() 

    /**
     * 
     * 
     * @param index
    **/
    public Sysproperty getSysproperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _syspropertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Sysproperty) _syspropertyList.get(index);
    } //-- Sysproperty getSysproperty(int) 

    /**
    **/
    public Sysproperty[] getSysproperty()
    {
        int size = _syspropertyList.size();
        Sysproperty[] mArray = new Sysproperty[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Sysproperty) _syspropertyList.get(index);
        }
        return mArray;
    } //-- Sysproperty[] getSysproperty() 

    /**
    **/
    public int getSyspropertyCount()
    {
        return _syspropertyList.size();
    } //-- int getSyspropertyCount() 

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
     * @param vSysproperty
    **/
    public boolean removeSysproperty(Sysproperty vSysproperty)
    {
        boolean removed = _syspropertyList.remove(vSysproperty);
        return removed;
    } //-- boolean removeSysproperty(Sysproperty) 

    /**
     * 
     * 
     * @param index
     * @param vSysproperty
    **/
    public void setSysproperty(int index, Sysproperty vSysproperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _syspropertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _syspropertyList.set(index, vSysproperty);
    } //-- void setSysproperty(int, Sysproperty) 

    /**
     * 
     * 
     * @param syspropertyArray
    **/
    public void setSysproperty(Sysproperty[] syspropertyArray)
    {
        //-- copy array
        _syspropertyList.clear();
        for (int i = 0; i < syspropertyArray.length; i++) {
            _syspropertyList.add(syspropertyArray[i]);
        }
    } //-- void setSysproperty(Sysproperty) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.init.systemxml.Sysproperties unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.init.systemxml.Sysproperties) Unmarshaller.unmarshal(cn.com.youtong.apollo.init.systemxml.Sysproperties.class, reader);
    } //-- cn.com.youtong.apollo.init.systemxml.Sysproperties unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
