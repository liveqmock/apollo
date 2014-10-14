package cn.com.youtong.apollo.data.db;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.common.sql.*;
import java.util.*;
import org.apache.commons.logging.*;

/**
 * 单位的数据库实现
 */
public class DBUnitTreeNode
	implements UnitTreeNode, Comparable
{
	private Log log = LogFactory.getLog(DBUnitTreeNode.class);

	public DBUnitTreeNode()
	{
            //默认为单户报表
            this.reportType="0";
	}

	/**单位ID号单位的标识(一般为: 单位代码 + 报表类型)*/
	private String id;
	/** 单位代码 */
	private String unitCode;
	/** 上级单位代码 */
	private String parentUnitCode;
	/** 总部代码 */
	private String hqCode;
	/** 单位名称 */
	private String unitName;
	/**报表类型*/
	private String reportType;
	/** 隐含上级单位id */
	private String p_Parent;
	/**是否可见*/
	private int display;
    /** 选择汇总条件 */
    private String selectSumCondition;
	/** 父节点 */
	private DBUnitTreeNode parent;
	/** 下级单位Unit集合 */
	private Collection children = new TreeSet();

	/**
	 * 得到直接下级单位集合
	 * @return each Iterator element is UnitTreeNode
	 */
	public java.util.Iterator getChildren()
	{
		return children.iterator();
	}

	/**
	 * 得到父节点
	 * @return 父节点，如果不存在，返回null
	 */
	public UnitTreeNode getParent()
	{
		return this.parent;
	}

	/**
	 * 设置父节点
	 * @return 父节点，如果不存在，返回null
	 */
	public void setParent(DBUnitTreeNode parent)
	{
		this.parent = parent;
	}

	/**
	 * 得到单位的标识(一般为: 单位代码 + 报表类型)
	 * @return 单位的标识
	 */
	public String id()
	{
		return this.id;
	}

	/**
	 * 报表类型
	 * @return 报表类型
	 */
	public String getReportType()
	{
		return this.reportType;
	}

	/**
	 * 得到总部代码
	 * @return 总部代码
	 */
	public String getHQCode()
	{
		return hqCode;
	}

	/**
	 * 得到上级单位代码
	 * @return 上级单位代码
	 */
	public String getParentUnitCode()
	{
		return parentUnitCode;
	}

	/**
	 * 得到单位代码
	 * @return 单位代码
	 */
	public String getUnitCode()
	{
		return unitCode;
	}

	/**
	 * 得到单位名称
	 * @return 单位名称
	 */
	public String getUnitName()
	{
		return unitName;
	}

	public void setChildren(java.util.Collection children)
	{
		this.children = children;
	}
	public void addChild(DBUnitTreeNode dbUnitTreeNode)
	{
		this.children.add(dbUnitTreeNode);
	}

	public void setHQCode(String hqCode)
	{
		this.hqCode = hqCode;
	}

	public void setParentUnitCode(String parentUnitCode)
	{
		this.parentUnitCode = parentUnitCode;
	}

	public void setUnitCode(String unitCode)
	{
		this.unitCode = unitCode;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public void setID(String id)
	{
		this.id = id;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}
    public String getP_Parent()
    {
        return p_Parent;
    }
    public void setP_Parent(String p_Parent)
    {
        this.p_Parent = p_Parent;
    }

    /**
     * 比较大小
     * @param node 单位树节点
     * @return int
     */
    public int compareTo(Object node)
    {
        DBUnitTreeNode unit = (DBUnitTreeNode) node;
        //按报表类型排序
		if(reportType == null)
		{
			log.error("Report type is null, unit: " + this.unitCode);
			return 1;
		}

		if(unit.reportType == null)
		{
			log.error("Report type is null, unit: " + unit.unitCode);
			return -1;
		}

        int compare = this.reportType.compareTo(unit.reportType);
        if(compare == 0)
        {
            //按id排序
            if (unit.unitName.indexOf("直属") >= 0)
                return -1;
            return id.compareTo(unit.id);
        }
        else
        {
            return compare;
        }
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof DBUnitTreeNode)
        {
            DBUnitTreeNode unit = (DBUnitTreeNode)obj;
            return id.equals(unit.id);
        }
        else
        {
            return false;
        }
    }

    /**
     * 判断当前单位是否是指定单位森林中某颗单位树的子节点
     * @param forest 单位DBUnitTree森林
     * @return 当前单位是指定单位森林中某颗单位树的子节点，返回true；否则返回false;
     */
    public boolean isDescendantOf(Collection forest)
    {
        for(Iterator itr = forest.iterator(); itr.hasNext();)
        {
            DBUnitTreeNode tree = (DBUnitTreeNode)itr.next();
            if(isDescendantOf(tree))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前单位是否是指定单位树的子节点
     * @param tree 单位树
     * @return 当前单位是指定单位树的子节点，返回true；否则返回false;
     */
    public boolean isDescendantOf(DBUnitTreeNode tree)
    {
        if(equals(tree))
        {
            return false;
        }
        else
        {
            return isInTree(tree);
        }
    }

    /**
     * 判断当前单位是否在指定的单位树中
     * @param tree 单位树
     * @return 当前单位在指定的单位树中，返回true；否则返回false
     */
    private boolean isInTree(DBUnitTreeNode tree)
    {
        if(equals(tree))
        {
            return true;
        }
        else
        {
            //对子树第归
            for(Iterator itr = tree.children.iterator(); itr.hasNext();)
            {
                DBUnitTreeNode subTree = (DBUnitTreeNode)itr.next();
                if(isInTree(subTree))
                {
                    return true;
                }
            }
            return false;
        }
    }

    public String getSelectSumCondition()
    {
        return this.selectSumCondition;
    }

    public void setSelectSumCondition(String selectSumCondition)
    {
        this.selectSumCondition = selectSumCondition;
    }

	/**
	 * @return Returns the display.
	 */
	public int getDisplay() {
		return this.display;
	}

	/**
	 * @param display The display to set.
	 */
	public void setDisplay(int display) {
		this.display = display;
	}

}