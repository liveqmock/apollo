package cn.com.youtong.apollo.common;

import java.text.*;
import java.util.*;
import java.sql.Clob;
import java.io.*;

/**
 * һЩ����ת������
 */
public class Convertor {
  /**
   * ���ָ�ʽ
   */
  private static DecimalFormat decimalFormShowdata = new DecimalFormat("#,##0.00");
  private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

  /**
   * ��ʱ���ʽ
   */
  private static SimpleDateFormat sdfLong = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");
  private static SimpleDateFormat sdfDec = new SimpleDateFormat("HH:mm:ss");
  /**
   * ��ʱ���ʽ
   */
  private static SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
  /** Mini Date Format */
  private static SimpleDateFormat sdfMini = new SimpleDateFormat("yyyyMMdd");

  /**
   * �·�ʱ���ʽ
   */
  private static SimpleDateFormat monthly = new SimpleDateFormat("yyyy��MM��");
  
  /**
   * �·�ʱ���ʽ
   */
  //private static SimpleDateFormat quate = new SimpleDateFormat("yyyy��MM��");

  /**
   * ��ָ���ַ���װ��Ϊ��html���ܹ�������ʾ���ַ��� ������jsp����ʾ����
   *
   * @param str
   *            Ҫת�����ַ���
   * @return ת������ַ���
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
   * �����ڶ����ʽ��Ϊ�ַ��� ��ʽΪ��yyyy-MM-dd HH:mm:ss
   *
   * @param date
   *            Ҫ��ʽ�������ڶ���
   * @return �����ַ���
   */
  public static String date2String(Date date) {
    String result = "";
    if (date != null) {
      result = sdfLong.format(date);
    }
    return result;
  }

  /**
   * XML datetime��ʱ���ʽ��YYYY-MM-ddTHH:mm:ss+08:00����date��ʽ����xml datetime
   * ��ʽ��������й�������Ӳ���룬ƫ��������Ϊ8Сʱ
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
   * �����ڶ����ʽ��Ϊ�ַ��� ��ʽΪ��yyyy��MM��
   *
   * @param date
   *            Ҫ��ʽ�������ڶ���
   * @return �����ַ���
   */
  public static String date2MonthlyString(Date date) {
    String result = "";
    if (date != null) {
      result = monthly.format(date);
    }
    return result;
  }
  /**
   * ������ת��Ϊ�����ַ���
   * @param date
   * @return
   */
  public static String date2QuateString(Date date) {
	  String result = "";
	  if (date != null){
		  int m = date.getMonth();
		  int q = m/3 + 1;
		  int year = date.getYear() + 1900;
		  result = year + "��" + q + "����";
	  }
	  return result;	  
  }
  
  /**
   * ������ת��Ϊ����ַ���
   * @param date
   * @return
   */
  public static String date2YearString(Date date){
	  String result = "";
	  if (date != null)
	  {
		  int year = date.getYear() + 1900;
		  result = year + "��";
	  }
	  return result;
  }

  /**
   * �����ڶ����ʽ��Ϊ�ַ��� ��ʽΪ��yyyy-MM-dd
   *
   * @param date
   *            Ҫ��ʽ�������ڶ���
   * @return �����ַ���
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
    * double��intת��
    * @param number Ҫ��ʽ����double
    * @return ��ʽ�����
    */
   public static String double2Int(Double number) {
     DecimalFormat percentFormat = new DecimalFormat("0");
     return percentFormat.format(number);
   }

  /**
   * �����ڶ����ʽ��Ϊ�ַ��� ��ʽΪ��yyyy-MM-dd
   *
   * @param date
   *            Ҫ��ʽ�������ڶ���
   * @return �����ַ���
   */
  public static Collection array2Collection(Object[] array) {
    Collection list = new ArrayList();
    for (int i = 0; i < array.length; i++) {
      list.add(array[i]);
    }
    return list;
  }

  /**
   * ���ַ���ת����Date���͡�
   * ����������null�����ǿո���ô����null��
   * @param str
   *            Ҫת���������ַ���
   * @return ���ڶ���
   * @throws Warning
   *             ҵ���쳣
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
   * ��Iterator����ΪCollection����
   *
   * @param iter
   *            Iterator����
   * @return Collection����
   */
  public static Collection collection(Iterator iter) {
    Collection coll = new ArrayList();

    for (; iter.hasNext(); ) {
      coll.add(iter.next());
    }

    return coll;
  }

  /**
   * ��Clob����ת��ΪString����
   * @param clob Clob����
   * @return String����
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
   * ��Clob����ת��Ϊbyte����
   * @param clob Clob����
   * @return byte����
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
   * ��Clob����ת��ΪString����
   * @param in ������
   * @return String����
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
   * ��ʽ������
   * @param number Ҫ��ʽ������
   * @return ��ʽ�����
   */
  public static String formatDoubleShowdata(Double number) {     //��ʾǧ�ַ�
    return decimalFormShowdata.format(number.doubleValue());
  }
  
  public static String formatDouble(Double number){              //����ʾǧ�ַ�
	  return decimalFormat.format(number.doubleValue());
  }

  /**
   * double���ٷֱȵ�ת��
   * @param number Ҫ��ʽ����double
   * @return ��ʽ�����
   */
  public static String double2Percent(double number) {
    DecimalFormat percentFormat = new DecimalFormat("0.00%");
    return percentFormat.format(number);
  }

  /**
   * Objec��boolean��ת��
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
   * ��InputStream������д��OutputStream��
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
   * ��Reader������д��Writer��
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
   * ���parseʧ�ܣ�����null
   * @param htmlColor             ����#FF00FF��ʽ
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
