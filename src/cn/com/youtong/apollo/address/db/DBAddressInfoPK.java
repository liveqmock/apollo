package cn.com.youtong.apollo.address.db;

import cn.com.youtong.apollo.address.AddressInfoPK;
import cn.com.youtong.apollo.address.db.form.AddressInfoFormPK;

public class DBAddressInfoPK
	implements AddressInfoPK
{
	private AddressInfoFormPK pk = null;
	public DBAddressInfoPK(AddressInfoFormPK pk)
	{
		this.pk = pk;
	}

	public String getTaskID()
	{
		return pk.getTaskID();
	}

	public String getUnitID()
	{
		return pk.getUnitID();
	}

}