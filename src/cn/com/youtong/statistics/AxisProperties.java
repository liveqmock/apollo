package cn.com.youtong.statistics;

import java.awt.Font;
import java.awt.Color;

/**
 * �����ߣ������õ�����
 *
 * <p>
 * <ol>
 *  <li>������ɫaxisColor</li>
 *  <li>���߱�ע��ɫlabelColor</li>
 *  <li>���߱�ע���뻭����Ե�Ŀ��labelMarginSpace</li>
 *  <li>���߱�ע�����߼��labelAxisSpace</li>
 *  <li>�����������ϱ�ע����expectPointNum</li>
 *  <li>��ע��ĸ߶ȣ��򳤶ȣ�axisPointHeight</li>
 *  <li>������������</li>
 *  <li>���߱�ע����������</li>
 *	<li>��ͷ����arrowLength</li>
 *	<li>��ͷ���arrowHalfWidth*2</li>
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
	// ��ͷ����
	private int arrowLength; // ����
	private int arrowHalfWidth; // ���

	// ��label������
	private int labelAxisSpace; // label�����߼��
	private int labelMarginSpace; // label�ͱ߿���
	private int expectPointNum; // ����������ף�ϱ�ע������
	private int axisPointHeight; // �������ϱ�ע�ĵ�߶�
	private java.awt.Font labelFont; // label������
	private java.awt.Font axisPointFont; //��ע������������

	private Color axisColor; // ���ߵ���ɫ
	private Color labelColor; // ��ע������ɫ

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