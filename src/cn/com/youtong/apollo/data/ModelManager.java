package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * �������ݹ���
 * <ul> ��������������ص����в���
 *   <li> �����������ݣ����㣬���
 *   <li> ������������
 *   <li> ���ܣ�������
 *   <li> ����λȨ�޵ķ���
 * </ul>
 * ModelManager��Task��һ��һ��ϵ������ModelManager��һ��factory��<br>
 * ��ModelManager����DataImporter��DataExporter��Summer��UnitPermissionManager�����룬����������<br>
 * �ض�Task�����ݵȲ�������Щ��֮��Ĺ�ϵ��ͼ��<br>
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
	 * �õ��뱾ModelManager��ص�UnitTreeManager
	 * @return �뱾ModelManager��ص�UnitTreeManager
	 * @throws ModelException
	 */
	UnitTreeManager getUnitTreeManager() throws ModelException;

	/**
	 * �õ���model�Ļ�����
	 * @return ������
	 * @throws ModelException
	 */
	Summer getSummer() throws ModelException;

	/**
	 * �õ���model�ĵ�����
	 * @return ������
	 * @throws ModelException
	 */
	DataImporter getDataImporter() throws ModelException;

	/**
	 * �õ���model������Դ
	 * @return ����Դ
	 * @throws ModelException
	 */
	DataSource getDataSource() throws ModelException;

	/**
	 * �õ���model�ĵ�����
	 * @return ������
	 * @throws ModelException
	 */
	DataExporter getDataExporter() throws ModelException;

	/**
	 * �õ���model�ĵ�λȨ�޹�����
	 * @return ��λȨ�޹�����
	 * @throws ModelException
	 */
	UnitPermissionManager getUnitPermissionManager() throws ModelException;

	/**
	 * ȡ�û�Ȩ���б�
	 * @param user   �û�
	 * @return       Ȩ���б�
	 * @throws ModelException
	 */
	UnitACL getUnitACL(User user) throws ModelException;

}