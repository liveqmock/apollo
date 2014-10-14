package cn.com.youtong.apollo.common;

/**
 * crm÷–µƒ“Ï≥£°£
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class Warning extends Exception
{

	public Warning()
	{
	}

	public Warning(String s)
	{
		super(s);
	}

	public Warning(String message, Throwable cause)
	{
		super(message, cause);
	}

	public Warning(Throwable cause)
	{
		super(cause);
	}
}