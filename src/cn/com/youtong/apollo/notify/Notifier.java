package cn.com.youtong.apollo.notify;

/**
 * ֪ͨ�ӿڡ�
 * ֪ͨʵ�������ʵ�ֶ��̣߳�����Ϣ�Ŷӡ�
 *
 * <p>Title: </p>
 * <p>Description: �߱��ӿ�</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������ͨ</p>
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