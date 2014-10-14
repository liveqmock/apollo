package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;

/**
 * <p>Title: ������ͼ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class LineChart extends Chart
{

	/**
	 * ���� ͼ�����ʾ���
	 * @param chart  ����ͼ��Ķ���
	 * @return       ������Ķ����޸ĺ󷵻���
	 */
	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("����", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setItemFont(font);

		XYPlot plot = chart.getXYPlot();

		//�����������
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);
		vaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//�Ժ��������
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font);
		domainAxis.setTickLabelFont(font);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//�����ͽ��в���
		StandardXYItemRenderer renderer = (StandardXYItemRenderer) plot.getRenderer();
		renderer.setPlotShapes(true); //���ߵ�ת�۴��ӵ�
		renderer.setShapesFilled(true); //���õ�
//    renderer.setItemLabelsVisible(true);

//    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	/**
	 * ���캯��
	 * @param chart �����JFreeChart����
	 */
	public LineChart(JFreeChart chart)
	{
		super(chart);
	}
}