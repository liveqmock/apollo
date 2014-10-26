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
 * �������ݵ�����������ִ�б��������ϱ��ĸ������̣����棬���нű�����¼������
 */
class DBDataImporter implements DataImporter
{
	/** DBUnitTreeManagerʵ�� */
	private DBUnitTreeManager treeManager;

	/** log���� */
	private static Log log = LogFactory.getLog( DBDataImporter.class );

	/** ���������� */
	private Task task;

	/**
	 * �����ϱ����
	 */
	private LoadResult loadResult = new LoadResult();
	/** ��ʼ����װ��ʱ�䣬�ͽ���װ��ʱ�� */
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
	 * �������ݣ�������Ϣ�����нű����㣬��������
	 * @param xmlInputStream ������
	 * @param acl UnitACL
	 * @return �ϱ����
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
				log.info( "��ʼ�ű�����" );
				excuteScripts(loadResult.getStoredUnitIDs(), loadResult.getTaskTime());
				log.info( "����ű���ɣ�\n" );
	
				log.info( "��ʼ��������" );
				fillUnitStates(loadResult.getTaskTime(),
							   Convertor.collection(loadResult.getStoredUnitIDs()),
							   DataStatus.REPORTED_UNENVLOP);
				log.info( "�������¼��ɣ�\n" );
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
	 * �Զ����ϱ����µ�λ��Ȩ�޷�����ϱ����ݵ��û�
	 * @param newUnitIDItr �µ�λID��Iterator
	 * @param user �ϱ��µ�λ���ݵ��û�
	 * @throws ModelException
	 */
	private void autoAssignPremission(Iterator newUnitIDItr, User user) throws ModelException
	{
		Collection unitIDs = Convertor.collection(newUnitIDItr);
		UnitPermissionManager permissionManager =  new DBUnitPermissionManager(task, treeManager);

		//�Ե�λ����ȫ����Ȩ��
		UnitPermission permission = new UnitPermission();
		permission.setAllPermissions(true);

		//��ÿ���µ�λ�Ŀ���Ȩ�޷�����ϱ��û����ڵ�ÿ����
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
	//д���ļ�
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
	 * ����ֻ��Ϊ�˱������ݣ���������Ϣ��
	 * @param xmlInputStream
	 * @param acl
	 * @throws ModelException
	 */
	LoadResult storeData( InputStream xmlInputStream, UnitACL acl )
		throws ModelException
	{
		log.info( "��ʼ�����ݿⱣ������" );
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

			log.info( "���ݱ��浽���ݿ���ɣ�\n" );

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
	 * ���ӻ��߸��µ�λ������
	 * @param taskTime    ����ʱ��
	 * @param units       ��λ����
	 * @param dataStatus  ����״̬(�ϱ�,���,�˻�)
	 * @throws ModelException   �����쳣
	 */
	private void fillUnitStates( TaskTime taskTime, Collection units,
								 int dataStatus )
		throws ModelException
	{
		Session session = null;
		Transaction trans = null;
		PreparedStatement deleteFillStatePStmt = null; // ɾ����Ӧ���ϱ������¼
		PreparedStatement insertFillStatePStmt = null; // ����Ytapl_FillState���¼��λ���ϱ�����

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			trans = session.beginTransaction();

			Connection con = session.connection();

			// ��ʼ��ɾ���ͼ�¼��λ�ϱ�����PreparedStatement
			deleteFillStatePStmt = con.prepareStatement(DELETE_FILLSTATE_SQL);
			insertFillStatePStmt = con.prepareStatement(INSERT_FILLSTATE_SQL);

			String taskID = task.id();

			// ��¼��λ���ϱ�����
			// ��ɾ����Ӧ��¼�������
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
	 * ��ָ���ĵ�λִ�нű�
	 * @param unitIDItr ��λID��Iterator
	 * @param taskTime ����ʱ��
	 * @throws ModelException
	 */
	public void excuteScripts(Iterator unitIDItr, TaskTime taskTime) throws
		ModelException
	{
		DataSource dataSource = new DBDataSource( task, treeManager);
		try
		{
			//ִ�нű�
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

				// һ����ȡ������TaskData
				Iterator taskDataIter = dataSource.get( unitIDs, taskTime );

				//���ζ�TaskData���нű�����
				while (taskDataIter.hasNext())
				{
					TaskData taskData = (TaskData) taskDataIter.next();

                    // ����
					scriptEngine.execCalculateScript(taskData,
						calculateScripts);

					// ���
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
				log.error("����Դ�ύ��¼����", ex );
			}

			try
			{
				dataSource.close();
			}
			catch (Exception ex )
			{
				log.error("�ر�����Դ����", ex );
			}
		}
	}

	/**
	 * �õ��ű�������
	 * @param scripts �ű�Script����
	 * @return �ű�����String����
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
		StringBuffer buff = new StringBuffer( "�ű���˽��" );
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
		buff.append( "\n\tװ �� �� �� С ��\n\n" );
		buff.append( "\tװ����: " )
			.append( user.getName() )
			.append( "\n\t��ҵ����: " )
			.append( user.getEnterpriseName() )
			.append( "\n" );
		buff.append( "\tװ�뿪ʼʱ��: " )
			.append( Convertor.date2String( beginLoad ) );
		buff.append( "\n\tװ�����ʱ��: " )
			.append( Convertor.date2String( endLoad ) )
			.append( "\n" );

		buff.append( "װ�������: " )
			.append( "\n\t��������: " )
			.append( task.getName() )
			.append( "\n\t����ʱ��: " )
			.append( Convertor.date2String( task.getCreatedDate() ) )
			.append( "\n\t�޸�ʱ��: " )
			.append( Convertor.date2String( task.getModifiedDate() ) )
			.append( "\n" );

		buff.append( "װ�������ʱ��: " )
			.append( "\n\t��ʼʱ��: " )
			.append( Convertor.date2String( result.getTaskTime().getFromTime() ) )
			.append( "\n\t����ʱ��: " )
			.append( Convertor.date2String( result.getTaskTime().getEndTime() ) )
			.append( "\n" );

		buff.append( result.getMessage() );
		buff.append( "\n" );
		buff.append( "*********************************************************" );

		return buff.toString();
	}

	/**
	 * ֪ͨװ��ɹ�
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
					log.info("����֪ͨ�û�: " + user.getName()
						 +" �ֻ�����: " + mobile + "װ����");

				Properties prop = new Properties();
				prop.put(mobile, content);
				try
				{
					NotifyService.instance().pushNotify("sms", new Message(prop));
				}
				catch (NotifyException ex)
				{
					log.error("���Ͷ���ʧ��", ex);
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
				log.info("�����ʼ�֪ͨ�û�: " + user.getName()
						 +" �ʼ���ַ: " + userMailAddr + "װ����");
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
	 * ֪ͨװ��ʧ�ܣ����������װ��ʱ����Ϣ��
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
		buff.append( "ϵͳ����װ���������ݡ�����" )
			.append( task.id() )
			.append( "װ��ʱ��" )
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
				content = content + " (װ��ʱ��"
						  + Convertor.date2String( beginLoad )
						  + ")";
			}
			if (!Util.isEmptyString(mobile))
			{
				if( log.isInfoEnabled() )
					log.info("����֪ͨ�û�: " + user.getName()
						 +" �ֻ�����: " + mobile + "װ��ʧ��");

				Properties prop = new Properties();
				prop.put(mobile, content);
				try
				{
					NotifyService.instance().pushNotify("sms", new Message(prop));
				}
				catch (NotifyException ex)
				{
					log.error("���Ͷ���ʧ��", ex);
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
				log.info("�����ʼ�֪ͨ�û�: " + user.getName()
						 +" �ʼ���ַ: " + userMailAddr + "װ��ʧ��");
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