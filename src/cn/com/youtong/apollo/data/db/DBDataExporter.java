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
 * �������ݣ����ﲻ����Ȩ�����⡣
 * ���˼·��
 * �����ݿ��ж�ȡ���ݣ�
 *   ÿ�����ݿ���ֻ����һ��SQL��䡣��ѯ�������unitID����
 * д����ʱ�ļ���
 *   ÿ����ѯ������ֱ�д��һ���ļ����ļ���Ϊ��ǰʱ�� + "_" + 0��9���������д��Ŀ¼${WEB-INF}/stage
 * ��ȡ�ļ���д���������
 *   д����ͷ��
 *   д��λ���ݣ�
 *     д�������ȡ������Ӧ���ļ��������ȡ��</unit>��ô��ʾ��unit�ڸñ��м�¼��ȡ��ϣ���ȡ��һ����
 *     ��ȡ��������ȡ��Ӧ�ļ����鿴unit�Ƿ�͸ղŶ�ȡ�ķ�����е�Unitһ�£������һ�£�������
 *     ���һ�£���ȡ��ֱ��</unit>
 *     �鿴�ñ��Ƿ���ڸ�����������ڣ�д���������ݡ�
 *         ���������ݣ���ȡ֪��<unit>��ID��һ��Ϊֹ��
 *   �ظ�д��λ���ݣ�ֱ����ȡ��������Ӧ�ļ���β����
 *   д����β��
 * ɾ����ʱ�ļ���
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

  /** DBUnitTreeManagerʵ�� */
  private DBUnitTreeManager treeManager;

  /** log���� */
  private Log log = LogFactory.getLog(this.getClass());
  ;

  /** ���������� */
  private Task task;

  /** xml ����*/
  private String XML_INDENT = "\t";
  /** ��ʼ�µ��� */
  private String NEW_LINE = "\n";
  /** ���뷽ʽ */
  private String XML_ENCODING = "gb2312";

  public DBDataExporter(Task task, DBUnitTreeManager treeManager) {
    this.task = task;
    this.treeManager = treeManager;
  }

  /**
   * �����λXML����,��taskModel.xsd
   * @param unitID ��λID
   * @param taskTime ����ʱ��
   * @param writer   xml �����
   * @param acl ���ʿ���
   * @throws ModelException
   * @throws IOException
   */
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
                         UnitACL acl) throws ModelException {
    if (acl.isReadable(unitID)) {
      List list = new LinkedList();
      list.add(unitID);

      // ��������
      exportData(task, taskTime, list.iterator(), writer, SHOWDATA);
    }
    else {
      // û�е���Ȩ��
      throw new ModelException("����" + unitID + "��λû�е���Ȩ�ޣ���ѡ����ȷ�ĵ�λ��");
    }

  }

  /**
   * ����༭����
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

      // ��������
      String cnt = DBAnalyseHelper.queryDraft(unitID, task.id(), taskTime.getTaskTimeID().intValue());
      if(cnt!=null){
    	  writer.write(cnt);
    	  writer.flush();
      }
      else exportData(task, taskTime, list.iterator(), writer, EDITDATA);
    }
    else {
      // û�е���Ȩ��
      throw new ModelException("����" + unitID + "��λû�е���Ȩ�ޣ���ѡ����ȷ�ĵ�λ��");
    }

  }

  /**
   * �����λ�����ݡ�
   * @param unitID �ڵ㵥λID
   * @param taskTime ����ʱ��
   * @param writer �����
   * @param acl ���ʿ���Ȩ��
   * @throws ModelException
   * @throws IOException
   */
  public void exportDataByTree(String unitID, TaskTime taskTime, Writer writer,
                               UnitACL acl) throws ModelException {
    if (!acl.isReadable(unitID)) {
      // û�е���Ȩ��
      throw new ModelException("����" + unitID + "��λ��û�е���Ȩ�ޣ���ѡ����ȷ�ĵ�λ����");
    }

    UnitTreeNode unit = treeManager.getUnitTree(unitID);

    Collection unitIDCol = new LinkedList();
    unitIDCol = getUnitIDs(unit, unitIDCol);

    exportData(task, taskTime, unitIDCol.iterator(), writer, EDITDATA);
  }

  /**
   * ������е�λXML����,��taskModel.xsd
   * @param taskTime ����ʱ��
   * @param writer   xml �����
   * @param acl ���ʿ���
   * 
   * @throws ModelException
   * @throws IOException
   */
  public void exportAllData(TaskTime taskTime, Writer writer, UnitACL acl) throws
      ModelException {
    Iterator unitTrees = treeManager.getUnitForest(acl); // ����õ��ľ���������Ȩ�޵ĵ�λ
    Collection unitIDCol = new LinkedList();
    while (unitTrees.hasNext()) {
      UnitTreeNode unit = (UnitTreeNode) unitTrees.next();
      unitIDCol = getUnitIDs(unit, unitIDCol);
    }

    if (unitIDCol.size() == 0) {
      // û�е���Ȩ�ޣ�����û�е�λ����
      throw new ModelException("��û�е���Ȩ�ޣ�����Ŀǰû�е�λ���ݣ�");
    }

    exportData(task, taskTime, unitIDCol.iterator(), writer, SHOWDATA);
  }

  /**
   * �������
   * @param unitID ��λid
   * @param taskTime ����ʱ��
   * @param out �����������
   * @param acl ��λACL
   * @throws ModelException
   */
  public void exportAttachment(String unitID,
                               cn.com.youtong.apollo.task.TaskTime taskTime,
                               OutputStream out, UnitACL acl) throws
      ModelException {
    if (acl.isReadable(unitID)) {
      // ��������
      exportAttachment(task, taskTime, unitID, out);
    }
    else {
      // û�е���Ȩ��
      throw new ModelException("����" + unitID + "��λû�е���Ȩ�ޣ���ѡ����ȷ�ĵ�λ��");
    }
  }

  /**
   * �������������������ݡ������������ܲ�����Ȩ�����⣬���Ȩ�������ڵ��ø÷���ǰ���ǡ�
   * �����xml���ݸ�ʽ���Բμ�taskModel.xsd��
   *
   * <p>
   * ʹ�ð��ɼ��ԣ���Ҫ��¶��DBDataSource.javaʹ�á�
   * </p>
   * @param task                              ����
   * @param taskTime                          ����ʱ��
   * @param unitIDsIter                       �����ĵ�λID
   * @param writer                            XML�����
   * @throws ModelException                   �����쳣��
   *                                          Hibenate�쳣��
   *                                          ���ݿ��׳��쳣��
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


    // �����ݿ⵼�뵽��ʱ�ļ�
    /*
    Map fileMap = dbToFile(task, taskTime, unitIDsIter, flag);
    log.debug("Get data from database to File finished");

    // ����ʱ�ļ�д��xml
    fileToWriter(task, taskTime.getFromTime(), fileMap, writer);
    log.debug("Read and output data to output stream finished");

    deleteTempFiles(fileMap.values()); // ɾ����ʱ�ļ�
    log.debug("Temp files deleted");
    */
  }

  /**
   * ������������ָ����λ���������еĸ���
   *
   * @param task                              ����
   * @param taskTime                          ����ʱ��
   * @param unitID                            ��λID
   * @param out                            �����
   * @throws ModelException                   �����쳣��
   *                                          Hibenate�쳣��
   *                                          ���ݿ��׳��쳣��
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

      log.debug("�������sql = " + sql);

      if (rs.next()) {
        Clob clob = rs.getClob(1);
        //base64����
        byte[] buf = Util.decodeBase64(Convertor.Clob2Bytes(clob));
        //���
        out.write(buf);
      }
    }
    catch (Exception ex) {
      throw new ModelException("�������ʧ��", ex);
    }
    finally {
      HibernateUtil.close(rs, pst);
      HibernateUtil.close(session);
    }

  }

  private void fileToWriter(Task task, java.util.Date taskTime,
                            Map fileMap, Writer writer) throws
      ModelException {
    // ��һ�����źö�ȡ�ļ���˳��
    // �������������飬��һ���Ƿ�����ID�Ͷ�Ӧ���ļ�����
    // �Ժ��������ǰ���Ԫ��Ϊ<table>���м��Ǳ�ID�Ͷ�Ӧ���ļ�������β��Ԫ��Ϊ</table>
    // ����ָʾ��ʼ�ͽ�����������Ŀ�ʼ�ͽ����ֱ�ʹ��<float></float>��
    // ������������ʱ����һ���Ƿ������ȡ��������ݣ�
    // ������������<table>����ȡ<table>����ı�ID����ȡ�����ݣ�
    // �������<float>����ȡ<float>����ı�ID����ȡ���ݣ�
    // �������</float>����ʾ�ø����������
    // ����</table>��ʾ�������

    List readerList = new LinkedList();
    List fileReaderList = new LinkedList();
    FileReader attachFileReader = null;
    LineNumberReader attachLineReader = null;

    try {
      String[] seq = getFileReadSequence(task, fileMap);

      if (log.isDebugEnabled()) {
        log.debug("Task �Ķ�ȡ˳������:");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < seq.length; i++) {
          sb.append(seq[i]).append(" ");
        }
        log.debug(sb.toString());
      }

      // �����ȡ
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
          // ���ȫ���и�������ɣ���ô��Ӧ�Ļ����ļ�����Ϊnull
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
        attachUnitID = null; // ��ʵֻ��ҪattachUnitIDΪnull�ͱ�ʾ��ȡ������
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
          // line�ĸ�ʽ����<unit ID="XXXX">
          currUnitID[i] = getUnitID(line);
        }
      }

      while (!eof[0]) {
        // ��ʼһ����λ
        writer.write("<unit ID=\"" + currUnitID[0] + "\">");
        writer.write(NEW_LINE);

        String tableID = seq[0];
        writer.write("<table ID=\"" + tableID + "\">");
        writer.write(NEW_LINE);

        LineNumberReader reader = (LineNumberReader) readerList.get(0);
        this.readSameUnitTableDataAndWrite(writer, reader, true);

        writer.write("</table>");
        writer.write(NEW_LINE);

        int readerListCursor = 1; // ��ʾ���ĸ�LineNumberReader��ȡ���ݣ�����ʹ��֮ǰ����Ϊ 1
        for (int i = 2; i < seq.length; ) {
          String temp = seq[i];
          if (temp.equals("<table>")) {
            // ��ʼһ�����
            i++; // �ƶ�����ָ�룬ָ��tableID
            tableID = seq[i];

            i++; // �ƶ�����ָ�룬ָ��tableID�����fileName
            if (seq[i] == null) {
              writer.write("<table ID=\"" + tableID + "\">");
              writer.write(NEW_LINE);

              i++; // ���ĳ��ȫ���и�������ɣ���ôfileName==null
              continue;
            }
            else {
              i++; // �ƶ�����ָ��
            }

            writer.write("<table ID=\"" + tableID + "\">");
            writer.write(NEW_LINE);

            if (!eof[readerListCursor]) {
              reader = (LineNumberReader) readerList.get(
                  readerListCursor);

              if (currUnitID[0].equals(currUnitID[
                                       readerListCursor])) {
                // ͬһ��unit����ôд���ݣ�������д������һ���ٽ��бȽ�
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
            } // �����Readerû�ж�����ȡ����ô�Ͳ�������ֱ�ӽ����ƶ�Reader���α�

            readerListCursor++; // �ƶ�Reader���αָ꣬����һ��
          }
          else if (temp.equals("</table>")) {
            i++;
            writer.write("</table>");
            writer.write(NEW_LINE);
          }
          else if (temp.equals("<float>")) {
            i++; // �ƶ�����ָ�룬ָ��floatID
            String floatID = seq[i];
            i += 2; // �ƶ�����ָ�룬floatID����������floatID��Ӧ��������ʱ�ļ�����λ�ã�������

            reader = (LineNumberReader) readerList.get(
                readerListCursor);

            writer.write("<floatRow ID=\"" + floatID + "\">");
            writer.write(NEW_LINE);

            if (!eof[readerListCursor]) {
              while (currUnitID[0].equals(currUnitID[
                                          readerListCursor])) {
                // ͬһ��unit����ôд���ݣ�������д������һ���ٽ��бȽ�
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
            // ��ʵ�ϳ��򲻻ᵽ������
          }
        }

        // дattachement
        if (currUnitID[0].equals(attachUnitID)) {
          readSameUnitTableDataAndWrite(writer, attachLineReader, true);
          // ��Ϊunit����ֱ�Ӿ���attachment�����Ե���������������������һ��

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

        // �鿴������Ƿ����ļ���ĩβ�����߶�ȡ��һ��unit ID
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
      log.error("IO�쳣", ex);
      throw new ModelException("IO�쳣", ex);
    }
    catch (TaskException ex) {
      log.error("��ȡ�������쳣", ex);
      throw new ModelException("��ȡ�������쳣", ex);
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
      reader.readLine(); // �Ƿ�������һ��<cell field="taskTime" value="XXXX" />������
    }

    boolean sameUnit = true;
    while (sameUnit) {
      String line = reader.readLine();
      if (line.equals("</unit>")) {
        sameUnit = false; // ���unit��ȡ���
      }
      else {
        writer.write(line);
        writer.write(NEW_LINE);
      }
    }
  }

  /**
   * ������<unit ID="XXXX">�ж�ȡunitID
   * @param line             ����
   * @return                 unitID
   */
  private String getUnitID(String line) {
    return line.substring(10, line.length() - 2);
  }

  /**
   * ��������task�����������ݵĻ����ļ���tableID��Map�����и����ʵĴ���
   * @param task         ����
   * @param fileMap      key/value=tableID/fileName
   * @return             ���ʴ���
   * @throws TaskException
   */
  private String[] getFileReadSequence(Task task, Map fileMap) throws
      TaskException {
    List list = new LinkedList();
    String taskID = task.id();

    // ��ӷ����
    String tableID = task.getUnitMetaTable().id();
    list.add(tableID);
    String tableName = NameGenerator.generateDataTableName(taskID, tableID);
    String fileName = (String) fileMap.get(tableName);
    list.add(fileName);

    String metaTableID = tableID;

// ������
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
            // �����Ǹ�����
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
   * ɾ����ʱ�ļ�
   * @param files         ��ʱ�ļ�����
   */
  private void deleteTempFiles(Collection files) {
    Iterator iter = files.iterator();
    while (iter.hasNext()) {
      String fileName = (String) iter.next();

      new File(fileName).delete(); // ɾ���ļ�
    }
  }

  /**
   * �����ݿ��ж�ȡ���ݣ�Ȼ��д����ʱ�ļ�
   *
   * @param task                              ����
   * @param taskTime                          ����ʱ��
   * @param unitIDsIter                       �����ĵ�λID
   * @return Map                              ���ݻ�����ʱ�ļ����ͱ���Map, key/value=(���ݿ��)tableName/filePath
   * @throws ModelException                   �����쳣��
   *                                          Hibenate�쳣��
   *                                          ���ݿ��׳��쳣��
   */
  private Map dbToFile(Task task, TaskTime taskTime, Iterator unitIDsIter,
                       int flag) throws
      ModelException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    Session session = null;

    // ��Ĳ�ѯ���Map, key/value= (���ݿ��)tableName/SQL
    Map sqlMap = null;
    // ��ѯ���������ݻ������ʱ�ļ����ͱ��Map, key/value=(���ݿ��)tableName/filePath
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
        // ����ÿ������һ��SQL��ѯ���
        Map.Entry entry = (Map.Entry) sqlIter.next();
        String sql = (String) entry.getValue();

        con = session.connection();
        pstmt = con.prepareStatement(sql);
        rs = pstmt.executeQuery();

        String tableName = (String) entry.getKey(); // (���ݿ��)tableName

        String fileName = tempFileName(); // ��ʱ�ļ���

        Writer writer = null;

        try {
          writer = new OutputStreamWriter(new FileOutputStream(fileName),
                                          "gb2312");

          dump(rs, writer, tableName, flag); // �������XML��ʽ������ļ�
          fileMap.put(tableName, fileName);
        }
        catch (IOException ioe) {
          log.error("IO�쳣", ioe);
          throw new ModelException("IO�쳣", ioe);
        }
        finally {
          // �ر���Դ
          Util.close(writer);
          HibernateUtil.close(rs, pstmt);
          HibernateUtil.close(session);
        }
      }

      // ����SQL,��д���ļ�
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
      log.error("Hibernate����", he);
      throw new ModelException("Hiberate �쳣", he);
    }
    catch (SQLException sqle) {
      log.error("���ݿ��쳣", sqle);
      throw new ModelException("���ݿ��쳣", sqle);
    }
    catch (TaskException te) {
      log.error("��ȡ�������쳣", te);
      throw new ModelException("��ȡ�������쳣", te);
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
      log.error("���ݿ��쳣", sqle);
      throw new ModelException("���ݿ��쳣", sqle);
    }
    catch (IOException ioe) {
      log.error("IO�쳣", ioe);
      throw new ModelException("IO�쳣", ioe);
    }
    finally {
      // �ر���Դ
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
   * �õ�ȡ�����ݵĲ�ѯ���SQL
   * @param task
   * @param taskTimeID
   * @param unitIDs
   * @return
   * @throws TaskException
   */
  private Map getSQLs(Task task, Integer taskTimeID, String unitIDs) throws
      TaskException {
    Map sqlMap = new HashMap();
    Iterator tableIter = task.getAllTables(); // ȡ�����еı�
    StringBuffer sqlBuff = new StringBuffer(); // ����sql����ʱ��������
    while (tableIter.hasNext()) {
      Table table = (Table) tableIter.next();
      Map tableSQLMap = getSQL(task, taskTimeID, unitIDs,
                               table);
      sqlMap.putAll(tableSQLMap);
    }
    return sqlMap;
  }

  /**
   * �õ������ֵ�
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

    String[] cellNames = new String[count + 1]; // �ֶ���,�ӵڶ�λ��ʼcellNames[0]û�и�ֵ
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // ���ֶ������Ƶ�������
    }

    // дXML
    while (rs.next()) {
      String unitID = rs.getString(1);
      writer.write("<unit ID=\"" + unitID + "\">");
      writer.write(this.NEW_LINE);

      for (int i = 2; i <= count; i++) {
        String cellName = cellNames[i];
        String value = getValueFromResultSet(rs, i,flag);

        Element element = new Element("cell");

        element.setAttribute("field", cellName);
        //�����޸Ĵ����ֵ�
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
      //��ʽ��double
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
   * дxmlͷ��������xml��������Ԫ��taskModel��taskTime
   *
   * @param writer    �����
   * @param taskID    ����ID
   * @param taskTime  ����ʱ��(java.util.Date)����ΪtaskTimeԪ�ص�taskTime�������
   * @throws IOException
   * @return          �����ġ���ꡱ����λ��
   */
  String writeHead(Writer writer, String taskID,
                   java.util.Date taskTime) throws IOException {
    // ��λ�����ʼλ��
    String currentIndent = "";

    writer.write("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
    writer.write(this.NEW_LINE); // ��һ��
    writer.write("<taskModel ID=\"" + taskID + "\">");
    writer.write(this.NEW_LINE);

    currentIndent = increaseIndent(currentIndent); // ���λ�ã�����ꡱ������

    String strTaskTime = Convertor.date2XMLDateTime(taskTime);
    writer.write(currentIndent + "<taskTime taskTime=\"" + strTaskTime +
                 "\">");
    writer.write(this.NEW_LINE);

    currentIndent = increaseIndent(currentIndent); // ���λ�ã�����ꡱ������

    return currentIndent;
  }

  /**
   * д�ļ�β
   *
   * @param writer                     �����
   * @param currentIndent              ��ǰ�������λ��
   * @throws IOException
   */
  void writeEnd(Writer writer, String currentIndent) throws
      IOException {
    writer.write(currentIndent + "</taskTime>" + this.NEW_LINE);
    writer.write("</taskModel>" + this.NEW_LINE);
  }

  /**
   * ǰ������
   * @param currentIndent   ��ǰ����
   * @return   ǰ��һ����λ��Ķ���
   */
  private String increaseIndent(String currentIndent) {
    return currentIndent + XML_INDENT;
  }

  /**
   * ��������
   * @param currentIndent  ��ǰ����
   * @return    ����һ����λ��Ķ���
   */
  private String decreaseIndent(String currentIndent) {
    return currentIndent.substring(XML_INDENT.length());
  }

  /**
   * ���StringBuffer����
   * @param sb         StringBuffer����
   */
  private void clearStringBuffer(StringBuffer sb) {
    sb.delete(0, sb.length());
  }

  /**
   * �õ���λ�Ĵ�����Ϣ�������ӵ�λ������Ϣ
   *
   * @param unit
   *            ��λ��Ϣ
   * @param col
   *            ����
   * @return ����
   */
  private Collection getUnitIDs(UnitTreeNode unit, Collection col) {
    //�������뱾��λ
    col.add( (String) unit.id());

    Iterator itrUnits = unit.getChildren();
    //(ע�⣬û�н����ж�children�Ƿ����)
    while (itrUnits.hasNext()) {
      UnitTreeNode childUnit = (UnitTreeNode) itrUnits.next();
      //�����¼���λ
      getUnitIDs(childUnit, col);
    }

    return col;
  }

  /**
   * ��õ����SQL
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
    String metaTableID = task.getUnitMetaTable().id(); // ����ķ����ID
    StringBuffer sqlBuff = new StringBuffer(); // ����sql����ʱ��������

    String tableID = table.id();
    boolean isMetaTable = metaTableID.equals(tableID);

    Iterator rowIter = table.getRows();
    boolean hasNormalCells = false; // �Ƿ�ȫ���Ǹ����У�û�������̶���

    //��������
    while (rowIter.hasNext()) {
      Row row = (Row) rowIter.next();
      if (row.getFlag(Row.FLAG_FLOAT_ROW)) {
        // ���￼�ǵ��Ǹ�����
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
        clearStringBuffer(sqlBuff); // ���buff���������
      }
      else {
        hasNormalCells = true;
      }
    }

    //����̶���
    if (hasNormalCells) {
      // ��ѯ���SQL���
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
   * �õ�ȡ������Ӧ���SQL���
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
    Iterator tableIter = task.getAllTables(); // ȡ�����еı�
    StringBuffer sqlBuff = new StringBuffer(); // ����sql����ʱ��������
    for (int i = 0; i < tableList.size(); i++) {
      Table table = (Table) tableList.get(i);
      Map tableSQLMap = getSQL(task, taskTimeID, unitIDs,
                               table);
      sqlMap.putAll(tableSQLMap);
    }
    return sqlMap;
  }

  /**
   * �����ݿ��ж�ȡ���ݣ�Ȼ��д����ʱ�ļ�
   *
   * @param task                              ����
   * @param taskTime                          ����ʱ��
   * @param unitIDsIter                       �����ĵ�λID
   * @param tableList                         ��Ҫ�����ı�
   * @return Map                              ���ݻ�����ʱ�ļ����ͱ���Map, key/value=(���ݿ��)tableName/filePath
   * @throws ModelException                   �����쳣��
   *                                          Hibenate�쳣��
   *                                          ���ݿ��׳��쳣��
   */
  private Map dbToFile(Task task, TaskTime taskTime, Iterator unitIDsIter,
                       int flag, ArrayList tableList) throws
      ModelException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    Session session = null;

    // ��Ĳ�ѯ���Map, key/value= (���ݿ��)tableName/SQL
    Map sqlMap = null;
    // ��ѯ���������ݻ������ʱ�ļ����ͱ��Map, key/value=(���ݿ��)tableName/filePath
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
        // ����ÿ������һ��SQL��ѯ���
        Map.Entry entry = (Map.Entry) sqlIter.next();
        String sql = (String) entry.getValue();

        con = session.connection();
        pstmt = con.prepareStatement(sql);
        rs = pstmt.executeQuery();

        String tableName = (String) entry.getKey(); // (���ݿ��)tableName

        String fileName = tempFileName(); // ��ʱ�ļ���

        Writer writer = null;

        try {
          writer = new OutputStreamWriter(new FileOutputStream(fileName),
                                          "gb2312");

          dump(rs, writer, tableName, flag); // �������XML��ʽ������ļ�
          fileMap.put(tableName, fileName);
        }
        catch (IOException ioe) {
          log.error("IO�쳣", ioe);
          throw new ModelException("IO�쳣", ioe);
        }
        finally {
          // �ر���Դ
          Util.close(writer);
          HibernateUtil.close(rs, pstmt);
          HibernateUtil.close(session);
        }
      }

      // ����SQL,��д���ļ�
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
      log.error("Hibernate����", he);
      throw new ModelException("Hiberate �쳣", he);
    }
    catch (SQLException sqle) {
      log.error("���ݿ��쳣", sqle);
      throw new ModelException("���ݿ��쳣", sqle);
    }
    catch (TaskException te) {
      log.error("��ȡ�������쳣", te);
      throw new ModelException("��ȡ�������쳣", te);
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
   * �������������������ݡ������������ܲ�����Ȩ�����⣬���Ȩ�������ڵ��ø÷���ǰ���ǡ�
   * �����xml���ݸ�ʽ���Բμ�taskModel.xsd��
   *
   * <p>
   * ʹ�ð��ɼ��ԣ���Ҫ��¶��DBDataSource.javaʹ�á�
   * </p>
   * @param task                              ����
   * @param taskTime                          ����ʱ��
   * @param unitIDsIter                       �����ĵ�λID
   * @param writer                            XML�����
   * @throws ModelException                   �����쳣��
   *                                          Hibenate�쳣��
   *                                          ���ݿ��׳��쳣��
   */
  public void exportData(Task task, TaskTime taskTime, Iterator unitIDsIter,
                         Writer writer, int flag, ArrayList tableList) throws
      ModelException {
    //long beginTime=(new java.util.Date()).getTime();
    db2XML(task, writer, taskTime, unitIDsIter, flag, tableList);

    /*
         // �����ݿ⵼�뵽��ʱ�ļ�
         Map fileMap = dbToFile(task, taskTime, unitIDsIter, flag, tableList);
         log.debug("Get data from database to File finished");
         // ����ʱ�ļ�д��xml
         fileToWriter(task, taskTime.getFromTime(), fileMap, writer);
         log.debug("Read and output data to output stream finished");
         // deleteTempFiles(fileMap.values()); // ɾ����ʱ�ļ�
         log.debug("Temp files deleted");
         long endTime=(new java.util.Date()).getTime();
     */
  }

  /**
   * �����λXML����,��taskModel.xsd
   * @param unitID ��λID
   * @param taskTime ����ʱ��
   * @param writer   xml �����
   * @param acl ���ʿ���
   * @throws ModelException
   * @throws IOException
   */
  public void exportData(String unitID, TaskTime taskTime, Writer writer,
                         UnitACL acl, ArrayList tableList) throws
      ModelException {
    if (acl.isReadable(unitID)) {
      List list = new LinkedList();
      list.add(unitID);
      // ��������
      exportData(task, taskTime, list.iterator(), writer, SHOWDATA, tableList);
    }
    else {
      // û�е���Ȩ��
      throw new ModelException("����" + unitID + "��λû�е���Ȩ�ޣ���ѡ����ȷ�ĵ�λ��");
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
   * �����ݴ����ݿ���ȡ������ʽ��ΪXML
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


      //����ÿ����λ��������
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
   * �������ݴ����XML�ļ�
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
    String metaTableID = task.getUnitMetaTable().id(); // ����ķ����ID
    StringBuffer sqlBuff = new StringBuffer(); // ����sql����ʱ��������

    String tableID = table.id();
    boolean isMetaTable = metaTableID.equals(tableID);

    Iterator rowIter = table.getRows();
    boolean hasNormalCells = false; // �Ƿ�ȫ���Ǹ����У�û�������̶���

    //��������
    while (rowIter.hasNext()) {
      Row row = (Row) rowIter.next();
      if (row.getFlag(Row.FLAG_FLOAT_ROW)) {
        // ���￼�ǵ��Ǹ�����
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
        clearStringBuffer(sqlBuff); // ���buff���������
      }
      else {
        hasNormalCells = true;
      }
    }

    //����̶���
   // if (hasNormalCells) {
      // ��ѯ���SQL���
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
      //ִ�й̶���
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
   * �������
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

    String[] cellNames = new String[count + 1]; // �ֶ���,�ӵڶ�λ��ʼcellNames[0]û�и�ֵ
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // ���ֶ������Ƶ�������
    }

    String oldUnitID = "";
    StringBuffer strBuffer = new StringBuffer();
    // дXML
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
   * �����ַ���
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
   * �����ַ���
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
   * ȡ������
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

    String[] cellNames = new String[count + 1]; // �ֶ���,�ӵڶ�λ��ʼcellNames[0]û�и�ֵ
    for (int i = 1; i < cellNames.length; i++) {
      cellNames[i] = rsmd.getColumnName(i).toUpperCase(); // ���ֶ������Ƶ�������
    }

    String oldUnitID = "";
    StringBuffer strBuffer = new StringBuffer();
    // дXML
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
