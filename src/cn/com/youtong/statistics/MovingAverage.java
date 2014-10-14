package cn.com.youtong.statistics;

import java.awt.Image;

/**
 * �ƶ�ƽ������
 * ���㷽����
 * <ol>
 * <li>����������»��߸��������ϣ���װ����Ϊ12�»���4����ƽ���ƶ�����
 *     �õ�����ֵ T ������Ҫ����ƽ�ƣ�</li>
 * <li>��ʵ��ֵ Y ��������ֵ T ���õ��Ѳ���������ֵ�ı䶯��ֵY / T ��</li>
 * <li>���ݼ�ƽ�������㼾��ָ��</li>
 * </ol>
 *
 * <p>
 * �ͼ�ƽ�����������ڣ��޳��˳������Ƶ�Ӱ�졣
 *
 * <p>
 * ���룺�����ĸ��£����߼��ȣ���ָ��ֵ
 * ��������£����ڣ�ָ��
 * ͼ�α�ʾ����񣬸�����������ָ��
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class MovingAverage
{
	private final double[] seasonIndex;
	/**
	 * �ṩ����ֵ���Ͱ��¼��㻹�ǰ����ȼ��㡣
	 * @param yArray                      ����ֵ����
	 * @param period                      ���Ϊ4����ʾ�����Ƚ���ƽ����
	 *                                    ���Ϊ12����ʾ���½���ƽ����
	 */
	public MovingAverage( final double[] yArray, final int period )
	{
		if( yArray.length <= period )
		{
			throw new IllegalArgumentException(
				"Array size should larger than " + period );
		}

		// ƽ���м�ֵ�õ�������ֵ
		double[] moving1 = new double[yArray.length - period + 1 ];

		// һ��ƽ��
		for( int i = 0; i < moving1.length; i++ )
		{
			// �ۼ�
			for( int j = i, max = i + period; j < max; j++ )
			{
				moving1[i] = moving1[i] + yArray[j];
			}

			// ƽ��
			moving1[i] = moving1[i] / period;
		}

		// ����ƽ��
		double[] moving2 = new double[ moving1.length - 1];
		for( int i=0; i<moving2.length; i++ )
		{
			moving2[i] = ( moving1[i] + moving1[i+1] ) / 2;
		}

		// ���㲻�����Ʊ䶯��ֵ
		for( int i=0; i<moving2.length; i++ )
		{
			moving2[i] = yArray[i + period/2] / moving2[i];
		}

		// ���ü�ƽ��������
		SimpleAverage simple = new SimpleAverage( moving2, period );
		double[] result = simple.seaseonIndex();

		// ת��һ������
		double[] shiftResult = new double[result.length];
		for( int i=0; i<shiftResult.length; i++ )
		{
			int position = i + period/2;
			if( position >= shiftResult.length )
				position -= shiftResult.length;

			shiftResult[i] = result[ position ];
		}

		seasonIndex = shiftResult;
	}


	/**
	 * ����ָ��
	 * @return         ����ָ��
	 */
	public double[] seaseonIndex()
	{
		return seasonIndex;
	}
}