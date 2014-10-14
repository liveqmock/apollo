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
 * 针对数据库的批量导入数据
 * <p>
 * 大数据量的包，如果采用一个表一个表的方式导入数据，效率很低。
 * 程序实践说明，采用SAX事件模型解析xml速度很快，Castor也还行。
 *
 * 整个设计思路是：
 * 利用SAX解析数据包，同时生成针对数据库的批处理需要的数据文件。
 * 解析完毕后，采用数据库不同的优化方式批量导入数据。
 *
 * 异常处理：
 * 基本情况和DBModelManager的loadData差不多，但是这里的异常写入日志文件，
 * 该程序的客户端维护该文件，是否通过email通知，还是其他方式。
 *
 * Note:
 * 针对sql server数据库Bulk insert采用PreparedStatement预编译，然后addBatch，
 * 再执行可以成功。
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
				throw new SAXException("格式不正确");
			}
			inTaskTime = true;

		}
		else if(eName.equals(UNIT_ELE_NAME))
		{
			if(!inTaskTime)
			{
				throw new SAXException("格式不正确");
			}
			inUnit = true;

		}
		else if(eName.equals(TABLE_ELE_NAME))
		{
			if(!inUnit)
			{
				throw new SAXException("格式不正确");
			}
			inTable = true;

		}
		else if(eName.equals(CELL_ELE_NAME))
		{
			if(!inTable || (inTable && !(inFloatRow == inRow)))
			{
				throw new SAXException("格式不正确");
			}

		}
		else if(eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if(!inTable)
			{
				throw new SAXException("格式不正确");
			}
			inFloatRow = true;

		}
		else if(eName.equals(ROW_ELE_NAME))
		{
			if(!inFloatRow)
			{
				throw new SAXException("格式不正确");
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
				throw new SAXException("格式不正确");
			}
			inTaskTime = true;

		}
		else if(eName.equals(UNIT_ELE_NAME))
		{
			if(!inTaskTime)
			{
				throw new SAXException("格式不正确");
			}
			inUnit = true;

		}
		else if(eName.equals(TABLE_ELE_NAME))
		{
			if(!inUnit)
			{
				throw new SAXException("格式不正确");
			}
			inTable = true;

		}
		else if(eName.equals(CELL_ELE_NAME))
		{
			if(!inTable || (inTable && !(inFloatRow == inRow)))
			{
				throw new SAXException("格式不正确");
			}

		}
		else if(eName.equals(FLOAT_ROW_ELE_NAME))
		{
			if(!inTable)
			{
				throw new SAXException("格式不正确");
			}
			inFloatRow = true;

		}
		else if(eName.equals(ROW_ELE_NAME))
		{
			if(!inFloatRow)
			{
				throw new SAXException("格式不正确");
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
	 * 清除表格中已存在的数据，重复保存报表的时候，首先删除原来的数据
	 *
	 * @param unitID
	 *            单位代码与报表类型的联合体(主键)
	 * @param taskTimeID
	 *            任务时间id。如果是封面表，那么taskTimeID可以为null
	 * @param tableName
	 *            表格名称
	 * @param con
	 *            数据连接，此方法没有关闭
	 * @param isMetaTable
	 *            是不是封面表
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
	 * 拼凑插入表格的sql语句 如果是用户封面表，sql语句的最后添加了一个字段为unitID；
	 * 否则，最后添加两个字段unitID,taskTimeID。 这些添加的字段不是cellModels提供的，而且无需提供
	 *
	 * @param tableName
	 *            插入的表名
	 * @param cellModels
	 *            将要插入的单元格
	 * @param isUnitMetaTable
	 *            插入的表是不是用户封面表
	 * @return 拼凑后的sql语句
	 */
	private String composeInsertSQL(String unitID, int taskTimeID, String tableName, Vector names, Vector values, boolean isUnitMetaTable)
	{
		StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + "(");

		for(int i = 0, length = names.size(); i < length; i++)
		{
			String fieldName = (String) names.get(i);

			sql.append(fieldName).append(", ");
		}

		// 追加unitID字段,如果不是封面表还要taskTimeID字段
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

		// 追加unitID占位符,如果不是封面表还要taskTimeID占位符
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
