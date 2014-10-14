package cn.com.youtong.apollo.usermanager.db;

import java.util.*;
import java.util.Date;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.usermanager.User;
import cn.com.youtong.apollo.usermanager.Group;
import cn.com.youtong.apollo.usermanager.Role;
import cn.com.youtong.apollo.usermanager.db.form.*;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.common.mail.*;
import java.io.*;
import cn.com.youtong.apollo.usermanager.db.xml.*;
import org.exolab.castor.xml.*;
import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBUserManager
	implements UserManager
{
	private Log log = LogFactory.getLog(this.getClass());

	/* ϵͳ��ʼ��ʱ��Ҫ���û���Ϣ
	 * 1��ϵͳ����Ա�� INIT_GROUP
	 * 2������Ա��ɫ INIT_ROLE_1
	 * 3����߽�ɫ INIT_ROLE_2
	 * 4�����ݷ���Ա INIT_ROLE_3
	 * 5������Ա�û� INIT_USER
	 * 6���û�����Ĺ�ϵ INIT_GROUPMEMBER
	 */
	private static final String INIT_GROUP = "INSERT INTO YTAPL_Groups(groupID, name, memo, flag) VALUES (1, '����Ա��', '����ϵͳ����', 0)";
	private static final String INIT_ROLE_1 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (1, '����Ա��ɫ', 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT', '����ϵͳ����', 0)";
	private static final String INIT_ROLE_2 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (2, '��߽�ɫ', 'FFTTTTTTTTTTTTTTT', '�����', 0)";
	private static final String INIT_ROLE_3 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (3, '���ݷ���Ա', 'FFTTTFFFFFFFFFFFFFFFFFFFFFFFFFFF', '���ݷ���', 0)";
	private static final String INIT_USER = "INSERT INTO YTAPL_Users(userID, name, password, roleID, enterpriseName,lawPersionCode, lawPersionName,lawPersionPhone,contactPersionName, contactPersionPhone, contactPersionMobile, contactAddress,postcode, memo, flag) VALUES (1, 'admin', '5F4DCC3B5AA765D61D8327DEB882CF99', 1,'ϵͳ����Ա','���˴���','���˴���','���˵绰','��ϵ��','��ϵ�绰','��ϵ���ֻ�','��ϵ�˵�ַ','�ʱ�','����ϵͳ����', 1)";
	private static final String INIT_GROUPMEMBER =
		"INSERT INTO YTAPL_GroupMember(userID, groupID)VALUES (1, 1)";

	private static final List INIT_LIST = new LinkedList();
	static
	{
		INIT_LIST.add(INIT_GROUP);
		INIT_LIST.add(INIT_ROLE_1);
		INIT_LIST.add(INIT_ROLE_2);
		INIT_LIST.add(INIT_ROLE_3);
		INIT_LIST.add(INIT_USER);
		INIT_LIST.add(INIT_GROUPMEMBER);
	}

	public DBUserManager()
	{
	}

	/**
	 * �ӳ�ʼ���ļ��ж�ȡ������ʼ���û��ĸ�����Ϣ
	 * @param in ������
	 * @throws UserManagerException
	 */
	public void initFromFile(InputStream in) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			HashMap roleMap, userMap;
			/*��ʼ��ʱҪ�ȳ�ʼ�����ݿ���е�����*/
			clearRecord(session);

			Userinit userinit = parseXMLFile(in);

			roleMap = initRoleInfo(userinit, session);
			userMap = initUserInfo(userinit, roleMap, session);
			initGroupInfo(userinit, userMap, session);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û�ʧ��", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * ���������û��Ƿ���Ч
	 * @param userIDs �û�ID������
	 * @param isValidate ��Ч����true ���� false
	 * @throws UserManagerException
	 */
	public void batchSetUserFlag(Integer[] userIDs, boolean isValidate) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < userIDs.length; i++)
			{
				setUserFlag(userIDs[i], isValidate, session);
			}
			tx.commit();
			for (int i = 0; i < userIDs.length; i++)
			{
				User user = getUserByID(userIDs[i]);
				sendMail(user.getEmail(), isValidate);
			}
		}
		catch (HibernateException ex)
		{
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			log.error("�����û�FLAGʧ�ܣ�", ex);
			throw new UserManagerException("�����û�FLAGʧ��", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * ���õ����û��Ƿ���Ч
	 * @param userID �û�ID
	 * @param isValidate true or false
	 * @throws UserManagerException
	 */
	public void setUserFlag(Integer userID, boolean isValidate) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			setUserFlag(userID, isValidate, session);
			tx.commit();
			User user = getUserByID(userID);
			sendMail(user.getEmail(), isValidate);
		}
		catch (HibernateException ex)
		{
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			log.error("�����û�FLAGʧ�ܣ�", ex);
			throw new UserManagerException("�����û�FLAGʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	private void sendMail(String email, boolean isValidate)
	{
		String body = "�����ʺ�����" + Convertor.date2String(new Date()) +
			(isValidate ? "��ͨ" : "ֹͣ");
		Mail.send(Config.getString("mail.admin.address"), email, null, null,
				  "�ʺ���Ϣ", body, false);
	}

	/**
	 * �����û��Ƿ�ͨ�ı�־
	 * @param userID �û�ID��
	 * @param value �Ƿ�ͨ��true or false��
	 * @param session Hibernate����
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private void setUserFlag(Integer userID, boolean value, Session session) throws
		HibernateException, UserManagerException
	{
		if (!isUserDefined(userID, session))
		{
			throw new UserManagerException("ָ�����û�������");
		}
		UserForm user = new UserForm();
		session.load(user, userID);
		int flag = 0;
		if (value)
		{
			flag = 1;
		}
		user.setFlag(flag);
		session.update(user);
	}

	/**
	 * ���ݱ�־��ѯ�û����û��Ƿ�ͨ����֤��
	 * @param isValidate true or false����ѯ������
	 * @return User����ĵ�����
	 * @throws UserManagerException
	 */
	public Collection getUserByFlag(boolean isValidate) throws
		UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			ArrayList users = new ArrayList();
			List result = session.find(
				"FROM UserForm as user where user.flag = ?",
				new Integer( (isValidate ? "1" : "0")), Hibernate.INTEGER);
			Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				UserForm user = (UserForm) itr.next();
				users.add(loadDetail(user));
			}
			return users;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ��ѯ�û���ģ����ѯ������ѯ����������Ϊ��
	 * @param name �û���
	 * @param enterpriseName ��ҵ����
	 * @return User����ĵ�����
	 * @throws UserManagerException
	 */
	public Collection getUser(String name, String enterpriseName) throws
		UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			ArrayList users = new ArrayList();
			String sql = null;
			if (name == null && enterpriseName == null)
			{
				sql = "FROM UserForm as user";
			}
			else if (name != null && enterpriseName == null)
			{
				sql = "FROM UserForm as user where user.name like " + "'%" +
					name + "%'";
			}
			else if (name == null && enterpriseName != null)
			{
				sql = "FROM UserForm as user where user.enterpriseName like " +
					"'%" + enterpriseName + "%'";
			}
			else if (name != null && enterpriseName != null)
			{
				sql = "FROM UserForm as user where user.name like " + "'%" +
					name + "%' and user.enterpriseName like " + "'%" +
					enterpriseName + "%'";
			}
			List result = session.find(sql);
			Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				UserForm user = (UserForm) itr.next();
				users.add(loadDetail(user));
			}
			return users;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			log.debug(ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �޸��û������޸����룩    ʧ�����׳��쳣
	 * @param userID  �û�ID��
	 * @param name �ʺ�
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @throws UserManagerException
	 */
	public void updateUser(Integer userID, String name, String enterpriseName,
						   String lawPersionCode, String lawPersionName,
						   String lawPersionPhone, String contactPersionName,
						   String contactPersionPhone,
						   String contactPersionMobile, String contactAddress,
						   String postcode, String email, String fax,
						   boolean isValidate, String memo) throws
		UserManagerException

	{
		updateUser(userID, name, null, enterpriseName, lawPersionCode,
				   lawPersionName, lawPersionPhone, contactPersionName,
				   contactPersionPhone, contactPersionMobile, contactAddress,
				   postcode, email,
				   fax, isValidate, memo);
	}

	/**
	 * �޸��û������޸����룩    ʧ�����׳��쳣
	 * @param userID  �û�ID��
	 * @param name �ʺ�
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @param roleID ��ɫID��
	 * @param groupIDs ��ID��
	 * @throws UserManagerException
	 */
	public void updateUser(Integer userID, String name, String enterpriseName,
						   String lawPersionCode, String lawPersionName,
						   String lawPersionPhone, String contactPersionName,
						   String contactPersionPhone,
						   String contactPersionMobile, String contactAddress,
						   String postcode, String email, String fax,
						   boolean isValidate, String memo, Integer roleID,
						   Integer[] groupIDs) throws UserManagerException
	{
		updateUser(userID, name, null, enterpriseName, lawPersionCode,
				   lawPersionName, lawPersionPhone, contactPersionName,
				   contactPersionPhone, contactPersionMobile, contactAddress,
				   postcode, email,
				   fax, isValidate, memo, roleID, groupIDs);
	}

	/**
	 * �޸��û�
	 * @param userID �û�ID��
	 * @param name �ʺ�
	 * @param password ����
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @param roleID ��ɫID��
	 * @param groupIDs ��ID��
	 * @throws UserManagerException �޸�ʧ���׳�
	 */
	public void updateUser(Integer userID, String name, String password,
						   String enterpriseName, String lawPersionCode,
						   String lawPersionName, String lawPersionPhone,
						   String contactPersionName,
						   String contactPersionPhone,
						   String contactPersionMobile, String contactAddress,
						   String postcode, String email, String fax,
						   boolean isValidate, String memo, Integer roleID,
						   Integer[] groupIDs) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			//���»�����Ϣ
			UserForm user = updateUserBasicInfo(userID, name, password,
												enterpriseName, lawPersionCode,
												lawPersionName, lawPersionPhone,
												contactPersionName,
												contactPersionPhone,
												contactPersionMobile,
												contactAddress, postcode, email,
												fax, isValidate, memo, session);
			//���û������ɫ����
			assignRoleToUser(user, roleID, session);
			assignGroupToUser(user, groupIDs, session);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �����鵽�û��Ĺ�ϵ  ���������û�������ʧ�����׳��쳣
	 * @param user �û�����
	 * @param groupIDs ��ID����
	 * @param session session����
	 * @throws HibernateException �쳣
	 */
	private void assignGroupToUser(UserForm user, Integer[] groupIDs,
								   Session session) throws HibernateException
	{
		Set groups = new HashSet();
		for (int i = 0; i < groupIDs.length; i++)
		{
			GroupForm group = new GroupForm();
			group.setGroupID(groupIDs[i]);
			groups.add(group);
		}
		user.setGroups(groups);
		session.update(user);
	}

	/**
	 * ������ɫ���û��Ĺ�ϵ  �ѽ�ɫ������û�������ʧ�����׳��쳣
	 * @param user �û�����
	 * @param roleID ��ɫID
	 * @param session session����
	 * @throws HibernateException
	 */
	private void assignRoleToUser(UserForm user, Integer roleID,
								  Session session) throws HibernateException
	{
		RoleForm role = new RoleForm();
		role.setRoleID(roleID);
		user.setRole(role);
		session.update(user);
	}

	/**
	 * �����û�������ROLE��GROUP  �����ɹ��򷵻��û����󣬴���ʧ�����׳��쳣
	 * @param name �ʺ�
	 * @param password ����
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @param roleID ��ɫID��
	 * @param groupIDs ��ID��
	 * @return User�ӿ�
	 * @throws UserManagerException ����ʧ���׳�
	 */
	public User createUser(String name, String password, String enterpriseName,
						   String lawPersionCode, String lawPersionName,
						   String lawPersionPhone, String contactPersionName,
						   String contactPersionPhone,
						   String contactPersionMobile, String contactAddress,
						   String postcode, String email, String fax,
						   boolean isValidate, String memo, Integer roleID,
						   Integer[] groupIDs) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			//�����û�
			if (isUserDefined(name, session))
			{
				throw new UserManagerException("�˺�Ϊ" + name + "�Ѿ�����");
			}
			UserForm user = createUser(name, password, enterpriseName,
									   lawPersionCode, lawPersionName,
									   lawPersionPhone, contactPersionName,
									   contactPersionPhone,
									   contactPersionMobile, contactAddress,
									   postcode, email, fax, isValidate, memo,
									   session);
			//���û������ɫ����
			assignRoleToUser(user, roleID, session);
			assignGroupToUser(user, groupIDs, session);
			tx.commit();
			return new DBUser(user);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �����û�������ROLE��GROUP  �����ɹ��򷵻��û����󣬴���ʧ�����׳��쳣
	 * @param name �ʺ�
	 * @param password ����
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @param session Hibernate����
	 * @return UserForm
	 * @throws HibernateException ����ʧ���׳�
	 */
	private UserForm createUser(String name, String password,
								String enterpriseName, String lawPersionCode,
								String lawPersionName, String lawPersionPhone,
								String contactPersionName,
								String contactPersionPhone,
								String contactPersionMobile,
								String contactAddress, String postcode,
								String email, String fax, boolean isValidate,
								String memo,
								Session session) throws HibernateException
	{
		UserForm user = new UserForm();

		user.setUserID(HibernateUtil.getNextUserID());
		user.setName(name);
		user.setPassword(getMd5Password(password));
		user.setEnterpriseName(enterpriseName);
		user.setLawPersionCode(lawPersionCode);
		user.setLawPersionName(lawPersionName);
		user.setLawPersionPhone(lawPersionPhone);
		user.setContactPersionName(contactPersionName);
		user.setContactPersionPhone(contactPersionPhone);
		user.setContactPersionMobile(contactPersionMobile);
		user.setContactAddress(contactAddress);
		user.setPostcode(postcode);
		user.setEmail(email);
		user.setFax(fax);
		int flag = 0;
		if (isValidate)
		{
			flag = 1;
		}
		user.setFlag(flag);
		user.setMemo(memo);
		user.setDateCreated(new Date());
		user.setDateModified(new Date());

		session.save(user);
		return user;
	}

	/**
	 * ����GROUP  �����ɹ��򷵻��û����󣬴���ʧ�����׳��쳣
	 * @param name ����
	 * @param memo ��ע
	 * @param session Hibernate����
	 * @return GroupForm
	 * @throws HibernateException ����ʧ���׳�
	 */
	private GroupForm createGroup(String name, String memo,
								  Session session) throws HibernateException
	{
		GroupForm group = new GroupForm();
		group.setDateCreated(new Date());
		group.setDateModified(new Date());
		group.setGroupID(HibernateUtil.getNextGroupID());
		group.setMemo(memo);
		group.setName(name);
		session.save(group);
		return group;
	}

	/**
	 * ����Role  �����ɹ��򷵻ؽ�ɫ���󣬴���ʧ�����׳��쳣
	 * @param name ��ɫ��
	 * @param memo ��ע
	 * @param privileges Ȩ�޼�
	 * @param session Hiberante����
	 * @return RoleForm����
	 * @throws HibernateException
	 */
	private RoleForm createRole(String name, String memo,
								SetOfPrivileges privileges,
								Session session) throws HibernateException
	{
		RoleForm role = new RoleForm();
		role.setDateCreated(new Date());
		role.setDateModified(new Date());
		role.setMemo(memo);
		role.setName(name);
		role.setRoleID(HibernateUtil.getNextRoleID());
		role.setUserRights(privileges.toString());
		session.save(role);
		return role;
	}

	/**
	 * �õ���MD5���������
	 * @param password ����ǰ������
	 * @return ����֮�������
	 */
	private String getMd5Password(String password)
	{
		org.apache.java.security.MD5 md5 = new org.apache.java.security.MD5();
		String md5Password = org.apache.java.lang.Bytes.toString(md5.digest(
			password.getBytes()));
		return md5Password;
	}

	/**
	 * ɾ���û�
	 * @param userID �û�ID
	 * @throws UserManagerException
	 */
	public void deleteUser(Integer userID) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (!isUserDefined(userID, session))
			{
				throw new UserManagerException("ָ�����û�������");
			}

			session.delete("select user from UserForm user where user.userID=?",
						   userID, Hibernate.INTEGER);

			// delete scalar query template of this user
			session.delete(
				"select template from ScalarQueryTemplateForm template where template.userID=?",
				userID, Hibernate.INTEGER);

			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("ɾ���û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ���û��һ�����
	 * @param name �û���name
	 * @throws UserManagerException ʧ��
	 */
	public void findPassword(String name) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			//�ҵ�ָ�����û�
			if (!isUserDefined(name, session))
			{
				throw new UserManagerException("�������˺�Ϊ" + name + "���û�");
			}
			List result = session.find(
				"FROM UserForm as user WHERE user.name = ?", name,
				Hibernate.STRING);
			UserForm user = (UserForm) result.get(0);
			//�����µ�����
			String newPassword = generateRandomPassword();
			user.setPassword(getMd5Password(newPassword));
			//���µ����뷢�͸��û�
			session.update(user);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}

	}

	/**
	 * ɾ���û�
	 * @param user Ҫɾ�����û�
	 * @throws UserManagerException
	 */
	public void deleteUser(User user) throws UserManagerException
	{
		deleteUser(user.getUserID());
	}

	/**
	 * �޸��û�
	 * @param userID �û�ID��
	 * @param name �ʺ�
	 * @param password ����
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @throws UserManagerException �޸�ʧ���׳�
	 */
	public void updateUser(Integer userID, String name, String password,
						   String enterpriseName, String lawPersionCode,
						   String lawPersionName, String lawPersionPhone,
						   String contactPersionName,
						   String contactPersionPhone,
						   String contactPersionMobile, String contactAddress,
						   String postcode, String email, String fax,
						   boolean isValidate, String memo) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			//���»�����Ϣ
			updateUserBasicInfo(userID, name, password, enterpriseName,
								lawPersionCode, lawPersionName, lawPersionPhone,
								contactPersionName, contactPersionPhone,
								contactPersionMobile, contactAddress,
								postcode, email, fax, isValidate, memo, session);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ���»�����Ϣ
	 * @param userID �û�ID��
	 * @param name �ʺ�
	 * @param password ����
	 * @param enterpriseName ��ҵ����
	 * @param lawPersionCode ���˴���
	 * @param lawPersionName ���˴���
	 * @param lawPersionPhone ���˴���绰
	 * @param contactPersionName ��ϵ��
	 * @param contactPersionPhone ��ϵ�˵绰
	 * @param contactPersionMobile ��ϵ���ֻ�
	 * @param contactAddress ��ϵ�˵�ַ
	 * @param postcode �ʱ�
	 * @param email �����ʼ�
	 * @param fax ����
	 * @param isValidate �Ƿ�ͨ
	 * @param memo ��ע
	 * @param session session����
	 * @return UserForm���º���û�����
	 * @throws HibernateException �ײ��쳣
	 * @throws UserManagerException ҵ���쳣
	 */
	private UserForm updateUserBasicInfo(Integer userID, String name,
										 String password, String enterpriseName,
										 String lawPersionCode,
										 String lawPersionName,
										 String lawPersionPhone,
										 String contactPersionName,
										 String contactPersionPhone,
										 String contactPersionMobile,
										 String contactAddress, String postcode,
										 String email, String fax,
										 boolean isValidate, String memo,
										 Session session) throws
		HibernateException, UserManagerException
	{
		if (!isUserDefined(userID, session))
		{
			throw new UserManagerException("ָ�����û�������");
		}
		if (hasSameAccountUser(userID, name, session))
		{
			throw new UserManagerException("�˺�Ϊ" + name + "���û��Ѿ�����");
		}
		UserForm user = new UserForm();
		session.load(user, userID);

		user.setName(name);
		user.setEnterpriseName(enterpriseName);
		user.setLawPersionCode(lawPersionCode);
		user.setLawPersionName(lawPersionName);
		user.setLawPersionPhone(lawPersionPhone);
		user.setContactPersionName(contactPersionName);
		user.setContactPersionPhone(contactPersionPhone);
		user.setContactPersionMobile(contactPersionMobile);
		user.setContactAddress(contactAddress);
		user.setPostcode(postcode);
		user.setEmail(email);
		user.setFax(fax);
		int flag = 0;
		if (isValidate)
		{
			flag = 1;
		}
		user.setFlag(flag);
		user.setMemo(memo);
		user.setDateModified(new Date());
		//��Ҫ�޸�����
		if (password != null)
		{
			user.setPassword(getMd5Password(password));
		}

		session.update(user);
		return user;
	}

	/**
	 * �����û��������û�
	 * @param fullName �û���
	 * @return �û�User����
	 * @throws UserManagerException
	 */
//	public Collection getUserByFullName(String fullName)
//		throws UserManagerException
//	{
//		Session session = null;
//		try
//		{
//			session = HibernateUtil.getSessionFactory().openSession();
//			ArrayList users = new ArrayList();
//			List result = session.find("FROM UserForm as user WHERE user.fullName = ?", fullName, Hibernate.STRING);
//			Iterator itr = result.iterator();
//			while(itr.hasNext())
//			{
//				users.add(new DBUser((UserForm) itr.next()));
//			}
//			return users;
//		}
//		catch(HibernateException ex)
//		{
//			log.error("", ex);
//			throw new UserManagerException("��ѯ�û�ʧ��");
//		}
//		finally
//		{
//			if(session != null)
//			{
//				try
//				{
//					session.close();
//				}
//				catch(HibernateException ex2)
//				{
//				}
//			}
//		}
//	}

	/**
	 * �����û��˺Ų����û�
	 * @param name �û��˺�
	 * @return �û��������û�����ͽ�ɫ��Ϣ
	 * @throws UserManagerException
	 */
	public User getUserByName(String name) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isUserDefined(name, session))
			{
				throw new UserManagerException("�������˺�Ϊ" + name + "���û�");
			}
			List result = session.find(
				"FROM UserForm as user WHERE user.name = ?", name,
				Hibernate.STRING);
			return loadDetail( (UserForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �����û�
	 * @param userID �û�ID��
	 * @return �û��������û�����ͽ�ɫ��Ϣ
	 * @throws UserManagerException
	 */
	public User getUserByID(Integer userID) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isUserDefined(userID, session))
			{
				throw new UserManagerException("ָ�����û�������");
			}
			UserForm user = new UserForm();
			session.load(user, userID);
			return loadDetail(user);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ��ѯ���ڸ���������û�
	 * @param groupID ���ID��
	 * @return UserForm����
	 * @throws UserManagerException
	 */
	public Collection getUsersNotInGroup(Integer groupID) throws
		UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			List users = new LinkedList();

			GroupForm groupForm = new GroupForm();
			session.load(groupForm, groupID);

			//�����ѯ�����������û���sql
			StringBuffer sqlBuff = new StringBuffer(
				"FROM UserForm as u ");

			if (groupForm.getUsers().size() > 0)
			{
				sqlBuff.append(" WHERE u.userID NOT IN ( ");

				groupForm.getUsers().size();
				Set uSet = groupForm.getUsers();

				Iterator uIter = uSet.iterator();
				if (uIter.hasNext())
				{
					UserForm u = (UserForm) uIter.next();
					Integer userID = u.getUserID();
					sqlBuff.append(userID.intValue());
				}

				while (uIter.hasNext())
				{
					UserForm u = (UserForm) uIter.next();
					Integer userID = u.getUserID();
					sqlBuff.append(",")
						.append(userID.intValue());
				}

				sqlBuff.append(")");
			}
			List notInUsers = session.find(sqlBuff.toString());

			for (Iterator itr = notInUsers.iterator(); itr.hasNext(); )
			{
				UserForm form = (UserForm) itr.next();
				users.add(new DBUser(form));
			}

			return users;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �������е��û�
	 * @return User�ļ���
	 * @throws UserManagerException
	 */
	public Collection getAllUsers() throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			ArrayList users = new ArrayList();
			List result = session.find("FROM UserForm as user");
			Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				UserForm user = (UserForm) itr.next();
				users.add(loadDetail(user));
			}
			return users;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û�ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ������ �� �����ɹ���������󣬴���ʧ�����׳��쳣
	 * @param name ����
	 * @param memo ��ע
	 * @param userIDs �����������û�id����
	 * @return Group�ӿ�
	 * @throws UserManagerException
	 */
	public Group createGroup(String name, String memo, Integer[] userIDs) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (isGroupDefined(name, session))
			{
				throw new UserManagerException("����Ϊ" + name + "���û����Ѿ�����");
			}

			GroupForm group = createGroup(name, memo, session);
			//���û����䵽����
			assignUserToGroup(group, userIDs, session);
			tx.commit();
			//�������װ��DBGroup
			return new DBGroup(group);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ɾ����  ɾ��ʧ�����׳��쳣
	 * @param group ��ID��
	 * @throws UserManagerException
	 */
	public void deleteGroup(Group group) throws UserManagerException
	{
		deleteGroup(group.getGroupID());
	}

	/**
	 * ɾ����  ɾ��ʧ�����׳��쳣
	 * @param groupID ��ID��
	 * @throws UserManagerException
	 */

	public void deleteGroup(Integer groupID) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (!isGroupDefined(groupID, session))
			{
				throw new UserManagerException("ָ�����û��鲻����");
			}

			session.delete("select unitPerm from UnitPermissionForm unitPerm where unitPerm.comp_id.groupID =?",
						   groupID, Hibernate.INTEGER);
			session.delete("select g from GroupForm g where g.groupID=?",
						   groupID, Hibernate.INTEGER);

			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("ɾ���û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �޸���   ʧ�����׳��쳣
	 * @param groupID ��ID��
	 * @param name ����
	 * @param memo ��ע
	 * @param userIDs �����������û�id����
	 * @throws UserManagerException
	 */
	public void updateGroup(Integer groupID, String name, String memo,
							Integer[] userIDs) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (!isGroupDefined(groupID, session))
			{
				throw new UserManagerException("ָ�����û��鲻����");
			}
			if (hasSameNameGroup(groupID, name, session))
			{
				throw new UserManagerException("����Ϊ" + name + "���û����Ѿ�����");
			}
			GroupForm group = new GroupForm();
			session.load(group, groupID);
			group.setDateModified(new Date());
			group.setMemo(memo);
			group.setName(name);
			session.update(group);
			//���û����䵽����
			assignUserToGroup(group, userIDs, session);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("�����û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ������
	 * @param groupID ���ID��
	 * @return �飬��������û�
	 * @throws UserManagerException
	 */
	public Group getGroupByID(Integer groupID) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isGroupDefined(groupID, session))
			{
				throw new UserManagerException("ָ�����û��鲻����");
			}
			GroupForm group = new GroupForm();
			session.load(group, groupID);
			return loadDetail(group);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}


        /**
         * ������
         * @param name ����
         * @return �飬��������û�
         * @throws UserManagerException
         */
        public List queryGroupByName(String name) throws UserManagerException
        {
                List result=new ArrayList();
                Session session = null;
                try
                {
                        session = HibernateUtil.getSessionFactory().openSession();

                        List tmp = session.find("FROM GroupForm as g WHERE g.name like '%"+name+"%'");
                        for(int i=0;i<tmp.size();i++)
                        {
                           GroupForm group=(GroupForm)tmp.get(i);
                           result.add(loadDetail(group));
                        }

                        return result;
                }
                catch (HibernateException ex)
                {
                        log.error("", ex);
                        throw new UserManagerException("��ѯ�û���ʧ��", ex);
                }
                finally
                {
                        if (session != null)
                        {
                                try
                                {
                                        session.close();
                                }
                                catch (HibernateException ex2)
                                {
                                }
                        }
                }
        }



	/**
	 * ������
	 * @param name ����
	 * @return �飬��������û�
	 * @throws UserManagerException
	 */
	public Group getGroupByName(String name) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isGroupDefined(name, session))
			{
				throw new UserManagerException("����������Ϊ" + name + "���û���");
			}
			List result = session.find("FROM GroupForm as g WHERE g.name = ?",
									   name, Hibernate.STRING);
			return loadDetail( (GroupForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ��ѯ���������û�����
	 * @param userID �û�ID��
	 * @return Group����
	 * @throws UserManagerException
	 */
	public Collection getGroupsNotContainUser(Integer userID) throws
		UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			List groups = new LinkedList();

			UserForm userForm = new UserForm();
			session.load(userForm, userID);

			//�����ѯ�������������sql
			StringBuffer sqlBuff = new StringBuffer("FROM GroupForm as g");
			if (userForm.getGroups().size() > 0)
			{
				sqlBuff.append(" WHERE g.groupID NOT IN ( ");

				Set gSet = userForm.getGroups();

				Iterator gIter = gSet.iterator();
				if (gIter.hasNext())
				{
					GroupForm g = (GroupForm) gIter.next();
					Integer groupID = g.getGroupID();
					sqlBuff.append(groupID.intValue());
				}

				while (gIter.hasNext())
				{
					GroupForm g = (GroupForm) gIter.next();
					Integer groupID = g.getGroupID();
					sqlBuff.append(",")
						.append(groupID.intValue());
				}

				sqlBuff.append(")");
			}

			List notInGroups = session.find(sqlBuff.toString());

			for (Iterator itr = notInGroups.iterator(); itr.hasNext(); )
			{
				GroupForm form = (GroupForm) itr.next();
				groups.add(new DBGroup(form));
			}

			return groups;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û���ʧ��", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * ����������
	 * @return Gruop �ļ���
	 * @throws UserManagerException
	 */
	public Collection getAllGroups() throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			ArrayList groups = new ArrayList();
			List result = session.find("FROM GroupForm as g");
			Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				groups.add(new DBGroup( (GroupForm) itr.next()));
			}
			return groups;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ�û���ʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ����Role  �����ɹ��򷵻ؽ�ɫ���󣬴���ʧ�����׳��쳣
	 * @param name ��ɫ��
	 * @param memo ��ע
	 * @param privileges Ȩ�޼�
	 * @return Role�ӿ�
	 * @throws UserManagerException
	 */
	public Role createRole(String name, String memo, SetOfPrivileges privileges) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (isRoleDefined(name, session))
			{
				throw new UserManagerException("����Ϊ" + name + "�Ľ�ɫ�Ѿ�����");
			}

			RoleForm role = createRole(name, memo, privileges, session);
			tx.commit();
			//�������װ��DBRole
			return new DBRole(role);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("������ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ɾ��Role
	 * @param role ��ɫ
	 * @throws UserManagerException
	 */
	public void deleteRole(Role role) throws UserManagerException
	{
		deleteRole(role.getRoleID());
	}

	/**
	 * ɾ��Role
	 * @param roleID RoleID��
	 * @throws UserManagerException
	 */
	public void deleteRole(Integer roleID) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (!isRoleDefined(roleID, session))
			{
				throw new UserManagerException("ָ���Ľ�ɫ������");
			}
			if (hasUserAssociated(roleID, session))
			{
				throw new UserManagerException("ָ���Ľ�ɫ�Ѿ���������û�������ɾ���ý�ɫ���û��Ĺ�ϵ");
			}

			session.delete("select role from RoleForm role where role.roleID=?",
						   roleID, Hibernate.INTEGER);

			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("ɾ����ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �޸�Role   ʧ�����׳��쳣
	 * @param roleID ��ɫID��
	 * @param name ��ɫ��
	 * @param memo ��ע
	 * @param privileges Ȩ��
	 * @throws UserManagerException
	 */
	public void updateRole(Integer roleID, String name, String memo,
						   SetOfPrivileges privileges) throws
		UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (!isRoleDefined(roleID, session))
			{
				throw new UserManagerException("ָ���Ľ�ɫ������");
			}
			if (hasSameNameRole(roleID, name, session))
			{
				throw new UserManagerException("����Ϊ" + name + "�Ľ�ɫ�Ѿ�����");
			}
			RoleForm role = new RoleForm();
			session.load(role, roleID);
			role.setDateModified(new Date());
			role.setMemo(memo);
			role.setName(name);
			role.setUserRights(privileges.toString());
			session.update(role);
			tx.commit();
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new UserManagerException("���½�ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ����ɫ�����û�
	 * @param role ��ɫ
	 * @param users �û�
	 * @throws UserManagerException
	 */
//    public void assignUserToRole(Integer roleID, Integer[] userIDs) throws
//        UserManagerException {
//        Session session = null;
//        Transaction tx = null;
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            tx = session.beginTransaction();
//            if (!isRoleDefined(roleID, session)) {
//                throw new UserManagerException("ָ���Ľ�ɫ������");
//            }
//            RoleForm role = new RoleForm();
//            session.load(role, roleID);
//            Set users = new HashSet();
//            for (int i = 0; i < userIDs.length; i++) {
//                UserForm user = new UserForm();
//                user.setUserID(userIDs[i]);
//                users.add(user);
//            }
//            role.setUsers(users);
//            session.update(role);
//            tx.commit();
//        }
//        catch (HibernateException ex) {
//            YtLogger.error("", ex);
//            try {
//                tx.rollback();
//            }
//            catch (HibernateException ex1) {
//            }
//            throw new UserManagerException("���û��������ɫʧ��");
//        }
//        finally {
//            if (session != null) {
//                try {
//                    session.close();
//                }
//                catch (HibernateException ex2) {
//                }
//            }
//        }
//    }

	/**
	 * ���û�������飬����ʧ�����׳��쳣
	 * @param group �����
	 * @param userIDs �û�ID����
	 * @param session session����
	 * @throws HibernateException
	 */
	private void assignUserToGroup(GroupForm group, Integer[] userIDs,
								   Session session) throws HibernateException
	{
		Set users = new HashSet();
		for (int i = 0; i < userIDs.length; i++)
		{
			UserForm user = new UserForm();
			user.setUserID(userIDs[i]);
			users.add(user);
		}
		group.setUsers(users);
		session.update(group);
	}

	/**
	 * ����Role
	 * @param roleID ��ɫID��
	 * @return ��ɫ��������ɫ���û���Ϣ
	 * @throws UserManagerException
	 */
	public Role getRoleByID(Integer roleID) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isRoleDefined(roleID, session))
			{
				throw new UserManagerException("ָ���Ľ�ɫ������");
			}
			RoleForm role = new RoleForm();
			session.load(role, roleID);
			return loadDetail(role);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ��ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * ����ROLE
	 * @param name ��ɫ����
	 * @return ��ɫ��������ɫ���û���Ϣ
	 * @throws UserManagerException
	 */
	public Role getRoleByName(String name) throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if (!isRoleDefined(name, session))
			{
				throw new UserManagerException("����������Ϊ" + name + "�Ľ�ɫ");
			}
			List result = session.find(
				"FROM RoleForm as role WHERE role.name = ?", name,
				Hibernate.STRING);
			return loadDetail( (RoleForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ��ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �������н�ɫ
	 * @return Role �ļ���
	 * @throws UserManagerException
	 */
	public Collection getAllRoles() throws UserManagerException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			ArrayList roles = new ArrayList();
			List result = session.find("FROM RoleForm as role");
			Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				roles.add(new DBRole( (RoleForm) itr.next()));
			}
			return roles;
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("��ѯ��ɫʧ��", ex);
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �ж��û��Ƿ����
	 * @param userID �û�ID��
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	private boolean isUserDefined(Integer userID, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from UserForm as user WHERE user.userID = ?",
			userID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * �ж��û��Ƿ����
	 * @param name �û�����
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	private boolean isUserDefined(String name, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from UserForm as user WHERE user.name = ?", name,
			Hibernate.STRING).next()).intValue() > 0;
	}

	/**
	 * �ж����Ƿ����
	 * @param groupID  ��ID��
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	public boolean isGroupDefined(Integer groupID, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from GroupForm as g WHERE g.groupID = ?", groupID,
			Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * �ж����Ƿ����
	 * @param groupName ������
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	private boolean isGroupDefined(String groupName, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) FROM GroupForm as g WHERE g.name = ?", groupName,
			Hibernate.STRING).next()).intValue() > 0;
	}

	/**
	 * �жϸ�ID��Role�Ƿ����
	 * @param roleID  RoleID��
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	private boolean isRoleDefined(Integer roleID, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from RoleForm as role WHERE role.roleID = ?",
			roleID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * �жϸ����Ƶ�Role�Ƿ����
	 * @param roleName Role����
	 * @param session Session����
	 * @return boolean
	 * @throws HibernateException
	 */
	private boolean isRoleDefined(String roleName, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from RoleForm as role WHERE role.name = ?",
			roleName, Hibernate.STRING).next()).intValue() > 0;
	}

	/**
	 * �ж��Ƿ������ָ���û����˺���ͬ���û�
	 * @param userID ָ���û�ID
	 * @param name ָ���û����˺�
	 * @param session Session����
	 * @return ������ָ���û����˺���ͬ���û�������true�����򷵻�false
	 * @throws HibernateException
	 */
	private boolean hasSameAccountUser(Integer userID, String name,
									   Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from UserForm as user WHERE user.userID != ? AND user.name = ?",
			new Object[]
			{userID, name}
			, new Type[]
			{Hibernate.INTEGER, Hibernate.STRING}).next()).intValue() > 0;
	}

	/**
	 * �ж��Ƿ������ָ���û���ͬ�����û���
	 * @param groupID ָ���û���ID
	 * @param fullName ָ���û��������
	 * @param session Session����
	 * @return ������ָ���û���ͬ�����û��飬����true�����򷵻�false
	 * @throws HibernateException
	 */
	private boolean hasSameNameGroup(Integer groupID, String fullName,
									 Session session) throws HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from GroupForm as g WHERE g.groupID != ? AND g.name = ?",
			new Object[]
			{groupID, fullName}
			, new Type[]
			{Hibernate.INTEGER, Hibernate.STRING}).next()).intValue() > 0;
	}

	/**
	 * �ж��Ƿ������ָ����ɫͬ���Ľ�ɫ
	 * @param roleID ָ����ɫID
	 * @param fullName ָ����ɫ������
	 * @param session Session����
	 * @return ������ָ����ɫͬ���Ľ�ɫ������true�����򷵻�false
	 * @throws HibernateException
	 */
	private boolean hasSameNameRole(Integer roleID, String fullName,
									Session session) throws HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from RoleForm as role WHERE role.roleID != ? AND role.name = ?",
			new Object[]
			{roleID, fullName}
			, new Type[]
			{Hibernate.INTEGER, Hibernate.STRING}).next()).intValue() > 0;
	}

	/**
	 * �����û�����ϸ��Ϣ����ɫ���û��飩
	 * @param user UserForm����
	 * @return ������ϸ��Ϣ��DBUser����
	 * @throws HibernateException
	 */
	private DBUser loadDetail(UserForm user)
	{
		user.getRole();
		user.getGroups().size();
		return new DBUser(user);
	}

	/**
	 * �����û������ϸ��Ϣ���û���
	 * @param group GroupForm����
	 * @return ������ϸ��Ϣ��DBGroup����
	 * @throws HibernateException
	 */
	private DBGroup loadDetail(GroupForm group)
	{
		group.getUsers().size();
		return new DBGroup(group);
	}

	/**
	 * ���ؽ�ɫ����ϸ��Ϣ���û���
	 * @param role RoleForm����
	 * @return ������ϸ��Ϣ��DBRole����
	 * @throws HibernateException
	 */
	private DBRole loadDetail(RoleForm role)
	{
		role.getUsers().size();
		return new DBRole(role);
	}

	/**
	 * �ж��Ƿ�����û���ָ���Ľ�ɫ�����
	 * @param roleID ��ɫID
	 * @param session session����
	 * @return �����û���ָ���Ľ�ɫ�����������true�����򷵻�false
	 * @throws HibernateException �ײ��쳣
	 */
	private boolean hasUserAssociated(Integer roleID, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from UserForm as user WHERE user.role.roleID = ?",
			roleID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * �������������
	 * @return ����
	 */
	private String generateRandomPassword()
	{
		String randomStr = System.currentTimeMillis() + "";
		return randomStr;
	}

	/**
	 * ��ʼ����ɫ
	 * @param userinit Castor����
	 * @param session Hibernate����
	 * @return HashMap ��ɫ���ơ�ID�ŵ�ֵ��
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private HashMap initRoleInfo(Userinit userinit, Session session) throws
		HibernateException, UserManagerException
	{
		HashMap roleMap = new HashMap();
		roleMap.put("����Ա��ɫ", new Integer(1));
		roleMap.put("��߽�ɫ", new Integer(2));
		roleMap.put("���ݷ���Ա", new Integer(3));

		Roles roles = userinit.getRoles();
		if (roles == null)
		{
			return roleMap;
		}

		cn.com.youtong.apollo.usermanager.db.xml.Role[] arrayRole = roles.
			getRole();
		for (int i = 0; i < arrayRole.length; i++)
		{
			cn.com.youtong.apollo.usermanager.db.xml.Role tempRole = arrayRole[
				i];
			if (roleMap.get(tempRole.getName()) != null)
			{
				throw new UserManagerException("����Ϊ" + tempRole.getName() +
											   "�Ľ�ɫ�Ѿ�����");
			}

			RoleForm r = createRole(tempRole.getName(),
									tempRole.getMemo(),
									new SetOfPrivileges(tempRole.getUserRights()),
									session);
			roleMap.put(r.getName(), r.getRoleID());
		}
		return roleMap;
	}

	/**
	 * ��ʼ��ʱ������е��û������Ϣ����������С��ʼ��
	 * @param session Hibernate����
	 * @throws HibernateException
	 */
	private void clearRecord(Session session) throws HibernateException
	{
		Statement st = null;
		try
		{
			Connection con = session.connection();
			st = con.createStatement();

			String delRoleSql = "delete from YTAPL_ROLES";
			st.addBatch(delRoleSql);

			String delUserSql = "delete from YTAPL_USERS";
			st.addBatch(delUserSql);

			String delGroupSql = "delete from YTAPL_GROUPS";
			st.addBatch(delGroupSql);

			String delGroupMemberSql = "delete from YTAPL_GROUPMEMBER";
			st.addBatch(delGroupMemberSql);

			Iterator itrInitInfo = INIT_LIST.iterator();
			while (itrInitInfo.hasNext())
			{
				String tempInitSQL = (String) itrInitInfo.next();
				st.addBatch(tempInitSQL);
			}

			st.executeBatch();
		}
		catch (SQLException ex)
		{
			log.error("��ʼ���û�ʧ��", ex);
			throw new HibernateException(ex);
		}
		finally
		{
			if (st != null)
			{
				try
				{
					st.close();
				}
				catch (SQLException ex1)
				{
				}
			}
		}
	}

	/**
	 * ��ʼ���û�
	 * @param userinit Castor����
	 * @param roleMap ��ɫ���ơ�ID�ŵ�ֵ��
	 * @param session Hibernate����
	 * @return HashMap �û����ơ�ID�ŵ�ֵ��
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private HashMap initUserInfo(Userinit userinit, HashMap roleMap,
								 Session session) throws HibernateException,
		UserManagerException
	{
		HashMap userMap = new HashMap();
		userMap.put("admin", new Integer(1));

		Users users = userinit.getUsers();
		if (users == null)
		{
			return userMap;
		}

		cn.com.youtong.apollo.usermanager.db.xml.User[] arrayUser = users.
			getUser();
		for (int i = 0; i < arrayUser.length; i++)
		{
			cn.com.youtong.apollo.usermanager.db.xml.User tempUser = arrayUser[
				i];

			Property[] arrayProperty = tempUser.getProperty();
			HashMap map = getProperty(arrayProperty);

			//�����û�
			if (userMap.get(map.get("name")) != null)
			{
				throw new HibernateException("�˺�Ϊ" + (String) map.get("name") +
											 "�Ѿ�����");
			}

			UserForm user = createUser(
				(String) map.get("name"),
				(String) map.get("password"),
				(String) map.get("enterpriseName"),
				(String) map.get("lawPersionCode"),
				(String) map.get("lawPersionName"),
				(String) map.get("lawPersionPhone"),
				(String) map.get("contactPersionName"),
				(String) map.get("contactPersionPhone"),
				(String) map.get("contactPersionMobile"),
				(String) map.get("contactAddress"),
				(String) map.get("postcode"),
				(String) map.get("email"),
				(String) map.get("fax"),
				Boolean.valueOf( (String) map.get("flag")).booleanValue(),
				(String) map.get("memo"),
				session);
			//���û������ɫ
			assignRoleToUser(user,
							 (Integer) roleMap.get( (String) map.get("roleName")),
							 session);
			userMap.put(user.getName(), user.getUserID());
		}
		return userMap;
	}

	private HashMap getProperty(Property[] arrayProperty)
	{
		HashMap map = new HashMap();
		for (int i = 0; i < arrayProperty.length; i++)
		{
			Property property = arrayProperty[i];
			map.put(property.getKey(), property.getValue());
		}
		return map;
	}

	/**
	 * ��ʼ����
	 * @param userinit Castor����
	 * @param userMap �û����ơ�ID�ŵ�ֵ��
	 * @param session Hibernate����
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private void initGroupInfo(Userinit userinit, HashMap userMap,
							   Session session) throws HibernateException,
		UserManagerException
	{
		HashMap groupMap = new HashMap();
		groupMap.put("����Ա��", new Integer(1));

		Groups groups = userinit.getGroups();
		if (groups == null)
		{
			return;
		}

		cn.com.youtong.apollo.usermanager.db.xml.Group[] arrayGroup = groups.
			getGroup();
		for (int i = 0; i < arrayGroup.length; i++)
		{
			cn.com.youtong.apollo.usermanager.db.xml.Group tempGroup =
				arrayGroup[i];

			if (groupMap.get("tempGroup.getName()") != null)
			{
				throw new UserManagerException("����Ϊ" + tempGroup.getName() +
											   "���û����Ѿ�����");
			}

			GroupForm group = createGroup(tempGroup.getName(),
										  tempGroup.getMemo(), session);
			groupMap.put(group.getName(), group.getGroupID());

			//���û����䵽����
			Member[] members = tempGroup.getMember();
			if (members.length > 0)
			{
				Integer[] userIDs = new Integer[members.length];
				for (int j = 0; j < members.length; j++)
				{
					userIDs[j] = (Integer) userMap.get(members[j].getUserName());
				}
				assignUserToGroup(group, userIDs, session);
			}
		}
	}

	/**
	 * ������ʼ��XML�ļ�
	 * @param in �ļ�������
	 * @return Castor����
	 * @throws UserManagerException
	 */
	private Userinit parseXMLFile(InputStream in) throws UserManagerException
	{
		Userinit userinit = null;

		try
		{
			InputStreamReader reader = new InputStreamReader(in, "gb2312");
			userinit = Userinit.unmarshal(reader);
		}
		catch (Exception ex)
		{
			log.error("��ʼ���û�ʧ�ܣ�XML�ļ�����ʧ�� ", ex);
			throw new UserManagerException(ex);
		}

		if (userinit == null)
		{
			throw new UserManagerException("��ʼ���û�ʧ�ܣ����������ļ��� ");
		}
		return userinit;
	}
}