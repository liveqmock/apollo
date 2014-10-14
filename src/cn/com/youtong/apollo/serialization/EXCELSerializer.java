package cn.com.youtong.apollo.serialization;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.apache.commons.logging.*;
import org.apache.cocoon.serialization.HSSFSerializer;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.common.*;

public class EXCELSerializer
    implements Serializer
{
  private Log log = LogFactory.getLog(EXCELSerializer.class.getName());
  private static org.apache.avalon.framework.logger.Logger avalonLogger = new
      org.apache.avalon.framework.logger.ConsoleLogger(org.apache.avalon.
      framework.logger.ConsoleLogger.LEVEL_INFO);

  /**
   * 序列化指定任务数据中的表
   * @param taskXml 包含任务数据的xml
   * @param table 表定义对象，包含表的样式信息
   * @param out 输出流
   * @throws SerializeException
   */
  public void serializeTable(InputStream taskXml, Table table, OutputStream out)
      throws SerializeException
  {
    ByteArrayOutputStream tempOut = null;
    ByteArrayInputStream xmlIn = null;
    try
    {
      //将XSLT与XML数据流转换为EXCEL需要的XML格式流
      tempOut = new ByteArrayOutputStream();
      getXmlWithTable(taskXml, table, tempOut);

      //将EXCEL的XML格式流转换为EXCEL格式文件流（二进制），并用out流输出
      xmlIn = new ByteArrayInputStream(tempOut.toByteArray());
      formatExcelStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
      log.error("将XSLT与XML数据流转换为EXCEL需要的XML格式流时失败", ex);
      throw new SerializeException(ex);
    }
    finally
    {
      Util.close(xmlIn);
      Util.close(tempOut);
    }
  }

  /**
   * 序列化指定的任务数据
   * @param taskXml 包含任务数据的xml
   * @param task 任务定义对象，包含任务的样式信息
   * @param out 输出流
   * @throws SerializeException
   */
  public void serializeTask(InputStream taskXml, Task task, OutputStream out)
      throws SerializeException
  {
    ByteArrayInputStream xmlIn = null;
    try
    {
      //将每个table的XSLT转化为XML
      Iterator itrXMLs = getAllXMLsForExcel(taskXml, task);

      //将多个分XML组合成一个大XML即EXCEL需要的XML格式
      String xml = integrateXML(itrXMLs);

      //将EXCEL的XML格式流转换为EXCEL格式文件流（二进制），并用out流输出
      xmlIn = new ByteArrayInputStream(xml.getBytes());
      formatExcelStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
      log.error("转换为EXCEL格式文件时出错",ex);
      ex.printStackTrace();
      throw ex;
    }
    finally
    {
      Util.close(xmlIn);
    }
  }

  /**
   * 将每个table的XSLT转化为XML
   * @param taskXml 数据流
   * @param task 包含XSLT的任务对象
   * @return 迭代器，所有的XML以String形式保存
   * @throws SerializeException
   */
  private Iterator getAllXMLsForExcel(InputStream taskXml, Task task)
      throws SerializeException
  {
    Map result = new TreeMap();
    ByteArrayOutputStream tempOut = null;
    ByteArrayInputStream xmlIn = null;
    try
    {

      //将输入流转换为字符串保存在内存里，以便重复调用
      String strXML = Convertor.Stream2String(taskXml);
      if (strXML == null)
      {
        throw new SerializeException("数据输入不正确");
      }

      //循环处理每一个XSLT
      Iterator itrTables = task.getAllTables();
      while (itrTables.hasNext())
      {
        Table table = (Table) itrTables.next();

        xmlIn = new ByteArrayInputStream(strXML.getBytes());
        tempOut = new ByteArrayOutputStream();
        getXmlWithTable(xmlIn, table, tempOut);

        String xml = tempOut.toString();
        if (xml != null && !xml.equals(""))
        {
          result.put(table.getTableID(), xml);
        }
      }
    }
    catch (IOException ex)
    {
      log.info(ex);
      throw new SerializeException(ex);
    }
    catch (TaskException ex)
    {
      log.info("无法获得表对象", ex);
      throw new SerializeException(ex);
    }
    finally
    {
      Util.close(tempOut);
      Util.close(xmlIn);
    }
    return result.values().iterator();
  }

  /**
   * 序列化指定任务数据中的表
   * @param taskXml 数据流
   * @param table 包含XSLT的表对象
   * @param out 输出XML格式的文件
   * @throws SerializeException
   */
  private void getXmlWithTable(InputStream taskXml, Table table,
                               OutputStream out)
      throws SerializeException
  {
    ByteArrayInputStream xsltIn = null;
    try
    {
      //找出要处理的表单，并进行处理
      Iterator itrViews = table.getViews();
      while (itrViews.hasNext())
      {
        TableView tempView = (TableView) itrViews.next();
        if (tempView.getType().equals(TableViewType.TYPE_FOR_EXCEL))
        {
          String view = tempView.getXSLTString();

          //将XSLT与XML数据流转换为EXCEL需要的XML格式流
          xsltIn = new ByteArrayInputStream(tempView.getXSLTString().getBytes());
          formatXMLStream(taskXml, xsltIn, out);
          break;
        }
      }
    }
    catch (SerializeException ex)
    {
      throw new SerializeException(ex);
    }
    finally
    {
      Util.close(xsltIn);
    }
  }

  /**
   * 给XML加上头部和尾部，使之成为一个完整，正确的XML
   * @param itrXMLs 包含每个表的xml文件
   * @return 完整的XSLT文件
   */
  private String integrateXML(Iterator itrXMLs)
      throws SerializeException
  {
    StringBuffer result = new StringBuffer();
    StringBuffer temp = new StringBuffer();
    while (itrXMLs.hasNext())
    {
      String xml = (String) itrXMLs.next();
      int startPosition = xml.indexOf("<gmr:Sheets>") + 12;
      int endPosition = xml.indexOf("</gmr:Sheets>");
      if (startPosition == 11 || endPosition == -1)
      {
        throw new SerializeException("转换EXCEL用的XSLT文件不正确");
      }
      temp.append(xml.substring(startPosition, endPosition));
    }

    result.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");
    result.append("<gmr:Workbook xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" xmlns:gmr=\"http://www.gnumeric.org/v10.dtd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
    result.append("<gmr:Attributes/>");
    result.append("<gmr:Summary/>");
    result.append("<gmr:SheetNameIndex/>");
    result.append("<gmr:Names/>");
    result.append("<gmr:Geometry Width=\"766\" Height=\"372\"/>");
    result.append("<gmr:Sheets>");
    result.append(temp);
    result.append("</gmr:Sheets>");
    result.append("<gmr:UIData SelectedTab=\"0\"/>");
    result.append("<gmr:Calculation/>");
    result.append("</gmr:Workbook>");
    return result.toString();
  }

  /**
   * 将EXCEL的XML格式流转换为EXCEL格式文件流（二进制），并用out流输出
   * @param xmlStream 符合Excel的schema的XML文件
   * @param out Excel（二进制）文件
   * @throws SerializeException
   */
  private void formatExcelStream(InputStream xmlStream,
                                 OutputStream out)
      throws SerializeException
  {
    try
    {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(xmlStream);
      HSSFSerializer hssf = new HSSFSerializer();
      hssf.enableLogging(avalonLogger);
      hssf.initialize();

      hssf.setOutputStream(out);
      SAXOutputter saxOutput = new SAXOutputter(hssf);
      saxOutput.output(doc);
    }
    catch (Exception ex)
    {
      log.error("转换XML到EXCEL失败", ex);
      throw new SerializeException("转换XML到EXCEL失败", ex);
    }
    catch(Throwable ex)
    {
      log.error("转换XML到EXCEL失败", ex);
      throw new SerializeException("转换XML到EXCEL失败", ex);
    }
  }

  /**
   * 将输入数据流XML，用XSLT转换为EXCEL格式需要的XML文件流
   * @param xmlStream xml输入流
   * @param xsltStream xslt输入流
   * @param out 转换后的XML流
   * @throws SerializeException
   */
  private void formatXMLStream(InputStream xmlStream,
                               InputStream xsltStream,
                               OutputStream out)
      throws SerializeException
  {
    try
    {
      StreamSource xml = new StreamSource(new InputStreamReader(xmlStream, "gb2312"));
      StreamSource xsl = new StreamSource(new InputStreamReader(xsltStream, "gb2312"));
      StreamResult streamResult = new StreamResult(out);

      //根据xslt获得Transformer对象
      Transformer trans = TransformerFactory.newInstance().newTransformer(xsl);
      trans.setOutputProperty("encoding", "gb2312");
      //进行转换
      trans.transform(xml, streamResult);
    }
    catch (Exception ex)
    {
      log.error("将XML转换为EXCEL的输入XML文件时失败", ex);
      throw new SerializeException(ex);
    }
  }

  public static void main(String[] args)
  {
    try
    {
      File xmlIN = new File("c:/test/test.xml");
      File xsltIN= new File("c:/test/book.xls");
      File out= new File("c:/test/result2.xml");

      InputStream xmlStream = new FileInputStream(xmlIN);
      InputStream xsltStream = new FileInputStream(xsltIN);
      java.io.OutputStream outFile=new java.io.FileOutputStream(out);

      EXCELSerializer serializer=new EXCELSerializer();
      serializer.formatXMLStream(xmlStream,xsltStream,outFile);

      /*
      InputStream xmlStream = new FileInputStream(fileIn);
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(xmlStream);
      HSSFSerializer hssf = new HSSFSerializer();
      hssf.enableLogging(avalonLogger);
      hssf.initialize();

      hssf.setOutputStream(new FileOutputStream(fileOut));
      SAXOutputter saxOutput = new SAXOutputter(hssf);
      saxOutput.output(doc);
      */
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}