package cn.com.youtong.apollo.usermanager;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface User
{

	/**
	 * ���ظ��û���ID��
	 * @return Integer
	 */
	public Integer getUserID();

	/**
	 * ���ظ��û����ʺ�
	 * @return �ַ���
	 */
	public String getName();

	/**
	 * ����û�����
	 * @return �û�����
	 */
	public String getPassword();

	/**
	 * �����ҵ����
	 * @return ��ҵ����
	 */
	public String	getEnterpriseName();

	/**
	 * ��÷��˴���
	 * @return ���˴���
	 */
	public String	getLawPersionCode();

	/**
	 * ��÷��˴���
	 * @return ���˴���
	 */
	public String	getLawPersionName();

	/**
	 * ��÷��˴���绰
	 * @return ���˴���绰
	 */
	public String	getLawPersionPhone();

	/**
	 * �����ϵ������
	 * @return ��ϵ������
	 */
	public String	getContactPersionName();

	/**
	 * �����ϵ�˵绰
	 * @return ��ϵ�˵绰
	 */
	public String	getContactPersionPhone();

	/**
	 * �����ϵ���ֻ�
	 * @return ��ϵ���ֻ�
	 */
	public String getContactPersionMobile();

	/**
	 * �����ϵ�˵�ַ
	 * @return ��ϵ�˵�ַ
	 */
	public String	getContactAddress();

	/**
	 * ����ʱ�
	 * @return �ʱ�
	 */
	public String	getPostcode();

	/**
	 * ���ظ��û��ĵ����ʼ�
	 * @return �����ʼ�
	 */
	public String getEmail();

	/**
	 * ��ô���
	 * @return ����
	 */
	public String	getFax();

	/**
	 * �Ƿ���Ч���Ƿ�ͨ����֤��
	 * @return ͨ���򷵻�true ����flase
	 */
	public boolean isValidated();

	/**
	 * ���ظ��û��ı�ע
	 * @return �ַ���
	 */
	public String getMemo();


	/**
	 * ���ظ��û��Ľ�ɫ
	 * @return �ӿ�Role
	 */
	public Role getRole();

	/**
	 * ����ʱ��
	 * @return ����ʱ��
	 */
	public java.util.Date getDateCreated();

	/**
	 * ����޸�ʱ��
	 * @return ����޸�ʱ��
	 */
	public java.util.Date getDateModified();

	/**
	 * ����û�������ļ���
	 * @return Group����
	 */
	public Collection getGroups();
}