/*
 * Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import java.util.Comparator;
import java.util.Date;

import cn.com.youtong.apollo.task.TaskTime;
import cn.com.youtong.apollo.task.db.form.TaskTimeForm;

/**
 * @author wjb
 */
public class DBTaskTime
	implements TaskTime, Comparable
{
	private Integer _ID;
	private Date _begin;
	private Date _end;
	private Date _submitBegin;
	private Date _submitEnd;
	private Date _attenBegin;
	private Date _attenEnd;

	public DBTaskTime(TaskTimeForm form)
	{
		this._ID = form.getTaskTimeID();
		this._begin = form.getBeginTime();
		this._end = form.getEndTime();
		this._submitBegin = form.getSubmitBeginTime();
		this._submitEnd = form.getSubmitEndTime();
		this._attenBegin = form.getAttentionBeginTime();
		this._attenEnd = form.getAttentionEndTime();
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getFromTime()
	 */
	public Date getFromTime()
	{
		return _begin;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getEndTime()
	 */
	public Date getEndTime()
	{
		return _end;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getSubmitFromTime()
	 */
	public Date getSubmitFromTime()
	{
		return _submitBegin;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getSubmitEndTime()
	 */
	public Date getSubmitEndTime()
	{
		return _submitEnd;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getAttentionFromTime()
	 */
	public Date getAttentionFromTime()
	{
		return _attenBegin;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.TaskTime#getAttentionEndTime()
	 */
	public Date getAttentionEndTime()
	{
		return _attenEnd;
	}

	/**
	 * @return 任务时间id
	 */
	public Integer getTaskTimeID()
	{
		return _ID;
	}

    /**
     * 比较大小
     * @param other
     * @return
     */
    public int compareTo(Object other)
    {
        return this.getFromTime().compareTo(((TaskTime)other).getFromTime());
    }
}

class TaskTimeComparator implements Comparator
{
	public int compare( Object one, Object other )
	{
		// throw ClassCastException if they are not DBTaskTime object
		DBTaskTime oneTime = (DBTaskTime) one;
		DBTaskTime otherTime = (DBTaskTime) other;

		return oneTime.getFromTime().compareTo( otherTime.getFromTime() );
	}

	public boolean equals( Object other )
	{
		return super.equals( other );
	}
}