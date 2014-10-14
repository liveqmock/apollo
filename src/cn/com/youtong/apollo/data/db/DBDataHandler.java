package cn.com.youtong.apollo.data.db;

// java
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// apache
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// jdom
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

// apollo
import cn.com.youtong.apollo.common.Convertor;
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.data.LoadResult;
import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.UnitACL;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskException;
import cn.com.youtong.apollo.task.TaskTime;

import cn.com.youtong.apollo.analyse.db.*;

/**
 * 考虑权限,考虑任务时间的情况下,解析任务数据.
 *
 * <p>
 * 逐个单位的读取数据,然后使用DBDataStorer解析单位数据,达到导入数据的目的.
 * DBDataStorer不考虑权限和任务时间.
 * </p>
 *
 * <p>
 * 解析数据完毕后,必须调用release方法是否开启的数据库连接资源.
 * </p>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yutop</p>
 * @author wjb
 * @version 1.0
 */
class DBDataHandler
	extends DefaultHandler
{
	private Log log = LogFactory.getLog(this.getClass());
	private UnitACL acl;
	private Task task;
	private DBUnitTreeManager treeMng;

	private DBDataStorer storer;
	/**
	 * 导入数据的任务时间
	 */
	private TaskTime taskTime;

	private static final String UNIT_ELE_NAME = "unit";
	private static final String UNIT_ID_ATTR_NAME = "ID";
	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String TASK_ID_ATTR_NAME = "ID";
	private static final String TASK_TIME_ATTR_NAME = "taskTime";
	private static final String TABLE_ELE_NAME = "table";
	private static final String TABLE_ID_ATTR_NAME = "ID";
	private static final String CELL_ELE_NAME = "cell";
	private static final String FLOAT_ROW_ELE_NAME = "floatRow";
	private static final String ROW_ELE_NAME = "row";
	private static final String ATTACHMENT_ELE_NAME = "attachment";
	private static final String FIELD_ATTR_NAME = "field";
	private static final String VALUE_ATTR_NAME = "value";
	
	private static final String FLAG_ELE_NAME = "flag";        //客户端上报前保存为草稿的标志
	private static final String FLAG_OPR_ATTR_NAME="operation";//客户端上报前保存为草稿的标志值
	
	private static final String XML_ENCODING = "gb2312";
	private static final String XML_INDENT = "\t";
	private static final String NEW_LINE = "\n";

	private boolean ingoreTaskTime;
	private boolean suitableTaskTime;

	private List commitedUnitIDs;
	/**
	 * 没有导入权限得单位id列表。
	 * 如果当前不能导入新单位，那么新单位在prohibitUnitIDs里面出现，不会出现在newUnitIDs里面
	 */
	private List prohibitUnitIDs;
	/** 导入的新增单位列表。
	 * 如果当前不能导入新单位，那么新单位在prohibitUnitIDs里面出现，不会出现在newUnitIDs里面
	 */
	private List newUnitIDs;
	private List overdueUnitIDs;

	/**
	 * 正在处理，尚未提交的unitID
	 */
	private List underCommitedUnitIDs;
	/**
	 * 正在处理，尚未提交的新单位unitID
	 */
	private List underNewUnitIDs;

	/** 指示当前对该单位是否有写权限，或者有权限新增单位。考虑任务时间的影响 */
	private boolean permit;

	/** 保存当前读取的单位数据 */
	private StringWriter oneUnitData;

	private XMLOutputter output = new XMLOutputter(XML_INDENT, true,
		XML_ENCODING);
	
	private String unitID;
	private String flagOper;
	private boolean isUpload = true;
	public DBDataHandler(UnitACL acl, Task task, DBUnitTreeManager treeMng)
	{
		this.acl = acl;
		this.task = task;
		this.treeMng = treeMng;
	}

	/**
	 *  在每个unit结尾，根据underCommitedUnitIDs和newUnitIDs的长度，判断是否调用commit方法。
	 *  达到配置中的值，调用storer的commit;
	 *  tasktime结束时，也调用commit;
	 *  commit方法结束，添加underCommitedUnitIDs和underNewUnitIDs添加到相应的list中去。然后清空。
	 */
	public void startElement(String uri, String localName, String qName,
							 Attributes attrs) throws SAXException
	{
		String eName = qName; // 当前读取元素名
		if (Util.isEmptyString(eName))
		{
			eName = localName;
		}

		if (eName.equals(CELL_ELE_NAME))
		{
			if (permit)
			{
				String field = attrs.getValue(FIELD_ATTR_NAME);
				String value = attrs.getValue(VALUE_ATTR_NAME);
				doStartCell(field, value);
			}

			return;
		}
		else if (eName.equals(TABLE_ELE_NAME))
		{
			if (permit)
			{
				String tableID = attrs.getValue(TABLE_ID_ATTR_NAME);
				write(eName, attrs);
			}

			return;
		}
		else if (eName.equals(ROW_ELE_NAME))
		{
			if (permit)
			{
				write(eName, attrs);
			}
			return;
		}
		else if (eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if (permit)
			{
				write(eName, attrs);
			}
			return;
		}
		else if (eName.equals(UNIT_ELE_NAME))
		{
			unitID = attrs.getValue(UNIT_ID_ATTR_NAME);
			doStartUnit(unitID);
			return;
		}
		else if (eName.equals(TASK_TIME_ELE_NAME))
		{
			String time = attrs.getValue(TASK_TIME_ATTR_NAME);
			doStartTaskTime(time);
			return;
		}
		else if (eName.equals(TASK_ELE_NAME))
		{
			String taskID = attrs.getValue(TASK_ID_ATTR_NAME);
			if (task == null || !taskID.equals(task.id()))
			{
				throw new SAXException("数据与当前任务不匹配");
			}

			return;
		}
		else if (eName.equals(ATTACHMENT_ELE_NAME))
		{
			if (permit)
			{
				write(eName, attrs);
			}
			return;
		}
		else if(eName.equalsIgnoreCase(FLAG_ELE_NAME))
		{
			flagOper=attrs.getValue(FLAG_OPR_ATTR_NAME);
			if(flagOper.equalsIgnoreCase("save")) isUpload=false;
			write(eName,attrs);
		}
	}

	public void endElement(String uri, String localName, String qName) throws
		SAXException
	{
		String eName = qName; // 当前读取元素名
		if (Util.isEmptyString(eName))
		{
			eName = localName;
		}

		if (eName.equals(CELL_ELE_NAME))
		{
			return;
		}
		else if (eName.equals(TABLE_ELE_NAME))
		{
			if (permit)
			{
				write("</" + TABLE_ELE_NAME + ">" + NEW_LINE);
			}
			return;
		}
		else if (eName.equals(ROW_ELE_NAME))
		{
			if (permit)
			{
				write("</" + ROW_ELE_NAME + ">" + NEW_LINE);
			}
			return;
		}
		else if (eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if (permit)
			{
				write("</" + FLOAT_ROW_ELE_NAME + ">" + NEW_LINE);
			}
			return;
		}
		else if (eName.equals(UNIT_ELE_NAME))
		{
			if (permit)
			{
				write("</" + UNIT_ELE_NAME + ">" + NEW_LINE);
				doEndUnit();
			}
			permit = false;
			return;
		}
		else if (eName.equals(TASK_TIME_ELE_NAME))
		{
			// 如果可以上报
			if (suitableTaskTime || ingoreTaskTime)
			{
				commit();
			}

			return;
		}
		else if (eName.equals(TASK_ELE_NAME))
		{
			return;
		}
		else if (eName.equals(ATTACHMENT_ELE_NAME))
		{
			if (permit)
			{
				write("</" + ATTACHMENT_ELE_NAME + ">" + NEW_LINE);
			}
			return;
		}
		else if(eName.equalsIgnoreCase(this.FLAG_ELE_NAME))
			if(permit)
			{
				write("</"+FLAG_ELE_NAME+">"+NEW_LINE);
			}
		    return;
	}

	public void characters(char[] ch, int start, int length) throws
		SAXException
	{
		if (permit)
		{
			String s = new String(ch, start, length);
			write(s);
			write(NEW_LINE);
		}
	}

	/**
	 * 设置是否强行导入过期任务数据
	 * @param ingore true表示强行导入过期数据;false表示不导入，过期数据被忽略
	 */
	public void setIngoreTaskTime(boolean ingore)
	{
		this.ingoreTaskTime = ingore;
	}

	public boolean isIngoreTaskTime()
	{
		return this.ingoreTaskTime;
	}

	private void commit() throws SAXException
	{
		try
		{
			storer.commit();
			// 已经导入的单位
			commitedUnitIDs.addAll(underCommitedUnitIDs);
			commitedUnitIDs.addAll(underNewUnitIDs);
			newUnitIDs.addAll(underNewUnitIDs);

			// 清空未提交的记录
			underCommitedUnitIDs = new LinkedList();
			underNewUnitIDs = new LinkedList();
		}
		catch (ModelException ex)
		{
			throw new SAXException(ex);
		}
	}

	/**
	 * 处理任务时间标签。
	 *
	 * <p>
	 * 如果time格式不正确，退出解析；
	 * 如果time对应的任务时间没有找到，退出解析；
	 * 如果正常，新建DBDataStorer，然后初始化DBDataStorer；
	 * 如果初始化失败，退出解析。
	 * </p>
	 *
	 * @param time           时间 @see {@link Convertor#string2Date}
	 * @throws SAXException
	 */
	private void doStartTaskTime(String time) throws SAXException
	{
		prohibitUnitIDs = new LinkedList();
		newUnitIDs = new LinkedList();
		commitedUnitIDs = new LinkedList();
		overdueUnitIDs = new LinkedList();

		underNewUnitIDs = new LinkedList();
		underCommitedUnitIDs = new LinkedList();

		java.util.Date now = new java.util.Date();
		try
		{
			java.util.Date date = Convertor.string2Date(time);
			taskTime = task.getTaskTime(date);

			if (taskTime == null)
			{
				StringBuffer buf = new StringBuffer();
				buf.append("没有找到对应的任务上报时间").append(time);
				TaskTime tempTaskTime = task.getTaskTime(now);
				if (tempTaskTime != null)
				{
					buf.append("\r\n当前时间").append(Convertor.date2String(now));
					buf.append("\r\n当前时间匹配的任务时间:");
					buf.append(Convertor.date2String(tempTaskTime.getFromTime()));
					buf.append(" ~ ");
					buf.append(Convertor.date2String(tempTaskTime.getEndTime()));
					buf.append("\r\n导入期限:");
					buf.append(Convertor.date2String(tempTaskTime.
						getSubmitFromTime()));
					buf.append(" ~ ");
					buf.append(Convertor.date2String(tempTaskTime.
						getSubmitEndTime()));
				}
				String msg = buf.toString();

				log.info(msg);
				throw new SAXException(msg);
			}
		}
		catch (ParseException ex)
		{
			log.error("不正确时间格式" + time);
			throw new SAXException("不正确时间格式" + time);
		}
		catch (TaskException ex)
		{
			log.error("读取任务定义信息出错", ex);
			throw new SAXException("读取任务定义信息出错", ex);
		}

		if (now.after(taskTime.getSubmitFromTime())
			&& now.before(taskTime.getSubmitEndTime()))
		{
			suitableTaskTime = true;
		}
		else
		{
			suitableTaskTime = false;
		}

		if (suitableTaskTime || ingoreTaskTime)
		{
			try
			{
				storer = new DBDataStorer(task, taskTime);
				Map config = new HashMap();
				config.put(treeMng.getClass().getName(), treeMng);
				storer.init(config);
			}
			catch (ModelException ex)
			{
				throw new SAXException(ex);
			}
		}
	}

	private void doStartCell(String field, String value) throws SAXException
	{
		try
		{
			Element element = new Element(CELL_ELE_NAME);
			element.setAttribute(FIELD_ATTR_NAME, field);
			element.setAttribute(VALUE_ATTR_NAME, value);
			//xmlOut.write(currentIndent.getBytes());
			output.output(element, oneUnitData);
			oneUnitData.write(this.NEW_LINE);
		}
		catch (IOException ex)
		{
			throw new SAXException(ex);
		}
	}

	private void doStartUnit(String unitID) throws SAXException
	{
		if (!suitableTaskTime && !ingoreTaskTime)
		{
			overdueUnitIDs.add(unitID);
			permit = false;
			return;
		}

		//是否新增单位
		boolean isNewUnit = !treeMng.isUnitExist(unitID);
		if (isNewUnit)
		{
			//检查是否能导入新单位
			permit = Config.getBoolean(
				"cn.com.youtong.apollo.data.import.loadnewunit");
			underNewUnitIDs.add(unitID);
			oneUnitData = new StringWriter();
			write("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n");
			write("<" + UNIT_ELE_NAME + " " + UNIT_ID_ATTR_NAME + "=\"");
			write(unitID);
			write("\">" + NEW_LINE);
		}
		else
		{
			permit = acl.isWritable(unitID);
			if (permit)
			{
				underCommitedUnitIDs.add(unitID);
				oneUnitData = new StringWriter();
				write("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n");
				write("<" + UNIT_ELE_NAME + " " + UNIT_ID_ATTR_NAME + "=\"");
				write(unitID);
				write("\">" + NEW_LINE);
			}
			else
			{
				prohibitUnitIDs.add(unitID);
			}
		}
	}
	

	/**
	 * 判断当前已经解析了多少个单位数据,如果和设置的
	 * cn.com.youtong.apollo.loaddata.units.percommit的值(缺省值为1)相同,
	 * 则提交一次.
	 * @throws SAXException
	 */
	private void doEndUnit() throws SAXException
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String content =oneUnitData.toString();
			ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes("gb2312"));

			String cnt =DBAnalyseHelper.queryDraft(unitID, getTask().id(), taskTime.getTaskTimeID().intValue());
			if(cnt!=null) DBAnalyseHelper.delDraft(unitID, getTask().id(), taskTime.getTaskTimeID().intValue());
			if(isUpload){
				storer.store(in);
			}else{
				content=content.substring("<?xml version\"1.0\" encoding=\"gb2312\"?>".length()+1);
				content="<?xml version=\"1.0\" encoding=\"gb2312\"?><taskModel ID=\""+task.id()+"\"><taskTime taskTime=\""+sdf.format(taskTime.getSubmitFromTime())+"T00:00:00+08:00\">"+content;
				content+="</taskTime></taskModel>";
				DBAnalyseHelper.createDraft(unitID, getTask().id(), taskTime.getTaskTimeID().intValue(), content);
				prohibitUnitIDs.clear();
				newUnitIDs.clear();
				commitedUnitIDs.clear();
				overdueUnitIDs.clear();

				underNewUnitIDs.clear();
				underCommitedUnitIDs.clear();
			}
			in.close();
			oneUnitData.close();
		}
		catch (Exception ex)
		{
			log.error("保存单位数据发生异常", ex);
			throw new SAXException("保存单位数据发生异常", ex);
		}

		int underUnitIDs = underCommitedUnitIDs.size() + underNewUnitIDs.size();
		if (underUnitIDs
			==
			Config.getInt("cn.com.youtong.apollo.loaddata.units.percommit", 1))
		{
			commit();
		}
	}

	private void write(String eName, Attributes attrs) throws SAXException
	{
		write("<");
		write(eName);
		for (int i = 0, size = attrs.getLength(); i < size; i++)
		{
			String field = attrs.getQName(i);
			if (Util.isEmptyString(field))
			{
				field = attrs.getLocalName(i);

			}
			String value = attrs.getValue(i);
			write(" ");
			write(field);
			write("=\"");
			write(value);
			write("\"");
		}
		write(">" + NEW_LINE);
	}

	// 向保存当前单位数据的流,写
	private void write(String s) throws SAXException
	{
		oneUnitData.write(s);
	}

	/**
	 * 释放开启的数据资源
	 */
	public void release()
	{
		if (storer != null)
		{
			storer.release();
		}
	}

	/**
	 * 调用了{@link commit()}方法后，就可以得到导入结果。
	 * 如果当前设置不能导入新单位，那么新单位在{@link LoadResult#getProhibitUnitIDs}里面出现，
	 * 不会出现在{@link LoadResult#getNewUnitIDs}里面。
	 * 如果当前设置能导入新单位，那么新单位在{@link LoadResult#getNewUnitIDs}里面，
	 * 同时也在{@link LoadResult#getStoredUnitIDs}里面。
	 * @return   LoadResult
	 */
	public LoadResult getLoadResult()
	{
		LoadResult result = new LoadResult(taskTime,
										   commitedUnitIDs, newUnitIDs,
										   overdueUnitIDs, prohibitUnitIDs);
		return result;
	}

	/**
	 * 如果传入DBDataHandler构造器的task为空，那么解析的过程中会给task赋值。
	 * 如果不为空，但是解析的过程中，taskID和传入的task不相符，抛出解析异常。
	 * @return         Task
	 */
	public Task getTask()
	{
		return task;
	}
}