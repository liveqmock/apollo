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
	 * ȡҪ�жϷ���Ȩ�޵��û�
	 * @return ACL����û�
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * ��ǰ�û��Ƿ���Բ鿴ָ����λ������
	 * @param unitID ��λID
	 * @return TRUE - ���Զ�ȡ�� FALSE - �����Զ�ȡ
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

			//ѭ���ж����е����Ȩ�ޣ������������е�ѭ�������������Ϊtrue
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
			log.error("����Ȩ��ʧ��", ex);
		}
		return result;
	}

	/**
	 * ��λ�����Ƿ��д
	 * @param unitID ��λID
	 * @return TRUE - ��д��FALSE - ����д
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

			//ѭ���ж����е����Ȩ�ޣ������������е�ѭ�������������Ϊtrue
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
			log.error("����Ȩ��ʧ��", ex);
		}
		return result;
	}
}