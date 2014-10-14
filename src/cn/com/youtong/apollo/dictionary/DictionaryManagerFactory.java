package cn.com.youtong.apollo.dictionary;

import cn.com.youtong.apollo.dictionary.db.*;

/**
 * Dictionary manager factory.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface DictionaryManagerFactory
{
	/**
	 * Create dictionary manager
	 * @return dictionary manager created
	 * @throws DictionaryException
	 * @see DictionaryManager
	 */
	DictionaryManager createDictionaryManager()
		throws DictionaryException;
}