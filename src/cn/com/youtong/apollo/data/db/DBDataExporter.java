package cn.com.youtong.apollo.data.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.common.database.DataBase;

import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.form.TableInfo;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.analyse.db.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;

import cn.com.youtong.apollo.dictionary.DictionaryHelp;

/**
 * 导出数据，这里不考虑权限问题。
 * 设计思路：
 * 从数据库中读取数据；
 *   每个数据库表格，只发出一条SQL语句。查询结果按照unitID排序。
 * 写入临时文件；
 *   每个查询结果，分别写到一个文件，文件名为当前时间 + "_" + 0～9的随机数，写到目录${WEB-INF}/stage
 * 读取文件，写入输出流；
 *   写流的头；
 *   写单位数据；
 *     写封面表，读取封面表对应的文件。如果读取到</unit>那么表示该unit在该表中记录读取完毕，读取下一个表。
 *     读取其他表，读取对应文件。查看unit是否和刚才读取的封面表中的Unit一致，如果不一致，跳过；
 *     如果一致，读取，直到</unit>
 *     查看该表是否存在浮动表，如果存在，写浮动表数据。
 *         浮动表数据，读取知道<unit>的ID不一致为止。
 *   重复写单位数据，直到读取到封面表对应文件的尾部；
 *   写流的尾；
 * 删除临时文件。
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author wjb
 * @version 1.0
 */
class DBDataExporter
    implements DataExporter {

  public static int SHOWDATA = 1;
  public static int EDITDATA = 0;

  /** DBUnitTreeManager实例 */
  private DBUnitTreeManager treeManager;

  /** log对象 */
  private Log log = LogFactory.getLog(this.getClass());
  ;

  /** 报表任务定义 */
  private Task task;

  /** xml 缩进*/
  private String XML_INDENT = "\t";
  /** 开始新的行 */
  private String NEW_LINE = "\n";
  /** 编码方式 */
  private String XML_ENCODING = "gb2312";

  public DBDataExporter(Task task, DBUnitTreeManager treeManager) {
    this.task = task;
    this.treeManager = treeManager;
  }

  /**
   * 输出单位XML数据,见taskModel.xsd
   * @param unitID 单位ID
   * @param taskTime 任务时间
   * @param writer   xml 输出流
   * @param acl 访问控制
   * @throws ModelException
   * @throws IOException
   */
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
                         UnitACL acl) throws ModelException {
    if (acl.isReadable(unitID)) {
      List list = new LinkedList();
      list.add(unitID);

      // 导出数据
      exportData(task, taskTime, list.iterator(), writer, SHOWDATA);
    }
    else {
      // 没有导出权限
      throw new ModelException("您对" + unitID + "单位没有导出权限，请选择正确的单位！");
    }

  }

  /**
   * 输出编辑数据
   * @param unitID
   * @param taskTime
   * @param writer
   * @param acl
   * @throws ModelException
   * @throws IOException
   */
  public void exportEditorData(String unitID, TaskTime taskTime, Writer writer,
                               UnitACL acl) throws ModelException, IOException {
    if (acl.isReadable(unitID)) {
      List list = new LinkedList();
      list.add(unitID);

      // 导出数据
      String cnt = DBAnalyseHelper.queryDraft(unitID, task.id(), taskTime.getTaskTimeID().intValue());
      if(cnt!=null){
    	  writer.write(cnt);
    	  writer.flush();
      }
      else exportData(task, taskTime, list.iterator(), writer, EDITDATA);
    }
    else {
      // 没有导出权限
      throw new ModelException("您对" + unitID + "单位没有导出权限，请选择正确的单位！");
    }

  }

  /**
   * 输出单位树数据。
   * @param unitID 节点单位ID
   * @param taskTime 任务时间
   * @param writer 输出流
   * @param acl 访问控制权限
   * @throws ModelException
   * @throws IOException
   */
  public void exportDataByTree(String unitID, TaskTime taskTime, Writer writer,
                               UnitACL acl) throws ModelException {
    if (!acl.isReadable(unitID)) {
      // 没有导出权限
      throw new ModelException("您对" + unitID + "单位树没有导出权限，请选择正确的单位树！");
    }

    UnitTreeNode unit = treeManager.getUnitTree(unitID);

    Collection unitIDCol = new LinkedList();
    unitIDCol = getUnitIDs(unit, unitIDCol);

    exportData(task, taskTime, unitIDCol.iterator(), writer, EDITDATA);
  }

  /**
   * 输出所有单位XML数据,见taskModel.xsd
   * @param taskTime 任务时间
   * @param writer   xml 输出流
   * @param acl 访问控制
   * 
   * @throws ModelException
   * @throws IOException
   */
  public void exportAllData(TaskTime taskTime, Writer writer, UnitACL acl) throws
      ModelException {
    Iterator unitTrees = treeManager.getUnitForest(acl); // 这里得到的就是所有有权限的单位
    Collection unitIDCol = new LinkedList();
    while (unitTrees.hasNext()) {
      UnitTreeNode unit = (UnitTreeNode) unitTrees.next();
      unitIDCol = getUnitIDs(unit, unitIDCol);
    }

    if (unitIDCol.size() == 0) {
      // 没有导出权限，或者没有单位存在
      throw new ModelException("您没有导出权限，或者目前没有单位数据！");
    }

    exportData(task, taskTime, unitIDCol.iterator(), writer, SHOWDATA);
  }

  /**
   * 输出附件
   * @param unitID 单位id
   * @param taskTime 任务时间
   * @param out 附件的输出流
   * @param acl 单位ACL
   * @throws ModelException
   */
  public void exportAttachment(String unitID,
                               cn.com.youtong.apollo.task.TaskTime taskTime,
                               OutputStream out, UnitACL acl) throws
      ModelException {
    if (acl.isReadable(unitID)) {
      // 导出附件
      exportAttachment(task, taskTime, unitID, out);
    }
    else {
      // 没有导出权限
      throw new ModelException("您对" + unitID + "单位没有导出权限，请选择正确的单位！");
    }
  }

  /**
   * 向输出流中输出任务数据。这里的输出功能不考虑权限问题，因此权限问题在调用该方法前考虑。
   * 输出的xml内容格式可以参见taskModel.xsd。
   *
   * <p>
   * 使用包可见性，主要暴露给DBDataSource.java使用。
   * </p>
   * @param task                              任务
   * @param taskTime                          任务时间
   * @param unitIDsIter                       导出的单位ID
   * @param writer                            XML输出流
   * @throws ModelException                   发生异常。
   *                                          Hibenate异常；
   *                                          数据库抛出异常；
   */
  void exportData(Task task, TaskTime taskTime, Iterator unitIDsIter,
                  Writer writer, int flag) throws ModelException {



    ArrayList tableList=new ArrayList();
    try{
      Iterator itr = task.getAllTables();
      while (itr.hasNext()) {
        tableList.add(itr.next());
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    db2XML(task, writer, taskTime, unitIDsIter, flag, tableList);


    // 从数据库导入到临时文件
    /*
    Map fileMap = dbToFile(task, taskTime, unitIDsIter, flag);
    log.debug("Get data from database to File finished");

    // 从临时文件写到xml
    fileToWriter(task, taskTime.getFromTime(), fileMap, writer);
    log.debug("Read and output data to output stream finished");

    deleteTempFiles(fileMap.values()); // 删除临时文件
    log.debug("Temp files deleted");
    */
  }

  /**
   * 向输出流中输出指定单位任务数据中的附件
   *
   * @param task                              任务
   * @param taskTime                          任务时间
   * @param unitID                            单位ID
   * @param out                            输出流
   * @throws ModelException                   发生异常。
   *                                          Hibenate异常；
   *                                          数据库抛出异常；
   */
  private void exportAttachment(Task task, TaskTime taskTime, String unitID,
                                OutputStream out) throws ModelException {
    String sql = "SELECT content FROM "
        + NameGenerator.generateAttachmentTableName(task.id())
        + " WHERE taskTimeID = " + taskTime.getTaskTimeID()
        + " AND unitID = '" + unitID + "'";

    Session session = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection conn=null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      pst = conn.prepareStatement(sql);
      rs = pst.executeQuery();

      log.debug("输出附件sql = " + sql);

      if (rs.next()) {
        Clob clob = rs.getClob(1);
        //base64解码
        byte[] buf = Util.decodeBase64(Convertor.Clob2Bytes(clob));
        //输出
        out.write(buf);
      }
    }
    catch (Exception ex) {
      throw new ModelException("输出附件失败", ex);
    }
    finally {
      HibernateUtil.close(rs, pst);
      HibernateUtil.close(session);
    }

  }

  private void fileToWriter(Task task, java.util.Date taskTime,
                            Map fileMap, Writer writer) throws
      ModelException {
    // 第一步，排好读取文件的顺序
    // 建立这样的数组，第一个是封面表表ID和对应的文件名，
    // 以后的其他表前面的元素为<table>，中间是表ID和对应的文件名，结尾的元素为</table>
    // 这样指示表开始和结束。浮动表的开始和结束分别使用<float></float>。
    // 遍历这个数组的时，第一个是封面表，读取封面表数据；
    // 接下来，遇到<table>，读取<table>后面的表ID，读取表数据；
    // 如果遇到<float>，读取<float>后面的表ID，读取数据；
    // 如果遇到</float>，表示该浮动表结束；
    // 遇到</table>表示表结束；

    List readerList = new LinkedList();
    List fileReaderList = new LinkedList();
    FileReader attachFileReader = null;
    LineNumberReader attachLineReader = null;

    try {
      String[] seq = getFileReadSequence(task, fileMap);

      if (log.isDebugEnabled()) {
        log.debug("Task 的读取顺序如下:");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < seq.length; i++) {
          sb.append(seq[i]).append(" ");
        }
        log.debug(sb.toString());
      }

      // 逐个读取
      writeHead(writer, task.id(), taskTime);

      for (int i = 0; i < seq.length; ) {
        String temp = seq[i];
        if (temp.equals("<table>") || temp.equals("</table>") ||
            temp.equals("<float>") || temp.equals("</float>")) {
          i++;
        }
        else {
          i++;
          temp = seq[i];
          // 如果全部有浮动表组成，那么对应的缓冲文件设置为null
          if (temp != null) {
            FileInputStream in = new FileInputStream(temp);
            LineNumberReader reader = new LineNumberReader(new
                InputStreamReader(in, "gb2312"));
            fileReaderList.add(reader);
            readerList.add(reader);
          }
          i++;
        }
      }

      int readerNum = readerList.size();
      boolean[] eof = new boolean[readerNum];
      String[] currUnitID = new String[readerNum];
      String attachUnitID = null;
      String attachFile = (String) fileMap.get(NameGenerator.
                                               generateAttachmentTableName(task.
          id()));
      attachFileReader = new FileReader(attachFile);
      attachLineReader = new LineNumberReader(attachFileReader);

      String attLine = attachLineReader.readLine();
      if (attLine == null) {
        attachUnitID = null; // 其实只需要attachUnitID为null就表示读取结束了
      }
      else {
        attachUnitID = getUnitID(attLine);
      }

      for (int i = 0; i < readerNum; i++) {
        LineNumberReader reader = (LineNumberReader) readerList.get(i);
        String line = reader.readLine();

        if (line == null) {
          eof[i] = true;
        }
        else {
          // line的格式形如<unit ID="XXXX">
          currUnitID[i] = getUnitID(line);
        }
      }

      while (!eof[0]) {
        // 开始一个单位
        writer.write("<unit ID=\"" + currUnitID[0] + "\">");
        writer.write(NEW_LINE);

        String tableID = seq[0];
        writer.write("<table ID=\"" + tableID + "\">");
        writer.write(NEW_LINE);

        LineNumberReader reader = (LineNumberReader) readerList.get(0);
        this.readSameUnitTableDataAndWrite(writer, reader, true);

        writer.write("</table>");
        writer.write(NEW_LINE);

        int readerListCursor = 1; // 表示从哪个LineNumberReader读取数据，重新使用之前重置为 1
        for (int i = 2; i < seq.length; ) {
          String temp = seq[i];
          if (temp.equals("<table>")) {
            // 开始一个表格
            i++; // 移动数组指针，指向tableID
            tableID = seq[i];

            i++; // 移动数组指针，指向tableID后面的fileName
            if (seq[i] == null) {
              writer.write("<table ID=\"" + tableID + "\">");
              writer.write(NEW_LINE);

              i++; // 如果某表全部有浮动表组成，那么fileName==null
              continue;
            }
            else {
              i++; // 移动数组指针
            }

            writer.write("<table ID=\"" + tableID + "\">");
            writer.write(NEW_LINE);

            if (!eof[readerListCursor]) {
              reader = (LineNumberReader) readerList.get(
                  readerListCursor);

              if (currUnitID[0].equals(currUnitID[
                                       readerListCursor])) {
                // 同一个unit，那么写数据；否则不用写，等下一个再进行比较
                readSameUnitTableDataAndWrite(writer, reader, false);

                String line = reader.readLine();
                if (line == null) {
                  eof[readerListCursor] = true;
                  currUnitID[readerListCursor] = null;
                }
                else {
                  currUnitID[readerListCursor] = getUnitID(
                      line);
                }
              }
            } // 如果该Reader没有东西读取，那么就不操作，直接进行移动Reader的游标

            readerListCursor++; // 移动Reader的游标，指向下一个
          }
          else if (temp.equals("</table>")) {
            i++;
            writer.write("</table>");
            writer.write(NEW_LINE);
          }
          else if (temp.equals("<float>")) {
            i++; // 移动数组指针，指向floatID
            String floatID = seq[i];
            i += 2; // 移动数组指针，floatID后面的是这个floatID对应的数据临时文件所在位置，跳过它

            reader = (LineNumberReader) readerList.get(
                readerListCursor);

            writer.write("<floatRow ID=\"" + floatID + "\">");
            writer.write(NEW_LINE);

            if (!eof[readerListCursor]) {
              while (currUnitID[0].equals(currUnitID[
                                          readerListCursor])) {
                // 同一个unit，那么写数据；否则不用写，等下一个再进行比较
                writer.write("<row>");
                writer.write(NEW_LINE);

                readSameUnitTableDataAndWrite(writer, reader, false);

                writer.write("</row>");
                writer.write(NEW_LINE);

                String line = reader.readLine();
                if (line == null) {
                  eof[readerListCursor] = true;
                  currUnitID[readerListCursor] = null;
                }
                else {
                  currUnitID[readerListCursor] = getUnitID(
                      line);
                }
              }
            }

            readerListCursor++;
          }
          else if (temp.equals("</float>")) {
            i++;
            writer.write("</floatRow>");
            writer.write(NEW_LINE);
          }
          else {
            i++;
            // 事实上程序不会到达这里
          }
        }

        // 写attachement
        if (currUnitID[0].equals(attachUnitID)) {
          readSameUnitTableDataAndWrite(writer, attachLineReader, true);
          // 因为unit后面直接就是attachment，所以当作封面表出来，不用跳过一行

          String line = attachLineReader.readLine();
          if (line == null) {
            attachUnitID = null;
          }
          else {
            attachUnitID = getUnitID(line);
          }
        }

        writer.write("</unit>");
        writer.write(NEW_LINE);

        // 查看封面表是否到了文件的末尾，或者读取下一个unit ID
        reader = (LineNumberReader) readerList.get(0);
        String line = reader.readLine();
        if (line == null) {
          eof[0] = true;
          currUnitID[0] = null;
        }
        else {
          currUnitID[0] = getUnitID(line);
        }
      }

      writeEnd(writer, "");
      writer.flush();
    }
    catch (IOException ex) {
      log.error("IO异常", ex);
      throw new ModelException("IO异常", ex);
    }
    catch (TaskException ex) {
      log.error("读取任务定义异常", ex);
      throw new ModelException("读取任务定义异常", ex);
    }
    finally {
      closeReader(attachFileReader);
      closeReader(attachLineReader);

      for (int i = 0, size = fileReaderList.size(); i < size; i++) {
        Reader reader = (Reader) fileReaderList.get(i);
        closeReader(reader);
      }

      for (int i = 0, size = readerList.size(); i < size; i++) {
        Reader reader = (Reader) readerList.get(i);
        closeReader(reader);
      }
    }
  }

  private void closeReader(Reader reader) {
    if (reader != null) {
      try {
        reader.close();
      }
      catch (IOException ioe) {}
    }
  }

  private void readSameUnitTableDataAndWrite(Writer writer,
                                             LineNumberReader reader,
                                             boolean isMetaTable) throws
      IOException {
    if (!isMetaTable) {
      reader.readLine(); // 非封面表多了一行<cell field="taskTime" value="XXXX" />，跳过
    }

    boolean sameUnit = true;
    while (sameUnit) {
      String line = reader.readLine();
      if (line.equals("</unit>")) {
        sameUnit = false; // 这个unit读取完毕
      }
      else {
        writer.write(line);
        writer.write(NEW_LINE);
      }
    }
  }

  /**
   * 从形如<unit ID="XXXX">中读取unitID
   * @param line             内容
   * @return                 unitID
   */
  private String getUnitID(String line) {
    return line.substring(10, line.length() - 2);
  }

  /**
   * 对于任务task，和任务数据的缓存文件和tableID的Map，排列个访问的次序
   * @param task         任务
   * @param fileMap      key/value=tableID/fileName
   * @return             访问次序
   * @throws TaskException
   */
  private String[] getFileReadSequence(Task task, Map fileMap) throws
      TaskException {
    List list = new LinkedList();
    String taskID = task.id();

    // 添加封面表
    String tableID = task.getUnitMetaTable().id();
    list.add(tableID);
    String tableName = NameGenerator.generateDataTableName(taskID, tableID);
    String fileName = (String) fileMap.get(tableName);
    list.add(fileName);

    String metaTableID = tableID;

// 其他表
    Iterator tableIter = task.getAllTables();
    while (tableIter.hasNext()) {
      Table table = (Table) tableIter.next();
      tableID = table.id();
      boolean isMetaTable = metaTableID.equals(tableID);

      if (!isMetaTable) {
        tableName = NameGenerator.generateDataTableName(taskID, tableID);
        fileName = (String) fileMap.get(tableName);

        list.add("<table>");
        list.add(tableID);
        list.add(fileName);

        Iterator rowIter = table.getRows();
        while (rowIter.hasNext()) {
          Row row = (Row) rowIter.next();
          if (row.getFlag(Row.FLAG_FLOAT_ROW)) {
            // 这里是浮动行
            String rowID = row.id();
            tableName = NameGenerator.generateFloatDataTableName(
                taskID, tableID, rowID);
            fileName = (String) fileMap.get(tableName);

            list.add("<float>");
            list.add(rowID);
            list.add(fileName);
            list.add("</float>");
          }
        }

        list.add("</table>");
      }
    }

    String[] sa = new String[0];
    return (String[]) list.toArray(sa);
  }

  /**
   * 删除临时文件
   * @param files         临时文件集合
   */
  private void deleteTempFiles(Collection files) {
    Iterator iter = files.iterator();
    while (iter.hasNext()) {
      String fileName = (String) iter.next();

      new File(fileName).delete(); // 删除文件
    }
  }

  /**
   * 从数据库中读取数据，然后写入临时文件
   *
   * @param task                              任务
   * @param taskTime                          任务时间
   * @param unitIDsIter                       导出的单位ID
   * @return Map                              数据缓存临时文件名和表名Map, key/value=(数据库的)tableName/filePath
   * @throws ModelException                   发生异常。
   *                                          Hibenate异常；
   *                                          数据库抛出异常；
   */
  private Map dbToFile(Task task, TaskTime taskTime, Iterator unitIDsIter,
                       int flag) throws
      ModelException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    Session session = null;

    // 表的查询语句Map, key/value= (数据库的)tableName/SQL
    Map sqlMap = null;
    // 查询出来的数据缓存的临时文件名和表的Map, key/value=(数据库的)tableName/filePath
    Map fileMap = new HashMap();
    String tempTableName = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tempTableName = TempTableUtil.createTempUnitIDTable(unitIDsIter,
          session);
      String unitIDs = "SELECT UNITID FROM " + tempTableName;
      sqlMap = getSQLs(task, taskTime.getTaskTimeID(), unitIDs);

      Iterator sqlIter = sqlMap.entrySet().iterator();
      while (sqlIter.hasNext()) {
        // 对于每个表发出一条SQL查询语句
        Map.Entry entry = (Map.Entry) sqlIter.next();
        String sql = (String) entry.getValue();

        con = session.connection();
        pstmt = con.prepareStatement(sql);
        rs = pstmt.executeQuery();

        String tableName = (String) entry.getKey(); // (数据库的)tableName

        String fileName = tempFileName(); // 临时文件名

        Writer writer = null;

        try {
          writer = new OutputStreamWriter(new FileOutputStream(fileName),
                                          "gb2312");

          dump(rs, writer, tableName, flag); // 将结果以XML格式输出到文件
          fileMap.put(tableName, fileName);
        }
        catch (IOException ioe) {
          log.error("IO异常", ioe);
          throw new ModelException("IO异常", ioe);
        }
        finally {
          // 关闭资源
          Util.close(writer);
          HibernateUtil.close(rs, pstmt);
          HibernateUtil.close(session);
        }
      }

      // 附件SQL,并写到文件
      String tableName = NameGenerator.generateAttachmentTableName(task.
          id());
      String sql = "SELECT * FROM "
          + tableName
          + " WHERE taskTimeID = " + taskTime.getTaskTimeID().intValue()
          + " AND unitID in ( "
          + unitIDs
          + " ) ORDER BY unitID";

      String attachmentFile = tempFileName();
      fileMap.put(tableName, attachmentFile);

      File file = new File(attachmentFile);
      dumpAttachment(con, sql, file);
    }
    catch (HibernateException he) {
      log.error("Hibernate出错", he);
      throw new ModelException("Hiberate 异常", he);
    }
    catch (SQLException sqle) {
      log.error("数据库异常", sqle);
      throw new ModelException("数据库异常", sqle);
    }
    catch (TaskException te) {
      log.error("读取任务定义异常", te);
      throw new ModelException("读取任务定义异常", te);
    }
    finally {
      if (tempTableName != null) {
        TempTableUtil.dropTempTable(tempTableName, session);
      }
      HibernateUtil.close(session);
    }

    return fileMap;
  }

  private void dumpAttachment(Connection con, String sql, File file) throws
      ModelException {
    XMLOutputter output = new XMLOutputter(XML_INDENT, true, XML_ENCODING);

    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = con.prepareStatement(sql);
      rs = pstmt.executeQuery();

      fos = new FileOutputStream(file);
      bos = new BufferedOutputStream(fos);
      while (rs.next()) {
        String unitID = rs.getString(1);
        String name = rs.getString("name");
        String content = Convertor.Clob2String(rs.getClob("content"));

        bos.write( ("<unit ID=\"" + unitID + "\">").getBytes());
        bos.write(this.NEW_LINE.getBytes());

        Element attachment = new Element("attachment");
        attachment.setAttribute("name", name);
        attachment.addContent(content);

        output.output(attachment, bos);
        bos.write(this.NEW_LINE.getBytes());

        bos.write( ("</unit>").getBytes());
        bos.write(this.NEW_LINE.getBytes());
      }
    }
    catch (SQLException sqle) {
      log.error("数据库异常", sqle);
      throw new ModelException("数据库异常", sqle);
    }
    catch (IOException ioe) {
      log.error("IO异常", ioe);
      throw new ModelException("IO异常", ioe);
    }
    finally {
      // 关闭资源
      Util.close(bos);
      Util.close(fos);

      SQLUtil.close(rs, pstmt, null);
    }
  }

  private String tempFileName() {
    String tempDir = Config.getString(
        "cn.com.youtong.apollo.data.export.tempdir");

    long currTimeInMs = System.currentTimeMillis();

    return tempDir + File.separator + currTimeInMs + "_" +
        Util.generateRandom();
  }

  /**
   * 得到取得数据的查询表的SQL
   * @param task
   * @param taskTimeID
   * @param unitIDs
   * @return
   * @throws TaskException
   */
  private Map getSQLs(Task task, Integer taskTimeID, String unitIDs) throws
      TaskException {
    Map sqlMap = new HashMap();
    Iterator tableIter = task.getAllTables(); // 取得所有的表
    StringBuffer sqlBuff = new StringBuffer(); // 生成sql的临时辅助变量
    while (tableIter.hasNext()) {
      Table table = (Table) tableIter.next();
      Map tableSQLMap = getSQL(task, taskTimeID, unitIDs,
                               table);
      sqlMap.putAll(tableSQLMap);
    }
    return sqlMap;
  }

  /**
   * 得到代码字典
   * @param task
   * @param cellName
   * @return
   */
  private String getDicValue(Task task, String tableName, String cellName,
                             String value) {
    try {
      String tableID = "";
      String[] ary = tableName.split("_");
      if (ary.length >= 3) {
        tableID = ary[2];
      }
      else {
        return value;
      }

      Table table = task.getTableByID(tableID);
      Cell cell = table.getCellByDBFieldName(cellName);
      String dicID = cell.getDictionaryID();
      if (dicID != null && !dicID.equals("")) {
        return DictionaryHelp.getValue(dicID, value);
      }

    }
    catch (Exception ex) {
    }

    return value;
  }

  private void dump(ResultSet rs, Writer writer, String tableName, int flag) throws
      SQLException,
      IOException {
    XMLOutputter output = new XMLOutputter(XML_INDENT, true, XML_ENCODING);

    ResultSetMetaData rsmd = rs.getMetaData();
    int count = rsmd.getColumnCount();

    String[] cellNames = new String[count + 1]; // 字段名,从第二位开始cellNames[0]没有赋值
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // 将字段名复制到数组中
    }

    // 写XML
    while (rs.next()) {
      String unitID = rs.getString(1);
      writer.write("<unit ID=\"" + unitID + "\">");
      writer.write(this.NEW_LINE);

      for (int i = 2; i <= count; i++) {
        String cellName = cellNames[i];
        String value = getValueFromResultSet(rs, i,flag);

        Element element = new Element("cell");

        element.setAttribute("field", cellName);
        //增加修改代码字典
        /*
        if (flag == SHOWDATA) {
          String dicValue = "";
          dicValue = getDicValue(task, tableName, cellName, value);
          element.setAttribute("value", dicValue);
        }
        else {
          element.setAttribute("value", value);
        }
*/
        element.setAttribute("value", value);
        //xmlOut.write(currentIndent.getBytes());
        output.output(element, writer);
        writer.write(this.NEW_LINE);
      }

      writer.write("</unit>");
      writer.write(this.NEW_LINE);
    }
  }

  private String getValueFromResultSet(ResultSet rs, int index,int showData) throws
      SQLException {
    String value = "";
    Object obj = rs.getObject(index);
    if (obj != null) {
      //格式化double
      if (obj instanceof Double) {
        if(showData==SHOWDATA) value = Convertor.formatDoubleShowdata( (Double) obj);
        else value = Convertor.formatDouble( (Double) obj);
      }
      else if (obj instanceof java.math.BigDecimal) {
        Double doubleValue = new Double(obj.toString());
        if(showData==SHOWDATA) value = Convertor.formatDoubleShowdata(doubleValue);
        else value = Convertor.formatDouble(doubleValue);
      }
      else {
        try{
          if(rs.getString(index)!=null){
//            value = new String(rs.getString(index).getBytes("gb2312"),
//                               "ISO8859-1");
        	  value=rs.getString(index);
          }
        }
        catch(Exception ex)
        {}
      }
    }
    return value;
  }

  /**
   * 写xml头部，包含xml声明，根元素taskModel和taskTime
   *
   * @param writer    输出流
   * @param taskID    任务ID
   * @param taskTime  任务时间(java.util.Date)，作为taskTime元素的taskTime属性输出
   * @throws IOException
   * @return          输出后的“光标”对齐位置
   */
  String writeHead(Writer writer, String taskID,
                   java.util.Date taskTime) throws IOException {
    // 复位输出起始位置
    String currentIndent = "";

    writer.write("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
    writer.write(this.NEW_LINE); // 下一行
    writer.write("<taskModel ID=\"" + taskID + "\">");
    writer.write(this.NEW_LINE);

    currentIndent = increaseIndent(currentIndent); // 输出位置（“光标”）缩进

    String strTaskTime = Convertor.date2XMLDateTime(taskTime);
    writer.write(currentIndent + "<taskTime taskTime=\"" + strTaskTime +
                 "\">");
    writer.write(this.NEW_LINE);

    currentIndent = increaseIndent(currentIndent); // 输出位置（“光标”）缩进

    return currentIndent;
  }

  /**
   * 写文件尾
   *
   * @param writer                     输出流
   * @param currentIndent              当前输出对齐位置
   * @throws IOException
   */
  void writeEnd(Writer writer, String currentIndent) throws
      IOException {
    writer.write(currentIndent + "</taskTime>" + this.NEW_LINE);
    writer.write("</taskModel>" + this.NEW_LINE);
  }

  /**
   * 前进对齐
   * @param currentIndent   当前对齐
   * @return   前进一个单位后的对齐
   */
  private String increaseIndent(String currentIndent) {
    return currentIndent + XML_INDENT;
  }

  /**
   * 缩进对齐
   * @param currentIndent  当前对齐
   * @return    缩进一个单位后的对齐
   */
  private String decreaseIndent(String currentIndent) {
    return currentIndent.substring(XML_INDENT.length());
  }

  /**
   * 清除StringBuffer内容
   * @param sb         StringBuffer对象
   */
  private void clearStringBuffer(StringBuffer sb) {
    sb.delete(0, sb.length());
  }

  /**
   * 得到单位的代码信息和所有子单位代码信息
   *
   * @param unit
   *            单位信息
   * @param col
   *            集合
   * @return 集合
   */
  private Collection getUnitIDs(UnitTreeNode unit, Collection col) {
    //将本加入本单位
    col.add( (String) unit.id());

    Iterator itrUnits = unit.getChildren();
    //(注意，没有进行判断children是否存在)
    while (itrUnits.hasNext()) {
      UnitTreeNode childUnit = (UnitTreeNode) itrUnits.next();
      //加入下级单位
      getUnitIDs(childUnit, col);
    }

    return col;
  }

  /**
   * 获得单表的SQL
   * @param task
   * @param taskTimeID
   * @param unitIDs
   * @param table
   * @return
   * @throws TaskException
   */
  private Map getSQL(Task task, Integer taskTimeID, String unitIDs,
                     Table table) throws
      TaskException {

    Map sqlMap = new HashMap();
    String metaTableID = task.getUnitMetaTable().id(); // 任务的封面表ID
    StringBuffer sqlBuff = new StringBuffer(); // 生成sql的临时辅助变量

    String tableID = table.id();
    boolean isMetaTable = metaTableID.equals(tableID);

    Iterator rowIter = table.getRows();
    boolean hasNormalCells = false; // 是否全部是浮动行，没有其他固定行

    //处理浮动行
    while (rowIter.hasNext()) {
      Row row = (Row) rowIter.next();
      if (row.getFlag(Row.FLAG_FLOAT_ROW)) {
        // 这里考虑的是浮动行
        String rowID = row.id();
        String tableName = NameGenerator.generateFloatDataTableName(
            task.id(), tableID, rowID);

        sqlBuff.append("SELECT * FROM ").append(tableName);
        sqlBuff.append(" WHERE taskTimeID = ").append(taskTimeID.
            intValue());
        sqlBuff.append(" AND unitID IN ( ");
        sqlBuff.append(unitIDs);
        sqlBuff.append(" ) ORDER BY unitID");

        String sql = sqlBuff.toString();
        if (log.isDebugEnabled()) {
          log.debug("SELECT FloatTable SQL: " + sql);

        }
        sqlMap.put(tableName, sql);
        clearStringBuffer(sqlBuff); // 清除buff里面的内容
      }
      else {
        hasNormalCells = true;
      }
    }

    //处理固定行
    if (hasNormalCells) {
      // 查询表的SQL语句
      String tableName = NameGenerator.generateDataTableName(task.id(),
          tableID);
      sqlBuff.append("SELECT * FROM ").append(tableName);
      sqlBuff.append(" WHERE ");

      if (isMetaTable) {
        sqlBuff.append(" unitID in ( ").append(unitIDs).append(" )");
      }
      else {
        sqlBuff.append(" taskTimeID = ").append(taskTimeID.intValue());
        sqlBuff.append(" AND unitID in ( ").append(unitIDs).append(
            " )");
      }
      sqlBuff.append(" ORDER BY unitID");

      String sql = sqlBuff.toString();
      if (log.isDebugEnabled()) {
        log.debug("SELECT Table SQL: " + sql);

      }
      sqlMap.put(tableName, sql);
    }

    return sqlMap;
  }

  /**
   * 得到取得所对应表的SQL语句
   * @param task
   * @param taskTimeID
   * @param unitIDs
   * @param tableList
   * @return
   * @throws TaskException
   */
  private Map getSQLs(Task task, Integer taskTimeID, String unitIDs,
                      ArrayList tableList) throws
      TaskException {
    Map sqlMap = new HashMap();
    Iterator tableIter = task.getAllTables(); // 取得所有的表
    StringBuffer sqlBuff = new StringBuffer(); // 生成sql的临时辅助变量
    for (int i = 0; i < tableList.size(); i++) {
      Table table = (Table) tableList.get(i);
      Map tableSQLMap = getSQL(task, taskTimeID, unitIDs,
                               table);
      sqlMap.putAll(tableSQLMap);
    }
    return sqlMap;
  }

  /**
   * 从数据库中读取数据，然后写入临时文件
   *
   * @param task                              任务
   * @param taskTime                          任务时间
   * @param unitIDsIter                       导出的单位ID
   * @param tableList                         所要导出的表
   * @return Map                              数据缓存临时文件名和表名Map, key/value=(数据库的)tableName/filePath
   * @throws ModelException                   发生异常。
   *                                          Hibenate异常；
   *                                          数据库抛出异常；
   */
  private Map dbToFile(Task task, TaskTime taskTime, Iterator unitIDsIter,
                       int flag, ArrayList tableList) throws
      ModelException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    Session session = null;

    // 表的查询语句Map, key/value= (数据库的)tableName/SQL
    Map sqlMap = null;
    // 查询出来的数据缓存的临时文件名和表的Map, key/value=(数据库的)tableName/filePath
    Map fileMap = new HashMap();
    String tempTableName = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tempTableName = TempTableUtil.createTempUnitIDTable(unitIDsIter,
          session);
      String unitIDs = "SELECT UNITID FROM " + tempTableName;
      sqlMap = getSQLs(task, taskTime.getTaskTimeID(), unitIDs, tableList);

      Iterator sqlIter = sqlMap.entrySet().iterator();
      while (sqlIter.hasNext()) {
        // 对于每个表发出一条SQL查询语句
        Map.Entry entry = (Map.Entry) sqlIter.next();
        String sql = (String) entry.getValue();

        con = session.connection();
        pstmt = con.prepareStatement(sql);
        rs = pstmt.executeQuery();

        String tableName = (String) entry.getKey(); // (数据库的)tableName

        String fileName = tempFileName(); // 临时文件名

        Writer writer = null;

        try {
          writer = new OutputStreamWriter(new FileOutputStream(fileName),
                                          "gb2312");

          dump(rs, writer, tableName, flag); // 将结果以XML格式输出到文件
          fileMap.put(tableName, fileName);
        }
        catch (IOException ioe) {
          log.error("IO异常", ioe);
          throw new ModelException("IO异常", ioe);
        }
        finally {
          // 关闭资源
          Util.close(writer);
          HibernateUtil.close(rs, pstmt);
          HibernateUtil.close(session);
        }
      }

      // 附件SQL,并写到文件
      String tableName = NameGenerator.generateAttachmentTableName(task.
          id());
      String sql = "SELECT * FROM "
          + tableName
          + " WHERE taskTimeID = " + taskTime.getTaskTimeID().intValue()
          + " AND unitID in ( "
          + unitIDs
          + " ) ORDER BY unitID";

      String attachmentFile = tempFileName();
      fileMap.put(tableName, attachmentFile);

      File file = new File(attachmentFile);
      dumpAttachment(con, sql, file);
    }
    catch (HibernateException he) {
      log.error("Hibernate出错", he);
      throw new ModelException("Hiberate 异常", he);
    }
    catch (SQLException sqle) {
      log.error("数据库异常", sqle);
      throw new ModelException("数据库异常", sqle);
    }
    catch (TaskException te) {
      log.error("读取任务定义异常", te);
      throw new ModelException("读取任务定义异常", te);
    }
    finally {
      if (tempTableName != null) {
        TempTableUtil.dropTempTable(tempTableName, session);
      }
      HibernateUtil.close(rs,pstmt);
      HibernateUtil.close(session);
    }

    return fileMap;
  }

  /**
   * 向输出流中输出任务数据。这里的输出功能不考虑权限问题，因此权限问题在调用该方法前考虑。
   * 输出的xml内容格式可以参见taskModel.xsd。
   *
   * <p>
   * 使用包可见性，主要暴露给DBDataSource.java使用。
   * </p>
   * @param task                              任务
   * @param taskTime                          任务时间
   * @param unitIDsIter                       导出的单位ID
   * @param writer                            XML输出流
   * @throws ModelException                   发生异常。
   *                                          Hibenate异常；
   *                                          数据库抛出异常；
   */
  public void exportData(Task task, TaskTime taskTime, Iterator unitIDsIter,
                         Writer writer, int flag, ArrayList tableList) throws
      ModelException {
    //long beginTime=(new java.util.Date()).getTime();
    db2XML(task, writer, taskTime, unitIDsIter, flag, tableList);

    /*
         // 从数据库导入到临时文件
         Map fileMap = dbToFile(task, taskTime, unitIDsIter, flag, tableList);
         log.debug("Get data from database to File finished");
         // 从临时文件写到xml
         fileToWriter(task, taskTime.getFromTime(), fileMap, writer);
         log.debug("Read and output data to output stream finished");
         // deleteTempFiles(fileMap.values()); // 删除临时文件
         log.debug("Temp files deleted");
         long endTime=(new java.util.Date()).getTime();
     */
  }

  /**
   * 输出单位XML数据,见taskModel.xsd
   * @param unitID 单位ID
   * @param taskTime 任务时间
   * @param writer   xml 输出流
   * @param acl 访问控制
   * @throws ModelException
   * @throws IOException
   */
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
                         UnitACL acl, ArrayList tableList) throws
      ModelException {
    if (acl.isReadable(unitID)) {
      List list = new LinkedList();
      list.add(unitID);
      // 导出数据
      exportData(task, taskTime, list.iterator(), writer, SHOWDATA, tableList);
    }
    else {
      // 没有导出权限
      throw new ModelException("您对" + unitID + "单位没有导出权限，请选择正确的单位！");
    }

  }

  private String getDotSQL(ArrayList unitList)
  {
      String ret="";
      if(unitList.size()>0) ret+="'";
      for(int i=0;i<unitList.size();i++)
      {
          ret=ret+ (String)unitList.get(i) +"','";
      }
      if(ret.length()>1)
        ret=ret.substring(0,ret.length()-2);
//      if(unitList.size()>0) ret+="'";
      return ret;
  }


  /**
   * 将数据从数据库中取出并格式化为XML
   * @param task
   * @param taskTime
   * @param unitIDsIter
   * @param flag
   * @param tableList
   * @throws ModelException
   */
  private void db2XML(Task task, Writer writer, TaskTime taskTime,
                      Iterator unitIDsIter,
                      int flag, ArrayList tableList) throws
      ModelException {
    ArrayList unitIDList = new ArrayList();
    while (unitIDsIter.hasNext()) {
      unitIDList.add(unitIDsIter.next());
    }

    Session session = null;
    String tempTableName = null;

    try {
      String curIndex = writeHead(writer, task.id(), taskTime.getSubmitEndTime());
      session = HibernateUtil.getSessionFactory().openSession();
      String unitIDs = "SELECT UNITID FROM " + tempTableName;
      if(unitIDList.size()<10)
      {
        unitIDs=getDotSQL(unitIDList);
      }
      else
      {
        tempTableName = TempTableUtil.createTempUnitIDTable(unitIDList,
                 session);
         unitIDs = "SELECT UNITID FROM " + tempTableName;
      }


      //构造每个单位的数据流
      HashMap unitDatasMap = new HashMap();
      for (int i = 0; i < unitIDList.size(); i++) {
        unitDatasMap.put(unitIDList.get(i), new StringBuffer());
      }
      appendUnitHead(unitDatasMap);

      for(int i=0;i<tableList.size();i++)
      {
          Table tmpTable=(Table)tableList.get(i);
          writeTableToXML(task,taskTime.getTaskTimeID(),unitIDs,tmpTable,unitDatasMap,flag,session);

      }

      appendString(unitDatasMap, "</unit>\n");

      Iterator itr = unitDatasMap.keySet().iterator();
      while (itr.hasNext()) {
        String key = (String) itr.next();
        StringBuffer strBuffer = (StringBuffer) unitDatasMap.get(key);
        writer.write(strBuffer.toString());
        strBuffer=null;
      }

      writeEnd(writer, curIndex);
      writer.flush();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally
    {
      if (tempTableName != null) {
       TempTableUtil.dropTempTable(tempTableName, session);
     }
       HibernateUtil.close(session);
    }
  }

  /**
   * 将表数据处理成XML文件
   * @param task
   * @param taskTimeID
   * @param unitIDs
   * @param table
   * @param unitDatasMap
   */
  private void writeTableToXML(Task task, Integer taskTimeID,
                                   String unitIDs,
                                   Table table, HashMap unitDatasMap,int flag,Session session) {
    if(table==null)
       return;
    String tableName = NameGenerator.generateDataTableName(task.id(),table.id());
    String fixTableSQL = "";
    HashMap floatSQLMap = new HashMap();
    String metaTableID = task.getUnitMetaTable().id(); // 任务的封面表ID
    StringBuffer sqlBuff = new StringBuffer(); // 生成sql的临时辅助变量

    String tableID = table.id();
    boolean isMetaTable = metaTableID.equals(tableID);

    Iterator rowIter = table.getRows();
    boolean hasNormalCells = false; // 是否全部是浮动行，没有其他固定行

    //处理浮动行
    while (rowIter.hasNext()) {
      Row row = (Row) rowIter.next();
      if (row.getFlag(Row.FLAG_FLOAT_ROW)) {
        // 这里考虑的是浮动行
        String rowID = row.id();
        String floattableName = NameGenerator.generateFloatDataTableName(
            task.id(), tableID, rowID);

        sqlBuff.append("SELECT * FROM ").append(floattableName);
        sqlBuff.append(" WHERE TASKTIMEID = ").append(taskTimeID.
            intValue());
        sqlBuff.append(" AND UNITID IN ( ");
        sqlBuff.append(unitIDs);
        sqlBuff.append(" ) ORDER BY UNITID");

        String sql = sqlBuff.toString();
        if (log.isDebugEnabled()) {
          log.debug("SELECT FloatTable SQL: " + sql);
        }
        floatSQLMap.put(rowID, sql);
        clearStringBuffer(sqlBuff); // 清除buff里面的内容
      }
      else {
        hasNormalCells = true;
      }
    }

    //处理固定行
   // if (hasNormalCells) {
      // 查询表的SQL语句
      sqlBuff.append("SELECT * FROM ").append(tableName);
      sqlBuff.append(" WHERE ");

      if (isMetaTable) {
        sqlBuff.append(" UNITID in ( ").append(unitIDs).append(" )");
      }
      else {
        sqlBuff.append(" TASKTIMEID = ").append(taskTimeID.intValue());
        sqlBuff.append(" AND UNITID in ( ").append(unitIDs).append(
            " )");
      }
      sqlBuff.append(" ORDER BY UNITID");

      String sql = sqlBuff.toString();
      if (log.isDebugEnabled()) {
        log.debug("SELECT Table SQL: " + sql);

      }
      fixTableSQL = sql;
    //}

    appendString(unitDatasMap, "<table ID=\"" + table.id().toUpperCase() + "\">\n");

    Connection con = null;
    Statement pstmt = null;
    ResultSet rs = null;


    try{
      con = session.connection();
      //con=HibernateUtil.openSession().connection();
      //执行固定行
      pstmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = pstmt.executeQuery(sql);
      dump(rs,unitDatasMap,tableName,flag);
      //rs.close();
      //pstmt.close();
      Iterator floatRow=floatSQLMap.keySet().iterator();
      while(floatRow.hasNext())
      {
         String rowID=(String)floatRow.next();
         String floatSQL=(String)floatSQLMap.get(rowID);
         pstmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
         rs = pstmt.executeQuery(floatSQL);
         dumpFloatTable(rs,unitDatasMap,tableName,rowID,flag);
         rs.close();
         pstmt.close();
      }

    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      HibernateUtil.close(rs,pstmt);
    }

     appendString(unitDatasMap, "</table>\n");
  }

  /**
   * 输出数据
   * @param rs
   * @param unitDataMap
   * @param tableName
   * @param isRead
   * @param isFloat
   * @throws SQLException
   * @throws IOException
   */
  private void dumpFloatTable(ResultSet rs, HashMap unitDataMap,
                              String tableName, String rowID,
                              int isRead) throws
      SQLException,
      IOException {

    ResultSetMetaData rsmd = rs.getMetaData();
    int count = rsmd.getColumnCount();

    String[] cellNames = new String[count + 1]; // 字段名,从第二位开始cellNames[0]没有赋值
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // 将字段名复制到数组中
    }

    String oldUnitID = "";
    StringBuffer strBuffer = new StringBuffer();
    // 写XML
    appendString(unitDataMap, "<floatRow ID=\"" + rowID + "\">");

    while (rs.next()) {
      appendString(unitDataMap, "<row>");
      String unitID = rs.getString(1);
      if (!oldUnitID.equals(unitID)) {
        oldUnitID = unitID;
        strBuffer = (StringBuffer) unitDataMap.get(oldUnitID);
      }
      for (int i = 2; i <= count; i++) {
        String cellName = cellNames[i];
        String value = getValueFromResultSet(rs, i,isRead);
   
        if (isRead == SHOWDATA) {
          String dicValue = "";
          dicValue = getDicValue(task, tableName, cellName, value);
          value = dicValue;
        }

        strBuffer.append("<cell field=\"" + cellName + "\" value=\"" + value +
                         "\" />\n");
      }
      appendString(unitDataMap, "</row>");
    }

    appendString(unitDataMap, "</floatRow>");
  }

  /**
   * 附加字符串
   * @param unitDatasMap
   * @param appStr
   */
  private void appendUnitHead(HashMap unitDatasMap) {
    Iterator itr = unitDatasMap.keySet().iterator();
    while (itr.hasNext()) {
      String key = (String) itr.next();
      StringBuffer strBuffer = (StringBuffer) unitDatasMap.get(key);
      strBuffer.append("<unit ID=\"" + key + "\">");
    }
  }

  /**
   * 附加字符串
   * @param unitDatasMap
   * @param appStr
   */
  private void appendString(HashMap unitDatasMap, String appStr) {
    Iterator itr = unitDatasMap.keySet().iterator();
    while (itr.hasNext()) {
      String key = (String) itr.next();
      StringBuffer strBuffer = (StringBuffer) unitDatasMap.get(key);
      strBuffer.append(appStr);
    }
  }

  /**
   * 取得数据
   * @param rs
   * @param unitDataMap
   * @param tableName
   * @param flag
   * @throws SQLException
   * @throws IOException
   */
  private void dump(ResultSet rs, HashMap unitDataMap, String tableName,int flag) throws
      SQLException,IOException {

    ResultSetMetaData rsmd = rs.getMetaData();
    int count = rsmd.getColumnCount();

    String[] cellNames = new String[count + 1]; // 字段名,从第二位开始cellNames[0]没有赋值
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // 将字段名复制到数组中
    }

    String oldUnitID = "";
    StringBuffer strBuffer = new StringBuffer();
    // 写XML
    while (rs.next()) {
      String unitID = rs.getString(1);
      if (!oldUnitID.equals(unitID)) {
        oldUnitID = unitID;
        strBuffer = (StringBuffer) unitDataMap.get(oldUnitID);
      }
      for (int i = 2; i <= count; i++) {
        String cellName = cellNames[i];
         String value = getValueFromResultSet(rs, i,flag);
      

        if (flag == SHOWDATA) {
          String dicValue = "";
          dicValue = getDicValue(task, tableName, cellName, value);
          value = dicValue;
        }
        
        strBuffer.append("<cell field=\"" + cellName + "\" value=\"" + value +
                         "\" />\n");
      }
    }
  }


 

}
