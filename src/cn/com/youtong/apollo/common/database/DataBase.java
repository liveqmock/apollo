package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.util.*;
import java.io.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.xml.Cell;
import cn.com.youtong.apollo.task.xml.Row;
import cn.com.youtong.apollo.task.xml.Table;
import cn.com.youtong.apollo.task.xml.Task;

/**
 * 所有数据库父类，定义各种数据库应该提供的功能
 */
public abstract class DataBase
{
	/**
	 * JDBC数据类型值
	 */
	private int[] jdbcTypes =
		{
		0, java.sql.Types.NUMERIC, java.sql.Types.VARCHAR, java.sql.Types.BLOB, java.sql.Types.CLOB, java.sql.Types.DATE};

	/**
	 * 从任务设计文档中提取任务数据表的创建sql
	 * @param task 任务设计文档内容
	 * @return 任务数据表的创建sql集合
	 * @throws TaskException
	 */
	public Collection getDataTableCreateSqls(Task task)
		throws TaskException
	{
		LinkedList sqls = new LinkedList();
		for(int i = 0; i < task.getTableCount(); i++)
		{
			sqls.addAll(getCreateTableSqls(task.getTable(i), task.getID()));
		}

		sqls.add(getCreateAttachmentTableSql(task.getID()));

		return sqls;
	}

	/**
	 * 创建存放报表附件的表的sql
	 * @param taskID 任务id
	 * @return 创建存放报表附件的表的sql
	 */
	public abstract String getCreateAttachmentTableSql(String taskID);

	/**
	 * varchar类型值
	 * @return varchar类型值
	 */
	public int getVarcharType()
	{
		return Types.VARCHAR;
	}

	/**
	 * 得到单元格数据类型在JDBC中的类型值
	 * @param dataType 单元格数据类型常量, 常量定义在cn.com.youtong.apollo.task.CellDataType中
	 * @return JDBC类型值
	 */
	public int getJDBCType(int dataType)
	{
		return jdbcTypes[dataType];
	}

	/**
	 * 得到创建数据表的数值类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @param width 数值长度
	 * @param decimalWidth 数值小数部分的位数
	 * @return 创建数据表的数值类型字段的sql
	 */
	public String getNumberSql(String fieldName, int width, int decimalWidth)
	{
		//缺省实现，子类需要时重载
		return " " + fieldName + " decimal(" + width + "," + decimalWidth + ") ";
	}

	/**
	 * 得到创建数据表的日期时间类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @return 创建数据表的日期时间类型字段的sql
	 */
	public String getDateTimeSql(String fieldName)
	{
		//缺省实现，子类需要时重载
		return " " + fieldName + " datetime ";
	}

	/**
	 * 得到创建数据表的二进制类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @param width 字段长度
	 * @return 创建数据表的日期时间类型字段的sql
	 */
	public String getBinarySql(String fieldName, int width)
	{
		throw new UnsupportedOperationException();
		//缺省实现，子类需要时重载
//		return " " + fieldName + " varbinary(" + width + ") ";
	}

	/**
	 * 得到创建数据表的大数据块类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @return 创建数据表的日期时间类型字段的sql
	 */
	public String getBlockSql(String fieldName)
	{
		throw new UnsupportedOperationException();
		//缺省实现，子类需要时重载
//		return " " + fieldName + " ntext ";
	}

	/**
	 * 得到创建数据表的布尔类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @return 创建数据表的布尔类型字段的sql
	 */
	public String getBooleanSql(String fieldName)
	{
		//缺省实现，子类需要时重载
		return " " + fieldName + " int ";
	}

	/**
	 * 得到创建数据表的字符串类型字段的sql
	 * @param fieldName 要创建的字段名称
	 * @param width 字符串长度
	 * @return 创建数据表的字符串类型字段的sql
	 */
	public String getStringSql(String fieldName, int width)
	{
		//缺省实现，子类需要时重载
		return " " + fieldName + " varchar(" + width + ") ";
	}

	/**
	 * 根据数据库错误代码判断当前错误是否是“超出数据表的行的最大size”
	 * @param errorCode 错误代码
	 * @return 如果错误代码表示超出数据表的行的最大size，返回true；否则返回false
	 */
	public boolean isExceedMaxRowSizeCode(int errorCode)
	{
		//缺省实现，子类需要时重载
		return false;
	}

	/**
	 * 根据数据库错误代码判断当前错误是否是“超出数据表的最大字段数”
	 * @param errorCode 错误代码
	 * @return 如果错误代码表示超出数据表的最大字段数，返回true；否则返回false
	 */
	public boolean isExceedMaxColumnCode(int errorCode)
	{
		//缺省实现，子类需要时重载
		return false;
	}

	/**
	 * 计算创建数据库表的sql
	 * @param table 包含要数据库表创建信息的Table对象
	 * @param taskID 任务ID
	 * @return 创建数据库表的sql
	 * @throws TaskException
	 */
	private Collection getCreateTableSqls(Table table, String taskID)
		throws TaskException
	{

		Collection result = new LinkedList();
		//计算sql
		String sql = "";
		for(int i = 0; i < table.getRowCount(); i++)
		{
			Row row = table.getRow(i);
			// 生成浮动表的 Sql
			if(row.getFlag() == 1)
			{
				String sql2 = getBasicSql(row, taskID, table.getID());
				String tableName = NameGenerator.generateFloatDataTableName(taskID, table.getID(), row.getID());
				if(!sql2.equals(""))
				{
					sql2 = "CREATE TABLE " + tableName + " ( unitID varchar(100) NOT NULL, taskTimeID int NOT NULL," + sql2 + ")";
					result.add(sql2);
				}
			}
			else
			{
				if(!sql.equals("") && !getBasicSql(row, taskID, table.getID()).equals(""))
				{
					sql += ",";
				}
				sql += getBasicSql(row, taskID, table.getID());
			}
		}

		//生成　单位表的Sql
		if(table.getIsUnitMetaTable())
		{
			String tableName = NameGenerator.generateDataTableName(taskID, table.getID());
			sql = "CREATE TABLE " + tableName + " ( unitID varchar(100) NOT NULL" + ((sql.equals(""))?"":" , ") + sql + " , " + UnitMetaTable.P_PARENT + " varchar(100) , display int default 1, PRIMARY KEY (unitID)) ";
		}
		else
		{
			String tableName = NameGenerator.generateDataTableName(taskID, table.getID());
			sql = "CREATE TABLE " + tableName + " ( unitID varchar(100) NOT NULL, taskTimeID int NOT NULL" + ((sql.equals(""))?"":" , ") + sql + " , PRIMARY KEY (unitID, taskTimeID)) ";
		}
		result.add(sql);

		return result;
	}

	/**
	 * 得到创建数据表的sql的字段部分
	 * @param row Row对象
	 * @param taskID 任务id
	 * @param tableID 表id
	 * @return 创建数据表的sql的字段部分
	 * @throws TaskException
	 */
	private String getBasicSql(Row row, String taskID, String tableID)
		throws TaskException
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < row.getCellCount(); i++)
		{
			if(result.length() != 0)
			{
				result.append(",");
			}
			Cell cell = row.getCell(i);
			int type = cell.getDataType();

			if(type == NameGenerator.CELL_TYPE_NUMBER)
			{
				result.append(getNumberSql(cell.getDBFieldName(), 20, 5));
			}
			else if(type == NameGenerator.CELL_TYPE_STRING)
			{
				result.append(getStringSql(cell.getDBFieldName(), cell.getWidth()));
			}
			else if(type == NameGenerator.CELL_TYPE_BINARY)
			{
				result.append(getBinarySql(cell.getDBFieldName(), cell.getWidth()));
			}
			else if(type == NameGenerator.CELL_TYPE_BLOCK)
			{
				result.append(getBlockSql(cell.getDBFieldName()));
			}
			else if(type == NameGenerator.CELL_TYPE_DATETIME)
			{
				result.append(getDateTimeSql(cell.getDBFieldName()));
			}
			else
			{
				throw new TaskException("未定义的单元格类型: " + type);
			}
		}
		return result.toString();
	}

	/**
	 * 得到创建临时表的SQL语句
	 * @param tableName 临时表名称
	 * @param columnDefinitions 临时表子段定义部分
	 * @return 创建临时表的SQL语句
	 */
	public abstract String getTempTableCreateSql(String tableName, String columnDefinitions);

	/**
	 * 得到删除临时表的SQL语句
	 * @param tableName 临时表名称
	 * @return 删除临时表的SQL语句
	 */
	public abstract String getTempTableDropSql(String tableName);

	/**
	 * 产生一个临时表名字
	 * @return 临时表名字
	 */
	public String generateTempTableName()
	{
		return "tem_" + Util.generateRandom();
	}

	/**
	 * 更新Clob
	 * @param clob 要更新的clob
	 * @param srcReader 包含新clob数据的Reader
	 * @param updateSql 更新的sql， 其中包含一个参数,
	 * 格式如 update table_a set clob_a = ? where ...
	 * @param conn 数据库连接
	 * @throws SQLException
	 * @throws IOException
	 */
	abstract public void UpdateClob(Clob clob, Reader srcReader, String updateSql, Connection conn)
		throws SQLException, IOException;

    /**
     * 更新Blob
     * @param blob 要更新的blob
     * @param srcIn 包含新blob数据的InputStream
     * @param updateSql 更新的sql， 其中包含一个参数,
     * 格式如 update table_a set blob_a = ? where ...
     * @param conn 数据库连接
     * @throws SQLException
     * @throws IOException
     */
    abstract public void UpdateBlob(Blob blob, InputStream srcIn, String updateSql, Connection conn)
        throws SQLException, IOException;

	/**
	 * 更新Clob
	 * @param clob 要更新的clob
	 * @param content 新clob数据
	 * @param updateSql 更新的sql， 其中包含一个参数,
	 * 格式如 update table_a set clob_a = ? where ...
	 * @param conn 数据库连接
	 * @throws SQLException
	 * @throws IOException
	 */
	abstract public void UpdateClob(Clob clob, String content, String updateSql, Connection conn)
		throws SQLException, IOException;

}
