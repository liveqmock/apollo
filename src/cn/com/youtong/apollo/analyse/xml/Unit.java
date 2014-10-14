/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.analyse.xml;

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
 * 单位
 * 
 * @version $Revision$ $Date$
**/
public class Unit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 单位id
    **/
    private java.util.ArrayList _unitIDList;

    /**
     * 单位选择条件表达式
    **/
    private java.lang.String _condition;


      //----------------/
     //- Constructors -/
    //----------------/

    public Unit() {
        super();
        _unitIDList = new ArrayList();
    } //-- cn.com.youtong.apollo.analyse.xml.Unit()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUnitID
    **/
    public void addUnitID(java.lang.String vUnitID)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitIDList.add(vUnitID);
    } //-- void addUnitID(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vUnitID
    **/
    public void addUnitID(int index, java.lang.String vUnitID)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitIDList.add(index, vUnitID);
    } //-- void addUnitID(int, java.lang.String) 

    /**
    **/
    public void clearUnitID()
    {
        _unitIDList.clear();
    } //-- void clearUnitID() 

    /**
    **/
    public java.util.Enumeration enumerateUnitID()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_unitIDList.iterator());
    } //-- java.util.Enumeration enumerateUnitID() 

    /**
     * Returns the value of field 'condition'. The field
     * 'condition' has the following description: 单位选择条件表达式
     * 
     * @return the value of field 'condition'.
    **/
    public java.lang.String getCondition()
    {
        return this._condition;
    } //-- java.lang.String getCondition() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getUnitID(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitIDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_unitIDList.get(index);
    } //-- java.lang.String getUnitID(int) 

    /**
    **/
    public java.lang.String[] getUnitID()
    {
        int size = _unitIDList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_unitIDList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getUnitID() 

    /**
    **/
    public int getUnitIDCount()
    {
        return _unitIDList.size();
    } //-- int getUnitIDCount() 

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
     * @param vUnitID
    **/
    public boolean removeUnitID(java.lang.String vUnitID)
    {
        boolean removed = _unitIDList.remove(vUnitID);
        return removed;
    } //-- boolean removeUnitID(java.lang.String) 

    /**
     * Sets the value of field 'condition'. The field 'condition'
     * has the following description: 单位选择条件表达式
     * 
     * @param condition the value of field 'condition'.
    **/
    public void setCondition(java.lang.String condition)
    {
        this._condition = condition;
    } //-- void setCondition(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vUnitID
    **/
    public void setUnitID(int index, java.lang.String vUnitID)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitIDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _unitIDList.set(index, vUnitID);
    } //-- void setUnitID(int, java.lang.String) 

    /**
     * 
     * 
     * @param unitIDArray
    **/
    public void setUnitID(java.lang.String[] unitIDArray)
    {
        //-- copy array
        _unitIDList.clear();
        for (int i = 0; i < unitIDArray.length; i++) {
            _unitIDList.add(unitIDArray[i]);
        }
    } //-- void setUnitID(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Unit unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Unit) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Unit.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Unit unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
