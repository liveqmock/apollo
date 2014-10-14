package cn.com.youtong.statistics;

import java.awt.Color;

public class CurveProperties {
	/** ������ɫ */
	private Color curveColor;

	/** ���߶ε�X��ʼֵ */
	private double startPointX;
	/** ���߶ε�X��ֵֹ */
	private double endPointX;
	/** ���ߵĲ���ֵ */
	private double deltaX;


	public Color getCurveColor() {
		return curveColor;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public double getEndPointX() {
		return endPointX;
	}

	public double getStartPointX() {
		return startPointX;
	}

	public void setCurveColor( Color curveColor ) {
		this.curveColor = curveColor;
	}

	public void setDeltaX( double deltaX ) {
		this.deltaX = deltaX;
	}

	public void setEndPointX( double endPointX ) {
		this.endPointX = endPointX;
	}

	public void setStartPointX( double startPointX ) {
		this.startPointX = startPointX;
	}

}