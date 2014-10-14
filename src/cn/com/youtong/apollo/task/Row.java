package cn.com.youtong.apollo.task;

/**
 * �ж����ж����и������������Ҫ����,�������ж���ȴ����Ҫ���ж���.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Row
{
	/**
	 * �����б�ʶ
	 */
	static final int FLAG_FLOAT_ROW = 0x00000001;

	/**
	 * ȡ���б�ʶ
	 * @return �б�ʶ
	 */
	String id();

	/**
	 * �еĵ�Ԫ��
	 * @return each element is a Cell object
	 */Iterator getCells();

	/**
	 * Get row attribute
	 * @param flag
	 * @return
	 */boolean getFlag(int flag);
}