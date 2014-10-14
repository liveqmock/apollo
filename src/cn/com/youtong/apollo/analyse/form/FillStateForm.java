package cn.com.youtong.apollo.analyse.form;

import java.util.Date;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

/**
 * ����form
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
	 * ����
	 */
	private Task task;

	/**
	 * ����ʱ��
	 */
	private TaskTime taskTime;

	/**
	 * ��λ
	 */
	private UnitTreeNode unit;

	/**
	 * �Ƿ��Ѿ��
	 */
	private boolean filled;

	/**
	 * ��ʶλ
	 * 0x00000001 ��ʾ���
	 */
	private int flag;

	/** ����� */
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
