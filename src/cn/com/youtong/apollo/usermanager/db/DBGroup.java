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

public class DBGroup
	implements Group
{

	/**
	 * 用户组信息对象
	 */
	private GroupForm groupForm;

	public DBGroup(GroupForm groupForm)
	{
		this.groupForm = groupForm;
	}

	/**
	 * 返回该组的ID号
	 * @return ID号
	 */
	public Integer getGroupID()
	{
		return groupForm.getGroupID();
	}

	/**
	 * 获得组的创建时间
	 * @return 时间
	 */
	public java.util.Date getDateCreated()
	{
		return groupForm.getDateCreated();
	}

	/**
	 * 获得组的最后修改时间
	 * @return 最后修改时间
	 */
	public java.util.Date getDateModified()
	{
		return groupForm.getDateModified();
	}

	/**
	 * 返回组的名字
	 * @return 组的名字
	 */
	public String getName()
	{
		return groupForm.getName();
	}

	/**
	 * 返回组的备注
	 * @return 备注.
	 */
	public String getMemo()
	{
		return groupForm.getMemo();
	}

	/**
	 * 设置组的名字
	 * @param name 名字
	 * @throws UserManagerException
	 */
//  public void setName(String name)
//  {
//    groupForm.setName(name);
//  }

	/**
	 * 设置组的备注
	 * @param memo 备注.
	 * @throws UserManagerException
	 */
//  public void setMemo(String memo)
//  {
//    groupForm.setMemo(memo);
//  }

	/**
	 * 获得所有用户
	 * @return 用户的集合 User的集合
	 */
	public Collection getMembers()
	{
		//将UserForm集合封装成DBUser集合
		Collection users = new ArrayList();
		Iterator itr = groupForm.getUsers().iterator();
		while(itr.hasNext())
		{
			DBUser user = new DBUser((UserForm) itr.next());
			users.add(user);
		}
		return users;

	}

	public boolean equals(Object other)
	{
		return this.groupForm.equals(((DBGroup) other).groupForm);
	}

}