package cn.com.youtong.apollo.task;

/**
 * ����ʱ��.
 * <ul>ʱ��
 * <li>����ʱ��
 * <li>�ϱ���ʼ
 * <li>�߱�ʱ��
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface TaskTime
{
	/**
	 * ����ʱ���ʶ
	 * @return		����ʱ���ʶ
	 */
	Integer getTaskTimeID();

	/**
	 * ��ʼʱ��
	 * @return
	 */Date getFromTime();

	/**
	 * ��ֹʱ��
	 * @return
	 */Date getEndTime();

	/**
	 * �ϱ�ʱ��.
	 * @return
	 */Date getSubmitFromTime();

	/**
	 * �ϱ�����ʱ��.
	 * @return
	 */Date getSubmitEndTime();

	/**
	 * �߱���ʼʱ��.
	 * @return
	 */Date getAttentionFromTime();

	/**
	 * �߱���ֹʱ��
	 * @return
	 */Date getAttentionEndTime();
}