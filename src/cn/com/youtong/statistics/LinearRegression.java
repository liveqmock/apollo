package cn.com.youtong.statistics;

import java.awt.*;


/**
 * һԪ���Իع顣
 * ���̣�Y = a + b * t
 *
 * <p>
 * ���룺x�����y���飨ֵ�ԣ�
 * �����������ֽнؾࣩ��������ϵ��
 * ͼ�α�ʾ������ϵ�л���ɢ��ͻع�ֱ��
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class LinearRegression implements CurveEquation
{
	// �����Է���Ϊ y = a + bx
	private final double b; // �ع�ϵ��
	private final double a; // ������ؾ�

	private final double[] xArray;
	private final double[] yArray;

	public LinearRegression( final double[] xArray, final double[] yArray )
	{
		// �������
		if( xArray.length != yArray.length )
			throw new IllegalArgumentException( "Argument Arrays Should Be Same Size" );

		this.xArray = xArray;
		this.yArray = yArray;

		// ƽ��ֵ
		double xMean = SAMath.mean( xArray );
		double yMean = SAMath.mean( yArray );

		// x�����xy��Э����
		double lxx = SAMath.variance( xArray );
		double lxy = SAMath.covariance( xArray, yArray );

		b = lxy / lxx;
		a = yMean - b * xMean;
	}

	/**
	 * ���ع�ֱ��ͼ
	 * @param width                 ͼƬ���
	 * @param height                ͼƬ�߶�
	 * @param title                 ͼƬ����
	 * @param xLabel                x������
	 * @param yLabel                y������
	 * @return                      �ع�ֱ��ͼ
	 * @throws SAException
	 */
	public CurveDrawer getCurveDrawer( int width, int height, String title,
									 String xLabel, String yLabel )
		throws SAException
	{
		double[] maxAndMin = SAMath.maxAndMin( xArray );
		double maxX = maxAndMin[0];
		double minX = maxAndMin[1];

		double y2MinX = a + b * minX;
		double y2MaxX = a + b * maxX;

		double[] fxArray = maxAndMin;
		double[] fyArray = { y2MaxX, y2MinX };

		CurveDrawer helper
			= new CurveDrawer( width, height,
							  this,
							  minX, maxX,
							  y2MinX, y2MaxX,
							  xArray, yArray,
							  title,
							  xLabel, yLabel );

		return helper;
	}

	/**
	 * �ع�ϵ��
	 * @return            �ع�ϵ��
	 */
	public double regressionCoefficient()
	{
		return b;
	}

	/**
	 * ������
	 * @return                ������
	 */
	public double intercept()
	{
		return a;
	}

	public double value( double x )
	{
		return a + b * x;
	}

	public static void main( String[] args )
	{
		double[] xArray = {20, 30, 33, 40, 15,
						  13, 26, 38, 35, 43};
		double[] yArray = {7, 9, 8, 11, 5,
						  4, 8, 10, 9, 10};

		LinearRegression line = new LinearRegression( xArray, yArray );

		try
		{
			CurveDrawer drawer = line.getCurveDrawer( 300, 600, "ֱ�߻ع�", "�Ա���", "�����" );
			Image image = drawer.draw();
			javax.imageio.ImageIO.write( (java.awt.image.BufferedImage) image, "png", new java.io.File("le.png") );

			drawer.getCurveProperties().setCurveColor( Color.RED );
			drawer.getCurveProperties().setStartPointX( -2.3 );
			drawer.getCurveProperties().setEndPointX( 55 );

			image = drawer.draw();
			javax.imageio.ImageIO.write( (java.awt.image.BufferedImage) image, "png", new java.io.File("le2.png") );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
}