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
 * 脚本条目
 * 
 * @version $Revision$ $Date$
**/
public class ScriptEntry implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 脚本名称，值为scriptSuit中脚本的名称，这里的脚本只包括表间和表内运算脚本，因为审核脚本没有执行顺序
    **/
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public ScriptEntry() {
        super();
    } //-- cn.com.youtong.apollo.task.xml.ScriptEntry()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description:
     * 脚本名称，值为scriptSuit中脚本的名称，这里的脚本只包括表间和表内运算脚本，因为审核脚本没有执行顺序
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
     * Sets the value of field 'name'. The field 'name' has the
     * following description:
     * 脚本名称，值为scriptSuit中脚本的名称，这里的脚本只包括表间和表内运算脚本，因为审核脚本没有执行顺序
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
    public static cn.com.youtong.apollo.task.xml.ScriptEntry unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.ScriptEntry) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.ScriptEntry.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.ScriptEntry unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
