package cn.com.youtong.statistics;

import java.awt.Image;


/**
 * �򵥼���ָ����
 * ���㹫ʽ��
 * ����ָ�� �� ͬ�£��򼾣�ƽ���������£��򼾣�ƽ����
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
public class SimpleAverage
{
	private final double[] seasonIndex; // ����ָ��
	/**
	 * �ṩ����ֵ���Ͱ��¼��㻹�ǰ����ȼ��㡣
	 * @param yArray                      ����ֵ����
	 * @param period                      ���Ϊ4����ʾ�����Ƚ���ƽ����
	 *                                    ���Ϊ12����ʾ���½���ƽ����
	 */
    public SimpleAverage( final double[] yArray, final int period )
	{
		// step1: ����������ں�

		// Ϊ�˱��⿪����ôһ��������ռ䣬�ó�����ظ�������seasonIndex����.
		// Ҳ���Ǵ�����seasonIndex�ڲ�ͬ��Step�׶Σ����в�ͬ�ĺ��塣
		// Step1���ۼƸ������ڵ���ֵ��
		// Step2���������ڵ�ƽ��ֵ��
		// Step3������ָ�ꡣ

		seasonIndex = new double[ period ];
		for( int i=0; i<yArray.length;  )
		{
			for( int j=0; i<yArray.length && j<period; j++, i++ )
			{
				seasonIndex[ j ] = seasonIndex[ j ] + yArray[ i ];
			}
		}

		// Step2: ������м���ֵ�ĺͣ�����yArray����ֵ�ĺͣ�������ƽ��ֵ

		// yArray����Ԫ��ֵ
		double sumAll = 0;
		// ÿ������ƽ��ֵ
		for( int j=0; j<period; j++ )
		{
			sumAll += seasonIndex[ j ]; // �ۼ����м���ֵ

			seasonIndex[ j ] = seasonIndex[ j ] / ( ( (yArray.length -1 - j ) / period ) + 1 );
		}

		// Step3: �������ָ��

		// ���м���ƽ��ֵ
		double averageAll = sumAll / yArray.length;
		for( int j=0; j<period; j++ )
		{
			seasonIndex[ j ] = seasonIndex[ j ] / averageAll;
		}
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