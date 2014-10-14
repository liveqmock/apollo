package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.io.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;

/**
 * SqlServer���ݿ�
 */
public class SqlServer extends DataBase
{
    public boolean isExceedMaxRowSizeCode(int errorCode)
    {
        return (errorCode == 1701);
    }

    public boolean isExceedMaxColumnCode(int errorCode)
    {
        return (errorCode == 1702);
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
		throw new UnsupportedOperationException ();
	}

    /**
     * �õ�������ʱ���SQL���
     * @param tableName ��ʱ������
     * @param columnDefinitions ��ʱ���Ӷζ��岿��
     * @return ������ʱ���SQL���
     */
    public String getTempTableCreateSql(String tableName, String columnDefinitions)
    {
		throw new UnsupportedOperationException ();
    }

    /**
     * ������ű������ı��sql
     * @param taskID ����id
     * @return ������ű������ı��sql
     */
    public String getCreateAttachmentTableSql(String taskID)
    {
        throw new UnsupportedOperationException ();
    }

    /**
     * ����һ����ʱ������
     * @return ��ʱ������
     */
    public String generateTempTableName()
    {
        return "#tem_" + Util.generateRandom();
    }
    /**
     * �õ�ɾ����ʱ���SQL���
     * @param tableName ��ʱ������
     * @return ɾ����ʱ���SQL���
     */
    public String getTempTableDropSql(String tableName)
    {
        throw new UnsupportedOperationException ();
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
        throw new UnsupportedOperationException ();
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
        throw new UnsupportedOperationException ();
	}

}
