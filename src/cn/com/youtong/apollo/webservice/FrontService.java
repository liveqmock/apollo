package cn.com.youtong.apollo.webservice;

import java.util.Date;

/**
 * WebService�ӿڣ����ﶨ�� service ����
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: YouTong</p>
 * @author wjb
 * @version 1.0
 */
public interface FrontService
{
	/**
	 * �����û��������뵼�����ݡ�
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ����룻</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 *  <li> 11 ��ʾ û�ж�Ӧ���� </li>
	 * </ul>
	 * @param data               �ϱ������ݣ�XML��ʽ��
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult uploadData(String data, String username, String password);

	/**
	 * ��������
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 *
	 * @param taskID  �����ʶ
	 * @param unitID  ��λ����
	 * @param date    ������������ʱ����е�һ��
	 * @param username �û���
	 * @param password ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadData(String taskID, String unitID, Date date, String username, String password);

	/**
	 * ������������
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 *
	 * @param taskID  �����ʶ
	 * @param date    ������������ʱ����е�һ��
	 * @param username �û���
	 * @param password ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadAllData(String taskID, Date date, String username, String password);

	/**
	 * ��������
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 *
	 * @param taskID  �����ʶ
	 * @param unitID  ��λ����
	 * @param date    ������������ʱ����е�һ��
	 * @param username �û���
	 * @param password ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadDataByTree(String taskID, String unitID, Date date, String username, String password);

	/**
	 * ��������
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 *
	 * @param definition  ������
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult publishTask(String definition, String username, String password);

	/**
	 *
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param id                 ����id
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadTask(String id, String username, String password);

	/**
	 * ɾ������
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param taskID             �����ʶ
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult deleteTask(String taskID, String username, String password);

	/**
	 * ���������ֵ�
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param content           �ֵ�����
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult publishDictionary(String content, String username, String password);

	/**
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param id                 �ֵ��ʶ
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadDictionary(String id, String username, String password);

	/**
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param id                 �ֵ��ʶ
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult deleteDictionary(String id, String username, String password);

	/**
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param taskID           �����ʶ
	 * @param script           �ű�����
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult publishScriptSuit(String taskID, String script, String username, String password);

	/**
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param taskID            �����ʶ
	 * @param suitName           �ű�����
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult downloadScriptSuit(String taskID, String suitName, String username, String password);

	/**
	 * ���ܵķ���ֵ���£�
	 * <ul>
	 *  <li> 0  ��ʾ �ɹ�������contentֵΪ���ݵ�xml��ʾ</li>
	 *  <li> 1  ��ʾ �û��������벻ƥ�䣻</li>
	 *  <li> -1 ��ʾ ϵͳ�ڲ�����contentֵ���Ϊ:ϵͳ�ڲ�����: + �쳣��ʾ </li>
	 * </ul>
	 * @param taskID            �����ʶ
	 * @param suitName          �ű�����
	 * @param username           �û���
	 * @param password           ����
	 * @return ServiceResult      Լ���ķ���ֵ
	 */
	ServiceResult deleteScriptSuit(String taskID, String suitName, String username, String password);
}