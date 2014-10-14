package cn.com.youtong.apollo.analyse.form;

import java.io.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

/**
 * 封装指标查询结果的Form
 */
public class ScalarResultForm
    implements Serializable
{
    /**
     * 构造函数
     * @param task 任务
     * @param units 单位数组
     * @param taskTimes 指标结果的所属期数组
     * @param scalars 指标数组
     * @param result 指标值结果
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
     * 任务
     */
    private Task task;

    /**
     * 单位数组
     */
    private UnitTreeNode[] units;

    /**
     * 指标结果的所属期数组
     */
    private TaskTime[] taskTimes;

    /**
     * 指标数组
     */
    private ScalarForm[] scalars;

    /**
     * 指标值，三维数组
     * 第一维，size = units.length，代表各个单位
     * 第二维，size = taskTimes.length，代表各个所属期
     * 第三维，size = scalars.length，代表各个指标
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