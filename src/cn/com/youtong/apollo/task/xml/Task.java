/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.task.xml;

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
 * 任务
 * 
 * @version $Revision$ $Date$
**/
public class Task implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _memo;

    /**
     * 任务标识，在整个系统中唯一
    **/
    private java.lang.String _ID;

    /**
     * 表
    **/
    private java.util.ArrayList _tableList;

    /**
     * 任务时间
    **/
    private java.util.ArrayList _taskTimeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Task() {
        super();
        _tableList = new ArrayList();
        _taskTimeList = new ArrayList();
    } //-- cn.com.youtong.apollo.task.xml.Task()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTable
    **/
    public void addTable(Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(vTable);
    } //-- void addTable(Table) 

    /**
     * 
     * 
     * @param index
     * @param vTable
    **/
    public void addTable(int index, Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(index, vTable);
    } //-- void addTable(int, Table) 

    /**
     * 
     * 
     * @param vTaskTime
    **/
    public void addTaskTime(TaskTime vTaskTime)
        throws java.lang.IndexOutOfBoundsException
    {
        _taskTimeList.add(vTaskTime);
    } //-- void addTaskTime(TaskTime) 

    /**
     * 
     * 
     * @param index
     * @param vTaskTime
    **/
    public void addTaskTime(int index, TaskTime vTaskTime)
        throws java.lang.IndexOutOfBoundsException
    {
        _taskTimeList.add(index, vTaskTime);
    } //-- void addTaskTime(int, TaskTime) 

    /**
    **/
    public void clearTable()
    {
        _tableList.clear();
    } //-- void clearTable() 

    /**
    **/
    public void clearTaskTime()
    {
        _taskTimeList.clear();
    } //-- void clearTaskTime() 

    /**
    **/
    public java.util.Enumeration enumerateTable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_tableList.iterator());
    } //-- java.util.Enumeration enumerateTable() 

    /**
    **/
    public java.util.Enumeration enumerateTaskTime()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_taskTimeList.iterator());
    } //-- java.util.Enumeration enumerateTaskTime() 

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 任务标识，在整个系统中唯一
     * 
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

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
     * 
     * 
     * @param index
    **/
    public Table getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Table) _tableList.get(index);
    } //-- Table getTable(int) 

    /**
    **/
    public Table[] getTable()
    {
        int size = _tableList.size();
        Table[] mArray = new Table[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Table) _tableList.get(index);
        }
        return mArray;
    } //-- Table[] getTable() 

    /**
    **/
    public int getTableCount()
    {
        return _tableList.size();
    } //-- int getTableCount() 

    /**
     * 
     * 
     * @param index
    **/
    public TaskTime getTaskTime(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taskTimeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (TaskTime) _taskTimeList.get(index);
    } //-- TaskTime getTaskTime(int) 

    /**
    **/
    public TaskTime[] getTaskTime()
    {
        int size = _taskTimeList.size();
        TaskTime[] mArray = new TaskTime[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (TaskTime) _taskTimeList.get(index);
        }
        return mArray;
    } //-- TaskTime[] getTaskTime() 

    /**
    **/
    public int getTaskTimeCount()
    {
        return _taskTimeList.size();
    } //-- int getTaskTimeCount() 

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
     * @param vTable
    **/
    public boolean removeTable(Table vTable)
    {
        boolean removed = _tableList.remove(vTable);
        return removed;
    } //-- boolean removeTable(Table) 

    /**
     * 
     * 
     * @param vTaskTime
    **/
    public boolean removeTaskTime(TaskTime vTaskTime)
    {
        boolean removed = _taskTimeList.remove(vTaskTime);
        return removed;
    } //-- boolean removeTaskTime(TaskTime) 

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 任务标识，在整个系统中唯一
     * 
     * @param ID the value of field 'ID'.
    **/
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

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
     * 
     * 
     * @param index
     * @param vTable
    **/
    public void setTable(int index, Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.set(index, vTable);
    } //-- void setTable(int, Table) 

    /**
     * 
     * 
     * @param tableArray
    **/
    public void setTable(Table[] tableArray)
    {
        //-- copy array
        _tableList.clear();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.add(tableArray[i]);
        }
    } //-- void setTable(Table) 

    /**
     * 
     * 
     * @param index
     * @param vTaskTime
    **/
    public void setTaskTime(int index, TaskTime vTaskTime)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _taskTimeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _taskTimeList.set(index, vTaskTime);
    } //-- void setTaskTime(int, TaskTime) 

    /**
     * 
     * 
     * @param taskTimeArray
    **/
    public void setTaskTime(TaskTime[] taskTimeArray)
    {
        //-- copy array
        _taskTimeList.clear();
        for (int i = 0; i < taskTimeArray.length; i++) {
            _taskTimeList.add(taskTimeArray[i]);
        }
    } //-- void setTaskTime(TaskTime) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.Task unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.Task) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.Task.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.Task unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
