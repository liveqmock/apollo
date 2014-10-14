/*
 * Created on 2003-11-23
 */
package cn.com.youtong.apollo.data.db;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.serialization.*;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * ������������ݿⷽʽʵ��
 */
class DBModelManager
	implements ModelManager
{
	/** DBUnitTreeManagerʵ�� */
	private DBUnitTreeManager treeManager;

	private Log log = LogFactory.getLog(this.getClass());

	/** ���������� */
	private Task task;

	/**
	 * �õ���model�ĵ�����
	 * @return ������
	 * @throws ModelException
	 */
	public DataExporter getDataExporter() throws ModelException
	{
		return new DBDataExporter(task, treeManager);
	}

	/**
	 * �������������
	 * <p>
	 * ÿ������������Ӧһ������
	 * @param taskID
	 * 				�����ʶ
	 * @throws ModelException
	 * 				���񲻴��ڣ�����������ȡ�������������ݷ����쳣
	 */
	public DBModelManager(String taskID) throws ModelException
	{
		init(taskID);
	}

	/**
	 * ��ʼ��
	 * @param taskID ����id
	 * @throws ModelException
	 */
	private void init(String taskID) throws ModelException
	{
		try
		{
			TaskManager taskMng = ( (TaskManagerFactory) Factory.getInstance(
			TaskManagerFactory.class.getName())).createTaskManager();

			this.task = taskMng.getTaskByID(taskID);

			//load DBUnitTreeManager instance
			this.treeManager = new DBUnitTreeManager(task);
		}
		catch (FactoryException e)
		{
			throw new ModelException(e);
		}
		catch (TaskException e)
		{
			throw new ModelException(e);
		}
	}

	/**
	 * �õ���model�Ļ�����
	 * @return ������
	 * @throws ModelException
	 */
	public Summer getSummer() throws ModelException
	{
		return new DBSummer(task, treeManager);
	}

	/**
	 * �õ���model�ĵ�λȨ�޹�����
	 * @return ��λȨ�޹�����
	 * @throws ModelException
	 */
	public UnitPermissionManager getUnitPermissionManager() throws
		ModelException
	{
		return new DBUnitPermissionManager(task, treeManager);
	}

	/**
	 * �õ���model�ĵ�����
	 * @return ������
	 * @throws ModelException
	 */
	public DataImporter getDataImporter() throws ModelException
	{
		return new DBDataImporter(task, treeManager);
	}

	/**
	 * ����������
	 * @return			����������
	 * @see cn.com.youtong.apollo.data.ModelManager#getTask()
	 */
	public Task getTask()
	{
		return task;
	}

	/**
	 * @see cn.com.youtong.apollo.data.ModelManager#getUnitACL(cn.com.youtong.apollo.usermanager.User)
	 */
	public UnitACL getUnitACL(User user) throws ModelException
	{
		return new DBUnitACL(task, user);
	}

	/**
	 * �õ��뱾ModelManager��ص�UnitTreeManager
	 * @return �뱾ModelManager��ص�UnitTreeManager
	 * @throws ModelException
	 */
	public UnitTreeManager getUnitTreeManager() throws ModelException
	{
		return this.treeManager;
	}

	/**
	 * �õ���model������Դ
	 * @return ����Դ
	 * @throws ModelException
	 */
	public DataSource getDataSource() throws ModelException
	{
		return new DBDataSource(task, treeManager);
	}
}
