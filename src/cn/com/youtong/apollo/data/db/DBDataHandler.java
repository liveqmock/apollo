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
 * ����Ȩ��,��������ʱ��������,������������.
 *
 * <p>
 * �����λ�Ķ�ȡ����,Ȼ��ʹ��DBDataStorer������λ����,�ﵽ�������ݵ�Ŀ��.
 * DBDataStorer������Ȩ�޺�����ʱ��.
 * </p>
 *
 * <p>
 * ����������Ϻ�,�������release�����Ƿ��������ݿ�������Դ.
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
	 * �������ݵ�����ʱ��
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
	
	private static final String FLAG_ELE_NAME = "flag";        //�ͻ����ϱ�ǰ����Ϊ�ݸ�ı�־
	private static final String FLAG_OPR_ATTR_NAME="operation";//�ͻ����ϱ�ǰ����Ϊ�ݸ�ı�־ֵ
	
	private static final String XML_ENCODING = "gb2312";
	private static final String XML_INDENT = "\t";
	private static final String NEW_LINE = "\n";

	private boolean ingoreTaskTime;
	private boolean suitableTaskTime;

	private List commitedUnitIDs;
	/**
	 * û�е���Ȩ�޵õ�λid�б�
	 * �����ǰ���ܵ����µ�λ����ô�µ�λ��prohibitUnitIDs������֣����������newUnitIDs����
	 */
	private List prohibitUnitIDs;
	/** �����������λ�б�
	 * �����ǰ���ܵ����µ�λ����ô�µ�λ��prohibitUnitIDs������֣����������newUnitIDs����
	 */
	private List newUnitIDs;
	private List overdueUnitIDs;

	/**
	 * ���ڴ�����δ�ύ��unitID
	 */
	private List underCommitedUnitIDs;
	/**
	 * ���ڴ�����δ�ύ���µ�λunitID
	 */
	private List underNewUnitIDs;

	/** ָʾ��ǰ�Ըõ�λ�Ƿ���дȨ�ޣ�������Ȩ��������λ����������ʱ���Ӱ�� */
	private boolean permit;

	/** ���浱ǰ��ȡ�ĵ�λ���� */
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
	 *  ��ÿ��unit��β������underCommitedUnitIDs��newUnitIDs�ĳ��ȣ��ж��Ƿ����commit������
	 *  �ﵽ�����е�ֵ������storer��commit;
	 *  tasktime����ʱ��Ҳ����commit;
	 *  commit�������������underCommitedUnitIDs��underNewUnitIDs��ӵ���Ӧ��list��ȥ��Ȼ����ա�
	 */
	public void startElement(String uri, String localName, String qName,
							 Attributes attrs) throws SAXException
	{
		String eName = qName; // ��ǰ��ȡԪ����
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
				throw new SAXException("�����뵱ǰ����ƥ��");
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
		String eName = qName; // ��ǰ��ȡԪ����
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
			// ��������ϱ�
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
	 * �����Ƿ�ǿ�е��������������
	 * @param ingore true��ʾǿ�е����������;false��ʾ�����룬�������ݱ�����
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
			// �Ѿ�����ĵ�λ
			commitedUnitIDs.addAll(underCommitedUnitIDs);
			commitedUnitIDs.addAll(underNewUnitIDs);
			newUnitIDs.addAll(underNewUnitIDs);

			// ���δ�ύ�ļ�¼
			underCommitedUnitIDs = new LinkedList();
			underNewUnitIDs = new LinkedList();
		}
		catch (ModelException ex)
		{
			throw new SAXException(ex);
		}
	}

	/**
	 * ��������ʱ���ǩ��
	 *
	 * <p>
	 * ���time��ʽ����ȷ���˳�������
	 * ���time��Ӧ������ʱ��û���ҵ����˳�������
	 * ����������½�DBDataStorer��Ȼ���ʼ��DBDataStorer��
	 * �����ʼ��ʧ�ܣ��˳�������
	 * </p>
	 *
	 * @param time           ʱ�� @see {@link Convertor#string2Date}
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
				buf.append("û���ҵ���Ӧ�������ϱ�ʱ��").append(time);
				TaskTime tempTaskTime = task.getTaskTime(now);
				if (tempTaskTime != null)
				{
					buf.append("\r\n��ǰʱ��").append(Convertor.date2String(now));
					buf.append("\r\n��ǰʱ��ƥ�������ʱ��:");
					buf.append(Convertor.date2String(tempTaskTime.getFromTime()));
					buf.append(" ~ ");
					buf.append(Convertor.date2String(tempTaskTime.getEndTime()));
					buf.append("\r\n��������:");
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
			log.error("����ȷʱ���ʽ" + time);
			throw new SAXException("����ȷʱ���ʽ" + time);
		}
		catch (TaskException ex)
		{
			log.error("��ȡ��������Ϣ����", ex);
			throw new SAXException("��ȡ��������Ϣ����", ex);
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

		//�Ƿ�������λ
		boolean isNewUnit = !treeMng.isUnitExist(unitID);
		if (isNewUnit)
		{
			//����Ƿ��ܵ����µ�λ
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
	 * �жϵ�ǰ�Ѿ������˶��ٸ���λ����,��������õ�
	 * cn.com.youtong.apollo.loaddata.units.percommit��ֵ(ȱʡֵΪ1)��ͬ,
	 * ���ύһ��.
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
			log.error("���浥λ���ݷ����쳣", ex);
			throw new SAXException("���浥λ���ݷ����쳣", ex);
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

	// �򱣴浱ǰ��λ���ݵ���,д
	private void write(String s) throws SAXException
	{
		oneUnitData.write(s);
	}

	/**
	 * �ͷſ�����������Դ
	 */
	public void release()
	{
		if (storer != null)
		{
			storer.release();
		}
	}

	/**
	 * ������{@link commit()}�����󣬾Ϳ��Եõ���������
	 * �����ǰ���ò��ܵ����µ�λ����ô�µ�λ��{@link LoadResult#getProhibitUnitIDs}������֣�
	 * ���������{@link LoadResult#getNewUnitIDs}���档
	 * �����ǰ�����ܵ����µ�λ����ô�µ�λ��{@link LoadResult#getNewUnitIDs}���棬
	 * ͬʱҲ��{@link LoadResult#getStoredUnitIDs}���档
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
	 * �������DBDataHandler��������taskΪ�գ���ô�����Ĺ����л��task��ֵ��
	 * �����Ϊ�գ����ǽ����Ĺ����У�taskID�ʹ����task��������׳������쳣��
	 * @return         Task
	 */
	public Task getTask()
	{
		return task;
	}
}