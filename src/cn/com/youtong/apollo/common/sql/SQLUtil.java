package cn.com.youtong.apollo.common.sql;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: YouTong</p>
 * @author not attributable
 * @version 1.0
 */

import java.sql.*;
import java.text.ParseException;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.Convertor;

public class SQLUtil {
  private SQLUtil() {
  }

  /**
   * ����PreparedStatement pstmt����posλ�õ�ֵ��
   * ���ֵ����ת��Ϊ���ڵ����ͣ���ô����Ϊnull��
   * @param pstmt PreparedStatement
   * @param pos   ���õ�λ��
   * @param type  ���õ�λ��ֵ��SQL����
   * @param value ֵ
   * @throws SQLException  ���û���ҵ���Ӧ�����ͣ��׳�SQLException��
   *                       Ŀǰ֧��VARBINARY, LONGVARCHAR, NUMERIC, TIMESTAMP,
   *                       DATE, DECIMAL, VARCHAR����
   */
  public static void setParamValue(PreparedStatement pstmt, int pos, int type,
                                   String value) throws SQLException {
    if (Util.isEmptyString(value)) {
      pstmt.setNull(pos, type);
      return;
    }

    switch (type) {
      case Types.VARBINARY:
        pstmt.setBytes(pos, value.getBytes());
        break;
      case Types.LONGVARCHAR:
        pstmt.setString(pos, value);
        break;

      case Types.NUMERIC:
        pstmt.setDouble(pos, Double.parseDouble(value));
        break;

      case Types.INTEGER:
        pstmt.setDouble(pos, Double.parseDouble(value));
        break;
      case Types.TIMESTAMP:
        try {
          java.util.Date date = Convertor.string2Date(value);
          if (date == null) {
            pstmt.setNull(pos, type);
          }
          else {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            pstmt.setDate(pos, sqlDate);
          }
        }
        catch (ParseException p) {
          pstmt.setNull(pos, type);
        }
        break;
      case Types.DATE:
        try {
          java.util.Date date = Convertor.string2Date(value);
          if (date == null) {
            pstmt.setNull(pos, type);
          }
          else {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            pstmt.setDate(pos, sqlDate);
          }
        }
        catch (ParseException p) {
          pstmt.setNull(pos, type);
        }
        break;
      case Types.DECIMAL:
        pstmt.setDouble(pos, Double.parseDouble(value));
        break;
      case Types.VARCHAR:
        pstmt.setString(pos, value);
        break;
      case Types.CHAR:
        pstmt.setString(pos, value);
        break;

      default:
        throw new SQLException("��֧�ֵ��������͡�" + type + "��");
    }

  }

  /**
   * �ر����������Դ
   * @param rs                �����
   * @param pstmt             ��ѯ���
   * @param con               ���ݿ�����
   */
  public static void close(ResultSet rs, PreparedStatement pstmt,
                           Connection con) {
    if (rs != null) {
      try {
        rs.close();
      }
      catch (SQLException sqle) {}
    }
    if (pstmt != null) {
      try {
        pstmt.close();
      }
      catch (SQLException sqle) {}
    }
    if (con != null) {
      try {
        con.close();
      }
      catch (SQLException sqle) {}
    }
  }

  public static void close(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      }
      catch (SQLException sqle) {}
    }
  }
}
