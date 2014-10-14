package cn.com.youtong.apollo.usermanager.db;

import java.util.*;

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
public class DBUser
	implements User
{
	/**
	 * 用户信息对象
	 */
	private UserForm userForm;

	public DBUser(UserForm user)
	{
		this.userForm = user;
	}


	/**
	 * 返回该用户的ID号
	 * @return Integer
	 */
	public Integer getUserID()
	{
		return userForm.getUserID();
	}

	/**
	 * 返回该用户的帐号
	 * @return 字符串
	 */
	public String getName()
	{
		return userForm.getName();
	}


	/**
	 * 获得用户密码
	 * @return 用户密码
	 */
	public String getPassword()
	{
		return userForm.getPassword();
	}

	/**
	 * 获得企业名称
	 * @return 企业名称
	 */
	public String	getEnterpriseName()
	{
		return userForm.getEnterpriseName();
	}

	/**
	 * 获得法人代码
	 * @return 法人代码
	 */
	public String	getLawPersionCode()
	{
		return userForm.getLawPersionCode();
	}

	/**
	 * 获得法人代表
	 * @return 法人代表
	 */
	public String	getLawPersionName()
	{
		return userForm.getLawPersionName();
	}

	/**
	 * 获得法人代表电话
	 * @return 法人代表电话
	 */
	public String	getLawPersionPhone()
	{
		return userForm.getLawPersionPhone();
	}

	/**
	 * 获得联系人名称
	 * @return 联系人名称
	 */
	public String	getContactPersionName()
	{
		return userForm.getContactPersionName();
	}

	/**
	 * 获得联系人电话
	 * @return 联系人电话
	 */
	public String	getContactPersionPhone()
	{
		return userForm.getContactPersionPhone();
	}

	/**
	 * 获得联系人手机
	 * @return 联系人手机
	 */
	public String getContactPersionMobile()
	{
		return userForm.getContactPersionMobile();
	}

	/**
	 * 获得联系人地址
	 * @return 联系人地址
	 */
	public String	getContactAddress()
	{
		return userForm.getContactAddress();
	}

	/**
	 * 获得邮编
	 * @return 邮编
	 */
	public String	getPostcode()
	{
		return userForm.getPostcode();
	}

	/**
	 * 返回该用户的电子邮件
	 * @return 电子邮件
	 */
	public String getEmail()
	{
		return userForm.getEmail();
	}

	/**
	 * 获得传真
	 * @return 传真
	 */
	public String	getFax()
	{
		return userForm.getFax();
	}

	/**
	 * 是否有效（是否通过验证）
	 * @return 通过则返回true 否则flase
	 */
	public boolean isValidated()
	{
		if(userForm.getFlag()==1)
		{
			return true;
		}
		return false;
	}

	/**
	 * 返回该用户的备注
	 * @return 字符串
	 */
	public String getMemo()
	{
		return userForm.getMemo();
	}

	/**
	 * 返回该用户的角色
	 * @return 接口Role
	 */
	public Role getRole()
	{
		//将RoleForm对象封装成DBRole对象
		RoleForm role= userForm.getRole();
		if(role == null)
			return null;
		else
			return new DBRole(role);
	}

	/**
	 * 创建时间
	 * @return 创建时间
	 */
	public java.util.Date getDateCreated()
	{
		return userForm.getDateCreated();
	}

	/**
	 * 最后修改时间
	 * @return 最后修改时间
	 */
	public java.util.Date getDateModified()
	{
		return userForm.getDateModified();
	}

	/**
	 * 获得用户所在组的集合
	 * @return Group集合
	 */
	public Collection getGroups()
	{
		//将GroupForm集合封装成DBGroup集合
		Collection groups = new ArrayList();
		Iterator itr = userForm.getGroups().iterator();
		while(itr.hasNext())
		{
			DBGroup group = new DBGroup((GroupForm) itr.next());
			groups.add(group);
		}
		return groups;
	}
}