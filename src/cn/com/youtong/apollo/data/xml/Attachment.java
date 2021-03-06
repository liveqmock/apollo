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
 * 附件，用base64编码
 * 
 * @version $Revision$ $Date$
**/
public class Attachment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
    **/
    private byte[] _content;

    /**
     * 附件名称，比如"分析文档.zip"
    **/
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public Attachment() {
        super();
    } //-- cn.com.youtong.apollo.data.xml.Attachment()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
    **/
    public byte[] getContent()
    {
        return this._content;
    } //-- byte[] getContent() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: 附件名称，比如"分析文档.zip"
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
    **/
    public void setContent(byte[] content)
    {
        this._content = content;
    } //-- void setContent(byte) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: 附件名称，比如"分析文档.zip"
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
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.Attachment unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.Attachment) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.Attachment.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Attachment unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
