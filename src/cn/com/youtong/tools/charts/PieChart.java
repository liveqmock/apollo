package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;

/**
 * <p>Title: ���ñ�ͼ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class PieChart extends Chart
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

//    plot.setItemLabelGenerator(new StandardPieItemLabelGenerator());

		chart.setBackgroundPaint(Color.white); //�������ޱ��� �� ��������ɫ
//    plot.setBackgroundPaint(null);

		Pie3DPlot plot = (Pie3DPlot) chart.getPlot();
		plot.setStartAngle(270); //���ÿ�ʼ�ĽǶ�
//    plot.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);//������ʾ�����е�label��ʾ����,����+�ٷֱ�
//    plot.setSectionLabelType(PiePlot.NO_LABELS);//����ʾ ��ʾ�����е� Label
		plot.setSectionLabelType(PiePlot.PERCENT_LABELS); //������ʾ�����е�label��ʾ����,�ٷֱ�
		font = new Font("����", Font.TRUETYPE_FONT, 20);
		plot.setSectionLabelFont(font); //������ʾ�����е�����

//    plot.setDirection(Rotation.CLOCKWISE);

		plot.setOutlinePaint(null); //����������߿� �� ��߿����ɫ
//    plot.setOutlinePaint(Color.YELLOW);

		plot.setForegroundAlpha(0.5f); //����͸����
//    plot.setNoDataMessage("No data to display");
		return chart;
	}

	/**
	 * ���캯��
	 * @param chart �����JFreeChart����
	 */
	public PieChart(JFreeChart chart)
	{
		super(chart);
	}
}