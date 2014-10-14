package cn.com.youtong.apollo.data.db;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.common.sql.*;
import java.util.*;
import org.apache.commons.logging.*;

/**
 * ��λ�����ݿ�ʵ��
 */
public class DBUnitTreeNode
	implements UnitTreeNode, Comparable
{
	private Log log = LogFactory.getLog(DBUnitTreeNode.class);

	public DBUnitTreeNode()
	{
            //Ĭ��Ϊ��������
            this.reportType="0";
	}

	/**��λID�ŵ�λ�ı�ʶ(һ��Ϊ: ��λ���� + ��������)*/
	private String id;
	/** ��λ���� */
	private String unitCode;
	/** �ϼ���λ���� */
	private String parentUnitCode;
	/** �ܲ����� */
	private String hqCode;
	/** ��λ���� */
	private String unitName;
	/**��������*/
	private String reportType;
	/** �����ϼ���λid */
	private String p_Parent;
	/**�Ƿ�ɼ�*/
	private int display;
    /** ѡ��������� */
    private String selectSumCondition;
	/** ���ڵ� */
	private DBUnitTreeNode parent;
	/** �¼���λUnit���� */
	private Collection children = new TreeSet();

	/**
	 * �õ�ֱ���¼���λ����
	 * @return each Iterator element is UnitTreeNode
	 */
	public java.util.Iterator getChildren()
	{
		return children.iterator();
	}

	/**
	 * �õ����ڵ�
	 * @return ���ڵ㣬��������ڣ�����null
	 */
	public UnitTreeNode getParent()
	{
		return this.parent;
	}

	/**
	 * ���ø��ڵ�
	 * @return ���ڵ㣬��������ڣ�����null
	 */
	public void setParent(DBUnitTreeNode parent)
	{
		this.parent = parent;
	}

	/**
	 * �õ���λ�ı�ʶ(һ��Ϊ: ��λ���� + ��������)
	 * @return ��λ�ı�ʶ
	 */
	public String id()
	{
		return this.id;
	}

	/**
	 * ��������
	 * @return ��������
	 */
	public String getReportType()
	{
		return this.reportType;
	}

	/**
	 * �õ��ܲ�����
	 * @return �ܲ�����
	 */
	public String getHQCode()
	{
		return hqCode;
	}

	/**
	 * �õ��ϼ���λ����
	 * @return �ϼ���λ����
	 */
	public String getParentUnitCode()
	{
		return parentUnitCode;
	}

	/**
	 * �õ���λ����
	 * @return ��λ����
	 */
	public String getUnitCode()
	{
		return unitCode;
	}

	/**
	 * �õ���λ����
	 * @return ��λ����
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
     * �Ƚϴ�С
     * @param node ��λ���ڵ�
     * @return int
     */
    public int compareTo(Object node)
    {
        DBUnitTreeNode unit = (DBUnitTreeNode) node;
        //��������������
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
            //��id����
            if (unit.unitName.indexOf("ֱ��") >= 0)
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
     * �жϵ�ǰ��λ�Ƿ���ָ����λɭ����ĳ�ŵ�λ�����ӽڵ�
     * @param forest ��λDBUnitTreeɭ��
     * @return ��ǰ��λ��ָ����λɭ����ĳ�ŵ�λ�����ӽڵ㣬����true�����򷵻�false;
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
     * �жϵ�ǰ��λ�Ƿ���ָ����λ�����ӽڵ�
     * @param tree ��λ��
     * @return ��ǰ��λ��ָ����λ�����ӽڵ㣬����true�����򷵻�false;
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
     * �жϵ�ǰ��λ�Ƿ���ָ���ĵ�λ����
     * @param tree ��λ��
     * @return ��ǰ��λ��ָ���ĵ�λ���У�����true�����򷵻�false
     */
    private boolean isInTree(DBUnitTreeNode tree)
    {
        if(equals(tree))
        {
            return true;
        }
        else
        {
            //�������ڹ�
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