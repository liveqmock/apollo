package cn.com.youtong.apollo.usermanager;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Group
{

	/**
	 * 返回该组的ID号
	 * @return ID号
	 */
	public Integer getGroupID();

	/**
	 * 获得组的创建时间
	 * @return 时间
	 */
	public java.util.Date getDateCreated();

	/**
	 * 获得组的最后修改时间
	 * @return 最后修改时间
	 */
	public java.util.Date getDateModified();

	/**
	 * 返回组的名字
	 * @return 组的名字
	 */
	public String getName();

	/**
	 * 返回组的备注
	 * @return 备注.
	 */
	public String getMemo();

	/**
	 * 获得所有用户
	 * @return 用户的集合 User的集合
	 */
	public Collection getMembers();
}