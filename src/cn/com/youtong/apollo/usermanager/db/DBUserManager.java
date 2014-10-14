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

	/* 系统初始化时需要的用户信息
	 * 1、系统管理员组 INIT_GROUP
	 * 2、管理员角色 INIT_ROLE_1
	 * 3、填报者角色 INIT_ROLE_2
	 * 4、数据分析员 INIT_ROLE_3
	 * 5、管理员用户 INIT_USER
	 * 6、用户与组的关系 INIT_GROUPMEMBER
	 */
	private static final String INIT_GROUP = "INSERT INTO YTAPL_Groups(groupID, name, memo, flag) VALUES (1, '管理员组', '负责系统管理', 0)";
	private static final String INIT_ROLE_1 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (1, '管理员角色', 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT', '负责系统管理', 0)";
	private static final String INIT_ROLE_2 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (2, '填报者角色', 'FFTTTTTTTTTTTTTTT', '负责填报', 0)";
	private static final String INIT_ROLE_3 = "INSERT INTO YTAPL_Roles(roleID, name, userRights, memo, flag) VALUES (3, '数据分析员', 'FFTTTFFFFFFFFFFFFFFFFFFFFFFFFFFF', '数据分析', 0)";
	private static final String INIT_USER = "INSERT INTO YTAPL_Users(userID, name, password, roleID, enterpriseName,lawPersionCode, lawPersionName,lawPersionPhone,contactPersionName, contactPersionPhone, contactPersionMobile, contactAddress,postcode, memo, flag) VALUES (1, 'admin', '5F4DCC3B5AA765D61D8327DEB882CF99', 1,'系统管理员','法人代码','法人代表','法人电话','联系人','联系电话','联系人手机','联系人地址','邮编','负责系统管理', 1)";
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
	 * 从初始化文件中读取，并初始化用户的各种信息
	 * @param in 输入流
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
			/*初始化时要先初始化数据库表中的数据*/
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
			throw new UserManagerException("创建用户失败", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 批量设置用户是否有效
	 * @param userIDs 用户ID的数组
	 * @param isValidate 有效则是true 否则 false
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
			log.error("设置用户FLAG失败：", ex);
			throw new UserManagerException("设置用户FLAG失败", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 设置单个用户是否有效
	 * @param userID 用户ID
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
			log.error("设置用户FLAG失败：", ex);
			throw new UserManagerException("设置用户FLAG失败", ex);
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
		String body = "您的帐号已于" + Convertor.date2String(new Date()) +
			(isValidate ? "开通" : "停止");
		Mail.send(Config.getString("mail.admin.address"), email, null, null,
				  "帐号信息", body, false);
	}

	/**
	 * 设置用户是否开通的标志
	 * @param userID 用户ID号
	 * @param value 是否开通（true or false）
	 * @param session Hibernate对象
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private void setUserFlag(Integer userID, boolean value, Session session) throws
		HibernateException, UserManagerException
	{
		if (!isUserDefined(userID, session))
		{
			throw new UserManagerException("指定的用户不存在");
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
	 * 根据标志查询用户（用户是否通过验证）
	 * @param isValidate true or false（查询条件）
	 * @return User对象的迭代器
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
			throw new UserManagerException("查询用户失败", ex);
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
	 * 查询用户（模糊查询），查询条件均可以为空
	 * @param name 用户名
	 * @param enterpriseName 企业名称
	 * @return User对象的迭代器
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
			throw new UserManagerException("查询用户失败", ex);
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
	 * 修改用户（不修改密码）    失败则抛出异常
	 * @param userID  用户ID号
	 * @param name 帐号
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
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
	 * 修改用户（不修改密码）    失败则抛出异常
	 * @param userID  用户ID号
	 * @param name 帐号
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @param roleID 角色ID号
	 * @param groupIDs 组ID号
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
	 * 修改用户
	 * @param userID 用户ID号
	 * @param name 帐号
	 * @param password 密码
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @param roleID 角色ID号
	 * @param groupIDs 组ID号
	 * @throws UserManagerException 修改失败抛出
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
			//更新基本信息
			UserForm user = updateUserBasicInfo(userID, name, password,
												enterpriseName, lawPersionCode,
												lawPersionName, lawPersionPhone,
												contactPersionName,
												contactPersionPhone,
												contactPersionMobile,
												contactAddress, postcode, email,
												fax, isValidate, memo, session);
			//给用户分配角色和组
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
			throw new UserManagerException("更新用户失败", ex);
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
	 * 建立组到用户的关系  把组分配给用户，分配失败则抛出异常
	 * @param user 用户对象
	 * @param groupIDs 组ID数组
	 * @param session session对象
	 * @throws HibernateException 异常
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
	 * 建立角色到用户的关系  把角色分配给用户，分配失败则抛出异常
	 * @param user 用户对象
	 * @param roleID 角色ID
	 * @param session session对象
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
	 * 创建用户并分配ROLE与GROUP  创建成功则返回用户对象，创建失败则抛出异常
	 * @param name 帐号
	 * @param password 密码
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @param roleID 角色ID号
	 * @param groupIDs 组ID号
	 * @return User接口
	 * @throws UserManagerException 创建失败抛出
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
			//创建用户
			if (isUserDefined(name, session))
			{
				throw new UserManagerException("账号为" + name + "已经存在");
			}
			UserForm user = createUser(name, password, enterpriseName,
									   lawPersionCode, lawPersionName,
									   lawPersionPhone, contactPersionName,
									   contactPersionPhone,
									   contactPersionMobile, contactAddress,
									   postcode, email, fax, isValidate, memo,
									   session);
			//给用户分配角色和组
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
			throw new UserManagerException("创建用户组失败", ex);
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
	 * 创建用户并分配ROLE与GROUP  创建成功则返回用户对象，创建失败则抛出异常
	 * @param name 帐号
	 * @param password 密码
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @param session Hibernate对象
	 * @return UserForm
	 * @throws HibernateException 创建失败抛出
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
	 * 创建GROUP  创建成功则返回用户对象，创建失败则抛出异常
	 * @param name 组名
	 * @param memo 备注
	 * @param session Hibernate对象
	 * @return GroupForm
	 * @throws HibernateException 创建失败抛出
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
	 * 创建Role  创建成功则返回角色对象，创建失败则抛出异常
	 * @param name 角色名
	 * @param memo 备注
	 * @param privileges 权限集
	 * @param session Hiberante对象
	 * @return RoleForm对象
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
	 * 得到用MD5编码的密码
	 * @param password 编码前的密码
	 * @return 编码之后的密码
	 */
	private String getMd5Password(String password)
	{
		org.apache.java.security.MD5 md5 = new org.apache.java.security.MD5();
		String md5Password = org.apache.java.lang.Bytes.toString(md5.digest(
			password.getBytes()));
		return md5Password;
	}

	/**
	 * 删除用户
	 * @param userID 用户ID
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
				throw new UserManagerException("指定的用户不存在");
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
			throw new UserManagerException("删除用户失败", ex);
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
	 * 帮用户找回密码
	 * @param name 用户的name
	 * @throws UserManagerException 失败
	 */
	public void findPassword(String name) throws UserManagerException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			//找到指定的用户
			if (!isUserDefined(name, session))
			{
				throw new UserManagerException("不存在账号为" + name + "的用户");
			}
			List result = session.find(
				"FROM UserForm as user WHERE user.name = ?", name,
				Hibernate.STRING);
			UserForm user = (UserForm) result.get(0);
			//设置新的密码
			String newPassword = generateRandomPassword();
			user.setPassword(getMd5Password(newPassword));
			//将新的密码发送给用户
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
			throw new UserManagerException("查询用户失败", ex);
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
	 * 删除用户
	 * @param user 要删除的用户
	 * @throws UserManagerException
	 */
	public void deleteUser(User user) throws UserManagerException
	{
		deleteUser(user.getUserID());
	}

	/**
	 * 修改用户
	 * @param userID 用户ID号
	 * @param name 帐号
	 * @param password 密码
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @throws UserManagerException 修改失败抛出
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
			//更新基本信息
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
			throw new UserManagerException("更新用户失败", ex);
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
	 * 更新基本信息
	 * @param userID 用户ID号
	 * @param name 帐号
	 * @param password 密码
	 * @param enterpriseName 企业名称
	 * @param lawPersionCode 法人代码
	 * @param lawPersionName 法人代表
	 * @param lawPersionPhone 法人代表电话
	 * @param contactPersionName 联系人
	 * @param contactPersionPhone 联系人电话
	 * @param contactPersionMobile 联系人手机
	 * @param contactAddress 联系人地址
	 * @param postcode 邮编
	 * @param email 电子邮件
	 * @param fax 传真
	 * @param isValidate 是否开通
	 * @param memo 备注
	 * @param session session对象
	 * @return UserForm更新后的用户对象
	 * @throws HibernateException 底层异常
	 * @throws UserManagerException 业务异常
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
			throw new UserManagerException("指定的用户不存在");
		}
		if (hasSameAccountUser(userID, name, session))
		{
			throw new UserManagerException("账号为" + name + "的用户已经存在");
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
		//需要修改密码
		if (password != null)
		{
			user.setPassword(getMd5Password(password));
		}

		session.update(user);
		return user;
	}

	/**
	 * 根据用户名查找用户
	 * @param fullName 用户名
	 * @return 用户User集合
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
//			throw new UserManagerException("查询用户失败");
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
	 * 根据用户账号查找用户
	 * @param name 用户账号
	 * @return 用户，包含用户的组和角色信息
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
				throw new UserManagerException("不存在账号为" + name + "的用户");
			}
			List result = session.find(
				"FROM UserForm as user WHERE user.name = ?", name,
				Hibernate.STRING);
			return loadDetail( (UserForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询用户失败", ex);
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
	 * 查找用户
	 * @param userID 用户ID号
	 * @return 用户，包含用户的组和角色信息
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
				throw new UserManagerException("指定的用户不存在");
			}
			UserForm user = new UserForm();
			session.load(user, userID);
			return loadDetail(user);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询用户失败", ex);
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
	 * 查询不在该组里面的用户
	 * @param groupID 组的ID号
	 * @return UserForm集合
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

			//构造查询符合条件的用户的sql
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
			throw new UserManagerException("查询用户失败", ex);
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
	 * 查找所有的用户
	 * @return User的集合
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
			throw new UserManagerException("查询用户失败", ex);
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
	 * 创建组 ， 创建成功返回组对象，创建失败则抛出异常
	 * @param name 组名
	 * @param memo 备注
	 * @param userIDs 分配给该组的用户id数组
	 * @return Group接口
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
				throw new UserManagerException("名称为" + name + "的用户组已经存在");
			}

			GroupForm group = createGroup(name, memo, session);
			//将用户分配到该组
			assignUserToGroup(group, userIDs, session);
			tx.commit();
			//将结果封装成DBGroup
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
			throw new UserManagerException("创建用户组失败", ex);
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
	 * 删除组  删除失败则抛出异常
	 * @param group 组ID号
	 * @throws UserManagerException
	 */
	public void deleteGroup(Group group) throws UserManagerException
	{
		deleteGroup(group.getGroupID());
	}

	/**
	 * 删除组  删除失败则抛出异常
	 * @param groupID 组ID号
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
				throw new UserManagerException("指定的用户组不存在");
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
			throw new UserManagerException("删除用户组失败", ex);
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
	 * 修改组   失败则抛出异常
	 * @param groupID 组ID号
	 * @param name 组名
	 * @param memo 备注
	 * @param userIDs 分配给该组的用户id数组
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
				throw new UserManagerException("指定的用户组不存在");
			}
			if (hasSameNameGroup(groupID, name, session))
			{
				throw new UserManagerException("名称为" + name + "的用户组已经存在");
			}
			GroupForm group = new GroupForm();
			session.load(group, groupID);
			group.setDateModified(new Date());
			group.setMemo(memo);
			group.setName(name);
			session.update(group);
			//将用户分配到该组
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
			throw new UserManagerException("更新用户组失败", ex);
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
	 * 查找组
	 * @param groupID 组的ID号
	 * @return 组，包含组的用户
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
				throw new UserManagerException("指定的用户组不存在");
			}
			GroupForm group = new GroupForm();
			session.load(group, groupID);
			return loadDetail(group);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询用户组失败", ex);
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
         * 查找组
         * @param name 组名
         * @return 组，包含组的用户
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
                        throw new UserManagerException("查询用户组失败", ex);
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
	 * 查找组
	 * @param name 组名
	 * @return 组，包含组的用户
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
				throw new UserManagerException("不存在名称为" + name + "的用户组");
			}
			List result = session.find("FROM GroupForm as g WHERE g.name = ?",
									   name, Hibernate.STRING);
			return loadDetail( (GroupForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询用户组失败", ex);
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
	 * 查询不包含该用户的组
	 * @param userID 用户ID号
	 * @return Group集合
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

			//构造查询符合条件的组的sql
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
			throw new UserManagerException("查询用户组失败", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 查找所有组
	 * @return Gruop 的集合
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
			throw new UserManagerException("查询用户组失败", ex);
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
	 * 创建Role  创建成功则返回角色对象，创建失败则抛出异常
	 * @param name 角色名
	 * @param memo 备注
	 * @param privileges 权限集
	 * @return Role接口
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
				throw new UserManagerException("名称为" + name + "的角色已经存在");
			}

			RoleForm role = createRole(name, memo, privileges, session);
			tx.commit();
			//将结果封装成DBRole
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
			throw new UserManagerException("创建角色失败", ex);
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
	 * 删除Role
	 * @param role 角色
	 * @throws UserManagerException
	 */
	public void deleteRole(Role role) throws UserManagerException
	{
		deleteRole(role.getRoleID());
	}

	/**
	 * 删除Role
	 * @param roleID RoleID号
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
				throw new UserManagerException("指定的角色不存在");
			}
			if (hasUserAssociated(roleID, session))
			{
				throw new UserManagerException("指定的角色已经分配给了用户，请先删除该角色与用户的关系");
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
			throw new UserManagerException("删除角色失败", ex);
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
	 * 修改Role   失败则抛出异常
	 * @param roleID 角色ID号
	 * @param name 角色名
	 * @param memo 备注
	 * @param privileges 权限
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
				throw new UserManagerException("指定的角色不存在");
			}
			if (hasSameNameRole(roleID, name, session))
			{
				throw new UserManagerException("名称为" + name + "的角色已经存在");
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
			throw new UserManagerException("更新角色失败", ex);
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
	 * 给角色分配用户
	 * @param role 角色
	 * @param users 用户
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
//                throw new UserManagerException("指定的角色不存在");
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
//            throw new UserManagerException("将用户分配给角色失败");
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
	 * 把用户分配给组，分配失败则抛出异常
	 * @param group 组对象
	 * @param userIDs 用户ID数组
	 * @param session session对象
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
	 * 查找Role
	 * @param roleID 角色ID号
	 * @return 角色，包含角色的用户信息
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
				throw new UserManagerException("指定的角色不存在");
			}
			RoleForm role = new RoleForm();
			session.load(role, roleID);
			return loadDetail(role);
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询角色失败", ex);
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
	 * 查找ROLE
	 * @param name 角色名称
	 * @return 角色，包含角色的用户信息
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
				throw new UserManagerException("不存在名称为" + name + "的角色");
			}
			List result = session.find(
				"FROM RoleForm as role WHERE role.name = ?", name,
				Hibernate.STRING);
			return loadDetail( (RoleForm) result.get(0));
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			throw new UserManagerException("查询角色失败", ex);
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
	 * 查找所有角色
	 * @return Role 的集合
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
			throw new UserManagerException("查询角色失败", ex);
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
	 * 判断用户是否存在
	 * @param userID 用户ID号
	 * @param session Session对象
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
	 * 判断用户是否存在
	 * @param name 用户名称
	 * @param session Session对象
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
	 * 判断组是否存在
	 * @param groupID  组ID号
	 * @param session Session对象
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
	 * 判断组是否存在
	 * @param groupName 组名称
	 * @param session Session对象
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
	 * 判断该ID的Role是否存在
	 * @param roleID  RoleID号
	 * @param session Session对象
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
	 * 判断该名称的Role是否存在
	 * @param roleName Role名称
	 * @param session Session对象
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
	 * 判断是否存在与指定用户的账号相同的用户
	 * @param userID 指定用户ID
	 * @param name 指定用户的账号
	 * @param session Session对象
	 * @return 存在与指定用户的账号相同的用户，返回true；否则返回false
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
	 * 判断是否存在与指定用户组同名的用户组
	 * @param groupID 指定用户组ID
	 * @param fullName 指定用户组的名称
	 * @param session Session对象
	 * @return 存在与指定用户组同名的用户组，返回true；否则返回false
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
	 * 判断是否存在与指定角色同名的角色
	 * @param roleID 指定角色ID
	 * @param fullName 指定角色的名称
	 * @param session Session对象
	 * @return 存在与指定角色同名的角色，返回true；否则返回false
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
	 * 加载用户的详细信息（角色，用户组）
	 * @param user UserForm对象
	 * @return 包含详细信息的DBUser对象
	 * @throws HibernateException
	 */
	private DBUser loadDetail(UserForm user)
	{
		user.getRole();
		user.getGroups().size();
		return new DBUser(user);
	}

	/**
	 * 加载用户组的详细信息（用户）
	 * @param group GroupForm对象
	 * @return 包含详细信息的DBGroup对象
	 * @throws HibernateException
	 */
	private DBGroup loadDetail(GroupForm group)
	{
		group.getUsers().size();
		return new DBGroup(group);
	}

	/**
	 * 加载角色的详细信息（用户）
	 * @param role RoleForm对象
	 * @return 包含详细信息的DBRole对象
	 * @throws HibernateException
	 */
	private DBRole loadDetail(RoleForm role)
	{
		role.getUsers().size();
		return new DBRole(role);
	}

	/**
	 * 判断是否存在用户与指定的角色相关联
	 * @param roleID 角色ID
	 * @param session session对象
	 * @return 存在用户与指定的角色相关联，返回true；否则返回false
	 * @throws HibernateException 底层异常
	 */
	private boolean hasUserAssociated(Integer roleID, Session session) throws
		HibernateException
	{
		return ( (Integer) session.iterate(
			"SELECT COUNT(*) from UserForm as user WHERE user.role.roleID = ?",
			roleID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * 产生随机的密码
	 * @return 密码
	 */
	private String generateRandomPassword()
	{
		String randomStr = System.currentTimeMillis() + "";
		return randomStr;
	}

	/**
	 * 初始化角色
	 * @param userinit Castor对象
	 * @param session Hibernate对象
	 * @return HashMap 角色名称、ID号的值对
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private HashMap initRoleInfo(Userinit userinit, Session session) throws
		HibernateException, UserManagerException
	{
		HashMap roleMap = new HashMap();
		roleMap.put("管理员角色", new Integer(1));
		roleMap.put("填报者角色", new Integer(2));
		roleMap.put("数据分析员", new Integer(3));

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
				throw new UserManagerException("名称为" + tempRole.getName() +
											   "的角色已经存在");
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
	 * 初始化时清除表中的用户相关信息，并进行最小初始化
	 * @param session Hibernate对象
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
			log.error("初始化用户失败", ex);
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
	 * 初始化用户
	 * @param userinit Castor对象
	 * @param roleMap 角色名称、ID号的值对
	 * @param session Hibernate对象
	 * @return HashMap 用户名称、ID号的值对
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

			//创建用户
			if (userMap.get(map.get("name")) != null)
			{
				throw new HibernateException("账号为" + (String) map.get("name") +
											 "已经存在");
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
			//给用户分配角色
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
	 * 初始化组
	 * @param userinit Castor对象
	 * @param userMap 用户名称、ID号的值对
	 * @param session Hibernate对象
	 * @throws HibernateException
	 * @throws UserManagerException
	 */
	private void initGroupInfo(Userinit userinit, HashMap userMap,
							   Session session) throws HibernateException,
		UserManagerException
	{
		HashMap groupMap = new HashMap();
		groupMap.put("管理员组", new Integer(1));

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
				throw new UserManagerException("名称为" + tempGroup.getName() +
											   "的用户组已经存在");
			}

			GroupForm group = createGroup(tempGroup.getName(),
										  tempGroup.getMemo(), session);
			groupMap.put(group.getName(), group.getGroupID());

			//将用户分配到该组
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
	 * 解析初始化XML文件
	 * @param in 文件输入流
	 * @return Castor对象
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
			log.error("初始化用户失败，XML文件解析失败 ", ex);
			throw new UserManagerException(ex);
		}

		if (userinit == null)
		{
			throw new UserManagerException("初始化用户失败，请检查输入文件！ ");
		}
		return userinit;
	}
}