package cn.com.youtong.apollo.dictionary;

import java.io.*;
import java.util.*;

/**
 * Dictionary manager interface.
 * Dictionary is shared by tasks.
 * 代码字典权限由角色权限控制，有2个权限：delete, update.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
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
	 * 更新代码字典，如果代码字典不存在则创建新的代码字典.
	 * 输入流为xml流，见dictionary.xsd
	 * @param in input xml stream
	 * @throws DictionaryException throws exception if dictionary already existed,
	 * or internal errors.
	 * @throws IOException read stream failed.
	 */
	void updateDictionary(InputStream in)
		throws DictionaryException, IOException;
}