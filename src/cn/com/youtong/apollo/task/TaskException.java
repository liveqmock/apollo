package cn.com.youtong.apollo.task;

import cn.com.youtong.apollo.common.*;

/**
 * Task Exception
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public class TaskException extends Warning
{
	public TaskException()
	{
	}

	public TaskException(String message)
	{
		super(message);
	}

	public TaskException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TaskException(Throwable cause)
	{
		super(cause);
	}
}