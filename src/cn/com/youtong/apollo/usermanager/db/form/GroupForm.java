package cn.com.youtong.apollo.usermanager.db.form;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class GroupForm
	implements Serializable
{

	/** identifier field */
	private Integer groupID;

	/** persistent field */
	private String name;

	/** nullable persistent field */
	private java.util.Date dateCreated;

	/** nullable persistent field */
	private java.util.Date dateModified;

	/** nullable persistent field */
	private String memo;

	/** persistent field */
	private int flag;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** persistent field */
	private Set users;

	/** full constructor */
	public GroupForm(java.lang.Integer groupID, java.lang.String name, java.util.Date dateCreated, java.util.Date dateModified, java.lang.String memo, int flag, java.lang.String reserved1,
					 java.lang.String reserved2, java.lang.String reserved3, Set users)
	{
		this.groupID = groupID;
		this.name = name;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.memo = memo;
		this.flag = flag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.users = users;
	}

	/** default constructor */
	public GroupForm()
	{
	}

	/** minimal constructor */
	public GroupForm(java.lang.Integer groupID, java.lang.String name, int flag, Set users)
	{
		this.groupID = groupID;
		this.name = name;
		this.flag = flag;
		this.users = users;
	}

	public java.lang.Integer getGroupID()
	{
		return this.groupID;
	}

	public void setGroupID(java.lang.Integer groupID)
	{
		this.groupID = groupID;
	}

	public java.lang.String getName()
	{
		return this.name;
	}

	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	public java.util.Date getDateCreated()
	{
		return this.dateCreated;
	}

	public void setDateCreated(java.util.Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public java.util.Date getDateModified()
	{
		return this.dateModified;
	}

	public void setDateModified(java.util.Date dateModified)
	{
		this.dateModified = dateModified;
	}

	public java.lang.String getMemo()
	{
		return this.memo;
	}

	public void setMemo(java.lang.String memo)
	{
		this.memo = memo;
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

	public java.util.Set getUsers()
	{
		return this.users;
	}

	public void setUsers(java.util.Set users)
	{
		this.users = users;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("groupID", getGroupID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof GroupForm))
		{
			return false;
		}
		GroupForm castOther = (GroupForm) other;
		return new EqualsBuilder().append(this.getGroupID(), castOther.getGroupID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getGroupID()).toHashCode();
	}

}
