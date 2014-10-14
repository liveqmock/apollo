package cn.com.youtong.apollo.dictionary;

import java.io.*;
import java.util.*;

/**
 * Dictionary manager interface.
 * Dictionary is shared by tasks.
 * �����ֵ�Ȩ���ɽ�ɫȨ�޿��ƣ���2��Ȩ�ޣ�delete, update.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
public interface DictionaryManager
{
	/**
	 * Get all available dictionaries.
	 * @return Only return dictionary ids, exch iterator element is a String Object.
	 * @throws DictionaryException
	 */
	Iterator getAllDictionaries()
		throws DictionaryException;

	/**
	 * Get dictionary by id.
	 * @param id
	 * @return return dictionary if existed, throws exception
	 * if specfic id dictionary ot existed.
	 * @throws DictionaryException if failed get dictionary.
	 */
    Dictionary getDictionaryByID(String id)
		throws DictionaryException;

	/**
	 * Delete specfic dictionary
	 * @param id dictionary id
	 * @throws DictionaryException
	 */
     void deleteDictionary(String id)
		throws DictionaryException;

     /**
      * Output specfic dictionary
      * @param id dictionary id
      * @param out output stream
      * @throws DictionaryException
      */
     void outPutDictionary(String id, OutputStream out)
         throws DictionaryException;

	/**
	 * ���´����ֵ䣬��������ֵ䲻�����򴴽��µĴ����ֵ�.
	 * ������Ϊxml������dictionary.xsd
	 * @param in input xml stream
	 * @throws DictionaryException throws exception if dictionary already existed,
	 * or internal errors.
	 * @throws IOException read stream failed.
	 */
	void updateDictionary(InputStream in)
		throws DictionaryException, IOException;
}