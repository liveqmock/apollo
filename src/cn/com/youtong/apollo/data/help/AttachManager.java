package cn.com.youtong.apollo.data.help;

import java.util.*;
import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;
import cn.com.youtong.apollo.common.Warning;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import java.sql.*;
import java.io.*;

public class AttachManager {

  /**
   *
   * @param attachModel
   */
  public static void insertAttach(AttachModel attachModel) throws Warning {
    String tableName=attachModel.getTableName();
    String insertSql = "insert "+tableName +" (ID,unitID,taskTimeID,name,fileType,content)values(?,?,?,?,?,?)";

    String selectSql = "SELECT content  FROM "+tableName+" WHERE ID=?";

    String updateSql = "update  " +tableName+" set  content=?  where ID=?";

    Session session = null;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement preStat = null;

    StringWriter attachmentWriter;
    Clob attachmentClob = null;

    try {
      Integer id = HibernateUtil.getNextID(tableName);
      attachModel.setID(id);

      session = HibernateUtil.openSession();
      conn = session.connection();
      preStat = conn.prepareStatement(insertSql);
      preStat.setInt(1, attachModel.getID().intValue());
      preStat.setString(2, attachModel.getUnitID());
      preStat.setString(3, attachModel.getTaskTimeID());
      preStat.setString(4, attachModel.getFileName());
      preStat.setString(5, attachModel.getFileType());
      preStat.setString(6, "");
      preStat.execute();
      preStat.close();

      preStat = conn.prepareStatement(selectSql);
      preStat.setInt(1, attachModel.getID().intValue());
      rs = preStat.executeQuery();
      if (rs.next()) {
        attachmentClob = rs.getClob(1);
      }
      rs.close();
      rs = null;
      preStat.close();

      preStat = conn.prepareStatement(updateSql);
      //update clob
      Writer writer = getClobWriter(attachmentClob);
      writer.write(attachModel.getContent());
      writer.close();

      //update database
      preStat.setClob(1, attachmentClob);
      preStat.setInt(2, attachModel.getID().intValue());
      preStat.execute();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      HibernateUtil.close(rs, preStat);
      HibernateUtil.close(session);
    }

  }

  /**
   * 删除
   * @param tmpAttachID
   * @throws Warning
   */
  public static void deleteAttach(String attachID,String tableName) throws Warning {
    String deleteSQL = "delete from " +tableName +" where ID='" +
        attachID + "'";
    try {
      HibernateUtil.executeSQL(deleteSQL);
    }
    catch (Exception ex) {
      throw new Warning("删除附件有误", ex);
    }

  }

  /**
   *
   * @param attachtype
   * @param attachtypeID
   * @return
   */
  public static ArrayList getAttachList(String unitID, String taskTimeID,String tableName) {
    ArrayList attachList = new ArrayList();

    Session session = null;
    Connection conn = null;
    ResultSet rs = null;
    Statement stat = null;
    int totalCount = 0;
    try {
      String sql =
          "select ID,unitID,taskTimeID,name,fileType from "+ tableName +" where  unitID='" +
          unitID + "' and taskTimeID='" + taskTimeID + "'";

      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      stat = conn.createStatement();
      rs = stat.executeQuery(sql);
      while (rs.next()) {
        AttachModel attachModel = new AttachModel();
        attachModel.setID(new Integer(rs.getInt("ID")));
        attachModel.setFileName(rs.getString("name"));
        attachModel.setFileType(rs.getString("fileType"));
        attachList.add(attachModel);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      //throw new Warning("查询有误: " + ex.getMessage());
    }
    finally {
      HibernateUtil.close(rs, stat);
      HibernateUtil.close(session);
    }

    return attachList;
  }



  /**
   * 显示附件
   * @param ID
   * @return
   */
  public static AttachModel showAttach(String ID,String tableName) {
    AttachModel attachModel = null;
    Session session = null;
    Connection conn = null;
    ResultSet rs = null;
    Statement stat = null;
    int totalCount = 0;
    try {
      String sql =
          "select * from "+tableName+" where  ID='" +
          ID + "'";

      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      stat = conn.createStatement();
      rs = stat.executeQuery(sql);
      if (rs.next()) {
        attachModel = new AttachModel();
        attachModel.setID(new Integer(rs.getInt("ID")));
        attachModel.setFileName(rs.getString("name"));
        attachModel.setFileType(rs.getString("fileType"));
        Clob clob = rs.getClob("content");
        attachModel.setContent(Clob2String(clob));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      //throw new Warning("查询有误: " + ex.getMessage());
    }
    finally {
      HibernateUtil.close(rs, stat);
      HibernateUtil.close(session);
    }
    return attachModel;
  }

  /**
   * 得到指定blob的OutputStream
   * @param blob Blob对象
   * @return 指定clob的writer
   * @throws SQLException
   */
  private static OutputStream getBlobOutputStream(Blob blob) throws
      SQLException {
    return blob.setBinaryStream(1);
  }

  /**
   * 得到指定clob的writer
   * @param clob Clob对象
   * @return 指定clob的writer
   * @throws SQLException
   */
  private static Writer getClobWriter(Clob clob) throws SQLException {
    return clob.setCharacterStream(1);
  }

  /**
   *
   * @param clob
   * @return
   * @throws IOException
   * @throws java.sql.SQLException
   */
  public static String Clob2String(Clob clob) throws IOException,
      java.sql.SQLException {
    if (clob == null) {
      return "";
    }
    CharArrayWriter writer = null;
    Reader reader = null;
    try {
      writer = new CharArrayWriter();
      char[] buf = new char[1024];
      reader = clob.getCharacterStream();
      int count = 0;
      while ( (count = reader.read(buf)) != -1) {
        writer.write(buf, 0, count);
      }
      return new String(writer.toCharArray());
    }
    finally {
      if (writer != null) {
        writer.close();
      }
      if (reader != null) {
        reader.close();
      }
    }
  }

}