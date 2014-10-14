/*
 * Created on 2003-10-21
 * 字典条目管理
 */
package cn.com.youtong.apollo.dictionary.db;

import java.util.*;

import cn.com.youtong.apollo.dictionary.db.form.*;

/**
 * @author wjb
 */
public class DBDictionaryEntry
	implements Map.Entry, Comparable
{
	private DictionaryEntryForm form;
	/**
	 * 该代码字典条目的下级子条目
	 */
	private TreeSet children = new TreeSet();
	/**
	 * 构造函数
	 * @param form 字典条目的form of hibernate
	 */
	public DBDictionaryEntry(DictionaryEntryForm form)
	{
		this.form = form;
	}

	/**
	 * 得到字典条目键值
	 * @return 键值
	 */
	public Object getKey()
	{
		return form.getEntryKey();
	}

	/**
	 * 设置字典条目内容值
	 * @param value 内容值
	 */
	public Object setValue(Object value)
	{
		Object oldValue = form.getEntryValue();
		form.setEntryValue((String) value);
		return oldValue;
	}

	/**
	 * 得到字典条目内容值
	 * @return 内容值
	 */
	public Object getValue()
	{
		return form.getEntryValue();
	}

	/**
	 * 得到字典条目的子结点
	 * @return 字典条目子结点
	 */
	public TreeSet getChildren()
	{
		return children;
	}

	/**
	 * 设置字典条目的子结点
	 */
	public void setChildren(TreeSet children)
	{
		this.children = children;
	}

	/**
	 * 比较大小
	 * @param entry
	 * @return int
	 */
	public int compareTo(Object entry)
	{
		return form.getEntryKey().compareTo(((DBDictionaryEntry) entry).getKey().toString());
	}
}
