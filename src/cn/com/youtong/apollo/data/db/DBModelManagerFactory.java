package cn.com.youtong.apollo.data.db;

import java.util.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.*;

public class DBModelManagerFactory extends DefaultFactory implements ModelManagerFactory
{
    /**
     * ���ModelManagerʵ����Map
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
     * ʹָ����Manager��Ч��factory������load��Manager
     * @param taskID Manager������ID
     * @throws ModelException
     */
    public void updateModelManager(String taskID) throws ModelException
    {
        DBModelManagerFactory.managers.put(taskID, new DBModelManager(taskID));
    }

    /**
     * ɾ��ָ����Manager
     * @param taskID Manager������ID
     * @throws ModelException
     */
    public void deleteModelManager(String taskID) throws ModelException
    {
        DBModelManagerFactory.managers.remove(taskID);
    }

}