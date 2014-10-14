package cn.com.youtong.apollo.data;

/**
 * ��λ���ڵ�
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */

import java.util.*;

public interface UnitTreeNode
{
	/**
	 * �õ���λ�ı�ʶ(һ��Ϊ: ��λ���� + ��������)
	 * @return ��λ�ı�ʶ
	 */
	String id();

	/**
	 * �õ���λ����
	 * @return ��λ����
	 */
    String getUnitCode();

	/**
	 * �õ���λ����
	 * @return ��λ����
	 */
    String getUnitName();

	/**
	 * ��������
	 * @return ��������
	 */
    String getReportType();

	/**
	 * �õ�ֱ���¼���λ����
	 * @return each Iterator element is UnitTreeNode
	 */
    Iterator getChildren();

	/**
	 * �õ��ϼ���λ����
	 * @return �ϼ���λ����
	 */
    String getParentUnitCode();

	/**
	 * �õ��ܲ�����
	 * @return �ܲ�����
	 */
    String getHQCode();

    /**
     * �õ������ϼ���λid
     * @return �����ϼ���λid
     */
    String getP_Parent();
    
    /**
     *�Ƿ�ɼ�
     */
    int getDisplay();

	/**
	 * �õ����ڵ�
	 * @return ���ڵ㣬��������ڣ�����null
	 */
	UnitTreeNode getParent();

    /**
     * �õ�ѡ����ܵ�����
     * @return ѡ����ܵ�����
     */
    String getSelectSumCondition();
}