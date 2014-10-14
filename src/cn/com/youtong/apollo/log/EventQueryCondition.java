package cn.com.youtong.apollo.log;

import java.util.*;

/**
 * 事件查询条件
 */
public class EventQueryCondition
{
	/**
	 * 	类型数组
	 */
	private int[] types;

	/**
	 * 时间范围--开始时间
	 */
	private Date startTime;

	/**
	 * 时间范围--结束时间
	 */
	private Date endTime;

	/**
	 * 产生事件的客户计算机的IP地址
	 */
	private String source;

	/**
	 * 触发事件的登录用户的名称
	 */
	private String userName;

	/**
	 * 事件的描述
	 */
	private String memo;

	public EventQueryCondition(int[] types, Date startTime, Date endTime,
							   String source, String userName, String memo)
	{
		this.types = types;
		this.startTime = startTime;
		this.endTime = endTime;
		this.source = source;
		this.userName = userName;
		this.memo = memo;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public String getMemo()
	{
		return memo;
	}

	public String getSource()
	{
		return source;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public int[] getTypes()
	{
		return types;
	}

	public String getUserName()
	{
		return userName;
	}

}