/*
 * Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;

/**
 * @author wjb
 */
public class DBUnitMetaTable extends DBTable
	implements UnitMetaTable
{
	private Cell _unitCodeCell;
	private Cell _reportTypeCell;
	private Cell _parentUnitCodeCell;
	private Cell _hqCodeCell;
	private Cell _unitNameCell;
	private Cell _displayCell;

	public DBUnitMetaTable(TableForm form,
						   CellForm unitCodeCellForm,
						   CellForm reportTypeCellForm,
						   CellForm parentUnitCodeCellForm,
						   CellForm hqCodeCellForm,
						   CellForm unitNameCellForm,CellForm displayCellForm)
	{
		super( form );

		if(unitCodeCellForm != null)
			this._unitCodeCell = new DBCell( unitCodeCellForm );

		if(reportTypeCellForm != null)
			this._reportTypeCell = new DBCell( reportTypeCellForm );

		if(parentUnitCodeCellForm != null)
			this._parentUnitCodeCell = new DBCell( parentUnitCodeCellForm );

		if(hqCodeCellForm != null)
			this._hqCodeCell = new DBCell( hqCodeCellForm );

		if(unitNameCellForm != null)
			this._unitNameCell = new DBCell( unitNameCellForm );
		
		if(displayCellForm!=null)
			this._displayCell = new DBCell(displayCellForm);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.com.youtong.apollo.task.UnitMetaTable#getUnitCodeCell()
	 */
	public Cell getUnitCodeCell()
	{
		return _unitCodeCell;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.com.youtong.apollo.task.UnitMetaTable#getReprotTypeCell()
	 */
	public Cell getReportTypeCell()
	{

		return _reportTypeCell;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.com.youtong.apollo.task.UnitMetaTable#getParentUnitCodeCell()
	 */
	public Cell getParentUnitCodeCell()
	{

		return _parentUnitCodeCell;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.com.youtong.apollo.task.UnitMetaTable#getUnitNameCell()
	 */
	public Cell getUnitNameCell()
	{
		return _unitNameCell;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.com.youtong.apollo.task.UnitMetaTable#getHQCodeCell()
	 */
	public Cell getHQCodeCell()
	{
		return _hqCodeCell;
	}
	
	public Cell getDisplayCell()
	{
		return _displayCell;
	}
}
