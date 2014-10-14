package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import java.util.*;

/**
 * �ű���
 */
public class DBScriptSuit
    implements ScriptSuit
{
	private String _name;
	private String _memo;
	private Date _created;
	private Date _modified;

	private Set _scripts;
	private List _auditScripts;
	private List _calcuScripts;

    public DBScriptSuit(ScriptSuitForm form)
    {
		this._created = form.getDateCreated();
		this._modified = form.getDateModified();
		this._scripts = getAllScripts( form );
		this._name = form.getName();
		this._memo = form.getMemo();
		this._auditScripts = getAuditScripts();
		this._calcuScripts = getCalculateScripts( form );
    }

    /**
     * �õ��ű���Ĵ���ʱ��
     * @return �ű���Ĵ���ʱ��
     */
    public Date getDateCreated()
    {
        return _created;
    }

    /**
     * �õ��ű�����޸�ʱ��
     * @return �ű�����޸�ʱ��
     */
    public Date getDateModified()
    {
        return _modified;
    }

    /**
     * �õ���ִ��˳�����еĽű�Script����
	 * @param form    ScriptForm
     * @return ��ִ��˳�����еĽű�Script����
     * @throws TaskException ����ʧ��
     */
    private List getScriptsByExecSeqence( ScriptSuitForm form )
    {
        List scripts = new LinkedList();
        if(form.getExecSeqence() != null && !form.getExecSeqence().trim().equals(""))
        {
            StringTokenizer tokenizer = new StringTokenizer(form.getExecSeqence(),
                ",");
            while (tokenizer.hasMoreTokens())
            {
                DBScript script = getCalculateScript(tokenizer.nextToken());
                if (script != null)
                {
                    scripts.add(script);
                }
            }
        }

        return scripts;
    }

    /**
     * �õ��ű����п����е�����ű�Script����
     * @return �ű����п����е�����ű�Script����
     * @throws TaskException
     */
    public List getCalculateScriptToExec() throws TaskException
    {
        return _calcuScripts;
    }

	private List getCalculateScripts( ScriptSuitForm form )
	{
		//�õ�ָ��ִ��˳��Ľű�����
		List result = getScriptsByExecSeqence( form );
		//û��ָ��ִ��˳�򣬵õ���������ű�����
		if (result.size() == 0)
		{
			Iterator itr = _scripts.iterator();
			while (itr.hasNext())
			{
				Script script = (Script) itr.next();
				if (script.getType() == Script.CALCULATE_CROSS_TABLE ||
					script.getType() == Script.CALCULATE_IN_TABLE)
				{
					result.add(script);
				}
			}
		}
		return result;
	}

    /**
     * �õ��ű����п����е���˽ű�Script����
     * @return �ű����п����е���˽ű�Script����
     * @throws TaskException
     */
    public List getAuditScriptToExec() throws TaskException
    {
		return _auditScripts;
    }

	private List getAuditScripts()
	{
		List result = new LinkedList();

		Iterator itr = _scripts.iterator();

		while (itr.hasNext())
		{
			Script script = (Script) itr.next();
			if (script.getType() == Script.AUDIT_CROSS_TABLE ||
				script.getType() == Script.AUDIT_IN_TABLE)
			{
				result.add(script);
			}
		}
		return result;
	}

    /**
     * �õ�ָ�����Ƶ�����ű�
     * @param name �ű�����
     * @return ָ�����Ƶ�����ű���û���ҵ�������null
     */
    private DBScript getCalculateScript(String name)
    {
		DBScript result = null;

		for(Iterator itr = _scripts.iterator(); itr.hasNext();)
		{
			DBScript script = (DBScript) itr.next();

			if (script.getName().equals(name) && (result.getType() == Script.CALCULATE_CROSS_TABLE
				  || result.getType() == Script.CALCULATE_IN_TABLE))
			{
				result = script;
				break;
			}
		}
		return result;
    }

    /**
     * �õ����нű�Script���ϵ�Iterator
     * @return ���нű�Script���ϵ�Iterator
     * @throws TaskException ����ʧ��
     */
    public Iterator getAllScripts() throws TaskException
    {
        return _scripts.iterator();
    }

	private Set getAllScripts( ScriptSuitForm form )
	{
		Set scripts = new HashSet();
		Iterator itr = form.getScripts().iterator();
		while (itr.hasNext())
		{
			ScriptForm temp = (ScriptForm) itr.next();
			scripts.add(new DBScript( temp ));
		}
		return scripts;
	}

    /**
     * �õ��ű��������
     * @return �ű�������
     */
    public String getName()
    {
        return _name;
    }

    /**
     * �õ���ע
     * @return ��ע
     */
    public String getMemo()
    {
        return _memo;
    }

}