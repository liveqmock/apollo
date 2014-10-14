/*
 * Created on 2003-10-14
 */
package cn.com.youtong.apollo.common.mail;

import java.io.*;
import java.util.*;

/**
 * MailBean �洢mail������
 * ���������ˣ��ռ��ˣ��������ڣ����⣬�������ݣ�����
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
	 * @return   �ʼ���������
	 */
	public String getBodyText()
	{
		return bodyText;
	}

	/**
	 * @return		������
	 */
	public String getFrom()
	{
		return from;
	}

	/**
	 * @return		��������
	 */
	public Date getSendDate()
	{
		return sendDate;
	}

	/**
	 * @return		�ʼ�����
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * �����ʼ���������
	 * @param string	��������
	 */
	public void setBodyText(String string)
	{
		bodyText = string;
	}

	/**
	 * ���÷�����
	 * @param string	������
	 */
	public void setFrom(String string)
	{
		from = string;
	}

	/**
	 * ���÷�������
	 * @param date		��������
	 */
	public void setSendDate(Date date)
	{
		sendDate = date;
	}

	/**
	 * �����ʼ�����
	 * @param string	�ʼ�����
	 */
	public void setSubject(String string)
	{
		subject = string;
	}

	/**
	 * @return		�ռ���
	 */
	public String getTo()
	{
		return to;
	}

	/**
	 * �����ռ���
	 * @param string	�ռ���
	 */
	public void setTo(String string)
	{
		to = string;
	}

	/**
	 * @return		����
	 */
	public Attachment[] getAttachments()
	{
		return attachments;
	}

	/**
	 * ���ø���
	 * @param attachments	����
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
