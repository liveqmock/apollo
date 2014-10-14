package cn.com.youtong.apollo.data;

import java.util.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.script.*;

/**
 * 任务数据导入结果
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
	 * 数据的审核结果Map。 key/value = unitID/AuditResult
	 */
	private Map auditResults = new HashMap();

	/**
	 * 导入数据的任务时间
	 */
	private TaskTime taskTime;

	/**
	 * 导入数据的单位id列表
	 */
	private Collection storedUnitIDs;

	/**
	 * 过期数据单位id列表
	 */
	private Collection overdueUnitIDs;

	/**
	 * 没有导入权限得单位id列表
	 */
	private Collection prohibitUnitIDs;

	/** 新增单位列表 */
	private Collection newUnitIDs;

	public TaskTime getTaskTime()
	{
		return taskTime;
	}

	/**
	 * 返回导入数据的单位id列表的Iterator。
	 * @return 导入数据的单位id列表的Iterator
	 */
	public Iterator getStoredUnitIDs()
	{
		return storedUnitIDs.iterator();
	}

	/**
	 * 过期数据单位id。
	 * 如果过期数据可以导入，那么<code>getOverdueUnitIDs</code>返回空的Iterator
	 * @return   过期数据单位id
	 */
	public Iterator getOverdueUnitIDs()
	{
		return overdueUnitIDs.iterator();
	}

	/**
	 * 没有导入权限单位id
	 * @return       没有导入权限单位id
	 */
	public Iterator getProhibitUnitIDs()
	{
		return prohibitUnitIDs.iterator();
	}

	/**
	 * 返回上报数据中新单位ID集合的Iterator
	 * @return 上报数据中新单位ID集合的Iterator
	 */
	public Iterator getNewUnitIDs()
	{
		return newUnitIDs.iterator();
	}

	/**
	 * 返回上报数据中没有通过审核的单位ID集合的Iterator
	 * @return 上报数据中新单位ID集合的Iterator
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
	 * 添加一个审核结果
	 * @param unitID 单位id
	 * @param auditResult 审核结果
	 */
	public void addAuditResult(String unitID, AuditResult auditResult)
	{
		auditResults.put(unitID, auditResult);
	}

	/**
	 * 得到数据上报的信息
	 * @return 数据上报的信息
	 */
	public String getMessage()
	{
		StringBuffer message = new StringBuffer();

		message.append(messageStoredUnits());
		message.append(messageNewUnits());
		message.append(messageOverdueUnits());
		message.append(messageProhibitUnits());

		//审核信息
		message.append( getAuditMessage() );

		return message.toString();
	}

	public StringBuffer getAuditMessage()
	{
		StringBuffer message = new StringBuffer();

		//审核信息
		for(Iterator itr = auditResults.entrySet().iterator(); itr.hasNext();)
		{
			Map.Entry entry = (Map.Entry)itr.next();
			String unitID = (String)entry.getKey();
			AuditResult auditResult = (AuditResult)entry.getValue();

			if (auditResult.getErrors().size() > 0)
			{
				message.append("单位" + unitID + "没有通过审核。\r\n" +
							   getErrorMessage(auditResult));
			}
			else
			{
				message.append("单位" + unitID + "通过审核。\r\n");
			}
			if (auditResult.getWarnings().size() > 0)
			{
				message.append(getWarningMessage(auditResult));
			}
		}

		return message;
	}

	/**
	 * 记录上报数据中所有过期的单位数据
	 * @return 信息
	 */
	private StringBuffer messageOverdueUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = overdueUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();
			message.append("过期数据单位（没有导入）:\r\n");
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
			message.append( "\r\n共 ")
				.append( count )
				.append( " 个\r\n");
		return message;
	}

	/**
	 * 记录上报数据中所有没有权限上报的单位数据
	 * @return 信息
	 */
	private StringBuffer messageProhibitUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = prohibitUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("缺少权限的单位（没有导入）:\r\n");
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
			message.append( "\r\n共 ")
				.append( count )
				.append( " 个\r\n");

		return message;
	}

	/**
	 * 记录上报数据中所有已经装入到数据库中的单位数据
	 * @return 信息
	 */
	private StringBuffer messageStoredUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = storedUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("数据成功装入的单位:\r\n");
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
			message.append( "\r\n共 ")
				.append( count )
				.append( " 个\r\n");

		return message;
	}

	/**
	 * 记录上报数据中所有已经装入到数据库中的新单位数据
	 * @return 信息
	 */
	private StringBuffer messageNewUnits()
	{
		StringBuffer message = new StringBuffer();

		Iterator itr = newUnitIDs.iterator();
		int count = 0;
		if (itr.hasNext())
		{
			String unitID = (String) itr.next();

			message.append("数据成功装入的新单位:\r\n");
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
			message.append( "\r\n共 ")
				.append( count )
				.append( " 个\r\n");

		return message;
	}

	/**
	 * 得到审核结果的错误信息
	 * @param auditResult 审核结果
	 * @return 审核结果的错误信息
	 */
	private String getErrorMessage(AuditResult auditResult)
	{
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("错误数" + errors.size() + ":\r\n");
		for (int i = 0; i < errors.size(); i++)
		{
			errorMessage.append("（" + (i + 1) + "）" + errors.get(i) + "\r\n");
		}
		return errorMessage.toString();
	}

	/**
	 * 得到审核结果的警告信息
	 * @param auditResult 审核结果
	 * @return 审核结果的警告信息
	 */
	private String getWarningMessage(AuditResult auditResult)
	{
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("警告数" + warnings.size() + ":\r\n");
		for (int i = 0; i < warnings.size(); i++)
		{
			warningMessage.append("（" + (i + 1) + "）" + warnings.get(i) +
								  "\r\n");
		}
		return warningMessage.toString();
	}

}