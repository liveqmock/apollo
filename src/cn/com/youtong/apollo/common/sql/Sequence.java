package cn.com.youtong.apollo.common.sql;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class Sequence
	implements Serializable
{

	/** identifier field */
	private String tableName;

	/** nullable persistent field */
	private Integer maxID;

	/** full constructor */
	public Sequence(java.lang.String tableName, java.lang.Integer maxID)
	{
		this.tableName = tableName;
		this.maxID = maxID;
	}

	/** default constructor */
	public Sequence()
	{
	}

	/** minimal constructor */
	public Sequence(java.lang.String tableName)
	{
		this.tableName = tableName;
	}

	public java.lang.String getTableName()
	{
		return this.tableName;
	}

	public void setTableName(java.lang.String tableName)
	{
		this.tableName = tableName;
	}

	public java.lang.Integer getMaxID()
	{
		return this.maxID;
	}

	public void setMaxID(java.lang.Integer maxID)
	{
		this.maxID = maxID;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("tableName", getTableName()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof Sequence))
		{
			return false;
		}
		Sequence castOther = (Sequence) other;
		return new EqualsBuilder().append(this.getTableName(), castOther.getTableName()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getTableName()).toHashCode();
	}

}
