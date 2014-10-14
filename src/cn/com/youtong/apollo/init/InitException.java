package cn.com.youtong.apollo.init;

public class InitException extends Exception
{
    public InitException()
    {
    }
	public InitException(String s)
	{
		super(s);
	}
	public InitException(Throwable e)
	{
		super(e);
	}
	public InitException(String s,Throwable e)
	{
		super(s,e);
	}
}