package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.io.*;
import cn.com.youtong.apollo.common.sql.*;

/**
 * Oracle���ݿ�
 */
public class Oracle extends DataBase
{
	/**
	 * �õ�������ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @param columnDefinitions ��ʱ���Ӷζ��岿��
	 * @return ������ʱ���SQL���
	 */
	public String getTempTableCreateSql(String tableName, String columnDefinitions)
	{
//		return "CREATE  GLOBAL  TEMPORARY  TABLE " + tableName + " " + columnDefinitions + " ON  COMMIT  DELETE  ROWS";
        return "CREATE  TABLE " + tableName + " " + columnDefinitions;

	}

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
	public void UpdateBlob(Blob blob, InputStream srcIn, String updateSql, Connection conn)
		throws SQLException, IOException
	{
		OutputStream out = getBlobOutputStream(blob);
		byte[] buf = new byte[1024];
		for (int count = 0; (count = srcIn.read(buf)) >= 0; )
		{
			out.write(buf, 0, count);
		}
		out.close();
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
	public void UpdateClob(Clob clob, Reader srcReader, String updateSql,
						   Connection conn) throws SQLException, IOException
	{
		Writer writer = getClobWriter(clob);
		char[] buf = new char[1024];
		for (int count = 0; (count = srcReader.read(buf)) >= 0; )
		{
			writer.write(buf, 0, count);
		}
		writer.close();
	}

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
	public void UpdateClob(Clob clob, String content, String updateSql,
						   Connection conn) throws SQLException, IOException
	{
		Writer writer = getClobWriter(clob);
		writer.write(content);
		writer.close();
	}

    /**
     * ������ű������ı��sql
     * @param taskID ����id
     * @return ������ű������ı��sql
     */
    public String getCreateAttachmentTableSql(String taskID)
    {
        return "CREATE TABLE " + NameGenerator.generateAttachmentTableName(taskID) + " ( unitID VARCHAR(100) NOT NULL , taskTimeID INT NOT NULL, name VARCHAR(100)  NOT NULL, content CLOB DEFAULT NULL, PRIMARY KEY  (unitID, taskTimeID))";
    }

    public String getDateTimeSql(String fieldName)
    {
        return " " + fieldName + " date ";
    }

	/**
	 * �õ�ɾ����ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @return ɾ����ʱ���SQL���
	 */
	public String getTempTableDropSql(String tableName)
	{
		return "DROP TABLE " + tableName;
	}

    /**
     * �õ�ָ��clob��writer
     * @param clob Clob����
     * @return ָ��clob��writer
     * @throws SQLException
     */
    private Writer getClobWriter(Clob clob) throws SQLException
    {
        return ((oracle.sql.CLOB)clob).getCharacterOutputStream();
    }

	/**
	 * �õ�ָ��blob��OutputStream
	 * @param blob Blob����
	 * @return ָ��clob��writer
	 * @throws SQLException
	 */
	private OutputStream getBlobOutputStream(Blob blob) throws SQLException
	{
		return ((oracle.sql.BLOB)blob).getBinaryOutputStream();
	}

}
