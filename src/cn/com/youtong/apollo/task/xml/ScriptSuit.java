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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 脚本组
 *
 * @version $Revision$ $Date$
**/
public class ScriptSuit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 脚本组名称，区分同一任务的不同脚本组
    **/
    private java.lang.String _name;

    /**
     * 备注
    **/
    private java.lang.String _memo;

    /**
     * 该套脚本最后修改日期
    **/
    private java.util.Date _dateModified;

    /**
     * 公共脚本，定义其他脚本都可以使用的函数
    **/
    private java.lang.String _common;

    /**
     * 表内运算脚本
    **/
    private java.util.ArrayList _calculateInTableList;

    /**
     * 表内审核脚本
    **/
    private java.util.ArrayList _auditInTableList;

    /**
     * 表间运算脚本
    **/
    private CalculateCrossTable _calculateCrossTable;

    /**
     * 表间审核脚本
    **/
    private AuditCrossTable _auditCrossTable;

    /**
     * 脚本的执行顺序
    **/
    private Sequence _sequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public ScriptSuit() {
        super();
        _calculateInTableList = new ArrayList();
        _auditInTableList = new ArrayList();
    } //-- cn.com.youtong.apollo.task.xml.ScriptSuit()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vAuditInTable
    **/
    public void addAuditInTable(AuditInTable vAuditInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _auditInTableList.add(vAuditInTable);
    } //-- void addAuditInTable(AuditInTable)

    /**
     *
     *
     * @param index
     * @param vAuditInTable
    **/
    public void addAuditInTable(int index, AuditInTable vAuditInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _auditInTableList.add(index, vAuditInTable);
    } //-- void addAuditInTable(int, AuditInTable)

    /**
     *
     *
     * @param vCalculateInTable
    **/
    public void addCalculateInTable(CalculateInTable vCalculateInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _calculateInTableList.add(vCalculateInTable);
    } //-- void addCalculateInTable(CalculateInTable)

    /**
     *
     *
     * @param index
     * @param vCalculateInTable
    **/
    public void addCalculateInTable(int index, CalculateInTable vCalculateInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _calculateInTableList.add(index, vCalculateInTable);
    } //-- void addCalculateInTable(int, CalculateInTable)

    /**
    **/
    public void clearAuditInTable()
    {
        _auditInTableList.clear();
    } //-- void clearAuditInTable()

    /**
    **/
    public void clearCalculateInTable()
    {
        _calculateInTableList.clear();
    } //-- void clearCalculateInTable()

    /**
    **/
    public java.util.Enumeration enumerateAuditInTable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_auditInTableList.iterator());
    } //-- java.util.Enumeration enumerateAuditInTable()

    /**
    **/
    public java.util.Enumeration enumerateCalculateInTable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_calculateInTableList.iterator());
    } //-- java.util.Enumeration enumerateCalculateInTable()

    /**
     * Returns the value of field 'auditCrossTable'. The field
     * 'auditCrossTable' has the following description: 表间审核脚本
     *
     * @return the value of field 'auditCrossTable'.
    **/
    public AuditCrossTable getAuditCrossTable()
    {
        return this._auditCrossTable;
    } //-- AuditCrossTable getAuditCrossTable()

    /**
     *
     *
     * @param index
    **/
    public AuditInTable getAuditInTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _auditInTableList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (AuditInTable) _auditInTableList.get(index);
    } //-- AuditInTable getAuditInTable(int)

    /**
    **/
    public AuditInTable[] getAuditInTable()
    {
        int size = _auditInTableList.size();
        AuditInTable[] mArray = new AuditInTable[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (AuditInTable) _auditInTableList.get(index);
        }
        return mArray;
    } //-- AuditInTable[] getAuditInTable()

    /**
    **/
    public int getAuditInTableCount()
    {
        return _auditInTableList.size();
    } //-- int getAuditInTableCount()

    /**
     * Returns the value of field 'calculateCrossTable'. The field
     * 'calculateCrossTable' has the following description: 表间运算脚本
     *
     * @return the value of field 'calculateCrossTable'.
    **/
    public CalculateCrossTable getCalculateCrossTable()
    {
        return this._calculateCrossTable;
    } //-- CalculateCrossTable getCalculateCrossTable()

    /**
     *
     *
     * @param index
    **/
    public CalculateInTable getCalculateInTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _calculateInTableList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (CalculateInTable) _calculateInTableList.get(index);
    } //-- CalculateInTable getCalculateInTable(int)

    /**
    **/
    public CalculateInTable[] getCalculateInTable()
    {
        int size = _calculateInTableList.size();
        CalculateInTable[] mArray = new CalculateInTable[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CalculateInTable) _calculateInTableList.get(index);
        }
        return mArray;
    } //-- CalculateInTable[] getCalculateInTable()

    /**
    **/
    public int getCalculateInTableCount()
    {
        return _calculateInTableList.size();
    } //-- int getCalculateInTableCount()

    /**
     * Returns the value of field 'common'. The field 'common' has
     * the following description: 公共脚本，定义其他脚本都可以使用的函数
     *
     * @return the value of field 'common'.
    **/
    public java.lang.String getCommon()
    {
        return this._common;
    } //-- java.lang.String getCommon()

    /**
     * Returns the value of field 'dateModified'. The field
     * 'dateModified' has the following description: 该套脚本最后修改日期
     *
     * @return the value of field 'dateModified'.
    **/
    public java.util.Date getDateModified()
    {
        return this._dateModified;
    } //-- java.util.Date getDateModified()

    /**
     * Returns the value of field 'memo'. The field 'memo' has the
     * following description: 备注
     *
     * @return the value of field 'memo'.
    **/
    public java.lang.String getMemo()
    {
        return this._memo;
    } //-- java.lang.String getMemo()

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: 脚本组名称，区分同一任务的不同脚本组
     *
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName()

    /**
     * Returns the value of field 'sequence'. The field 'sequence'
     * has the following description: 脚本的执行顺序
     *
     * @return the value of field 'sequence'.
    **/
    public Sequence getSequence()
    {
        return this._sequence;
    } //-- Sequence getSequence()

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
			Marshaller marshaller = new Marshaller(out);
			marshaller.setEncoding("gb2312");
			marshaller.marshal(this);
		}
		catch (IOException ex)
		{
			throw new MarshalException(ex);
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
     *
     *
     * @param vAuditInTable
    **/
    public boolean removeAuditInTable(AuditInTable vAuditInTable)
    {
        boolean removed = _auditInTableList.remove(vAuditInTable);
        return removed;
    } //-- boolean removeAuditInTable(AuditInTable)

    /**
     *
     *
     * @param vCalculateInTable
    **/
    public boolean removeCalculateInTable(CalculateInTable vCalculateInTable)
    {
        boolean removed = _calculateInTableList.remove(vCalculateInTable);
        return removed;
    } //-- boolean removeCalculateInTable(CalculateInTable)

    /**
     * Sets the value of field 'auditCrossTable'. The field
     * 'auditCrossTable' has the following description: 表间审核脚本
     *
     * @param auditCrossTable the value of field 'auditCrossTable'.
    **/
    public void setAuditCrossTable(AuditCrossTable auditCrossTable)
    {
        this._auditCrossTable = auditCrossTable;
    } //-- void setAuditCrossTable(AuditCrossTable)

    /**
     *
     *
     * @param index
     * @param vAuditInTable
    **/
    public void setAuditInTable(int index, AuditInTable vAuditInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _auditInTableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _auditInTableList.set(index, vAuditInTable);
    } //-- void setAuditInTable(int, AuditInTable)

    /**
     *
     *
     * @param auditInTableArray
    **/
    public void setAuditInTable(AuditInTable[] auditInTableArray)
    {
        //-- copy array
        _auditInTableList.clear();
        for (int i = 0; i < auditInTableArray.length; i++) {
            _auditInTableList.add(auditInTableArray[i]);
        }
    } //-- void setAuditInTable(AuditInTable)

    /**
     * Sets the value of field 'calculateCrossTable'. The field
     * 'calculateCrossTable' has the following description: 表间运算脚本
     *
     * @param calculateCrossTable the value of field
     * 'calculateCrossTable'.
    **/
    public void setCalculateCrossTable(CalculateCrossTable calculateCrossTable)
    {
        this._calculateCrossTable = calculateCrossTable;
    } //-- void setCalculateCrossTable(CalculateCrossTable)

    /**
     *
     *
     * @param index
     * @param vCalculateInTable
    **/
    public void setCalculateInTable(int index, CalculateInTable vCalculateInTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _calculateInTableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _calculateInTableList.set(index, vCalculateInTable);
    } //-- void setCalculateInTable(int, CalculateInTable)

    /**
     *
     *
     * @param calculateInTableArray
    **/
    public void setCalculateInTable(CalculateInTable[] calculateInTableArray)
    {
        //-- copy array
        _calculateInTableList.clear();
        for (int i = 0; i < calculateInTableArray.length; i++) {
            _calculateInTableList.add(calculateInTableArray[i]);
        }
    } //-- void setCalculateInTable(CalculateInTable)

    /**
     * Sets the value of field 'common'. The field 'common' has the
     * following description: 公共脚本，定义其他脚本都可以使用的函数
     *
     * @param common the value of field 'common'.
    **/
    public void setCommon(java.lang.String common)
    {
        this._common = common;
    } //-- void setCommon(java.lang.String)

    /**
     * Sets the value of field 'dateModified'. The field
     * 'dateModified' has the following description: 该套脚本最后修改日期
     *
     * @param dateModified the value of field 'dateModified'.
    **/
    public void setDateModified(java.util.Date dateModified)
    {
        this._dateModified = dateModified;
    } //-- void setDateModified(java.util.Date)

    /**
     * Sets the value of field 'memo'. The field 'memo' has the
     * following description: 备注
     *
     * @param memo the value of field 'memo'.
    **/
    public void setMemo(java.lang.String memo)
    {
        this._memo = memo;
    } //-- void setMemo(java.lang.String)

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: 脚本组名称，区分同一任务的不同脚本组
     *
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String)

    /**
     * Sets the value of field 'sequence'. The field 'sequence' has
     * the following description: 脚本的执行顺序
     *
     * @param sequence the value of field 'sequence'.
    **/
    public void setSequence(Sequence sequence)
    {
        this._sequence = sequence;
    } //-- void setSequence(Sequence)

    /**
     *
     *
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.ScriptSuit unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.ScriptSuit) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.ScriptSuit.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.ScriptSuit unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
