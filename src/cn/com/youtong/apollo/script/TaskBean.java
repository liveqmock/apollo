package cn.com.youtong.apollo.script;

import java.util.Calendar;

import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;
/**
 * 任务bean
 */
public class TaskBean
{
    /**
     * 任务id
     */
    public String id;

    /**
     * 任务名称
     */
    public String name;

    /**
     * 当前任务年
     */
    public int year;

    /**
     * 当前任务月
     */
    public int month;

    /**
     * 当前任务日，月报无意义
     */
    public int day;

    public TaskBean( TaskData taskData )
    {
        Task task = taskData.getTask();
        TaskTime taskTime = taskData.getTaskTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(taskTime.getFromTime());

        this.id = task.id();
        this.name = task.getName();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
    }

}