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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * ������Ϣ---XSLT�ַ����� ���Ҽ����� ��Ҫת��Ϊ��<���͡�>����
 * 
 * @version $Revision$ $Date$
**/
public class TableView implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
    **/
    private java.lang.String _content = "";

    /**
     * ��־��Ϣ����ʾ��XSLT����;
     * 
     * 1--����HTML
     * 
     * 2--����EXCEL
     * 
     * 3--����PDF
    **/
    private int _type;

    /**
     * keeps track of state for field: _type
    **/
    private boolean _has_type;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableView() {
        super();
        setContent("");
    } //-- cn.com.youtong.apollo.task.xml.TableView()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
    **/
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'type'. The field 'type' has the
     * following description: ��־��Ϣ����ʾ��XSLT����;
     * 
     * 1--����HTML
     * 
     * 2--����EXCEL
     * 
     * 3--����PDF
     * 
     * @return the value of field 'type'.
    **/
    public int getType()
    {
        return this._type;
    } //-- int getType() 

    /**
    **/
    public boolean hasType()
    {
        return this._has_type;
    } //-- boolean hasType() 

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
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'type'. The field 'type' has the
     * following description: ��־��Ϣ����ʾ��XSLT����;
     * 
     * 1--����HTML
     * 
     * 2--����EXCEL
     * 
     * 3--����PDF
     * 
     * @param type the value of field 'type'.
    **/
    public void setType(int type)
    {
        this._type = type;
        this._has_type = true;
    } //-- void setType(int) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.TableView unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.TableView) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.TableView.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.TableView unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
