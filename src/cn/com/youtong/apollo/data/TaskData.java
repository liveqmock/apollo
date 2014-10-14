package cn.com.youtong.apollo.data;

import java.util.*;

import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.script.Table;
import cn.com.youtong.apollo.task.*;
/**
 * 任务数据类
 */

public class TaskData
{
    /**
     * 任务
     */
    private Task task;

    /**
     * 任务时间
     */
    private TaskTime taskTime;

    /**
     * 任务所有表的数据Map，key为table的id, value为TableData对象
     */
    private Map tables;

    /**
     * 单位id
     */
    private String unitID;

	/**
	 * 数据源，构造ScriptTable需要
	 */
	private DataSource dataSource;

	/**
	 * 任务数据表对应的脚本表Map
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
	 * 得到所有的数据表Map
	 * @return 所有的数据表Map，key为table的id, value为TableData对象
	 */
    public Map getTables()
    {
        return tables;
    }

	/**
	 * 得到任务数据表对应的脚本表Map
	 * @return 任务数据表对应的脚本表Map，key为table的id, value为cn.com.youtong.apollo.script.Table对象
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