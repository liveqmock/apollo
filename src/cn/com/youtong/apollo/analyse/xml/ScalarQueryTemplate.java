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
 * 指标查模板
 *
 * @version $Revision$ $Date$
**/
public class ScalarQueryTemplate implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 模板名称
    **/
    private java.lang.String _name;

    /**
     * 任务ID
    **/
    private java.lang.String _taskID;

    /**
     * 打印方向。Horizontal表示水平打印；Vertical竖直打印。
    **/
    private java.lang.String _printOrder = "Horizontal";

    private Fonts _fonts;

    private ColWidth _colWidth;

    private RowHeight _rowHeight;

    /**
     * 表头
    **/
    private Head _head;

    /**
     * 显示主体部分
    **/
    private Body _body;

    /**
     * 打印信息
    **/
    private PrintInformation _printInformation;


      //----------------/
     //- Constructors -/
    //----------------/

    public ScalarQueryTemplate() {
        super();
        setPrintOrder("Horizontal");
    } //-- cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'body'. The field 'body' has the
     * following description: 显示主体部分
     *
     * @return the value of field 'body'.
    **/
    public Body getBody()
    {
        return this._body;
    } //-- Body getBody()

    /**
     * Returns the value of field 'colWidth'.
     *
     * @return the value of field 'colWidth'.
    **/
    public ColWidth getColWidth()
    {
        return this._colWidth;
    } //-- ColWidth getColWidth()

    /**
     * Returns the value of field 'fonts'.
     *
     * @return the value of field 'fonts'.
    **/
    public Fonts getFonts()
    {
        return this._fonts;
    } //-- Fonts getFonts()

    /**
     * Returns the value of field 'head'. The field 'head' has the
     * following description: 表头
     *
     * @return the value of field 'head'.
    **/
    public Head getHead()
    {
        return this._head;
    } //-- Head getHead()

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: 模板名称
     *
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName()

    /**
     * Returns the value of field 'printInformation'. The field
     * 'printInformation' has the following description: 打印信息
     *
     * @return the value of field 'printInformation'.
    **/
    public PrintInformation getPrintInformation()
    {
        return this._printInformation;
    } //-- PrintInformation getPrintInformation()

    /**
     * Returns the value of field 'printOrder'. The field
     * 'printOrder' has the following description:
     * 打印方向。Horizontal表示水平打印；Vertical竖直打印。
     *
     * @return the value of field 'printOrder'.
    **/
    public java.lang.String getPrintOrder()
    {
        return this._printOrder;
    } //-- java.lang.String getPrintOrder()

    /**
     * Returns the value of field 'rowHeight'.
     *
     * @return the value of field 'rowHeight'.
    **/
    public RowHeight getRowHeight()
    {
        return this._rowHeight;
    } //-- RowHeight getRowHeight()

    /**
     * Returns the value of field 'taskID'. The field 'taskID' has
     * the following description: 任务ID
     *
     * @return the value of field 'taskID'.
    **/
    public java.lang.String getTaskID()
    {
        return this._taskID;
    } //-- java.lang.String getTaskID()

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
		try
		{
			Marshaller marshaller = new Marshaller( out );
			marshaller.setEncoding( "gb2312" );
			marshaller.marshal( this );
		} catch( IOException ex )
		{
			throw new MarshalException( ex );
		}
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
     * Sets the value of field 'body'. The field 'body' has the
     * following description: 显示主体部分
     *
     * @param body the value of field 'body'.
    **/
    public void setBody(Body body)
    {
        this._body = body;
    } //-- void setBody(Body)

    /**
     * Sets the value of field 'colWidth'.
     *
     * @param colWidth the value of field 'colWidth'.
    **/
    public void setColWidth(ColWidth colWidth)
    {
        this._colWidth = colWidth;
    } //-- void setColWidth(ColWidth)

    /**
     * Sets the value of field 'fonts'.
     *
     * @param fonts the value of field 'fonts'.
    **/
    public void setFonts(Fonts fonts)
    {
        this._fonts = fonts;
    } //-- void setFonts(Fonts)

    /**
     * Sets the value of field 'head'. The field 'head' has the
     * following description: 表头
     *
     * @param head the value of field 'head'.
    **/
    public void setHead(Head head)
    {
        this._head = head;
    } //-- void setHead(Head)

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: 模板名称
     *
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String)

    /**
     * Sets the value of field 'printInformation'. The field
     * 'printInformation' has the following description: 打印信息
     *
     * @param printInformation the value of field 'printInformation'
    **/
    public void setPrintInformation(PrintInformation printInformation)
    {
        this._printInformation = printInformation;
    } //-- void setPrintInformation(PrintInformation)

    /**
     * Sets the value of field 'printOrder'. The field 'printOrder'
     * has the following description:
     * 打印方向。Horizontal表示水平打印；Vertical竖直打印。
     *
     * @param printOrder the value of field 'printOrder'.
    **/
    public void setPrintOrder(java.lang.String printOrder)
    {
        this._printOrder = printOrder;
    } //-- void setPrintOrder(java.lang.String)

    /**
     * Sets the value of field 'rowHeight'.
     *
     * @param rowHeight the value of field 'rowHeight'.
    **/
    public void setRowHeight(RowHeight rowHeight)
    {
        this._rowHeight = rowHeight;
    } //-- void setRowHeight(RowHeight)

    /**
     * Sets the value of field 'taskID'. The field 'taskID' has the
     * following description: 任务ID
     *
     * @param taskID the value of field 'taskID'.
    **/
    public void setTaskID(java.lang.String taskID)
    {
        this._taskID = taskID;
    } //-- void setTaskID(java.lang.String)

    /**
     *
     *
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
