package cn.com.youtong.apollo.serialization;

import java.io.*;
import cn.com.youtong.apollo.task.*;

/**
 * 报表多种格式的序列化器接口
 */
public interface Serializer
{
  /**
   * 序列化指定任务数据中的表
   * @param taskXml 包含任务数据的xml
   * @param table 表定义对象，包含表的样式信息
   * @param out 输出流
   * @throws SerializeException
   */
  public void serializeTable(InputStream taskXml,
                             Table table,
                             OutputStream out)
      throws SerializeException;

  /**
   * 序列化指定的任务数据
   * @param taskXml 包含任务数据的xml
   * @param task 任务定义对象，包含任务的样式信息
   * @param out 输出流
   * @throws SerializeException
   */
  public void serializeTask(InputStream taskXml,
                            Task task,
                            OutputStream out)
      throws SerializeException;
}