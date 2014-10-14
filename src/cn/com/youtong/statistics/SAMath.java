package cn.com.youtong.statistics;

import java.text.DecimalFormat;

/**
 * ���һЩ���������ʵ�֡�
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
final class SAMath
{
	/**
	 * �Ը�������������
	 * @param array           ����
	 * @return                ��
	 */
	public static final double sum( final double[] array )
	{
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			sum += array[i];
		}

		return sum;
	}

	/**
	 * ����������ѧƽ��ֵ
	 * @param array              ����
	 * @return                   ƽ��ֵ
	 */
	public static final double mean( final double[] array )
	{
		return sum( array ) / ( array.length );
	}

	/**
	 * ƽ���Average Deviation��
	 *
	 * <p>
	 *
	 * ���㲽�裺
	 * <ol>
	 * <li>�������ƽ��ֵ��</li>
	 * <li>�������ÿ���ƽ��ֵ�Ĳ�ֵ�ľ���ֵ�ͣ�</li>
	 * <li>����ֵ�ͳ��������С��</li>
	 * </ol>
	 *
	 * @param array      ����
	 * @return           ƽ����
	 */
	public static final double averageDeviation( final double[] array )
	{
		double meanValue = mean( array );

		// �������ÿ���ƽ��ֵ�Ĳ�ľ���ֵ
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			sum = sum + Math.abs( array[i] - meanValue );
		}

		return sum / ( array.length );
	}

	/**
	 * ���
	 *
	 * <p>
	 *
	 * ���㲽�裺
	 * <ol>
	 * <li>����ƽ��ֵ��</li>
	 * <li>����ÿ���ƽ��ֵ�Ĳ��ƽ���ͣ�</li>
	 * <li>ƽ���ͳ��������С��ȥ1��</li>
	 * </ol>
	 *
	 * @param array       ����
	 * @return            ����
	 */
	public static final double variance( final double[] array )
	{
		double mean = mean( array );

		// ������ÿ���ƽ��ֵ�Ĳ�ֵ��ƽ����Ȼ��ȫ�����
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			double diff = array[i] - mean;
			sum = sum + diff * diff;
		}

		return sum / ( array.length - 1 );
	}

	/**
	 * ����Э���
	 * @param xArray                ����x
	 * @param yArray                ����y
	 * @return                      ����Э����
	 */
	public static final double covariance( final double[] xArray,
										   final double[] yArray )
	{
		double xMean = mean( xArray );
		double yMean = mean( yArray );

		double[] xDev = new double[ xArray.length ];
		double[] yDev = new double[ yArray.length ];

		for( int i=0; i<xDev.length; i++ )
		{
			xDev[i] = xArray[i] - xMean;
			yDev[i] = yArray[i] - yMean;
		}

		double lxy = 0;

		for( int i=0; i<xDev.length; i++ )
		{
			lxy = lxy + ( xDev[i] * yDev[i] );
		}

		return lxy / ( xArray.length - 1 );
	}

	/**
	 * ��׼���Standard Deviation�����ֽ�S<font size="-1">n-1</font>
	 *
	 * <p>
	 *
	 * ���㲽�裺
	 * <ol>
	 * <li>������</li>
	 * <li>��ƽ����</li>
	 * </ol>
	 *
	 * @param array              ����
	 * @return                   ��׼����
	 */
	public static final double standardDeviation( final double[] array )
	{
		double variance = variance( array );

		return Math.sqrt( variance );
	}

	/**
	 * ��dValue����һλ��Ч����
	 * @param dValue        ԭ��ֵ
	 * @return    ����һλ��Ч���ֵ�ֵ
	 */
	public static double retainOneSignificantDigit( double dValue )
	{
		String s = String.valueOf( dValue );
		StringBuffer buff = new StringBuffer();

		for( int i=0, size=s.length(); i<size; i++ )
		{
			char current = s.charAt( i );

			if( current<='9' && current>='1' )
			{
				if( i == size-1 )
				{
					// �ַ����������
					buff.append( current );
				}
				else
				{
					// �Խ����ŵ��ַ���������������
					char next = s.charAt( i + 1 );
					if( next =='.' )
					{
						++i;
						next = s.charAt( i + 1 );
					}

					if( next < '5' )
					{
						buff.append( current );
					} else
					{
						current = ( char ) ( current + 1 );
						buff.append( current );
					}

					// �鿴�Ƿ�����С������
					int indexOfDot = s.indexOf( '.' );
					if( indexOfDot > i )
					{
						// ��ʾ��û�е�С������
						for( int j = i + 1; j < indexOfDot; j++ )
						{
							buff.append( '0' );
						}
					}

					// �鿴�Ƿ��п�ѧ������
					int indexOfE = s.indexOf( 'E' );
					if( indexOfE == -1 )
					{
						// �鿴�ַ�e
						indexOfE = s.indexOf( 'e' );
					}

					if( indexOfE > 0 )
					{
						buff.append( s.substring( indexOfE ) );
					}
				}

				break;
			}
			else
			{
				buff.append( current );
			}
		}

		String result = buff.toString();

		double dResult = Double.parseDouble( result );

		return dResult;
	}

	/**
	 * �������������ֵ����Сֵ
	 * @param array       ����
	 * @return            ���ֵ=double[0]����Сֵ=double[1]
	 */
	public static double[] maxAndMin( double[] array )
	{
		// ���������ֵ����Сֵ
		double min = array[0];
		double max = array[0];

		for( int i=1; i<array.length; i++ )
		{
			if ( array[i] > max )
			{
				max = array[i];
			}
			else if( array[i] < min )
			{
				min = array[i];
			}
		}

		return new double[]{ max, min };
	}

	/**
	 * ��������������ʵ��ĸ�ʽ������ౣ��4λ��Ч����
	 * @param input          �������ȷֲ���double����
	 * @return               ��ʽ�����ַ�������
	 */
	public static String[] format( double[] input )
	{
		String[] output = new String[ input.length ];

		String pattern = "0.####";
		if( output.length >= 1 )
		{
			double step = input[1] - input[0];
			step = retainOneSignificantDigit( step );

			// ����п�ѧ��������ʹ�ÿ�ѧ������
			String sStep = Double.toString( step );
			if( sStep.indexOf( 'E' ) > 0
				|| sStep.indexOf( 'e' ) > 0 )
			{
				pattern = pattern + "E0";
			}
		}

		DecimalFormat dFormat = new DecimalFormat( pattern );
		for( int i=0; i<input.length; i++ )
		{
			output[i] = dFormat.format( input[i] );
		}

		return output;
	}
	/**
	 * �������� Bias Corrected Standard Deviation����׼���
	 * �ֽ�S<font size="-1">n-1</font>
	 *
	 * <p>
	 *
	 * �ͱ�׼����������ǣ���׼����õ����ǳ���n�Ĳ�ƽ���ͣ�Ȼ��ƽ������
	 * ������׼����ǳ���n-1��
	 *
	 * @param array                  ����
	 * @return                       �������ģ���׼����
	 */
	/**public static final double bcStandardDeviation( final double[] array )
	{
		double bcvariance = variance( array ) * ( array.length ) / ( array.length - 1 );

		return Math.sqrt( bcvariance );
	}*/
}