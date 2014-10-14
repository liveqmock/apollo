package cn.com.youtong.apollo.address.db;

import cn.com.youtong.apollo.services.DefaultFactory;
import cn.com.youtong.apollo.address.AddressManager;
import cn.com.youtong.apollo.address.AddressManagerFactory;

public class DBAddressManagerFactory extends DefaultFactory implements AddressManagerFactory
{
	public DBAddressManagerFactory()
	{
	}

	public AddressManager createAddressManager()
	{
		return new DBAddressManager();
	}
}