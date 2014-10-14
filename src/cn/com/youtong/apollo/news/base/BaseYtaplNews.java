package cn.com.youtong.apollo.news.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the ytapl_news table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ytapl_news"
 */

public abstract class BaseYtaplNews  implements Comparable, Serializable {

	public static String REF = "YtaplNews";
	public static String PROP_TASK_I_D = "TaskID";
	public static String PROP_TITLE = "Title";
	public static String PROP_CONTENT = "Content";
	public static String PROP_ID = "Id";


	// constructors
	public BaseYtaplNews () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseYtaplNews (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String title;
	private java.lang.String content;
	private java.lang.String taskID;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: title
	 */
	public java.lang.String getTitle () {
		return title;
	}

	/**
	 * Set the value related to the column: title
	 * @param title the title value
	 */
	public void setTitle (java.lang.String title) {
		this.title = title;
	}



	/**
	 * Return the value associated with the column: content
	 */
	public java.lang.String getContent () {
		return content;
	}

	/**
	 * Set the value related to the column: content
	 * @param content the content value
	 */
	public void setContent (java.lang.String content) {
		this.content = content;
	}



	/**
	 * Return the value associated with the column: taskID
	 */
	public java.lang.String getTaskID () {
		return taskID;
	}

	/**
	 * Set the value related to the column: taskID
	 * @param taskID the taskID value
	 */
	public void setTaskID (java.lang.String taskID) {
		this.taskID = taskID;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof cn.com.youtong.apollo.news.YtaplNews)) return false;
		else {
			cn.com.youtong.apollo.news.YtaplNews ytaplNews = (cn.com.youtong.apollo.news.YtaplNews) obj;
			if (null == this.getId() || null == ytaplNews.getId()) return false;
			else return (this.getId().equals(ytaplNews.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}