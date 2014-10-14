package cn.com.youtong.apollo.task;

/**
 * 用户原数据表.
 * 是否参与汇总
 * <ul> 基础信息
 * <li>1．	普通表的所有属性
 * <li>2．	单位代码
 * <li>3．	报表类型
 * <li>4．	上级单位代码
 * <li>5.   集团代码
 * <li>6．	单位名称
 * </ul>
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface UnitMetaTable extends Table
{
	/**
	 * 隐含上级单位id字段
	 */
	public final static String P_PARENT = "P_PARENT";

	/**
	 * 单位代码单元格
	 * @return
	 */
	Cell getUnitCodeCell();

	/**
	 * 报表类型单元格
	 * @return
	 */Cell getReportTypeCell();

	/**
	 * 上级单位代码单元格
	 * @return
	 */Cell getParentUnitCodeCell();

	/**
	 * 集团总部代码单元格
	 * @return
	 */Cell getHQCodeCell();

	/**
	 * 单位名称单元格
	 * @return
	 */Cell getUnitNameCell();
	 
	 /**
	  * 是否显示
	  */
	 Cell getDisplayCell();
}