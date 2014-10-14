package cn.com.youtong.statistics;

import java.awt.*;


/**
 * 一元线性回归。
 * 方程：Y = a + b * t
 *
 * <p>
 * 输入：x数组和y数组（值对）
 * 输出：常数项（又叫截距），和线性系数
 * 图形表示：坐标系中画出散点和回归直线
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
	// 设线性方程为 y = a + bx
	private final double b; // 回归系数
	private final double a; // 常数项，截距

	private final double[] xArray;
	private final double[] yArray;

	public LinearRegression( final double[] xArray, final double[] yArray )
	{
		// 检验参数
		if( xArray.length != yArray.length )
			throw new IllegalArgumentException( "Argument Arrays Should Be Same Size" );

		this.xArray = xArray;
		this.yArray = yArray;

		// 平均值
		double xMean = SAMath.mean( xArray );
		double yMean = SAMath.mean( yArray );

		// x方差和xy的协方差
		double lxx = SAMath.variance( xArray );
		double lxy = SAMath.covariance( xArray, yArray );

		b = lxy / lxx;
		a = yMean - b * xMean;
	}

	/**
	 * 作回归直线图
	 * @param width                 图片宽度
	 * @param height                图片高度
	 * @param title                 图片标题
	 * @param xLabel                x轴名称
	 * @param yLabel                y轴名称
	 * @return                      回归直线图
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
	 * 回归系数
	 * @return            回归系数
	 */
	public double regressionCoefficient()
	{
		return b;
	}

	/**
	 * 常数项
	 * @return                常数项
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
			CurveDrawer drawer = line.getCurveDrawer( 300, 600, "直线回归", "自变量", "因变量" );
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