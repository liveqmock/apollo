package cn.com.youtong.apollo.analyse.form;

import cn.com.youtong.apollo.analyse.xml.Head;
import cn.com.youtong.apollo.analyse.xml.Cell;
import cn.com.youtong.apollo.analyse.xml.Row;
import cn.com.youtong.apollo.common.Convertor;

public class HeadForm {
	private int colnum;
	private int rownum;

	private RowForm[] rows;

	private String defaultColor;
	private int defaultFontID;

	public HeadForm() {
	}

	public HeadForm( Head header ) {
		colnum = header.getColnum();
		rownum = header.getRownum();

		this.defaultColor = header.getDefaultColor();
		this.defaultFontID = header.getDefaultFontID();

		Row[] xmlRows = header.getRow();

		rows = new RowForm[xmlRows.length];

		for ( int i = 0; i < xmlRows.length; i++ ) {
			Row xmlRow = xmlRows[i];
			Cell[] xmlCells = xmlRow.getCell();

			rows[i] = new RowForm();

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
}