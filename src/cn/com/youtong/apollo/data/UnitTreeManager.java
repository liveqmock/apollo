package cn.com.youtong.apollo.data;

import java.util.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import cn.com.youtong.apollo.data.db.DBUnitTreeNode;

/**
 * ��λ��������
 */
public interface UnitTreeManager
{
	/**
	 * ���ݸ�����taskID ���Ҹ������е����еĵ�λ(�Ե�λ���󼯺ϵ���ʽ����)
	 * ���ݸ���paramname  �õ���Ԫ������Ϣ
	 * @param taskID ����ID��
	 * @param session Hibernate����
	 * @return ��λ����ļ���
	 * @throws HibernateException
	 */
	public List<DBUnitTreeNode> getAllUnitTreeNodes(String taskID,String paramname,UnitACL unitACL);
	/**
	 * �жϵ�λ�Ƿ����
	 * @param unitID ��λID��
	 * @return ����ֵ
	 */
	public boolean isUnitExist(String unitID);

	/**
	 * �õ���λɭ��
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest() throws ModelException;

	/**
	 * �õ���λɭ��
	 * @param unitACL ���ʿ���Ȩ��
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest(UnitACL unitACL) throws ModelException;

	/**
	 * ��õ�λ���ݶ���(�����Ķ��󣬲������¼��ڵ�)
	 * @param unitIDs ��λID����
	 * @return ��λ���󼯺�(UnitTreeNode)
	 * @throws ModelException
	 */
	public Collection getUnits(String[] unitIDs) throws ModelException;

	/**
	 * ���ָ������ĵ�λ������unitIDָ���ĵ�λΪ�� ��unitID���������׳��쳣
	 * @param unitID ��λID��
	 * @return ��λ��
	 * @throws ModelException
	 */
	public UnitTreeNode getUnitTree(String unitID) throws ModelException;

	/**
	 * ������λ
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @return �´����ĵ�λ�ڵ����
	 * @throws ModelException
	 */
	public UnitTreeNode createUnit(String code, String name, String reportType,
						   String parentCode, String HQCode, String p_Parent) throws
		ModelException;

	/**
	 * ���µ�λ
	 * @param unitID ��λid
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @return ���º�ĵ�λ�ڵ����
	 * @throws ModelException
	 */
	public UnitTreeNode updateUnit(String unitID, String code, String name,
						   String reportType, String parentCode, String HQCode,
						   String p_Parent,int display) throws ModelException;

	/**
	 * ɾ����λ
	 * @param id ��λid
	 * @throws ModelException
	 */
	public void deleteUnit(String id) throws ModelException;
	
	//ȡ�õ�λ���µ����е�λ
	public void getUnits(UnitTreeNode node, Map map);

}