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
 * ��XML�ļ�������Hibernate��Ӧ�Ķ���
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBXMLBuilder
{

    /**�洢Hibernate����TaskForm*/
    private TaskForm taskForm;
    /**�洢Hibernate����UnitMetaForm*/
    private UnitMetaForm unitMetaForm;
    /**�洢XML����Task*/
    private Task task;
    /**���洴�����ݱ��sql���*/
    private Collection sqls;
    /**����XSLT��Ϣ*/
    private Collection tableViewList = new ArrayList();

    /**
     * ���캯������XML�ļ�
     * @param xmlReader �������Reader
     * @throws TaskException ����ʧ�����׳��쳣
     */
    public DBXMLBuilder(Reader xmlReader)
            throws TaskException
    {

        //���XML����������Task
        try {
            //��Castor��XML�ļ�ӳ�䵽������
            task = Task.unmarshal(xmlReader);
            parseTask(task);
        }
        catch (Exception ex) {
            throw new TaskException("��Ч��XML�ļ�", ex);
        }
    }

    /**
     * ���Hibernate������TaskForm
     * XML���󲻴������׳��쳣
     * @return ������taskForm
     * @throws TaskException
     */
    public TaskForm getTaskForm()
            throws TaskException
    {
        return this.taskForm;
    }

    /**
     * ���HashMap����
     * @return Map���󱣴��� tableID �� XSLT �ַ���
     * @throws TaskException
     */
    public Iterator getTableViews()
            throws TaskException
    {
        return this.tableViewList.iterator();
    }


    /**
     * ���Hibernate������TaskForm
     * XML���󲻴������׳��쳣
     * @return ������taskForm
     * @throws TaskException
     */
    public Collection getCreateSqls()
            throws TaskException
    {
        return this.sqls;
    }

    /**
     * ����XML�������� TaskForm �� sql����伯��
     * @param task castor���ɵ�XML����
     * @throws TaskException
     */
    private void parseTask(Task task)
            throws TaskException
    {

        if (task == null) {
            throw new TaskException("����XML�ļ����Ͳ���ȷ");
        }
        taskForm = new TaskForm();
        unitMetaForm = new UnitMetaForm();
        try {
            //���û�����Ϣ
            taskForm.setTaskID(HibernateUtil.getNextTaskID());
            taskForm.setName(task.getName());
            taskForm.setDateCreated(new Date());
            taskForm.setDateModified(new Date());
            taskForm.setVersion(1);
            taskForm.setMemo(task.getMemo());
            taskForm.setID(task.getID());

            //���ù�ϵ
            taskForm.setTaskTimes(getTaskTimeForms(task));
            taskForm.setTables(getTableForms(task));
            //����UnitMetaForm������TaskForm
            unitMetaForm.setTaskID(taskForm.getTaskID());
            unitMetaForm.setTask(taskForm);
            taskForm.setUnitMeta(unitMetaForm);

            //����Sql ���
            sqls = Config.getCurrentDatabase().getDataTableCreateSqls(task);
        }
        catch (HibernateException ex) {
            throw new TaskException("ת��XML�ļ���������ʱʧ��", ex);
        }
        catch (ConfigException ex1) {
            throw new TaskException("�������ݿ����", ex1);
        }
    }

    /**
     * ��XML�н�������task�������õ�
     * ����TableForm�ļ���
     * @param task �������
     * @return��TableForm�ļ���
     * @throws HibernateException
     */
    private Set getTableForms(Task task)
            throws HibernateException
    {

        Set result = new HashSet();

        for (int i = 0; i < task.getTableCount(); i++) {
            Table table = task.getTable(i);
            TableForm tableForm = new TableForm();

            //������Ϣ
            tableForm.setTableID(HibernateUtil.getNextTableID());
            tableForm.setName(table.getName());
            tableForm.setID(table.getID());
            tableForm.setFlag(table.getFlag());

            //���ù�ϵ
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

            //�ŵ�tableForms ������ȥ
            result.add(tableForm);
        }
        return result;
    }

    /**
     * ��XML�н�������task�������õ�
     * ����RowForm�ļ���
     * @param table XML����
     * @param tableForm �����
     * @param isUnitMeta �Ƿ�����
     * @return��RowForm�ļ���
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
            //���û�����Ϣ
            rowForm.setRowID(HibernateUtil.getNextRowID());
            rowForm.setID(row.getID());
            rowForm.setFlag(row.getFlag());
            rowForm.setIsFloat(getIsFloatFlag(row));

            //���ù�ϵ
            rowForm.setTable(tableForm);
            rowForm.setCells(getCellForms(row, rowForm, isUnitMeta));

            //��rowForm���浽������
            result.add(rowForm);
        }
        return result;
    }

    /**
     * �õ�ָ���еĸ����б�ʶ
     * @param row �ж���
     * @return ָ���еĸ����б�ʶ
     */
    private int getIsFloatFlag(Row row)
    {
        return isFloatRow(row) ? 1 : 0;
    }

    /**
     * �ж�ָ�������Ƿ��Ǹ�����
     * @param row �ж���
     * @return �Ǹ����У����� true;���򷵻�false
     */
    private boolean isFloatRow(Row row)
    {
        return (row.getFlag() & cn.com.youtong.apollo.task.Row.FLAG_FLOAT_ROW) >
                0;
    }

    /**
     * ��XML�н�������task�������õ�
     * ����CellForm�ļ���
     * @param row XML����
     * @param rowForm �ж���
     * @param isUnitMeta �����
     * @return CellForm�ļ���
     * @throws HibernateException
     */
    private Set getCellForms(Row row, RowForm rowForm, boolean isUnitMeta)
            throws HibernateException
    {
        Set result = new HashSet();
        for (int i = 0; i < row.getCellCount(); i++) {
            Cell cell = row.getCell(i);
            CellForm cellForm = new CellForm();

            //���û�����Ϣ
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

            //���ù�ϵ
            cellForm.setRow(rowForm);

            result.add(cellForm);
        }
        return result;
    }

    /**
     * ��XML�н�������task�������õ�
     * ����TaskTimeForm�ļ���
     * @param task XML����
     * @return TaskTimeForm�ļ���
     * @throws HibernateException
     */
    private Set getTaskTimeForms(Task task)
            throws HibernateException
    {
        Set result = new HashSet();

        for (int i = 0; i < task.getTaskTimeCount(); i++) {
            TaskTime taskTime = task.getTaskTime(i);
            TaskTimeForm taskTimeForm = new TaskTimeForm();
            //���û�����Ϣ
            taskTimeForm.setTaskTimeID(HibernateUtil.getNextTaskTimeID());
            taskTimeForm.setBeginTime(taskTime.getBeginTime());
            taskTimeForm.setEndTime(taskTime.getEndTime());
            taskTimeForm.setSubmitBeginTime(taskTime.getSubmitBeginTime());
            taskTimeForm.setSubmitEndTime(taskTime.getSubmitEndTime());
            taskTimeForm.setAttentionBeginTime(taskTime.getAttentionBeginTime());
            taskTimeForm.setAttentionEndTime(taskTime.getAttentionEndTime());
            //���ù�ϵ
            taskTimeForm.setTask(taskForm);
            //��taskTimeForm���浽������
            result.add(taskTimeForm);
        }
        return result;
    }
}