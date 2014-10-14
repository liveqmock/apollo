package cn.com.youtong.apollo.dictionary;

import cn.com.youtong.apollo.common.*;

/**
 * Dictionary operation exception.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public class DictionaryException extends Warning
{
	public DictionaryException()
	{
	}

	public DictionaryException(String message)
	{
		super(message);
	}

	public DictionaryException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DictionaryException(Throwable cause)
	{
		super(cause);
	}
}