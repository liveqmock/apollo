package cn.com.youtong.statistics;

import java.awt.Font;
import java.awt.Color;

/**
 * 画轴线，可设置的属性
 *
 * <p>
 * <ol>
 *  <li>轴线颜色axisColor</li>
 *  <li>轴线标注颜色labelColor</li>
 *  <li>轴线标注距离画布边缘的宽度labelMarginSpace</li>
 *  <li>轴线标注与轴线间距labelAxisSpace</li>
 *  <li>期望在轴线上标注点数expectPointNum</li>
 *  <li>标注点的高度（或长度）axisPointHeight</li>
 *  <li>轴线描述字体</li>
 *  <li>轴线标注点描述字体</li>
 *	<li>箭头长度arrowLength</li>
 *	<li>箭头宽度arrowHalfWidth*2</li>
 * </ol>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class AxisProperties {
	// 箭头属性
	private int arrowLength; // 长度
	private int arrowHalfWidth; // 宽度

	// 画label的属性
	private int labelAxisSpace; // label和轴线间距
	private int labelMarginSpace; // label和边框间距
	private int expectPointNum; // 期望在坐标祝上标注几个点
	private int axisPointHeight; // 坐标轴上标注的点高度
	private java.awt.Font labelFont; // label的字体
	private java.awt.Font axisPointFont; //标注描述文字字体

	private Color axisColor; // 轴线的颜色
	private Color labelColor; // 标注文字颜色

	public int getArrowHalfWidth() {
		return arrowHalfWidth;
	}

	public void setArrowHalfWidth( int arrowHalfWidth ) {
		this.arrowHalfWidth = arrowHalfWidth;
	}

	public void setArrowLength( int arrowLength ) {
		this.arrowLength = arrowLength;
	}

	public int getArrowLength() {
		return arrowLength;
	}

	public java.awt.Font getAxisPointFont() {
	return axisPointFont;
	}

	public int getExpectPointNum() {
		return expectPointNum;
	}

	public int getAxisPointHeight() {
		return axisPointHeight;
	}

	public int getLabelAxisSpace() {
		return labelAxisSpace;
	}

	public java.awt.Font getLabelFont() {
	return labelFont;
	}

	public int getLabelMarginSpace() {
		return labelMarginSpace;
	}

	public void setLabelMarginSpace( int labelMarginSpace ) {
		this.labelMarginSpace = labelMarginSpace;
	}

	public void setLabelAxisSpace( int labelAxisSpace ) {
		this.labelAxisSpace = labelAxisSpace;
	}

	public void setLabelFont(java.awt.Font labelFont) {
	this.labelFont = labelFont;
	}

	public void setExpectPointNum( int expectPointNum ) {
		this.expectPointNum = expectPointNum;
	}

	public void setAxisPointHeight( int axisPointHeight ) {
		this.axisPointHeight = axisPointHeight;
	}

	public void setAxisPointFont( java.awt.Font axisPointFont ) {
		this.axisPointFont = axisPointFont;
	}

	public Color getLabelColor() {
		return labelColor;
	}

	public void setLabelColor( Color labelColor ) {
		this.labelColor = labelColor;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor( Color axisColor ) {
		this.axisColor = axisColor;
    }

}