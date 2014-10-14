package cn.com.youtong.apollo.task.db.form;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class UnitMetaForm
	implements Serializable
{

	/** identifier field */
	private Integer taskID;

	/** nullable persistent field */
	private Integer parentUnitCodeCellID;

	/** nullable persistent field */
	private Integer reprotTypeCellID;

	/** nullable persistent field */
	private Integer unitCodeCellID;

	/** nullable persistent field */
	private Integer headquarterCodeCellID;

	/** nullable persistent field */
	private Integer unitNameCellID;
	
	private Integer displayCellID;

	/** nullable persistent field */
	private Integer unitMetaTableID;

	/** persistent field */
	private int flag;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** nullable persistent field */
	private cn.com.youtong.apollo.task.db.form.TaskForm task;

	/** full constructor */
	public UnitMetaForm(java.lang.Integer parentUnitCodeCellID, java.lang.Integer reprotTypeCellID, java.lang.Integer unitCodeCellID, java.lang.Integer headquarterCodeCellID,
						java.lang.Integer unitNameCellID,Integer displayCellID, java.lang.Integer unitMetaTableID, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3,
						cn.com.youtong.apollo.task.db.form.TaskForm task)
	{
		this.parentUnitCodeCellID = parentUnitCodeCellID;
		this.reprotTypeCellID = reprotTypeCellID;
		this.unitCodeCellID = unitCodeCellID;
		this.headquarterCodeCellID = headquarterCodeCellID;
		this.unitNameCellID = unitNameCellID;
		this.displayCellID = displayCellID;
		this.unitMetaTableID = unitMetaTableID;
		this.flag = flag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.task = task;
	}

	/** default constructor */
	public UnitMetaForm()
	{
	}

	/** minimal constructor */
	public UnitMetaForm(int flag)
	{
		this.flag = flag;
	}

	public java.lang.Integer getTaskID()
	{
		return this.taskID;
	}

	public void setTaskID(java.lang.Integer taskID)
	{
		this.taskID = taskID;
	}

	public java.lang.Integer getParentUnitCodeCellID()
	{
		return this.parentUnitCodeCellID;
	}

	public void setParentUnitCodeCellID(java.lang.Integer parentUnitCodeCellID)
	{
		this.parentUnitCodeCellID = parentUnitCodeCellID;
	}

	public java.lang.Integer getReprotTypeCellID()
	{
		return this.reprotTypeCellID;
	}

	public void setReprotTypeCellID(java.lang.Integer reprotTypeCellID)
	{
		this.reprotTypeCellID = reprotTypeCellID;
	}

	public java.lang.Integer getUnitCodeCellID()
	{
		return this.unitCodeCellID;
	}

	public void setUnitCodeCellID(java.lang.Integer unitCodeCellID)
	{
		this.unitCodeCellID = unitCodeCellID;
	}

	public java.lang.Integer getHeadquarterCodeCellID()
	{
		return this.headquarterCodeCellID;
	}

	public void setHeadquarterCodeCellID(java.lang.Integer headquarterCodeCellID)
	{
		this.headquarterCodeCellID = headquarterCodeCellID;
	}

	public java.lang.Integer getUnitNameCellID()
	{
		return this.unitNameCellID;
	}

	public void setUnitNameCellID(java.lang.Integer unitNameCellID)
	{
		this.unitNameCellID = unitNameCellID;
	}

	public java.lang.Integer getUnitMetaTableID()
	{
		return this.unitMetaTableID;
	}

	public void setUnitMetaTableID(java.lang.Integer unitMetaTableID)
	{
		this.unitMetaTableID = unitMetaTableID;
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

	public cn.com.youtong.apollo.task.db.form.TaskForm getTask()
	{
		return this.task;
	}

	public void setTask(cn.com.youtong.apollo.task.db.form.TaskForm task)
	{
		this.task = task;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("taskID", getTaskID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof UnitMetaForm))
		{
			return false;
		}
		UnitMetaForm castOther = (UnitMetaForm) other;
		return new EqualsBuilder().append(this.getTaskID(), castOther.getTaskID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getTaskID()).toHashCode();
	}

	/**
	 * @return Returns the displayCellID.
	 */
	public Integer getDisplayCellID() {
		return this.displayCellID;
	}

	/**
	 * @param displayCellID The displayCellID to set.
	 */
	public void setDisplayCellID(Integer displayCellID) {
		this.displayCellID = displayCellID;
	}

}
