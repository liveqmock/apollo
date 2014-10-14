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
   * ���л�ָ�����������еı�
   * @param taskXml �����������ݵ�xml
   * @param table ������󣬰��������ʽ��Ϣ
   * @param out �����
   * @throws SerializeException
   */
  public void serializeTable(InputStream taskXml, Table table, OutputStream out)
      throws SerializeException
  {
    ByteArrayOutputStream tempOut = null;
    ByteArrayInputStream xmlIn = null;
    try
    {
      //��XSLT��XML������ת��ΪEXCEL��Ҫ��XML��ʽ��
      tempOut = new ByteArrayOutputStream();
      getXmlWithTable(taskXml, table, tempOut);

      //��EXCEL��XML��ʽ��ת��ΪEXCEL��ʽ�ļ����������ƣ�������out�����
      xmlIn = new ByteArrayInputStream(tempOut.toByteArray());
      formatExcelStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
      log.error("��XSLT��XML������ת��ΪEXCEL��Ҫ��XML��ʽ��ʱʧ��", ex);
      throw new SerializeException(ex);
    }
    finally
    {
      Util.close(xmlIn);
      Util.close(tempOut);
    }
  }

  /**
   * ���л�ָ������������
   * @param taskXml �����������ݵ�xml
   * @param task ��������󣬰����������ʽ��Ϣ
   * @param out �����
   * @throws SerializeException
   */
  public void serializeTask(InputStream taskXml, Task task, OutputStream out)
      throws SerializeException
  {
    ByteArrayInputStream xmlIn = null;
    try
    {
      //��ÿ��table��XSLTת��ΪXML
      Iterator itrXMLs = getAllXMLsForExcel(taskXml, task);

      //�������XML��ϳ�һ����XML��EXCEL��Ҫ��XML��ʽ
      String xml = integrateXML(itrXMLs);

      //��EXCEL��XML��ʽ��ת��ΪEXCEL��ʽ�ļ����������ƣ�������out�����
      xmlIn = new ByteArrayInputStream(xml.getBytes());
      formatExcelStream(xmlIn, out);
    }
    catch (SerializeException ex)
    {
      log.error("ת��ΪEXCEL��ʽ�ļ�ʱ����",ex);
      ex.printStackTrace();
      throw ex;
    }
    finally
    {
      Util.close(xmlIn);
    }
  }

  /**
   * ��ÿ��table��XSLTת��ΪXML
   * @param taskXml ������
   * @param task ����XSLT���������
   * @return �����������е�XML��String��ʽ����
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

      //��������ת��Ϊ�ַ����������ڴ���Ա��ظ�����
      String strXML = Convertor.Stream2String(taskXml);
      if (strXML == null)
      {
        throw new SerializeException("�������벻��ȷ");
      }

      //ѭ������ÿһ��XSLT
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
      log.info("�޷���ñ����", ex);
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
   * ���л�ָ�����������еı�
   * @param taskXml ������
   * @param table ����XSLT�ı����
   * @param out ���XML��ʽ���ļ�
   * @throws SerializeException
   */
  private void getXmlWithTable(InputStream taskXml, Table table,
                               OutputStream out)
      throws SerializeException
  {
    ByteArrayInputStream xsltIn = null;
    try
    {
      //�ҳ�Ҫ����ı��������д���
      Iterator itrViews = table.getViews();
      while (itrViews.hasNext())
      {
        TableView tempView = (TableView) itrViews.next();
        if (tempView.getType().equals(TableViewType.TYPE_FOR_EXCEL))
        {
          String view = tempView.getXSLTString();

          //��XSLT��XML������ת��ΪEXCEL��Ҫ��XML��ʽ��
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
   * ��XML����ͷ����β����ʹ֮��Ϊһ����������ȷ��XML
   * @param itrXMLs ����ÿ�����xml�ļ�
   * @return ������XSLT�ļ�
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
        throw new SerializeException("ת��EXCEL�õ�XSLT�ļ�����ȷ");
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
   * ��EXCEL��XML��ʽ��ת��ΪEXCEL��ʽ�ļ����������ƣ�������out�����
   * @param xmlStream ����Excel��schema��XML�ļ�
   * @param out Excel�������ƣ��ļ�
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
      log.error("ת��XML��EXCELʧ��", ex);
      throw new SerializeException("ת��XML��EXCELʧ��", ex);
    }
    catch(Throwable ex)
    {
      log.error("ת��XML��EXCELʧ��", ex);
      throw new SerializeException("ת��XML��EXCELʧ��", ex);
    }
  }

  /**
   * ������������XML����XSLTת��ΪEXCEL��ʽ��Ҫ��XML�ļ���
   * @param xmlStream xml������
   * @param xsltStream xslt������
   * @param out ת�����XML��
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

      //����xslt���Transformer����
      Transformer trans = TransformerFactory.newInstance().newTransformer(xsl);
      trans.setOutputProperty("encoding", "gb2312");
      //����ת��
      trans.transform(xml, streamResult);
    }
    catch (Exception ex)
    {
      log.error("��XMLת��ΪEXCEL������XML�ļ�ʱʧ��", ex);
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