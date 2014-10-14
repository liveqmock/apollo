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
      //��XSLT��XML������ת��ΪPDF��Ҫ��XML��ʽ��
      tempOut = new ByteArrayOutputStream();
      getXmlWithTable(taskXml,table,tempOut);

      //��EXCEL��XML��ʽ��ת��ΪPDF��ʽ�ļ����������ƣ�������out�����
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
      Iterator itrXMLs = getAllXMLsForPDF(taskXml,task);

      //�������XML��ϳ�һ����XML��EXCEL��Ҫ��XML��ʽ
      String xml = integrateXML(itrXMLs);

      //��PDF��XML��ʽ��ת��ΪPDF��ʽ�ļ����������ƣ�������out�����
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
   * ��ÿ��table��XSLTת��ΪXML
   * @param taskXml ������
   * @param task ����XSLT���������
   * @return �����������е�XML��String��ʽ����
   * @throws SerializeException
   */
  private Iterator getAllXMLsForPDF(InputStream taskXml,Task task) throws SerializeException
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
      log.info("�޷���ñ����",ex);
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
  private void getXmlWithTable(InputStream taskXml,Table table,OutputStream out)
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
        if (tempView.getType().equals(TableViewType.TYPE_FOR_PDF))
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
   * @param itrXMLs ����ÿ�����XML�ļ�
   * @return ������XSLT�ļ�
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
        throw new SerializeException("ת��PDF�õ�XSLT�ļ�����ȷ");
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
   * ��PDF��XML��ʽ��ת��ΪPDF��ʽ�ļ����������ƣ�������out�����
   * @param xmlStream ����PDF��schema��XML�ļ�
   * @param out PDF�������ƣ��ļ�
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
      log.error("ת��XML��PDFʧ��",ex);
      throw new SerializeException("ת��XML��PDFʧ��", ex);
    }
    catch (IOException ex)
    {
      log.error("ת��XML��PDFʧ��",ex);
      throw new SerializeException("ת��XML��PDFʧ��", ex);
    }
    catch (FactoryConfigurationError ex)
    {
      log.error("ת��XML��PDFʧ��",ex);
      throw new SerializeException("ת��XML��PDFʧ��", ex);
    }
    catch (DocumentException ex)
    {
      log.error("ת��XML��PDFʧ��",ex);
      throw new SerializeException("ת��XML��PDFʧ��", ex);
    }
    catch (ParserConfigurationException ex)
    {
      log.error("ת��XML��PDFʧ��",ex);
      throw new SerializeException("ת��XML��PDFʧ��", ex);
    }
    finally
    {
      document.close();
    }
  }

  /**
   * ������������XML����XSLTת��ΪPDF��ʽ��Ҫ��XML�ļ���
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
    /**@todo �÷�����Ҫ�ع�����EXCEL����ͬ������֪���÷��������������
     * �Ƿ�Ҫ��һ��ģ�巽���أ�
     * */
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
      log.error("��XMLת��ΪPDF������XML�ļ�ʱʧ��", ex);
      throw new SerializeException(ex);
    }

  }

  /**
   * ������
   * @param args ����������
   */
  public static void main(String[] args)
  {


  }
}