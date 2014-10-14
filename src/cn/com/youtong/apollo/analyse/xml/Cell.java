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
public class Cell implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _colspan = 1;

    /**
     * keeps track of state for field: _colspan
    **/
    private boolean _has_colspan;

    private int _rowspan = 1;

    /**
     * keeps track of state for field: _rowspan
    **/
    private boolean _has_rowspan;

    private java.lang.String _label;

    private java.lang.String _expression;

    private java.lang.String _bgcolor = "#FFFFFF";

    /**
     * 单元格内容。可能值为string, number, date, datetime (文字，数值，日期或者时间)
    **/
    private java.lang.String _contentStyle = "string";

    /**
     * 对齐方式。可能值有： center, left, right
    **/
    private java.lang.String _halign = "center";

    /**
     * 对齐方式，可能值：top, bottom, center
    **/
    private java.lang.String _valign = "bottom";

    private int _fontID;

    /**
     * keeps track of state for field: _fontID
    **/
    private boolean _has_fontID;

    /**
     * 如果是数值，或者日期型，指定格式化方法。定义格式与excel定义一致
    **/
    private java.lang.String _formatStyle;


      //----------------/
     //- Constructors -/
    //----------------/

    public Cell() {
        super();
        setBgcolor("#FFFFFF");
        setContentStyle("string");
        setHalign("center");
        setValign("bottom");
    } //-- cn.com.youtong.apollo.analyse.xml.Cell()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteColspan()
    {
        this._has_colspan= false;
    } //-- void deleteColspan() 

    /**
    **/
    public void deleteFontID()
    {
        this._has_fontID= false;
    } //-- void deleteFontID() 

    /**
    **/
    public void deleteRowspan()
    {
        this._has_rowspan= false;
    } //-- void deleteRowspan() 

    /**
     * Returns the value of field 'bgcolor'.
     * 
     * @return the value of field 'bgcolor'.
    **/
    public java.lang.String getBgcolor()
    {
        return this._bgcolor;
    } //-- java.lang.String getBgcolor() 

    /**
     * Returns the value of field 'colspan'.
     * 
     * @return the value of field 'colspan'.
    **/
    public int getColspan()
    {
        return this._colspan;
    } //-- int getColspan() 

    /**
     * Returns the value of field 'contentStyle'. The field
     * 'contentStyle' has the following description:
     * 单元格内容。可能值为string, number, date, datetime (文字，数值，日期或者时间)
     * 
     * @return the value of field 'contentStyle'.
    **/
    public java.lang.String getContentStyle()
    {
        return this._contentStyle;
    } //-- java.lang.String getContentStyle() 

    /**
     * Returns the value of field 'expression'.
     * 
     * @return the value of field 'expression'.
    **/
    public java.lang.String getExpression()
    {
        return this._expression;
    } //-- java.lang.String getExpression() 

    /**
     * Returns the value of field 'fontID'.
     * 
     * @return the value of field 'fontID'.
    **/
    public int getFontID()
    {
        return this._fontID;
    } //-- int getFontID() 

    /**
     * Returns the value of field 'formatStyle'. The field
     * 'formatStyle' has the following description:
     * 如果是数值，或者日期型，指定格式化方法。定义格式与excel定义一致
     * 
     * @return the value of field 'formatStyle'.
    **/
    public java.lang.String getFormatStyle()
    {
        return this._formatStyle;
    } //-- java.lang.String getFormatStyle() 

    /**
     * Returns the value of field 'halign'. The field 'halign' has
     * the following description: 对齐方式。可能值有： center, left, right
     * 
     * @return the value of field 'halign'.
    **/
    public java.lang.String getHalign()
    {
        return this._halign;
    } //-- java.lang.String getHalign() 

    /**
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
    **/
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'rowspan'.
     * 
     * @return the value of field 'rowspan'.
    **/
    public int getRowspan()
    {
        return this._rowspan;
    } //-- int getRowspan() 

    /**
     * Returns the value of field 'valign'. The field 'valign' has
     * the following description: 对齐方式，可能值：top, bottom, center
     * 
     * @return the value of field 'valign'.
    **/
    public java.lang.String getValign()
    {
        return this._valign;
    } //-- java.lang.String getValign() 

    /**
    **/
    public boolean hasColspan()
    {
        return this._has_colspan;
    } //-- boolean hasColspan() 

    /**
    **/
    public boolean hasFontID()
    {
        return this._has_fontID;
    } //-- boolean hasFontID() 

    /**
    **/
    public boolean hasRowspan()
    {
        return this._has_rowspan;
    } //-- boolean hasRowspan() 

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
     * Sets the value of field 'bgcolor'.
     * 
     * @param bgcolor the value of field 'bgcolor'.
    **/
    public void setBgcolor(java.lang.String bgcolor)
    {
        this._bgcolor = bgcolor;
    } //-- void setBgcolor(java.lang.String) 

    /**
     * Sets the value of field 'colspan'.
     * 
     * @param colspan the value of field 'colspan'.
    **/
    public void setColspan(int colspan)
    {
        this._colspan = colspan;
        this._has_colspan = true;
    } //-- void setColspan(int) 

    /**
     * Sets the value of field 'contentStyle'. The field
     * 'contentStyle' has the following description:
     * 单元格内容。可能值为string, number, date, datetime (文字，数值，日期或者时间)
     * 
     * @param contentStyle the value of field 'contentStyle'.
    **/
    public void setContentStyle(java.lang.String contentStyle)
    {
        this._contentStyle = contentStyle;
    } //-- void setContentStyle(java.lang.String) 

    /**
     * Sets the value of field 'expression'.
     * 
     * @param expression the value of field 'expression'.
    **/
    public void setExpression(java.lang.String expression)
    {
        this._expression = expression;
    } //-- void setExpression(java.lang.String) 

    /**
     * Sets the value of field 'fontID'.
     * 
     * @param fontID the value of field 'fontID'.
    **/
    public void setFontID(int fontID)
    {
        this._fontID = fontID;
        this._has_fontID = true;
    } //-- void setFontID(int) 

    /**
     * Sets the value of field 'formatStyle'. The field
     * 'formatStyle' has the following description:
     * 如果是数值，或者日期型，指定格式化方法。定义格式与excel定义一致
     * 
     * @param formatStyle the value of field 'formatStyle'.
    **/
    public void setFormatStyle(java.lang.String formatStyle)
    {
        this._formatStyle = formatStyle;
    } //-- void setFormatStyle(java.lang.String) 

    /**
     * Sets the value of field 'halign'. The field 'halign' has the
     * following description: 对齐方式。可能值有： center, left, right
     * 
     * @param halign the value of field 'halign'.
    **/
    public void setHalign(java.lang.String halign)
    {
        this._halign = halign;
    } //-- void setHalign(java.lang.String) 

    /**
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
    **/
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'rowspan'.
     * 
     * @param rowspan the value of field 'rowspan'.
    **/
    public void setRowspan(int rowspan)
    {
        this._rowspan = rowspan;
        this._has_rowspan = true;
    } //-- void setRowspan(int) 

    /**
     * Sets the value of field 'valign'. The field 'valign' has the
     * following description: 对齐方式，可能值：top, bottom, center
     * 
     * @param valign the value of field 'valign'.
    **/
    public void setValign(java.lang.String valign)
    {
        this._valign = valign;
    } //-- void setValign(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Cell unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Cell) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Cell.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Cell unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
