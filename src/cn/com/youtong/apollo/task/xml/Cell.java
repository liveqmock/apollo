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
 * 单元格
 * 
 * @version $Revision$ $Date$
**/
public class Cell implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 数据类型：
     * 
     * 
     * 1 -- 数值型 
     * 2 -- 文本型
     * 
     * 
     *  3 -- 二进制
     * 
     * 
     *  4 -- 大文本
     * 
     * 
     *  5 -- 日期型
    **/
    private int _dataType;

    /**
     * keeps track of state for field: _dataType
    **/
    private boolean _has_dataType;

    /**
     * 存储该单元格数据的数据库字段名
    **/
    private java.lang.String _DBFieldName;

    /**
     * 该单元格所代表的指标的名称
    **/
    private java.lang.String _scalarName;

    /**
     * 单元格的标签，如A12
    **/
    private java.lang.String _label;

    /**
     * 单元个内容的长度
    **/
    private int _width;

    /**
     * keeps track of state for field: _width
    **/
    private boolean _has_width;

    private java.lang.String _dictionaryID;

    /**
     * 单元格标志，1代表该单元格为汇总单元格；否则为0
    **/
    private int _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    /**
     * 该单元格是否是集团总部代码单元格：是则为true；否则为false
    **/
    private boolean _isHeadquarterCodeCell;

    /**
     * keeps track of state for field: _isHeadquarterCodeCell
    **/
    private boolean _has_isHeadquarterCodeCell;

    /**
     * 该单元格是否是上级单位代码单元格：是则为true；否则为false
    **/
    private boolean _isParentUnitCodeCell;

    /**
     * keeps track of state for field: _isParentUnitCodeCell
    **/
    private boolean _has_isParentUnitCodeCell;

    /**
     * 该单元格是否是报表类型单元格：是则为true；否则为false
    **/
    private boolean _isReportTypeCell;

    /**
     * keeps track of state for field: _isReportTypeCell
    **/
    private boolean _has_isReportTypeCell;

    /**
     * 该单元格是否是单位代码单元格：是则为true；否则为false
    **/
    private boolean _isUnitCodeCell;

    /**
     * keeps track of state for field: _isUnitCodeCell
    **/
    private boolean _has_isUnitCodeCell;

    /**
     * 该单元格是否是单位名称单元格：是则为true；否则为false
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
     * 'DBFieldName' has the following description: 存储该单元格数据的数据库字段名
     * 
     * @return the value of field 'DBFieldName'.
    **/
    public java.lang.String getDBFieldName()
    {
        return this._DBFieldName;
    } //-- java.lang.String getDBFieldName() 

    /**
     * Returns the value of field 'dataType'. The field 'dataType'
     * has the following description: 数据类型：
     * 
     * 
     * 1 -- 数值型 
     * 2 -- 文本型
     * 
     * 
     *  3 -- 二进制
     * 
     * 
     *  4 -- 大文本
     * 
     * 
     *  5 -- 日期型
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
     * following description: 单元格标志，1代表该单元格为汇总单元格；否则为0
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
     * 该单元格是否是集团总部代码单元格：是则为true；否则为false
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
     * 该单元格是否是上级单位代码单元格：是则为true；否则为false
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
     * 该单元格是否是报表类型单元格：是则为true；否则为false
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
     * 该单元格是否是单位代码单元格：是则为true；否则为false
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
     * 该单元格是否是单位名称单元格：是则为true；否则为false
     * 
     * @return the value of field 'isUnitNameCell'.
    **/
    public boolean getIsUnitNameCell()
    {
        return this._isUnitNameCell;
    } //-- boolean getIsUnitNameCell() 

    /**
     * Returns the value of field 'label'. The field 'label' has
     * the following description: 单元格的标签，如A12
     * 
     * @return the value of field 'label'.
    **/
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'scalarName'. The field
     * 'scalarName' has the following description: 该单元格所代表的指标的名称
     * 
     * @return the value of field 'scalarName'.
    **/
    public java.lang.String getScalarName()
    {
        return this._scalarName;
    } //-- java.lang.String getScalarName() 

    /**
     * Returns the value of field 'width'. The field 'width' has
     * the following description: 单元个内容的长度
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
     * 'DBFieldName' has the following description: 存储该单元格数据的数据库字段名
     * 
     * @param DBFieldName the value of field 'DBFieldName'.
    **/
    public void setDBFieldName(java.lang.String DBFieldName)
    {
        this._DBFieldName = DBFieldName;
    } //-- void setDBFieldName(java.lang.String) 

    /**
     * Sets the value of field 'dataType'. The field 'dataType' has
     * the following description: 数据类型：
     * 
     * 
     * 1 -- 数值型 
     * 2 -- 文本型
     * 
     * 
     *  3 -- 二进制
     * 
     * 
     *  4 -- 大文本
     * 
     * 
     *  5 -- 日期型
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
     * following description: 单元格标志，1代表该单元格为汇总单元格；否则为0
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
     * 该单元格是否是集团总部代码单元格：是则为true；否则为false
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
     * 该单元格是否是上级单位代码单元格：是则为true；否则为false
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
     * 该单元格是否是报表类型单元格：是则为true；否则为false
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
     * 该单元格是否是单位代码单元格：是则为true；否则为false
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
     * 该单元格是否是单位名称单元格：是则为true；否则为false
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
     * following description: 单元格的标签，如A12
     * 
     * @param label the value of field 'label'.
    **/
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'scalarName'. The field 'scalarName'
     * has the following description: 该单元格所代表的指标的名称
     * 
     * @param scalarName the value of field 'scalarName'.
    **/
    public void setScalarName(java.lang.String scalarName)
    {
        this._scalarName = scalarName;
    } //-- void setScalarName(java.lang.String) 

    /**
     * Sets the value of field 'width'. The field 'width' has the
     * following description: 单元个内容的长度
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
