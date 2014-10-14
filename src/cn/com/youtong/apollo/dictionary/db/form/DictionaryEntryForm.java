package cn.com.youtong.apollo.dictionary.db.form;

import java.io.*;

/** @author Hibernate CodeGenerator */
public class DictionaryEntryForm
	implements Serializable
{

	/** identifier field */

	/** persistent field */
	private String entryKey;

	/** persistent field */
	private String entryValue;

	/** persistent field */

	/** nullable persistent field */

	/** nullable persistent field */

	/** nullable persistent field */

	/** persistent field */

	/** full constructor */
	public DictionaryEntryForm(java.lang.String entryKey, java.lang.String entryValue)
	{
		this.entryKey = entryKey;
		this.entryValue = entryValue;
	}

	/** default constructor */
	public DictionaryEntryForm()
	{
	}

	public java.lang.String getEntryKey()
	{
		return this.entryKey;
	}

	public void setEntryKey(java.lang.String entryKey)
	{
		this.entryKey = entryKey;
	}

	public java.lang.String getEntryValue()
	{
		return this.entryValue;
	}

	public void setEntryValue(java.lang.String entryValue)
	{
		this.entryValue = entryValue;
	}
}
