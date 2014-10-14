/*
 * Created on 2003-10-21
 * �ֵ���Ŀ����
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
	 * �ô����ֵ���Ŀ���¼�����Ŀ
	 */
	private TreeSet children = new TreeSet();
	/**
	 * ���캯��
	 * @param form �ֵ���Ŀ��form of hibernate
	 */
	public DBDictionaryEntry(DictionaryEntryForm form)
	{
		this.form = form;
	}

	/**
	 * �õ��ֵ���Ŀ��ֵ
	 * @return ��ֵ
	 */
	public Object getKey()
	{
		return form.getEntryKey();
	}

	/**
	 * �����ֵ���Ŀ����ֵ
	 * @param value ����ֵ
	 */
	public Object setValue(Object value)
	{
		Object oldValue = form.getEntryValue();
		form.setEntryValue((String) value);
		return oldValue;
	}

	/**
	 * �õ��ֵ���Ŀ����ֵ
	 * @return ����ֵ
	 */
	public Object getValue()
	{
		return form.getEntryValue();
	}

	/**
	 * �õ��ֵ���Ŀ���ӽ��
	 * @return �ֵ���Ŀ�ӽ��
	 */
	public TreeSet getChildren()
	{
		return children;
	}

	/**
	 * �����ֵ���Ŀ���ӽ��
	 */
	public void setChildren(TreeSet children)
	{
		this.children = children;
	}

	/**
	 * �Ƚϴ�С
	 * @param entry
	 * @return int
	 */
	public int compareTo(Object entry)
	{
		return form.getEntryKey().compareTo(((DBDictionaryEntry) entry).getKey().toString());
	}
}
