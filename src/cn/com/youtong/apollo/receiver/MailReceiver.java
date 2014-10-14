package cn.com.youtong.apollo.receiver;

import cn.com.youtong.apollo.common.mail.*;
import java.util.*;
import java.io.*;
import cn.com.youtong.apollo.services.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class MailReceiver
{
	//保存一个静态对象，保证多线程时不重复调用
	private static MailReceiver mailReceiver;
	//参数
	private String[] popServerNames;
	private String[] popUserNames;
	private String[] popPasswords;

	private Log log = LogFactory.getLog(this.getClass());

	//当前是否在执行导入的任务
	private boolean isBusy;

	private MailReceiver()
	{
		popServerNames = Config.getStringArray("mail.pop3.host");
		popUserNames = Config.getStringArray("mail.pop3.user");
		popPasswords = Config.getStringArray("mail.pop3.password");
//		popServerName = "pop.21cn.com pop.21cn.com";
//		popUserName = "apollotest apollo001";
//		popPassword = "apollotest apollo001";
	}

	public static MailReceiver getInstance()
	{
		if(mailReceiver == null)
		{
			mailReceiver = new MailReceiver();
		}
		return mailReceiver;
	}

	public String[] getPOPServerNames()
	{
		return popServerNames;
	}

	public String[] getPOPUserNames()
	{
		return popUserNames;
	}

	public String[] getPOPPasswords()
	{
		return popPasswords;
	}

	public void setPOPServerName(String[] popServerNames)
	{
		this.popServerNames = popServerNames;
	}

	public void setPOPUserName(String[] popUserNames)
	{
		this.popUserNames = popUserNames;
	}

	public void setPOPPassword(String[] popPasswords)
	{
		this.popPasswords = popPasswords;
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
			log.info("开始接收邮件...");
			String zipDataPath = Config.getString("cn.com.youtong.apollo.zipdata.directory");
//			String zipDataPath = "d:/zip";

			for(int i = 0; i < popServerNames.length; i++)
			{
				String tempServerName = popServerNames[i];
				String tempUserName = popUserNames[i];
				String tempPassword = popPasswords[i];
				doReceive(tempServerName, tempUserName, tempPassword, zipDataPath);
			}
		}
		catch(ReceiveException ex1)
		{
			log.error("", ex1);
		}
		finally
		{
			log.info("数据处理完毕，结果请看日志\r\n\r\n");
			setBusy(false);
		}
	}

	private void doReceive(String serverName, String user, String password, String outPutPath)
		throws ReceiveException
	{
		if(serverName == null || serverName.equals(""))
		{
			log.error("POP3邮箱配置信息不正确，请检查！");
			return;
		}
		log.info("在服务器：" + serverName + "上接收邮件..");
		Vector vector = cn.com.youtong.apollo.common.mail.MailReceiver.receive(serverName, user, password,Config.getString("mail.pop3.datafile.postfix"));
		log.info("共收到" + vector.size() + "封邮件");

		int count = 0;

		MailBean[] mailBeans = new MailBean[vector.size()];
		mailBeans = (MailBean[])vector.toArray(mailBeans);
		mailBeans = sortMailBySendDate(mailBeans);

		for(int j=0;j<mailBeans.length;j++)
		{
			MailBean mailBean = mailBeans[j];
			String subject = mailBean.getSubject();
			String postFix = Config.getString("mail.pop3.datafile.postfix");
			if(postFix == null || postFix.equals("") || subject.endsWith(postFix))
			{
				count++;
				Attachment[] attachments = mailBean.getAttachments();
				if (attachments != null)
				{
					for(int i = 0; i < attachments.length; i++)
					{
						Attachment attachment = attachments[i];
						int index = attachment.getFileName().lastIndexOf(".");
						String outPutName = attachment.getFileName().substring(0, index) + "_From_" + serverName + attachment.getFileName().substring(index);
						File file = new File(outPutPath + "/" + outPutName);
						try
						{
							FileOutputStream fileOutputStream = new FileOutputStream(file);
							fileOutputStream.write(attachment.getFileContent());
						}
						catch(Exception ex)
						{
							throw new ReceiveException("文件接收失败", ex);
						}
					}
				}
			}
		}
		log.info("在服务器：" + serverName + "上接收邮件接收完毕，其中有" + count + "份数据邮件");
	}


	/**
	 * 按最后修改时间排序
	 * @param mails 文件列表
	 * @return 排序过的文件列表
	 */
	private MailBean[] sortMailBySendDate(MailBean[] mails)
	{
		MailBean tempMail;
		int length = mails.length;

		for(int i=0;i<length-1;i++)
		{
			Date d1 = mails[i].getSendDate();
			int position = i;
			for(int j=i+1;j<length;j++)
			{
				Date d2 = mails[j].getSendDate();
				if(d2.before(d1))
				{
					d1 = d2;
					position = j;
				}
			}
			if(position > i)
			{
				tempMail = mails[i];
				mails[i] = mails[position];
				mails[position] = tempMail;
			}
		}
		return mails;
	}



	private synchronized void setBusy(boolean isBusy)
	{
		this.isBusy = isBusy;
	}



	public static void main(String[] args)
	{
		MailReceiver m = MailReceiver.getInstance();
		m.receive();
	}
}
