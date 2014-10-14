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
import java.util.Date;
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

    private java.util.Date _beginTime;

    private java.util.Date _endTime;

    private java.util.Date _submitBeginTime;

    private java.util.Date _submitEndTime;

    private java.util.Date _attentionBeginTime;

    private java.util.Date _attentionEndTime;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskTime() {
        super();
    } //-- cn.com.youtong.apollo.task.xml.TaskTime()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'attentionBeginTime'.
     * 
     * @return the value of field 'attentionBeginTime'.
    **/
    public java.util.Date getAttentionBeginTime()
    {
        return this._attentionBeginTime;
    } //-- java.util.Date getAttentionBeginTime() 

    /**
     * Returns the value of field 'attentionEndTime'.
     * 
     * @return the value of field 'attentionEndTime'.
    **/
    public java.util.Date getAttentionEndTime()
    {
        return this._attentionEndTime;
    } //-- java.util.Date getAttentionEndTime() 

    /**
     * Returns the value of field 'beginTime'.
     * 
     * @return the value of field 'beginTime'.
    **/
    public java.util.Date getBeginTime()
    {
        return this._beginTime;
    } //-- java.util.Date getBeginTime() 

    /**
     * Returns the value of field 'endTime'.
     * 
     * @return the value of field 'endTime'.
    **/
    public java.util.Date getEndTime()
    {
        return this._endTime;
    } //-- java.util.Date getEndTime() 

    /**
     * Returns the value of field 'submitBeginTime'.
     * 
     * @return the value of field 'submitBeginTime'.
    **/
    public java.util.Date getSubmitBeginTime()
    {
        return this._submitBeginTime;
    } //-- java.util.Date getSubmitBeginTime() 

    /**
     * Returns the value of field 'submitEndTime'.
     * 
     * @return the value of field 'submitEndTime'.
    **/
    public java.util.Date getSubmitEndTime()
    {
        return this._submitEndTime;
    } //-- java.util.Date getSubmitEndTime() 

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
     * Sets the value of field 'attentionBeginTime'.
     * 
     * @param attentionBeginTime the value of field
     * 'attentionBeginTime'.
    **/
    public void setAttentionBeginTime(java.util.Date attentionBeginTime)
    {
        this._attentionBeginTime = attentionBeginTime;
    } //-- void setAttentionBeginTime(java.util.Date) 

    /**
     * Sets the value of field 'attentionEndTime'.
     * 
     * @param attentionEndTime the value of field 'attentionEndTime'
    **/
    public void setAttentionEndTime(java.util.Date attentionEndTime)
    {
        this._attentionEndTime = attentionEndTime;
    } //-- void setAttentionEndTime(java.util.Date) 

    /**
     * Sets the value of field 'beginTime'.
     * 
     * @param beginTime the value of field 'beginTime'.
    **/
    public void setBeginTime(java.util.Date beginTime)
    {
        this._beginTime = beginTime;
    } //-- void setBeginTime(java.util.Date) 

    /**
     * Sets the value of field 'endTime'.
     * 
     * @param endTime the value of field 'endTime'.
    **/
    public void setEndTime(java.util.Date endTime)
    {
        this._endTime = endTime;
    } //-- void setEndTime(java.util.Date) 

    /**
     * Sets the value of field 'submitBeginTime'.
     * 
     * @param submitBeginTime the value of field 'submitBeginTime'.
    **/
    public void setSubmitBeginTime(java.util.Date submitBeginTime)
    {
        this._submitBeginTime = submitBeginTime;
    } //-- void setSubmitBeginTime(java.util.Date) 

    /**
     * Sets the value of field 'submitEndTime'.
     * 
     * @param submitEndTime the value of field 'submitEndTime'.
    **/
    public void setSubmitEndTime(java.util.Date submitEndTime)
    {
        this._submitEndTime = submitEndTime;
    } //-- void setSubmitEndTime(java.util.Date) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.TaskTime unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.TaskTime) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.TaskTime.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.TaskTime unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
