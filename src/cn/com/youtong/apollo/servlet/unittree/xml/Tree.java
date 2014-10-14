/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.servlet.unittree.xml;

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
public class Tree implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _text;

    private java.lang.String _src;

    private java.lang.String _action;

    private java.lang.String _icon;

    private java.lang.String _openIcon;

    private java.lang.String _target;

    /**
     * checkbox或radio button是否被选中
    **/
    private boolean _checked;

    /**
     * keeps track of state for field: _checked
    **/
    private boolean _has_checked;

    /**
     * checkbox或radio button的值
    **/
    private java.lang.String _checkValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public Tree() {
        super();
    } //-- cn.com.youtong.apollo.servlet.unittree.xml.Tree()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteChecked()
    {
        this._has_checked= false;
    } //-- void deleteChecked() 

    /**
     * Returns the value of field 'action'.
     * 
     * @return the value of field 'action'.
    **/
    public java.lang.String getAction()
    {
        return this._action;
    } //-- java.lang.String getAction() 

    /**
     * Returns the value of field 'checkValue'. The field
     * 'checkValue' has the following description: checkbox或radio
     * button的值
     * 
     * @return the value of field 'checkValue'.
    **/
    public java.lang.String getCheckValue()
    {
        return this._checkValue;
    } //-- java.lang.String getCheckValue() 

    /**
     * Returns the value of field 'checked'. The field 'checked'
     * has the following description: checkbox或radio button是否被选中
     * 
     * @return the value of field 'checked'.
    **/
    public boolean getChecked()
    {
        return this._checked;
    } //-- boolean getChecked() 

    /**
     * Returns the value of field 'icon'.
     * 
     * @return the value of field 'icon'.
    **/
    public java.lang.String getIcon()
    {
        return this._icon;
    } //-- java.lang.String getIcon() 

    /**
     * Returns the value of field 'openIcon'.
     * 
     * @return the value of field 'openIcon'.
    **/
    public java.lang.String getOpenIcon()
    {
        return this._openIcon;
    } //-- java.lang.String getOpenIcon() 

    /**
     * Returns the value of field 'src'.
     * 
     * @return the value of field 'src'.
    **/
    public java.lang.String getSrc()
    {
        return this._src;
    } //-- java.lang.String getSrc() 

    /**
     * Returns the value of field 'target'.
     * 
     * @return the value of field 'target'.
    **/
    public java.lang.String getTarget()
    {
        return this._target;
    } //-- java.lang.String getTarget() 

    /**
     * Returns the value of field 'text'.
     * 
     * @return the value of field 'text'.
    **/
    public java.lang.String getText()
    {
        return this._text;
    } //-- java.lang.String getText() 

    /**
    **/
    public boolean hasChecked()
    {
        return this._has_checked;
    } //-- boolean hasChecked() 

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
     * Sets the value of field 'action'.
     * 
     * @param action the value of field 'action'.
    **/
    public void setAction(java.lang.String action)
    {
        this._action = action;
    } //-- void setAction(java.lang.String) 

    /**
     * Sets the value of field 'checkValue'. The field 'checkValue'
     * has the following description: checkbox或radio button的值
     * 
     * @param checkValue the value of field 'checkValue'.
    **/
    public void setCheckValue(java.lang.String checkValue)
    {
        this._checkValue = checkValue;
    } //-- void setCheckValue(java.lang.String) 

    /**
     * Sets the value of field 'checked'. The field 'checked' has
     * the following description: checkbox或radio button是否被选中
     * 
     * @param checked the value of field 'checked'.
    **/
    public void setChecked(boolean checked)
    {
        this._checked = checked;
        this._has_checked = true;
    } //-- void setChecked(boolean) 

    /**
     * Sets the value of field 'icon'.
     * 
     * @param icon the value of field 'icon'.
    **/
    public void setIcon(java.lang.String icon)
    {
        this._icon = icon;
    } //-- void setIcon(java.lang.String) 

    /**
     * Sets the value of field 'openIcon'.
     * 
     * @param openIcon the value of field 'openIcon'.
    **/
    public void setOpenIcon(java.lang.String openIcon)
    {
        this._openIcon = openIcon;
    } //-- void setOpenIcon(java.lang.String) 

    /**
     * Sets the value of field 'src'.
     * 
     * @param src the value of field 'src'.
    **/
    public void setSrc(java.lang.String src)
    {
        this._src = src;
    } //-- void setSrc(java.lang.String) 

    /**
     * Sets the value of field 'target'.
     * 
     * @param target the value of field 'target'.
    **/
    public void setTarget(java.lang.String target)
    {
        this._target = target;
    } //-- void setTarget(java.lang.String) 

    /**
     * Sets the value of field 'text'.
     * 
     * @param text the value of field 'text'.
    **/
    public void setText(java.lang.String text)
    {
        this._text = text;
    } //-- void setText(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.servlet.unittree.xml.Tree unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.servlet.unittree.xml.Tree) Unmarshaller.unmarshal(cn.com.youtong.apollo.servlet.unittree.xml.Tree.class, reader);
    } //-- cn.com.youtong.apollo.servlet.unittree.xml.Tree unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
