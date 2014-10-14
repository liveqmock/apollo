package cn.com.youtong.apollo.script;

/**
 * 脚本对象
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

import java.util.Iterator;

public interface ScriptObject
{
	/**
	 * Get dynamic fields
	 * @return
	 */
	Iterator s_getFields();

	/**
	 * is field of object
	 * @param field field name
	 * @return true - if is field of object
	 */
	boolean isField(String field);

	/**
	 * Get field value
	 * @param key
	 * @return
	 */
	Object s_getField(String key);

	/**
	 * set field value
	 * @param key
	 * @param value
	 */
	void s_setField(String key, Object value);
}