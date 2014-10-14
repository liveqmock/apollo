package cn.com.youtong.apollo.serialization;

import cn.com.youtong.apollo.common.*;

/**
 * ���л��쳣
 */
public class SerializeException extends Warning
{
	public SerializeException()
	{
	}

	public SerializeException(String message)
	{
		super(message);
	}

	public SerializeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SerializeException(Throwable cause)
	{
		super(cause);
	}
}