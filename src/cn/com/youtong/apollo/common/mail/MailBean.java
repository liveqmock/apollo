/*
 * Created on 2003-10-14
 */
package cn.com.youtong.apollo.common.mail;

import java.io.*;
import java.util.*;

/**
 * MailBean 存储mail中属性
 * 包含发件人，收件人，发送日期，主题，主题内容，附件
 * @author wjb
 */
public class MailBean
	implements Serializable
{
	private String to;
	private String from;
	private String subject;
	private Date sendDate;
	private String bodyText;
	private Attachment[] attachments;

	public MailBean()
	{}

	public MailBean(String to)
	{
		this.to = to;
	}

	/**
	 * @return   邮件主题内容
	 */
	public String getBodyText()
	{
		return bodyText;
	}

	/**
	 * @return		发件人
	 */
	public String getFrom()
	{
		return from;
	}

	/**
	 * @return		发送日期
	 */
	public Date getSendDate()
	{
		return sendDate;
	}

	/**
	 * @return		邮件主题
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * 设置邮件主题内容
	 * @param string	主题内容
	 */
	public void setBodyText(String string)
	{
		bodyText = string;
	}

	/**
	 * 设置发件人
	 * @param string	发件人
	 */
	public void setFrom(String string)
	{
		from = string;
	}

	/**
	 * 设置发送日期
	 * @param date		发送日期
	 */
	public void setSendDate(Date date)
	{
		sendDate = date;
	}

	/**
	 * 设置邮件主题
	 * @param string	邮件主题
	 */
	public void setSubject(String string)
	{
		subject = string;
	}

	/**
	 * @return		收件人
	 */
	public String getTo()
	{
		return to;
	}

	/**
	 * 设置收件人
	 * @param string	收件人
	 */
	public void setTo(String string)
	{
		to = string;
	}

	/**
	 * @return		附件
	 */
	public Attachment[] getAttachments()
	{
		return attachments;
	}

	/**
	 * 设置附件
	 * @param attachments	附件
	 */
	public void setAttachments(Attachment[] attachments)
	{
		this.attachments = attachments;
	}

	private void readObject(java.io.ObjectInputStream os)
		throws IOException, ClassNotFoundException
	{
		to = (String) os.readObject();
		from = (String) os.readObject();
		subject = (String) os.readObject();
		sendDate = (Date) os.readObject();
		bodyText = (String) os.readObject();
	}

	private void writeObject(java.io.ObjectOutputStream os)
		throws IOException
	{
		os.writeObject(to);
		os.writeObject(from);
		os.writeObject(subject);
		os.writeObject(sendDate);
		os.writeObject(bodyText);

	}
}
