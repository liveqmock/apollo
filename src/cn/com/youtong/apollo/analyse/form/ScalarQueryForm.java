package cn.com.youtong.apollo.analyse.form;

import java.util.*;

/**
 * ָ���ѯ����Form
 */
public class ScalarQueryForm
{
    /**
     * ����ID
     */
    private String taskID;

    /**
     * ��λID����
     */
    private String[] unitIDs;

    /**
     * ָ������
     */
    private ScalarForm[] scalars;

    /**
     * ����ʱ��ID����
     */
    private Integer[] taskTimeIDs;

    public ScalarForm[] getScalars()
    {
        return scalars;
    }

    public String[] getUnitIDs()
    {
        return unitIDs;
    }

    public void setScalars(ScalarForm[] scalars)
    {
        this.scalars = scalars;
    }

    public void setUnitIDs(String[] unitIDs)
    {
        this.unitIDs = unitIDs;
    }

    public String getTaskID()
    {
        return taskID;
    }

    public void setTaskID(String taskID)
    {
        this.taskID = taskID;
    }

    public Integer[] getTaskTimeIDs()
    {
        return taskTimeIDs;
    }

    public void setTaskTimeIDs(Integer[] taskTimeIDs)
    {
        this.taskTimeIDs = taskTimeIDs;
    }

}