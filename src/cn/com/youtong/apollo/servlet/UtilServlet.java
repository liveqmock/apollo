package cn.com.youtong.apollo.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.servlet.unittree.*;
import cn.com.youtong.apollo.serialization.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.services.*;
import java.lang.reflect.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.SetOfPrivileges;
import cn.com.youtong.apollo.servlet.unittree.AddressInfoTree;
import cn.com.youtong.apollo.dictionary.DictionaryManager;
import cn.com.youtong.apollo.servlet.dictionarytree.DefaultDictionaryTree;
import cn.com.youtong.apollo.dictionary.DictionaryManagerFactory;

public class UtilServlet
    extends RootServlet {

  public static final String SHOW_TREE = "showTree";
  public static final String SHOW_DICTIONARY_TREE = "showDictionaryTree";
  public static final String SHOW_HOME_PAGE = "showHomePage";
  public static final String DOWN_EXCEL = "downExcel";
  public static final String SEARCH_UNIT_PAGE =
      "/jsp/modelManager/searchUnit.jsp";
  public static final String SHOW_SEARCH_UNIT = "showSearchUnit";
  public static final String SELECT_UNIT = "selectUnit";
  public static final String SELECT_UNIT_PAGE =
      "/jsp/modelManager/selectUnit.jsp";
  public static final String SELECT_UNIT_HEAD = "selectUnitHead";
  public static final String SELECT_UNIT_HEAD_PAGE =
      "/jsp/modelManager/selectUnitHead.jsp";

  //打印单位树
  public static final String PRINT_UNITTREE = "print_unittree";
  //
  public static final String PRINT_UNITTREE_PAGE =
      "/jsp/modelManager/printunittree.jsp";

  /**
   * 分发页面请求。
   * @param req
   * @param res
   * @throws cn.com.youtong.formserver.common.Warning
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   */
  public void perform(HttpServletRequest request,
                      HttpServletResponse response) throws Warning,
      IOException, ServletException {
    String operation = request.getParameter("operation");

    if (operation == null) {
      throw new Warning("无效的参数operation = " + operation);
    }

    if (operation.equalsIgnoreCase(SHOW_TREE)) {
      showTree(request, response);
      return;
    }
    if(operation.equalsIgnoreCase("showTreeOfShow")){
    	showTreeOfShow(request,response);
    	return;
    }
    if (operation.equalsIgnoreCase(SHOW_HOME_PAGE)) {
      showHomePage(request, response);
      return;
    }
    if (operation.equalsIgnoreCase(SHOW_DICTIONARY_TREE)) {
      showDictionaryTree(request, response);
      return;
    }
    if (operation.equalsIgnoreCase(DOWN_EXCEL)) {
      dumpExcel(request, response);
      return;
    }
    else if (operation.equalsIgnoreCase(SHOW_SEARCH_UNIT)) {
      showSearchUnitPage(request, response);
      return;
    }
    else if (operation.equalsIgnoreCase(SELECT_UNIT)) {
      selectUnit(request, response);
      return;
    }
    else if (operation.equalsIgnoreCase(SELECT_UNIT_HEAD)) {
      selectUnitHead(request, response);
      return;
    }
    else if (operation.equalsIgnoreCase(PRINT_UNITTREE)) {
       showAllUnitTreeXML(request, response);
      return;
    }

    else {
      throw new Warning("无效的参数operation = " + operation);
    }
  }

  public static void CreateUnitTreeInfo(){
	  
  }
  
  
  /**
   * 显示代码字典树
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showDefaultDictionaryTree(HttpServletRequest req,
                                               HttpServletResponse res) throws
      ServletException, IOException, Warning {

    //得到对象树
    String dictionaryID = req.getParameter("dictionaryID");
    DictionaryManager dictionaryManager = ( (DictionaryManagerFactory) Factory.
                                           getInstance(DictionaryManagerFactory.class.
        getName())).createDictionaryManager();
    cn.com.youtong.apollo.dictionary.Dictionary dictionary = dictionaryManager.
        getDictionaryByID(dictionaryID);
    DefaultDictionaryTree dictionaryTree = new DefaultDictionaryTree();
    String tree = dictionaryTree.getDictionaryForest(dictionaryID,
        dictionary.getName(), dictionary.getRootItems().hasNext());
    req.setAttribute("defaultDictionaryTree", tree);
  }

  /**
   * 显示代码字典树
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  private synchronized void showDictionaryTree(HttpServletRequest req,
                                               HttpServletResponse res) throws
      ServletException, IOException, Warning {

    //得到对象树
    String dictionaryID = req.getParameter("dictionaryID");
    DictionaryManager dictionaryManager = ( (DictionaryManagerFactory) Factory.
                                           getInstance(DictionaryManagerFactory.class.
        getName())).createDictionaryManager();
    cn.com.youtong.apollo.dictionary.Dictionary dictionary = dictionaryManager.
        getDictionaryByID(dictionaryID);
    DefaultDictionaryTree dictionaryTree = new DefaultDictionaryTree();
    String Tree = "";
    if (req.getParameter("key") != null) {
      Tree = dictionaryTree.getDictionaryTree(dictionaryID,
                                              dictionary.
                                              getChildItems(req.
          getParameter("key")));
    }
    else {
      Tree = dictionaryTree.getDictionaryTree(dictionaryID,
                                              dictionary.getRootItems());
    }
    res.setContentType("text/xml; charset=gb2312");
    Writer writer = res.getWriter();
    writer.write(Tree);
    writer.flush();
    writer.close();
  }

  /**
   * 显示树
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  private synchronized void showTree(HttpServletRequest req,
                                     HttpServletResponse res) throws
      ServletException, IOException, Warning {

    //得到对象树
    String taskID = req.getParameter("taskID");
    String treeName = req.getParameter("treeName");
    String[] checkedUnitIDs = null;
    if (req.getParameter("checkedUnitIDs") != null) {
      checkedUnitIDs = req.getParameterValues("checkedUnitIDs");
    }
    Object[] param = {
        taskID};
    UnitTree tree = null;
    try {
      tree = (UnitTree) Class.forName(treeName).getConstructors()[0].
          newInstance(param);
    }
    catch (SecurityException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }
    catch (InvocationTargetException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }
    catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }
    catch (IllegalAccessException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }
    catch (InstantiationException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace();
      throw new Warning(ex);
    }

    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);
    String unitTree = tree.getUnitTree(modelManager.getUnitTreeManager().
                                       getUnitTree(req.getParameter(
        "unitID")), checkedUnitIDs);

    res.setContentType("text/xml; charset=gb2312");
    Writer writer = res.getWriter();
    writer.write(unitTree);
    writer.flush();
    writer.close();
  }
  
  private synchronized void showTreeOfShow(HttpServletRequest req,
          HttpServletResponse res) throws
ServletException, IOException, Warning {

//得到对象树
String taskID = req.getParameter("taskID");
String treeName = req.getParameter("treeName");
String[] checkedUnitIDs = null;
if (req.getParameter("checkedUnitIDs") != null) {
checkedUnitIDs = req.getParameterValues("checkedUnitIDs");
}
Object[] param = {
taskID};
UnitTree tree = null;
try {
tree = (UnitTree) Class.forName(treeName).getConstructors()[0].
newInstance(param);
}
catch (SecurityException ex) {
ex.printStackTrace();
throw new Warning(ex);
}
catch (InvocationTargetException ex) {
ex.printStackTrace();
throw new Warning(ex);
}
catch (IllegalArgumentException ex) {
ex.printStackTrace();
throw new Warning(ex);
}
catch (IllegalAccessException ex) {
ex.printStackTrace();
throw new Warning(ex);
}
catch (InstantiationException ex) {
ex.printStackTrace();
throw new Warning(ex);
}
catch (ClassNotFoundException ex) {
ex.printStackTrace();
throw new Warning(ex);
}

ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
ModelManagerFactory.class.getName())).createModelManager(taskID);
String unitTree = tree.getUnitTreeOfShow(modelManager.getUnitTreeManager().
            getUnitTree(req.getParameter(
"unitID")), checkedUnitIDs);

res.setContentType("text/xml; charset=gb2312");
Writer writer = res.getWriter();
writer.write(unitTree);
writer.flush();
writer.close();
}

  /**
   * 显示checkbox单位树
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showCheckboxTree(HttpServletRequest req,
                                      HttpServletResponse res,
                                      String functionName) throws
      ServletException, IOException, Warning {
    String taskID = (String) req.getSession().getAttribute("taskID");
    TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
        TaskManagerFactory.class.getName())).createTaskManager();
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);

    UnitCheckboxTree tree = new UnitCheckboxTree(taskID);
    String unitTree = "";

    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().
                                  getUnitForest(unitACL), null,
                                  functionName);

    req.setAttribute("checkboxUnitTree", unitTree);
  }
  
  public static void showCheckboxTreeOfShow(HttpServletRequest req,
          HttpServletResponse res,
          String functionName) throws
ServletException, IOException, Warning {
String taskID = (String) req.getSession().getAttribute("taskID");
TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
TaskManagerFactory.class.getName())).createTaskManager();
ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
ModelManagerFactory.class.getName())).createModelManager(taskID);

UnitCheckboxTree tree = new UnitCheckboxTree(taskID);
String unitTree = "";

UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
unitTree = tree.getUnitForestOfShow(modelManager.getUnitTreeManager().
      getUnitForest(), null,
      functionName);
unitTree+=" <script>tree1.setChecked(false);</script>";

req.setAttribute("checkboxUnitTree", unitTree);
}

  /**
   * 显示radio单位树
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showRadioTree(HttpServletRequest req,
                                   HttpServletResponse res,
                                   String functionName) throws
      ServletException, IOException, Warning {
    String taskID = (String) req.getSession().getAttribute("taskID");
    TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
        TaskManagerFactory.class.getName())).createTaskManager();
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);

    ScalarQueryTree tree = new ScalarQueryTree(taskID);
    String unitTree = "";

    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().
                                  getUnitForest(unitACL), null,
                                  functionName);
    req.setAttribute("radioUnitTree", unitTree);
  }

  /**
   * 显示分配单位权限的checkbox单位树，全部单位
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showCheckboxTreeOfRight(HttpServletRequest req,
                                             HttpServletResponse res,
                                             String functionName) throws
      ServletException, IOException, Warning {
    String taskID = (String) req.getSession().getAttribute("taskID");
    TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
        TaskManagerFactory.class.getName())).createTaskManager();
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);

    UnitCheckboxTree tree = new UnitCheckboxTree(taskID);
    String unitTree = "";

    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().
                                  getUnitForest(),null,functionName);

    req.setAttribute("checkboxUnitTree", unitTree);
  }

  /**
   * 显示普通单位树，全部单位
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showAllUnitTree(HttpServletRequest req,
                                     HttpServletResponse res) throws
      ServletException, IOException, Warning {
    String taskID = (String) req.getSession().getAttribute("taskID");
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);
    String unitTree = "";
    AddressInfoTree tree = new AddressInfoTree(taskID);

    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().
                                  getUnitForest(), null);

    req.setAttribute("unitTree", unitTree);

  }

  /**
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public void showAllUnitTreeXML(HttpServletRequest req,
                                 HttpServletResponse res) throws
      ServletException, IOException, Warning {

    String taskID = (String) req.getParameter("taskID");
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);

    Iterator treeItr = modelManager.getUnitTreeManager().getUnitForest();
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
    strBuf.append("<tree>");
    while (treeItr.hasNext()) {
      UnitTreeNode unitTreeNode = (UnitTreeNode) treeItr.next();
      strBuf.append(getUnitTreeNodeXML(unitTreeNode));
    }
    strBuf.append("</tree>");
    //request.setAttribute("unittree",strBuf.toString());
    res.reset();
    res.setContentType("text/xml; charset=gb2312");
    Writer writer = res.getWriter();
    writer.write(strBuf.toString());
    writer.flush();
    writer.close();
  }

  private String getUnitTreeNodeXML(UnitTreeNode unitTreeNode) {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("<tree text=\"" + unitTreeNode.getUnitName() + "\">");
    Iterator treeItr = unitTreeNode.getChildren();
    while (treeItr.hasNext()) {
      UnitTreeNode childUnitTreeNode = (UnitTreeNode) treeItr.next();
      strBuf.append(getUnitTreeNodeXML(childUnitTreeNode));
    }

    strBuf.append("</tree>");
    return strBuf.toString();
  }

  /**
   * 显示普通单位树(changeUnit带unitName)
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  public static void showUnitTree(HttpServletRequest req,
                                  HttpServletResponse res) throws
      ServletException, IOException, Warning {
    String taskID = (String) req.getSession().getAttribute("taskID");
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
    String unitTree = "";
    AddressInfoTree tree = new AddressInfoTree(taskID);

    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().getUnitForest(unitACL), null);

    req.setAttribute("unitTree", unitTree);
  }

  /**
   * 显示主页
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   * @throws Warning
   */
  private void showHomePage(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    this.go2HomePage(req, res);
  }

  /**
   * 获得用于显示数据的HTML代码
   * 若该任务的XSLT文件没找到则返回Iterator,isEmpty为空的对象，不报异常
   * @param xmlDataInput XML形式的数据输入流
   * @return TableViewer对象的集合，该对象包含：表名与显示表的HTML代码
   * @throws ModelException
   */
  public static Iterator getHtmlCodeForShowTable(InputStream xmlDataInput,
                                                 Task task) throws
      ServletException, IOException, Warning {
    Collection result = new TreeSet();

    InputStream in = null;
    ByteArrayOutputStream out = null;

    //将输入流转换为字符串保存在内存里，以便重复调用
    String strXML = Convertor.Stream2String(xmlDataInput);
    if (strXML == null) {
      throw new ModelException("数据输入不正确");
    }

    TaskManager taskManager = ( (TaskManagerFactory) Factory.
                               getInstance(TaskManagerFactory.class.
                                           getName())).createTaskManager();
    Iterator tables = task.getAllTables();
    while (tables.hasNext()) {
      Table tempTable = (Table) tables.next();

      Serializer htmlSerializer = new HTMLSerializer();
// modified by xxh 2009-5-13
      in = new ByteArrayInputStream(strXML.getBytes("gb2312"));
      out = new ByteArrayOutputStream();

      htmlSerializer.serializeTable(in, tempTable, out);

      DBTableViewer dbTableViewer = new DBTableViewer();
      dbTableViewer.setTaskID(task.id());
      dbTableViewer.setTaskName(task.getName());
      dbTableViewer.setTableName(tempTable.getName());
      dbTableViewer.setTableID(tempTable.id());
      dbTableViewer.setIntID(tempTable.getTableID());
      dbTableViewer.setHtmlString(out.toString("gb2312"));

      result.add(dbTableViewer);
    }

    Util.close(in);
    Util.close(out);

    return result.iterator();
  }

  /**
   * 得到单位树html代码
   * @param req
   * @param res
   * @param unitIDs 指定树中选中节点的单位ID
   * @param attributeName 设置的属性名称，如果没有或者长度为0，使用名称: checkboxUnitTree
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  public static void showCheckboxUnitTree(HttpServletRequest req,
                                          HttpServletResponse res,
                                          String[] unitIDs,
                                          String attributeName) throws
      Warning, IOException, ServletException {
    if (Util.isEmptyString(attributeName)) {
      attributeName = "checkboxUnitTree";

    }
    Task task = (Task) req.getAttribute("task");

    String taskID = req.getParameter("taskID");
    if (!Util.isEmptyString(taskID)) {
      TaskManager mng = ( (TaskManagerFactory) Factory.getInstance(
          TaskManagerFactory.class.getName())).createTaskManager();
      task = mng.getTaskByID(taskID);
    }

    if (task == null) {
      task = (Task) req.getSession().getAttribute("task");

      if (task == null) {
        taskID = (String) req.getAttribute("taskID");
        if (!Util.isEmptyString(taskID)) {
          TaskManager mng = ( (TaskManagerFactory) Factory.getInstance(
              TaskManagerFactory.class.getName())).createTaskManager();
          task = mng.getTaskByID(taskID);
        }
      }
    }

    if (task == null) {
      return;
    }

    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(task.id());

    UnitTreeManager treeMng = modelManager.getUnitTreeManager();

    UnitCheckboxTree tree = new UnitCheckboxTree(task.id());
    String unitTree = "";
    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
    unitTree = tree.getUnitForest(treeMng.getUnitForest(unitACL),
                                  unitIDs);

    if (unitIDs != null) {
      req.setAttribute("units", treeMng.getUnits(unitIDs));
    }

    req.setAttribute(attributeName, unitTree);
  }
  
  public static void showCheckboxUnitTreeOfShow(HttpServletRequest req,
          HttpServletResponse res,String[] unitIDs,String attributeName) throws Warning, IOException, ServletException {
		if (Util.isEmptyString(attributeName)) {
		attributeName = "checkboxUnitTree";
		
		}
		Task task = (Task) req.getAttribute("task");
		
		String taskID = req.getParameter("taskID");
		if (!Util.isEmptyString(taskID)) {
		TaskManager mng = ( (TaskManagerFactory) Factory.getInstance(
		TaskManagerFactory.class.getName())).createTaskManager();
		task = mng.getTaskByID(taskID);
		}
		
		if (task == null) {
		task = (Task) req.getSession().getAttribute("task");
		
		if (task == null) {
		taskID = (String) req.getAttribute("taskID");
		if (!Util.isEmptyString(taskID)) {
		TaskManager mng = ( (TaskManagerFactory) Factory.getInstance(
		TaskManagerFactory.class.getName())).createTaskManager();
		task = mng.getTaskByID(taskID);
		}
		}
		}
		
		if (task == null) {
		return;
		}
		
		ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
		ModelManagerFactory.class.getName())).createModelManager(task.id());
		
		UnitTreeManager treeMng = modelManager.getUnitTreeManager();
		
		UnitCheckboxTree tree = new UnitCheckboxTree(task.id());
		String unitTree = "";
		UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));
		unitTree = tree.getUnitForestOfShow(treeMng.getUnitForest(unitACL),
		  unitIDs,"");
		unitTree+=" <script>tree1.setChecked(false);</script>";
		
		if (unitIDs != null) {
		req.setAttribute("units", treeMng.getUnits(unitIDs));
		}
		
		req.setAttribute(attributeName, unitTree);
	}

  /**
   * 检查某个checkbox是否被选中
   * @param request         请求
   * @param boxHtmlName     checkbox的html代码名称
   * @return                true/false
   */
  static boolean isCheckboxChecked(HttpServletRequest request,
                                   String boxHtmlName) {
    String sChecked = request.getParameter(boxHtmlName);

    if (Util.isEmptyString(sChecked)) {
      return false;
    }

    if (sChecked.equalsIgnoreCase("on")
        || sChecked.equalsIgnoreCase("yes")
        || sChecked.equalsIgnoreCase("true")) {
      return true;
    }

    return false;
  }

  private void dumpExcel(HttpServletRequest req,
                         HttpServletResponse res) throws Warning, IOException,
      ServletException {
    String fileName = (String) req.getSession().getAttribute("fileName");
    //System.out.println( "filename = " + fileName );
    //req.getSession().removeAttribute( "fileName" ); // remove

    String absoluteFileName = Config.getString(
        "cn.com.youtong.apollo.excel.directory")
        + File.separator + fileName;

    if (fileName.endsWith("xls")
        || fileName.endsWith("XLS")) {
      res.setContentType("application/vnd.ms-excel; charset=gb2312");
      res.setHeader("Content-disposition",
                    "attachment; filename=excels.xls");
    }
    else if (fileName.endsWith("zip")
             || fileName.endsWith("ZIP")) {
      res.setContentType("application/x-zip-compressed; charset=gb2312");
      res.setHeader("Content-disposition",
                    "attachment; filename=excels.zip");
    }

    UtilServlet.dumpFileContent(res.getOutputStream(), absoluteFileName);
  }

  private void showSearchUnitPage(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {
    String taskID = (String) req.getSession().getAttribute("taskID");

    if (Util.isEmptyString(taskID)) {
      taskID = req.getParameter("taskID");

    }
    TaskManager taskMng = ( (TaskManagerFactory) Factory.getInstance(
        TaskManagerFactory.class.getName())).createTaskManager();
    ModelManager modelMng = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);
    UnitTreeManager treeMng = modelMng.getUnitTreeManager();

    UnitACL unitACL = modelMng.getUnitACL(RootServlet.getLoginUser(req));

    Collection nodesCol = new LinkedList();

    // 向nodesCol里面赋值值
    for (Iterator iter = treeMng.getUnitForest(unitACL);
         iter.hasNext(); ) {
      UnitTreeNode node = (UnitTreeNode) iter.next();
      UnitTreeNodeUtil.getAllChildren(nodesCol, node);
    }

    req.setAttribute("nodes", nodesCol);
    go2UrlWithAttibute(req, res, SEARCH_UNIT_PAGE);
  }

  /**
   * 将文件file 内容读取出来，然后写入<code>OutputStream</code>
   *
   * @param out OutputStream
   * @param file String 文件名
   * @throws IOException exception
   */
  public static void dumpFileContent(OutputStream out, String file) throws
      IOException {
    File tmpfile=new File(file);
    FileInputStream fis = new FileInputStream(tmpfile);
    // 写入数据流
    try {
      int len;
      byte buf[] = new byte[1024];
      while ( (len = fis.read(buf, 0, 1024)) != -1) {
        out.write(buf, 0, len);
      }
      out.flush();

    }
    catch(Exception ex)
    {

    }
    finally {
      if (fis != null) {
        try {
          fis.close();
          tmpfile.delete();
        }
        catch (IOException ioe) {
        }
        finally
            {

        }
      }
    }

  }

  private void selectUnit(HttpServletRequest req,
                          HttpServletResponse res) throws Warning, IOException,
      ServletException {
    go2UrlWithAttibute(req, res, SELECT_UNIT_PAGE);
  }

  private void selectUnitHead(HttpServletRequest req,
                              HttpServletResponse res) throws Warning,
      IOException, ServletException {
    go2UrlWithAttibute(req, res, SELECT_UNIT_HEAD_PAGE);
  }

}
