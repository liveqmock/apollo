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
 * ��Ԫ��
 * 
 * @version $Revision$ $Date$
**/
public class CellModel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * �洢�õ�Ԫ�����ݵ����ݿ���ֶ�����
    **/
    private java.lang.String _field;

    /**
     * �õ�Ԫ���ֵ
    **/
    private java.lang.String _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public CellModel() {
        super();
    } //-- cn.com.youtong.apollo.data.xml.Cell()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'field'. The field 'field' has
     * the following description: �洢�õ�Ԫ�����ݵ����ݿ���ֶ�����
     * 
     * @return the value of field 'field'.
    **/
    public java.lang.String getField()
    {
        return this._field;
    } //-- java.lang.String getField() 

    /**
     * Returns the value of field 'value'. The field 'value' has
     * the following description: �õ�Ԫ���ֵ
     * 
     * @return the value of field 'value'.
    **/
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

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
     * Sets the value of field 'field'. The field 'field' has the
     * following description: �洢�õ�Ԫ�����ݵ����ݿ���ֶ�����
     * 
     * @param field the value of field 'field'.
    **/
    public void setField(java.lang.String field)
    {
        this._field = field;
    } //-- void setField(java.lang.String) 

    /**
     * Sets the value of field 'value'. The field 'value' has the
     * following description: �õ�Ԫ���ֵ
     * 
     * @param value the value of field 'value'.
    **/
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.CellModel unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.CellModel) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.CellModel.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Cell unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
