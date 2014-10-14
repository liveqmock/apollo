/*
 * Created on 2003-11-24
 */
package cn.com.youtong.apollo.notify;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.common.Queue;
import cn.com.youtong.apollo.common.mail.*;

/**
 * �ʼ��߱�
 *
 * <p>
 * �ʼ��߱�����MessageӦ���������¹���
 * �߱��ʼ���Ŀ�ĵ�ַ��Ӧһ���߱�����(MailBean����)��
 * ͬʱ��һ��Message�������Ӻܶ��������ֵ�ԡ�
 *
 * <p>
 * Message��HashMap��
 * key=�ʼ�Ŀ�ĵ�ַ,value=�ʼ�����
 *
 *
 * @author wjb
 */
public class EmailNotifier
	implements Notifier
{
	private Log log = LogFactory.getLog(this.getClass());
	//private Message _msg;
	private Queue queue;
	private MainLoop mainLoop;
	private Thread thread;

	public EmailNotifier()
	{
		mainLoop = new MainLoop();
		thread = null;
		queue = new Queue();
		loadStorge();
		cleanStorge();
	}

	/**
	 * ��ȡ�����е���Ϣ
	 */
	private void loadStorge()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try
		{
			fis = new FileInputStream(Config.getString("email.outputfile"));
			if(fis.available() == 0)
			{
				return;
			}

			ois = new ObjectInputStream(fis);
			HashMap props = new HashMap();
			try
			{
				while(true)
				{
					String to = (String) ois.readObject();
					props.put(to, ois.readObject());
				}
			}
			catch(IOException e)
			{}
			catch(ClassNotFoundException cnfe)
			{}

			Message msg = new Message(props);
			queue.add(msg);
		}
		catch(Exception ex)
		{
			log.error("", ex);
		}
		finally
		{
			try
			{
				if(ois != null)
				{
					ois.close();
				}
				if(fis != null)
				{
					fis.close();
				}
			}
			catch(IOException e)
			{}
		}

		restart();
	}

	private void cleanStorge()
	{
		try
		{
			String mailOutputPath = Config.getString("email.outputfile");
			File mail = new File(mailOutputPath);
			mail.delete();
			mail.createNewFile(); //����������ļ�����ô���������������ô���Ḳ�ǵ�
		}
		catch(IOException ex)
		{
			log.error("��ջ�����ʼ���Ϣʧ��");
		}
	}
	/**
	 * Start (or restart) a thread to process commands, or wake up an
	 * existing thread if one is already running.  This method can be
	 * invoked if the background thread crashed due to an
	 * unrecoverable exception in an executed command.
	 */
	public synchronized void restart()
	{
		if(thread == null)
		{
			// Create the the housekeeping thread of the scheduler. It will wait
			// for the time when the next task needs to be started, and then
			// launch a worker thread to execute the task.
			thread = new Thread(mainLoop);
			thread.start();
		}
		else
		{
			notify();
		}
	}

	/**
	 * @param msg
	 * @throws NotifyException
	 *
	 * @see cn.com.youtong.apollo.notify.Notifier#pushMessage(cn.com.youtong.apollo.notify.Message)
	 */
	public void pushMessage(Message msg)
		throws NotifyException
	{
		//_msg = msg;
		queue.add(msg);
		restart();
	}

	/**
	 * �ڷ������رյ�����£�Ҫ�ȹرմ߱�����
	 * �߱�����رյ�ʱ�򣬿���Message�ﻹ����Ϣ��û�з��͡�
	 * ����Ҫ������ЩMessage���Ա��´�������ʱ�򣬼����߱���
	 *
	 * @see cn.com.youtong.apollo.notify.Notifier#close()
	 */
	public synchronized void close()
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try
		{
			fos = new FileOutputStream(Config.getString("email.outputfile"));
			oos = new ObjectOutputStream(fos);

			// ������Ϣ
			while(queue.getNext() != null)
			{
				Message msg = (Message) queue.getNext();
				Set keys = msg._props.keySet();

				for(Iterator keyIter = keys.iterator(); keyIter.hasNext(); )
				{
					String to = (String) keyIter.next();
					oos.writeObject(to);
					oos.writeObject((MailBean) msg.getProperty(to));
				}
			}
		}
		catch(Exception ex)
		{
			log.error("", ex);
		}
		finally
		{
			try
			{
				oos.close();
				fos.close();
			}
			catch(IOException e)
			{}
		}
		// interrupte thread
		if(thread != null)
		{
			thread.interrupt();
		}
	}

	/**
	 *  Return the next object to execute, or null if thread is
	 *  interrupted.
	 *
	 * @return A Object.
	 * @exception Exception, a generic exception.
	 */
	private synchronized Object next()
		throws Exception
	{
		try
		{
			while(!Thread.interrupted())
			{
				// Grab the next object off the queue.
				Object obj = queue.getNext();
				if(obj == null)
				{
					// Queue must be empty. Wait on it.
					wait();
				}
				else
				{
					return obj;
				}
			}
		}
		catch(InterruptedException ex)
		{
		}
		// On interrupt.
		return null;
	}

	/**
	 * Inner class.  This is isolated in its own Runnable class just
	 * so that the main class need not implement Runnable, which would
	 * allow others to directly invoke run, which is not supported.
	 */
	protected class MainLoop
		implements Runnable
	{
		/**
		 * Method to run the class.
		 */
		public void run()
		{
			log.debug("MainLoop running....");
			try
			{
				for(; ; )
				{
					Object obj = next();
					Message msg = (Message) obj;
					if(msg != null)
					{
						sendMails(msg);
						queue.remove(msg);
					}
					else
					{
						break;
					}
				}
			}
			catch(Exception e)
			{
				log.error("Error running a queue notify email: " + e);
			}
		}

		private void sendMails(Message msg)
		{
			Set keys = msg._props.keySet();

			for(Iterator keyIter = keys.iterator(); keyIter.hasNext(); )
			{
				String to = (String) keyIter.next();
				Object obj = msg.getProperty(to);

				if(!(obj instanceof MailBean))
				{
					return;
				}

				MailBean mail = (MailBean) obj;
				if(log.isDebugEnabled())
				{
					log.debug("Send mail to " + mail.getTo() + "; From " + mail.getFrom() + "; Subject " + mail.getSubject());
				}
				Mail.send(mail.getFrom(), mail.getTo(), "", "", mail.getSubject(), mail.getBodyText(), false);
			}

			msg._props.clear();
		}
	}
}
