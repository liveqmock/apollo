package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;

/**
 * <p>Title: ��������Barͼ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class BarChart extends Chart
{

	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("����", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setItemFont(font);

		CategoryPlot plot = chart.getCategoryPlot();
		//�Ժ��������
		CategoryAxis axis = plot.getDomainAxis();
		axis.setVerticalCategoryLabels(false);
		axis.setLabelFont(font);
		axis.setTickLabelFont(font);
		//�����������
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);

		return chart;
	}

	/**
	 * ���캯��
	 * @param chart �����JFreeChart����
	 */
	public BarChart(JFreeChart chart)
	{
		super(chart);
	}
}