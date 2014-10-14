package cn.com.youtong.apollo.common.sql;

import java.sql.*;
import java.util.*;

import cn.com.youtong.apollo.common.database.DataBase;
import cn.com.youtong.apollo.services.*;

import net.sf.hibernate.*;
import org.apache.commons.logging.*;

/**
 * ������ʱ������ʱ��������ݣ�ɾ����ʱ��ȹ��ܣ�������ʵ��
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
                 * ����һ������unitID����ʱ��
                 * @param unitIDs    ������ʱ���unitID�ı���������Ϊnull
                 * @param session    Hibernate�Ự
                 * @return           ������
                 * @throws SQLException     �쳣����
                 */
                public static String createTempUnitIDTable(ArrayList unitIDs, Session session)
                        throws SQLException
                {
                        String tempTableName = null;

                        PreparedStatement pstmt = null;
                        PreparedStatement batchInsertPstmt = null;
                        try
                        {
                                // ������ʱ������unitID
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
                                log.error("�����������쳣", ce);
                                throw new SQLException("�����������쳣" + ce);
                        }
                        catch(SQLException sqle)
                        {
                                log.error("���ݿ��쳣", sqle);
                                throw sqle;
                        }
                        catch(HibernateException he)
                        {
                                log.error("Hibernate����", he);
                                throw new SQLException("Hiberate �쳣" + he);
                        }
                        finally
                        {
                                SQLUtil.close(null, pstmt, null);
                                SQLUtil.close(null, batchInsertPstmt, null);
                        }

                        return tempTableName;
                }



	/**
	 * ����һ������unitID����ʱ��
	 * @param unitIDs    ������ʱ���unitID�ı���������Ϊnull
	 * @param session    Hibernate�Ự
	 * @return           ������
	 * @throws SQLException     �쳣����
	 */
	public static String createTempUnitIDTable(Iterator unitIDs, Session session)
		throws SQLException
	{
		String tempTableName = null;

		PreparedStatement pstmt = null;
		PreparedStatement batchInsertPstmt = null;
		try
		{
			// ������ʱ������unitID
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
			log.error("�����������쳣", ce);
			throw new SQLException("�����������쳣" + ce);
		}
		catch(SQLException sqle)
		{
			log.error("���ݿ��쳣", sqle);
			throw sqle;
		}
		catch(HibernateException he)
		{
			log.error("Hibernate����", he);
			throw new SQLException("Hiberate �쳣" + he);
		}
		finally
		{
			SQLUtil.close(null, pstmt, null);
			SQLUtil.close(null, batchInsertPstmt, null);
		}

		return tempTableName;
	}

	/**
	 * ɾ�����ݿ���ʱ��
	 * @param tableName     ��ʱ������
	 * @param session       hibernate�Ự
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
			log.error("�����������쳣", ce);
		}
		catch(SQLException sqle)
		{
			log.error("���ݿ��쳣", sqle);
		}
		catch(HibernateException he)
		{
			log.error("Hibernate����", he);
		}
		finally
		{
			SQLUtil.close(null, ps, null);
		}
	}
}