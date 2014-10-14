package cn.com.youtong.apollo.common.mail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.services.Config;
import org.apache.commons.logging.*;

class UserPasswordAuthenticator extends Authenticator
{
	private PasswordAuthentication auth;

	public UserPasswordAuthenticator(String user, String password)
	{
		auth = new PasswordAuthentication(user, password);
	}

	public PasswordAuthentication getPasswordAuthentication()
	{
		return auth;
	}
}

/**
 * ·¢ËÍÓÊ¼þ
 */
class MailDelivery
{
	private static Log log = LogFactory.getLog(MailDelivery.class);
	static UserPasswordAuthenticator auth;
	
	static Session session;

	static
	{
		Properties props = System.getProperties();

		props.put("mail.smtp.host", Config.getString("mail.smtp.host"));
		props.put("mail.smtp.auth", Config.getString("mail.smtp.auth", "true"));
		props.put("mail.debug", Config.getString("mail.debug", "false"));

		String user = Config.getString("mail.smtp.user");
		String password = Config.getString("mail.smtp.password");
		auth = new UserPasswordAuthenticator(user, password);
		
		session = Session.getInstance(System.getProperties(), auth);
	}

	String getFromString(Address[] addresses)
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

	static void postMessage(Message msg)
	{
		try
		{
			log.debug("send mail...");			
			Transport t = session.getTransport("smtp");
			t.connect() ;
			t.sendMessage(msg, msg.getAllRecipients() );
		}
		catch(Exception e)
		{
			log.error("", e);
		}
	}

	/**
	 * Send a e-mail.
	 * @param from (required) e-mail from field
	 * @param to   (required) e-mail to field
	 * @param cc    cc field
	 * @param bcc   bcc field
	 * @param body  mail text
	 * @param isHTML whether to send e-mail in HTML format.
	 */
	public static void sendMessage(String from, String to, String cc, String bcc, String subject, String body, boolean isHTML)
		throws MessagingException
	{

		Message msg = new MimeMessage((Session) null);
		InternetAddress[] address;

		if(Util.isEmptyString(from) == false)
		{
			address = InternetAddress.parse(from, false);
			msg.setFrom(new InternetAddress(from));
		}

		if(Util.isEmptyString(to) == false)
		{
			address = InternetAddress.parse(to, false);
			msg.setRecipients(Message.RecipientType.TO, address);
		}
		else
		{
			throw new MessagingException("No \"To\" address specified");
		}

		if(Util.isEmptyString(cc) == false)
		{
			address = InternetAddress.parse(cc, false);
			msg.setRecipients(Message.RecipientType.CC, address);
		}
		if(Util.isEmptyString(bcc) == false)
		{
			address = InternetAddress.parse(bcc, false);
			msg.setRecipients(Message.RecipientType.BCC, address);
		}

		if(Util.isEmptyString(subject) == false)
		{
			msg.setSubject(subject);
		}
		if(isHTML == false)
		{
			msg.setText(body);
		}
		else
		{
			msg.setDataHandler(new DataHandler(new ByteArrayDataSource(body, "text/html")));
		}

		postMessage(msg);

		//YtLogger.debug(YtLogUser.SYSTEM, YtAction.SEND_MAIL, "Send mail finished!");
	}

	/**
	 * Send single attachment through mupltipart mime message.
	 * Message is composed in two part. Part I is normal html mail,
	 * Part II is html attachment.
	 * @param from (required) e-mail from field
	 * @param to   (required) e-mail to field
	 * @param cc    cc field
	 * @param bcc   bcc field
	 * @param body  mail text
	 * @param htmlAttach  attachment
	 */
	public static void sendMultipartMsg(String from, String to, String cc, String bcc, String subject, String body, String htmlAttach)
		throws MessagingException
	{

		MimeMessage message = new MimeMessage((Session) null);
		InternetAddress[] address;

		if(Util.isEmptyString(subject) == false)
		{
			message.setSubject(subject);
		}

		if(Util.isEmptyString(to) == false)
		{
			address = InternetAddress.parse(to, false);
			message.setRecipients(Message.RecipientType.TO, address);
		}
		else
		{
			throw new MessagingException("No \"To\" address specified");
		}

		if(Util.isEmptyString(from) == false)
		{
			message.setFrom(new InternetAddress(from));
		}

		if(Util.isEmptyString(cc) == false)
		{
			address = InternetAddress.parse(cc, false);
			message.setRecipients(Message.RecipientType.CC, address);
		}

		if(Util.isEmptyString(bcc) == false)
		{
			address = InternetAddress.parse(bcc, false);
			message.setRecipients(Message.RecipientType.BCC, address);
		}

		message.setSentDate(Calendar.getInstance().getTime());

		MimeMultipart multipart = new MimeMultipart();

		MimeBodyPart bodyPart = new MimeBodyPart();

		bodyPart.setText(body);
		multipart.addBodyPart(bodyPart);

		MimeBodyPart bodyPart2 = new MimeBodyPart();

		FileDataSource fds = new FileDataSource(htmlAttach);
		bodyPart2.setDataHandler(new DataHandler(fds));

		try
		{
			bodyPart2.setFileName(MimeUtility.encodeWord(fds.getName(), "GB2312", null));
		}
		catch(java.io.UnsupportedEncodingException uee)
		{
			throw new MessagingException(uee.getMessage());
		}

		multipart.addBodyPart(bodyPart2);

		message.setContent(multipart);
		postMessage(message);

	}
}
