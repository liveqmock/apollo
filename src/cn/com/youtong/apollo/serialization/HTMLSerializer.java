package cn.com.youtong.apollo.serialization;

import java.io.*;
import cn.com.youtong.apollo.task.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.apache.commons.logging.*;

public class HTMLSerializer
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
    try
    {
      Iterator itrViews = table.getViews();
      while (itrViews.hasNext())
      {
        TableView tempView = (TableView) itrViews.next();
        if (!tempView.getType().equals(TableViewType.TYPE_FOR_HTML))
        {
          continue;
        }

        StreamSource xml = new StreamSource(new InputStreamReader(taskXml, "gb2312"));
//        StreamSource xml = new StreamSource(new InputStreamReader(taskXml));
        StreamSource xsl = new StreamSource(new StringReader(tempView.
            getXSLTString()));

        StreamResult streamResult = new StreamResult(out);
        //����xslt���Transformer����
        Transformer trans = TransformerFactory.newInstance().
            newTransformer(xsl);

        trans.setOutputProperty("encoding", "gb2312");
        //����ת��
        trans.transform(xml, streamResult);
      }
    }
    catch (Exception ex)
    {
      log.error("��XMLת��ΪHTMLʧ��", ex);
      throw new SerializeException(ex);
    }

  }

  public void serializeTask(InputStream taskXml, Task task, OutputStream out)
      throws SerializeException
  {
    /**@todo Implement this cn.com.youtong.apollo.serialization.Serializer method*/
    throw new java.lang.UnsupportedOperationException(
        "Method serializeTask() not yet implemented.");
  }
}