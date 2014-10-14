package cn.com.youtong.statistics;

import java.awt.Color;
import java.awt.Font;

public class TitleProperties {
	private Color titleColor;
	private Font titleFont;

	/** �ӱ������ֱ߿�Ĵ�ֱ�д��ߵ�Xֵ */
	private int centerX;
	/** ���������ϱ߿��Xֵ */
	private int topY;

	public int getCenterX() {
		return centerX;
	}

	public int getTopY() {
		return topY;
	}

	public Color getTitleColor() {
		return titleColor;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setCenterX( int centerX ) {
		this.centerX = centerX;
	}

	public void setTopY( int topY ) {
		this.topY = topY;
	}

	public void setTitleColor( Color titleColor ) {
		this.titleColor = titleColor;
	}

	public void setTitleFont( Font titleFont ) {
		this.titleFont = titleFont;
	}

}