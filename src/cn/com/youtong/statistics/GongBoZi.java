package cn.com.youtong.statistics;

import java.awt.Image;


/**
 * ����������
 * ���̣�Y<font size="-1">t</font> = k * ( Math.pow( a, Math.pow( b, t ) );
 * ( 0<a<1, 0<b<1 )
 *
 * <p>
 * ���룺�����ĸ��£����߼��ȣ���ָ��ֵ
 * �������Ӧ��������Ϸ���
 * ͼ�α�ʾ��ɢ�������ͼ
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class GongBoZi implements CurveEquation
{
	// �跽�̣�y=k*(Math.pow(a, Math.pow(b,t));
	private final double k;
	private final double a;
	private final double b;

	private final double[] yArray;
    public GongBoZi( final double[] yArray )
	{
		this.yArray = yArray;

		double[] lnyArray = new double[ yArray.length ];
		for( int i=0; i<lnyArray.length; i++ )
		{
			lnyArray[i] = Math.log( yArray[i] );
		}

		ReviseExponent re = new ReviseExponent( lnyArray );
		double lnk = re.constantTerm();
		double lna = re.coefficient();
		b = re.base();

		k = Math.exp( lnk );
		a = Math.exp( lna );
    }

	/**
	 * ������������ͼ
	 * @param width                 ͼƬ���
	 * @param height                ͼƬ�߶�
	 * @param title                 ͼƬ����
	 * @param xLabel                x������
	 * @param yLabel                y������
	 * @return                      ����������ͼ
	 * @throws SAException
	 */
	public CurveDrawer getCurveDrawer( int width, int height, String title,
									String xLabel, String yLabel )
		throws SAException
	{
		int minX = 0;
		int maxX = yArray.length - 1;

		double bottonSideY = value( 0 );
		double topSideY = value( yArray.length + 1 );

		double minY = Math.min( bottonSideY, topSideY );
		double maxY = Math.max( bottonSideY, topSideY );

		double[] xArray = new double[ yArray.length ];
		for( int i=0; i<xArray.length; i++ )
		{
			xArray[i] = i;
		}

		CurveDrawer helper = new CurveDrawer( width, height,
											this,
											minX, maxX,
											minY, maxY,
											xArray, yArray,
											title,
											xLabel, yLabel);
		return helper;
	}

	public double coefficient()
	{
		return k;
	}

	public double base()
	{
		return a;
	}

	public double middleBase()
	{
		return b;
	}

	public double value( double x )
	{
		return k * ( Math.pow( a, Math.pow( b, x ) ) );
	}

	public static void main( String[] args )
	{
		double[] yArray =
				  {4.94, 6.21, 7.18, 7.74, 8.38, 8.45, 8.73, 9.42, 10.24 };

		GongBoZi gbz = new GongBoZi( yArray );
		System.out.println("k=" + gbz.coefficient() );
		System.out.println("a=" + gbz.base() );
		System.out.println("b=" + gbz.middleBase() );

		try
		{
			CurveDrawer drawer = gbz.getCurveDrawer( 300, 300, "����������", "X��", "Y��" );

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
}