package cn.com.youtong.apollo.data.db.form;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class UnitPermissionFormPK
	implements Serializable
{

	/** identifier field */
	private Integer groupID;

	/** identifier field */
	private String taskID;

	/** identifier field */
	private String unitID;

	/** full constructor */
	public UnitPermissionFormPK(java.lang.Integer groupID, java.lang.String taskID, java.lang.String unitID)
	{
		this.groupID = groupID;
		this.taskID = taskID;
		this.unitID = unitID;
	}

	/** default constructor */
	public UnitPermissionFormPK()
	{
	}

	public java.lang.Integer getGroupID()
	{
		return this.groupID;
	}

	public void setGroupID(java.lang.Integer groupID)
	{
		this.groupID = groupID;
	}

	public java.lang.String getTaskID()
	{
		return this.taskID;
	}

	public void setTaskID(java.lang.String taskID)
	{
		this.taskID = taskID;
	}

	public java.lang.String getUnitID()
	{
		return this.unitID;
	}

	public void setUnitID(java.lang.String unitID)
	{
		this.unitID = unitID;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("groupID", getGroupID()).append("taskID", getTaskID()).append("unitID", getUnitID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof UnitPermissionFormPK))
		{
			return false;
		}
		UnitPermissionFormPK castOther = (UnitPermissionFormPK) other;
		return new EqualsBuilder().append(this.getGroupID(), castOther.getGroupID()).append(this.getTaskID(), castOther.getTaskID()).append(this.getUnitID(), castOther.getUnitID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getGroupID()).append(getTaskID()).append(getUnitID()).toHashCode();
	}

}
