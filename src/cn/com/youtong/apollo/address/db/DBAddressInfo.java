package cn.com.youtong.apollo.address.db;

import cn.com.youtong.apollo.address.AddressInfoPK;
import cn.com.youtong.apollo.address.AddressInfo;
import cn.com.youtong.apollo.address.db.form.AddressInfoForm;
import cn.com.youtong.apollo.data.UnitTreeNode;

public class DBAddressInfo
	implements AddressInfo
{
	private AddressInfoForm info = null;
	private UnitTreeNode node = null;
	public DBAddressInfo(AddressInfoForm info)
	{
		this.info = info;
	}

	public AddressInfoPK getPK()
	{
		return new DBAddressInfoPK(info.getComp_id());
	}

	public String getEmail()
	{
		return info.getEmail();
	}

	public String getMobile()
	{
		return info.getMobile();
	}

	public String getPhone()
	{
		return info.getPhone();
	}

	public String getFax()
	{
		return info.getFax();
	}

	public UnitTreeNode getUnitTreeNode()
	{
		return node;
	}

	public void setUnitTreeNode(UnitTreeNode node)
	{
		this.node = node;
	}
}