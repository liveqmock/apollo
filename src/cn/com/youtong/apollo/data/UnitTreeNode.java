package cn.com.youtong.apollo.data;

/**
 * 单位树节点
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

import java.util.*;

public interface UnitTreeNode
{
	/**
	 * 得到单位的标识(一般为: 单位代码 + 报表类型)
	 * @return 单位的标识
	 */
	String id();

	/**
	 * 得到单位代码
	 * @return 单位代码
	 */
    String getUnitCode();

	/**
	 * 得到单位名称
	 * @return 单位名称
	 */
    String getUnitName();

	/**
	 * 报表类型
	 * @return 报表类型
	 */
    String getReportType();

	/**
	 * 得到直接下级单位集合
	 * @return each Iterator element is UnitTreeNode
	 */
    Iterator getChildren();

	/**
	 * 得到上级单位代码
	 * @return 上级单位代码
	 */
    String getParentUnitCode();

	/**
	 * 得到总部代码
	 * @return 总部代码
	 */
    String getHQCode();

    /**
     * 得到隐含上级单位id
     * @return 隐含上级单位id
     */
    String getP_Parent();
    
    /**
     *是否可见
     */
    int getDisplay();

	/**
	 * 得到父节点
	 * @return 父节点，如果不存在，返回null
	 */
	UnitTreeNode getParent();

    /**
     * 得到选择汇总的条件
     * @return 选择汇总的条件
     */
    String getSelectSumCondition();
}