package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;
import cn.com.youtong.apollo.task.*;

/**
 * 报表数据导出器
 */
public interface DataExporter {

  /**
   * 输出单位XML数据,见taskModel.xsd
   * @param unitID 单位ID
   * @param taskTime 任务时间
   * @param writer   xml 输出流
   * @param acl 访问控制
   * @throws ModelException
   * @throws IOException
   */
  void exportData(String unitID, TaskTime taskTime, Writer writer,
                  UnitACL acl) throws ModelException, IOException;

  /**
   * 输出编辑数据
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
   * 输出附件
   * @param unitID 单位id
   * @param taskTime 任务时间
   * @param out 附件的输出流
   * @param acl 单位ACL
   * @throws ModelException
   */
  void exportAttachment(String unitID,
                        cn.com.youtong.apollo.task.TaskTime taskTime,
                        OutputStream out, UnitACL acl) throws
      ModelException;

  /**
   * 输出所有单位XML数据,见taskModel.xsd
   * @param taskTime 任务时间
   * @param writer   xml 输出流
   * @param acl 访问控制
   * @throws ModelException
   * @throws IOException
   */
  void exportAllData(TaskTime taskTime, Writer writer, UnitACL acl) throws
      ModelException, IOException;

  /**
   * 输出单位树数据。
   * @param unitID 节点单位ID
   * @param taskTime 任务时间
   * @param writer 输出流
   * @param acl 访问控制权限
   * @throws ModelException
   * @throws IOException
   */
  void exportDataByTree(String unitID, TaskTime taskTime, Writer writer,
                        UnitACL acl) throws ModelException, IOException;
  
  
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
        UnitACL acl,ArrayList tableList) throws ModelException ;

}