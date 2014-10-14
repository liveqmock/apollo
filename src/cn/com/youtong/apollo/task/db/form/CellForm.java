package cn.com.youtong.apollo.task.db.form;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class CellForm
	implements Serializable
{

	/** identifier field */
	private Integer cellID;

	/** persistent field */
	private int dataType;

	/** persistent field */
	private String dbfieldName;

	/** nullable persistent field */
	private String scalarName;

	/** persistent field */
	private int flag;

	/** persistent field */
	private String label;

	/** nullable persistent field */
	private int width;

	/** nullable persistent field */
	private String dictionaryID;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** persistent field */
	private cn.com.youtong.apollo.task.db.form.RowForm row;

	/** full constructor */
	public CellForm(java.lang.Integer cellID, int dataType, java.lang.String dbfieldName, java.lang.String scalarName, int flag, java.lang.String label, int width, java.lang.String dictionaryID,
					java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, cn.com.youtong.apollo.task.db.form.RowForm row)
	{
		this.cellID = cellID;
		this.dataType = dataType;
		this.dbfieldName = dbfieldName;
		this.scalarName = scalarName;
		this.flag = flag;
		this.label = label;
		this.width = width;
		this.dictionaryID = dictionaryID;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.row = row;
	}

	/** default constructor */
	public CellForm()
	{
	}

	/** minimal constructor */
	public CellForm(java.lang.Integer cellID, int dataType, java.lang.String dbfieldName, int flag, java.lang.String label, cn.com.youtong.apollo.task.db.form.RowForm row)
	{
		this.cellID = cellID;
		this.dataType = dataType;
		this.dbfieldName = dbfieldName;
		this.flag = flag;
		this.label = label;
		this.row = row;
	}

	public java.lang.Integer getCellID()
	{
		return this.cellID;
	}

	public void setCellID(java.lang.Integer cellID)
	{
		this.cellID = cellID;
	}

	public int getDataType()
	{
		return this.dataType;
	}

	public void setDataType(int dataType)
	{
		this.dataType = dataType;
	}

	public java.lang.String getDbfieldName()
	{
		return this.dbfieldName;
	}

	public void setDbfieldName(java.lang.String dbfieldName)
	{
		this.dbfieldName = dbfieldName;
	}

	public java.lang.String getScalarName()
	{
		return this.scalarName;
	}

	public void setScalarName(java.lang.String scalarName)
	{
		this.scalarName = scalarName;
	}

	public int getFlag()
	{
		return this.flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public java.lang.String getLabel()
	{
		return this.label;
	}

	public void setLabel(java.lang.String label)
	{
		this.label = label;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public java.lang.String getDictionaryID()
	{
		return this.dictionaryID;
	}

	public void setDictionaryID(java.lang.String dictionaryID)
	{
		this.dictionaryID = dictionaryID;
	}

	public java.lang.String getReserved1()
	{
		return this.reserved1;
	}

	public void setReserved1(java.lang.String reserved1)
	{
		this.reserved1 = reserved1;
	}

	public java.lang.String getReserved2()
	{
		return this.reserved2;
	}

	public void setReserved2(java.lang.String reserved2)
	{
		this.reserved2 = reserved2;
	}

	public java.lang.String getReserved3()
	{
		return this.reserved3;
	}

	public void setReserved3(java.lang.String reserved3)
	{
		this.reserved3 = reserved3;
	}

	public cn.com.youtong.apollo.task.db.form.RowForm getRow()
	{
		return this.row;
	}

	public void setRow(cn.com.youtong.apollo.task.db.form.RowForm row)
	{
		this.row = row;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("cellID", getCellID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof CellForm))
		{
			return false;
		}
		CellForm castOther = (CellForm) other;
		return new EqualsBuilder().append(this.getCellID(), castOther.getCellID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getCellID()).toHashCode();
	}

}
