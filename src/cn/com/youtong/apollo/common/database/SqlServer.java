package cn.com.youtong.apollo.common.database;

import java.sql.*;
import java.io.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;

/**
 * SqlServer数据库
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
		throw new UnsupportedOperationException ();
	}

    /**
     * 得到创建临时表的SQL语句
     * @param tableName 临时表名称
     * @param columnDefinitions 临时表子段定义部分
     * @return 创建临时表的SQL语句
     */
    public String getTempTableCreateSql(String tableName, String columnDefinitions)
    {
		throw new UnsupportedOperationException ();
    }

    /**
     * 创建存放报表附件的表的sql
     * @param taskID 任务id
     * @return 创建存放报表附件的表的sql
     */
    public String getCreateAttachmentTableSql(String taskID)
    {
        throw new UnsupportedOperationException ();
    }

    /**
     * 产生一个临时表名字
     * @return 临时表名字
     */
    public String generateTempTableName()
    {
        return "#tem_" + Util.generateRandom();
    }
    /**
     * 得到删除临时表的SQL语句
     * @param tableName 临时表名称
     * @return 删除临时表的SQL语句
     */
    public String getTempTableDropSql(String tableName)
    {
        throw new UnsupportedOperationException ();
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
        throw new UnsupportedOperationException ();
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
        throw new UnsupportedOperationException ();
	}

}
