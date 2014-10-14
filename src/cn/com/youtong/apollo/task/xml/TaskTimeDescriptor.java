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

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.handlers.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.*;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class TaskTimeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskTimeDescriptor() {
        super();
        xmlName = "taskTime";
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _beginTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_beginTime", "beginTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getBeginTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setBeginTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _beginTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _endTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_endTime", "endTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getEndTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setEndTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _endTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _submitBeginTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_submitBeginTime", "submitBeginTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getSubmitBeginTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setSubmitBeginTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _submitBeginTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _submitEndTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_submitEndTime", "submitEndTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getSubmitEndTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setSubmitEndTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _submitEndTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _attentionBeginTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_attentionBeginTime", "attentionBeginTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getAttentionBeginTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setAttentionBeginTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _attentionBeginTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _attentionEndTime
        desc = new XMLFieldDescriptorImpl(java.util.Date.class, "_attentionEndTime", "attentionEndTime", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TaskTime target = (TaskTime) object;
                return target.getAttentionEndTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TaskTime target = (TaskTime) object;
                    target.setAttentionEndTime( (java.util.Date) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _attentionEndTime
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- initialize element descriptors
        
    } //-- cn.com.youtong.apollo.task.xml.TaskTimeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass()
    {
        return cn.com.youtong.apollo.task.xml.TaskTime.class;
    } //-- java.lang.Class getJavaClass() 

    /**
    **/
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
    **/
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
