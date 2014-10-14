package cn.com.youtong.apollo.task;

/**
 * 单元格数据类型.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface CellDataType
{
	/** 数值型 */
	static final int TYPE_NUMERIC = 1;
	/** 文本型 */
	static final int TYPE_TEXT = 2;
	/** 二进制 */
	static final int TYPE_BLOB = 3;
	/** 大文本 */
	static final int TYPE_CLOB = 4;
	/** 日期型 */
	static final int TYPE_DATE = 5;
}