package cn.com.youtong.apollo.receiver;

import cn.com.youtong.apollo.receiver.*;
import org.apache.commons.logging.*;
//import sun.net.ftp.*;
//import sun.net.*;
import cn.com.youtong.apollo.services.*;
import java.io.*;
import java.util.*;
import org.apache.commons.net.ftp.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class FTPReceiver
{
	private static FTPReceiver ftpReceiver;
	private Log log = LogFactory.getLog(this.getClass());
	private String[] ftpServerNames;
	private String[] ftpPaths;
	private String[] ftpUserNames;
	private String[] ftpPasswords;

	//当前是否在执行导入的任务
	private boolean isBusy;

	public FTPReceiver()
	{
		ftpServerNames = Config.getStringArray("cn.com.youtong.ftp.host");
		ftpUserNames = Config.getStringArray("cn.com.youtong.ftp.user");
		ftpPasswords = Config.getStringArray("cn.com.youtong.ftp.password");
//		ftpServerName = "127.0.0.1 jbwang";
//		ftpUserName = "user2 mk";
//		ftpPassword = "password mk";
	}

	public static FTPReceiver getInstance()
	{
		if(ftpReceiver == null)
		{
			ftpReceiver = new FTPReceiver();
		}
		return ftpReceiver;
	}

	public String[] getFTPServerName()
	{
		return ftpServerNames;
	}

	public String[] getFTPUserName()
	{
		return ftpUserNames;
	}

	public String[] getFTPPassword()
	{
		return ftpPasswords;
	}

	public void setFTPPassword(String[] ftpPasswords)
	{
		this.ftpPasswords = ftpPasswords;
	}

	public void setFTPServerName(String[] ftpServerNames)
	{
		this.ftpServerNames = ftpServerNames;
	}

	public void setFTPUserName(String[] ftpUserNames)
	{
		this.ftpUserNames = ftpUserNames;
	}

	public void receive()
	{
		if(isBusy)
		{
			return;
		}
		setBusy(true);
		try
		{
			log.info("开始连接FTP服务器...");
			String zipDataPath = Config.getString("cn.com.youtong.apollo.zipdata.directory");
//			String zipDataPath = "d:/zip";

			for(int j = 0; j < ftpServerNames.length; j++)
			{
				String tempServerName = ftpServerNames[j];
				String tempUserName = ftpUserNames[j];
				String tempPassword = ftpPasswords[j];
				doReceive(tempServerName, tempUserName, tempPassword, zipDataPath);
			}
		}
		catch(IOException ex)
		{
			log.error(ex);
		}
		finally
		{
			log.info("数据处理完毕，结果请看日志\r\n\r\n");
			setBusy(false);
		}
	}

	private void doReceive(String serverName, String loginName, String loginPass, String localPath)
		throws IOException
	{
		FTPClient ftpClient = new FTPClient();
		int reply;
		if(serverName == null || serverName.equals(""))
		{
			log.error("FTP服务器配置信息不正确，请检查！");
			return;
		}
		log.info("连接服务器：" + serverName);
		try
		{
			ftpClient.connect(serverName);
		}
		catch(IOException ex)
		{
			log.error("连接失败");
			return;
		}
		reply = ftpClient.getReplyCode();
		if(!FTPReply.isPositiveCompletion(reply))
		{
			ftpClient.disconnect();
			log.error("连接失败");
			return;
		}
		log.info("连接完毕，正在登陆...");

		if(!ftpClient.login(loginName, loginPass))
		{
			ftpClient.disconnect();
			log.error("登陆失败，用户名或密码不正确");
			return;
		}

		log.info("登陆成功");
//		if(ftpPaths != null && ftpPaths.length != 0)
//		{
//			ftpClient.changeWorkingDirectory(ftpPaths);
//		}
		FTPFile[] ftpFiles = ftpClient.listFiles();

		if(ftpFiles == null)
		{
			log.info("没有文件可供下载，下载结束，断开连接!\n");
			ftpClient.disconnect();
			return;
		}
		log.info("开始下载文件...");

		ftpFiles = sortFileByModifyDate(ftpFiles);
		int count=0;
		for(int i = 0; i < ftpFiles.length; i++)
		{
			String tempFileName = ftpFiles[i].getName();
			if(tempFileName.indexOf(".zip")==-1)
			{
				continue;
			}
			count++;
			log.info("下载文件:" + ftpFiles[i].getName());
			FileOutputStream os = null;
			try
			{
				int index = tempFileName.lastIndexOf(".");
				String outPutName = tempFileName.substring(0, index) + "_From_" + serverName + tempFileName.substring(index);
				java.io.File file = new File(localPath + "/" + outPutName);
				os = new FileOutputStream(file);
				ftpClient.retrieveFile(tempFileName, os);
				ftpClient.deleteFile(tempFileName);
			}
			catch(IOException ex1)
			{
				log.error("收取文件失败" + ex1.getMessage());
				return;
			}
			finally
			{
				os.close();
			}
		}
		log.info("在服务器 " + serverName + " 上下载了" + count + "个数据文件");
		log.info("下载结束，断开连接!");
		ftpClient.disconnect();
	}

	/**
	 * 按最后修改时间排序
	 * @param mails 文件列表
	 * @return 排序过的文件列表
	 */
	private FTPFile[] sortFileByModifyDate(FTPFile[] files)
	{
		FTPFile tempMail;
		int length = files.length;

		for(int i=0;i<length-1;i++)
		{
			Date d1 = files[i].getTimestamp().getTime();
			int position = i;
			for(int j=i+1;j<length;j++)
			{
				Date d2 = files[j].getTimestamp().getTime();
				if(d2.before(d1))
				{
					d1 = d2;
					position = j;
				}
			}
			if(position > i)
			{
				tempMail = files[i];
				files[i] = files[position];
				files[position] = tempMail;
			}
		}
		return files;
	}


	private synchronized void setBusy(boolean isBusy)
	{
		this.isBusy = isBusy;
	}

	public static void main(String[] args)
	{
		FTPReceiver.getInstance().receive();
	}
}
