package cn.com.youtong.apollo.webservice;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.*;
import cn.com.youtong.apollo.authentication.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.dictionary.*;
import cn.com.youtong.apollo.log.*;
import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * @todo ���ڴ����ֵ��Ȩ���ж���������Ϊ׼�ģ���������ʹ����ֵ��ڶ������Ѿ��ֿ���
 * ��������û����ɡ�
 */
public class FrontServiceImpl
	implements FrontService
{
	/** Log */
	private Log log = LogFactory.getLog(FrontServiceImpl.class);
	private final static int SUCCESS_CODE = 0;
	private final static int INVALID_USER_CODE = 1;
	private final static String INVALID_USER_DESC = "�û��������벻ƥ��";
	private final static int SYSTEM_ERROR_CODE = -1;
	private final static String SYSTEM_ERROR_DESC = "ϵͳ���ݴ���: ";
	private final static int PROHIBIT_CODE = 2;
	private final static String PROHIBIT_DESC = "��û����Ӧ����Ȩ��";

	private final static int NO_TASK_CODE = 11;
	private final static String NO_TASK_DESC = "û������������";

	public FrontServiceImpl()
	{
	}

	public ServiceResult uploadData(String data, String username,
									String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		try
		{
			// �ж�Ȩ��
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_REPORT))
			{
				return noPrivilegeResult(result);
			}
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}

		// ת��Ϊ������
		//byte[] dataBytes = data.getBytes();
		//ByteArrayInputStream in = new ByteArrayInputStream(dataBytes);
		InputStream in = null;
		String taskID = null;
		try
		{
			in = Util.decodeBase64AndUnGZIP(data);

			taskID = TaskIDFinder.findTaskID(in);
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			Util.close(in); ;
		}

		if (taskID == null)
		{
			return noTaskResult(result);
			/** @todo xml���ݲ��Ϸ�������������ID*/
		}

		// ��������
		//in = new ByteArrayInputStream(dataBytes);
		ServiceResult result2 = null;
		try
		{
			in = Util.decodeBase64AndUnGZIP(data);
			result2 = loadData(in, taskID, username);
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			Util.close(in);
		}

		return result2;
	}

	/**
	 * ��������
	 * @param taskID
	 * @param date   ������������ʱ����е�һ��
	 * @param username
	 * @param password
	 * @return
	 */
	public ServiceResult downloadData(String taskID, String unitID, Date date,
									  String username, String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);
		taskID = Util.decodeBase64(taskID);
		unitID = Util.decodeBase64(unitID);

		if(log.isDebugEnabled())
		{
			log.debug("Download data");
			log.debug("\tusername: "+ username);
			log.debug("\tpassword: "+ password);
			log.debug("\ttaskID: "+ taskID);
			log.debug("\tunitID: "+ unitID);
			java.text.SimpleDateFormat fromat= new java.text.SimpleDateFormat("yyyy-MM-dd");
			log.debug("\tdate: "+ fromat.format(date));
		}

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		String fileName = tempFileName();
		OutputStream out = null;
		try
		{
			// �ж�Ȩ��
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_REPORT))
			{
				return noPrivilegeResult(result);
			}

			ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
				getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			Task task = modelMng.getTask();
			TaskTime taskTime = task.getTaskTime(date);

			User user = getUser(username);
			UnitACL acl = modelMng.getUnitACL(user);

			out = new GZIPOutputStream(new FileOutputStream(fileName));
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");

			try
			{
				modelMng.getDataExporter().exportEditorData(unitID, taskTime, writer,
					acl);
			}
			finally
			{
				Util.close(out);
			}

			byte[] content = readFromFile(fileName);

			result.setErrCode(SUCCESS_CODE);
			result.setContent(new String(Util.encodeBase64(content)));
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			deleteFile(fileName);
		}

		return result;
	}

	/**
	 * �����������ݣ�
	 * @param taskID
	 * @param date   ������������ʱ����е�һ��
	 * @param username
	 * @param password
	 * @return
	 */
	public ServiceResult downloadAllData(String taskID, Date date,
										 String username, String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		taskID = Util.decodeBase64(taskID);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		String fileName = tempFileName();

		try
		{
			// �ж�Ȩ��
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_REPORT))
			{
				return noPrivilegeResult(result);
			}

			ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
				getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			Task task = modelMng.getTask();
			TaskTime taskTime = task.getTaskTime(date);

			User user = getUser(username);
			UnitACL acl = modelMng.getUnitACL(user);

			OutputStream out = null;
			try
			{
				out = new GZIPOutputStream(new FileOutputStream(fileName));
				OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
				modelMng.getDataExporter().exportAllData(taskTime, writer, acl);
			}
			finally
			{
				Util.close(out);
			}

			byte[] content = readFromFile(fileName);

			result.setErrCode(SUCCESS_CODE);
			result.setContent(new String(Util.encodeBase64(content)));
		}
		catch (Exception ex)
		{
			result.setErrCode(SYSTEM_ERROR_CODE);
			result.setContent(SYSTEM_ERROR_DESC);
		}
		finally
		{
			deleteFile(fileName);
		}

		return result;
	}

	public ServiceResult downloadDataByTree(String taskID, String unitID,
											Date date, String username,
											String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		taskID = Util.decodeBase64(taskID);
		unitID = Util.decodeBase64(unitID);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		String fileName = tempFileName();

		try
		{
			// �ж�Ȩ��
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_REPORT))
			{
				return noPrivilegeResult(result);
			}

			ModelManagerFactory modelMngFcty =
				(ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.
				getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);

			Task task = modelMng.getTask();
			TaskTime taskTime = task.getTaskTime(date);

			User user = getUser(username);
			UnitACL acl = modelMng.getUnitACL(user);

			OutputStream out = null;
			try
			{
				out = new GZIPOutputStream(new FileOutputStream(fileName));
				OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
				modelMng.getDataExporter().exportDataByTree(unitID, taskTime,
					writer, acl);
			}
			finally
			{
				Util.close(out);
			}

			byte[] content = readFromFile(fileName);

			result.setErrCode(SUCCESS_CODE);
			result.setContent(new String(Util.encodeBase64(content)));
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			deleteFile(fileName);
		}

		return result;
	}

	/**
	 * ��������
	 * <p>
	 * Լ���ڴ�
	 *
	 * @param definition  ������
	 * @param username
	 * @param password
	 */
	public ServiceResult publishTask(String definition, String username,
									 String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		InputStream input = null;
		try
		{
			// �ж�Ȩ��
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}

			input = Util.decodeBase64AndUnGZIP(definition);

			TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
				getInstance(TaskManagerFactory.class.getName());

			TaskManager taskMng = taskMngFcty.createTaskManager();
			taskMng.publishTask(input);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return this.systemErrorResult(result, ex);
		}
		finally
		{
			Util.close(input);
		}
	}

	public ServiceResult downloadTask(String id, String username,
									  String password)
	{
		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		// �ж�Ȩ��
		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}
			TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
				getInstance(TaskManagerFactory.class.getName());
			TaskManager taskMng = taskMngFcty.createTaskManager();
			Task task = taskMng.getTaskByID(id);
			/** @todo �������� Ҫ�������������������������� */
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}

		return null;
	}

	/**
	 * ɾ������
	 *
	 * @param taskID
	 * @param username
	 * @param password
	 */
	public ServiceResult deleteTask(String taskID, String username,
									String password)
	{
		taskID = Util.decodeBase64(taskID);
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}
			TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
				getInstance(TaskManagerFactory.class.getName());
			TaskManager taskMng = taskMngFcty.createTaskManager();
			Task task = taskMng.getTaskByID(taskID);
			taskMng.deleteTask(task);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
	}

	/**
	 * ���������ֵ�
	 *
	 * @param content
	 * @param username
	 * @param password
	 */
	public ServiceResult publishDictionary(String content, String username,
										   String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		InputStream in = null;
		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}

			in = Util.decodeBase64AndUnGZIP(content);

			DictionaryManagerFactory dictMngFcty = (DictionaryManagerFactory)
				Factory.getInstance(DictionaryManagerFactory.class.getName());
			DictionaryManager dictMng = dictMngFcty.createDictionaryManager();
			dictMng.updateDictionary(in);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			Util.close(in);
		}
	}

	public ServiceResult downloadDictionary(String id, String username,
											String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);
		id = Util.decodeBase64(id);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		String fileName = tempFileName();
		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}

			OutputStream out = null;
			try
			{
				out = new GZIPOutputStream(new FileOutputStream(fileName));

				DictionaryManagerFactory dictMngFcty = (
					DictionaryManagerFactory) Factory.getInstance(
					DictionaryManagerFactory.class.getName());
				DictionaryManager dictMng = dictMngFcty.createDictionaryManager();
				dictMng.outPutDictionary(id, out);
			}
			finally
			{
				Util.close(out);
			}

			byte[] cont = readFromFile(fileName);

			result.setErrCode(SUCCESS_CODE);
			result.setContent(new String(Util.encodeBase64(cont)));

			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			deleteFile(fileName);
		}
	}

	public ServiceResult deleteDictionary(String id, String username,
										  String password)
	{
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		id = Util.decodeBase64(id);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}
			DictionaryManagerFactory dictMngFcty = (DictionaryManagerFactory)
				Factory.getInstance(DictionaryManagerFactory.class.getName());
			DictionaryManager dictMng = dictMngFcty.createDictionaryManager();
			dictMng.deleteDictionary(id);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
	}

	public ServiceResult publishScriptSuit(String taskID, String script,
										   String username, String password)
	{
		taskID = Util.decodeBase64(taskID);
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		InputStream in = null;
		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}

			in = Util.decodeBase64AndUnGZIP(script);
			TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
				getInstance(TaskManagerFactory.class.getName());
			TaskManager taskMng = taskMngFcty.createTaskManager();
			taskMng.publishScriptSuit(taskID, in);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			Util.close(in);
		}
	}

	public ServiceResult downloadScriptSuit(String taskID, String suitName,
											String username, String password)
	{
		taskID = Util.decodeBase64(taskID);
		suitName = Util.decodeBase64(suitName);
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		String fileName = tempFileName();
		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}

			OutputStream out = null;
			try
			{
				TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
					getInstance(TaskManagerFactory.class.getName());
				TaskManager taskMng = taskMngFcty.createTaskManager();
				taskMng.outputScriptSuit(taskID, suitName, out);
			}
			finally
			{
				Util.close(out);
			}

			byte[] content = readFromFile(fileName);

			result.setErrCode(SUCCESS_CODE);
			result.setContent(new String(Util.encodeBase64(content)));
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
		finally
		{
			deleteFile(fileName);
		}
	}

	public ServiceResult deleteScriptSuit(String taskID, String suitName,
										  String username, String password)
	{
		taskID = Util.decodeBase64(taskID);
		username = Util.decodeBase64(username);
		password = Util.decodeBase64(password);

		ServiceResult result = new ServiceResult();
		// ��֤�û���������
		if (!validateUser(username, password))
		{
			return invalidUserResult(result);
		}

		try
		{
			if (!hasPrivilege(username, SetOfPrivileges.MANAGE_TASK))
			{
				return noPrivilegeResult(result);
			}
			TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
				getInstance(TaskManagerFactory.class.getName());
			TaskManager taskMng = taskMngFcty.createTaskManager();
			taskMng.deleteScriptSuit(taskID, suitName);

			result.setErrCode(SUCCESS_CODE);
			return result;
		}
		catch (Exception ex)
		{
			return systemErrorResult(result, ex);
		}
	}

	/**
	 * ��֤�û����������Ƿ���Ե�¼��ϵͳ
	 * @param username           �û���
	 * @param password           ����
	 * @return                   ���Ե�¼����true������false��ϵͳ�ڲ�����Ҳ����false
	 public
	 */
	boolean validateUser(String username, String password)
	{
		AuthEnvironment authEnvironment = new AuthEnvironment(username,
			password);

		try
		{
			//�ж���֤����
			AuthenticationFactory authFcty = (AuthenticationFactory) Factory.
				getInstance(AuthenticationFactory.class.getName());
			Authentication authentication = authFcty.getAuthentication(
				authEnvironment);

			if (authentication.authenticate())
			{
				return true;
			}
		}
		catch (Exception ex)
		{
			log.error(ex);
		}
		return false;
	}

	private ServiceResult invalidUserResult(ServiceResult result)
	{
		result.setErrCode(INVALID_USER_CODE);
		result.setContent(
			new sun.misc.BASE64Encoder().encode(INVALID_USER_DESC.getBytes()));

		return result;
	}

	private ServiceResult noPrivilegeResult(ServiceResult result)
	{
		result.setErrCode(PROHIBIT_CODE);
		result.setContent(
			new sun.misc.BASE64Encoder().encode( (PROHIBIT_DESC).getBytes()));

		return result;
	}

	private ServiceResult noTaskResult(ServiceResult result)
	{
		result.setErrCode(NO_TASK_CODE);
		result.setContent(NO_TASK_DESC);

		return result;
	}

	private ServiceResult systemErrorResult(ServiceResult result, Exception ex)
	{
		result.setErrCode(SYSTEM_ERROR_CODE);
		result.setContent(
			new sun.misc.BASE64Encoder().encode( (SYSTEM_ERROR_DESC +
												  ex.getMessage()).getBytes()));

		return result;
	}

	/**
	 * �����û�username��������taskID�ķ���Ȩ���б��������������null
	 *
	 * @param taskID        ����ID
	 * @param username      �û���
	 * @return              ����Ȩ�ޣ��������������null
	 */
	private UnitACL getUnitACL(String taskID, String username)
	{
		try
		{
			User user = getUser(username);

			ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
				getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			UnitACL acl = modelMng.getUnitACL(user);
			return acl;
		}
		catch (Exception ex)
		{
			log.error(ex);
		}
		return null;
	}

	private User getUser(String username) throws FactoryException,
		UserManagerException
	{
		UserManagerFactory userMngFcty = (UserManagerFactory) Factory.
			getInstance(UserManagerFactory.class.getName());
		UserManager userMng = userMngFcty.createUserManager();
		User user = userMng.getUserByName(username);

		return user;
	}

	private ServiceResult loadData(InputStream stream, String taskID,
								   String username)
	{
		ServiceResult result = new ServiceResult();
		try
		{
			UserManagerFactory userMngFcty = (UserManagerFactory) Factory.
				getInstance(UserManagerFactory.class.getName());
			UserManager userMng = userMngFcty.createUserManager();
			User user = userMng.getUserByName(username);

			ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
				getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			UnitACL acl = modelMng.getUnitACL(user);

			//�ϱ�����
			LoadResult loadResult = modelMng.getDataImporter().importData(
				stream, acl);
			result.setErrCode(SUCCESS_CODE);

			//��¼�ϱ���־
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logLoadDataEvent(loadResult, acl.getUser(), "", LogManager.CLIENT_MODE);

			result.setContent(Util.encodeBase64(loadResult.getMessage()));
		}
		catch (Exception ex)
		{
			log.error(ex);
			return systemErrorResult(result, ex);
		}

		return result;
	}

	/**
	 * �õ��ű�������
	 * @param scripts �ű�Script����
	 * @return �ű�����String����
	 */
	private List getScriptContent(List scripts)
	{
		List result = new LinkedList();
		for (Iterator itr = scripts.iterator(); itr.hasNext(); )
		{
			result.add( ( (Script) itr.next()).getContent());
		}
		return result;
	}

	/**
	 * �õ���˽���Ĵ�����Ϣ
	 * @param auditResult ��˽��
	 * @return ��˽���Ĵ�����Ϣ
	 */
	private String getErrorMessage(AuditResult auditResult)
	{
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("������" + errors.size() + ":\n");
		for (int i = 0; i < errors.size(); i++)
		{
			errorMessage.append("��" + (i + 1) + "��" + errors.get(i) + "\n");
		}
		return errorMessage.toString();
	}

	/**
	 * �õ���˽���ľ�����Ϣ
	 * @param auditResult ��˽��
	 * @return ��˽���ľ�����Ϣ
	 */
	private String getWarningMessage(AuditResult auditResult)
	{
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("������" + warnings.size() + ":\n");
		for (int i = 0; i < warnings.size(); i++)
		{
			warningMessage.append("��" + (i + 1) + "��" + warnings.get(i) + "\n");
		}
		return warningMessage.toString();
	}

	/**
	 * ��ĳ�����Ƿ�߱�Ȩ��
	 * @param username
	 * @param privilege
	 * @return
	 */
	private boolean hasPrivilege(String username, int privilege) throws
		FactoryException, UserManagerException
	{
		User user = this.getUser(username);
		return hasPrivilege(user, privilege);
	}

	/**
	 * ��ĳ�����Ƿ�߱�Ȩ��
	 * @param user               �û�
	 * @param privilege          ��������
	 * @return                   �Ƿ���Է���
	 */
	private boolean hasPrivilege(User user, int privilege)
	{
		Role role = user.getRole();
		return role.getPrivileges().getPrivilege(privilege);
	}

	private String tempFileName()
	{
		String tempDir = Config.getString(
			"cn.com.youtong.apollo.data.export.tempdir");

		long currTimeInMs = System.currentTimeMillis();

		return tempDir + File.separator + currTimeInMs + "_" +
			Util.generateRandom();
	}

	private void deleteFile(String fileName)
	{
		File f = new File(fileName);
		f.delete();
	}

	private byte[] readFromFile(String fileName) throws IOException
	{
		InputStream in = null;
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			in = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buf = new byte[1024];
			for (int len = 0; (len = in.read(buf)) > 0; )
			{
				out.write(buf, 0, len);
			}

			return out.toByteArray();
		}
		catch (IOException ex)
		{
			throw ex;
		}
		finally
		{
			Util.close(in); ;
		}
	}
}