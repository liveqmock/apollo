package cn.com.youtong.apollo.data;

import cn.com.youtong.apollo.data.db.*;

/**
 * 任务数据模型工厂
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface ModelManagerFactory
{

	/**
	 * Create model manager
	 * @param taskID task id
	 * @return model manager
	 * @throws ModelException
	 */
	public ModelManager createModelManager(String taskID)
		throws ModelException;

    /**
     * 更新指定的Manager
     * @param taskID Manager的任务ID
     * @throws ModelException
     */
    public void updateModelManager(String taskID) throws ModelException;

    /**
     * 删除指定的Manager
     * @param taskID Manager的任务ID
     * @throws ModelException
     */
    public void deleteModelManager(String taskID) throws ModelException;


}