package cn.com.youtong.apollo.common.sql;

import java.sql.*;
import java.util.*;

import cn.com.youtong.apollo.common.database.DataBase;
import cn.com.youtong.apollo.services.*;

import net.sf.hibernate.*;
import org.apache.commons.logging.*;

/**
 * 创建临时表，向临时表插入数据，删除临时表等功能，在这里实现
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TempTableUtil
{
	private static Log log = LogFactory.getLog(TempTableUtil.class);


              /**
                 * 创建一个保存unitID的临时表
                 * @param unitIDs    插入临时表的unitID的遍历，不能为null
                 * @param session    Hibernate会话
                 * @return           表名称
                 * @throws SQLException     异常发生
                 */
                public static String createTempUnitIDTable(ArrayList unitIDs, Session session)
                        throws SQLException
                {
                        String tempTableName = null;

                        PreparedStatement pstmt = null;
                        PreparedStatement batchInsertPstmt = null;
                        try
                        {
                                // 创建临时表，放入unitID
                                DataBase db = Config.getCurrentDatabase();
                                tempTableName = db.generateTempTableName();
                                String createTempTableSQL = db.getTempTableCreateSql(tempTableName, " (UNITID VARCHAR(100)) ");
                                log.debug(createTempTableSQL);

                                String insertSQL = "INSERT INTO " + tempTableName + " VALUES (?)";
                                log.debug(insertSQL); ;

                                Connection con = session.connection();
                                Transaction trans = session.beginTransaction();

                                pstmt = con.prepareStatement(createTempTableSQL);
                                pstmt.execute();

                                batchInsertPstmt = con.prepareStatement(insertSQL);
                              for(int i=0;i<unitIDs.size();i++)
                                {
                                        String unitID = (String) unitIDs.get(i);
                                        batchInsertPstmt.setString(1, unitID);
                                        batchInsertPstmt.addBatch();
                                }
                                batchInsertPstmt.executeBatch();

                                trans.commit();
                        } catch (ConfigException ce) {
                                log.error("服务器配置异常", ce);
                                throw new SQLException("服务器配置异常" + ce);
                        }
                        catch(SQLException sqle)
                        {
                                log.error("数据库异常", sqle);
                                throw sqle;
                        }
                        catch(HibernateException he)
                        {
                                log.error("Hibernate出错", he);
                                throw new SQLException("Hiberate 异常" + he);
                        }
                        finally
                        {
                                SQLUtil.close(null, pstmt, null);
                                SQLUtil.close(null, batchInsertPstmt, null);
                        }

                        return tempTableName;
                }



	/**
	 * 创建一个保存unitID的临时表
	 * @param unitIDs    插入临时表的unitID的遍历，不能为null
	 * @param session    Hibernate会话
	 * @return           表名称
	 * @throws SQLException     异常发生
	 */
	public static String createTempUnitIDTable(Iterator unitIDs, Session session)
		throws SQLException
	{
		String tempTableName = null;

		PreparedStatement pstmt = null;
		PreparedStatement batchInsertPstmt = null;
		try
		{
			// 创建临时表，放入unitID
			DataBase db = Config.getCurrentDatabase();
			tempTableName = db.generateTempTableName();
			String createTempTableSQL = db.getTempTableCreateSql(tempTableName, " (UNITID VARCHAR(100)) ");
			log.debug(createTempTableSQL);

			String insertSQL = "INSERT INTO " + tempTableName + " VALUES (?)";
			log.debug(insertSQL); ;

			Connection con = session.connection();
			Transaction trans = session.beginTransaction();

			pstmt = con.prepareStatement(createTempTableSQL);
			pstmt.execute();

			batchInsertPstmt = con.prepareStatement(insertSQL);
			while(unitIDs.hasNext())
			{
				String unitID = (String) unitIDs.next();
				batchInsertPstmt.setString(1, unitID);
				batchInsertPstmt.addBatch();
			}
			batchInsertPstmt.executeBatch();

			trans.commit();
		} catch (ConfigException ce) {
			log.error("服务器配置异常", ce);
			throw new SQLException("服务器配置异常" + ce);
		}
		catch(SQLException sqle)
		{
			log.error("数据库异常", sqle);
			throw sqle;
		}
		catch(HibernateException he)
		{
			log.error("Hibernate出错", he);
			throw new SQLException("Hiberate 异常" + he);
		}
		finally
		{
			SQLUtil.close(null, pstmt, null);
			SQLUtil.close(null, batchInsertPstmt, null);
		}

		return tempTableName;
	}

	/**
	 * 删除数据库临时表
	 * @param tableName     临时表名称
	 * @param session       hibernate会话
	 */
	public static void dropTempTable(String tableName, Session session)
	{
		PreparedStatement ps = null;
		try
		{
			Connection con = session.connection();
			DataBase db = Config.getCurrentDatabase();
			String dropSQL = db.getTempTableDropSql(tableName);

			ps = con.prepareStatement(dropSQL);

			Transaction trans = session.beginTransaction();
			ps.execute();
			trans.commit();
		} catch (ConfigException ce) {
			log.error("服务器配置异常", ce);
		}
		catch(SQLException sqle)
		{
			log.error("数据库异常", sqle);
		}
		catch(HibernateException he)
		{
			log.error("Hibernate出错", he);
		}
		finally
		{
			SQLUtil.close(null, ps, null);
		}
	}
}