package cn.com.youtong.apollo.usermanager.db;

import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.usermanager.db.form.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DBRole
	implements Role
{
	/**
	 * 角色信息对象
	 */
	private RoleForm roleForm;

	public DBRole(RoleForm role)
	{
		this.roleForm = role;
	}

	/**
	 * 角色的ID号
	 * @return ID号
	 */
	public Integer getRoleID()
	{
		return roleForm.getRoleID();
	}

	/**
	 * 获得创建日期
	 * @return 创建日期
	 */
	public java.util.Date getDateCreated()
	{
		return roleForm.getDateCreated();
	}

	/**
	 * 获得最后修改日期
	 * @return 最后修改日期
	 */
	public java.util.Date getDateModified()
	{
		return roleForm.getDateModified();
	}

	/**
	 * 获得角色名称
	 * @return 名称
	 */
	public String getName()
	{
		return roleForm.getName();
	}

	/**
	 * 设置该角色的名称
	 * @param name 角色名
	 */
//  public void setName(String name)
//  {
//    roleForm.setName(name);
//  }

	/**
	 * 获得备注
	 * @return 备注
	 */
	public String getMemo()
	{
		return roleForm.getMemo();
	}

	/**
	 * 设置备注
	 * @param memo 备注
	 */
//  public void setMemo(String memo)
//  {
//    roleForm.setMemo(memo);
//  }

	/**
	 * 设置权限
	 * @param set 权限集对象
	 */
//  public void setPrivileges(SetOfPrivileges set)
//  {
//    roleForm.setUserRights(set.toString());
//  }

	/**
	 * 获得权限
	 * @return 权限集
	 */
	public SetOfPrivileges getPrivileges()
	{
		return new SetOfPrivileges(roleForm.getUserRights());
	}

}