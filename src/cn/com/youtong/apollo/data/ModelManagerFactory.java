package cn.com.youtong.apollo.data;

import cn.com.youtong.apollo.data.db.*;

/**
 * ��������ģ�͹���
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
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
     * ����ָ����Manager
     * @param taskID Manager������ID
     * @throws ModelException
     */
    public void updateModelManager(String taskID) throws ModelException;

    /**
     * ɾ��ָ����Manager
     * @param taskID Manager������ID
     * @throws ModelException
     */
    public void deleteModelManager(String taskID) throws ModelException;


}