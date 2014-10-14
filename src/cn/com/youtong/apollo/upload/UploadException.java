package cn.com.youtong.apollo.upload;

import cn.com.youtong.apollo.common.Warning;

public class UploadException extends Warning
{
	public UploadException()
	{
	}

	public UploadException(String message)
	{
		super(message);
	}

	public UploadException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public UploadException(Throwable cause)
	{
		super(cause);
	}
}