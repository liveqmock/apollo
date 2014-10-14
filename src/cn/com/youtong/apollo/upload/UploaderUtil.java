package cn.com.youtong.apollo.upload;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.FactoryException;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.receiver.xml.UserInfo;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.webservice.*;

/**
 * 构造ftp和mail上报的文件。
 *
 * <p>
 * 文件格式为zip文件。zip里面文件含有数据文件，用户信息属性文件metainf.properties。
 * metainf.properties文件名在cn.com.yuotong.apollo.receiver.Constant.java里面定义。
 * </p>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */

class UploaderUtil
{
	private static Log log = LogFactory.getLog(UploaderUtil.class.getName());

	/**
	 * 构造上报文件，该文件是zip文件，其中包含用户信息文件，数据文件
	 * @param taskID    任务ID
	 * @return          上报文件
	 * @throws Exception
	 */
	File buildUploadFile(String taskID) throws Exception
	{
		log.debug("Begin build zip data file");
		ZipOutputStream out = null;
		File file = null;

		try
		{
			file = new File(tempFileName() + ".zip");
			out = new ZipOutputStream(new BufferedOutputStream(new
				FileOutputStream(file)));
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
			String username = Config.getString(
				"cn.com.youtong.apollo.upload.user");
			String password = Config.getString(
				"cn.com.youtong.apollo.upload.password");
			if (Util.isEmptyString(username))
			{
				throw new Exception(
					"配置文件cn.com.youtong.apollo.upload.user为空，请配置用户名和密码!");
			}

			// 写用户信息文件到zip
			UserInfo info = new UserInfo();
			info.setUsername(username);
			info.setPassword(password);
			info.setTaskid(taskID);

			out.putNextEntry(new ZipEntry(
				cn.com.youtong.apollo.receiver.Constant.METAINF_FILE_NAME));
			info.marshal(writer);

			// 输出数据文件到zip
			out.putNextEntry(new ZipEntry("data.xml"));
			outputData(taskID, username, writer);
		}
		catch (Exception ex)
		{
			// 删除临时文件
			Util.close(out);
			file.delete();

			log.error("", ex);
			throw ex;
		}

		// 没有使用finally。发生异常的时候，我们也关闭了out，同时删除了文件。
		Util.close(out);

		log.debug("End build zip data file");
		return file;
	}

	/**
	 * 用户username，导出任务taskID所有上个月数据
	 * @param taskID              任务标识
	 * @param username            用户名
	 * @param writer                 输出流
	 * @throws Exception
	 */
	private void outputData(String taskID, String username, Writer writer) throws
		Exception
	{
		// 输出数据文件到zip
		ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
			getInstance(
			ModelManagerFactory.class.getName());
		ModelManager modelMng = modelMngFcty.createModelManager(taskID);

		// 选择上月的任务时间
		Calendar cldr = Calendar.getInstance();
		cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);

		TaskManagerFactory taskMngFcty = (TaskManagerFactory) Factory.
			getInstance(
			TaskManagerFactory.class.getName());
		TaskManager taskMng = taskMngFcty.createTaskManager();
		Task task = taskMng.getTaskByID(taskID);
		TaskTime taskTime = task.getTaskTime(cldr.getTime());

		if (taskTime == null)
		{
			Date date = cldr.getTime();
			String s = Convertor.date2ShortString(date);
			String msg = "无法找到" + s + "对应的任务时间";
			log.error(msg);
			throw new Exception(msg);
		}

		// 权限控制列表acl
		UserManagerFactory userMngFcty = (UserManagerFactory) Factory.
			getInstance(
			UserManagerFactory.class.getName());
		UserManager userMng = userMngFcty.createUserManager();
		User user = userMng.getUserByName(username);
		UnitACL acl = modelMng.getUnitACL(user);

		modelMng.getDataExporter().exportAllData(taskTime, writer, acl);
	}

	File buildUploadFile(Task task, TaskTime taskTime,
						 List unitIDs, String username, String password) throws
		Exception
	{
		log.debug("Begin build zip data file");
		ZipOutputStream out = null;
		File file = null;

		try
		{
			file = new File(tempFileName() + ".zip");
			out = new ZipOutputStream(new BufferedOutputStream(new
				FileOutputStream(file)));

			// 写用户信息文件到zip
			UserInfo info = new UserInfo();
			info.setUsername(username);
			info.setPassword(password);
			info.setTaskid(task.id());

			StringWriter writer = new StringWriter();
			info.marshal(writer);

			out.putNextEntry(new ZipEntry(
				cn.com.youtong.apollo.receiver.Constant.METAINF_FILE_NAME));
			out.write(writer.getBuffer().toString().getBytes("gb2312"));

			// 输出数据文件到zip
			for (int i = 0, size = unitIDs.size(); i < size; i++)
			{
				String unitID = (String) unitIDs.get(i);
				out.putNextEntry(new ZipEntry("data_" + unitID + ".xml"));
				outputData(task, taskTime, unitID, username, writer);
			}
		}
		catch (Exception ex)
		{
			// 删除临时文件
			Util.close(out);
			file.delete();

			log.error("", ex);
			throw ex;
		}

		// 没有使用finally。发生异常的时候，我们也关闭了out，同时删除了文件。
		Util.close(out);

		log.debug("End build zip data file");
		return file;
	}

	private void outputData(Task task, TaskTime taskTime, String unitID,
							String username, Writer writer) throws Exception
	{
		// 输出数据文件到zip
		ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
			getInstance(
			ModelManagerFactory.class.getName());
		ModelManager modelMng = modelMngFcty.createModelManager(task.id());

		// 权限控制列表acl
		UserManagerFactory userMngFcty = (UserManagerFactory) Factory.
			getInstance(
			UserManagerFactory.class.getName());
		UserManager userMng = userMngFcty.createUserManager();
		User user = userMng.getUserByName(username);
		UnitACL acl = modelMng.getUnitACL(user);

		modelMng.getDataExporter().exportDataByTree(unitID, taskTime, writer, acl);
	}

	/**
	 * 导出的数据格式是先压缩，然后采用base64编码的字符串。
	 *
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	String buildUploadData(String taskID) throws Exception
	{
		String username = Config.getString(
			"cn.com.youtong.apollo.upload.user");
		String password = Config.getString(
			"cn.com.youtong.apollo.upload.password");
		if (Util.isEmptyString(username))
		{
			throw new Exception(
				"配置文件cn.com.youtong.apollo.upload.user为空，请配置用户名和密码!");
		}

		// 选择上月的任务时间
		Calendar cldr = Calendar.getInstance();
		cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH - 1));

		Date date = cldr.getTime();

		// 压缩
		String fileName = tempFileName();
		OutputStream out = null;
		try
		{
			out = new GZIPOutputStream(new BufferedOutputStream(new
				FileOutputStream(fileName)));
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
			outputData(taskID, username, writer);
		}
		finally
		{
			Util.close(out);
		}

		// BASE64编码
		byte[] content = readFromFile(fileName);
		String result = null;
		if (content == null || content.length == 0)
		{
		}
		else
		{
			result = new String(Util.encodeBase64(content));
		}

		// 删除临时文件
		try
		{
			new File(fileName).delete();
		}
		catch (Exception ex)
		{}

		return result;
	}

	String buildUploadData(Task task, TaskTime taskTime,
						   String unitID,
						   String username, String password) throws Exception
	{
		// 压缩
		String fileName = tempFileName();
		OutputStream out = null;
		try
		{
			out = new GZIPOutputStream(new BufferedOutputStream(new
				FileOutputStream(fileName)));
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
			outputData(task, taskTime, unitID, username, writer);
		}
		finally
		{
			Util.close(out);
		}

		// BASE64编码
		byte[] content = readFromFile(fileName);
		String result = null;
		if (content == null || content.length == 0)
		{
		}
		else
		{
			result = new String(Util.encodeBase64(content));
		}

		// 删除临时文件
		try
		{
			new File(fileName).delete();
		}
		catch (Exception ex)
		{}

		return result;
	}

	private String tempFileName()
	{
		String tempDir = Config.getString(
			"cn.com.youtong.apollo.data.export.tempdir");

		long currTimeInMs = System.currentTimeMillis();

		return tempDir + File.separator + currTimeInMs + "_" +
			Util.generateRandom();
	}

	private byte[] readFromFile(String fileName) throws IOException
	{
		InputStream in = null;
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			in = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buf = new byte[1024];
			for (int len = 0; (len = in.read(buf)) > 0; )
			{
				out.write(buf, 0, len);
			}

			return out.toByteArray();
		}
		catch (IOException ex)
		{
			throw ex;
		}
		finally
		{
			Util.close(in); ;
		}
	}
}