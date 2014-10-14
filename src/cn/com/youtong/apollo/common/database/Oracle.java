package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.io.*;
import cn.com.youtong.apollo.common.sql.*;

/**
 * Oracle数据库
 */
public class Oracle extends DataBase
{
	/**
	 * 得到创建临时表的SQL语句
	 * @param tableName 临时表名称
	 * @param columnDefinitions 临时表子段定义部分
	 * @return 创建临时表的SQL语句
	 */
	public String getTempTableCreateSql(String tableName, String columnDefinitions)
	{
//		return "CREATE  GLOBAL  TEMPORARY  TABLE " + tableName + " " + columnDefinitions + " ON  COMMIT  DELETE  ROWS";
        return "CREATE  TABLE " + tableName + " " + columnDefinitions;

	}

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
	 * 更新Clob
	 * @param clob 要更新的clob
	 * @param srcReader 包含新clob数据的Reader
	 * @param updateSql 更新的sql， 其中包含一个参数,
	 * 格式如 update table_a set clob_a = ? where ...
	 * @param conn 数据库连接
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
	 * 更新Clob
	 * @param clob 要更新的clob
	 * @param content 新clob数据
	 * @param updateSql 更新的sql， 其中包含一个参数,
	 * 格式如 update table_a set clob_a = ? where ...
	 * @param conn 数据库连接
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
     * 创建存放报表附件的表的sql
     * @param taskID 任务id
     * @return 创建存放报表附件的表的sql
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
	 * 得到删除临时表的SQL语句
	 * @param tableName 临时表名称
	 * @return 删除临时表的SQL语句
	 */
	public String getTempTableDropSql(String tableName)
	{
		return "DROP TABLE " + tableName;
	}

    /**
     * 得到指定clob的writer
     * @param clob Clob对象
     * @return 指定clob的writer
     * @throws SQLException
     */
    private Writer getClobWriter(Clob clob) throws SQLException
    {
        return ((oracle.sql.CLOB)clob).getCharacterOutputStream();
    }

	/**
	 * 得到指定blob的OutputStream
	 * @param blob Blob对象
	 * @return 指定clob的writer
	 * @throws SQLException
	 */
	private OutputStream getBlobOutputStream(Blob blob) throws SQLException
	{
		return ((oracle.sql.BLOB)blob).getBinaryOutputStream();
	}

}
