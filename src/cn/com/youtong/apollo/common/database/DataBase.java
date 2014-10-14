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
 * �������ݿ⸸�࣬����������ݿ�Ӧ���ṩ�Ĺ���
 */
public abstract class DataBase
{
	/**
	 * JDBC��������ֵ
	 */
	private int[] jdbcTypes =
		{
		0, java.sql.Types.NUMERIC, java.sql.Types.VARCHAR, java.sql.Types.BLOB, java.sql.Types.CLOB, java.sql.Types.DATE};

	/**
	 * ����������ĵ�����ȡ�������ݱ�Ĵ���sql
	 * @param task ��������ĵ�����
	 * @return �������ݱ�Ĵ���sql����
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
	 * ������ű������ı��sql
	 * @param taskID ����id
	 * @return ������ű������ı��sql
	 */
	public abstract String getCreateAttachmentTableSql(String taskID);

	/**
	 * varchar����ֵ
	 * @return varchar����ֵ
	 */
	public int getVarcharType()
	{
		return Types.VARCHAR;
	}

	/**
	 * �õ���Ԫ������������JDBC�е�����ֵ
	 * @param dataType ��Ԫ���������ͳ���, ����������cn.com.youtong.apollo.task.CellDataType��
	 * @return JDBC����ֵ
	 */
	public int getJDBCType(int dataType)
	{
		return jdbcTypes[dataType];
	}

	/**
	 * �õ��������ݱ����ֵ�����ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @param width ��ֵ����
	 * @param decimalWidth ��ֵС�����ֵ�λ��
	 * @return �������ݱ����ֵ�����ֶε�sql
	 */
	public String getNumberSql(String fieldName, int width, int decimalWidth)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return " " + fieldName + " decimal(" + width + "," + decimalWidth + ") ";
	}

	/**
	 * �õ��������ݱ������ʱ�������ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @return �������ݱ������ʱ�������ֶε�sql
	 */
	public String getDateTimeSql(String fieldName)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return " " + fieldName + " datetime ";
	}

	/**
	 * �õ��������ݱ�Ķ����������ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @param width �ֶγ���
	 * @return �������ݱ������ʱ�������ֶε�sql
	 */
	public String getBinarySql(String fieldName, int width)
	{
		throw new UnsupportedOperationException();
		//ȱʡʵ�֣�������Ҫʱ����
//		return " " + fieldName + " varbinary(" + width + ") ";
	}

	/**
	 * �õ��������ݱ�Ĵ����ݿ������ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @return �������ݱ������ʱ�������ֶε�sql
	 */
	public String getBlockSql(String fieldName)
	{
		throw new UnsupportedOperationException();
		//ȱʡʵ�֣�������Ҫʱ����
//		return " " + fieldName + " ntext ";
	}

	/**
	 * �õ��������ݱ�Ĳ��������ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @return �������ݱ�Ĳ��������ֶε�sql
	 */
	public String getBooleanSql(String fieldName)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return " " + fieldName + " int ";
	}

	/**
	 * �õ��������ݱ���ַ��������ֶε�sql
	 * @param fieldName Ҫ�������ֶ�����
	 * @param width �ַ�������
	 * @return �������ݱ���ַ��������ֶε�sql
	 */
	public String getStringSql(String fieldName, int width)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return " " + fieldName + " varchar(" + width + ") ";
	}

	/**
	 * �������ݿ��������жϵ�ǰ�����Ƿ��ǡ��������ݱ���е����size��
	 * @param errorCode �������
	 * @return �����������ʾ�������ݱ���е����size������true�����򷵻�false
	 */
	public boolean isExceedMaxRowSizeCode(int errorCode)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return false;
	}

	/**
	 * �������ݿ��������жϵ�ǰ�����Ƿ��ǡ��������ݱ������ֶ�����
	 * @param errorCode �������
	 * @return �����������ʾ�������ݱ������ֶ���������true�����򷵻�false
	 */
	public boolean isExceedMaxColumnCode(int errorCode)
	{
		//ȱʡʵ�֣�������Ҫʱ����
		return false;
	}

	/**
	 * ���㴴�����ݿ���sql
	 * @param table ����Ҫ���ݿ������Ϣ��Table����
	 * @param taskID ����ID
	 * @return �������ݿ���sql
	 * @throws TaskException
	 */
	private Collection getCreateTableSqls(Table table, String taskID)
		throws TaskException
	{

		Collection result = new LinkedList();
		//����sql
		String sql = "";
		for(int i = 0; i < table.getRowCount(); i++)
		{
			Row row = table.getRow(i);
			// ���ɸ������ Sql
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

		//���ɡ���λ���Sql
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
	 * �õ��������ݱ��sql���ֶβ���
	 * @param row Row����
	 * @param taskID ����id
	 * @param tableID ��id
	 * @return �������ݱ��sql���ֶβ���
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
				throw new TaskException("δ����ĵ�Ԫ������: " + type);
			}
		}
		return result.toString();
	}

	/**
	 * �õ�������ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @param columnDefinitions ��ʱ���Ӷζ��岿��
	 * @return ������ʱ���SQL���
	 */
	public abstract String getTempTableCreateSql(String tableName, String columnDefinitions);

	/**
	 * �õ�ɾ����ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @return ɾ����ʱ���SQL���
	 */
	public abstract String getTempTableDropSql(String tableName);

	/**
	 * ����һ����ʱ������
	 * @return ��ʱ������
	 */
	public String generateTempTableName()
	{
		return "tem_" + Util.generateRandom();
	}

	/**
	 * ����Clob
	 * @param clob Ҫ���µ�clob
	 * @param srcReader ������clob���ݵ�Reader
	 * @param updateSql ���µ�sql�� ���а���һ������,
	 * ��ʽ�� update table_a set clob_a = ? where ...
	 * @param conn ���ݿ�����
	 * @throws SQLException
	 * @throws IOException
	 */
	abstract public void UpdateClob(Clob clob, Reader srcReader, String updateSql, Connection conn)
		throws SQLException, IOException;

    /**
     * ����Blob
     * @param blob Ҫ���µ�blob
     * @param srcIn ������blob���ݵ�InputStream
     * @param updateSql ���µ�sql�� ���а���һ������,
     * ��ʽ�� update table_a set blob_a = ? where ...
     * @param conn ���ݿ�����
     * @throws SQLException
     * @throws IOException
     */
    abstract public void UpdateBlob(Blob blob, InputStream srcIn, String updateSql, Connection conn)
        throws SQLException, IOException;

	/**
	 * ����Clob
	 * @param clob Ҫ���µ�clob
	 * @param content ��clob����
	 * @param updateSql ���µ�sql�� ���а���һ������,
	 * ��ʽ�� update table_a set clob_a = ? where ...
	 * @param conn ���ݿ�����
	 * @throws SQLException
	 * @throws IOException
	 */
	abstract public void UpdateClob(Clob clob, String content, String updateSql, Connection conn)
		throws SQLException, IOException;

}
