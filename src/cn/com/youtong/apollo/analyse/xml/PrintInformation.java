/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.analyse.xml;

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
 * ¥Ú”°–≈œ¢
 * 
 * @version $Revision$ $Date$
**/
public class PrintInformation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private Header _header;

    private Footer _footer;


      //----------------/
     //- Constructors -/
    //----------------/

    public PrintInformation() {
        super();
    } //-- cn.com.youtong.apollo.analyse.xml.PrintInformation()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'footer'.
     * 
     * @return the value of field 'footer'.
    **/
    public Footer getFooter()
    {
        return this._footer;
    } //-- Footer getFooter() 

    /**
     * Returns the value of field 'header'.
     * 
     * @return the value of field 'header'.
    **/
    public Header getHeader()
    {
        return this._header;
    } //-- Header getHeader() 

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
     * Sets the value of field 'footer'.
     * 
     * @param footer the value of field 'footer'.
    **/
    public void setFooter(Footer footer)
    {
        this._footer = footer;
    } //-- void setFooter(Footer) 

    /**
     * Sets the value of field 'header'.
     * 
     * @param header the value of field 'header'.
    **/
    public void setHeader(Header header)
    {
        this._header = header;
    } //-- void setHeader(Header) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.PrintInformation unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.PrintInformation) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.PrintInformation.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.PrintInformation unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
