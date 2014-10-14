package cn.com.youtong.apollo.notify;

import cn.com.youtong.apollo.common.*;

public class NotifyException extends Warning
{
	public NotifyException()
	{
	}

	public NotifyException(String message)
	{
		super(message);
	}

	public NotifyException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotifyException(Throwable cause)
	{
		super(cause);
	}
}
