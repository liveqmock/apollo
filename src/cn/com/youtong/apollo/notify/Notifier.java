package cn.com.youtong.apollo.notify;

/**
 * 通知接口。
 * 通知实现类必须实现多线程，对消息排队。
 *
 * <p>Title: </p>
 * <p>Description: 催报接口</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 世纪友通</p>
 * @author unascribed
 * @version 1.0
 */

public interface Notifier
{
	/**
	 * push message to message queue
	 * @param msg message to deliver
	 * @throws NotifyException
	 */
	void pushMessage(Message msg)
		throws NotifyException;

	/**
	 * close message queue delivery
	 */
	void close();
}