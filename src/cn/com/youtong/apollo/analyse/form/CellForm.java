package cn.com.youtong.apollo.analyse.form;

public class CellForm {
	private int colspan;
	private int rowspan;
	private String label;
	private String expression;
	private String bgColor;
	private String contentStyle;
	private String halign;
	private String valign;
	private int fontID;
	private String formatStyle;

	public int getColspan() {
		return colspan;
	}

	public String getExpression() {
		return expression;
	}

	public String getLabel() {
		return label;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setColspan( int colspan ) {
		this.colspan = colspan;
	}

	public void setExpression( String expression ) {
		this.expression = expression;
	}

	public void setLabel( String label ) {
		this.label = label;
	}

	public void setRowspan( int rowspan ) {
		this.rowspan = rowspan;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor( String bgColor ) {
		this.bgColor = bgColor;
	}

	public String getContentStyle() {
		return contentStyle;
	}

	public void setContentStyle( String contentStyle ) {
		this.contentStyle = contentStyle;
	}

	public void setFontID( int fontID ) {
		this.fontID = fontID;
	}

	public int getFontID() {
		return fontID;
	}

	public String getFormatStyle() {
		return formatStyle;
	}

	public void setFormatStyle( String formatStyle ) {
		this.formatStyle = formatStyle;
	}

	public void setHalign( String halign ) {
		this.halign = halign;
	}

	public String getHalign() {
		return halign;
	}

	public String getValign() {
		return valign;
	}

	public void setValign( String valign ) {
		this.valign = valign;
	}
}