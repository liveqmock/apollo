/*
 * Created on 2003-11-19
 */
package cn.com.youtong.apollo.data.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.*;

import org.apache.commons.logging.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import net.sf.hibernate.*;

/**
 * ������ݿ��������������
 * <p>
 * ���������İ����������һ����һ����ķ�ʽ�������ݣ�Ч�ʺܵ͡�
 * ����ʵ��˵��������SAX�¼�ģ�ͽ���xml�ٶȺܿ죬CastorҲ���С�
 *
 * �������˼·�ǣ�
 * ����SAX�������ݰ���ͬʱ����������ݿ����������Ҫ�������ļ���
 * ������Ϻ󣬲������ݿⲻͬ���Ż���ʽ�����������ݡ�
 *
 * �쳣����
 * ���������DBModelManager��loadData��࣬����������쳣д����־�ļ���
 * �ó���Ŀͻ���ά�����ļ����Ƿ�ͨ��email֪ͨ������������ʽ��
 *
 * Note:
 * ���sql server���ݿ�Bulk insert����PreparedStatementԤ���룬Ȼ��addBatch��
 * ��ִ�п��Գɹ���
 *
 * @author wjb
 */
public class BatchDataLoader extends DefaultHandler
{

	private Log log = LogFactory.getLog(this.getClass());

	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String UNIT_ELE_NAME = "unit";
	private static final String TABLE_ELE_NAME = "tableModel";
	private static final String CELL_ELE_NAME = "cellModel";
	private static final String FLOAT_ROW_ELE_NAME = "floatRow";
	private static final String ROW_ELE_NAME = "rowModel";

	private boolean inTask;
	private boolean inTaskTime;
	private boolean inUnit;
	private boolean inTable;
	private boolean inFloatRow;
	private boolean inRow;
	private int index;

	/**
	 * (non-Javadoc)
	 *
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
		throws SAXException
	{

		String eName = qName; // Element name
		if(eName.equals(TASK_ELE_NAME))
		{
			// TODO error
			inTask = false;

		}
		else if(eName.equals(TASK_TIME_ELE_NAME))
		{
			/**  TODO else ifssssss*/
			if(!inTask)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inTaskTime = true;

		}
		else if(eName.equals(UNIT_ELE_NAME))
		{
			if(!inTaskTime)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inUnit = true;

		}
		else if(eName.equals(TABLE_ELE_NAME))
		{
			if(!inUnit)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inTable = true;

		}
		else if(eName.equals(CELL_ELE_NAME))
		{
			if(!inTable || (inTable && !(inFloatRow == inRow)))
			{
				throw new SAXException("��ʽ����ȷ");
			}

		}
		else if(eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if(!inTable)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inFloatRow = true;

		}
		else if(eName.equals(ROW_ELE_NAME))
		{
			if(!inFloatRow)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inRow = true;

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException
	{

		String eName = qName; // Element name
		if(eName.equals(TASK_ELE_NAME))
		{

			inTask = true;

		}
		else if(eName.equals(TASK_TIME_ELE_NAME))
		{

			if(!inTask)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inTaskTime = true;

		}
		else if(eName.equals(UNIT_ELE_NAME))
		{
			if(!inTaskTime)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inUnit = true;

		}
		else if(eName.equals(TABLE_ELE_NAME))
		{
			if(!inUnit)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inTable = true;

		}
		else if(eName.equals(CELL_ELE_NAME))
		{
			if(!inTable || (inTable && !(inFloatRow == inRow)))
			{
				throw new SAXException("��ʽ����ȷ");
			}

		}
		else if(eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if(!inTable)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inFloatRow = true;

		}
		else if(eName.equals(ROW_ELE_NAME))
		{
			if(!inFloatRow)
			{
				throw new SAXException("��ʽ����ȷ");
			}
			inRow = true;

		}

	}

	/**
	 *
	 */
	private void writeDB()
	{
		log.info("**************Write DB******************" + (++index));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException e)
		throws SAXException
	{
		// TODO Auto-generated method stub
		super.warning(e);
	}

	public static void main(String[] args)
	{
		DefaultHandler handler = new BatchDataLoader();

		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try
		{

			SAXParser saxParser = factory.newSAXParser();
			File f = new File("F:\\apollo\\test\\cn\\com\\youtong\\apollo\\data\\db\\data\\simpleTask2LargeData.xml");
			saxParser.parse(f, handler);

		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}

	}

	/**
	 * ���������Ѵ��ڵ����ݣ��ظ����汨���ʱ������ɾ��ԭ��������
	 *
	 * @param unitID
	 *            ��λ�����뱨�����͵�������(����)
	 * @param taskTimeID
	 *            ����ʱ��id������Ƿ������ôtaskTimeID����Ϊnull
	 * @param tableName
	 *            �������
	 * @param con
	 *            �������ӣ��˷���û�йر�
	 * @param isMetaTable
	 *            �ǲ��Ƿ����
	 */
	private void clearTableData(String unitID, int taskTimeID, String tableName, Session session, boolean isMetaTable)
		throws SQLException
	{

		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer("delete from ").append(tableName).append(" where unitID=").append("?");
		if(!isMetaTable)
		{
			sql.append(" and taskTimeID=").append("?");
		}

		try
		{
			Connection con = session.connection();
			ps = con.prepareStatement(sql.toString());

			ps.setString(1, unitID);
			if(!isMetaTable)
			{
				ps.setInt(2, taskTimeID);
			}

			ps.execute();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(HibernateException e)
		{
			throw new SQLException(e.getMessage());
		}
		finally
		{
			if(ps != null)
			{
				ps.close();
			}
		}

	}

	/**
	 * ƴ�ղ������sql��� ������û������sql������������һ���ֶ�ΪunitID��
	 * ���������������ֶ�unitID,taskTimeID�� ��Щ��ӵ��ֶβ���cellModels�ṩ�ģ����������ṩ
	 *
	 * @param tableName
	 *            ����ı���
	 * @param cellModels
	 *            ��Ҫ����ĵ�Ԫ��
	 * @param isUnitMetaTable
	 *            ����ı��ǲ����û������
	 * @return ƴ�պ��sql���
	 */
	private String composeInsertSQL(String unitID, int taskTimeID, String tableName, Vector names, Vector values, boolean isUnitMetaTable)
	{
		StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + "(");

		for(int i = 0, length = names.size(); i < length; i++)
		{
			String fieldName = (String) names.get(i);

			sql.append(fieldName).append(", ");
		}

		// ׷��unitID�ֶ�,������Ƿ����ҪtaskTimeID�ֶ�
		if(isUnitMetaTable)
		{
			sql.append("unitID) ");
		}
		else
		{
			sql.append("unitID, taskTimeID) ");
		}

		sql.append(" values (");
		for(int i = 0, length = names.size(); i < length; i++)
		{
			sql.append("'" + (String) values.get(i) + "', ");
		}

		// ׷��unitIDռλ��,������Ƿ����ҪtaskTimeIDռλ��
		if(isUnitMetaTable)
		{
			sql.append("'").append(unitID).append("')");
		}
		else
		{
			sql.append("'").append(unitID).append("', '").append(taskTimeID).append("')");
		}

		return sql.toString();
	}
}
