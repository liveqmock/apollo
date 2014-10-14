package cn.com.youtong.apollo.script;

/**
 * Table data model
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 *
 * dbfield2valueMap和label2dbfieldMap的key和value都是大写的，
 * 在这里不用进行大小写转换
 */
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import cn.com.youtong.apollo.data.DataSource;
import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.TableData;
import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.task.TaskException;
import cn.com.youtong.apollo.task.TaskTime;

public class Table
	implements ScriptObject
{
	/**
	 * 表ID
	 */
	public String tableid;

	/**
	 * 表名称
	 */
	public String tablename;

	/**
	 * 行数，readonly
	 */
	public final int rowcount;

	/**
	 * 列数，readonly
	 */
	public final int colcount;

	protected int flag;
	protected String unit;
	protected Time time;

	/**
	 * 表的定义对象
	 */
	protected cn.com.youtong.apollo.task.Table tableDef;

	/**
	 * DBFieldName -- Value 的Map
	 */
	private Map dbfield2valueMap;

	/**
	 * Label -- DBFieldName 的Map
	 */
	private Map label2dbfieldMap;

	/**
	 * data source
	 */
	private DataSource dataSource;

	public Table(cn.com.youtong.apollo.task.Table tableDef, int flag,
				 Map dbfield2valueMap,
				 String unit, Time time, DataSource dataSource)
	{
		this.tableDef = tableDef;
		this.tableid = tableDef.id();
		this.tablename = tableDef.getName();
		this.flag = flag;
		this.dbfield2valueMap = dbfield2valueMap;
		this.label2dbfieldMap = tableDef.getLabel2dbfieldMap();
		this.unit = unit;
		this.time = time;
		this.dataSource = dataSource;

		//初始化行列数
		this.rowcount = getRowCount();
		this.colcount = getColCount();
	}

	/**
	 * 得到当前表的行数
	 * @return 当前表的行数
	 */
	private int getRowCount()
	{
		int result = 0;

		for (Iterator itr = tableDef.getAllCells(); itr.hasNext(); )
		{
			cn.com.youtong.apollo.task.Cell cellDef = (cn.com.youtong.apollo.
				task.Cell) itr.next();
			int row = getRowIndex(cellDef);

			if (result < row)
			{
				result = row;
			}
		}

		return result;
	}

	/**
	 * 得到当前表的列数
	 * @return 当前表的列数
	 */
	private int getColCount()
	{
		int result = 0;

		for (Iterator itr = tableDef.getAllCells(); itr.hasNext(); )
		{
			cn.com.youtong.apollo.task.Cell cellDef = (cn.com.youtong.apollo.
				task.Cell) itr.next();
			int row = getColIndex(cellDef);

			if (result < row)
			{
				result = row;
			}
		}

		return result;
	}

	/**
	 * 得到指定单元格所在的行数
	 * @param cellDef 单元格定义对象
	 * @return 指定单元格的所在的行数
	 */
	private int getRowIndex(cn.com.youtong.apollo.task.Cell cellDef)
	{
		String label = cellDef.getLabel();
		return getRowIndex(label);
	}

	/**
	 * 得到指定得到指定label的单元格所在的行数，行数由label中的数字表示；
	 * 如单元格的label为B1，则行数为1；label为B2，则行数为2，以此类推
	 * @param label label
	 * @return 指定label的单元格所在的行数
	 */
	private int getRowIndex(String label)
	{
		return Integer.parseInt(label.substring(1));
	}

	/**
	 * 得到指定得到指定label的单元格所在的行数，列数由label中的字母表示；
	 * 如单元格的label为A2，则列数为1；label为C2，则列数为3，以此类推
	 * @param label label
	 * @return 指定label的单元格所在的行数
	 */
	private int getColIndex(String label)
	{
		return label.charAt(0) - 'A' + 1;
	}

	/**
	 * 根据指定的行数和列数得到对应的label
	 * @param row 行数
	 * @param col 列数
	 * @return 指定的行数和列数所对应的label
	 */
	String getLabel(int row, int col)
	{
		char char1 = (char)('A' + col - 1);
		return "" + char1 + row;
	}

	/**
	 * 得到指定单元格所在的列数
	 * @param cellDef 单元格定义对象
	 * @return 指定单元格的所在的列数，以1开始计数
	 */
	private int getColIndex(cn.com.youtong.apollo.task.Cell cellDef)
	{
		String label = cellDef.getLabel();
		return getColIndex(label);
	}

	/**
	 * Table id
	 * @return Table id
	 */
	public String getId()
	{
		return tableid;
	}

	/**
	 * table name
	 * @return table name
	 */
	public String getName()
	{
		return tablename;
	}

	/**
	 * table flag
	 * @return table flag
	 */
	public int getFlag()
	{
		return flag;
	}

	/**
	 * Is float table
	 * @return Is float table
	 */
	public boolean isFloat()
	{
		return false;
	}

	/**
	 * Unit id
	 * @return Unit id
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * task time
	 * @return task time
	 */
	public Time getTime()
	{
		return time;
	}

	/**
	 * Get field by key
	 * Field operation helper function.
	 * @param field
	 * @return
	 */
	public Object s_getField(String key)
	{
		if (key.equals("cells"))
		{
			return new ValueCells(this);
		}
		else if (key.equals("_cells"))
		{
			return new ObjectCells(this);
		}
		else
		{ //动态字段
			//分别处理有前缀“_”和无前缀的情况
			if (key.startsWith("_"))
			{
				String label = getLabel(key.substring(1));
				//返回动态字段的Cell对象
				return getCell(label);
			}
			else
			{
				String label = getLabel(key);
				//返回动态字段的值
				return dbfield2valueMap.get(label2dbfieldMap.get(label));
			}
		}
	}

	/**
	 * Set field value
	 * Field operation help function.
	 * @param field
	 * @param value
	 */
	public void s_setField(String key, Object value)
	{
		//只更新动态字段
		String label = getLabel(key);

		if (label != null)
		{
			dbfield2valueMap.put(label2dbfieldMap.get(label), value);
		}
	}

	/**
	 * 得到指定label的Cell对象
	 * @param label label
	 * @return 指定label的Cell对象
	 */
	private Cell getCell(String label)
	{
		cn.com.youtong.apollo.task.Cell cellDef = tableDef.getCellByLabel(label);
		return new Cell(this, getRowIndex(cellDef), getColIndex(cellDef),
						cellDef.getDataType());
	}

	/**
	 * 根据可能的key值查找该key所对应的cell的label
	 * @param key 可能的key值，key值分四种情况
	 * 1. key = label
	 * 2. key = dbfieldName
	 * @return label，如果没找到，返回null
	 */
	private String getLabel(String key)
	{
		if (key != null)
		{
			key = key.toUpperCase();
		}

		if (label2dbfieldMap.keySet().contains(key))
		{
			//key为label
			return key;
		}
		else if (label2dbfieldMap.values().contains(key))
		{
			//key为dbfieldName
			return tableDef.getCellByDBFieldName(key).getLabel();
		}
		else
		{
			//无效的key
			return null;
		}
	}

	/**
	 * Enumerate fields.
	 * Field operation help function.
	 * @return
	 */
	public Iterator s_getFields()
	{
		return label2dbfieldMap.keySet().iterator();
	}

	public String toString()
	{
		return getId();
	}

	/**
	 * is field of object
	 * @param field field name
	 * @return true - if is field of object
	 */
	public boolean isField(String field)
	{
		boolean found = false;

		if (field.equals("cells") || field.equals("_cells"))
		{
			found = true;
		}
		else
		{
			//区分有前缀“_”和无前缀的情况
			if (field.startsWith("_"))
			{
				field = field.substring(1);
			}

			found = (getLabel(field) != null);
		}

		return found;
	}

	/**
	 * 取得指定月份m的本表数据，其中：m = 当前月份 + delta
	 * @param delta 计算指定月份的整数
	 * @return 指定月份m的本表数据
	 */
	public Table month(int delta) throws ScriptException
	{
		//计算指定的月份
		try
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time.getTaskTime().getFromTime());
			calendar.add(Calendar.MONTH, delta);

			TaskTime taskTime = dataSource.getTask().getTaskTime(calendar.getTime());

			TaskData taskData;
			if (taskTime == null)
			{
				taskData = dataSource.getEmptyTaskData( unit );
			}
			else
			{
				taskData = dataSource.get( unit, taskTime );
			}

			TableData tableData = (TableData) taskData.getTables().get( tableid );

			Table table = new Table( tableDef, 0, tableData.getDbfield2valueMap(),
									 unit, new Time( taskTime ),
									 dataSource );
			return table;
		}
		catch( TaskException ex )
		{
			throw new ScriptException("不能得到指定月份的表数据", ex);
		}
		catch( ModelException ex )
		{
			throw new ScriptException( "不能得到指定月份的表数据", ex );
		}
	}

	/**
	 * 取得指日期date的本表数据，其中：date = 当前日期 + delta
	 * @param delta 计算指定日期的整数
	 * @return 指定日期date的本表数据
	 */
	public Table date(int delta) throws ScriptException
	{
		//计算指定的月份
		try
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time.getTaskTime().getFromTime());
			calendar.add(Calendar.DATE, delta);

			TaskTime taskTime = dataSource.getTask().getTaskTime(calendar.getTime());

			TaskData taskData;
			if (taskTime == null)
			{
				taskData = dataSource.getEmptyTaskData( unit );
			}
			else
			{
				taskData = dataSource.get( unit, taskTime );
			}

			TableData tableData = (TableData) taskData.getTables().get( tableid );

			Table table = new Table( tableDef, 0, tableData.getDbfield2valueMap(),
									 unit, new Time( taskTime ),
									 dataSource );
			return table;
		}
		catch( TaskException ex )
		{
			throw new ScriptException("不能得到指定日期的表数据", ex);
		}
		catch( ModelException ex )
		{
			throw new ScriptException( "不能得到指定日期的表数据", ex );
		}
	}

	/**
	 * 根据Table对象得到TableData对象
	 * @param table Table对象
	 * @return TableData对象
	 */
	protected cn.com.youtong.apollo.data.TableData getTableData()
	{
		return new cn.com.youtong.apollo.data.TableData(dbfield2valueMap);
	}

	/**
	 * 该类定义表对象的cells二维数组，数组中的元素是表字段的值
	 */
	private class ValueCells
		implements ScriptArray
	{

		/**
		 * 记录定位cells数组元素的第一维index，如果为null，表示还没有指定第一维index
		 */
		private Integer firstDimensionIndex;

		/**
		 * 该cells相关的table对象
		 */
		private Table table;

		private ValueCells(Table table)
		{
			this.table = table;
		}

		/**
		 * 更新数组中指定index的元素
		 * @param index index
		 * @param value 新的元属
		 */
		public void put(int index, Object value)
		{
			try
			{
				if (index < 0)
				{
					throw new IllegalArgumentException();
				}

				if (firstDimensionIndex == null)
				{
					throw new UnsupportedOperationException("不允许更新cells的一维数组");
				}
				else
				{
					//更新cells[row][col]
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					table.s_setField(label, value);

					//重新初始化firstDimensionIndex
					firstDimensionIndex = null;
				}
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * 判断数组中指定index的元素是否存在
		 * @param index index
		 * @return 指定index的元素存在，返回true；否则，返回false
		 */
		public boolean has(int index)
		{
			try
			{
				if (firstDimensionIndex == null)
				{
					return true;
				}
				else
				{
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					return (label != null);
				}
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * 得到数组中指定index的元素值
		 * @param index index
		 * @return 数组中指定index的元素值
		 */
		public Object get(int index)
		{
			try
			{
				if (index < 0)
				{
					throw new IllegalArgumentException();
				}

				if (firstDimensionIndex == null)
				{
					//cells[row]的情况，返回本对象
					firstDimensionIndex = new Integer(index);
					return this;
				}
				else
				{
					//cells[row][col]的情况，返回真正的cell值
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					//重新初始化firstDimensionIndex
					firstDimensionIndex = null;

					return table.s_getField(label);
				}
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}
	}

	/**
	 * 该类定义表对象的_cells二维数组，数组中的元素是Cell对象
	 */
	private class ObjectCells
		implements ScriptArray
	{

		/**
		 * 记录定位_cells数组元素的第一维index，如果为null，表示还没有指定第一维index
		 */
		private Integer firstDimensionIndex;

		/**
		 * 该cells相关的table对象
		 */
		private Table table;

		private ObjectCells(Table table)
		{
			this.table = table;
		}

		/**
		 * 更新数组中指定index的元素
		 * @param index index
		 * @param value 新的元属
		 */
		public void put(int index, Object value)
		{
			try
			{
				throw new UnsupportedOperationException("不允许更新_cells");
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * 判断数组中指定index的元素是否存在
		 * @param index index
		 * @return 指定index的元素存在，返回true；否则，返回false
		 */
		public boolean has(int index)
		{
			try
			{
				if (firstDimensionIndex == null)
				{
					return true;
				}
				else
				{
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					return (label != null);
				}
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * 得到数组中指定index的元素值
		 * @param index index
		 * @return 数组中指定index的元素值
		 */
		public Object get(int index)
		{
			try
			{
				if (index < 0)
				{
					throw new IllegalArgumentException();
				}

				if (firstDimensionIndex == null)
				{
					//_cells[row]的情况，返回本对象
					firstDimensionIndex = new Integer(index);
					return this;
				}
				else
				{
					//_cells[row][col]的情况，返回真正的Cell对象
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					//重新初始化firstDimensionIndex
					firstDimensionIndex = null;

					return table.getCell(label);
				}
			}
			catch (RuntimeException ex)
			{
				//重新初始化firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}
	}
}