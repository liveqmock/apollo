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
	 * 取配置信息中上报方式，采用适当的方式上报数据。
	 * <p>
	 * 如果配置文件中cn.com.youtong.apollo.upload.type的值不是mail,ftp或者webservice
	 * 中的一种，那么记录日志，不做任何出来
	 * </p>
	 */
	public void upload()
	{
		String type = Config.getString( "cn.com.youtong.apollo.upload.type" );
		Uploader uploader = getUploader( type );
		if( uploader == null )
		{
			log.error( "不支持的上报方式：" + type );
			return;
		}

		uploader.upload();
	}

	/**
	 * 采用uploadType方式，上报任务task，任务时间taskTime,
	 * 单位树根节点列表unitIDs，登录上级用户名和密码为username,password，
	 * 向上级服务器上报数据。
	 *
	 * @param uploadType         mail, ftp, webservice
	 * @param task               Task
	 * @param taskTime           TaskTime
	 * @param unitIDs            单位树根节点列表，如下载某个单位树数据，只要指定该单位树根节点即可
	 * @param username           登录上级服务器用户名
	 * @param password           对应的密码
	 * @throws UploadException
	 */
	public void upload( String uploadType, Task task, TaskTime taskTime,
						List unitIDs, String username, String password )
		throws UploadException
	{
		Uploader uploader = getUploader( uploadType );
		if( uploader == null )
		{
			String msg = "不支持的上报方式：" + uploadType;
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
			unitIDs.add( "8043301360" ); // 河北省机电设备总公司
			unitIDs.add( "GONGMAO007" ); // 河北工贸资产经营公司
			// unitIDs.add( "HBGZW58127" ); //58家汇总
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