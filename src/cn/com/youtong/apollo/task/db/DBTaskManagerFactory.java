package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.services.*;

public class DBTaskManagerFactory extends DefaultFactory implements TaskManagerFactory
{
    /**
     * TaskManagerÊµÀý
     */
    private static TaskManager manager;

	public DBTaskManagerFactory()
	{
	}

	/**
	 * Create task manager
	 * @return task manager created
	 * @throws TaskException
	 * @see TaskManager
	 */
	public TaskManager createTaskManager()
		throws TaskException
	{
        if(DBTaskManagerFactory.manager == null)
        {
            DBTaskManagerFactory.manager = new DBTaskManager();
        }
        return DBTaskManagerFactory.manager;
	}
}