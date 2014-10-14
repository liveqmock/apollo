package cn.com.youtong.apollo.task;

/**
 * 行对象，行对象有浮动等情况比列要复杂,所以有行对象，却无需要有列对象.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Row
{
	/**
	 * 浮动行标识
	 */
	static final int FLAG_FLOAT_ROW = 0x00000001;

	/**
	 * 取得行标识
	 * @return 行标识
	 */
	String id();

	/**
	 * 行的单元格
	 * @return each element is a Cell object
	 */Iterator getCells();

	/**
	 * Get row attribute
	 * @param flag
	 * @return
	 */boolean getFlag(int flag);
}