package cn.com.youtong.apollo.address;

import cn.com.youtong.apollo.data.UnitTreeNode;

public interface AddressInfo
{
	/**
	 * @return 主键
	 */
	public AddressInfoPK getPK();
	/**
	 * @return 电子信箱
	 */
	public String getEmail();
	public String getMobile();
	public java.lang.String getPhone();
	public java.lang.String getFax();

	/**
	 * 得到单位数节点
	 * @return  单位数节点
	 */
	public UnitTreeNode getUnitTreeNode();
}