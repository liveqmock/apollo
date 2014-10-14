package cn.com.youtong.apollo.dictionary.db.form;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.builder.*;

/** @author Hibernate CodeGenerator */
public class DictionaryForm
	implements Serializable
{

	/** identifier field */
	private String dictid;

	/** persistent field */
	private String name;

    /** nullable persistent field */
    private java.util.Date dateCreated;

    /** nullable persistent field */
    private java.util.Date dateModified;

	/** nullable persistent field */
	private String memo;

	/** nullable persistent field */
	private String levelPosition;

	/** persistent field */
	private int keyLength;

	/** persistent field */
	private int flag;

	/** nullable persistent field */
	private String reserved1;

	/** nullable persistent field */
	private String reserved2;

	/** nullable persistent field */
	private String reserved3;

	/** DictionaryEntries */
	private Set dictionaryEntries = new HashSet();

	/** full constructor */
	public DictionaryForm(java.lang.String dictid, java.lang.String name, java.lang.String memo, java.lang.String levelPosition, int keyLength, int flag, java.lang.String reserved1,
						  java.lang.String reserved2, java.lang.String reserved3)
	{
		this.dictid = dictid;
		this.name = name;
		this.memo = memo;
		this.levelPosition = levelPosition;
		this.keyLength = keyLength;
		this.flag = flag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
	}

	/** default constructor */
	public DictionaryForm()
	{
	}

	/** minimal constructor */
	public DictionaryForm(java.lang.String dictid, java.lang.String name, int keyLength, int flag)
	{
		this.dictid = dictid;
		this.name = name;
		this.keyLength = keyLength;
		this.flag = flag;
	}

	public java.lang.String getDictid()
	{
		return this.dictid;
	}

	public java.lang.String getName()
	{
		return this.name;
	}

	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	public java.lang.String getMemo()
	{
		return this.memo;
	}

	public void setMemo(java.lang.String memo)
	{
		this.memo = memo;
	}

	public java.lang.String getLevelPosition()
	{
		return this.levelPosition;
	}

	public void setLevelPosition(java.lang.String levelPosition)
	{
		this.levelPosition = levelPosition;
	}

	public int getKeyLength()
	{
		return this.keyLength;
	}

	public void setKeyLength(int keyLength)
	{
		this.keyLength = keyLength;
	}

	public int getFlag()
	{
		return this.flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public java.lang.String getReserved1()
	{
		return this.reserved1;
	}

	public void setReserved1(java.lang.String reserved1)
	{
		this.reserved1 = reserved1;
	}

	public java.lang.String getReserved2()
	{
		return this.reserved2;
	}

	public void setReserved2(java.lang.String reserved2)
	{
		this.reserved2 = reserved2;
	}

	public java.lang.String getReserved3()
	{
		return this.reserved3;
	}

	public void setReserved3(java.lang.String reserved3)
	{
		this.reserved3 = reserved3;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("dictid", getDictid()).toString();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof DictionaryForm))
		{
			return false;
		}
		DictionaryForm castOther = (DictionaryForm) other;
		return new EqualsBuilder().append(this.getDictid(), castOther.getDictid()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getDictid()).toHashCode();
	}

	public void setDictid(String dictid)
	{
		this.dictid = dictid;
	}

	public Set getDictionaryEntries()
	{
		return dictionaryEntries;
	}

	public void setDictionaryEntries(Set dictionaryEntries)
	{
		this.dictionaryEntries = dictionaryEntries;
	}
    public java.util.Date getDateCreated()
    {
        return dateCreated;
    }
    public java.util.Date getDateModified()
    {
        return dateModified;
    }
    public void setDateCreated(java.util.Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    public void setDateModified(java.util.Date dateModified)
    {
        this.dateModified = dateModified;
    }

}
