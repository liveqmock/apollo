package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;
import cn.com.youtong.apollo.task.*;

/**
 * �������ݵ�����
 */
public interface DataExporter {

  /**
   * �����λXML����,��taskModel.xsd
   * @param unitID ��λID
   * @param taskTime ����ʱ��
   * @param writer   xml �����
   * @param acl ���ʿ���
   * @throws ModelException
   * @throws IOException
   */
  void exportData(String unitID, TaskTime taskTime, Writer writer,
                  UnitACL acl) throws ModelException, IOException;

  /**
   * ����༭����
   * @param unitID
   * @param taskTime
   * @param writer
   * @param acl
   * @throws ModelException
   * @throws IOException
   */
  void exportEditorData(String unitID, TaskTime taskTime, Writer writer,
                  UnitACL acl) throws ModelException, IOException;

  /**
   * �������
   * @param unitID ��λid
   * @param taskTime ����ʱ��
   * @param out �����������
   * @param acl ��λACL
   * @throws ModelException
   */
  void exportAttachment(String unitID,
                        cn.com.youtong.apollo.task.TaskTime taskTime,
                        OutputStream out, UnitACL acl) throws
      ModelException;

  /**
   * ������е�λXML����,��taskModel.xsd
   * @param taskTime ����ʱ��
   * @param writer   xml �����
   * @param acl ���ʿ���
   * @throws ModelException
   * @throws IOException
   */
  void exportAllData(TaskTime taskTime, Writer writer, UnitACL acl) throws
      ModelException, IOException;

  /**
   * �����λ�����ݡ�
   * @param unitID �ڵ㵥λID
   * @param taskTime ����ʱ��
   * @param writer �����
   * @param acl ���ʿ���Ȩ��
   * @throws ModelException
   * @throws IOException
   */
  void exportDataByTree(String unitID, TaskTime taskTime, Writer writer,
                        UnitACL acl) throws ModelException, IOException;
  
  
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
        UnitACL acl,ArrayList tableList) throws ModelException ;

}