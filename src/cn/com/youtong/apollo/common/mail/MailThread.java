/*
 * Created on 2003-10-10
 */
package cn.com.youtong.apollo.common.mail;

import org.apache.commons.logging.*;

/**
 * E-mail thread. Sending e-mail is slow, run it in threadpool is much more efficient.
 */
class MailThread
	implements Runnable
{
	private Log log = LogFactory.getLog(this.getClass());
	private String from = null;
	private String to = null;
	private String subject = null;
	private String cc = null;
	private String bcc = null;
	private String body = null;
	private boolean isHTML = false;
	private String attach = null;

	MailThread(String from, String to, String cc, String bcc, String subject, String body, boolean isHTML)
	{
		log.debug("Construct MailThread");
		
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.cc = cc;
		this.bcc = bcc;
		this.body = body;
		this.isHTML = isHTML;
	}

	MailThread(String from, String to, String cc, String bcc, String subject, String body, String htmlAttach)
	{
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.cc = cc;
		this.bcc = bcc;
		this.body = body;
		this.attach = htmlAttach;
	}

	public void run()
	{	
		log.debug("MailThread Running");
		
		try
		{
			if(attach == null)
			{
				MailDelivery.sendMessage(from, to, cc, bcc, subject, body, isHTML);
			}
			else
			{
				MailDelivery.sendMultipartMsg(from, to, cc, bcc, subject, body, attach);
			}
		}
		catch(Exception e)
		{
			log.error("Send message exception: " + e.getMessage());
		}
	}
}