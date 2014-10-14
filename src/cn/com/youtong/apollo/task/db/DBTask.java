/*
 * Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import java.util.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import java.text.SimpleDateFormat;

/**
 * <p>Title: Task�ӿڵ����ݿⷽʽʵ��</p>
 * <p>Description: �ṩTask�ӿڵ�ʵ��</p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: ����������ͨ
 * </p>
 *
 * @author wjb
 * @version 1.0
 */
public class DBTask implements Task
{
	private Integer _taskID;
	private String _name;
	private String _memo;
	private int _version;
	private ScriptSuit _activeScriptSuit;
	private String _id;
	private Date _createdDate;
	private Date _modifiedDate;

	private UnitMetaTable _unitMetaTable;

	// key/value=tableID[java.lang.String]/table[cn.com.youtong.apollo.task.Table]
	private Map _tableID2tableMap;
	private Collection _taskTimes;
	// key/value=name[java.lang.String]/scriptSuit[cn.com.youtong.apollo.task.ScriptSuit]
	private Map _name2scriptSuitMap;

	/**
	 * ������
	 * @param form    ����Task����������������������ȵ��ӽڵ���Ϣ
	 */
	public DBTask(TaskForm form)
	{
		this._taskID = form.getTaskID();
		this._name = form.getName();
		this._memo = form.getMemo();
		this._version = form.getVersion();
		this._id = form.getID();

		_createdDate = form.getDateCreated();
		_modifiedDate = form.getDateModified();

		_tableID2tableMap = new HashMap();
		// put int tableid table map
		Collection tables = form.getTables();
		for(Iterator iter = tables.iterator(); iter.hasNext(); )
		{
			TableForm temp = (TableForm) iter.next();
			_tableID2tableMap.put(temp.getID(),new DBTable(temp));
		}

		_taskTimes = new LinkedList();
		for(Iterator iter = form.getTaskTimes().iterator(); iter.hasNext(); )
		{
			TaskTimeForm temp = (TaskTimeForm) iter.next();
			_taskTimes.add(new DBTaskTime(temp));
		}

		Collections.sort( (List) _taskTimes, new TaskTimeComparator() );
		_name2scriptSuitMap = new HashMap();
		for( Iterator iter = form.getScriptSuits().iterator(); iter.hasNext(); )
		{
			ScriptSuitForm temp = (ScriptSuitForm) iter.next();
			_name2scriptSuitMap.put(temp.getName(),new DBScriptSuit( temp ) );
		}
		// activeScriptSuit
		_activeScriptSuit = (ScriptSuit) _name2scriptSuitMap.get( form.getActiveScriptSuitName() );
		_unitMetaTable = fetchUnitMetaTable( form );
	}
        /**
         * �õ���������(�ձ����±����������걨��Ѯ��)
         * @return ��������
         */
       public int getTaskType(){
         DBTaskTime dbTaskTime = (DBTaskTime)_taskTimes.iterator().next();
         Date beginTime = dbTaskTime.getFromTime();
         Date endTime = dbTaskTime.getEndTime();
         SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
         SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
         SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
         int begin = Integer.parseInt(dayFormat.format(beginTime));
         int end = Integer.parseInt(dayFormat.format(endTime));
         
         int beginMonth = Integer.parseInt(monthFormat.format(beginTime));
         int endMonth = Integer.parseInt(monthFormat.format(endTime));
         //if(!yearFormat.format(beginTime).equals(yearFormat.format(endTime))){	//�߼��д�
         if (endMonth - beginMonth > 10){
           return this.REPORT_OF_YEAR;
         }else if(!monthFormat.format(beginTime).equals(monthFormat.format(endTime))){
           return this.REPORT_OF_QUARTER;
         }else if(begin == end){
           return this.REPORT_OF_DAY;
         }else if(end-begin >= 27){
           return this.REPORT_OF_MONTH;
         }else{
           return this.REPORT_OF_XUN;
         }

       }
	/*
	 * ��������id
	 * @return ����id
	 * @see cn.com.youtong.apollo.task.Task#getTaskID()
	 */
	public Integer getTaskID()
	{
		return _taskID;
	}

	/*
	 * ������������
	 * @return ��������
	 * @see cn.com.youtong.apollo.task.Task#getName()
	 */
	public String getName()
	{
		return _name;
	}

	/*
	 * ������
	 * @return ������
	 * @see cn.com.youtong.apollo.task.Task#getMemo()
	 */
	public String getMemo()
	{
		return _memo;
	}

	/*
	 * ����İ汾(int��)
	 * @return ����İ汾(int��)
	 * @see cn.com.youtong.apollo.task.Task#getVersion()
	 */
	public int getVersion()
	{
		return _version;
	}

	/*
	 * ��������й�����񣬰��������
	 * @return �������й�����
	 * @see cn.com.youtong.apollo.task.Task#getAllTables()
	 */
	public Iterator getAllTables()
		throws TaskException
	{
		return _tableID2tableMap.values().iterator();
	}

	/*
	 * ���ݱ��id���ұ��
	 * @param ���id(String��)
	 * @return ������
	 * @see cn.com.youtong.apollo.task.Task#getTableByID(java.lang.String)
	 */
	public Table getTableByID(String ID)
		throws TaskException
	{
		return (Table) _tableID2tableMap.get( ID );
	}

	/*
	 * �������������ʱ����Ϣ
	 * @return ���������ʱ����Ϣ
	 * @see cn.com.youtong.apollo.task.Task#getTaskTimes()
	 */
	public Iterator getTaskTimes()
		throws TaskException
	{
		return _taskTimes.iterator();
	}

    /**
     * ȡ��ָ��ID������ʱ��
     * @param taskTimeID ����ʱ��ID
     * @return ָ��ID������ʱ�䡣û���ҵ�������null
     * @throws TaskException if error throw TaskException
     */
    public TaskTime getTaskTime(Integer taskTimeID) throws TaskException
    {
        for(Iterator itr = getTaskTimes(); itr.hasNext(); )
        {
            TaskTime taskTime = (TaskTime) itr.next();

            if(taskTime.getTaskTimeID().equals(taskTimeID))
            {
                return taskTime;
            }
        }
        return null;
    }

	/*
	 * �õ����������ڵ���������
	 * @return   ������������������
	 * @see cn.com.youtong.apollo.task.Task#getTaskTime(java.util.Date)
	 */
	public TaskTime getTaskTime(Date time)
		throws TaskException
	{
		Iterator times = getTaskTimes();

		for(; times.hasNext(); )
		{
			TaskTime temp = (TaskTime) times.next();
			if(isInside(temp.getFromTime(), temp.getEndTime(), time))
			{
				return temp;
			}
		}
		return null;
	}

    /**
     * �õ���������ʹ�õĽű���
     * @return ��������ʹ�õĽű��飬 û�ҵ�����null
     * @throws TaskException ����ʧ��
     */
    public ScriptSuit getActiveScriptSuit() throws TaskException
    {
		return _activeScriptSuit;
    }

	/**
	 * �Ƚ�ʱ���Ƿ�������ʱ���м�
	 * @param begin            ��ʼʱ��
	 * @param end              ĩβʱ��
	 * @param date             ����ʱ��
	 * @return �������ʼʱ���ĩβʱ���м䣬����true������false
	 */
	private boolean isInside(Date begin, Date end, Date date)
	{
		java.util.Date d = (java.util.Date)end.clone();
      	d.setYear(date.getYear());
      	d.setMonth(date.getMonth());
      	d.setDate(date.getDate());
		return (begin.compareTo(d) <= 0 && end.compareTo(d) >= 0);
		
/*            java.util.Calendar d = new GregorianCalendar();
            d.setTime(date);
            java.util.Calendar dBegin = new GregorianCalendar();
            dBegin.setTime(begin);
            java.util.Calendar dEnd = new GregorianCalendar();
            dEnd.setTime(end);
            
            
		return(dBegin.compareTo(d) <= 0 && dEnd.compareTo(d) >= 0);
*/		
	}

	/**
	 * @return �û������
	 * @see cn.com.youtong.apollo.task.Task#getUnitmetaTable()
	 */
	public UnitMetaTable getUnitMetaTable()
	{
		return _unitMetaTable;
	}

	/**
	 * @return    ���ش���ʱ��
	 * @see cn.com.youtong.apollo.task.Task#getCreatedDate()
	 */
	public Date getCreatedDate()
	{
		return _createdDate;
	}

	/*
	 * @return    �޸�ʱ��
	 * @see cn.com.youtong.apollo.task.Task#getModifiedDate()
	 */
	public Date getModifiedDate()
	{
		return _modifiedDate;
	}

	/*
	 * @return   ����id��
	 * @see cn.com.youtong.apollo.task.Task#id()
	 */
	public String id()
	{
		return _id;
	}

	private UnitMetaTable fetchUnitMetaTable( TaskForm form ) {
		UnitMetaForm umForm = form.getUnitMeta();
		TableForm tableForm = null;

		// Get table
		Integer umTableID = umForm.getUnitMetaTableID();

		Collection formTables = form.getTables();

		for(Iterator iter = formTables.iterator(); iter.hasNext(); )
		{
			TableForm temp = (TableForm) iter.next();

			if(temp.getTableID().intValue() == umTableID.intValue())
			{
				tableForm = temp;
				break;
			}
		}

		// Get cells
		Integer unitCodeID = umForm.getUnitCodeCellID();
		Integer reportTypeID = umForm.getReprotTypeCellID();
		Integer parentUnitID = umForm.getParentUnitCodeCellID();
		Integer hqCodeID = umForm.getHeadquarterCodeCellID();
		Integer unitNameID = umForm.getUnitNameCellID();
		Integer displayID = umForm.getDisplayCellID();

		CellForm unitCodeCell = null;
		CellForm reportTypeCell = null;
		CellForm parentUnitCell = null;
		CellForm hqCodeCell = null;
		CellForm unitNameCell = null;
		CellForm displayCell = null;

		CellForm[] unitMetaCells = new CellForm[] {
								   unitCodeCell, reportTypeCell,
								   parentUnitCell, hqCodeCell,
								   unitNameCell,displayCell };
		Integer[] ids = new Integer[] {
						unitCodeID, reportTypeID,
						parentUnitID, hqCodeID,
						unitNameID,displayID };

		boolean done = false;
		// indicate unit meta cells have been fetched

		Collection rows = tableForm.getRows();
		Iterator rowIter = rows.iterator();
		while(rowIter.hasNext())
		{
			Collection cells = ((RowForm) rowIter.next()).getCells();

			// if not done, continue to fetch them
			done = (unitMetaCells[0] != null)
				   && (unitMetaCells[1] != null)
				   && (unitMetaCells[2] != null)
				   && (unitMetaCells[3] != null)
				   && (unitMetaCells[4] != null
				   && (unitMetaCells[5] != null));
			if( !done )
			{
				unitMetaCells = fetchUnitMetaCells(cells, ids, unitMetaCells);
			}
			else
			{
				break;
			}
		}

		return new DBUnitMetaTable(
				  tableForm,
				  unitMetaCells[0],
				  unitMetaCells[1],
				  unitMetaCells[2],
				  unitMetaCells[3],
				  unitMetaCells[4],
				  unitMetaCells[5]);
	}

	private CellForm[] fetchUnitMetaCells(Collection cells, Integer[] ids, CellForm[] array)
	{
		// indicate not fetched
		boolean unitCodeN = (array[0] == null);
		boolean reportTypeN = (array[1] == null);
		boolean parentUnitIDN = (array[2] == null);
		boolean hqCodeIDN = (array[3] == null);
		boolean unitNameIDN = (array[4] == null);
		boolean displayN = (array[5]==null);

		for(Iterator cellIter = cells.iterator(); cellIter.hasNext(); )
		{
			CellForm cell = (CellForm) cellIter.next();
			Integer cellID = cell.getCellID();

			if(unitCodeN)
			{
				if(cellID.equals(ids[0]))
				{
					array[0] = cell;
					continue;
				}
			}
			if(reportTypeN)
			{
				if(cellID.equals(ids[1]))
				{
					array[1] = cell;
					continue;
				}
			}
			if(parentUnitIDN)
			{
				if(cellID.equals(ids[2]))
				{
					array[2] = cell;
					continue;
				}
			}
			if(hqCodeIDN)
			{
				if(cellID.equals(ids[3]))
				{
					array[3] = cell;
					continue;
				}
			}
			if(unitNameIDN)
			{
				if(cellID.equals(ids[4]))
				{
					array[4] = cell;
//					continue;
				}
			}
			if(displayN)
			{
				if(cellID.equals(ids[5]))
				{
					array[5] = cell;
					continue;
				}
			}
		}

		return array;
	}

    /**
     * �õ��������еĽű���
     * @return �������еĽű���ScriptSuit���ϵ�Iterator
     * @throws TaskException ����ʧ��
     */
    public Iterator getAllScriptSuits() throws TaskException
    {
        return _name2scriptSuitMap.values().iterator();
    }

    /**
     * �õ�ָ�����ƵĽű���
     * @param name �ű�������
     * @return ָ���Ľű��飬 û�ҵ�����null
     * @throws TaskException ����ʧ��
     */
    public ScriptSuit getScriptSuit(String name) throws TaskException
    {
		return (ScriptSuit) _name2scriptSuitMap.get( name );
    }

}