package cn.com.youtong.apollo.notify;

/**
 * Notify service
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.services.Config;

public final class NotifyService
{
	private Log log = LogFactory.getLog(this.getClass());

	private static NotifyService _notifyService;

	Hashtable _notifiers;

	protected NotifyService()
	{
		init();
	}

	/**
	 * initialize notify service
	 */
	void init()
	{
		_notifiers = new Hashtable();

		Hashtable notifiers = _notifiers;
		String[] notifierArray = Config.getStringArray("cn.com.youtong.apollo.notifiers");
		for(int i = 0; i < notifierArray.length; i++)
		{
			String notifiersProp = notifierArray[i];
			if(notifiersProp.equals("sms"))
			{
				notifiers.put("sms", new SmsNotifier());
			}
			else if(notifiersProp.equals("email"))
			{
				notifiers.put("email", new EmailNotifier());
			}
			else if(notifiersProp.equals("fax"))
			{
				notifiers.put("fax", new FaxNotifier());
			}
			else if(notifiersProp.equals("phone"))
			{
				notifiers.put("phone", new PhoneNotifier());
			}
		}

	}

	public static synchronized NotifyService instance()
	{
		if(_notifyService == null)
		{
			_notifyService = new NotifyService();
		}
		return _notifyService;
	}

	/**
	 * push notify to notify stack
	 * @param notifyClass notify class 值为sms,email,fax,phone中的一个
	 * @param msg message to be send
	 * @throws NotifyException
	 */
	public void pushNotify(String notifyClass, Message msg)
		throws NotifyException
	{
		if(_notifiers != null)
		{
			Notifier notifier = (Notifier) _notifiers.get(notifyClass);
			if(notifier != null)
			{
				notifier.pushMessage(msg);
			}
			else
			{
				log.error("不能找到消息通知实现：" + notifyClass);
				throw new NotifyException("不能找到消息通知实现：" + notifyClass);
			}
		}
	}

	/**
	 * close all notifier.
	 */
	public void close()
	{
		Iterator ite = _notifiers.entrySet().iterator();
		while(ite.hasNext())
		{
			Notifier notifier = (Notifier) ite.next();
			notifier.close();
		}
	}
}