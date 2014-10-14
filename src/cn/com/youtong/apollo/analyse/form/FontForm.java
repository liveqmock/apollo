package cn.com.youtong.apollo.analyse.form;

import cn.com.youtong.apollo.analyse.xml.Font;

public class FontForm {

	private Font font;
	public FontForm( Font font ) {
		this.font = font;
	}

	public String getColor() {
		return font.getColor();
	}

	public String getDescription() {
		return font.getDescription();
	}

	public int getID() {
		return font.getID();
	}

	public boolean isBold() {
		return font.getIsBold();
	}

	public boolean isItalic() {
		return font.getIsItalic();
	}

	public boolean isPdfEmbedded() {
		return font.getIsPdfEmbedded();
	}

	public boolean isStrikeThrough() {
		return font.getIsStrikeThrough();
	}

	public boolean isIsUnderline() {
		return font.getIsUnderline();
	}

	public String getName() {
		return font.getName();
	}

	public String getPdfEncoding() {
		return font.getPdfEncoding();
	}

	public String getPdfFontName() {
		return font.getPdfFontName();
	}

	public int getSize() {
		return Math.abs( font.getSize() );
    }
}