package cn.com.youtong.apollo.task;

import cn.com.youtong.apollo.task.db.*;

/**
 * Task manager factory
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface TaskManagerFactory
{
	/**
	 * Create task manager
	 * @return task manager created
	 * @throws TaskException
	 * @see TaskManager
	 */
	public TaskManager createTaskManager()
		throws TaskException;
}