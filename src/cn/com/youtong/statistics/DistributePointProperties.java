package cn.com.youtong.statistics;

import java.awt.Color;

public class DistributePointProperties {
	/** 散点颜色 */
	private Color disPointColor;
	/** 散点的园半径 */
	private int pointRadius;

	public Color getDisPointColor() {
		return disPointColor;
	}

	public int getPointRadius() {
		return pointRadius;
	}

	public void setDisPointColor( Color disPointColor ) {
		this.disPointColor = disPointColor;
	}

	public void setPointRadius( int pointRadius ) {
		this.pointRadius = pointRadius;
	}

}