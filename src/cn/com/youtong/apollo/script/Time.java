package cn.com.youtong.apollo.script;

/**
 * Task time
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */

import cn.com.youtong.apollo.task.TaskTime;

public class Time
{
	protected TaskTime time;

    public Time(TaskTime time)
    {
		this.time= time;
    }

	public String toString()
	{
		return time.toString();
	}

	public TaskTime getTaskTime()
	{
		return time;
	}

	/**
	 * ��һ������ʱ��
	 * @return
	 */
	public Time getPre()
	{
		/**
		 * @todo add your code here
		 */
		throw new UnsupportedOperationException ();
	}

	/**
	 * ȥ��ͬ������ʱ��
	 * @return
	 */
	public Time getPreYear()
	{
		/**
		 * @todo add your code here
		 */
		throw new UnsupportedOperationException ();
	}
}