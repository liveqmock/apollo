package cn.com.youtong.apollo.analyse.form;

import java.util.*;

/**
 * 指标查询条件Form
 */
public class ScalarQueryForm
{
    /**
     * 任务ID
     */
    private String taskID;

    /**
     * 单位ID数组
     */
    private String[] unitIDs;

    /**
     * 指标数组
     */
    private ScalarForm[] scalars;

    /**
     * 任务时间ID数组
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