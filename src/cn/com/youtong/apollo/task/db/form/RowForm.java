package cn.com.youtong.apollo.task.db.form;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class RowForm
	implements Serializable
{

	/** identifier field */
	private Integer rowID;

	/** persistent field */
	private int flag;

	private int isFloat;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** persistent field */
	private String ID;

	/** persistent field */
	private cn.com.youtong.apollo.task.db.form.TableForm table;

	/** persistent field */
	private Set cells;

	/** full constructor */
	public RowForm(java.lang.Integer rowID, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, java.lang.String ID,
				   cn.com.youtong.apollo.task.db.form.TableForm table, Set cells)
	{
		this.rowID = rowID;
		this.flag = flag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.ID = ID;
		this.table = table;
		this.cells = cells;
	}

	/** default constructor */
	public RowForm()
	{
	}

	/** minimal constructor */
	public RowForm(java.lang.Integer rowID, int flag, java.lang.String ID, cn.com.youtong.apollo.task.db.form.TableForm table, Set cells)
	{
		this.rowID = rowID;
		this.flag = flag;
		this.ID = ID;
		this.table = table;
		this.cells = cells;
	}

	public java.lang.Integer getRowID()
	{
		return this.rowID;
	}

	public void setRowID(java.lang.Integer rowID)
	{
		this.rowID = rowID;
	}

	public int getFlag()
	{
		return this.flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
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

	public java.lang.String getID()
	{
		return this.ID;
	}

	public void setID(java.lang.String ID)
	{
		this.ID = ID;
	}

	public cn.com.youtong.apollo.task.db.form.TableForm getTable()
	{
		return this.table;
	}

	public void setTable(cn.com.youtong.apollo.task.db.form.TableForm table)
	{
		this.table = table;
	}

	public java.util.Set getCells()
	{
		return this.cells;
	}

	public void setCells(java.util.Set cells)
	{
		this.cells = cells;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("rowID", getRowID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof RowForm))
		{
			return false;
		}
		RowForm castOther = (RowForm) other;
		return new EqualsBuilder().append(this.getRowID(), castOther.getRowID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getRowID()).toHashCode();
	}

	public int getIsFloat()
	{
		return isFloat;
	}

	public void setIsFloat(int isFloat)
	{
		this.isFloat = isFloat;
	}
}
