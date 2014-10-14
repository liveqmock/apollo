package cn.com.youtong.apollo.data;

import java.util.*;

import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.script.Table;
import cn.com.youtong.apollo.task.*;
/**
 * ����������
 */

public class TaskData
{
    /**
     * ����
     */
    private Task task;

    /**
     * ����ʱ��
     */
    private TaskTime taskTime;

    /**
     * �������б������Map��keyΪtable��id, valueΪTableData����
     */
    private Map tables;

    /**
     * ��λid
     */
    private String unitID;

	/**
	 * ����Դ������ScriptTable��Ҫ
	 */
	private DataSource dataSource;

	/**
	 * �������ݱ��Ӧ�Ľű���Map
	 */
	private Map scriptTables;

    public TaskData(DataSource dataSource,
					Task task, String unitID,
					TaskTime taskTime, Map tables)
    {
		this.dataSource = dataSource;
        this.task = task;
        this.unitID = unitID;
        this.taskTime = taskTime;
        this.tables = tables;
    }

	/**
	 * �õ����е����ݱ�Map
	 * @return ���е����ݱ�Map��keyΪtable��id, valueΪTableData����
	 */
    public Map getTables()
    {
        return tables;
    }

	/**
	 * �õ��������ݱ��Ӧ�Ľű���Map
	 * @return �������ݱ��Ӧ�Ľű���Map��keyΪtable��id, valueΪcn.com.youtong.apollo.script.Table����
	 */
	public Map getScriptTables()
	{
		if( scriptTables == null )
		{
			scriptTables = new Hashtable();

			try
			{
				for( Iterator iter = getTables().entrySet().iterator();
									 iter.hasNext(); )
				{
					Map.Entry entry = ( Map.Entry ) iter.next();
					String tableID = ( String ) entry.getKey();
					TableData td = ( TableData ) entry.getValue();

					Table table = new Table( task.getTableByID( tableID ), 0,
											 td.getDbfield2valueMap(),
											 getUnitID(),
											 new Time( taskTime ), dataSource );
					td.getDbfield2valueMap();

					scriptTables.put( tableID, table );
				}
			}
			catch( TaskException ex )
			{ // nothing to do,
			}
		}

		return scriptTables;
	}


    public Task getTask()
    {
        return task;
    }

    public TaskTime getTaskTime()
    {
        return taskTime;
    }
    public String getUnitID()
    {
        return unitID;
    }

	public DataSource getDataSource()
	{
		return dataSource;
	}
}