package cn.com.youtong.apollo.servlet.dictionarytree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * ������䵥λȨ�޵�html��
 */
public class DefaultDictionaryTree extends DictionaryTree
{
	public DefaultDictionaryTree()
	{
		super();
	}


	/**
	 * ָ������ڵ�ʱ���������Ķ�����һ���Ǹ������javascript ������
	 * @param node �ڵ����
	 * @return �����ַ���
	 */
	protected String onClickAction(String dictionaryID)
	{
		return "javascript:changeUnit('" + dictionaryID+ "')";
	}

}
