package cn.com.youtong.apollo.servlet.unittree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * ������䵥λȨ�޵�html��
 */
public class AddressInfoTree extends UnitTree
{
	private String taskID;
	public AddressInfoTree(String taskID)
	{
		super(taskID);
		this.taskID = taskID;
	}


	/**
	 * ָ������ڵ�ʱ���������Ķ�����һ���Ǹ������javascript ������
	 * @param node �ڵ����
	 * @return �����ַ���
	 */
	protected String onClickAction(UnitTreeNode node)
	{
		return "javascript:changeUnit('" + node.id() + "','" + node.getUnitName() + "')";
	}

	/**
	 * ��������
	 * this.CHECKBOX_STATIC_TREE  checkbox��ľ�̬������һ����װ�أ�
	 * this.CHECKBOX_XLOAD_TREE   checkbox��Ķ�̬���������װ�أ�
	 * this.RADIO_STATIC_TREE     radio��ľ�̬��
	 * this.RADIO_XLOAD_TREE      radio��Ķ�̬��
	 * this.DEFAULT_TREE          Ĭ��������ʽ����ѡ���
	 * @return �����ַ���
	 */
	protected String treeStyle()
	{
		return this.DEFAULT_TREE;
	}

	/**
	 * ���Ĭ�����������ʽ��ѡ����ֵ
	 * @param node ���ڵ����
	 * @return ѡ����ֵ
	 */
	protected String checkValue(UnitTreeNode node)
	{
		return node.id();
	}
}
