package cn.com.youtong.apollo.upload;

// java
import java.util.List;

// apache common logging
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// apollo
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UploaderManager
{
	private static Log log = LogFactory.getLog( UploaderManager.class.getName() );

    public UploaderManager()
    {
    }

	/**
	 * ȡ������Ϣ���ϱ���ʽ�������ʵ��ķ�ʽ�ϱ����ݡ�
	 * <p>
	 * ��������ļ���cn.com.youtong.apollo.upload.type��ֵ����mail,ftp����webservice
	 * �е�һ�֣���ô��¼��־�������κγ���
	 * </p>
	 */
	public void upload()
	{
		String type = Config.getString( "cn.com.youtong.apollo.upload.type" );
		Uploader uploader = getUploader( type );
		if( uploader == null )
		{
			log.error( "��֧�ֵ��ϱ���ʽ��" + type );
			return;
		}

		uploader.upload();
	}

	/**
	 * ����uploadType��ʽ���ϱ�����task������ʱ��taskTime,
	 * ��λ�����ڵ��б�unitIDs����¼�ϼ��û���������Ϊusername,password��
	 * ���ϼ��������ϱ����ݡ�
	 *
	 * @param uploadType         mail, ftp, webservice
	 * @param task               Task
	 * @param taskTime           TaskTime
	 * @param unitIDs            ��λ�����ڵ��б�������ĳ����λ�����ݣ�ֻҪָ���õ�λ�����ڵ㼴��
	 * @param username           ��¼�ϼ��������û���
	 * @param password           ��Ӧ������
	 * @throws UploadException
	 */
	public void upload( String uploadType, Task task, TaskTime taskTime,
						List unitIDs, String username, String password )
		throws UploadException
	{
		Uploader uploader = getUploader( uploadType );
		if( uploader == null )
		{
			String msg = "��֧�ֵ��ϱ���ʽ��" + uploadType;
			log.error( msg );
			throw new UploadException( msg );
		}

		uploader.upload( task, taskTime, unitIDs, username, password );
	}

	private Uploader getUploader( String type )
	{
		if( type.equalsIgnoreCase( "mail" ) )
			return new MailUploader();
		else if( type.equalsIgnoreCase( "ftp" ) )
			return new FtpUploader();
		else if( type.equalsIgnoreCase( "webservice" ) )
			return new WebServiceUploader();
		else
			return null;
	}

	/**public static void main(String[] args)
	{
		try
		{
			cn.com.youtong.apollo.MockService.init();

			Task task = new cn.com.youtong.apollo.task.db.DBTaskManager()
						.getTaskByID( "QYKB" );
			TaskTime taskTime = task.getTaskTime( new java.util.Date() );
			List unitIDs = new java.util.LinkedList();
			unitIDs.add( "8043301360" ); // �ӱ�ʡ�����豸�ܹ�˾
			unitIDs.add( "GONGMAO007" ); // �ӱ���ó�ʲ���Ӫ��˾
			// unitIDs.add( "HBGZW58127" ); //58�һ���
			UploaderManager mng = new UploaderManager();
			//mng.upload( "webservice", task, taskTime, unitIDs, "admin", "password" );
			mng.upload();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		finally
		{
			cn.com.youtong.apollo.MockService.shutdown();
		}
	}*/
}