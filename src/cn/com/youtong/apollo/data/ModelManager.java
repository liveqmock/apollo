package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * 任务数据管理。
 * <ul> 处理任务数据相关的所有操作
 *   <li> 导入任务数据，计算，审核
 *   <li> 导出任务数据
 *   <li> 汇总，调差额表
 *   <li> 管理单位权限的分配
 * </ul>
 * ModelManager与Task是一对一关系。并且ModelManager是一个factory，<br>
 * 由ModelManager生成DataImporter，DataExporter，Summer，UnitPermissionManager来导入，导出，汇总<br>
 * 特定Task的数据等操作。这些类之间的关系如图：<br>
 * <img src="uml.jpg"/>
 */
public interface ModelManager
{
	/**
	 * Get model manager coresponding task.
	 * @return task
	 */
	Task getTask();

	/**
	 * 得到与本ModelManager相关的UnitTreeManager
	 * @return 与本ModelManager相关的UnitTreeManager
	 * @throws ModelException
	 */
	UnitTreeManager getUnitTreeManager() throws ModelException;

	/**
	 * 得到该model的汇总器
	 * @return 汇总器
	 * @throws ModelException
	 */
	Summer getSummer() throws ModelException;

	/**
	 * 得到该model的导入器
	 * @return 导入器
	 * @throws ModelException
	 */
	DataImporter getDataImporter() throws ModelException;

	/**
	 * 得到该model的数据源
	 * @return 数据源
	 * @throws ModelException
	 */
	DataSource getDataSource() throws ModelException;

	/**
	 * 得到该model的导出器
	 * @return 导出器
	 * @throws ModelException
	 */
	DataExporter getDataExporter() throws ModelException;

	/**
	 * 得到该model的单位权限管理器
	 * @return 单位权限管理器
	 * @throws ModelException
	 */
	UnitPermissionManager getUnitPermissionManager() throws ModelException;

	/**
	 * 取用户权限列表
	 * @param user   用户
	 * @return       权限列表
	 * @throws ModelException
	 */
	UnitACL getUnitACL(User user) throws ModelException;

}