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
 * 字体定义
 * 
 * @version $Revision$ $Date$
**/
public class Font implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 给Font定义ID，方便其他的地方引用
    **/
    private int _ID;

    /**
     * keeps track of state for field: _ID
    **/
    private boolean _has_ID;

    private java.lang.String _name;

    private java.lang.String _description;

    private int _size = 9;

    /**
     * keeps track of state for field: _size
    **/
    private boolean _has_size;

    private boolean _isBold = false;

    /**
     * keeps track of state for field: _isBold
    **/
    private boolean _has_isBold;

    private boolean _isItalic = false;

    /**
     * keeps track of state for field: _isItalic
    **/
    private boolean _has_isItalic;

    private boolean _isUnderline = false;

    /**
     * keeps track of state for field: _isUnderline
    **/
    private boolean _has_isUnderline;

    private boolean _isStrikeThrough = false;

    /**
     * keeps track of state for field: _isStrikeThrough
    **/
    private boolean _has_isStrikeThrough;

    /**
     * 在PDF中使用的字体名称
    **/
    private java.lang.String _pdfFontName = "STSong-Light";

    /**
     * 对应的PDF字体编码方式
    **/
    private java.lang.String _pdfEncoding = "UniGB-UCS2-H";

    private boolean _isPdfEmbedded = true;

    /**
     * keeps track of state for field: _isPdfEmbedded
    **/
    private boolean _has_isPdfEmbedded;

    /**
     * 字体颜色，引用颜色定义中的ID。如果改ID对应的颜色不存在，该设置无效
    **/
    private java.lang.String _color = "#000000";


      //----------------/
     //- Constructors -/
    //----------------/

    public Font() {
        super();
        setPdfFontName("STSong-Light");
        setPdfEncoding("UniGB-UCS2-H");
        setColor("#000000");
    } //-- cn.com.youtong.apollo.analyse.xml.Font()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteIsBold()
    {
        this._has_isBold= false;
    } //-- void deleteIsBold() 

    /**
    **/
    public void deleteIsItalic()
    {
        this._has_isItalic= false;
    } //-- void deleteIsItalic() 

    /**
    **/
    public void deleteIsPdfEmbedded()
    {
        this._has_isPdfEmbedded= false;
    } //-- void deleteIsPdfEmbedded() 

    /**
    **/
    public void deleteIsStrikeThrough()
    {
        this._has_isStrikeThrough= false;
    } //-- void deleteIsStrikeThrough() 

    /**
    **/
    public void deleteIsUnderline()
    {
        this._has_isUnderline= false;
    } //-- void deleteIsUnderline() 

    /**
    **/
    public void deleteSize()
    {
        this._has_size= false;
    } //-- void deleteSize() 

    /**
     * Returns the value of field 'color'. The field 'color' has
     * the following description:
     * 字体颜色，引用颜色定义中的ID。如果改ID对应的颜色不存在，该设置无效
     * 
     * @return the value of field 'color'.
    **/
    public java.lang.String getColor()
    {
        return this._color;
    } //-- java.lang.String getColor() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 给Font定义ID，方便其他的地方引用
     * 
     * @return the value of field 'ID'.
    **/
    public int getID()
    {
        return this._ID;
    } //-- int getID() 

    /**
     * Returns the value of field 'isBold'.
     * 
     * @return the value of field 'isBold'.
    **/
    public boolean getIsBold()
    {
        return this._isBold;
    } //-- boolean getIsBold() 

    /**
     * Returns the value of field 'isItalic'.
     * 
     * @return the value of field 'isItalic'.
    **/
    public boolean getIsItalic()
    {
        return this._isItalic;
    } //-- boolean getIsItalic() 

    /**
     * Returns the value of field 'isPdfEmbedded'.
     * 
     * @return the value of field 'isPdfEmbedded'.
    **/
    public boolean getIsPdfEmbedded()
    {
        return this._isPdfEmbedded;
    } //-- boolean getIsPdfEmbedded() 

    /**
     * Returns the value of field 'isStrikeThrough'.
     * 
     * @return the value of field 'isStrikeThrough'.
    **/
    public boolean getIsStrikeThrough()
    {
        return this._isStrikeThrough;
    } //-- boolean getIsStrikeThrough() 

    /**
     * Returns the value of field 'isUnderline'.
     * 
     * @return the value of field 'isUnderline'.
    **/
    public boolean getIsUnderline()
    {
        return this._isUnderline;
    } //-- boolean getIsUnderline() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'pdfEncoding'. The field
     * 'pdfEncoding' has the following description: 对应的PDF字体编码方式
     * 
     * @return the value of field 'pdfEncoding'.
    **/
    public java.lang.String getPdfEncoding()
    {
        return this._pdfEncoding;
    } //-- java.lang.String getPdfEncoding() 

    /**
     * Returns the value of field 'pdfFontName'. The field
     * 'pdfFontName' has the following description: 在PDF中使用的字体名称
     * 
     * @return the value of field 'pdfFontName'.
    **/
    public java.lang.String getPdfFontName()
    {
        return this._pdfFontName;
    } //-- java.lang.String getPdfFontName() 

    /**
     * Returns the value of field 'size'.
     * 
     * @return the value of field 'size'.
    **/
    public int getSize()
    {
        return this._size;
    } //-- int getSize() 

    /**
    **/
    public boolean hasID()
    {
        return this._has_ID;
    } //-- boolean hasID() 

    /**
    **/
    public boolean hasIsBold()
    {
        return this._has_isBold;
    } //-- boolean hasIsBold() 

    /**
    **/
    public boolean hasIsItalic()
    {
        return this._has_isItalic;
    } //-- boolean hasIsItalic() 

    /**
    **/
    public boolean hasIsPdfEmbedded()
    {
        return this._has_isPdfEmbedded;
    } //-- boolean hasIsPdfEmbedded() 

    /**
    **/
    public boolean hasIsStrikeThrough()
    {
        return this._has_isStrikeThrough;
    } //-- boolean hasIsStrikeThrough() 

    /**
    **/
    public boolean hasIsUnderline()
    {
        return this._has_isUnderline;
    } //-- boolean hasIsUnderline() 

    /**
    **/
    public boolean hasSize()
    {
        return this._has_size;
    } //-- boolean hasSize() 

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
     * Sets the value of field 'color'. The field 'color' has the
     * following description: 字体颜色，引用颜色定义中的ID。如果改ID对应的颜色不存在，该设置无效
     * 
     * @param color the value of field 'color'.
    **/
    public void setColor(java.lang.String color)
    {
        this._color = color;
    } //-- void setColor(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
    **/
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 给Font定义ID，方便其他的地方引用
     * 
     * @param ID the value of field 'ID'.
    **/
    public void setID(int ID)
    {
        this._ID = ID;
        this._has_ID = true;
    } //-- void setID(int) 

    /**
     * Sets the value of field 'isBold'.
     * 
     * @param isBold the value of field 'isBold'.
    **/
    public void setIsBold(boolean isBold)
    {
        this._isBold = isBold;
        this._has_isBold = true;
    } //-- void setIsBold(boolean) 

    /**
     * Sets the value of field 'isItalic'.
     * 
     * @param isItalic the value of field 'isItalic'.
    **/
    public void setIsItalic(boolean isItalic)
    {
        this._isItalic = isItalic;
        this._has_isItalic = true;
    } //-- void setIsItalic(boolean) 

    /**
     * Sets the value of field 'isPdfEmbedded'.
     * 
     * @param isPdfEmbedded the value of field 'isPdfEmbedded'.
    **/
    public void setIsPdfEmbedded(boolean isPdfEmbedded)
    {
        this._isPdfEmbedded = isPdfEmbedded;
        this._has_isPdfEmbedded = true;
    } //-- void setIsPdfEmbedded(boolean) 

    /**
     * Sets the value of field 'isStrikeThrough'.
     * 
     * @param isStrikeThrough the value of field 'isStrikeThrough'.
    **/
    public void setIsStrikeThrough(boolean isStrikeThrough)
    {
        this._isStrikeThrough = isStrikeThrough;
        this._has_isStrikeThrough = true;
    } //-- void setIsStrikeThrough(boolean) 

    /**
     * Sets the value of field 'isUnderline'.
     * 
     * @param isUnderline the value of field 'isUnderline'.
    **/
    public void setIsUnderline(boolean isUnderline)
    {
        this._isUnderline = isUnderline;
        this._has_isUnderline = true;
    } //-- void setIsUnderline(boolean) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'pdfEncoding'. The field
     * 'pdfEncoding' has the following description: 对应的PDF字体编码方式
     * 
     * @param pdfEncoding the value of field 'pdfEncoding'.
    **/
    public void setPdfEncoding(java.lang.String pdfEncoding)
    {
        this._pdfEncoding = pdfEncoding;
    } //-- void setPdfEncoding(java.lang.String) 

    /**
     * Sets the value of field 'pdfFontName'. The field
     * 'pdfFontName' has the following description: 在PDF中使用的字体名称
     * 
     * @param pdfFontName the value of field 'pdfFontName'.
    **/
    public void setPdfFontName(java.lang.String pdfFontName)
    {
        this._pdfFontName = pdfFontName;
    } //-- void setPdfFontName(java.lang.String) 

    /**
     * Sets the value of field 'size'.
     * 
     * @param size the value of field 'size'.
    **/
    public void setSize(int size)
    {
        this._size = size;
        this._has_size = true;
    } //-- void setSize(int) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Font unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Font) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Font.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Font unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
