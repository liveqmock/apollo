package cn.com.youtong.apollo.servlet.unittree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * ָ���ѯ��ʹ�õĵ�λhtml��
 */
public class ScalarQueryTree extends UnitTree
{

	private String taskID;
	public ScalarQueryTree(String taskID)
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
		return "";
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
//		return this.CHECKBOX_XLOAD_TREE;
		return this.RADIO_XLOAD_TREE;
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
