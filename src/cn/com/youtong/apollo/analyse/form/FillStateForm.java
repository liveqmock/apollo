package cn.com.youtong.apollo.analyse.form;

import java.util.Date;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

/**
 * 填报情况form
 */
public class FillStateForm implements Comparable
{
        public FillStateForm(){}

	public FillStateForm(Task task, TaskTime taskTime, UnitTreeNode unit, boolean filled, int flag, Date fillDate)
	{
		this.task = task;
		this.taskTime = taskTime;
		this.unit = unit;
		this.filled = filled;
		this.flag = flag;
		this.fillDate = fillDate;
	}

	/**
	 * 任务
	 */
	private Task task;

	/**
	 * 任务时间
	 */
	private TaskTime taskTime;

	/**
	 * 单位
	 */
	private UnitTreeNode unit;

	/**
	 * 是否已经填报
	 */
	private boolean filled;

	/**
	 * 标识位
	 * 0x00000001 表示审核
	 */
	private int flag;

	/** 填报日期 */
	private Date fillDate;

	public boolean isFilled()
	{
		return filled;
	}

	public Task getTask()
	{
		return task;
	}

	public TaskTime getTaskTime()
	{
		return taskTime;
	}

	public UnitTreeNode getUnit()
	{
		return unit;
	}

	public Date getFillDate()
	{
		return fillDate;
	}

	public int getFlag()
	{
		return flag;
	}

	public int compareTo(Object o)
	{
		FillStateForm obj = (FillStateForm) o;
		return this.getUnit().id().compareTo(obj.getUnit().id());
	}
}
