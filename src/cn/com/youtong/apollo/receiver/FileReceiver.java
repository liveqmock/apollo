package cn.com.youtong.apollo.receiver;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.*;
import org.exolab.castor.xml.*;
import cn.com.youtong.apollo.authentication.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.receiver.xml.*;
import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.tools.zip.*;
import cn.com.youtong.apollo.common.mail.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.log.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class FileReceiver
{
	private static FileReceiver fileReceiver;

	/**�����û���Ϣ���ļ���*/
	private static final String METAINF_FILE_NAME = Constant.METAINF_FILE_NAME;
	private Log log = LogFactory.getLog(this.getClass());
	private String zipDataPath;
	private String extractDataPath;
	private String backupDataPath;
	private String outOfTimeLimitPath;
	//��ǰ�Ƿ���ִ�е��������
	private boolean isBusy;

	private FileReceiver()
	{
		//��ø���·��
		zipDataPath = Config.getString("cn.com.youtong.apollo.zipdata.directory");
		extractDataPath = Config.getString("cn.com.youtong.apollo.extractdata.directory");
		backupDataPath = Config.getString("cn.com.youtong.apollo.backupdata.directory");
		outOfTimeLimitPath = Config.getString("cn.com.youtong.apollo.outoftimelimitdata.directory");
//		zipDataPath = "d:/apollo/apollo/apollo/www/zip";
//		extractDataPath = "d:/apollo/apollo/apollo/www/tempextractdata";
//		backupDataPath = "d:/apollo/apollo/apollo/www/backupdata";
	}

	/**
	 * ������ʵ����ϵͳ��ֻ������һ��ʵ��
	 * @return ��ʵ��
	 */
	public static synchronized FileReceiver getInstance()
	{
		if(fileReceiver == null)
		{
			fileReceiver = new FileReceiver();
		}
		return fileReceiver;
	}

	/**
	 * ��ѹ�ļ����������е�����
	 */
	public void receive()
	{
		if(checkBusy())
		{
			return;
		}
//		log.info("����ϱ������ļ��У�" + zipDataPath);
		//��ѹ�����������ļ�
		try
		{
			extractAndImport();
		}
		catch(Exception ex)
		{
			log.error(ex);
		}
		finally
		{
			setBusy(false);
		}
	}

	private synchronized void setBusy(boolean isBusy)
	{
		this.isBusy = isBusy;
	}

	private synchronized boolean checkBusy()
	{
		if(this.isBusy)
		{
			return true;
		}
		setBusy(true);
		return false;
	}

	private void extractAndImport()
	{
		//��ѹ���е��ļ�
		File f = new File(zipDataPath);
		File[] files = f.listFiles();
		UserInfo userInfo = null;

		if(files == null || files.length == 0)
		{
			return;
		}

		log.info("��鵽���ļ���׼�������ļ�...");

		//��ʱ������
		files = sortFilesByLastModify(files);

		for(int i = 0; i < files.length; i++)
		{
			File tempFile = files[i];
			//�ж����ļ������ļ��У����ļ�����ɾ��
			if(tempFile.isDirectory())
			{
				clearDirectory(zipDataPath + "/" + tempFile.getName());
				tempFile.delete();
				log.info("�ļ��У�" + tempFile.getName() + "��Ч����ɾ����");
				continue;
			}
			//��ѹ�ļ�
			if(!tempFile.getName().endsWith(".zip"))
			{
				moveFile2BackupDirectory(tempFile, false);
				log.info("�ϱ��ļ���" + tempFile.getName() + "����ZIP�ļ�����ת�Ƶ������ļ��С�");
				continue;
			}
			try
			{
				String zipFilePath = zipDataPath + "/" + tempFile.getName();
				log.info("���ڽ�ѹ�ļ���" + tempFile.getName() + "...");

				try
				{
					Zip.unzip(zipFilePath, extractDataPath);
				}
				catch(IOException ex2)
				{
					if(ex2.getMessage().startsWith("��Դ���ڱ�ռ�ã�"))
					{
						log.info("��Դ���ڱ�ռ�ã��Ժ��ѹ");
						continue;
					}
					else
					{
						throw new IOException(ex2.getMessage());
					}
				}

				log.info("��ѹ���");
				//��֤�û�Ȩ��
				try
				{
					userInfo = getUserInfoHasPassedAuthentication();
				}
				catch(ReceiveException ex1)
				{
					moveFile2BackupDirectory(tempFile, false);
					log.error("�����ļ�ʧ�ܣ�" + ex1.getMessage() + tempFile.getName() + "��", ex1);
					//����ʧ�ܣ��������Ա
					sendMail("��������ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + ex1.getMessage() + tempFile.getName() + "��");
					throw new ReceiveException(ex1);
				}
				UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
				User user = u.createUserManager().getUserByName(userInfo.getUsername());
				String taskID = userInfo.getTaskid();

				//Ϊ�˱�֤�ϱ����� .xml ��ʽ�ģ������������ѹһ�鵼��һ��
				importData(user, taskID);

				//�޸����Ʋ�ת�� .zip �ļ�
				moveFile2BackupDirectory(tempFile, true);
			}
			catch(FactoryException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				log.error("�����ļ�ʧ��", ex);
				sendMail("��������ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + ex.getMessage());
			}
			catch(UserManagerException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				if(userInfo != null)
				{
					log.error("�����ļ�ʧ�ܣ�" + "�ϱ����û�������,�û�����" + userInfo.getUsername(), ex);
					sendMail("��������ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + "�ϱ����û�������,�û�����" + userInfo.getUsername());
				}
				else
				{
					log.error("�����ļ�ʧ��", ex);
					sendMail("��������ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + ex.getMessage());
				}
			}
			catch(ReceiveException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				log.error("�����ļ�ʧ��", ex);
				sendMail("��������ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + ex.getMessage());
			}
			catch(IOException ex)
			{
				log.error("�����ļ�ʧ�ܣ�" + "��ѹ�����ļ�ʧ�ܣ� �ļ�����" + tempFile.getName(), ex);
				sendMail("�����ļ�ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + "��ѹ�����ļ�ʧ�ܣ� �ļ�����" + tempFile.getName() + "  " + ex.getMessage());
				moveFile2BackupDirectory(tempFile, false);
			}
			catch(Exception ex)
			{
				log.error("�����ļ�ʧ�ܣ�" + " �ļ�����" + tempFile.getName(), ex);
				sendMail("�����ļ�ʧ��", "�����ļ�:" + tempFile.getName() + "ʧ�ܣ�" + " �ļ�����" + tempFile.getName() + "  " + ex.getMessage());
				moveFile2BackupDirectory(tempFile, false);
			}
			finally
			{
				//������� xml �ļ�
				clearDirectory(extractDataPath);
			}
		}
		log.info("���ݴ������\r\n\r\n");
	}

	/**
	 * ������޸�ʱ������
	 * @param files �ļ��б�
	 * @return ��������ļ��б�
	 */
	private File[] sortFilesByLastModify(File[] files)
	{
		File tempFile;
		int length = files.length;

		for(int i = 0; i < length - 1; i++)
		{
			Date d1 = new Date(files[i].lastModified());
			int position = i;
			for(int j = i + 1; j < length; j++)
			{
				Date d2 = new Date(files[j].lastModified());
				if(d2.before(d1))
				{
					d1 = d2;
					position = j;
				}
			}
			if(position > i)
			{
				tempFile = files[i];
				files[i] = files[position];
				files[position] = tempFile;
			}
		}
		return files;
	}

	/**
	 * ��֤�û���Ϣ
	 * @return �û���Ϣ����
	 * @throws ReceiveException
	 */
	private UserInfo getUserInfoHasPassedAuthentication()
		throws ReceiveException
	{
		UserInfo userInfo = null;
		File file = null;
		FileInputStream fis = null;
		try
		{
			file = new File(extractDataPath + "/" + METAINF_FILE_NAME);
			fis = new FileInputStream(file);
			userInfo = new UserInfo();
			Properties properties = new Properties();
			properties.load(fis);
			String userName = properties.getProperty("userName");
			String password = properties.getProperty("password");
			String taskID = properties.getProperty("taskID");
			userInfo.setPassword(password);
			userInfo.setUsername(userName);
			userInfo.setTaskid(taskID);
			log.info("��֤�û���" + userName + "��Ȩ��...");
			AuthEnvironment aE = new AuthEnvironment(userName, password);
			Authentication at = ((AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.getName())).getAuthentication(aE);

			if(!at.authenticate())
			{
				String message = "�û���" + userName + "��֤ʧ�ܣ�����װ������";
				log.info(message);
				throw new ReceiveException(message);
			}
			log.info("�û�ͨ����֤�������ϱ����ݣ�׼��װ������...");
		}
		catch(ReceiveException ex)
		{
			throw ex;
		}
		catch(Exception ex)
		{
			String message = "���ܵõ��û���֤��Ϣ";
			log.error(message, ex);
			throw new ReceiveException(message, ex);
		}
		finally
		{
			try
			{
				if(fis != null)
				{
					fis.close();
				}
			}
			catch(IOException ex1)
			{
			}
			if(file != null)
			{
				file.delete();
			}
		}

		return userInfo;
	}

	/**
	 * ����ѹĿ¼�µ������ļ��������ݿ�
	 * @param user �û�
	 * @param taskID ����ID
	 * @throws ReceiveException
	 */
	private void importData(User user, String taskID)
		throws ReceiveException
	{
		try
		{
			ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
			UnitACL unitACL = modelManager.getUnitACL(user);

			log.info("��ʼ���������ļ�...");
			File f = new File(extractDataPath);
			File[] files = f.listFiles();
			for(int i = 0; i < files.length; i++)
			{
				importDataFile(files[i], modelManager, unitACL);
			}
			log.info("�������");
		}
		catch(ModelException ex)
		{
			throw new ReceiveException(ex);
		}
		catch(FactoryException ex)
		{
			throw new ReceiveException(ex);
		}
	}

	/**
	 * ����ָ���������ļ�
	 * @param file �����ļ�
	 * @param modelManager ModelManager����
	 * @param unitACL UnitACL����
	 * @throws ReceiveException
	 */
	private void importDataFile(File file, ModelManager modelManager, UnitACL unitACL)
		throws ReceiveException
	{
		FileInputStream fileInputStream = null;
		try
		{
			fileInputStream = new FileInputStream(file);

			//�ϱ�����
			LoadResult loadResult = modelManager.getDataImporter().importData(fileInputStream, unitACL);

			//��¼�ϱ���־
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logLoadDataEvent(loadResult, unitACL.getUser(), "", LogManager.MAIL_MODE);

			if(loadResult.getAuditFailedUnitIDs().hasNext())
			{
				// ���û��ͨ��
				sendMail("����δ���ͨ��������", loadResult.getMessage());
			}

			if(loadResult.getOverdueUnitIDs().hasNext())
			{
				//�������ļ��Ƶ������ļ���
				String fileName = file.getName();
				int i = fileName.indexOf(".xml");
				String outOfTimeLimitFileName = fileName.substring(0, i) + "_YTAPL_" + System.currentTimeMillis() + fileName.substring(i);
				File newFile = new File(backupDataPath + "/" + outOfTimeLimitFileName);
				file.renameTo(newFile);
				log.error("�û���" + unitACL.getUser().getName() + "�ϱ����ݹ��ڣ������ļ���ת�Ƶ������ļ��У��ļ�����" + outOfTimeLimitFileName);

				sendMail("��������ʧ��", "�û���" + unitACL.getUser().getName() + "�ϱ����ݹ��ڣ������ļ���ת�Ƶ������ļ��У��ļ�����" + outOfTimeLimitFileName);
			}
		}
		catch(Exception ex)
		{
			throw new ReceiveException(ex);
		}
		finally
		{
			Util.close(fileInputStream);
		}
	}

	/**
	 * �жϵ�ǰʱ���Ƿ��������ʱ��֮�⣬λ������ʱ��֮�ⷵ�� true �����򷵻�false
	 * @param taskTime ����ʱ�����
	 * @return boolean
	 */
	private boolean isOutOfTimeLimt(TaskTime taskTime)
	{
		Date currentDate = new Date(System.currentTimeMillis());
		Date startDate = taskTime.getSubmitFromTime();
		Date endDate = taskTime.getSubmitEndTime();
		if(currentDate.before(endDate) && currentDate.after(startDate))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void sendMail(String subject, String body)
	{
		body = Convertor.date2String(new Date()) + "   " + body;
		Mail.send(Config.getString("mail.post.address"), Config.getString("mail.admin.address"), null, null, subject, body, false);
	}

	/**
	 * �õ��ű�������
	 * @param scripts �ű�Script����
	 * @return �ű�����String����
	 */
	private List getScriptContent(List scripts)
	{
		List result = new LinkedList();
		for(Iterator itr = scripts.iterator(); itr.hasNext(); )
		{
			result.add(((Script) itr.next()).getContent());
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
		for(int i = 0; i < errors.size(); i++)
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
		for(int i = 0; i < warnings.size(); i++)
		{
			warningMessage.append("��" + (i + 1) + "��" + warnings.get(i) + "\n");
		}
		return warningMessage.toString();
	}

	/**
	 * ��ն����ļ���(�ݹ�)
	 * @param path ·����
	 */
	private void clearDirectory(String path)
	{
		File f = new File(path);
		File[] files = f.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			File tempFile = files[i];
			if(tempFile.isDirectory())
			{
				clearDirectory(path + "/" + tempFile.getName());
				tempFile.delete();
			}
			tempFile.delete();
		}
	}

	/**
	 * �޸����Ʋ�ת�� .zip �ļ�
	 * @param file Ҫת�Ƶ��ļ�����
	 * @param hasReport �Ƿ����ϱ������ϱ����ļ����͡������ϱ����ļ����ֿ����
	 */
	private void moveFile2BackupDirectory(File file, boolean hasReport)
	{

		String postPath = Convertor.date2MonthlyString(new Date());
		File tempFile = new File(backupDataPath + "/" + postPath);
		tempFile.mkdir();
		tempFile = new File(backupDataPath + "/" + postPath + "/normal");
		tempFile.mkdir();
		tempFile = new File(backupDataPath + "/" + postPath + "/error");
		tempFile.mkdir();

		String fileName = file.getName();
		int i = fileName.lastIndexOf(".");

		String backupFileName = null;
		if(i != -1)
		{
			String postfix = fileName.substring(i);
			String name = fileName.substring(0, i);

			backupFileName = name + "_YTAPL_" + System.currentTimeMillis() + postfix;
		}
		else
		{
			backupFileName = fileName + "_YTAPL_" + System.currentTimeMillis();
		}

		File newFile = new File(backupDataPath + "/" + postPath + "/" + (hasReport ? "normal/" : "error/") + backupFileName);
		file.renameTo(newFile);
	}

	/**
	 * ���ش�� Zip���ļ���·��
	 * @return ·��
	 */
	public String getZipPath()
	{
		return zipDataPath;
	}

	/**
	 * ���ش�� xml �ļ���·��
	 * @return ·��
	 */
	public String getExtractPath()
	{
		return extractDataPath;
	}

	/**
	 * ���ش�� ���� �ļ���·��
	 * @return ·��
	 */
	public String getBackupPath()
	{
		return backupDataPath;
	}

	public static void main(String[] args)
	{
		FileReceiver t = FileReceiver.getInstance();

		t.receive();
	}
}
