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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Fonts implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * ×ÖÌå¶¨Òå
    **/
    private java.util.ArrayList _fontList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Fonts() {
        super();
        _fontList = new ArrayList();
    } //-- cn.com.youtong.apollo.analyse.xml.Fonts()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vFont
    **/
    public void addFont(Font vFont)
        throws java.lang.IndexOutOfBoundsException
    {
        _fontList.add(vFont);
    } //-- void addFont(Font) 

    /**
     * 
     * 
     * @param index
     * @param vFont
    **/
    public void addFont(int index, Font vFont)
        throws java.lang.IndexOutOfBoundsException
    {
        _fontList.add(index, vFont);
    } //-- void addFont(int, Font) 

    /**
    **/
    public void clearFont()
    {
        _fontList.clear();
    } //-- void clearFont() 

    /**
    **/
    public java.util.Enumeration enumerateFont()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_fontList.iterator());
    } //-- java.util.Enumeration enumerateFont() 

    /**
     * 
     * 
     * @param index
    **/
    public Font getFont(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fontList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Font) _fontList.get(index);
    } //-- Font getFont(int) 

    /**
    **/
    public Font[] getFont()
    {
        int size = _fontList.size();
        Font[] mArray = new Font[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Font) _fontList.get(index);
        }
        return mArray;
    } //-- Font[] getFont() 

    /**
    **/
    public int getFontCount()
    {
        return _fontList.size();
    } //-- int getFontCount() 

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
     * 
     * 
     * @param vFont
    **/
    public boolean removeFont(Font vFont)
    {
        boolean removed = _fontList.remove(vFont);
        return removed;
    } //-- boolean removeFont(Font) 

    /**
     * 
     * 
     * @param index
     * @param vFont
    **/
    public void setFont(int index, Font vFont)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fontList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fontList.set(index, vFont);
    } //-- void setFont(int, Font) 

    /**
     * 
     * 
     * @param fontArray
    **/
    public void setFont(Font[] fontArray)
    {
        //-- copy array
        _fontList.clear();
        for (int i = 0; i < fontArray.length; i++) {
            _fontList.add(fontArray[i]);
        }
    } //-- void setFont(Font) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Fonts unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Fonts) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Fonts.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Fonts unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
