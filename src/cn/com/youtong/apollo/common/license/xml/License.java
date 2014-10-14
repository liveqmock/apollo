/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.common.license.xml;

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
 * license 根目录
 * 
 * @version $Revision$ $Date$
**/
public class License implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 购买公司名称
    **/
    private java.lang.String _company;

    /**
     * 注册用户名
    **/
    private java.lang.String _username;

    /**
     * 产品安装日期
    **/
    private org.exolab.castor.types.Date _installdate;

    /**
     * 机器码
    **/
    private java.lang.String _machinecode;

    /**
     * 注册码
    **/
    private java.lang.String _regkey;


      //----------------/
     //- Constructors -/
    //----------------/

    public License() {
        super();
    } //-- cn.com.youtong.apollo.common.license.xml.License()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'company'. The field 'company'
     * has the following description: 购买公司名称
     * 
     * @return the value of field 'company'.
    **/
    public java.lang.String getCompany()
    {
        return this._company;
    } //-- java.lang.String getCompany() 

    /**
     * Returns the value of field 'installdate'. The field
     * 'installdate' has the following description: 产品安装日期
     * 
     * @return the value of field 'installdate'.
    **/
    public org.exolab.castor.types.Date getInstalldate()
    {
        return this._installdate;
    } //-- org.exolab.castor.types.Date getInstalldate() 

    /**
     * Returns the value of field 'machinecode'. The field
     * 'machinecode' has the following description: 机器码
     * 
     * @return the value of field 'machinecode'.
    **/
    public java.lang.String getMachinecode()
    {
        return this._machinecode;
    } //-- java.lang.String getMachinecode() 

    /**
     * Returns the value of field 'regkey'. The field 'regkey' has
     * the following description: 注册码
     * 
     * @return the value of field 'regkey'.
    **/
    public java.lang.String getRegkey()
    {
        return this._regkey;
    } //-- java.lang.String getRegkey() 

    /**
     * Returns the value of field 'username'. The field 'username'
     * has the following description: 注册用户名
     * 
     * @return the value of field 'username'.
    **/
    public java.lang.String getUsername()
    {
        return this._username;
    } //-- java.lang.String getUsername() 

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
     * Sets the value of field 'company'. The field 'company' has
     * the following description: 购买公司名称
     * 
     * @param company the value of field 'company'.
    **/
    public void setCompany(java.lang.String company)
    {
        this._company = company;
    } //-- void setCompany(java.lang.String) 

    /**
     * Sets the value of field 'installdate'. The field
     * 'installdate' has the following description: 产品安装日期
     * 
     * @param installdate the value of field 'installdate'.
    **/
    public void setInstalldate(org.exolab.castor.types.Date installdate)
    {
        this._installdate = installdate;
    } //-- void setInstalldate(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'machinecode'. The field
     * 'machinecode' has the following description: 机器码
     * 
     * @param machinecode the value of field 'machinecode'.
    **/
    public void setMachinecode(java.lang.String machinecode)
    {
        this._machinecode = machinecode;
    } //-- void setMachinecode(java.lang.String) 

    /**
     * Sets the value of field 'regkey'. The field 'regkey' has the
     * following description: 注册码
     * 
     * @param regkey the value of field 'regkey'.
    **/
    public void setRegkey(java.lang.String regkey)
    {
        this._regkey = regkey;
    } //-- void setRegkey(java.lang.String) 

    /**
     * Sets the value of field 'username'. The field 'username' has
     * the following description: 注册用户名
     * 
     * @param username the value of field 'username'.
    **/
    public void setUsername(java.lang.String username)
    {
        this._username = username;
    } //-- void setUsername(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.common.license.xml.License unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.common.license.xml.License) Unmarshaller.unmarshal(cn.com.youtong.apollo.common.license.xml.License.class, reader);
    } //-- cn.com.youtong.apollo.common.license.xml.License unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
