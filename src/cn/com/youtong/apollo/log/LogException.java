package cn.com.youtong.apollo.log;

import cn.com.youtong.apollo.common.*;

/**
 * log“Ï≥£
 */
public class LogException
	extends Warning
{
	public LogException()
	{
	}

	public LogException(String message)
	{
		super(message);
	}

	public LogException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public LogException(Throwable cause)
	{
		super(cause);
	}
}
