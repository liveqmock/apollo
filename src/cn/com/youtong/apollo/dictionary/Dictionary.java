package cn.com.youtong.apollo.dictionary;

/**
 *  代码字典.
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
	 * 只能选取叶节点.
	 */
	static final int FLAG_ONLY_SELECT_LEAF = 0x00000001;

	/**
	 * 代码字典ID.
	 * @return
	 */
	String id();

	/**
	 * 代码字典名称.
	 * @return
	 */String getName();

	/**
	 * 注释.
	 * @return
	 */String getMemo();

	/**
	 * 代码长度.
	 * @return
	 */int getKeyLegth();

	/**
	 * 代码级别,建树时用,如: 2,2,2
	 * @return
	 */
	int[] getLevels();

	/**
	 * 代码字典标志
	 * @param flag
	 * @return
	 */
	boolean getFlag(int flag);

	/**
	 * 取代码字典树根节点集合
	 * @return each item is a Map.Entry object
	 */
	Iterator getRootItems();

	/**
	 * 取代码字典树节点的子节点
	 * @param key dictionary keyword
	 * @return each item is a Map.Entry object
	 */Iterator getChildItems(String key);
}