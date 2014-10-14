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

	/**保存用户信息的文件名*/
	private static final String METAINF_FILE_NAME = Constant.METAINF_FILE_NAME;
	private Log log = LogFactory.getLog(this.getClass());
	private String zipDataPath;
	private String extractDataPath;
	private String backupDataPath;
	private String outOfTimeLimitPath;
	//当前是否在执行导入的任务
	private boolean isBusy;

	private FileReceiver()
	{
		//获得各种路径
		zipDataPath = Config.getString("cn.com.youtong.apollo.zipdata.directory");
		extractDataPath = Config.getString("cn.com.youtong.apollo.extractdata.directory");
		backupDataPath = Config.getString("cn.com.youtong.apollo.backupdata.directory");
		outOfTimeLimitPath = Config.getString("cn.com.youtong.apollo.outoftimelimitdata.directory");
//		zipDataPath = "d:/apollo/apollo/apollo/www/zip";
//		extractDataPath = "d:/apollo/apollo/apollo/www/tempextractdata";
//		backupDataPath = "d:/apollo/apollo/apollo/www/backupdata";
	}

	/**
	 * 获得类的实例，系统中只允许有一个实例
	 * @return 类实例
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
	 * 解压文件并导入所有的数据
	 */
	public void receive()
	{
		if(checkBusy())
		{
			return;
		}
//		log.info("检测上报数据文件夹：" + zipDataPath);
		//解压并导入所有文件
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
		//解压所有的文件
		File f = new File(zipDataPath);
		File[] files = f.listFiles();
		UserInfo userInfo = null;

		if(files == null || files.length == 0)
		{
			return;
		}

		log.info("检查到有文件，准备导入文件...");

		//按时间排序
		files = sortFilesByLastModify(files);

		for(int i = 0; i < files.length; i++)
		{
			File tempFile = files[i];
			//判断是文件还是文件夹，是文件夹则删除
			if(tempFile.isDirectory())
			{
				clearDirectory(zipDataPath + "/" + tempFile.getName());
				tempFile.delete();
				log.info("文件夹：" + tempFile.getName() + "无效，已删除。");
				continue;
			}
			//解压文件
			if(!tempFile.getName().endsWith(".zip"))
			{
				moveFile2BackupDirectory(tempFile, false);
				log.info("上报文件：" + tempFile.getName() + "不是ZIP文件，已转移到备份文件夹。");
				continue;
			}
			try
			{
				String zipFilePath = zipDataPath + "/" + tempFile.getName();
				log.info("正在解压文件：" + tempFile.getName() + "...");

				try
				{
					Zip.unzip(zipFilePath, extractDataPath);
				}
				catch(IOException ex2)
				{
					if(ex2.getMessage().startsWith("资源正在被占用："))
					{
						log.info("资源正在被占用，稍后解压");
						continue;
					}
					else
					{
						throw new IOException(ex2.getMessage());
					}
				}

				log.info("解压完毕");
				//认证用户权限
				try
				{
					userInfo = getUserInfoHasPassedAuthentication();
				}
				catch(ReceiveException ex1)
				{
					moveFile2BackupDirectory(tempFile, false);
					log.error("导入文件失败，" + ex1.getMessage() + tempFile.getName() + "！", ex1);
					//导入失败，报告管理员
					sendMail("导入数据失败", "导入文件:" + tempFile.getName() + "失败，" + ex1.getMessage() + tempFile.getName() + "！");
					throw new ReceiveException(ex1);
				}
				UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
				User user = u.createUserManager().getUserByName(userInfo.getUsername());
				String taskID = userInfo.getTaskid();

				//为了保证上报数据 .xml 格式的，不重名，则解压一组导入一组
				importData(user, taskID);

				//修改名称并转移 .zip 文件
				moveFile2BackupDirectory(tempFile, true);
			}
			catch(FactoryException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				log.error("导入文件失败", ex);
				sendMail("导入数据失败", "导入文件:" + tempFile.getName() + "失败，" + ex.getMessage());
			}
			catch(UserManagerException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				if(userInfo != null)
				{
					log.error("导入文件失败，" + "上报的用户不存在,用户名：" + userInfo.getUsername(), ex);
					sendMail("导入数据失败", "导入文件:" + tempFile.getName() + "失败，" + "上报的用户不存在,用户名：" + userInfo.getUsername());
				}
				else
				{
					log.error("导入文件失败", ex);
					sendMail("导入数据失败", "导入文件:" + tempFile.getName() + "失败，" + ex.getMessage());
				}
			}
			catch(ReceiveException ex)
			{
				moveFile2BackupDirectory(tempFile, false);
				log.error("导入文件失败", ex);
				sendMail("导入数据失败", "导入文件:" + tempFile.getName() + "失败，" + ex.getMessage());
			}
			catch(IOException ex)
			{
				log.error("导入文件失败，" + "解压数据文件失败！ 文件名：" + tempFile.getName(), ex);
				sendMail("导入文件失败", "导入文件:" + tempFile.getName() + "失败，" + "解压数据文件失败！ 文件名：" + tempFile.getName() + "  " + ex.getMessage());
				moveFile2BackupDirectory(tempFile, false);
			}
			catch(Exception ex)
			{
				log.error("导入文件失败！" + " 文件名：" + tempFile.getName(), ex);
				sendMail("导入文件失败", "导入文件:" + tempFile.getName() + "失败！" + " 文件名：" + tempFile.getName() + "  " + ex.getMessage());
				moveFile2BackupDirectory(tempFile, false);
			}
			finally
			{
				//清除所有 xml 文件
				clearDirectory(extractDataPath);
			}
		}
		log.info("数据处理完毕\r\n\r\n");
	}

	/**
	 * 按最后修改时间排序
	 * @param files 文件列表
	 * @return 排序过的文件列表
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
	 * 认证用户信息
	 * @return 用户信息对象
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
			log.info("认证用户：" + userName + "的权限...");
			AuthEnvironment aE = new AuthEnvironment(userName, password);
			Authentication at = ((AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.getName())).getAuthentication(aE);

			if(!at.authenticate())
			{
				String message = "用户：" + userName + "认证失败，不能装入数据";
				log.info(message);
				throw new ReceiveException(message);
			}
			log.info("用户通过认证，可以上报数据，准备装入数据...");
		}
		catch(ReceiveException ex)
		{
			throw ex;
		}
		catch(Exception ex)
		{
			String message = "不能得到用户认证信息";
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
	 * 将解压目录下的数据文件导入数据库
	 * @param user 用户
	 * @param taskID 任务ID
	 * @throws ReceiveException
	 */
	private void importData(User user, String taskID)
		throws ReceiveException
	{
		try
		{
			ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
			UnitACL unitACL = modelManager.getUnitACL(user);

			log.info("开始导入数据文件...");
			File f = new File(extractDataPath);
			File[] files = f.listFiles();
			for(int i = 0; i < files.length; i++)
			{
				importDataFile(files[i], modelManager, unitACL);
			}
			log.info("导入完毕");
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
	 * 导入指定的数据文件
	 * @param file 数据文件
	 * @param modelManager ModelManager对象
	 * @param unitACL UnitACL对象
	 * @throws ReceiveException
	 */
	private void importDataFile(File file, ModelManager modelManager, UnitACL unitACL)
		throws ReceiveException
	{
		FileInputStream fileInputStream = null;
		try
		{
			fileInputStream = new FileInputStream(file);

			//上报数据
			LoadResult loadResult = modelManager.getDataImporter().importData(fileInputStream, unitACL);

			//记录上报日志
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logLoadDataEvent(loadResult, unitACL.getUser(), "", LogManager.MAIL_MODE);

			if(loadResult.getAuditFailedUnitIDs().hasNext())
			{
				// 审核没有通过
				sendMail("存在未审核通过的数据", loadResult.getMessage());
			}

			if(loadResult.getOverdueUnitIDs().hasNext())
			{
				//将数据文件移到过期文件夹
				String fileName = file.getName();
				int i = fileName.indexOf(".xml");
				String outOfTimeLimitFileName = fileName.substring(0, i) + "_YTAPL_" + System.currentTimeMillis() + fileName.substring(i);
				File newFile = new File(backupDataPath + "/" + outOfTimeLimitFileName);
				file.renameTo(newFile);
				log.error("用户：" + unitACL.getUser().getName() + "上报数据过期，过期文件已转移到过期文件夹，文件名：" + outOfTimeLimitFileName);

				sendMail("导入数据失败", "用户：" + unitACL.getUser().getName() + "上报数据过期，过期文件已转移到过期文件夹，文件名：" + outOfTimeLimitFileName);
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
	 * 判断当前时间是否在允许填报时间之外，位于允许时间之外返回 true ，否则返回false
	 * @param taskTime 任务时间对象
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
	 * 得到脚本的内容
	 * @param scripts 脚本Script集合
	 * @return 脚本内容String集合
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
	 * 得到审核结果的错误信息
	 * @param auditResult 审核结果
	 * @return 审核结果的错误信息
	 */
	private String getErrorMessage(AuditResult auditResult)
	{
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("错误数" + errors.size() + ":\n");
		for(int i = 0; i < errors.size(); i++)
		{
			errorMessage.append("（" + (i + 1) + "）" + errors.get(i) + "\n");
		}
		return errorMessage.toString();
	}

	/**
	 * 得到审核结果的警告信息
	 * @param auditResult 审核结果
	 * @return 审核结果的警告信息
	 */
	private String getWarningMessage(AuditResult auditResult)
	{
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("警告数" + warnings.size() + ":\n");
		for(int i = 0; i < warnings.size(); i++)
		{
			warningMessage.append("（" + (i + 1) + "）" + warnings.get(i) + "\n");
		}
		return warningMessage.toString();
	}

	/**
	 * 清空定的文件夹(递归)
	 * @param path 路径　
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
	 * 修改名称并转移 .zip 文件
	 * @param file 要转移的文件对象
	 * @param hasReport 是否能上报，能上报的文件　和　不能上报的文件　分开存放
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
	 * 返回存放 Zip　文件的路径
	 * @return 路径
	 */
	public String getZipPath()
	{
		return zipDataPath;
	}

	/**
	 * 返回存放 xml 文件的路径
	 * @return 路径
	 */
	public String getExtractPath()
	{
		return extractDataPath;
	}

	/**
	 * 返回存放 备份 文件的路径
	 * @return 路径
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
