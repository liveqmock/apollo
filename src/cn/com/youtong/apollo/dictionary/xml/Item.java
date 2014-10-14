/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.dictionary.xml;

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
 * �����ֵ���Ŀ
 *
 * @version $Revision$ $Date$
**/
public class Item implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _key;

    private java.lang.String _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public Item() {
        super();
    } //-- cn.com.youtong.apollo.dictionary.xml.Item()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'key'.
     *
     * @return the value of field 'key'.
    **/
    public java.lang.String getKey()
    {
        return this._key;
    } //-- java.lang.String getKey()

    /**
     * Returns the value of field 'value'.
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
     * Sets the value of field 'key'.
     *
     * @param key the value of field 'key'.
    **/
    public void setKey(java.lang.String key)
    {
        this._key = key;
    } //-- void setKey(java.lang.String)

    /**
     * Sets the value of field 'value'.
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
    public static cn.com.youtong.apollo.dictionary.xml.Item unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.dictionary.xml.Item) Unmarshaller.unmarshal(cn.com.youtong.apollo.dictionary.xml.Item.class, reader);
    } //-- cn.com.youtong.apollo.dictionary.xml.Item unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
