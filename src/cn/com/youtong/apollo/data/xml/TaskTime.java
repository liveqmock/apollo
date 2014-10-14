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
import java.util.Date;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 任务时间
 * 
 * @version $Revision$ $Date$
**/
public class TaskTime implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 任务时间
    **/
    private java.util.Date _taskTime;

    /**
     * 单位
    **/
    private java.util.ArrayList _unitList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskTime() {
        super();
        _unitList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.TaskTime()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUnit
    **/
    public void addUnit(Unit vUnit)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitList.add(vUnit);
    } //-- void addUnit(Unit) 

    /**
     * 
     * 
     * @param index
     * @param vUnit
    **/
    public void addUnit(int index, Unit vUnit)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitList.add(index, vUnit);
    } //-- void addUnit(int, Unit) 

    /**
    **/
    public void clearUnit()
    {
        _unitList.clear();
    } //-- void clearUnit() 

    /**
    **/
    public java.util.Enumeration enumerateUnit()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_unitList.iterator());
    } //-- java.util.Enumeration enumerateUnit() 

    /**
     * Returns the value of field 'taskTime'. The field 'taskTime'
     * has the following description: 任务时间
     * 
     * @return the value of field 'taskTime'.
    **/
    public java.util.Date getTaskTime()
    {
        return this._taskTime;
    } //-- java.util.Date getTaskTime() 

    /**
     * 
     * 
     * @param index
    **/
    public Unit getUnit(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Unit) _unitList.get(index);
    } //-- Unit getUnit(int) 

    /**
    **/
    public Unit[] getUnit()
    {
        int size = _unitList.size();
        Unit[] mArray = new Unit[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Unit) _unitList.get(index);
        }
        return mArray;
    } //-- Unit[] getUnit() 

    /**
    **/
    public int getUnitCount()
    {
        return _unitList.size();
    } //-- int getUnitCount() 

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
     * @param vUnit
    **/
    public boolean removeUnit(Unit vUnit)
    {
        boolean removed = _unitList.remove(vUnit);
        return removed;
    } //-- boolean removeUnit(Unit) 

    /**
     * Sets the value of field 'taskTime'. The field 'taskTime' has
     * the following description: 任务时间
     * 
     * @param taskTime the value of field 'taskTime'.
    **/
    public void setTaskTime(java.util.Date taskTime)
    {
        this._taskTime = taskTime;
    } //-- void setTaskTime(java.util.Date) 

    /**
     * 
     * 
     * @param index
     * @param vUnit
    **/
    public void setUnit(int index, Unit vUnit)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _unitList.set(index, vUnit);
    } //-- void setUnit(int, Unit) 

    /**
     * 
     * 
     * @param unitArray
    **/
    public void setUnit(Unit[] unitArray)
    {
        //-- copy array
        _unitList.clear();
        for (int i = 0; i < unitArray.length; i++) {
            _unitList.add(unitArray[i]);
        }
    } //-- void setUnit(Unit) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.TaskTime unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.TaskTime) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.TaskTime.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.TaskTime unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
