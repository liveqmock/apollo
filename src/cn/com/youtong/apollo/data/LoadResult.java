package cn.com.youtong.apollo.data;

import java.util.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.script.*;

/**
 * �������ݵ�����
 */
public class LoadResult
{
	public LoadResult()
	{

	}

	public LoadResult(TaskTime taskTime, Collection storedUnitIDs,
					  Collection newUnitIDs,
					  Collection overdueUnitIDs, Collection prohibitUnitIDs)
	{
		this.taskTime = taskTime;
		this.storedUnitIDs = storedUnitIDs;
		this.newUnitIDs = newUnitIDs;
		this.overdueUnitIDs = overdueUnitIDs;
		this.prohibitUnitIDs = prohibitUnitIDs;
	}

	/**
	 * ���ݵ���˽��Map�� key/value = unitID/AuditResult
	 */
	private Map auditResults = new HashMap();

	/**
	 * �������ݵ�����ʱ��
	 */
	private TaskTime taskTime;

	/**
	 * �������ݵĵ�λid�б�
	 */
	private Collection storedUnitIDs;

	/**
	 * �������ݵ�λid�б�
	 */
	private Collection overdueUnitIDs;

	/**
	 * û�е���Ȩ�޵õ�λid�б�
	 */
	private Collection prohibitUnitIDs;

	/** ������λ�б� */
	private Collection newUnitIDs;

	public TaskTime getTaskTime()
	{
		return taskTime;
	}

	/**
	 * ���ص������ݵĵ�λid�б��Iterator��
	 * @return �������ݵĵ�λid�б��Iterator
	 */
	public Iterator getStoredUnitIDs()
	{
		return storedUnitIDs.iterator();
	}

	/**
	 * �������ݵ�λid��
	 * ����������ݿ��Ե��룬��ô<code>getOverdueUnitIDs</code>���ؿյ�Iterator
	 * @return   �������ݵ�λid
	 */
	public Iterator getOverdueUnitIDs()
	{
		return overdueUnitIDs.iterator();
	}

	/**
	 * û�е���Ȩ�޵�λid
	 * @return       û�е���Ȩ�޵�λid
	 */
	public Iterator getProhibitUnitIDs()
	{
		return prohibitUnitIDs.iterator();
	}

	/**
	 * �����ϱ��������µ�λID���ϵ�Iterator
	 * @return �ϱ��������µ�λID���ϵ�Iterator
	 */
	public Iterator getNewUnitIDs()
	{
		return newUnitIDs.iterator();
	}

	/**
	 * �����ϱ�������û��ͨ����˵ĵ�λID���ϵ�Iterator
	 * @return �ϱ��������µ�λID���ϵ�Iterator
	 */
	public Iterator getAuditFailedUnitIDs()
	{
		LinkedList result = new LinkedList();

		for(Iterator itr = auditResults.entrySet().iterator(); itr.hasNext();)
		{
			Map.Entry entry = (Map.Entry)itr.next();
			String unitID = (String)entry.getKey();
			AuditResult auditResult = (AuditResult)entry.getValue();

			if (auditResult.getErrors().size() > 0)
			{
				result.add(unitID);
			}
		}

		return result.iterator();

	}

	/**
	 * ���һ����˽��
	 * @param unitID ��λid
	 * @param auditResult ��˽��
	 */
	public void addAuditResult(String unitID, AuditResult auditResult)
	{
		auditResults.put(unitID, auditResult);
	}

	/**
	 * �õ������ϱ�����Ϣ
	 * @return �����ϱ�����Ϣ
	 */
	public String getMessage()
	{
		StringBuffer message = new StringBuffer();

		message.append(messageStoredUnits());
		message.append(messageNewUnits());
		message.append(messageOverdueUnits());
		message.append(messageProhibitUnits());

		//�����Ϣ
		message.append( getAuditMessage() );

		return message.toString();
	}

	public StringBuffer getAuditMessage()
	{
		StringBuffer message = new StringBuffer();

		//�����Ϣ
		for(Iterator itr = auditResults.entrySet().iterator(); itr.hasNext();)
		{
			Map.Entry entry = (Map.Entry)itr.next();
			String unitID = (String)entry.getKey();
			AuditResult auditResult = (AuditResult)entry.getValue();

			if (auditResult.getErrors().size() > 0)
			{
				message.append("��λ" + unitID + "û��ͨ����ˡ�\r\n" +
							   getErrorMessage(auditResult));
			}
			else
			{
				message.append("��λ" + unitID + "ͨ����ˡ�\r\n");
			}
			if (auditResult.getWarnings().size() > 0)
			{
				message.append(getWarningMessage(auditResult));
			}
		}

		return message;
	}

	/**
	 * ��¼�ϱ����������й��ڵĵ�λ����
	 * @return ��Ϣ
	 */
	private StringBuffer messageOverdueUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = overdueUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();
			message.append("�������ݵ�λ��û�е��룩:\r\n");
			message.append(unitID);
			++count;
		}

		while (itr.hasNext())
		{
			message.append(",");
			++count;
			if (count % 5 == 1)
			{
				message.append("\r\n");

			}
			String unitID = (String) itr.next();
			message.append(unitID);
		}

		if( count > 0 )
			message.append( "\r\n�� ")
				.append( count )
				.append( " ��\r\n");
		return message;
	}

	/**
	 * ��¼�ϱ�����������û��Ȩ���ϱ��ĵ�λ����
	 * @return ��Ϣ
	 */
	private StringBuffer messageProhibitUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = prohibitUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("ȱ��Ȩ�޵ĵ�λ��û�е��룩:\r\n");
			message.append(unitID);
			++count;
		}

		while (itr.hasNext())
		{
			message.append(",");
			++count;
			if (count % 5 == 1)
			{
				message.append("\r\n");
			}
			String unitID = (String) itr.next();
			message.append(unitID);
		}

		if( count > 0 )
			message.append( "\r\n�� ")
				.append( count )
				.append( " ��\r\n");

		return message;
	}

	/**
	 * ��¼�ϱ������������Ѿ�װ�뵽���ݿ��еĵ�λ����
	 * @return ��Ϣ
	 */
	private StringBuffer messageStoredUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = storedUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("���ݳɹ�װ��ĵ�λ:\r\n");
			message.append(unitID);
			++count;
		}

		while (itr.hasNext())
		{
			message.append(",");
			++count;
			if (count % 5 == 1)
			{
				message.append("\r\n");
			}
			String unitID = (String) itr.next();
			message.append(unitID);
		}

		if( count > 0 )
			message.append( "\r\n�� ")
				.append( count )
				.append( " ��\r\n");

		return message;
	}

	/**
	 * ��¼�ϱ������������Ѿ�װ�뵽���ݿ��е��µ�λ����
	 * @return ��Ϣ
	 */
	private StringBuffer messageNewUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = newUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("���ݳɹ�װ����µ�λ:\r\n");
			message.append(unitID);
			++count;
		}

		while (itr.hasNext())
		{
			message.append(",");
			++count;
			if (count % 5 == 1)
			{
				message.append("\r\n");
			}
			String unitID = (String) itr.next();
			message.append(unitID);
		}

		if( count > 0 )
			message.append( "\r\n�� ")
				.append( count )
				.append( " ��\r\n");

		return message;
	}

	/**
	 * �õ���˽���Ĵ�����Ϣ
	 * @param auditResult ��˽��
	 * @return ��˽���Ĵ�����Ϣ
	 */
	private String getErrorMessage(AuditResult auditResult)
	{
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("������" + errors.size() + ":\r\n");
		for (int i = 0; i < errors.size(); i++)
		{
			errorMessage.append("��" + (i + 1) + "��" + errors.get(i) + "\r\n");
		}
		return errorMessage.toString();
	}

	/**
	 * �õ���˽���ľ�����Ϣ
	 * @param auditResult ��˽��
	 * @return ��˽���ľ�����Ϣ
	 */
	private String getWarningMessage(AuditResult auditResult)
	{
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("������" + warnings.size() + ":\r\n");
		for (int i = 0; i < warnings.size(); i++)
		{
			warningMessage.append("��" + (i + 1) + "��" + warnings.get(i) +
								  "\r\n");
		}
		return warningMessage.toString();
	}

}