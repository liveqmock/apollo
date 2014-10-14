package cn.com.youtong.apollo.common.mail;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.common.*;

/**
 * This class provides service to send e-mail out. It is asynchronies class.
 * It adds e-mail message to threading pool and return to caller
 * immediately. E-mail can be either in html or plain/text
 * format.
 *
 */
public class Mail
{
	private static ThreadPool _pool = null;
	private static Log log = LogFactory.getLog(Mail.class);

	// Initilize thread pool with min and max size from config
	static
	{
		//cn.com.youtong.apollo.MockService.setup();
		_pool = new ThreadPool(Config.getInt("min.mail.size"), Config.getInt("max.mail.size"));
	}

	/**
	 * Send a e-mail.
	 * @param from (required) e-mail from field
	 * @param to   (required) e-mail to field
	 * @param cc    cc field
	 * @param bcc   bcc field
	 * @param subject subject field
	 * @param body  mail text
	 * @param isHTML whether to send e-mail in HTML format.
	 */
	public static void send(String from, String to, String cc, String bcc, String subject, String body, boolean isHTML)
	{
		MailThread sh = new MailThread(from, to, cc, bcc, subject, body, isHTML);
		log.debug("Put in pool and execute");
		_pool.execute(sh);
	}

	/**
	 * Send a e-mail.
	 * @param from (required) e-mail from field
	 * @param to   (required) e-mail to field
	 * @param cc    cc field
	 * @param bcc   bcc field
	 * @param subject subject field
	 * @param body  mail text
	 * @param htmlAttach  attachment
	 */
	public static void send(String from, String to, String cc, String bcc, String subject, String body, String htmlAttach)
	{
		MailThread sh = new MailThread(from, to, cc, bcc, subject, body, htmlAttach);

		_pool.execute(sh);
	}

	public static void main(String[] args)
	{
		Mail.send("apollotest@21cn.com", "apollotest@21cn.com", null, null, "Hello from Javamail", "来电中文", false);
		//Mail.send("apollotest@21cn.com", "apollotest@21cn.com", null, null, "Hello from Javamail", "<a href=\"http://www.21cn.com\">Tests only</a>", true);
		//Mail.send("apollotest@21cn.com", "apollotest@21cn.com", null, null, "Hello from Javamail with attach", "Tests only", "F:/apollo/src/config/config.properties");
	}
}