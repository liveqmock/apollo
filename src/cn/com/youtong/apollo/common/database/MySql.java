package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.io.*;
import cn.com.youtong.apollo.common.sql.*;

/**
 * MySql���ݿ�
 */
public class MySql
	extends DataBase
{
	/**
	 * �õ�������ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @param columnDefinitions ��ʱ���Ӷζ��岿��
	 * @return ������ʱ���SQL���
	 */
	public String getTempTableCreateSql(String tableName,
										String columnDefinitions)
	{
		return "CREATE TEMPORARY TABLE " + tableName + " " + columnDefinitions;
	}

	/**
	 * ������ű������ı��sql
	 * @param taskID ����id
	 * @return ������ű������ı��sql
	 */
	public String getCreateAttachmentTableSql(String taskID)
	{
		return "CREATE TABLE " +
			NameGenerator.generateAttachmentTableName(taskID) + " (ID int NOT NULL,unitID VARCHAR(100) NOT NULL , taskTimeID INT NOT NULL,filetype VARCHAR(100),name VARCHAR(100)  NOT NULL, content LONGTEXT DEFAULT NULL, PRIMARY KEY  (ID))";
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
		PreparedStatement pst = null;
		try
		{
			pst = conn.prepareStatement(updateSql);

			//update blob
			OutputStream out = getBlobOutputStream(blob);
			byte[] buf = new byte[1024];
			for (int count = 0; (count = srcIn.read(buf)) >= 0; )
			{
				out.write(buf, 0, count);
			}
			out.close();

			//update database
			pst.setBlob(1, blob);
			pst.execute();
		}
		finally
		{
			if (pst != null)
			{
				pst.close();
			}
		}
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
		PreparedStatement pst = null;
		try
		{
			pst = conn.prepareStatement(updateSql);

			//update clob
			Writer writer = getClobWriter(clob);
			char[] buf = new char[1024];
			for (int count = 0; (count = srcReader.read(buf)) >= 0; )
			{
				writer.write(buf, 0, count);
			}
			writer.close();

			//update database
			pst.setClob(1, clob);
			pst.execute();
		}
		finally
		{
			if (pst != null)
			{
				pst.close();
			}
		}

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
		PreparedStatement pst = null;
		try
		{
			pst = conn.prepareStatement(updateSql);

			//update clob
			Writer writer = getClobWriter(clob);
			writer.write(content);
			writer.close();

			//update database
			pst.setClob(1, clob);
			pst.execute();
		}
		finally
		{
			if (pst != null)
			{
				pst.close();
			}
		}
	}

	/**
	 * �õ�ɾ����ʱ���SQL���
	 * @param tableName ��ʱ������
	 * @return ɾ����ʱ���SQL���
	 */
	public String getTempTableDropSql(String tableName)
	{
		return "DROP TEMPORARY TABLE " + tableName;
	}

	/**
	 * �õ�ָ��clob��writer
	 * @param clob Clob����
	 * @return ָ��clob��writer
	 * @throws SQLException
	 */
	private Writer getClobWriter(Clob clob) throws SQLException
	{
		return clob.setCharacterStream(1);
	}

	/**
	 * �õ�ָ��blob��OutputStream
	 * @param blob Blob����
	 * @return ָ��clob��writer
	 * @throws SQLException
	 */
	private OutputStream getBlobOutputStream(Blob blob) throws SQLException
	{
		return blob.setBinaryStream(1);
	}

}
