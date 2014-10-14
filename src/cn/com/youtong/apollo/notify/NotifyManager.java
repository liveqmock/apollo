package cn.com.youtong.apollo.notify;

import java.sql.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.factory.FactoryException;
import org.apache.fulcrum.schedule.turbine.JobEntry;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import cn.com.youtong.apollo.address.*;
import cn.com.youtong.apollo.address.db.*;
import cn.com.youtong.apollo.address.db.form.*;
import cn.com.youtong.apollo.analyse.db.DBAnalyseHelper;
import cn.com.youtong.apollo.common.mail.MailBean;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.common.sql.NameGenerator;
import cn.com.youtong.apollo.data.ModelManager;
import cn.com.youtong.apollo.data.ModelManagerFactory;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.*;

/**
 * NotifyManager�������ļ���ȡ��Ҫ�߱�������Ȼ��鿴�Ƿ��˴߱����ڡ�
 * ������˴߱����ڣ��鿴��ǰ�Ƿ��˺��ʵĴ߱�ʱ�䣬������ˣ���ô���д߱�������ȴ���һʱ����
 *
 * <p>
 * <ul>
 *     <li>������Ҫ�߱�������</li>
 *     <li>���ô߱�������Ҫ�߱��ĵ�λdomain�еĵ�λ����(domain��˼���Բ鿴�����ļ�)</li>
 *     <li>�����ļ��������ã��Ƿ�ֻ�й����մ߱���ȱʡ��ֻ�й����մ߱���</li>
 *     <li>���ô߱�����ʼʱ��ͽ���ʱ�䡣�߱���ʼʱ��ͽ���ʱ�䣬ȱʡ������9:00��ʼ������17:00������</li>
 *     <li>���õ�һ��߱�������û��ȱʡֵ</li>
 *     <li>���ô߱����Ӵ�����ÿ�����ӵĴ߱���������Ȼ������ȱʡ��0</li>
 * </ul>
 *
 * <p>
 * �߱����򣺿�����ǰһ���߱������ǲ���һ���߱��������ܵĿ����߱�ʱ��㡣<br/>
 * �߱�ǰ������:�������������ʱ�����ȴ߱�����ʱ�����̡�<br/>
 *
 * ���磬10:10�д߱�����10:00�����˸ó��򣬶�����һ�������������ʱ��Ϊ10:40����ô���ھͽ��д߱���
 * �������������9:20������ó����ʱ������40���ӣ���ô��һ����ʱ����10:00����ô10:10�Ĵ߱�����
 * Ҫ�ȵ���һ�μ��ʼ�߱���
 *
 * <p>
 * ���˼��:<br/>
 * �������ļ���ȡ�߱���ʼʱ��ͽ���ʱ�䣬������������߱�������
 * �������ÿ�δ߱���ʱ��������ǰjob�������Ѹó���ļƻ������ʱ�䣬
 * ��������˴߱�����ʱ�䣬���߱���
 * ����ȴ߱���ʼʱ��Ҫ��߱�ʱ�����󣬲��߱���
 * ���򣬲��Ҵ߱�ʱ�䣬�ҵ���һ���ڵ�ǰ����ʱ�����Ĵ߱�ʱ�䣻
 * �Ƚϵ�ǰ����ʱ�����һ����ʱ�䣬�ֱ��ڲ��ҵ��ļƻ�����Ƚϣ������һ����ʱ����Ȼ�ڴ߱�����ǰ�棬
 * ��ô���߱�������һ����ʼ�߱���
 * ���������Ǵ߱������ʱ���ˡ�
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: YouTong</p>
 * @author wjb
 * @version 1.0
 */
public class NotifyManager
{
	private static NotifyManager instance = new NotifyManager();
	private Log log = LogFactory.getLog(NotifyManager.class);
	private ResourceBundle bundle;
	private NotifyManager()
	{
		bundle = ResourceBundle.getBundle("cn.com.youtong.apollo.notify.notify");
	}

	public static NotifyManager instance()
	{
		return instance;
	}

	/**
	 * �߱����õ�����͵�λ
	 * @param job ��ǰ��������
	 * @throws NotifyException
	 */
	public void notifyTasks(JobEntry job)
		throws NotifyException
	{
		// ��ȡҪ�߱�������ID
		String[] taskIDs = Config.getStringArray("cn.com.youtong.apollo.notify.task");
		if(taskIDs == null || taskIDs.length == 0)
		{
			return;
		}

		for(int i = 0; i < taskIDs.length; i++)
		{
			String taskID = taskIDs[i];
			checkAndNotifyTask(job, taskID);
		}
	}

	/**
	 * �������񣬲鿴�������Ƿ��˴߱�ʱ���ˣ�������ˣ��߱������򣬷���
	 * �������ֻ�й����մ߱�����ô�ǹ�����Ҳ���أ������д߱���
	 * �������ļ����õ�һ��߱����߱��������Ժ�ÿ�����Ӵ�������������Ȼ������ȱʡ��0
	 *
	 * @param job  ��ǰ��������
	 * @param taskID  ����ID
	 * @throws NotifyException
	 */
	private void checkAndNotifyTask(JobEntry job, String taskID)
		throws NotifyException
	{
		Calendar cldr = new GregorianCalendar();
		boolean onlyWorkday = Config.getBoolean("com.youtong.apollo.notify.workday.only", true);
		if(onlyWorkday)
		{
			// �ǹ����գ������д߱�
			int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
			{
				return;
			}
		}

		TaskManagerFactory taskMngFcty = null;
		TaskManager taskMng = null;
		String metaTableID = null;
		int taskTimeID;
		ModelManagerFactory modelMngFcty = null;
		ModelManager modelMng = null;
		Task task = null;
		try
		{
			taskMngFcty = (TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName());

			taskMng = taskMngFcty.createTaskManager();
			task = taskMng.getTaskByID(taskID);

			UnitMetaTable umt = task.getUnitMetaTable();
			metaTableID = umt.id();
			java.util.Date now = cldr.getTime();
			TaskTime time = task.getTaskTime(now);
			if(time == null)
			{
				// û�ж�Ӧ����ʱ�䷵��
				return;
			}

			java.util.Date attentionFromTime = time.getAttentionFromTime();
			java.util.Date attentionEndTime = time.getAttentionEndTime();

			if(!(now.before(attentionEndTime) && now.after(attentionFromTime)))
			{
				// ���ڴ߱�ʱ�䷶Χ�ڣ�����
				return;
			}

			long millSecondsOneDay = 86400000; // 60*60*24*1000=86400
			int days = (int) ((now.getTime() - attentionFromTime.getTime()) / (millSecondsOneDay));

			int base = Config.getInt("cn.com.youtong.apollo.notify.base");
			int increment = Config.getInt("cn.com.youtong.apollo.notify.increment", 0);

			int notifyTimes = base + days * increment; // Ҫ�߱��Ĵ���
			log.info("Ҫ�߱�����" + taskID + " ����" + notifyTimes);

			// �鿴��ǰ�Ƿ��ʺϴ߱�
			boolean suitForNotify = isSuitableForNotify(cldr, job, notifyTimes);
			if(suitForNotify)
			{
				if(log.isDebugEnabled())
				{
					StringBuffer sb = new StringBuffer("�����Ǵ߱������ʱ����");
					sb.append(" ����=").append(taskID).append(" ��ǰ����ʱ��Hour=");
					sb.append(job.getHour()).append(" Min=").append(job.getMinute());
					log.debug(sb.toString());
				}
				notifyTaskNow(task);
			}
		}
		catch(TaskException ex)
		{
			log.error("", ex);
			throw new NotifyException(ex);
		}
		catch(FactoryException fe)
		{
			log.error("", fe);
			throw new NotifyException(fe);
		}
		catch(Exception ex)
		{
			log.error("", ex);
			throw new NotifyException(ex);
		}
	}

	/**
	 * ��������ID taskID����ÿ��߱��Ĵ���notifyTimes��������Щ�ƻ�����
	 * �����������ļ������ã�ÿ��߱���ʼʱ��ͽ���ʱ�䣬ȱʡ������9:00��ʼ������17:00����
	 * <p>
	 * �߱����򣺿�����ǰһ���߱������ǲ���һ���߱��������ܵĿ����߱�ʱ��㡣<br/>
	 * �߱�ǰ������:�������������ʱ�����ȴ߱�����ʱ�����̡�<br/>
	 *
	 * ���磬10:10�д߱�����10:00�����˸ó��򣬶�����һ�������������ʱ��Ϊ10:40����ô���ھͽ��д߱���
	 * �������������9:20������ó����ʱ������40���ӣ���ô��һ����ʱ����10:00����ô10:10�Ĵ߱�����
	 * Ҫ�ȵ���һ�μ��ʼ�߱���
	 *
	 * <p>
	 * ���˼��:<br/>
	 * �������ļ���ȡ�߱���ʼʱ��ͽ���ʱ�䣬�����и����˴߱�������
	 * �������ÿ�δ߱���ʱ��������ǰjob�������Ѹó���ļƻ������ʱ�䣬
	 * ��������˴߱�����ʱ�䣬���߱���
	 * ����ȴ߱���ʼʱ��Ҫ��߱�ʱ�����󣬲��߱���
	 * ���򣬲��Ҵ߱�ʱ�䣬�ҵ���һ���ڵ�ǰ����ʱ�����Ĵ߱�ʱ�䣻
	 * �Ƚϵ�ǰ����ʱ�����һ����ʱ�䣬�ֱ��ڲ��ҵ��ļƻ�����Ƚϣ������һ����ʱ����Ȼ�ڴ߱�����ǰ�棬
	 * ��ô���߱�������һ����ʼ�߱���
	 * ���������Ǵ߱������ʱ���ˡ�
	 *
	 * @param cldr   ��ǰ����
	 * @param job    ��ǰ��������
	 * @param notifyTimes   �߱�����
	 * @return  ���˼���ʱ�䣬����true������false
	 */
	private boolean isSuitableForNotify(Calendar cldr, JobEntry job, int notifyTimes)
	{
		String dayBeginTime = Config.getString("cn.com.youtong.apollo.notify.day.begintime", "9:00");
		String dayEndTime = Config.getString("cn.com.youtong.apollo.notify.day.endtime", "17:00");

		int beginTimeColonIndex = dayBeginTime.indexOf(":");
		int endTimeColonIndex = dayEndTime.indexOf(":");

		int beginTimeHour = Integer.parseInt(dayBeginTime.substring(0, beginTimeColonIndex));
		int beginTimeMin = Integer.parseInt(dayBeginTime.substring(beginTimeColonIndex + 1));
		int beginTimeInMin = beginTimeHour * 60 + beginTimeMin;

		int endTimeHour = Integer.parseInt(dayEndTime.substring(0, endTimeColonIndex));
		int endTimeMin = Integer.parseInt(dayEndTime.substring(endTimeColonIndex + 1));
		int endTimeInMin = endTimeHour * 60 + endTimeMin;

		int jobHour = job.getHour();
		jobHour = ((jobHour == -1) ? 0 : jobHour);
		int jobMin = job.getMinute();
		int jobIntervalTimeInMin = jobHour * 60 + jobMin;

		int nowHour = cldr.get(Calendar.HOUR_OF_DAY);
		int nowMin = cldr.get(Calendar.MINUTE);
		int nowTimeInMin = nowHour * 60 + nowMin;

		// �ж��Ƿ��˺��ʵĴ߱�ʱ����
		int perInterval = (endTimeInMin - beginTimeInMin) / notifyTimes;
		if((beginTimeInMin - perInterval) >= nowTimeInMin)
		{
			return false; // û�е��߱�ʱ��
		}
		if((endTimeInMin - perInterval) < nowTimeInMin)
		{
			return false; // ���˴߱�ʱ��
		}

		int notifyTimeInMin = beginTimeInMin;

		for(int i = 0; i < notifyTimes; i++)
		{
			int temp = notifyTimeInMin - nowTimeInMin;
			if(temp >= 0)
			{
				break;
			}
			notifyTimeInMin += perInterval;
		}

		int nowJobInterval = notifyTimeInMin - nowTimeInMin;
		int nextJobInterval = nowJobInterval - jobIntervalTimeInMin;
		//�ȼ���int nextJobInterval=notifyTimeInMin - nowTimeInMin - jobIntervalTimeInMin;
		if(nextJobInterval >= 0)
		{
			// ˵����һ����ʱ�������
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * �����߱��ƻ����񣬽��д߱�
	 * @param task  ����
	 * @throws NotifyException
	 */
	private void notifyTaskNow(Task task)
		throws NotifyException
	{
		String[] unitIDs = getNotifyUnitDomains(task.id());
		notifyTaskUnits(task, unitIDs);
	}

	/**
	 * ����ĳ�ض�����taskID��Ҫ�߱��ĵ�λ
	 * @param taskID  ����ID
	 * @return  ������ID��Ҫ�߱��ĵ�λDomain
	 */
	private String[] getNotifyUnitDomains(String taskID)
	{
		return Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".domain");
	}

	/**
	 * ����ĳ�����񣬶���ĳ����λ���߱�����ȣ�ȱʡֵ��1
	 * @param taskID  ����ID
	 * @param unitID  ��λdomain ID
	 * @return �õ�λ�߱����
	 */
	private int getTaskUnitNotifyDepth(String taskID, String unitID)
	{
		return Config.getInt("cn.com.youtong.apollo.notify." + taskID + "." + unitID + ".depth", 1);
	}

	/**
	 * �߱�ĳ����taskID����λunitIDs���¼���λ�Ƿ�û���ϱ����ݣ�Ȼ��߱�
	 * @param task  ��Ҫ�߱���������
	 * @param unitIDs  ��Ҫ�߱��ĵ�λdomain����
	 * @throws NotifyException
	 */
	private void notifyTaskUnits(Task task, String[] unitIDs)
		throws NotifyException
	{
		String taskID = task.id();
		String metaTableID = null;
		int taskTimeID;
		try
		{
			UnitMetaTable umt = task.getUnitMetaTable();
			metaTableID = umt.id();
			java.util.Date now = new java.util.Date();
			TaskTime time = task.getTaskTime(now);
			if(time == null)
			{
				return;
			}

			taskTimeID = time.getTaskTimeID().intValue();
		}
		catch(Exception ex)
		{
			log.error("������������", ex);
			throw new NotifyException(ex);
		}

		Session session = null;
		// unitsList װ�ص�ÿ��������LinkedList����ÿ��list����û���ϱ����ݵĵ�λ����
		List[] unitLists = new LinkedList[unitIDs.length];
		try
		{
			ModelManagerFactory modelFcty = (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelFcty.createModelManager(taskID);
			session = HibernateUtil.getSessionFactory().openSession();
			for(int i = 0; i < unitIDs.length; i++)
			{
				UnitTreeNode unitNode = modelMng.getUnitTreeManager().getUnitTree(unitIDs[i]);
				int depth = getTaskUnitNotifyDepth(taskID, unitIDs[i]);

				List list = new LinkedList();
				List childList = this.getChildren(unitNode, depth);

				DBAnalyseHelper helper = new DBAnalyseHelper();
				List returnList = helper.hasReportData(taskID, childList, taskTimeID, session)[0];

				for (int j=0, size=childList.size(); j<size; j++)
				{
					boolean hasReport = ((Boolean) returnList.get(j)).booleanValue();
					//helper.hasReportData(session, task, childUnitID, metaTableID, taskTimeID);

					if(!hasReport)
					{
						String childUnitID = (String) list.get(j);
						list.add(childUnitID);
					}
				}

				if(log.isInfoEnabled())
				{
					StringBuffer buff = new StringBuffer( "��λ" )
										.append( unitIDs[i] )
										.append( "����" )
										.append( list.size() )
										.append( "����λҪ�߱�" );

					for(int j = 0, size = list.size(); j < size; j++)
					{
						if( j % 5 == 0 )
							buff.append("\n\t");

						buff.append("��λ").append(list.get(j)).append(" ");
					}
					log.info(buff.toString());
				}

				unitLists[i] = list;
			}
		}
		catch(Exception ex)
		{
			log.error("", ex);
			throw new NotifyException(ex);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException he)
				{}
			}
		}

		if(log.isDebugEnabled())
		{
			log.debug("��ʼ�߱�������" + taskID);
		}

		for(int i = 0; i < unitLists.length; i++)
		{
			List list = unitLists[i];
			notifyUnits(task.id(), list);
		}
	}

	/**
	 * ������λ���߱�����
	 * @param taskID ����ID
	 * @param list  ��λ�б�
	 * @throws NotifyException
	 */
	private void notifyUnits(String taskID, List list)
		throws NotifyException
	{
		sendMessages(taskID, list);
	}

	/**
	 * ����taskID�õ������ļ��еĴ߱���ʽ�����û�����ã���ô�������п��ܵĴ߱���ʽ
	 * @param taskID
	 * @return
	 */
	private String[] getTaskNotifiers(String taskID)
	{
		String[] notifiers = Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".notifiers");
		if(notifiers == null || notifiers.length == 0)
		{
			notifiers = Config.getStringArray("cn.com.youtong.apollo.notifiers");
		}

		return notifiers;
	}

	private void sendMessages(String taskID, List unitList)
		throws NotifyException
	{
		// taskID�����д߱���ʽ
		String[] notifiers = getTaskNotifiers(taskID);

		boolean emailNotify = false;
		boolean smsNotify = false;
		for (int i=0; i<notifiers.length; i++)
		{
			if (notifiers[i].equals("sms"))
			{
				emailNotify = true;
			}
			else if (notifiers[i].equals("email"))
			{
				smsNotify = true;
			}
		}

		Properties emailProp = null;
		Properties smsProp = null;
		String from = Config.getString("mail.admin.address");
		String subject = bundle.getString("mail.subject");
		String bodyText = bundle.getString("mail.bodytext");
		String smsContent =bundle.getString("sms.content");

		if (emailNotify)
		{
			emailProp = new Properties();
		}
		if (smsNotify)
			smsProp = new Properties();

		AddressManager addrMng = null;
		try
		{
			AddressManagerFactory addrFcty = (AddressManagerFactory) Factory.getInstance(AddressManagerFactory.class.getName());
			addrMng = addrFcty.createAddressManager();
		}
		catch(FactoryException ex)
		{
			throw new NotifyException("����������������", ex);
		}

		AddressInfoFormPK formPK = new AddressInfoFormPK();
		formPK.setTaskID(taskID);
		AddressInfoPK pk = new DBAddressInfoPK(formPK);

		for(int i = 0, size = unitList.size(); i < size; i++)
		{
			String unitID = (String) unitList.get(i);
			formPK.setUnitID(unitID);
			AddressInfo info = null;
			try
			{
				info = addrMng.findByPK(pk);
				if (info == null)
					continue;

				if (emailNotify)
				{
					String to = info.getEmail();
					MailBean bean = new MailBean();
					bean.setFrom(from);
					bean.setTo(to);
					bean.setSubject(subject);
					bean.setBodyText(bodyText);
					emailProp.put(to, bean);
				}
				if (smsNotify)
				{
					String rcvNum = info.getMobile();
					smsProp.put(rcvNum, smsContent);
				}
			}
			catch(AddressException ex1)
			{
			}
		}

		if (emailNotify)
		{
			Message msg = new Message(emailProp);
			NotifyService.instance().pushNotify("email", msg);
		}
		if (smsNotify)
		{
			Message msg = new Message(smsProp);
			NotifyService.instance().pushNotify("sms", msg);
		}
	}

	/**
	 * �������Ϊdepth���ڵ�unitNode�������ӽڵ����
	 * @param unitNode   ��λ���
	 * @param depth   �����ӽڵ�����
	 * @return  ���Ϊdepth�������ӽڵ㣬������unitNode��Ӧ�Ľ��
	 */
	private List getChildren(UnitTreeNode unitNode, int depth)
	{
		List result = new LinkedList();
		if(depth == 0)
		{
			return result;
		}

		Iterator children = unitNode.getChildren();
		while(children.hasNext())
		{
			UnitTreeNode child = (UnitTreeNode) children.next();
			result.add(child.id());
			result.addAll(getChildren(child, depth - 1));
		}

		return result;
	}



}