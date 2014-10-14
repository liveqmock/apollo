package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Demo {

	public static void main( String[] args )
	{
		lineRegression();
	}

	public static void lineRegression()
	{
		/**double[] xArray = {20, 30, 33, 40, 15,
						  13, 26, 38, 35, 43};
		double[] yArray = {7, 9, 8, 11, 5,
						  4, 8, 10, 9, 10};*/

		double[] xArray = {2.0, 68861.85, 53522.42, 56712.38, 53234.65, 68927.73, 62652.79, 63040.9, 75788.65};
		double[] yArray = {3.0, 26704.73, 39550.67, 20430.74, 30866.44, 37146.15, 22277.74, 27631.78, 24901.52};


		LinearRegression line = new LinearRegression( xArray, yArray );

		try
		{
			CurveDrawer drawer = line.getCurveDrawer( 400, 400, "直线回归", "自变量", "因变量" );
			Image image = drawer.draw();
			javax.imageio.ImageIO.write( (java.awt.image.BufferedImage) image, "png", new java.io.File("le.png") );

			drawer.getCurveProperties().setCurveColor( Color.RED );
			drawer.getCurveProperties().setStartPointX( -2.3 );
			drawer.getCurveProperties().setEndPointX( 5555 );

			image = drawer.draw();
			javax.imageio.ImageIO.write( (java.awt.image.BufferedImage) image, "png", new java.io.File("le2.png") );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void gongBoZi()
	{
		double[] yArray =
				  {4.94, 6.21, 7.18, 7.74, 8.38, 8.45, 8.73, 9.42, 10.24 };

		GongBoZi gbz = new GongBoZi( yArray );
		System.out.println("k=" + gbz.coefficient() );
		System.out.println("a=" + gbz.base() );
		System.out.println("b=" + gbz.middleBase() );

		try
		{
			CurveDrawer drawer = gbz.getCurveDrawer( 300, 300, "龚柏兹曲线", "X轴", "Y轴" );

			drawer.getCurveProperties().setStartPointX( -3 );
			drawer.getCurveProperties().setEndPointX( 55 );
			drawer.getCurveProperties().setDeltaX( 0.05 );

			Image image = drawer.draw();

			javax.imageio.ImageIO.write( (java.awt.image.BufferedImage)image, "jpg", new java.io.File("gongbozi.jpg") );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void reviseExponent()
	{
		double[] xArray = { 9.0, 15.0, 17.0, 20.0, 22.0,
						  23.5, 24.0, 26.8, 27.6, 27.0,
						  29.0, 28.4};
		ReviseExponent re = new ReviseExponent( xArray );

		try
		{
			CurveDrawer drawer = re.getCurveDrawer( 300, 300, "第一张图", "时间（年）", "收入（元）" );

			drawer.getAxisProperties().setAxisColor( Color.red );

			drawer.getCurveProperties().setCurveColor( Color.GREEN );
			drawer.getCurveProperties().setStartPointX( -2 );
			drawer.getCurveProperties().setEndPointX( 24 );

			drawer.getDistributePointProperties().setDisPointColor( Color.YELLOW );
			drawer.getDistributePointProperties().setPointRadius( 5 );

			Image image = drawer.draw();

			javax.imageio.ImageIO.write( ( BufferedImage ) image, "jpg",
										 new java.io.File( "re.jpg" ) );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}

	}
}
