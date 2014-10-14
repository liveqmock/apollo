package cn.com.youtong.apollo.log;

import java.util.*;

/**
 * 事件接口
 */
public interface Event
{
	/**
	 * 错误类型
	 */
	public final int ERROR = 0;

	/**
	 * 警告类型
	 */
	public final int WARNING = 1;

	/**
	 * 信息类型
	 */
	public final int INFO = 2;

	/**
	 * 成功审核类型
	 */
	public final int AUDIT_SUCCESS = 3;

	/**
	 * 失败审核类型
	 */
	public final int AUDIT_FAIL = 4;

	/**
	 * 得到事件类型
	 * @return 事件类型
	 */
	public int getType();

	/**
	 * 得到事件发生的时间
	 * @return 事件发生的时间
	 */
	public Date getTimeOccured();

	/**
	 * 得到产生事件的客户计算机的IP地址
	 * @return 产生事件的客户计算机的IP地址
	 */
	public String getSource();

	/**
	 * 得到触发事件的登录用户的名称
	 * @return 触发事件的登录用户的名称
	 */
	public String getUserName();

	/**
	 * 得到事件ID，值越大，时间发生的时间越晚
	 * @return 事件ID
	 */
	public Integer getEventID();

	/**
	 * 得到事件的详细描述
	 * @return 事件的详细描述
	 */
	public String getMemo();
}