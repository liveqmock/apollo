package cn.com.youtong.apollo.serialization;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.parsers.*;
import javax.xml.transform.stream.*;
import cn.com.youtong.apollo.task.Table;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.common.*;
import org.apache.commons.logging.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.SAXiTextHandler;
import com.lowagie.text.*;
import org.xml.sax.*;

public class PDFSerializer
    implements Serializer
{
  private Log log = LogFactory.getLog(this.getClass());
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
      //将XSLT与XML数据流转换为PDF需要的XML格式流
      tempOut = new ByteArrayOutputStream();
      getXmlWithTable(taskXml,table,tempOut);

      //将EXCEL的XML格式流转换为PDF格式文件流（二进制），并用out流输出
      xmlIn = new ByteArrayInputStream(tempOut.toByteArray());
      this.formatPDFStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
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
      Iterator itrXMLs = getAllXMLsForPDF(taskXml,task);

      //将多个分XML组合成一个大XML即EXCEL需要的XML格式
      String xml = integrateXML(itrXMLs);

      //将PDF的XML格式流转换为PDF格式文件流（二进制），并用out流输出
      xmlIn = new ByteArrayInputStream(xml.getBytes());
      this.formatPDFStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
      throw new SerializeException(ex);
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
  private Iterator getAllXMLsForPDF(InputStream taskXml,Task task) throws SerializeException
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
        getXmlWithTable(xmlIn,table,tempOut);

        String xml = tempOut.toString();
        if(xml != null && !xml.equals(""))
          result.put(table.getTableID(),xml);
      }
    }
    catch (IOException ex)
    {
      log.info(ex);
      throw new SerializeException(ex);
    }
    catch (TaskException ex)
    {
      log.info("无法获得表对象",ex);
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
  private void getXmlWithTable(InputStream taskXml,Table table,OutputStream out)
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
        if (tempView.getType().equals(TableViewType.TYPE_FOR_PDF))
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
   * @param itrXMLs 包含每个表的XML文件
   * @return 完整的XSLT文件
   */
  private String integrateXML(Iterator itrXMLs) throws SerializeException
  {
    StringBuffer result = new StringBuffer();
    StringBuffer temp = new StringBuffer();
    while(itrXMLs.hasNext())
    {
      if(temp.length()!=0)
        temp.append("<newpage/>");
      String xml = (String)itrXMLs.next();
      int startPosition = xml.indexOf("<itext>") + 7;
      int endPosition = xml.indexOf("</itext>");
      if(startPosition == 6 || endPosition == -1)
      {
        throw new SerializeException("转换PDF用的XSLT文件不正确");
      }
      temp.append(xml.substring(startPosition,endPosition));
    }

    result.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");
    result.append("<itext>");
    result.append(temp);
    result.append("</itext>");
    return result.toString();
  }

  /**
   * 将PDF的XML格式流转换为PDF格式文件流（二进制），并用out流输出
   * @param xmlStream 符合PDF的schema的XML文件
   * @param out PDF（二进制）文件
   * @throws SerializeException
   */
  private void formatPDFStream(InputStream xmlStream,
                               OutputStream out)
      throws SerializeException
  {
    Document document = null;
    try
    {
      document = new Document(PageSize.A4, 80, 50, 30, 65);
      PdfWriter.getInstance(document, out);
      document.open();
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      parser.parse(xmlStream, new SAXiTextHandler(document));
    }
    catch (SAXException ex)
    {
      log.error("转换XML到PDF失败",ex);
      throw new SerializeException("转换XML到PDF失败", ex);
    }
    catch (IOException ex)
    {
      log.error("转换XML到PDF失败",ex);
      throw new SerializeException("转换XML到PDF失败", ex);
    }
    catch (FactoryConfigurationError ex)
    {
      log.error("转换XML到PDF失败",ex);
      throw new SerializeException("转换XML到PDF失败", ex);
    }
    catch (DocumentException ex)
    {
      log.error("转换XML到PDF失败",ex);
      throw new SerializeException("转换XML到PDF失败", ex);
    }
    catch (ParserConfigurationException ex)
    {
      log.error("转换XML到PDF失败",ex);
      throw new SerializeException("转换XML到PDF失败", ex);
    }
    finally
    {
      document.close();
    }
  }

  /**
   * 将输入数据流XML，用XSLT转换为PDF格式需要的XML文件流
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
    /**@todo 该方法需要重构，与EXCEL中相同，但不知道该放在哪里，待定！！
     * 是否要搞一个模板方法呢？
     * */
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
      log.error("将XML转换为PDF的输入XML文件时失败", ex);
      throw new SerializeException(ex);
    }

  }

  /**
   * 测试用
   * @param args 命令行输入
   */
  public static void main(String[] args)
  {


  }
}