package cn.com.youtong.apollo.data;

/**
 * <p>Title: ֵ��������������ʾ�����õ�Html����</p>
 * <p>Description: ��������Ҫ���ݣ�һ�ǿ��Եõ�������ƣ����ǵõ�table��Html����</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public interface TableViewer
{

	/**
	 * �õ�������
	 * @return ������
	 */
	public String getTaskName();


	/**
	 * �õ�����ID
	 * @return ����ID
	 */
	public String getTaskID();


	/**
	 * �õ�����
	 * @return ����
	 */
	public String getTableName();

	/**
	 * �õ���ID
	 * @return ��ID
	 */
	public String getTableID();


	/**
	 * �õ���ʾ���Html����
	 * @return Html����
	 */
	public String getHtmlString();
}