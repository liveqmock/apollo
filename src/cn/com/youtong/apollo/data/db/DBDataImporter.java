package cn.com.youtong.apollo.data.db;

// java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.youtong.apollo.common.Convertor;
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.mail.Mail;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.common.sql.SQLUtil;
import cn.com.youtong.apollo.data.DataImporter;
import cn.com.youtong.apollo.data.DataSource;
import cn.com.youtong.apollo.data.DataStatus;
import cn.com.youtong.apollo.data.LoadResult;
import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.data.UnitACL;
import cn.com.youtong.apollo.data.UnitPermission;
import cn.com.youtong.apollo.data.UnitPermissionManager;
import cn.com.youtong.apollo.notify.Message;
import cn.com.youtong.apollo.notify.NotifyException;
import cn.com.youtong.apollo.notify.NotifyService;
import cn.com.youtong.apollo.script.AuditResult;
import cn.com.youtong.apollo.script.ScriptEngine;
import cn.com.youtong.apollo.script.ScriptException;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.Script;
import cn.com.youtong.apollo.task.ScriptSuit;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskException;
import cn.com.youtong.apollo.task.TaskTime;
import cn.com.youtong.apollo.usermanager.Group;
import cn.com.youtong.apollo.usermanager.SetOfPrivileges;
import cn.com.youtong.apollo.usermanager.User;


/**
 * 报表数据导入器，负责执行报表数据上报的各个流程：保存，运行脚本，记录填报情况等
 */
class DBDataImporter implements DataImporter
{
	/** DBUnitTreeManager实例 */
	private DBUnitTreeManager treeManager;

	/** log对象 */
	private static Log log = LogFactory.getLog( DBDataImporter.class );

	/** 报表任务定义 */
	private Task task;

	/**
	 * 数据上报结果
	 */
	private LoadResult loadResult = new LoadResult();
	/** 开始数据装入时间，和结束装入时间 */
	private Date beginLoad;
	private Date endLoad;
	private static final String INSERT_FILLSTATE_SQL
		= "insert into Ytapl_FillState values (?,?,?,?,?)";
	private static final String DELETE_FILLSTATE_SQL
		=
		"DELETE FROM Ytapl_FillState WHERE unitID=? and taskID=? and taskTimeID=?";

	public DBDataImporter(Task task, DBUnitTreeManager treeManager )
	{
		this.task = task;
		this.treeManager = treeManager;
	}

	/**
	 * 导入数据，发送消息，进行脚本计算，更新填报情况
	 * @param xmlInputStream 数据流
	 * @param acl UnitACL
	 * @return 上报结果
	 * @throws ModelException
	 */
	public LoadResult importData(InputStream xmlInputStream, UnitACL acl) throws
		ModelException
	{
		try
		{
			LoadResult ladrest = storeData( xmlInputStream, acl );
			if(ladrest.getStoredUnitIDs().hasNext()){
				autoAssignPremission(loadResult.getNewUnitIDs(), acl.getUser());
				doStoreDataLog( loadResult, acl.getUser() );
				notifySuccess( loadResult, acl.getUser() );
				log.info( "开始脚本计算" );
				excuteScripts(loadResult.getStoredUnitIDs(), loadResult.getTaskTime());
				log.info( "计算脚本完成！\n" );
	
				log.info( "开始更新填报情况" );
				fillUnitStates(loadResult.getTaskTime(),
							   Convertor.collection(loadResult.getStoredUnitIDs()),
							   DataStatus.REPORTED_UNENVLOP);
				log.info( "更新填报记录完成！\n" );
			}
		}
		catch( ModelException ex )
		{
			notifyFailure( acl.getUser() );
			throw ex;
		}
		return loadResult;
	}

	/**
	 * 自动将上报的新单位的权限分配给上报数据的用户
	 * @param newUnitIDItr 新单位ID的Iterator
	 * @param user 上报新单位数据的用户
	 * @throws ModelException
	 */
	private void autoAssignPremission(Iterator newUnitIDItr, User user) throws ModelException
	{
		Collection unitIDs = Convertor.collection(newUnitIDItr);
		UnitPermissionManager permissionManager =  new DBUnitPermissionManager(task, treeManager);

		//对单位的完全控制权限
		UnitPermission permission = new UnitPermission();
		permission.setAllPermissions(true);

		//将每个新单位的控制权限分配给上报用户所在的每个组
		Collection groups = user.getGroups();
		for(Iterator groupItr = groups.iterator(); groupItr.hasNext();)
		{
			Group group = (Group)groupItr.next();
			for(Iterator unitIDItr = unitIDs.iterator(); unitIDItr.hasNext();)
			{
				String unitID = (String)unitIDItr.next();
				permissionManager.setUnitPermission(group.getGroupID(), unitID, permission);
			}
		}
	}
	//写入文件
	public void inputStreamToString(String str){
		 String path="e://a.txt";   
         try {   
              FileWriter fw=new FileWriter(path,true);   
              BufferedWriter bw=new BufferedWriter(fw);   
              bw.newLine();   
  bw.write(str);     
  bw.close();  
   fw.close();   
       } catch (IOException e) {   
            // TODO Auto-generated catch block   
           e.printStackTrace();   
        }   
	}
	//inputstream2string
	public static String convertStreamToString(InputStream is) {      
        /*  
          * To convert the InputStream to String we use the BufferedReader.readLine()  
          * method. We iterate until the BufferedReader return null which means  
          * there's no more data to read. Each line will appended to a StringBuilder  
          * and returned as String.  
          */     
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
         StringBuilder sb = new StringBuilder();      
     
         String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {      
                 sb.append(line + "\n");      
             }      
         } catch (IOException e) {      
             e.printStackTrace();      
         } finally {      
            try {      
                 is.close();      
             } catch (IOException e) {      
                 e.printStackTrace();      
             }      
         }      
     
        return sb.toString();      
     }
	/**
	 * 单纯只是为了保存数据，不发送消息，
	 * @param xmlInputStream
	 * @param acl
	 * @throws ModelException
	 */
	LoadResult storeData( InputStream xmlInputStream, UnitACL acl )
		throws ModelException
	{
		log.info( "开始向数据库保存数据" );
		beginLoad = new Date();

		DBDataHandler handler = new DBDataHandler( acl, task, treeManager );
//		String string = this.convertStreamToString(xmlInputStream);
//		System.out.println("===========");
////		System.out.println(string);
//		inputStreamToString(string);
//		System.out.println("==========");
		if (acl.getUser().getRole().getPrivileges().getPrivilege(
			SetOfPrivileges.FORCE_IMPORT_OVERDUE_DATA))
		{
			handler.setIngoreTaskTime( true );
		}

		SAXParserFactory factory = SAXParserFactory.newInstance();

		SAXParser saxParser;
		
		try
		{
			saxParser = factory.newSAXParser();
			saxParser.parse(xmlInputStream, handler);

			if (task == null)
			{
				task = handler.getTask();
			}

			endLoad = new Date();
			loadResult = handler.getLoadResult();

			log.info( "数据保存到数据库完成！\n" );

			return loadResult;
		}
		catch( Exception ex )
		{
			log.error("", ex );
			ex.printStackTrace();
			throw new ModelException( ex );
		}
		finally
		{
			handler.release();
			treeManager.update(); // may throw ModelException
		}
	}
	/**
	 * 增加或者更新单位的填报情况
	 * @param taskTime    任务时间
	 * @param units       单位集合
	 * @param dataStatus  数据状态(上报,审核,退回)
	 * @throws ModelException   发生异常
	 */
	private void fillUnitStates( TaskTime taskTime, Collection units,
								 int dataStatus )
		throws ModelException
	{
		Session session = null;
		Transaction trans = null;
		PreparedStatement deleteFillStatePStmt = null; // 删除对应的上报情况记录
		PreparedStatement insertFillStatePStmt = null; // 插入Ytapl_FillState表记录单位已上报数据

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			trans = session.beginTransaction();

			Connection con = session.connection();

			// 初始化删除和记录单位上报数据PreparedStatement
			deleteFillStatePStmt = con.prepareStatement(DELETE_FILLSTATE_SQL);
			insertFillStatePStmt = con.prepareStatement(INSERT_FILLSTATE_SQL);

			String taskID = task.id();

			// 记录单位已上报数据
			// 先删除对应记录，后插入
			Iterator unitIter = units.iterator();
			while (unitIter.hasNext())
			{
				String unitID = (String) unitIter.next();
				deleteFillStatePStmt.setString(1, unitID);
				deleteFillStatePStmt.setString(2, taskID);
				deleteFillStatePStmt.setInt(3,
											taskTime.getTaskTimeID().intValue());
				deleteFillStatePStmt.addBatch();

				insertFillStatePStmt.setString(1, unitID);
				insertFillStatePStmt.setString(2, taskID);
				insertFillStatePStmt.setInt(3,
											taskTime.getTaskTimeID().intValue());
				insertFillStatePStmt.setTimestamp(4,
												  new java.sql.Timestamp(new
					java.
					util.Date().getTime()));
				insertFillStatePStmt.setInt(5, dataStatus);
				insertFillStatePStmt.addBatch();
			}

			deleteFillStatePStmt.executeBatch();
			insertFillStatePStmt.executeBatch();

			trans.commit();
		}
		catch (SQLException sqle)
		{
			log.error("", sqle);
			throw new ModelException(sqle);
		}
		catch (HibernateException he)
		{
			log.error("", he);
			throw new ModelException(he);
		}
		finally
		{
			SQLUtil.close(deleteFillStatePStmt);
			SQLUtil.close(insertFillStatePStmt);
			HibernateUtil.close(session);
		}

	}

	/**
	 * 对指定的单位执行脚本
	 * @param unitIDItr 单位ID的Iterator
	 * @param taskTime 任务时间
	 * @throws ModelException
	 */
	public void excuteScripts(Iterator unitIDItr, TaskTime taskTime) throws
		ModelException
	{
		DataSource dataSource = new DBDataSource( task, treeManager);
		try
		{
			//执行脚本
			ScriptSuit suit = task.getActiveScriptSuit();
			if (suit != null)
			{
				ScriptEngine scriptEngine = new ScriptEngine();
				//compile scripts
				List calculateScripts = scriptEngine.compile(getScriptContent(
					task.getActiveScriptSuit().
					getCalculateScriptToExec()));
				List auditScripts = scriptEngine.compile(getScriptContent(
					task.getActiveScriptSuit().
					getAuditScriptToExec()));

				List unitIDs = new LinkedList();
				while( unitIDItr.hasNext() )
				{
					unitIDs.add( unitIDItr.next() );
				}

				// 一次性取得所有TaskData
				Iterator taskDataIter = dataSource.get( unitIDs, taskTime );

				//依次对TaskData进行脚本计算
				while (taskDataIter.hasNext())
				{
					TaskData taskData = (TaskData) taskDataIter.next();

                    // 计算
					scriptEngine.execCalculateScript(taskData,
						calculateScripts);

					// 审核
					AuditResult auditResult = scriptEngine.execAuditScript(
						taskData, auditScripts);
					loadResult.addAuditResult(taskData.getUnitID(), auditResult);
				}

				doScriptAuditLog( loadResult );
			}
		}
		catch (TaskException ex)
		{
			throw new ModelException(ex);
		}
		catch (ScriptException ex)
		{
			throw new ModelException(ex);
		}
		catch( ModelException ex )
		{
			throw ex;
		}
		finally
		{
			try
			{
				dataSource.commit();
			}
			catch( Exception ex )
			{
				log.error("数据源提交记录出错", ex );
			}

			try
			{
				dataSource.close();
			}
			catch (Exception ex )
			{
				log.error("关闭数据源出错", ex );
			}
		}
	}

	/**
	 * 得到脚本的内容
	 * @param scripts 脚本Script集合
	 * @return 脚本内容String集合
	 */
	private List getScriptContent(List scripts)
	{
		List result = new LinkedList();
		for (Iterator itr = scripts.iterator(); itr.hasNext(); )
		{
			result.add( ( (Script) itr.next()).getContent());
		}
		return result;
	}

	private void doStoreDataLog( LoadResult result, User user )
	{
		log.info( getStoreDataMsg( result, user ) );
	}

	private void doScriptAuditLog( LoadResult result )
	{
		StringBuffer buff = new StringBuffer( "脚本审核结果" );
		buff.append( "\n" )
			.append( result.getAuditMessage() )
			.append( "\n" );
		log.info( buff.toString() );
	}

	private String getStoreDataMsg( LoadResult result, User user )
	{
		StringBuffer buff = new StringBuffer();
		buff.append( "\n" );
		buff.append( "*********************************************************" );
		buff.append( "\n\t装 入 数 据 小 节\n\n" );
		buff.append( "\t装入者: " )
			.append( user.getName() )
			.append( "\n\t企业名称: " )
			.append( user.getEnterpriseName() )
			.append( "\n" );
		buff.append( "\t装入开始时间: " )
			.append( Convertor.date2String( beginLoad ) );
		buff.append( "\n\t装入结束时间: " )
			.append( Convertor.date2String( endLoad ) )
			.append( "\n" );

		buff.append( "装入的任务: " )
			.append( "\n\t任务名称: " )
			.append( task.getName() )
			.append( "\n\t创建时间: " )
			.append( Convertor.date2String( task.getCreatedDate() ) )
			.append( "\n\t修改时间: " )
			.append( Convertor.date2String( task.getModifiedDate() ) )
			.append( "\n" );

		buff.append( "装入的任务时间: " )
			.append( "\n\t起始时间: " )
			.append( Convertor.date2String( result.getTaskTime().getFromTime() ) )
			.append( "\n\t结束时间: " )
			.append( Convertor.date2String( result.getTaskTime().getEndTime() ) )
			.append( "\n" );

		buff.append( result.getMessage() );
		buff.append( "\n" );
		buff.append( "*********************************************************" );

		return buff.toString();
	}

	/**
	 * 通知装入成功
	 * @param result
	 * @param user
	 */
	private void notifySuccess( LoadResult result, User user )
	{
		ResourceBundle bundle = ResourceBundle.getBundle("cn.com.youtong.apollo.data.db.data");

		String msg = getStoreDataMsg( result, user );
		String[] notifyType = Config.getStringArray(
			"cn.com.youtong.apollo.loaddata.notify.success");
		boolean useMail = false;
		boolean useSms = false;

		if (notifyType == null)
		{
			return;
		}
		for (int i = 0; i < notifyType.length; i++)
		{
			if (notifyType[i].equals("email"))
			{
				useMail = true;
			}
			else if (notifyType[i].equals("sms"))
			{
				useSms = true;
			}
		}

		if (useSms)
		{
			String mobile = user.getContactPersionMobile();
			String content = bundle.getString("sms.load.success.content");
			if (!Util.isEmptyString(mobile))
			{
				if( log.isInfoEnabled() )
					log.info("短信通知用户: " + user.getName()
						 +" 手机号码: " + mobile + "装入结果");

				Properties prop = new Properties();
				prop.put(mobile, content);
				try
				{
					NotifyService.instance().pushNotify("sms", new Message(prop));
				}
				catch (NotifyException ex)
				{
					log.error("发送短信失败", ex);
				}
			}
		}

		if (useMail)
		{
			String userMailAddr = user.getEmail();
			String adminMailAddr = Config.getString("mail.admin.address");

			boolean toAdmin = Config.getBoolean(
						 "cn.com.youtong.apollo.loaddata.notify.success.toadmin", true);

			if( log.isInfoEnabled() )
			{
				log.info("电子邮件通知用户: " + user.getName()
						 +" 邮件地址: " + userMailAddr + "装入结果");
			}

			if (Util.validateMailAddress(userMailAddr))
			{
				String cc = null;
				String subject = bundle.getString("mail.load.success.subject");
				if (toAdmin)
				{
					cc = adminMailAddr;
				}
				Mail.send(adminMailAddr, userMailAddr, cc, null, subject, msg, false);
			}
		}
	}
	/**
	 * 通知装入失败，后面添加了装入时间信息。
	 * @param user
	 */
	private void notifyFailure( User user)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("cn.com.youtong.apollo.data.db.data");

		String[] notifyType = Config.getStringArray(
				  "cn.com.youtong.apollo.loaddata.notify.failure");
		boolean useMail = false;
		boolean useSms = false;

		if (notifyType == null)
		{
			return;
		}
		for (int i = 0; i < notifyType.length; i++)
		{
			if (notifyType[i].equals("email"))
			{
				useMail = true;
			}
			else if (notifyType[i].equals("sms"))
			{
				useSms = true;
			}
		}

		StringBuffer buff = new StringBuffer();
		buff.append( "系统不能装入您的数据。任务" )
			.append( task.id() )
			.append( "装入时间" )
			.append( Convertor.date2String( beginLoad ) );

		String msg = buff.toString();

		if (useSms)
		{
			String mobile = user.getContactPersionMobile();
			String content = bundle.getString("sms.load.failure.content");

			if( content == null )
			{
				content = msg;
			}
			else
			{
				content = content + " (装入时间"
						  + Convertor.date2String( beginLoad )
						  + ")";
			}
			if (!Util.isEmptyString(mobile))
			{
				if( log.isInfoEnabled() )
					log.info("短信通知用户: " + user.getName()
						 +" 手机号码: " + mobile + "装入失败");

				Properties prop = new Properties();
				prop.put(mobile, content);
				try
				{
					NotifyService.instance().pushNotify("sms", new Message(prop));
				}
				catch (NotifyException ex)
				{
					log.error("发送短信失败", ex);
				}
			}
		}

		if (useMail)
		{
			String userMailAddr = user.getEmail();
			String adminMailAddr = Config.getString("mail.admin.address");

			boolean toAdmin = Config.getBoolean(
						 "cn.com.youtong.apollo.loaddata.notify.failure.toadmin", true);

			if( log.isInfoEnabled() )
			{
				log.info("电子邮件通知用户: " + user.getName()
						 +" 邮件地址: " + userMailAddr + "装入失败");
			}

			if (Util.validateMailAddress(userMailAddr))
			{
				String cc = null;
				String subject = bundle.getString("mail.load.failure.subject");
				if (toAdmin)
				{
					cc = adminMailAddr;
				}
				Mail.send(adminMailAddr, userMailAddr, cc, null, subject, msg, false);
			}
		}
	}
}