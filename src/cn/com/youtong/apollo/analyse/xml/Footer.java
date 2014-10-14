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
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Footer implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _left;

    private java.lang.String _middle;

    private java.lang.String _right;


      //----------------/
     //- Constructors -/
    //----------------/

    public Footer() {
        super();
    } //-- cn.com.youtong.apollo.analyse.xml.Footer()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'left'.
     * 
     * @return the value of field 'left'.
    **/
    public java.lang.String getLeft()
    {
        return this._left;
    } //-- java.lang.String getLeft() 

    /**
     * Returns the value of field 'middle'.
     * 
     * @return the value of field 'middle'.
    **/
    public java.lang.String getMiddle()
    {
        return this._middle;
    } //-- java.lang.String getMiddle() 

    /**
     * Returns the value of field 'right'.
     * 
     * @return the value of field 'right'.
    **/
    public java.lang.String getRight()
    {
        return this._right;
    } //-- java.lang.String getRight() 

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
     * Sets the value of field 'left'.
     * 
     * @param left the value of field 'left'.
    **/
    public void setLeft(java.lang.String left)
    {
        this._left = left;
    } //-- void setLeft(java.lang.String) 

    /**
     * Sets the value of field 'middle'.
     * 
     * @param middle the value of field 'middle'.
    **/
    public void setMiddle(java.lang.String middle)
    {
        this._middle = middle;
    } //-- void setMiddle(java.lang.String) 

    /**
     * Sets the value of field 'right'.
     * 
     * @param right the value of field 'right'.
    **/
    public void setRight(java.lang.String right)
    {
        this._right = right;
    } //-- void setRight(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Footer unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Footer) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Footer.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Footer unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
