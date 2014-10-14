package cn.com.youtong.apollo.script;

/**
 * Table data model
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 *
 * dbfield2valueMap��label2dbfieldMap��key��value���Ǵ�д�ģ�
 * �����ﲻ�ý��д�Сдת��
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
	 * ��ID
	 */
	public String tableid;

	/**
	 * ������
	 */
	public String tablename;

	/**
	 * ������readonly
	 */
	public final int rowcount;

	/**
	 * ������readonly
	 */
	public final int colcount;

	protected int flag;
	protected String unit;
	protected Time time;

	/**
	 * ��Ķ������
	 */
	protected cn.com.youtong.apollo.task.Table tableDef;

	/**
	 * DBFieldName -- Value ��Map
	 */
	private Map dbfield2valueMap;

	/**
	 * Label -- DBFieldName ��Map
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

		//��ʼ��������
		this.rowcount = getRowCount();
		this.colcount = getColCount();
	}

	/**
	 * �õ���ǰ�������
	 * @return ��ǰ�������
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
	 * �õ���ǰ�������
	 * @return ��ǰ�������
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
	 * �õ�ָ����Ԫ�����ڵ�����
	 * @param cellDef ��Ԫ�������
	 * @return ָ����Ԫ������ڵ�����
	 */
	private int getRowIndex(cn.com.youtong.apollo.task.Cell cellDef)
	{
		String label = cellDef.getLabel();
		return getRowIndex(label);
	}

	/**
	 * �õ�ָ���õ�ָ��label�ĵ�Ԫ�����ڵ�������������label�е����ֱ�ʾ��
	 * �絥Ԫ���labelΪB1��������Ϊ1��labelΪB2��������Ϊ2���Դ�����
	 * @param label label
	 * @return ָ��label�ĵ�Ԫ�����ڵ�����
	 */
	private int getRowIndex(String label)
	{
		return Integer.parseInt(label.substring(1));
	}

	/**
	 * �õ�ָ���õ�ָ��label�ĵ�Ԫ�����ڵ�������������label�е���ĸ��ʾ��
	 * �絥Ԫ���labelΪA2��������Ϊ1��labelΪC2��������Ϊ3���Դ�����
	 * @param label label
	 * @return ָ��label�ĵ�Ԫ�����ڵ�����
	 */
	private int getColIndex(String label)
	{
		return label.charAt(0) - 'A' + 1;
	}

	/**
	 * ����ָ���������������õ���Ӧ��label
	 * @param row ����
	 * @param col ����
	 * @return ָ������������������Ӧ��label
	 */
	String getLabel(int row, int col)
	{
		char char1 = (char)('A' + col - 1);
		return "" + char1 + row;
	}

	/**
	 * �õ�ָ����Ԫ�����ڵ�����
	 * @param cellDef ��Ԫ�������
	 * @return ָ����Ԫ������ڵ���������1��ʼ����
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
		{ //��̬�ֶ�
			//�ֱ�����ǰ׺��_������ǰ׺�����
			if (key.startsWith("_"))
			{
				String label = getLabel(key.substring(1));
				//���ض�̬�ֶε�Cell����
				return getCell(label);
			}
			else
			{
				String label = getLabel(key);
				//���ض�̬�ֶε�ֵ
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
		//ֻ���¶�̬�ֶ�
		String label = getLabel(key);

		if (label != null)
		{
			dbfield2valueMap.put(label2dbfieldMap.get(label), value);
		}
	}

	/**
	 * �õ�ָ��label��Cell����
	 * @param label label
	 * @return ָ��label��Cell����
	 */
	private Cell getCell(String label)
	{
		cn.com.youtong.apollo.task.Cell cellDef = tableDef.getCellByLabel(label);
		return new Cell(this, getRowIndex(cellDef), getColIndex(cellDef),
						cellDef.getDataType());
	}

	/**
	 * ���ݿ��ܵ�keyֵ���Ҹ�key����Ӧ��cell��label
	 * @param key ���ܵ�keyֵ��keyֵ���������
	 * 1. key = label
	 * 2. key = dbfieldName
	 * @return label�����û�ҵ�������null
	 */
	private String getLabel(String key)
	{
		if (key != null)
		{
			key = key.toUpperCase();
		}

		if (label2dbfieldMap.keySet().contains(key))
		{
			//keyΪlabel
			return key;
		}
		else if (label2dbfieldMap.values().contains(key))
		{
			//keyΪdbfieldName
			return tableDef.getCellByDBFieldName(key).getLabel();
		}
		else
		{
			//��Ч��key
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
			//������ǰ׺��_������ǰ׺�����
			if (field.startsWith("_"))
			{
				field = field.substring(1);
			}

			found = (getLabel(field) != null);
		}

		return found;
	}

	/**
	 * ȡ��ָ���·�m�ı������ݣ����У�m = ��ǰ�·� + delta
	 * @param delta ����ָ���·ݵ�����
	 * @return ָ���·�m�ı�������
	 */
	public Table month(int delta) throws ScriptException
	{
		//����ָ�����·�
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
			throw new ScriptException("���ܵõ�ָ���·ݵı�����", ex);
		}
		catch( ModelException ex )
		{
			throw new ScriptException( "���ܵõ�ָ���·ݵı�����", ex );
		}
	}

	/**
	 * ȡ��ָ����date�ı������ݣ����У�date = ��ǰ���� + delta
	 * @param delta ����ָ�����ڵ�����
	 * @return ָ������date�ı�������
	 */
	public Table date(int delta) throws ScriptException
	{
		//����ָ�����·�
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
			throw new ScriptException("���ܵõ�ָ�����ڵı�����", ex);
		}
		catch( ModelException ex )
		{
			throw new ScriptException( "���ܵõ�ָ�����ڵı�����", ex );
		}
	}

	/**
	 * ����Table����õ�TableData����
	 * @param table Table����
	 * @return TableData����
	 */
	protected cn.com.youtong.apollo.data.TableData getTableData()
	{
		return new cn.com.youtong.apollo.data.TableData(dbfield2valueMap);
	}

	/**
	 * ���ඨ�������cells��ά���飬�����е�Ԫ���Ǳ��ֶε�ֵ
	 */
	private class ValueCells
		implements ScriptArray
	{

		/**
		 * ��¼��λcells����Ԫ�صĵ�һάindex�����Ϊnull����ʾ��û��ָ����һάindex
		 */
		private Integer firstDimensionIndex;

		/**
		 * ��cells��ص�table����
		 */
		private Table table;

		private ValueCells(Table table)
		{
			this.table = table;
		}

		/**
		 * ����������ָ��index��Ԫ��
		 * @param index index
		 * @param value �µ�Ԫ��
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
					throw new UnsupportedOperationException("���������cells��һά����");
				}
				else
				{
					//����cells[row][col]
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					table.s_setField(label, value);

					//���³�ʼ��firstDimensionIndex
					firstDimensionIndex = null;
				}
			}
			catch (RuntimeException ex)
			{
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * �ж�������ָ��index��Ԫ���Ƿ����
		 * @param index index
		 * @return ָ��index��Ԫ�ش��ڣ�����true�����򣬷���false
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
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * �õ�������ָ��index��Ԫ��ֵ
		 * @param index index
		 * @return ������ָ��index��Ԫ��ֵ
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
					//cells[row]����������ر�����
					firstDimensionIndex = new Integer(index);
					return this;
				}
				else
				{
					//cells[row][col]�����������������cellֵ
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					//���³�ʼ��firstDimensionIndex
					firstDimensionIndex = null;

					return table.s_getField(label);
				}
			}
			catch (RuntimeException ex)
			{
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}
	}

	/**
	 * ���ඨ�������_cells��ά���飬�����е�Ԫ����Cell����
	 */
	private class ObjectCells
		implements ScriptArray
	{

		/**
		 * ��¼��λ_cells����Ԫ�صĵ�һάindex�����Ϊnull����ʾ��û��ָ����һάindex
		 */
		private Integer firstDimensionIndex;

		/**
		 * ��cells��ص�table����
		 */
		private Table table;

		private ObjectCells(Table table)
		{
			this.table = table;
		}

		/**
		 * ����������ָ��index��Ԫ��
		 * @param index index
		 * @param value �µ�Ԫ��
		 */
		public void put(int index, Object value)
		{
			try
			{
				throw new UnsupportedOperationException("���������_cells");
			}
			catch (RuntimeException ex)
			{
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * �ж�������ָ��index��Ԫ���Ƿ����
		 * @param index index
		 * @return ָ��index��Ԫ�ش��ڣ�����true�����򣬷���false
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
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}

		/**
		 * �õ�������ָ��index��Ԫ��ֵ
		 * @param index index
		 * @return ������ָ��index��Ԫ��ֵ
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
					//_cells[row]����������ر�����
					firstDimensionIndex = new Integer(index);
					return this;
				}
				else
				{
					//_cells[row][col]�����������������Cell����
					String label = table.getLabel(firstDimensionIndex.intValue(),
												  index);
					//���³�ʼ��firstDimensionIndex
					firstDimensionIndex = null;

					return table.getCell(label);
				}
			}
			catch (RuntimeException ex)
			{
				//���³�ʼ��firstDimensionIndex
				firstDimensionIndex = null;
				throw ex;
			}
		}
	}
}