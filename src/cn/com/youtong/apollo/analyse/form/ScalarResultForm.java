package cn.com.youtong.apollo.analyse.form;

import java.io.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

/**
 * ��װָ���ѯ�����Form
 */
public class ScalarResultForm
    implements Serializable
{
    /**
     * ���캯��
     * @param task ����
     * @param units ��λ����
     * @param taskTimes ָ����������������
     * @param scalars ָ������
     * @param result ָ��ֵ���
     */
    public ScalarResultForm(Task task, UnitTreeNode[] units,
                            TaskTime[] taskTimes, ScalarForm[] scalars,
                            Object[][][] result)
    {
        this.task = task;
        this.units = units;
        this.taskTimes = taskTimes;
        this.scalars = scalars;
        this.result = result;
    }

    /**
     * ����
     */
    private Task task;

    /**
     * ��λ����
     */
    private UnitTreeNode[] units;

    /**
     * ָ����������������
     */
    private TaskTime[] taskTimes;

    /**
     * ָ������
     */
    private ScalarForm[] scalars;

    /**
     * ָ��ֵ����ά����
     * ��һά��size = units.length�����������λ
     * �ڶ�ά��size = taskTimes.length���������������
     * ����ά��size = scalars.length���������ָ��
     */
    private Object[][][] result;

    public Object[][][] getResult()
    {
        return result;
    }

    public ScalarForm[] getScalars()
    {
        return scalars;
    }

    public TaskTime[] getTaskTimes()
    {
        return taskTimes;
    }

    public UnitTreeNode[] getUnits()
    {
        return units;
    }

    public Task getTask()
    {
        return task;
    }

}