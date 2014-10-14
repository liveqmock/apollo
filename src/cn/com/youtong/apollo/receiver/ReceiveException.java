package cn.com.youtong.apollo.receiver;

import cn.com.youtong.apollo.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ReceiveException extends Warning
{
	public ReceiveException()
	{
	}

	public ReceiveException(String s)
	{
		super(s);
	}

    public ReceiveException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ReceiveException(Throwable cause)
    {
        super(cause);
    }

}