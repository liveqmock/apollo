package cn.com.youtong.apollo.script;

/**
 * �ű��������
 */
public interface ScriptArray {

	/**
	 * �ж�������ָ��index��Ԫ���Ƿ����
	 * @param index index
	 * @return ָ��index��Ԫ�ش��ڣ�����true�����򣬷���false
	 */
	public boolean has(int index);

	/**
	 * �õ�������ָ��index��Ԫ��
	 * @param index index
	 * @return ������ָ��index��Ԫ��
	 */
	public Object get(int index);

	/**
	 * ����������ָ��index��Ԫ��
	 * @param index index
	 * @param value �µ�Ԫ��
	 */
	public void put(int index, Object value);
}