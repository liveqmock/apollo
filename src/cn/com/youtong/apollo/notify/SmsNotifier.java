/*
 * Created on 2003-11-24
 */
package cn.com.youtong.apollo.notify;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.common.Queue;
import cn.com.youtong.apollo.common.sms.SmsSender;

/**
 * 短信催报
 *
 * <p>
 * 短信格式要求为key/value=对方接受者手机号/短信内容
 * @author wjb
 */
public class SmsNotifier
	implements Notifier
{
	private Log log = LogFactory.getLog(this.getClass());
	//private Message _msg;
	private Queue queue;
	private MainLoop mainLoop;
	private Thread thread;

	public SmsNotifier()
	{
		mainLoop = new MainLoop();
		thread = null;
		queue = new Queue();
		loadStorge();
		cleanStorge();
	}

	private void loadStorge()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try
		{
			fis = new FileInputStream(Config.getString("sms.outputfile"));
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
					String rcvNum = (String) ois.readObject();
					String content = (String) ois.readObject();
					props.put(rcvNum, content);
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
			String smslOutputPath = Config.getString("sms.outputfile");
			File mail = new File(smslOutputPath);
			mail.delete();
			mail.createNewFile(); //如果不存在文件，那么创建，如果存在那么不会覆盖的
		}
		catch(IOException ex)
		{
			log.error("清空缓存的短信信息失败");
		}
	}

	/**
	 * @see cn.com.youtong.apollo.notify.Notifier#pushMessage(cn.com.youtong.apollo.notify.Message)
	 */
	public void pushMessage(Message msg)
		throws NotifyException
	{
		queue.add(msg);
		restart();
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
	 * @see cn.com.youtong.apollo.notify.Notifier#close()
	 */
	public void close()
	{
		// TODO Auto-generated method stub
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
						sendSms(msg);
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
				log.error("Error running a queue notify sms: " + e);
			}
		}

		private void sendSms(Message msg)
		{
			Set rcvs = msg._props.keySet();
			for(Iterator iter = rcvs.iterator(); iter.hasNext(); )
			{
				String rcv = (String) iter.next();
				String content = (String) msg._props.get(rcv);
				new SmsSender().sendMsg(rcv, content);
			}

			msg._props.clear();
		}
	}
}
