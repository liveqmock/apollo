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
 * NotifyManager从配置文件读取需要催报的任务，然后查看是否到了催报日期。
 * 如果到了催报日期，查看当前是否到了合适的催报时间，如果到了，那么进行催报，否则等待下一时机。
 *
 * <p>
 * <ul>
 *     <li>配置需要催报的任务</li>
 *     <li>配置催报任务需要催报的单位domain中的单位代码(domain意思可以查看配置文件)</li>
 *     <li>配置文件可以配置，是否只有工作日催报。缺省是只有工作日催报；</li>
 *     <li>配置催报的起始时间和结束时间。催报开始时间和结束时间，缺省是上午9:00开始，下午17:00结束；</li>
 *     <li>配置第一天催报次数。没有缺省值</li>
 *     <li>配置催报增加次数，每天增加的催报次数（自然数）。缺省是0</li>
 * </ul>
 *
 * <p>
 * 催报规则：可以提前一点点催报，但是不晚一点点催报；尽可能的靠近催报时间点。<br/>
 * 催报前提条件:激活任务的运行时间间隔比催报任务时间间隔短。<br/>
 *
 * 比如，10:10有催报任务，10:00运行了该程序，而且下一次运行这个程序时间为10:40，那么现在就进行催报。
 * 但是如果现在是9:20，激活该程序的时间间隔是40分钟，那么下一激活时间是10:00，那么10:10的催报任务，
 * 要等到下一次激活开始催报。
 *
 * <p>
 * 设计思想:<br/>
 * 从配置文件读取催报开始时间和结束时间，程序计算出任务催报次数。
 * 可以算出每次催报的时间间隔。当前job，即唤醒该程序的计划任务的时间，
 * 如果超出了催报结束时间，不催报；
 * 如果比催报开始时间要早催报时间间隔大，不催报；
 * 否则，查找催报时间，找到第一个在当前任务时间后面的催报时间；
 * 比较当前任务时间和下一任务时间，分别于查找到的计划任务比较，如果下一任务时间依然在催报任务前面，
 * 那么不催报，等下一任务开始催报；
 * 否则，现在是催报的最佳时机了。
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
	 * 催报设置的任务和单位
	 * @param job 当前激活任务
	 * @throws NotifyException
	 */
	public void notifyTasks(JobEntry job)
		throws NotifyException
	{
		// 读取要催报的任务ID
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
	 * 给定任务，查看该任务是否到了催报时间了，如果到了，催报。否则，返回
	 * 如果设置只有工作日催报，那么非工作日也返回，不进行催报。
	 * 在配置文件配置第一天催报，催报次数，以后每天增加次数（必须是自然数），缺省是0
	 *
	 * @param job  当前激活任务
	 * @param taskID  任务ID
	 * @throws NotifyException
	 */
	private void checkAndNotifyTask(JobEntry job, String taskID)
		throws NotifyException
	{
		Calendar cldr = new GregorianCalendar();
		boolean onlyWorkday = Config.getBoolean("com.youtong.apollo.notify.workday.only", true);
		if(onlyWorkday)
		{
			// 非工作日，不进行催报
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
				// 没有对应任务时间返回
				return;
			}

			java.util.Date attentionFromTime = time.getAttentionFromTime();
			java.util.Date attentionEndTime = time.getAttentionEndTime();

			if(!(now.before(attentionEndTime) && now.after(attentionFromTime)))
			{
				// 不在催报时间范围内，返回
				return;
			}

			long millSecondsOneDay = 86400000; // 60*60*24*1000=86400
			int days = (int) ((now.getTime() - attentionFromTime.getTime()) / (millSecondsOneDay));

			int base = Config.getInt("cn.com.youtong.apollo.notify.base");
			int increment = Config.getInt("cn.com.youtong.apollo.notify.increment", 0);

			int notifyTimes = base + days * increment; // 要催报的次数
			log.info("要催报任务" + taskID + " 次数" + notifyTimes);

			// 查看当前是否适合催报
			boolean suitForNotify = isSuitableForNotify(cldr, job, notifyTimes);
			if(suitForNotify)
			{
				if(log.isDebugEnabled())
				{
					StringBuffer sb = new StringBuffer("现在是催报的最好时机了");
					sb.append(" 任务=").append(taskID).append(" 当前激活时间Hour=");
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
	 * 给定任务ID taskID，和每天催报的次数notifyTimes，返回这些计划任务。
	 * 其中在配置文件中配置，每天催报开始时间和结束时间，缺省是上午9:00开始，下午17:00结束
	 * <p>
	 * 催报规则：可以提前一点点催报，但是不晚一点点催报；尽可能的靠近催报时间点。<br/>
	 * 催报前提条件:激活任务的运行时间间隔比催报任务时间间隔短。<br/>
	 *
	 * 比如，10:10有催报任务，10:00运行了该程序，而且下一次运行这个程序时间为10:40，那么现在就进行催报。
	 * 但是如果现在是9:20，激活该程序的时间间隔是40分钟，那么下一激活时间是10:00，那么10:10的催报任务，
	 * 要等到下一次激活开始催报。
	 *
	 * <p>
	 * 设计思想:<br/>
	 * 从配置文件读取催报开始时间和结束时间，参数中给出了催报次数。
	 * 可以算出每次催报的时间间隔。当前job，即唤醒该程序的计划任务的时间，
	 * 如果超出了催报结束时间，不催报；
	 * 如果比催报开始时间要早催报时间间隔大，不催报；
	 * 否则，查找催报时间，找到第一个在当前任务时间后面的催报时间；
	 * 比较当前任务时间和下一任务时间，分别于查找到的计划任务比较，如果下一任务时间依然在催报任务前面，
	 * 那么不催报，等下一任务开始催报；
	 * 否则，现在是催报的最佳时机了。
	 *
	 * @param cldr   当前日期
	 * @param job    当前激活任务
	 * @param notifyTimes   催报次数
	 * @return  到了激活时间，返回true；否则false
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

		// 判断是否到了合适的催报时间了
		int perInterval = (endTimeInMin - beginTimeInMin) / notifyTimes;
		if((beginTimeInMin - perInterval) >= nowTimeInMin)
		{
			return false; // 没有到催报时间
		}
		if((endTimeInMin - perInterval) < nowTimeInMin)
		{
			return false; // 过了催报时间
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
		//等价于int nextJobInterval=notifyTimeInMin - nowTimeInMin - jobIntervalTimeInMin;
		if(nextJobInterval >= 0)
		{
			// 说明下一任务时间更合适
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 给定催报计划任务，进行催报
	 * @param task  任务
	 * @throws NotifyException
	 */
	private void notifyTaskNow(Task task)
		throws NotifyException
	{
		String[] unitIDs = getNotifyUnitDomains(task.id());
		notifyTaskUnits(task, unitIDs);
	}

	/**
	 * 返回某特定任务taskID需要催报的单位
	 * @param taskID  任务ID
	 * @return  该任务ID需要催报的单位Domain
	 */
	private String[] getNotifyUnitDomains(String taskID)
	{
		return Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".domain");
	}

	/**
	 * 返回某个任务，对于某个单位，催报的深度，缺省值是1
	 * @param taskID  任务ID
	 * @param unitID  单位domain ID
	 * @return 该单位催报深度
	 */
	private int getTaskUnitNotifyDepth(String taskID, String unitID)
	{
		return Config.getInt("cn.com.youtong.apollo.notify." + taskID + "." + unitID + ".depth", 1);
	}

	/**
	 * 催报某任务taskID，单位unitIDs的下级单位是否没有上报数据，然后催报
	 * @param task  需要催报的任务定义
	 * @param unitIDs  需要催报的单位domain数组
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
			log.error("工厂方法出错", ex);
			throw new NotifyException(ex);
		}

		Session session = null;
		// unitsList 装载的每个对象都是LinkedList，，每个list包含没有上报数据的单位代码
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
					StringBuffer buff = new StringBuffer( "单位" )
										.append( unitIDs[i] )
										.append( "存在" )
										.append( list.size() )
										.append( "个单位要催报" );

					for(int j = 0, size = list.size(); j < size; j++)
					{
						if( j % 5 == 0 )
							buff.append("\n\t");

						buff.append("单位").append(list.get(j)).append(" ");
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
			log.debug("开始催报，任务" + taskID);
		}

		for(int i = 0; i < unitLists.length; i++)
		{
			List list = unitLists[i];
			notifyUnits(task.id(), list);
		}
	}

	/**
	 * 给定单位，催报他们
	 * @param taskID 任务ID
	 * @param list  单位列表
	 * @throws NotifyException
	 */
	private void notifyUnits(String taskID, List list)
		throws NotifyException
	{
		sendMessages(taskID, list);
	}

	/**
	 * 根据taskID得到配置文件中的催报方式，如果没有设置，那么返回所有可能的催报方式
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
		// taskID的所有催报方式
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
			throw new NotifyException("创建工厂方法出错", ex);
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
	 * 返回深度为depth，节点unitNode的所有子节点代码
	 * @param unitNode   单位结点
	 * @param depth   遍历子节点的深度
	 * @return  深度为depth的所有子节点，不包含unitNode对应的结点
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