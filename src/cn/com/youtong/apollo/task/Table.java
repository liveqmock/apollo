package cn.com.youtong.apollo.task;

/**
 * 表格对象.
 * <ul>普通表属性
 * <li>1．	表ID
 * <li>2．	表名
 * <li>3．	是否参与汇总
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Table
{
	/** 是否参与汇总 */
	static final int FLAG_SUM = 0X00000001;

	/**
	 * Table id
	 * @return
	 */
	String id();

	/**
	 * Table name
	 * @return
	 */String getName();
    /**
     * 得到Label -- DBFieldName 的Map
     * Label和DBFieldName都是大写
     */
    Map getLabel2dbfieldMap();

	/**
	 * 取表的标志信息.
	 * @param flag
	 * @return
	 */
	boolean getFlag(int flag);

	/**
	 * Get all table cell
	 * @return each iterator is a CellObject
	 * @throws TaskException if error throw TaskException
	 */
	Iterator getAllCells();

	/**
	 * Get cell name by cell label
	 * @param label cell label Ex: A12
	 * @return Cell if specif label cell existed, else null
	 */
	Cell getCellByLabel(String label);

	/**
	 * Get cell by cell database field name
	 * @param fieldName cell database field name
	 * @return Cell if specif fieldName cell existed, else null
	 */
	 Cell getCellByDBFieldName(String fieldName);

	/**
	 * get table rows
	 * @return each itaraotr memenber is a Row object
	 */
	Iterator getRows();

        /**
         * get table views
         * @return each itaraotr memenber is a Row object
         */
        Iterator getViews();


	/**
	 * get table integer ID
	 * @return table integer ID
	 */
	Integer getTableID();
}