package cn.com.youtong.apollo.address;

import java.util.Iterator;

public interface AddressManager
{
	/**
	 * 增加信息
	 * @param info
	 * @throws AddressException
	 */
	public void addAddressInfo(AddressInfo info) throws AddressException;
	/**
	 * 删除信息
	 * @param pk
	 * @throws AddressException
	 */
	public void deleteAddressInfo(AddressInfoPK pk) throws AddressException;
	/**
	 * 查找信息，如果没有，返回null
	 * @param pk
	 * @return
	 * @throws AddressException
	 */
	public AddressInfo findByPK(AddressInfoPK pk) throws AddressException;
	public void updateAddressInfo(AddressInfo info) throws AddressException;
	//public Iterator getAllAddressInfo() throws AddressException;
	public Iterator getAddressInfoByTaskID(String taskID) throws AddressException;
}