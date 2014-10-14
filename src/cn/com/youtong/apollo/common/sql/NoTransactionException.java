/*
 * Created on 2003-10-13
 */
package cn.com.youtong.apollo.common.sql;

/**
 * @author wjb
 */
public class NoTransactionException extends Exception
{
	public NoTransactionException()
	{
		super();
	}

	public NoTransactionException(String msg)
	{
		super(msg);
	}
}
