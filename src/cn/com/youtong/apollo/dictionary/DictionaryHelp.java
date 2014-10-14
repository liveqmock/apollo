package cn.com.youtong.apollo.dictionary;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import cn.com.youtong.apollo.services.Factory;

public class DictionaryHelp {

  /**
   * 得到转化值
   * @param dicID
   * @param value
   * @return
   */
  public static String getValue(String dictionaryID, String value) {
    try{
      DictionaryManager dictionaryManager = ( (DictionaryManagerFactory)
                                             Factory.
                                             getInstance(
          DictionaryManagerFactory.class.
          getName())).createDictionaryManager();
      cn.com.youtong.apollo.dictionary.Dictionary dictionary =
          dictionaryManager.
          getDictionaryByID(dictionaryID);
      String ret= (String)dictionary.get(value);
      if(ret!=null)
        return ret;
      else
        return "";
    }
    catch(Exception ex)
    {
       return "";
    }
  }

}