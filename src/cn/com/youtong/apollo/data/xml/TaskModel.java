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
 * 报表数据
 * 
 * @version $Revision$ $Date$
**/
public class TaskModel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 任务ID，表明该数据属于哪个任务
    **/
    private java.lang.String _ID;

    /**
     * 任务时间
    **/
    private TaskTime _taskTime;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskModel() {
        super();
    } //-- cn.com.youtong.apollo.data.xml.TaskModel()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 任务ID，表明该数据属于哪个任务
     * 
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'taskTime'. The field 'taskTime'
     * has the following description: 任务时间
     * 
     * @return the value of field 'taskTime'.
    **/
    public TaskTime getTaskTime()
    {
        return this._taskTime;
    } //-- TaskTime getTaskTime() 

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
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 任务ID，表明该数据属于哪个任务
     * 
     * @param ID the value of field 'ID'.
    **/
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'taskTime'. The field 'taskTime' has
     * the following description: 任务时间
     * 
     * @param taskTime the value of field 'taskTime'.
    **/
    public void setTaskTime(TaskTime taskTime)
    {
        this._taskTime = taskTime;
    } //-- void setTaskTime(TaskTime) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.TaskModel unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.TaskModel) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.TaskModel.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.TaskModel unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
