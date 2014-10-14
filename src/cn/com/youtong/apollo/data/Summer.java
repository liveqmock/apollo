package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;

/**
 * �������ݻ�������ִ�и��ֻ��ܲ���
 */
public interface Summer
{
	/**
	 * ��ȫ���ܵ�λID
	 */
	public final static String SUM_ALL_UNIT_ID = "00000000";

	/**
	 * �����ڵ����
	 * @param unitID ���ܽڵ㵥λID
	 * @param time ����ʱ��
	 * @param isRecursive �Ƿ�ݹ���������ӽڵ�
	 * @throws ModelException
	 */
	void adjustNodeDiff(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;

	/**
	 * ����ѡ����ܷ���
	 * @param schemaStream ������
	 * @throws ModelException
	 */
	public void createSelectSumSchema(InputStream schemaStream) throws
		ModelException;

	/**
	 * ɾ��ѡ����ܷ���
	 * @param schemaID ����id
	 * @throws ModelException
	 */
	public void deleteSelectSumSchema(Integer schemaID) throws ModelException;

	/**
	 * ִ��ѡ����ܷ���
	 * @param schemaID ����id
	 * @param taskTimeID ����ʱ��id
	 * @param unitACL ��λACL
	 * @throws ModelException
	 */
	public void executeSelectSumSchema(Integer schemaID,
									   cn.com.youtong.apollo.task.TaskTime
									   taskTimeID, UnitACL unitACL) throws
		ModelException;

	/**
	 * �õ�ָ�����������ѡ����ܷ���
	 * @taskID ����ID
	 * @return ѡ����ܷ���Iterator
	 * @throws ModelException
	 */
	public Iterator getAllSelectSumSchemas(String taskID) throws ModelException;

	/**
	 * �õ�ָ����ѡ����ܷ���
	 * @param schemaID ����id
	 * @return ѡ����ܷ���
	 * @throws ModelException
	 */
	public SelectSumSchema getSelectSumSchema(Integer schemaID) throws
		ModelException;

	/**
	 * �ڵ����
	 * @param unitID ���ܽڵ㵥λID
	 * @param time ����ʱ��
	 * @param isRecursive �Ƿ�ݹ���������ӽڵ�
	 * @throws ModelException
	 */
	void sumNode(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;

	/**
	 * ѡ�����
	 * @param unitID ������ܽ���ĵ�λID
	 * @param unitIDs Ҫ���ܵĵ�λID����
	 * @param time ���ܵ�����ʱ��
	 * @throws ModelException
	 */
	void sumUnits(String unitID, Collection unitIDs, TaskTime time) throws
		ModelException;

	/**
	 * ��ȫ���ܡ��������з�ѡ�����,��ȫ���ܺͽڵ���ܵ�λ
	 * @param unitID ������ܽ���ĵ�λID
	 * @param time ���ܵ�����ʱ��
	 * @throws ModelException
	 */
	void sumAll(String unitID, TaskTime time) throws ModelException;

	/**
	 * ��鼯�Ż��ܽڵ㵥λ�µ����нڵ���ܽ���Ƿ���ȷ
	 * @param unitID ���Ż��ܽڵ㵥λID
	 * @param time ����ʱ��
	 * @param isRecursive �Ƿ�ݹ���������ӽڵ�
	 * @return �����ValidateResult���󼯺ϵ�Iterator
	 * @throws ModelException
	 */
	Iterator validateNodeSum(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;
}