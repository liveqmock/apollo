package cn.com.youtong.statistics;

public class SAException extends Exception
{
    public SAException()
	{
		super();
    }

	public SAException( String msg )
	{
		super( msg );
	}

	public SAException( Exception parent )
	{
		super( parent );
	}

	public SAException( String msg, Exception parent )
	{
		super( msg, parent );
	}
}