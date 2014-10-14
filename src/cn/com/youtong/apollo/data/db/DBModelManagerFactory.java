package cn.com.youtong.apollo.data.db;

import java.util.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.*;

public class DBModelManagerFactory extends DefaultFactory implements ModelManagerFactory
{
    /**
     * 存放ModelManager实例的Map
     */
    private static Map managers = Collections.synchronizedMap(new HashMap());

    public DBModelManagerFactory()
    {
    }

	/**
	 * Create model manager
	 * @param taskID task id
	 * @return model manager
	 * @throws ModelException
	 */
	public ModelManager createModelManager(String taskID)
		throws ModelException
	{
        if(DBModelManagerFactory.managers.get(taskID) == null)
        {
            DBModelManagerFactory.managers.put(taskID, new DBModelManager(taskID));
        }
		return (ModelManager)DBModelManagerFactory.managers.get(taskID);
	}

    /**
     * 使指定的Manager无效，factory将重新load该Manager
     * @param taskID Manager的任务ID
     * @throws ModelException
     */
    public void updateModelManager(String taskID) throws ModelException
    {
        DBModelManagerFactory.managers.put(taskID, new DBModelManager(taskID));
    }

    /**
     * 删除指定的Manager
     * @param taskID Manager的任务ID
     * @throws ModelException
     */
    public void deleteModelManager(String taskID) throws ModelException
    {
        DBModelManagerFactory.managers.remove(taskID);
    }

}