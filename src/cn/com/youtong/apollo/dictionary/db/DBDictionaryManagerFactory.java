package cn.com.youtong.apollo.dictionary.db;

import cn.com.youtong.apollo.dictionary.*;
import cn.com.youtong.apollo.services.*;

public class DBDictionaryManagerFactory extends DefaultFactory implements DictionaryManagerFactory
{

	/**
	 * DictionaryManager¾²Ì¬ÊµÀý
	 */
	private static DictionaryManager manager = new DBDictionaryManager();

    public DBDictionaryManagerFactory()
    {
    }

	/**
	 * Create dictionary manager
	 * @return dictionary manager created
	 * @throws DictionaryException
	 * @see DictionaryManager
	 */
	public DictionaryManager createDictionaryManager()
		throws DictionaryException
	{
		return DBDictionaryManagerFactory.manager;
	}
}