package cn.com.youtong.apollo.analyse.form;

public class RowForm
{
	private CellForm[] cells;
	private boolean isTotalRow = false;

	public CellForm[] getCells()
	{
		return cells;
	}

	public void setCells( CellForm[] cells )
	{
		this.cells = cells;
    }

	public boolean isTotalRow()
	{
		return isTotalRow;
	}

	public void setIsTotalRow( boolean hasTotalRow )
	{
		this.isTotalRow = hasTotalRow;
	}
}