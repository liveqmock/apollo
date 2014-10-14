package cn.com.youtong.statistics;

import java.awt.Color;

public class CurveProperties {
	/** 曲线颜色 */
	private Color curveColor;

	/** 曲线段的X起始值 */
	private double startPointX;
	/** 曲线段的X终止值 */
	private double endPointX;
	/** 曲线的步进值 */
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