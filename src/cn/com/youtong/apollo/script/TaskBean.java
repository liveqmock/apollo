package cn.com.youtong.apollo.script;

import java.util.Calendar;

import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;
/**
 * ����bean
 */
public class TaskBean
{
    /**
     * ����id
     */
    public String id;

    /**
     * ��������
     */
    public String name;

    /**
     * ��ǰ������
     */
    public int year;

    /**
     * ��ǰ������
     */
    public int month;

    /**
     * ��ǰ�����գ��±�������
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