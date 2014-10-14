package cn.com.youtong.statistics;

import java.awt.Image;


/**
 * 简单季节指数。
 * 计算公式：
 * 季节指数 ＝ 同月（或季）平均数÷总月（或季）平均数
 *
 * <p>
 * 输入：连续的各月（或者季度）的指标值
 * 输出：各月（季节）指数
 * 图形表示：表格，给出各个季节指数
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
	private final double[] seasonIndex; // 季节指数
	/**
	 * 提供季节值，和按月计算还是按季度计算。
	 * @param yArray                      季节值数组
	 * @param period                      如果为4，表示按季度进行平均；
	 *                                    如果为12，表示按月进行平均。
	 */
    public SimpleAverage( final double[] yArray, final int period )
	{
		// step1: 求出各个季节和

		// 为了避免开启那么一点点的数组空间，该程序段重复利用了seasonIndex数组.
		// 也就是带来了seasonIndex在不同的Step阶段，具有不同的含义。
		// Step1，累计各个季节的总值；
		// Step2，各个季节的平均值；
		// Step3，季节指标。

		seasonIndex = new double[ period ];
		for( int i=0; i<yArray.length;  )
		{
			for( int j=0; i<yArray.length && j<period; j++, i++ )
			{
				seasonIndex[ j ] = seasonIndex[ j ] + yArray[ i ];
			}
		}

		// Step2: 求出所有季节值的和（即：yArray数组值的和），季节平均值

		// yArray所有元素值
		double sumAll = 0;
		// 每个季节平均值
		for( int j=0; j<period; j++ )
		{
			sumAll += seasonIndex[ j ]; // 累计所有季节值

			seasonIndex[ j ] = seasonIndex[ j ] / ( ( (yArray.length -1 - j ) / period ) + 1 );
		}

		// Step3: 求出季节指数

		// 所有季节平均值
		double averageAll = sumAll / yArray.length;
		for( int j=0; j<period; j++ )
		{
			seasonIndex[ j ] = seasonIndex[ j ] / averageAll;
		}
    }


	/**
	 * 季节指数
	 * @return         季节指数
	 */
	public double[] seaseonIndex()
	{
		return seasonIndex;
	}
}