package cn.com.youtong.apollo.data;

import java.util.Collection;
import java.util.Iterator;

import cn.com.youtong.apollo.script.Time;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * ����Դ�ӿ�
 */
public interface DataSource
{
	public Task getTask();
	/**
	 * �õ�ָ������������.
	 *
	 * ����õ�λ������,����null.
	 * @param unitID ��λID
	 * @param taskTime ����ʱ��
	 * @return �������ݶ���
	 * @throws ModelException
	 */
	public TaskData get(String unitID, TaskTime taskTime) throws ModelException;

	/**
	 * �õ�ָ�����������ݡ����ܱ�֤���ص�Iterator˳���unitIDs��Iterator˳��һ�¡�
	 *
	 * �����λ��������ĳ������ĳЩ��λ������,���صĽ���������û����Ӧ��TaskData.
	 * @param unitIDs ��λID����
	 * @param taskTime ����ʱ��
	 * @return �������ݶ���TaskData���ϵ�Iterator
	 * @throws ModelException
	 */
	public Iterator get(Collection unitIDs, TaskTime taskTime) throws
		ModelException;

	/**
	 * �õ�ָ�����������ݡ�
	 * ���ܱ�֤���ص�Iterator˳���unitIDs��Iterator˳��һ�£�
	 * ���ص�Iterator����˳���taskTimes��Iterator˳��һ�¡�
	 *
	 * �����λ��������ĳ������ĳЩ��λ������,���صĽ���������û����Ӧ��TaskData.
	 * <p>
	 * ����DataExporter��ȡ���ݣ�ÿ��ֻ�ܵõ�һ��TaskTime����������������岻��
	 * ���淽���ǣ���ε���get(Collection unitIDs, TaskTime taskTime)
	 * </p>
	 * @param unitIDs ��λID����
	 * @param taskTimes ����ʱ��TaskTime����
	 * @return �������ݶ���TaskData���ϵ�Iterator
	 * @throws ModelException
	 */
	public Iterator get(Collection unitIDs, Collection taskTimes) throws
		ModelException;

	/**
	 * ����ָ������������.
	 * <p>
	 * ���²�һ��Ҫ���Ϸ�Ӧ�����ݿ�.
	 * ���Ҫ�����Ϸ�Ӧ�����ݿ�,����{@link commit}����.
	 * ����update����,Ҫ������һ����ʱ��,�Լ���ʽ�ĵ���commit����.
	 * �������ݿⷽʽʵ��,������һ���Ĵ���,����10��update������,�����ݿ��ύ��¼.
	 * </p>
	 * @param taskData Ҫ���µ��������ݶ���
	 * @throws ModelException
	 */
	public void update(TaskData taskData) throws ModelException;

	/**
	 * �ύ���ݼ�¼,ʹ֮�����Ա���.
	 * @throws ModelException
	 */
	public void commit() throws ModelException;
	/**
	 * �ر������Դ.
	 *
	 *  DataSource�ر�,���Զ��ύ�޸ĵļ�¼.
	 * @throws ModelException
	 */
	public void close() throws ModelException;
	/**
	 * �õ�ȱʡ��TaskData��
	 * ��TaskData������������ֶ�ʹ��""���棬��ֵ���ֶ�ʹ��new Double( 0 )���棬
	 * ������ʹ��new Date()���档
	 * @param unitID           ��λid
	 * @return                 TaskData
	 * @throws ModelException
	 */
	public TaskData getEmptyTaskData( String unitID )
		throws ModelException;
}