package cn.com.youtong.apollo.data;

import cn.com.youtong.apollo.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelException extends Warning
{
	public ModelException()
	{
	}

	public ModelException(String message)
	{
		super(message);
	}

	public ModelException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ModelException(Throwable cause)
	{
		super(cause);
	}

}