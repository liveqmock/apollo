package cn.com.youtong.apollo.usermanager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Role
{

	/**
	 * ��ɫ��ID��
	 * @return ID��
	 */
	public Integer getRoleID();

	/**
	 * ��ô�������
	 * @return ��������
	 */
	public java.util.Date getDateCreated();

	/**
	 * �������޸�����
	 * @return ����޸�����
	 */
	public java.util.Date getDateModified();

	/**
	 * ��ý�ɫ����
	 * @return ����
	 */
	public String getName();

	/**
	 * ��ñ�ע
	 * @return ��ע
	 */
	public String getMemo();

	/**
	 * ���Ȩ��
	 * @return Ȩ�޼�
	 */
	public SetOfPrivileges getPrivileges();
}