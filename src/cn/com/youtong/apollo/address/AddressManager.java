package cn.com.youtong.apollo.address;

import java.util.Iterator;

public interface AddressManager
{
	/**
	 * ������Ϣ
	 * @param info
	 * @throws AddressException
	 */
	public void addAddressInfo(AddressInfo info) throws AddressException;
	/**
	 * ɾ����Ϣ
	 * @param pk
	 * @throws AddressException
	 */
	public void deleteAddressInfo(AddressInfoPK pk) throws AddressException;
	/**
	 * ������Ϣ�����û�У�����null
	 * @param pk
	 * @return
	 * @throws AddressException
	 */
	public AddressInfo findByPK(AddressInfoPK pk) throws AddressException;
	public void updateAddressInfo(AddressInfo info) throws AddressException;
	//public Iterator getAllAddressInfo() throws AddressException;
	public Iterator getAddressInfoByTaskID(String taskID) throws AddressException;
}