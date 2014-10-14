package cn.com.youtong.apollo.serialization;

import java.io.*;
import cn.com.youtong.apollo.task.*;

/**
 * ������ָ�ʽ�����л����ӿ�
 */
public interface Serializer
{
  /**
   * ���л�ָ�����������еı�
   * @param taskXml �����������ݵ�xml
   * @param table ������󣬰��������ʽ��Ϣ
   * @param out �����
   * @throws SerializeException
   */
  public void serializeTable(InputStream taskXml,
                             Table table,
                             OutputStream out)
      throws SerializeException;

  /**
   * ���л�ָ������������
   * @param taskXml �����������ݵ�xml
   * @param task ��������󣬰����������ʽ��Ϣ
   * @param out �����
   * @throws SerializeException
   */
  public void serializeTask(InputStream taskXml,
                            Task task,
                            OutputStream out)
      throws SerializeException;
}