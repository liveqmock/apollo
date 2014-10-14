package cn.com.youtong.apollo.notify;

import java.util.*;

/**
 * 催报消息
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

public class Message
{
	protected Map _props;

	public Message(Map props)
	{
		_props = props;
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Object getProperty(String key)
	{
		return _props.get(key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Object getProperty(Object key)
	{
		return _props.get(key);
	}
}