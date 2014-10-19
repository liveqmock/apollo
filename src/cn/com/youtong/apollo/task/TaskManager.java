package cn.com.youtong.apollo.task;

import java.io.*;
import java.util.*;

/**
 * Task manager
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
public interface TaskManager
{
    /**
     * Get all tasks
     * @return each element is a Task object
     * @throws TaskException throws exception if failed
     * @see Task
     */
    Iterator getAllTasks()
            throws TaskException;

    /**
     * Get task by task ID
     * @param id task id
     * @return Task if specfic task existed, else null
     * @throws TaskException throws exception if failed
     */
    Task getTaskByID(String id)
            throws TaskException;

    /**
     * Delete task
     * @param task task to be deleted
     * @throws TaskException throws exception if failed to delete exception
     */
    void deleteTask(Task task)
            throws TaskException;

    /**
     * ��������
     * @param xmlInputStream Ҫ���������������xml��
     * @return �ɹ�����������
     * @throws TaskException ����ʧ��ʱ�׳�
     */
    Task publishTask(InputStream xmlInputStream)
            throws TaskException;
    /**
     * ��������ʱ��
     * @param taskID ����ID
     * @param year ����ʱ��
     * @return �Ѿ�����������ʱ��
     * @throws TaskException �����ű���ʧ��
     */
    public List<TaskTime> publishTaskTime(String taskID,int year)throws
    TaskException;
    /**
     * �����ű��飬������ڣ�����
     * @param taskID ����ID
     * @param xmlInputStream �ű���xml��
     * @return �ɹ������Ľű���
     * @throws TaskException �����ű���ʧ��
     */
    public ScriptSuit publishScriptSuit(String taskID,
                                        InputStream xmlInputStream)
            throws
            TaskException;

    /**
     * ����XSLT�ļ���������ڣ�����
     * @param xmlInputStream xslt��
     * @return �ɹ������Ľű���
     * @throws TaskException �����ű���ʧ��
     */
    public Task publishXSLT(InputStream xmlInputStream)
            throws TaskException;


    /**
     * ɾ���ű���
     * @param taskID �ű�������������ID
     * @param suitName �ű�������ID
     * @throws TaskException ɾ���ű���ʧ��
     */
    public void deleteScriptSuit(String taskID, String suitName)
            throws
            TaskException;

    /**
     * ɾ��XSLT
     * @param taskID ����ID��
     * @param viewID viewID
     * @throws TaskException ɾ��ʧ��
     */
    public void deleteXSLT(String taskID,Integer viewID)
            throws TaskException;


    /**
     * ������������е�XSLT�ļ�
     * ��û�ҵ��򷵻ء�size Ϊ0����Collection
     * @param taskID ����ID��
     * @return ����XSLT�ļ��ļ���(TaskView����)
     * @throws TaskException
     */
//    public Iterator getAllViews(String taskID)
//            throws TaskException;

    /**
     * ���������XSLT�ļ�
     * @param taskID ����ID��
     * @param tableID ��ID��
     * @return TaskView����
     * @throws TaskException
     */
//    public TableView getView(String taskID, String tableID)
//            throws TaskException;


    /**
     * ������������ʹ�õĽű���
     * @param taskID �ű�������������ID
     * @param suitName �ű�������ID
     * @throws TaskException ����ʧ��
     */
    public void setActiveScriptSuit(String taskID, String suitName)
            throws TaskException;

    /**
     * �����ű���
     * @param taskID �ű�������������ID
     * @param suitName �ű�������ID
     * @param out �������
     * @throws TaskException ����ʧ��
     */
    public void outputScriptSuit(String taskID, String suitName,
                                 OutputStream out)
            throws TaskException;

}