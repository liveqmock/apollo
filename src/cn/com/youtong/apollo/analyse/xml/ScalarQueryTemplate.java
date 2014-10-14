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
 * ָ���ģ��
 *
 * @version $Revision$ $Date$
**/
public class ScalarQueryTemplate implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * ģ������
    **/
    private java.lang.String _name;

    /**
     * ����ID
    **/
    private java.lang.String _taskID;

    /**
     * ��ӡ����Horizontal��ʾˮƽ��ӡ��Vertical��ֱ��ӡ��
    **/
    private java.lang.String _printOrder = "Horizontal";

    private Fonts _fonts;

    private ColWidth _colWidth;

    private RowHeight _rowHeight;

    /**
     * ��ͷ
    **/
    private Head _head;

    /**
     * ��ʾ���岿��
    **/
    private Body _body;

    /**
     * ��ӡ��Ϣ
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
     * following description: ��ʾ���岿��
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
     * following description: ��ͷ
     *
     * @return the value of field 'head'.
    **/
    public Head getHead()
    {
        return this._head;
    } //-- Head getHead()

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: ģ������
     *
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName()

    /**
     * Returns the value of field 'printInformation'. The field
     * 'printInformation' has the following description: ��ӡ��Ϣ
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
     * ��ӡ����Horizontal��ʾˮƽ��ӡ��Vertical��ֱ��ӡ��
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
     * the following description: ����ID
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
     * following description: ��ʾ���岿��
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
     * following description: ��ͷ
     *
     * @param head the value of field 'head'.
    **/
    public void setHead(Head head)
    {
        this._head = head;
    } //-- void setHead(Head)

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: ģ������
     *
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String)

    /**
     * Sets the value of field 'printInformation'. The field
     * 'printInformation' has the following description: ��ӡ��Ϣ
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
     * ��ӡ����Horizontal��ʾˮƽ��ӡ��Vertical��ֱ��ӡ��
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
     * following description: ����ID
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
