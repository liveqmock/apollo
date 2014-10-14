package cn.com.youtong.apollo.log;

import java.util.*;

/**
 * �¼���ѯ����
 */
public class EventQueryCondition
{
	/**
	 * 	��������
	 */
	private int[] types;

	/**
	 * ʱ�䷶Χ--��ʼʱ��
	 */
	private Date startTime;

	/**
	 * ʱ�䷶Χ--����ʱ��
	 */
	private Date endTime;

	/**
	 * �����¼��Ŀͻ��������IP��ַ
	 */
	private String source;

	/**
	 * �����¼��ĵ�¼�û�������
	 */
	private String userName;

	/**
	 * �¼�������
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