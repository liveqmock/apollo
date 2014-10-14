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
 * ��Ԫ��
 * 
 * @version $Revision$ $Date$
**/
public class Cell implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * �������ͣ�
     * 
     * 
     * 1 -- ��ֵ�� 
     * 2 -- �ı���
     * 
     * 
     *  3 -- ������
     * 
     * 
     *  4 -- ���ı�
     * 
     * 
     *  5 -- ������
    **/
    private int _dataType;

    /**
     * keeps track of state for field: _dataType
    **/
    private boolean _has_dataType;

    /**
     * �洢�õ�Ԫ�����ݵ����ݿ��ֶ���
    **/
    private java.lang.String _DBFieldName;

    /**
     * �õ�Ԫ���������ָ�������
    **/
    private java.lang.String _scalarName;

    /**
     * ��Ԫ��ı�ǩ����A12
    **/
    private java.lang.String _label;

    /**
     * ��Ԫ�����ݵĳ���
    **/
    private int _width;

    /**
     * keeps track of state for field: _width
    **/
    private boolean _has_width;

    private java.lang.String _dictionaryID;

    /**
     * ��Ԫ���־��1����õ�Ԫ��Ϊ���ܵ�Ԫ�񣻷���Ϊ0
    **/
    private int _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    /**
     * �õ�Ԫ���Ƿ��Ǽ����ܲ����뵥Ԫ������Ϊtrue������Ϊfalse
    **/
    private boolean _isHeadquarterCodeCell;

    /**
     * keeps track of state for field: _isHeadquarterCodeCell
    **/
    private boolean _has_isHeadquarterCodeCell;

    /**
     * �õ�Ԫ���Ƿ����ϼ���λ���뵥Ԫ������Ϊtrue������Ϊfalse
    **/
    private boolean _isParentUnitCodeCell;

    /**
     * keeps track of state for field: _isParentUnitCodeCell
    **/
    private boolean _has_isParentUnitCodeCell;

    /**
     * �õ�Ԫ���Ƿ��Ǳ������͵�Ԫ������Ϊtrue������Ϊfalse
    **/
    private boolean _isReportTypeCell;

    /**
     * keeps track of state for field: _isReportTypeCell
    **/
    private boolean _has_isReportTypeCell;

    /**
     * �õ�Ԫ���Ƿ��ǵ�λ���뵥Ԫ������Ϊtrue������Ϊfalse
    **/
    private boolean _isUnitCodeCell;

    /**
     * keeps track of state for field: _isUnitCodeCell
    **/
    private boolean _has_isUnitCodeCell;

    /**
     * �õ�Ԫ���Ƿ��ǵ�λ���Ƶ�Ԫ������Ϊtrue������Ϊfalse
    **/
    private boolean _isUnitNameCell;

    /**
     * keeps track of state for field: _isUnitNameCell
    **/
    private boolean _has_isUnitNameCell;


      //----------------/
     //- Constructors -/
    //----------------/

    public Cell() {
        super();
    } //-- cn.com.youtong.apollo.task.xml.Cell()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag() 

    /**
    **/
    public void deleteIsHeadquarterCodeCell()
    {
        this._has_isHeadquarterCodeCell= false;
    } //-- void deleteIsHeadquarterCodeCell() 

    /**
    **/
    public void deleteIsParentUnitCodeCell()
    {
        this._has_isParentUnitCodeCell= false;
    } //-- void deleteIsParentUnitCodeCell() 

    /**
    **/
    public void deleteIsReportTypeCell()
    {
        this._has_isReportTypeCell= false;
    } //-- void deleteIsReportTypeCell() 

    /**
    **/
    public void deleteIsUnitCodeCell()
    {
        this._has_isUnitCodeCell= false;
    } //-- void deleteIsUnitCodeCell() 

    /**
    **/
    public void deleteIsUnitNameCell()
    {
        this._has_isUnitNameCell= false;
    } //-- void deleteIsUnitNameCell() 

    /**
    **/
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

    /**
     * Returns the value of field 'DBFieldName'. The field
     * 'DBFieldName' has the following description: �洢�õ�Ԫ�����ݵ����ݿ��ֶ���
     * 
     * @return the value of field 'DBFieldName'.
    **/
    public java.lang.String getDBFieldName()
    {
        return this._DBFieldName;
    } //-- java.lang.String getDBFieldName() 

    /**
     * Returns the value of field 'dataType'. The field 'dataType'
     * has the following description: �������ͣ�
     * 
     * 
     * 1 -- ��ֵ�� 
     * 2 -- �ı���
     * 
     * 
     *  3 -- ������
     * 
     * 
     *  4 -- ���ı�
     * 
     * 
     *  5 -- ������
     * 
     * @return the value of field 'dataType'.
    **/
    public int getDataType()
    {
        return this._dataType;
    } //-- int getDataType() 

    /**
     * Returns the value of field 'dictionaryID'.
     * 
     * @return the value of field 'dictionaryID'.
    **/
    public java.lang.String getDictionaryID()
    {
        return this._dictionaryID;
    } //-- java.lang.String getDictionaryID() 

    /**
     * Returns the value of field 'flag'. The field 'flag' has the
     * following description: ��Ԫ���־��1����õ�Ԫ��Ϊ���ܵ�Ԫ�񣻷���Ϊ0
     * 
     * @return the value of field 'flag'.
    **/
    public int getFlag()
    {
        return this._flag;
    } //-- int getFlag() 

    /**
     * Returns the value of field 'isHeadquarterCodeCell'. The
     * field 'isHeadquarterCodeCell' has the following description:
     * �õ�Ԫ���Ƿ��Ǽ����ܲ����뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @return the value of field 'isHeadquarterCodeCell'.
    **/
    public boolean getIsHeadquarterCodeCell()
    {
        return this._isHeadquarterCodeCell;
    } //-- boolean getIsHeadquarterCodeCell() 

    /**
     * Returns the value of field 'isParentUnitCodeCell'. The field
     * 'isParentUnitCodeCell' has the following description:
     * �õ�Ԫ���Ƿ����ϼ���λ���뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @return the value of field 'isParentUnitCodeCell'.
    **/
    public boolean getIsParentUnitCodeCell()
    {
        return this._isParentUnitCodeCell;
    } //-- boolean getIsParentUnitCodeCell() 

    /**
     * Returns the value of field 'isReportTypeCell'. The field
     * 'isReportTypeCell' has the following description:
     * �õ�Ԫ���Ƿ��Ǳ������͵�Ԫ������Ϊtrue������Ϊfalse
     * 
     * @return the value of field 'isReportTypeCell'.
    **/
    public boolean getIsReportTypeCell()
    {
        return this._isReportTypeCell;
    } //-- boolean getIsReportTypeCell() 

    /**
     * Returns the value of field 'isUnitCodeCell'. The field
     * 'isUnitCodeCell' has the following description:
     * �õ�Ԫ���Ƿ��ǵ�λ���뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @return the value of field 'isUnitCodeCell'.
    **/
    public boolean getIsUnitCodeCell()
    {
        return this._isUnitCodeCell;
    } //-- boolean getIsUnitCodeCell() 

    /**
     * Returns the value of field 'isUnitNameCell'. The field
     * 'isUnitNameCell' has the following description:
     * �õ�Ԫ���Ƿ��ǵ�λ���Ƶ�Ԫ������Ϊtrue������Ϊfalse
     * 
     * @return the value of field 'isUnitNameCell'.
    **/
    public boolean getIsUnitNameCell()
    {
        return this._isUnitNameCell;
    } //-- boolean getIsUnitNameCell() 

    /**
     * Returns the value of field 'label'. The field 'label' has
     * the following description: ��Ԫ��ı�ǩ����A12
     * 
     * @return the value of field 'label'.
    **/
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'scalarName'. The field
     * 'scalarName' has the following description: �õ�Ԫ���������ָ�������
     * 
     * @return the value of field 'scalarName'.
    **/
    public java.lang.String getScalarName()
    {
        return this._scalarName;
    } //-- java.lang.String getScalarName() 

    /**
     * Returns the value of field 'width'. The field 'width' has
     * the following description: ��Ԫ�����ݵĳ���
     * 
     * @return the value of field 'width'.
    **/
    public int getWidth()
    {
        return this._width;
    } //-- int getWidth() 

    /**
    **/
    public boolean hasDataType()
    {
        return this._has_dataType;
    } //-- boolean hasDataType() 

    /**
    **/
    public boolean hasFlag()
    {
        return this._has_flag;
    } //-- boolean hasFlag() 

    /**
    **/
    public boolean hasIsHeadquarterCodeCell()
    {
        return this._has_isHeadquarterCodeCell;
    } //-- boolean hasIsHeadquarterCodeCell() 

    /**
    **/
    public boolean hasIsParentUnitCodeCell()
    {
        return this._has_isParentUnitCodeCell;
    } //-- boolean hasIsParentUnitCodeCell() 

    /**
    **/
    public boolean hasIsReportTypeCell()
    {
        return this._has_isReportTypeCell;
    } //-- boolean hasIsReportTypeCell() 

    /**
    **/
    public boolean hasIsUnitCodeCell()
    {
        return this._has_isUnitCodeCell;
    } //-- boolean hasIsUnitCodeCell() 

    /**
    **/
    public boolean hasIsUnitNameCell()
    {
        return this._has_isUnitNameCell;
    } //-- boolean hasIsUnitNameCell() 

    /**
    **/
    public boolean hasWidth()
    {
        return this._has_width;
    } //-- boolean hasWidth() 

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
     * Sets the value of field 'DBFieldName'. The field
     * 'DBFieldName' has the following description: �洢�õ�Ԫ�����ݵ����ݿ��ֶ���
     * 
     * @param DBFieldName the value of field 'DBFieldName'.
    **/
    public void setDBFieldName(java.lang.String DBFieldName)
    {
        this._DBFieldName = DBFieldName;
    } //-- void setDBFieldName(java.lang.String) 

    /**
     * Sets the value of field 'dataType'. The field 'dataType' has
     * the following description: �������ͣ�
     * 
     * 
     * 1 -- ��ֵ�� 
     * 2 -- �ı���
     * 
     * 
     *  3 -- ������
     * 
     * 
     *  4 -- ���ı�
     * 
     * 
     *  5 -- ������
     * 
     * @param dataType the value of field 'dataType'.
    **/
    public void setDataType(int dataType)
    {
        this._dataType = dataType;
        this._has_dataType = true;
    } //-- void setDataType(int) 

    /**
     * Sets the value of field 'dictionaryID'.
     * 
     * @param dictionaryID the value of field 'dictionaryID'.
    **/
    public void setDictionaryID(java.lang.String dictionaryID)
    {
        this._dictionaryID = dictionaryID;
    } //-- void setDictionaryID(java.lang.String) 

    /**
     * Sets the value of field 'flag'. The field 'flag' has the
     * following description: ��Ԫ���־��1����õ�Ԫ��Ϊ���ܵ�Ԫ�񣻷���Ϊ0
     * 
     * @param flag the value of field 'flag'.
    **/
    public void setFlag(int flag)
    {
        this._flag = flag;
        this._has_flag = true;
    } //-- void setFlag(int) 

    /**
     * Sets the value of field 'isHeadquarterCodeCell'. The field
     * 'isHeadquarterCodeCell' has the following description:
     * �õ�Ԫ���Ƿ��Ǽ����ܲ����뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @param isHeadquarterCodeCell the value of field
     * 'isHeadquarterCodeCell'.
    **/
    public void setIsHeadquarterCodeCell(boolean isHeadquarterCodeCell)
    {
        this._isHeadquarterCodeCell = isHeadquarterCodeCell;
        this._has_isHeadquarterCodeCell = true;
    } //-- void setIsHeadquarterCodeCell(boolean) 

    /**
     * Sets the value of field 'isParentUnitCodeCell'. The field
     * 'isParentUnitCodeCell' has the following description:
     * �õ�Ԫ���Ƿ����ϼ���λ���뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @param isParentUnitCodeCell the value of field
     * 'isParentUnitCodeCell'.
    **/
    public void setIsParentUnitCodeCell(boolean isParentUnitCodeCell)
    {
        this._isParentUnitCodeCell = isParentUnitCodeCell;
        this._has_isParentUnitCodeCell = true;
    } //-- void setIsParentUnitCodeCell(boolean) 

    /**
     * Sets the value of field 'isReportTypeCell'. The field
     * 'isReportTypeCell' has the following description:
     * �õ�Ԫ���Ƿ��Ǳ������͵�Ԫ������Ϊtrue������Ϊfalse
     * 
     * @param isReportTypeCell the value of field 'isReportTypeCell'
    **/
    public void setIsReportTypeCell(boolean isReportTypeCell)
    {
        this._isReportTypeCell = isReportTypeCell;
        this._has_isReportTypeCell = true;
    } //-- void setIsReportTypeCell(boolean) 

    /**
     * Sets the value of field 'isUnitCodeCell'. The field
     * 'isUnitCodeCell' has the following description:
     * �õ�Ԫ���Ƿ��ǵ�λ���뵥Ԫ������Ϊtrue������Ϊfalse
     * 
     * @param isUnitCodeCell the value of field 'isUnitCodeCell'.
    **/
    public void setIsUnitCodeCell(boolean isUnitCodeCell)
    {
        this._isUnitCodeCell = isUnitCodeCell;
        this._has_isUnitCodeCell = true;
    } //-- void setIsUnitCodeCell(boolean) 

    /**
     * Sets the value of field 'isUnitNameCell'. The field
     * 'isUnitNameCell' has the following description:
     * �õ�Ԫ���Ƿ��ǵ�λ���Ƶ�Ԫ������Ϊtrue������Ϊfalse
     * 
     * @param isUnitNameCell the value of field 'isUnitNameCell'.
    **/
    public void setIsUnitNameCell(boolean isUnitNameCell)
    {
        this._isUnitNameCell = isUnitNameCell;
        this._has_isUnitNameCell = true;
    } //-- void setIsUnitNameCell(boolean) 

    /**
     * Sets the value of field 'label'. The field 'label' has the
     * following description: ��Ԫ��ı�ǩ����A12
     * 
     * @param label the value of field 'label'.
    **/
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'scalarName'. The field 'scalarName'
     * has the following description: �õ�Ԫ���������ָ�������
     * 
     * @param scalarName the value of field 'scalarName'.
    **/
    public void setScalarName(java.lang.String scalarName)
    {
        this._scalarName = scalarName;
    } //-- void setScalarName(java.lang.String) 

    /**
     * Sets the value of field 'width'. The field 'width' has the
     * following description: ��Ԫ�����ݵĳ���
     * 
     * @param width the value of field 'width'.
    **/
    public void setWidth(int width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(int) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.Cell unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.Cell) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.Cell.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.Cell unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
