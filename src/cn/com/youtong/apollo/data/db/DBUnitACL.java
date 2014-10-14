package cn.com.youtong.apollo.data.db;

import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.services.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBUnitACL
	implements UnitACL
{
	private Log log = LogFactory.getLog(this.getClass());;

	private User user;

	private Task task;

	public DBUnitACL(Task task, User user)
	{
		this.task = task;
		this.user = user;
	}

	/**
	 * 取要判断访问权限的用户
	 * @return ACL表的用户
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * 当前用户是否可以查看指定单位的数据
	 * @param unitID 单位ID
	 * @return TRUE - 可以读取， FALSE - 不可以读取
	 */
	public boolean isReadable(String unitID)
	{

		boolean result = false;
		try
		{
			Collection groups = user.getGroups();
			Iterator itrGroups = groups.iterator();

			ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
			ModelManagerFactory.class.getName())).createModelManager(task.id());
			UnitPermissionManager u = modelManager.getUnitPermissionManager();

			//循环判断所有的组的权限，有则跳出所有的循环，并将结果置为true
			while (itrGroups.hasNext())
			{
				Group tempGroup = (Group) itrGroups.next();

				if (u.getPermission(tempGroup.getGroupID(), unitID,
									UnitPermission.UNIT_PERMISSION_READ))
				{

					result = true;
					break;
				}
			}
		}
		catch (Exception ex)
		{
			log.error("查找权限失败", ex);
		}
		return result;
	}

	/**
	 * 单位数据是否可写
	 * @param unitID 单位ID
	 * @return TRUE - 可写，FALSE - 不可写
	 */
	public boolean isWritable(String unitID)
	{
		boolean result = false;
		try
		{
			Collection groups = user.getGroups();
			Iterator itrGroups = groups.iterator();

			ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
			ModelManagerFactory.class.getName())).createModelManager(task.id());
			UnitPermissionManager u = modelManager.getUnitPermissionManager();

			//循环判断所有的组的权限，有则跳出所有的循环，并将结果置为true
			while (itrGroups.hasNext())
			{
				Group tempGroup = (Group) itrGroups.next();

				if (u.getPermission(tempGroup.getGroupID(), unitID,
									UnitPermission.UNIT_PERMISSION_WRITE))
				{

					result = true;
					break;
				}
			}
		}
		catch (Exception ex)
		{
			log.error("查找权限失败", ex);
		}
		return result;
	}
}