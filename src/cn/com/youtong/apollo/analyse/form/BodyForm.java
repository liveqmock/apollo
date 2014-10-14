package cn.com.youtong.apollo.analyse.form;

import cn.com.youtong.apollo.analyse.xml.*;
import cn.com.youtong.apollo.common.Convertor;

public class BodyForm {
	private int colnum;
	private int rownum;

	private RowForm[] rows;
	private String defaultColor;
	private int defaultFontID;
	private boolean hasTotalRow;

	public BodyForm() {
	}

	public BodyForm( Body body ) {
		colnum = body.getColnum();
		rownum = body.getRownum();

		this.defaultColor =  body.getDefaultColor();
		this.defaultFontID = body.getDefaultFontID();
		this.hasTotalRow = body.getHasTotalRow();

		Row[] xmlRows = body.getRow();

		rows = new RowForm[xmlRows.length];

		for ( int i = 0; i < xmlRows.length; i++ ) {
			Row xmlRow = xmlRows[i];
			Cell[] xmlCells = xmlRow.getCell();

			rows[i] = new RowForm();
			rows[i].setIsTotalRow( xmlRow.getIsTotalRow() );

			CellForm[] cells = new CellForm[xmlCells.length];
			rows[i].setCells( cells );

			for ( int j = 0; j < xmlCells.length; j++ ) {
				Cell xmlCell = xmlCells[j];
				cells[j] = new CellForm();

				cells[j].setColspan( xmlCell.getColspan() );
				cells[j].setRowspan( xmlCell.getRowspan() );
				cells[j].setExpression( xmlCell.getExpression() );
				cells[j].setLabel( xmlCell.getLabel() );

				cells[j].setBgColor( xmlCell.getBgcolor() );
				cells[j].setContentStyle( xmlCell.getContentStyle() );
				cells[j].setHalign( xmlCell.getHalign() );
				cells[j].setValign( xmlCell.getValign() );
				cells[j].setFontID( xmlCell.getFontID() );
				cells[j].setFormatStyle( xmlCell.getFormatStyle() );
			}
		}
	}

	public int getColnum() {
		return colnum;
	}

	public int getRownum() {
		return rownum;
	}

	public RowForm[] getRows() {
		return rows;
	}

	public void setColnum( int colnum ) {
		this.colnum = colnum;
	}

	public void setRownum( int rownum ) {
		this.rownum = rownum;
	}

	public void setRows( RowForm[] rows ) {
		this.rows = rows;
	}

	public String getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor( String defaultColor ) {
		this.defaultColor = defaultColor;
	}

	public void setDefaultFontID( int defaultFontID ) {
		this.defaultFontID = defaultFontID;
	}

	public int getDefaultFontID() {
		return defaultFontID;
	}

	public boolean hasTotalRow() {
		return hasTotalRow;
	}

	public void setHasTotalRow( boolean hasTotalRow ) {
		this.hasTotalRow = hasTotalRow;
    }
}