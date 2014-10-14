package cn.com.youtong.apollo.data.db.form;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class UnitPermissionForm
	implements Serializable
{

	/** identifier field */
	private cn.com.youtong.apollo.data.db.form.UnitPermissionFormPK comp_id;

	/** nullable persistent field */
	private int permission;

	/** full constructor */
	public UnitPermissionForm(cn.com.youtong.apollo.data.db.form.UnitPermissionFormPK comp_id, int permission)
	{
		this.comp_id = comp_id;
		this.permission = permission;
	}

	/** default constructor */
	public UnitPermissionForm()
	{
	}

	/** minimal constructor */
	public UnitPermissionForm(cn.com.youtong.apollo.data.db.form.UnitPermissionFormPK comp_id)
	{
		this.comp_id = comp_id;
	}

	public cn.com.youtong.apollo.data.db.form.UnitPermissionFormPK getComp_id()
	{
		return this.comp_id;
	}

	public void setComp_id(cn.com.youtong.apollo.data.db.form.UnitPermissionFormPK comp_id)
	{
		this.comp_id = comp_id;
	}

	public int getPermission()
	{
		return this.permission;
	}

	public void setPermission(int permission)
	{
		this.permission = permission;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("comp_id", getComp_id()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof UnitPermissionForm))
		{
			return false;
		}
		UnitPermissionForm castOther = (UnitPermissionForm) other;
		return new EqualsBuilder().append(this.getComp_id(), castOther.getComp_id()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getComp_id()).toHashCode();
	}

}
