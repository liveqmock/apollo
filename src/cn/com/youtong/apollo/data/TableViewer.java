package cn.com.youtong.apollo.data;

/**
 * <p>Title: 值对象，用来保存显示数据用的Html代码</p>
 * <p>Description: 有两项重要内容，一是可以得到表的名称，二是得到table的Html代码</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public interface TableViewer
{

	/**
	 * 得到任务名
	 * @return 任务名
	 */
	public String getTaskName();


	/**
	 * 得到任务ID
	 * @return 任务ID
	 */
	public String getTaskID();


	/**
	 * 得到表名
	 * @return 表名
	 */
	public String getTableName();

	/**
	 * 得到表ID
	 * @return 表ID
	 */
	public String getTableID();


	/**
	 * 得到显示表的Html代码
	 * @return Html代码
	 */
	public String getHtmlString();
}