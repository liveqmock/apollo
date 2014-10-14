package cn.com.youtong.apollo.usermanager;

import cn.com.youtong.apollo.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class UserManagerException extends Warning
{
	public UserManagerException()
	{
	}

	public UserManagerException(String s)
	{
		super(s);
	}

	public UserManagerException(Throwable e)
	{
		super(e);
	}

	public UserManagerException(String s,Throwable e)
	{
		super(s,e);
	}
}