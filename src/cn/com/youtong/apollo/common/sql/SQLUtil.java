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
   * 设置PreparedStatement pstmt，第pos位置的值。
   * 如果值不能转换为对于的类型，那么设置为null。
   * @param pstmt PreparedStatement
   * @param pos   设置的位置
   * @param type  设置的位置值的SQL类型
   * @param value 值
   * @throws SQLException  如果没有找到对应的类型，抛出SQLException。
   *                       目前支持VARBINARY, LONGVARCHAR, NUMERIC, TIMESTAMP,
   *                       DATE, DECIMAL, VARCHAR类型
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
        throw new SQLException("不支持的数据类型“" + type + "”");
    }

  }

  /**
   * 关闭数据相关资源
   * @param rs                结果集
   * @param pstmt             查询语句
   * @param con               数据库连接
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
