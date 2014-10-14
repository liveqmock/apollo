package cn.com.youtong.apollo.common;

import java.text.*;
import java.util.*;
import java.sql.Clob;
import java.io.*;

/**
 * 一些常用转换功能
 */
public class Convertor {
  /**
   * 数字格式
   */
  private static DecimalFormat decimalFormShowdata = new DecimalFormat("#,##0.00");
  private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

  /**
   * 长时间格式
   */
  private static SimpleDateFormat sdfLong = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");
  private static SimpleDateFormat sdfDec = new SimpleDateFormat("HH:mm:ss");
  /**
   * 短时间格式
   */
  private static SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
  /** Mini Date Format */
  private static SimpleDateFormat sdfMini = new SimpleDateFormat("yyyyMMdd");

  /**
   * 月份时间格式
   */
  private static SimpleDateFormat monthly = new SimpleDateFormat("yyyy年MM月");
  
  /**
   * 月份时间格式
   */
  //private static SimpleDateFormat quate = new SimpleDateFormat("yyyy年MM月");

  /**
   * 将指定字符窜装换为在html中能够正常显示的字符串 用于在jsp中显示数据
   *
   * @param str
   *            要转换的字符串
   * @return 转换后的字符串
   */
  public static String getHTMLString(String str) {
    String result = filter(str);
    if (result == null) {
      return "";
    }
    return result.trim();
  }

  /**
   * Filter the specified string for characters that are senstive to HTML
   * interpreters, returning the string with these characters replaced by the
   * corresponding character entities.
   *
   * @param value
   *            The string to be filtered and returned
   */
  private static String filter(String value) {

    if (value == null) {
      return (null);
    }

    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
      switch (content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\n':
          result.append("<br>");
          break;

        default:
          result.append(content[i]);
      }
    }
    return (result.toString());
  }

  /**
   * 将日期对象格式化为字符串 格式为：yyyy-MM-dd HH:mm:ss
   *
   * @param date
   *            要格式化的日期对象
   * @return 日期字符串
   */
  public static String date2String(Date date) {
    String result = "";
    if (date != null) {
      result = sdfLong.format(date);
    }
    return result;
  }

  /**
   * XML datetime型时间格式是YYYY-MM-ddTHH:mm:ss+08:00。把date格式化成xml datetime
   * 格式。这里对中国进行了硬编码，偏移量设置为8小时
   * @param date
   * @return
   */
  public static String date2XMLDateTime(Date date) {
    String result = "";
    if (date != null) {
      result = sdfShort.format(date);
      String dec = sdfDec.format(date);
      result += "T" + dec + "+08:00";
    }
    return result;
  }

  /**
   * 将日期对象格式化为字符串 格式为：yyyy年MM月
   *
   * @param date
   *            要格式化的日期对象
   * @return 日期字符串
   */
  public static String date2MonthlyString(Date date) {
    String result = "";
    if (date != null) {
      result = monthly.format(date);
    }
    return result;
  }
  /**
   * 将日期转化为季度字符串
   * @param date
   * @return
   */
  public static String date2QuateString(Date date) {
	  String result = "";
	  if (date != null){
		  int m = date.getMonth();
		  int q = m/3 + 1;
		  int year = date.getYear() + 1900;
		  result = year + "年" + q + "季度";
	  }
	  return result;	  
  }
  
  /**
   * 将日期转化为年度字符串
   * @param date
   * @return
   */
  public static String date2YearString(Date date){
	  String result = "";
	  if (date != null)
	  {
		  int year = date.getYear() + 1900;
		  result = year + "年";
	  }
	  return result;
  }

  /**
   * 将日期对象格式化为字符串 格式为：yyyy-MM-dd
   *
   * @param date
   *            要格式化的日期对象
   * @return 日期字符串
   */
  public static String date2ShortString(Date date) {
    String result = "";
    if (date != null) {
      result = sdfShort.format(date);
    }
    return result;
  }

  public static String date2ShortString(String str) {
    String result = "";
    try {
      Date date = sdfLong.parse(str.trim());

      if (date != null) {
        result = sdfShort.format(date);
      }
    }
    catch (Exception e) {
    }

    return result;

  }

  /**
    * double到int转换
    * @param number 要格式化的double
    * @return 格式化结果
    */
   public static String double2Int(Double number) {
     DecimalFormat percentFormat = new DecimalFormat("0");
     return percentFormat.format(number);
   }

  /**
   * 将日期对象格式化为字符串 格式为：yyyy-MM-dd
   *
   * @param date
   *            要格式化的日期对象
   * @return 日期字符串
   */
  public static Collection array2Collection(Object[] array) {
    Collection list = new ArrayList();
    for (int i = 0; i < array.length; i++) {
      list.add(array[i]);
    }
    return list;
  }

  /**
   * 将字符串转换成Date类型。
   * 如果输入的是null或者是空格，那么返回null。
   * @param str
   *            要转换的日期字符串
   * @return 日期对象
   * @throws Warning
   *             业务异常
   */
  public static Date string2Date(String date) throws ParseException {
    if (date == null) {
      return null;
    }
    date = date.trim();

    date = date.replace('T', ' ');

    try {
      int length = date.length();
      if (length == 0) {
        return null;
      }

      if (length > 10) {
        return new Date(sdfLong.parse(date).getTime());
      }
      else if (length > 8) {
        return new Date(sdfShort.parse(date).getTime());
      }
      else {
        return new Date(sdfMini.parse(date).getTime());
      }
    }
    catch (ParseException e) {
      // try to parse again with first 10 letters.
      return sdfShort.parse(date.substring(0, 10));
    }
  }

  /**
   * 变Iterator对象为Collection对象
   *
   * @param iter
   *            Iterator对象
   * @return Collection对象
   */
  public static Collection collection(Iterator iter) {
    Collection coll = new ArrayList();

    for (; iter.hasNext(); ) {
      coll.add(iter.next());
    }

    return coll;
  }

  /**
   * 将Clob对象转换为String对象
   * @param clob Clob对象
   * @return String对象
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

  /**
   * 将Clob对象转换为byte数组
   * @param clob Clob对象
   * @return byte数组
   * @throws IOException
   * @throws java.sql.SQLException
   */
  public static byte[] Clob2Bytes(Clob clob) throws IOException,
      java.sql.SQLException {
    if (clob == null) {
      return new byte[0];
    }
    ByteArrayOutputStream out = null;
    InputStream in = null;
    try {
      out = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      in = clob.getAsciiStream();
      int count = 0;
      while ( (count = in.read(buf)) != -1) {
        out.write(buf, 0, count);
      }
      return out.toByteArray();
    }
    finally {
      Util.close(in);
      Util.close(out);
    }
  }

  /**
   * 将Clob对象转换为String对象
   * @param in 输入流
   * @return String对象
   * @throws IOException
   * @throws java.sql.SQLException
   */
  public static String Stream2String(InputStream in) throws IOException {
    if (in == null) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    InputStreamReader reader = new InputStreamReader(in);
    int c;
    while ( (c = reader.read()) != -1) {
      result.append( (char) c);
    }
    return result.toString();
  }

  /**
   * 格式化数字
   * @param number 要格式化的数
   * @return 格式化结果
   */
  public static String formatDoubleShowdata(Double number) {     //显示千分符
    return decimalFormShowdata.format(number.doubleValue());
  }
  
  public static String formatDouble(Double number){              //不显示千分符
	  return decimalFormat.format(number.doubleValue());
  }

  /**
   * double到百分比的转换
   * @param number 要格式化的double
   * @return 格式化结果
   */
  public static String double2Percent(double number) {
    DecimalFormat percentFormat = new DecimalFormat("0.00%");
    return percentFormat.format(number);
  }

  /**
   * Objec到boolean的转换
   * @param value object
   * @return boolean
   */
  public static boolean object2Boolean(Object value) {
    boolean result = false;

    try {
      if (value instanceof Boolean) {
        result = ( (Boolean) value).booleanValue();
      }
      else if (value instanceof Byte ||
               value instanceof Integer
               || value instanceof Number ||
               value instanceof Double
               || value instanceof Float) {
        int doubleValue = (int) Double.parseDouble(value.toString());
        result = (doubleValue != 0);
      }
      else if (value instanceof String) {
        result = Boolean.valueOf(value.toString().
                                 toLowerCase()).booleanValue();
      }

      return result;
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 将InputStream的内容写到OutputStream中
   * @param in InputStream
   * @param out OutputStream
   * @throws IOException
   */
  public static void input2Output(InputStream in, OutputStream out) throws
      IOException {
    byte[] buf = new byte[1024];
    for (int count = 0; (count = in.read(buf)) >= 0; ) {
      out.write(buf, 0, count);
    }
  }

  /**
   * 将Reader的内容写到Writer中
   * @param reader Reader
   * @param writer Writer
   * @throws IOException
   */
  public static void reader2Writer(Reader reader, Writer writer) throws
      IOException {
    char[] buf = new char[1024];
    for (int count = 0; (count = reader.read(buf)) >= 0; ) {
      writer.write(buf, 0, count);
    }
  }

  /**
   * 如果parse失败，返回null
   * @param htmlColor             形如#FF00FF格式
   * @return                      java.awt.Color
   */
  public static java.awt.Color parseHtmlColor(String htmlColor) {
    if (Util.isEmptyString(htmlColor)) {
      return null;
    }

    int red = Integer.parseInt(htmlColor.substring(1, 3), 16);
    int green = Integer.parseInt(htmlColor.substring(3, 5), 16);
    int blue = Integer.parseInt(htmlColor.substring(5, 7), 16);

    return new java.awt.Color(red, green, blue);
  }

  public static int[] parseArray(String strArray, String delim) {
    StringTokenizer strToken = new StringTokenizer(strArray, delim);

    int[] result = new int[strToken.countTokens()];

    for (int i = 0; i < result.length; i++) {
      String temp = strToken.nextToken();
      result[i] = (int) Double.parseDouble(temp.trim());
    }

    return result;
  }

  public static void main(String[] args) {
    /**java.awt.Color white = parseHtmlColor( "#FFFFFF" );
       java.awt.Color black = parseHtmlColor( "#000000" );
       java.awt.Color other = parseHtmlColor( "#FF2020" );
       System.out.println( white.getRed() );
       System.out.println( white.getGreen() );
       System.out.println( white.getBlue() );
       System.out.println( black.getRed() );
       System.out.println( black.getGreen() );
       System.out.println( black.getBlue() );
       System.out.println( other.getRed() );
       System.out.println( other.getGreen() );
       System.out.println( other.getBlue() );*/

    String s = "12, 125.0, 56, 56, 5";
    int[] result = parseArray(s, ",");
    System.out.println("===========");
    for (int i = 0; i < result.length; i++) {
      System.out.print(result[i] + " ");
    }

    s = "12.23";
    result = parseArray(s, ",");
    System.out.println("===========");
    for (int i = 0; i < result.length; i++) {
      System.out.print(result[i] + " ");
    }

    s = "12.56, 56.";
    result = parseArray(s, ",");
    System.out.println("===========");
    for (int i = 0; i < result.length; i++) {
      System.out.print(result[i] + " ");
    }
  }
}
