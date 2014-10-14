/*
 * Created on 2003-10-14
 */
package cn.com.youtong.apollo.common.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;

import cn.com.youtong.apollo.services.Config;
import org.apache.commons.logging.*;

/**
 * 接收邮件
 * @author wjb
 */
public class MailReceiver
{
	private static Log log = LogFactory.getLog(MailReceiver.class);
	private static Properties props;
	static
	{
		props = System.getProperties();
	}

	/**
	 * 接收邮件
	 * @param username		用户名
	 * @param password		用户密码
	 * @return				邮件集合
	 */
	public static Vector receive(String serverName, String username, String password)
	{
		return receive(serverName, username, password, null);
	}

	/**
	 * 接指定后缀的邮件，接收完毕后删除
	 * @param serverName
	 * @param userName
	 * @param password
	 * @param subjectPostFix
	 * @return
	 */
	public static Vector receive(String serverName, String userName, String password, String subjectPostFix)
	{
		Vector mails = new Vector(); // Contains MainBean objects

		// Get Session
		Session session = Session.getDefaultInstance(props, null);
		Store store = null;
		Folder folder = null;

		try
		{
			//		Get store
			store = session.getStore("pop3");
			store.connect(serverName, userName, password);

			//		Get folder
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);

			//		Get directory
			Message message[] = folder.getMessages();

			for(int i = 0, n = message.length; i < n; i++)
			{
				Message msgTemp = message[i];

				// Construts MailBean
				MailBean bean = new MailBean(userName);

				String from = getFromString(msgTemp.getFrom());
				String subject = msgTemp.getSubject();
				if(subjectPostFix != null && !subjectPostFix.equals("") && !subject.endsWith(subjectPostFix))
				{
					continue;
				}
				Date sendDate = msgTemp.getSentDate();

				// Set MailBean properties
				bean.setFrom(from);
				bean.setSubject(subject);
				bean.setSendDate(sendDate);

				Object content = msgTemp.getContent();
				if(content instanceof MimeMultipart)
				{
					MimeMultipart bodys = (MimeMultipart) content;
					handleMultipart(bodys, bean);
				}
				else
				{
					// treat as text
					String bodyText = "";
					if(content instanceof java.lang.String)
					{
						log.debug("Get plain text");
						bodyText = content.toString();
					}
					else
					{
						/** ${todo} Text/plain content type has finished, but not for text/html*/
					}
					bean.setBodyText(bodyText);
				}
				msgTemp.setFlag(Flags.Flag.DELETED, true);
				mails.add(bean);
			}
		}
		catch(NoSuchProviderException nspe)
		{
			log.error(nspe.getMessage());
		}
		catch(MessagingException me)
		{

		}
		catch(IOException ioe)
		{
			log.error(ioe.getMessage());
		}
		finally
		{ //			Close connection
			try
			{
				if(folder != null)
				{
					folder.close(true);
				}
				if(store != null)
				{
					store.close();
				}
			}
			catch(MessagingException me)
			{
				log.error("Close connection error");
			}

		}

		return mails;
	}

	/**
	 * 抽取收件人地址
	 * @param addresses			收件人地址
	 * @return					收件人地址(String型)
	 */
	private static String getFromString(Address[] addresses)
	{
		String fromStr = "";
		if(addresses != null)
		{
			for(int i = 0; i < addresses.length; i++)
			{
				fromStr += addresses[i].toString();
			}
		}
		return fromStr;
	}

	public static void handleMultipart(Multipart multipart, MailBean bean)
		throws MessagingException, IOException
	{
		java.util.List list = new java.util.LinkedList();
		for(int i = 0, n = multipart.getCount(); i < n; i++)
		{
			Attachment attchment = handlePart(multipart.getBodyPart(i), bean);
			if(attchment != null)
			{
				list.add(attchment);
			}
		}

		Attachment[] attachments = new Attachment[list.size()];
		list.toArray(attachments);
		bean.setAttachments(attachments);
	}

	public static Attachment handlePart(Part part, MailBean bean)
		throws MessagingException, IOException
	{
		String disposition = part.getDisposition();
		String contentType = part.getContentType();
		if(disposition == null)
		{ // When just body
			if(log.isDebugEnabled())
			{
				log.debug("Null: " + contentType);
				// Check if plain
			}
			if((contentType.length() >= 10) && (contentType.toLowerCase().substring(0, 10).equals("text/plain")))
			{
				bean.setBodyText(part.getContent().toString());
			}
			else
			{ // Don't think this will happen
				if(log.isDebugEnabled())
				{
					log.debug("Other body: " + contentType);
				}
				bean.setBodyText(part.getContent().toString());
			}
		}
		else if(disposition.equalsIgnoreCase(Part.ATTACHMENT))
		{
			if(log.isDebugEnabled())
			{
				log.debug("Attachment: " + part.getFileName() + " : " + contentType);
			}
			return saveFile(part.getFileName(), part.getInputStream());
		}
		else if(disposition.equalsIgnoreCase(Part.INLINE))
		{
			if(log.isDebugEnabled())
			{
				log.debug("Inline: " + part.getFileName() + " : " + contentType);
			}
			return saveFile(part.getFileName(), part.getInputStream());
		}
		else
		{ // Should never happen
			if(log.isDebugEnabled())
			{
				log.debug("Other: " + disposition);
			}
		}

		return null;
	}

	public static Attachment saveFile(String filename, InputStream input)
		throws IOException
	{
		Attachment attachment = new Attachment();
		attachment.setFileName(filename);
		if(filename == null)
		{
			filename = File.createTempFile("xx", ".out").getName();
		}
		// Do no overwrite existing file
		File file = new File(filename);
		for(int i = 0; file.exists(); i++)
		{
			file = new File(filename + i);
		}
		//FileOutputStream fos = new FileOutputStream(file);
		//BufferedOutputStream bos = new BufferedOutputStream(fos);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);

		BufferedInputStream bis = new BufferedInputStream(input);
		try
		{
			int aByte;
			while((aByte = bis.read()) != -1)
			{
				bos.write(aByte);
			}
		}
		catch(IOException ex)
		{
		}
		finally
		{
			try
			{
				bos.flush();
			}
			catch(Exception ex)
			{}
			try
			{
				bos.close();
			}
			catch(Exception ex)
			{}
			try
			{
				bis.close();
			}
			catch(Exception ex)
			{}
		}

		attachment.setFileContent(baos.toByteArray());
		return attachment;
	}
}