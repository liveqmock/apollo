package cn.com.youtong.apollo.common.license;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface License
{
	/**
	 * ����license�ļ���װ����
	 * @return           license�ļ���װ����
	 * @throws LicenseException
	 */
	public java.util.Date installDate()
			throws LicenseException;

	/**
	 * �鿴license�Ƿ���Ч,������,����true;
	 * ����������ѹ�,û��ע��,����false;
	 * ע���û�,����true
	 * @return           true or false
	 * @throws LicenseException
	 */
	public boolean isValid()
		throws LicenseException;

	/**
	 * ע��license
	 * @param key          ע����
	 * @return             ע���Ƿ�ɹ�
	 * @throws LicenseException
	 */
	public boolean regedit( String key )
		throws LicenseException;

	/**
	 * �鿴���ж೤ʱ�������ʱ��,���Ϊ-1,��ʾ����.
	 * @return          ʣ����������
	 * @throws LicenseException
	 */
	public int trailDay()
		throws LicenseException;

	/**
	 * ���ػ�����,���ݴ˻�����,ͨ���㷨�õ�ע��ֵ
	 * @return           ������
	 * @throws LicenseException
	 */
	public String machineCode()
		throws LicenseException;

	/**
	 * ����Ƿ���ע��
	 * @return         true or false
	 * @throws LicenseException
	 */
	public boolean isRegedited()
		throws LicenseException;
}