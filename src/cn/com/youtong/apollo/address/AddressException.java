package cn.com.youtong.apollo.address;

import cn.com.youtong.apollo.common.Warning;

public class AddressException extends Warning
{
	public AddressException()
	{
	}

	public AddressException(String message)
	{
		super(message);
	}

	public AddressException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AddressException(Throwable cause)
	{
		super(cause);
	}
}
