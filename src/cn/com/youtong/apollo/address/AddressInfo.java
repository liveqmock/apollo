package cn.com.youtong.apollo.address;

import cn.com.youtong.apollo.data.UnitTreeNode;

public interface AddressInfo
{
	/**
	 * @return ����
	 */
	public AddressInfoPK getPK();
	/**
	 * @return ��������
	 */
	public String getEmail();
	public String getMobile();
	public java.lang.String getPhone();
	public java.lang.String getFax();

	/**
	 * �õ���λ���ڵ�
	 * @return  ��λ���ڵ�
	 */
	public UnitTreeNode getUnitTreeNode();
}