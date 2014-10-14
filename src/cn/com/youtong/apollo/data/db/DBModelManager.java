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
 * 任务管理器数据库方式实现
 */
class DBModelManager
	implements ModelManager
{
	/** DBUnitTreeManager实例 */
	private DBUnitTreeManager treeManager;

	private Log log = LogFactory.getLog(this.getClass());

	/** 报表任务定义 */
	private Task task;

	/**
	 * 得到该model的导出器
	 * @return 导出器
	 * @throws ModelException
	 */
	public DataExporter getDataExporter() throws ModelException
	{
		return new DBDataExporter(task, treeManager);
	}

	/**
	 * 创建任务管理器
	 * <p>
	 * 每个任务管理起对应一个任务
	 * @param taskID
	 * 				任务标识
	 * @throws ModelException
	 * 				任务不存在，或者其他读取任务，任务定义数据发生异常
	 */
	public DBModelManager(String taskID) throws ModelException
	{
		init(taskID);
	}

	/**
	 * 初始化
	 * @param taskID 任务id
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
	 * 得到该model的汇总器
	 * @return 汇总器
	 * @throws ModelException
	 */
	public Summer getSummer() throws ModelException
	{
		return new DBSummer(task, treeManager);
	}

	/**
	 * 得到该model的单位权限管理器
	 * @return 单位权限管理器
	 * @throws ModelException
	 */
	public UnitPermissionManager getUnitPermissionManager() throws
		ModelException
	{
		return new DBUnitPermissionManager(task, treeManager);
	}

	/**
	 * 得到该model的导入器
	 * @return 导入器
	 * @throws ModelException
	 */
	public DataImporter getDataImporter() throws ModelException
	{
		return new DBDataImporter(task, treeManager);
	}

	/**
	 * 返回任务定义
	 * @return			返回任务定义
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
	 * 得到与本ModelManager相关的UnitTreeManager
	 * @return 与本ModelManager相关的UnitTreeManager
	 * @throws ModelException
	 */
	public UnitTreeManager getUnitTreeManager() throws ModelException
	{
		return this.treeManager;
	}

	/**
	 * 得到该model的数据源
	 * @return 数据源
	 * @throws ModelException
	 */
	public DataSource getDataSource() throws ModelException
	{
		return new DBDataSource(task, treeManager);
	}
}
