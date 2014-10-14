package cn.com.youtong.apollo.analyse;

import cn.com.youtong.apollo.common.Warning;

public class AnalyseException extends Warning
{
	public AnalyseException()
	{
	}

	public AnalyseException(String message)
	{
		super(message);
	}

	public AnalyseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AnalyseException(Throwable cause)
	{
		super(cause);
	}
}
