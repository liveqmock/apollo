package cn.com.youtong.apollo.task.db;

import java.io.*;
import java.util.*;

import org.exolab.castor.xml.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import cn.com.youtong.apollo.task.xml.Cell;
import cn.com.youtong.apollo.task.xml.Row;
import cn.com.youtong.apollo.task.xml.Table;
import cn.com.youtong.apollo.task.xml.Task;
import cn.com.youtong.apollo.task.xml.TaskTime;
import net.sf.hibernate.*;

/**
 * 将XML文件解析成Hibernate对应的对象
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBXMLBuilder
{

    /**存储Hibernate对象TaskForm*/
    private TaskForm taskForm;
    /**存储Hibernate对象UnitMetaForm*/
    private UnitMetaForm unitMetaForm;
    /**存储XML对象Task*/
    private Task task;
    /**保存创建数据表的sql语句*/
    private Collection sqls;
    /**保存XSLT信息*/
    private Collection tableViewList = new ArrayList();

    /**
     * 构造函数解析XML文件
     * @param xmlReader 任务参数Reader
     * @throws TaskException 解析失败则抛出异常
     */
    public DBXMLBuilder(Reader xmlReader)
            throws TaskException
    {

        //获得XML解析出来的Task
        try {
            //用Castor将XML文件映射到对象中
            task = Task.unmarshal(xmlReader);
            parseTask(task);
        }
        catch (Exception ex) {
            throw new TaskException("无效的XML文件", ex);
        }
    }

    /**
     * 获得Hibernate对象树TaskForm
     * XML对象不存在则抛出异常
     * @return 对象树taskForm
     * @throws TaskException
     */
    public TaskForm getTaskForm()
            throws TaskException
    {
        return this.taskForm;
    }

    /**
     * 获得HashMap对象
     * @return Map对象保存了 tableID 和 XSLT 字符串
     * @throws TaskException
     */
    public Iterator getTableViews()
            throws TaskException
    {
        return this.tableViewList.iterator();
    }


    /**
     * 获得Hibernate对象树TaskForm
     * XML对象不存在则抛出异常
     * @return 对象树taskForm
     * @throws TaskException
     */
    public Collection getCreateSqls()
            throws TaskException
    {
        return this.sqls;
    }

    /**
     * 解析XML对象生成 TaskForm 与 sql　语句集合
     * @param task castor生成的XML对象
     * @throws TaskException
     */
    private void parseTask(Task task)
            throws TaskException
    {

        if (task == null) {
            throw new TaskException("任务XML文件类型不正确");
        }
        taskForm = new TaskForm();
        unitMetaForm = new UnitMetaForm();
        try {
            //设置基本信息
            taskForm.setTaskID(HibernateUtil.getNextTaskID());
            taskForm.setName(task.getName());
            taskForm.setDateCreated(new Date());
            taskForm.setDateModified(new Date());
            taskForm.setVersion(1);
            taskForm.setMemo(task.getMemo());
            taskForm.setID(task.getID());

            //设置关系
            taskForm.setTaskTimes(getTaskTimeForms(task));
            taskForm.setTables(getTableForms(task));
            //设置UnitMetaForm并存入TaskForm
            unitMetaForm.setTaskID(taskForm.getTaskID());
            unitMetaForm.setTask(taskForm);
            taskForm.setUnitMeta(unitMetaForm);

            //创建Sql 语句
            sqls = Config.getCurrentDatabase().getDataTableCreateSqls(task);
        }
        catch (HibernateException ex) {
            throw new TaskException("转换XML文件到对象树时失败", ex);
        }
        catch (ConfigException ex1) {
            throw new TaskException("配置数据库出错", ex1);
        }
    }

    /**
     * 从XML中解析出的task任务对象得到
     * 对象TableForm的集合
     * @param task 任务对象
     * @return　TableForm的集合
     * @throws HibernateException
     */
    private Set getTableForms(Task task)
            throws HibernateException
    {

        Set result = new HashSet();

        for (int i = 0; i < task.getTableCount(); i++) {
            Table table = task.getTable(i);
            TableForm tableForm = new TableForm();

            //基本信息
            tableForm.setTableID(HibernateUtil.getNextTableID());
            tableForm.setName(table.getName());
            tableForm.setID(table.getID());
            tableForm.setFlag(table.getFlag());

            //设置关系
            tableForm.setTask(taskForm);
            if (table.getIsUnitMetaTable()) {
                unitMetaForm.setUnitMetaTableID(tableForm.getTableID());
            }
            tableForm.setRows(getRowForms(table, tableForm,
                                          table.getIsUnitMetaTable()));

            cn.com.youtong.apollo.task.xml.TableView[] tableViews = table.
                    getTableView();
            for(int j=0; j<tableViews.length;j++)
            {
                cn.com.youtong.apollo.task.xml.TableView tempView = tableViews[j];
                ArrayList tempList = new ArrayList();
                tempList.add(tableForm.getTableID());
                tempList.add(tableForm.getID());
                tempList.add(new Integer(tempView.getType()));
                tempList.add(tempView.getContent());
                tableViewList.add(tempList);
            }

            //放到tableForms 集合中去
            result.add(tableForm);
        }
        return result;
    }

    /**
     * 从XML中解析出的task任务对象得到
     * 对象RowForm的集合
     * @param table XML对象
     * @param tableForm 表对象
     * @param isUnitMeta 是否封面表
     * @return　RowForm的集合
     * @throws HibernateException
     */
    private Set getRowForms(Table table, TableForm tableForm,
                            boolean isUnitMeta)
            throws HibernateException
    {
        Set result = new HashSet();

        for (int i = 0; i < table.getRowCount(); i++) {
            Row row = table.getRow(i);
            RowForm rowForm = new RowForm();
            //设置基本信息
            rowForm.setRowID(HibernateUtil.getNextRowID());
            rowForm.setID(row.getID());
            rowForm.setFlag(row.getFlag());
            rowForm.setIsFloat(getIsFloatFlag(row));

            //设置关系
            rowForm.setTable(tableForm);
            rowForm.setCells(getCellForms(row, rowForm, isUnitMeta));

            //将rowForm保存到集合中
            result.add(rowForm);
        }
        return result;
    }

    /**
     * 得到指定行的浮动行标识
     * @param row 行对象
     * @return 指定行的浮动行标识
     */
    private int getIsFloatFlag(Row row)
    {
        return isFloatRow(row) ? 1 : 0;
    }

    /**
     * 判断指定的行是否是浮动行
     * @param row 行对象
     * @return 是浮动行，返回 true;否则返回false
     */
    private boolean isFloatRow(Row row)
    {
        return (row.getFlag() & cn.com.youtong.apollo.task.Row.FLAG_FLOAT_ROW) >
                0;
    }

    /**
     * 从XML中解析出的task任务对象得到
     * 对象CellForm的集合
     * @param row XML对象
     * @param rowForm 行对象
     * @param isUnitMeta 封面表
     * @return CellForm的集合
     * @throws HibernateException
     */
    private Set getCellForms(Row row, RowForm rowForm, boolean isUnitMeta)
            throws HibernateException
    {
        Set result = new HashSet();
        for (int i = 0; i < row.getCellCount(); i++) {
            Cell cell = row.getCell(i);
            CellForm cellForm = new CellForm();

            //设置基本信息
            cellForm.setCellID(HibernateUtil.getNextCellID());
            cellForm.setDataType(cell.getDataType());
            cellForm.setDbfieldName(cell.getDBFieldName());
            cellForm.setScalarName(cell.getScalarName());
            cellForm.setFlag(cell.getFlag());
            cellForm.setLabel(cell.getLabel());
            cellForm.setWidth(cell.getWidth());
            cellForm.setDictionaryID(cell.getDictionaryID());

            if (isUnitMeta) {
                if (cell.getIsParentUnitCodeCell()) {
                    unitMetaForm.setParentUnitCodeCellID(cellForm.getCellID());
                }
                else if (cell.getIsReportTypeCell()) {
                    unitMetaForm.setReprotTypeCellID(cellForm.getCellID());
                }
                else if (cell.getIsUnitCodeCell()) {
                    unitMetaForm.setUnitCodeCellID(cellForm.getCellID());
                }
                else if (cell.getIsHeadquarterCodeCell()) {
                    unitMetaForm.setHeadquarterCodeCellID(cellForm.getCellID());
                }
                else if (cell.getIsUnitNameCell()) {
                    unitMetaForm.setUnitNameCellID(cellForm.getCellID());
                }
            }

            //设置关系
            cellForm.setRow(rowForm);

            result.add(cellForm);
        }
        return result;
    }

    /**
     * 从XML中解析出的task任务对象得到
     * 对象TaskTimeForm的集合
     * @param task XML对象
     * @return TaskTimeForm的集合
     * @throws HibernateException
     */
    private Set getTaskTimeForms(Task task)
            throws HibernateException
    {
        Set result = new HashSet();

        for (int i = 0; i < task.getTaskTimeCount(); i++) {
            TaskTime taskTime = task.getTaskTime(i);
            TaskTimeForm taskTimeForm = new TaskTimeForm();
            //设置基本信息
            taskTimeForm.setTaskTimeID(HibernateUtil.getNextTaskTimeID());
            taskTimeForm.setBeginTime(taskTime.getBeginTime());
            taskTimeForm.setEndTime(taskTime.getEndTime());
            taskTimeForm.setSubmitBeginTime(taskTime.getSubmitBeginTime());
            taskTimeForm.setSubmitEndTime(taskTime.getSubmitEndTime());
            taskTimeForm.setAttentionBeginTime(taskTime.getAttentionBeginTime());
            taskTimeForm.setAttentionEndTime(taskTime.getAttentionEndTime());
            //设置关系
            taskTimeForm.setTask(taskForm);
            //将taskTimeForm保存到集合中
            result.add(taskTimeForm);
        }
        return result;
    }
}