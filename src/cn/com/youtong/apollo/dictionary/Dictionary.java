package cn.com.youtong.apollo.dictionary;

/**
 *  �����ֵ�.
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Dictionary extends Map
{
	/**
	 * ֻ��ѡȡҶ�ڵ�.
	 */
	static final int FLAG_ONLY_SELECT_LEAF = 0x00000001;

	/**
	 * �����ֵ�ID.
	 * @return
	 */
	String id();

	/**
	 * �����ֵ�����.
	 * @return
	 */String getName();

	/**
	 * ע��.
	 * @return
	 */String getMemo();

	/**
	 * ���볤��.
	 * @return
	 */int getKeyLegth();

	/**
	 * ���뼶��,����ʱ��,��: 2,2,2
	 * @return
	 */
	int[] getLevels();

	/**
	 * �����ֵ��־
	 * @param flag
	 * @return
	 */
	boolean getFlag(int flag);

	/**
	 * ȡ�����ֵ������ڵ㼯��
	 * @return each item is a Map.Entry object
	 */
	Iterator getRootItems();

	/**
	 * ȡ�����ֵ����ڵ���ӽڵ�
	 * @param key dictionary keyword
	 * @return each item is a Map.Entry object
	 */Iterator getChildItems(String key);
}