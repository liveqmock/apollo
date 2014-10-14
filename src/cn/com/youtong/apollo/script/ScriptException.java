package cn.com.youtong.apollo.script;

public class ScriptException extends cn.com.youtong.apollo.common.Warning
{
	public ScriptException()
	{
	}

	public ScriptException(String message)
	{
		super(message);
	}

	public ScriptException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ScriptException(Throwable cause)
	{
		super(cause);
	}

}