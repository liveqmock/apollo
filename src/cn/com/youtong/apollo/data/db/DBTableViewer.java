package cn.com.youtong.apollo.data.db;

import cn.com.youtong.apollo.data.*;

public class DBTableViewer
	implements TableViewer, Comparable
{
	private String taskName;
	private String taskID;
	private String tableID;
	private String tableName;
	private String htmlString;
	private Integer intID;

	public DBTableViewer()
	{
	}

	public void setHtmlString(String htmlString)
	{
		this.htmlString = htmlString;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * 得到显示表的Html代码
	 * @return Html代码
	 */
	public String getHtmlString()
	{
		return htmlString;
	}

	/**
	 * 得到表名
	 * @return 表名
	 */
	public String getTableName()
	{
		return tableName;
	}

	public String getTableID()
	{
		return tableID;
	}

	public String getTaskID()
	{
		return taskID;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTableID(String tableID)
	{
		this.tableID = tableID;
	}

	public void setTaskID(String taskID)
	{
		this.taskID = taskID;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
    public Integer getIntID()
    {
	return intID;
    }
    public void setIntID(Integer intID)
    {
	this.intID = intID;
    }

	public int compareTo(Object obj)
	{
		DBTableViewer viewer = (DBTableViewer)obj;
		return this.intID.compareTo(viewer.intID);
	}
}