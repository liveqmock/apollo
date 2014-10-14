package cn.com.youtong.apollo.task.db.form;

import java.io.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class TaskTimeForm
	implements Serializable
{

	/** identifier field */
	private Integer taskTimeID;

	/** nullable persistent field */
	private java.util.Date beginTime;

	/** nullable persistent field */
	private java.util.Date endTime;

	/** nullable persistent field */
	private java.util.Date submitBeginTime;

	/** nullable persistent field */
	private java.util.Date submitEndTime;

	/** nullable persistent field */
	private java.util.Date attentionBeginTime;

	/** nullable persistent field */
	private java.util.Date attentionEndTime;

	/** persistent field */
	private int flag;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** persistent field */
	private cn.com.youtong.apollo.task.db.form.TaskForm task;

	/** full constructor */
	public TaskTimeForm(java.lang.Integer taskTimeID, java.util.Date beginTime, java.util.Date endTime, java.util.Date submitBeginTime, java.util.Date submitEndTime, java.util.Date attentionBeginTime,
						java.util.Date attentionEndTime, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, cn.com.youtong.apollo.task.db.form.TaskForm task)
	{
		this.taskTimeID = taskTimeID;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.submitBeginTime = submitBeginTime;
		this.submitEndTime = submitEndTime;
		this.attentionBeginTime = attentionBeginTime;
		this.attentionEndTime = attentionEndTime;
		this.flag = flag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.task = task;
	}

	/** default constructor */
	public TaskTimeForm()
	{
	}

	/** minimal constructor */
	public TaskTimeForm(java.lang.Integer taskTimeID, int flag, cn.com.youtong.apollo.task.db.form.TaskForm task)
	{
		this.taskTimeID = taskTimeID;
		this.flag = flag;
		this.task = task;
	}

	public java.lang.Integer getTaskTimeID()
	{
		return this.taskTimeID;
	}

	public void setTaskTimeID(java.lang.Integer taskTimeID)
	{
		this.taskTimeID = taskTimeID;
	}

	public java.util.Date getBeginTime()
	{
		return this.beginTime;
	}

	public void setBeginTime(java.util.Date beginTime)
	{
		this.beginTime = beginTime;
	}

	public java.util.Date getEndTime()
	{
		return this.endTime;
	}

	public void setEndTime(java.util.Date endTime)
	{
		this.endTime = endTime;
	}

	public java.util.Date getSubmitBeginTime()
	{
		return this.submitBeginTime;
	}

	public void setSubmitBeginTime(java.util.Date submitBeginTime)
	{
		this.submitBeginTime = submitBeginTime;
	}

	public java.util.Date getSubmitEndTime()
	{
		return this.submitEndTime;
	}

	public void setSubmitEndTime(java.util.Date submitEndTime)
	{
		this.submitEndTime = submitEndTime;
	}

	public java.util.Date getAttentionBeginTime()
	{
		return this.attentionBeginTime;
	}

	public void setAttentionBeginTime(java.util.Date attentionBeginTime)
	{
		this.attentionBeginTime = attentionBeginTime;
	}

	public java.util.Date getAttentionEndTime()
	{
		return this.attentionEndTime;
	}

	public void setAttentionEndTime(java.util.Date attentionEndTime)
	{
		this.attentionEndTime = attentionEndTime;
	}

	public int getFlag()
	{
		return this.flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public java.lang.String getReserved1()
	{
		return this.reserved1;
	}

	public void setReserved1(java.lang.String reserved1)
	{
		this.reserved1 = reserved1;
	}

	public java.lang.String getReserved2()
	{
		return this.reserved2;
	}

	public void setReserved2(java.lang.String reserved2)
	{
		this.reserved2 = reserved2;
	}

	public java.lang.String getReserved3()
	{
		return this.reserved3;
	}

	public void setReserved3(java.lang.String reserved3)
	{
		this.reserved3 = reserved3;
	}

	// -----------------------------------------------------------
	// TaskTime 和 Task 之间本应该是单向的关系，但是单向时，
	// Hibernate 无法维护之间的关系（待解决）
	// -----------------------------------------------------------
	public cn.com.youtong.apollo.task.db.form.TaskForm getTask()
	{
		return this.task;
	}

	public void setTask(cn.com.youtong.apollo.task.db.form.TaskForm task)
	{
		this.task = task;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("taskTimeID", getTaskTimeID()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof TaskTimeForm))
		{
			return false;
		}
		TaskTimeForm castOther = (TaskTimeForm) other;
		return new EqualsBuilder().append(this.getTaskTimeID(), castOther.getTaskTimeID()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getTaskTimeID()).toHashCode();
	}

}
