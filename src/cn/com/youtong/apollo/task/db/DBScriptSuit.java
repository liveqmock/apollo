package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import java.util.*;

/**
 * 脚本组
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
     * 得到脚本组的创建时间
     * @return 脚本组的创建时间
     */
    public Date getDateCreated()
    {
        return _created;
    }

    /**
     * 得到脚本组的修改时间
     * @return 脚本组的修改时间
     */
    public Date getDateModified()
    {
        return _modified;
    }

    /**
     * 得到按执行顺序排列的脚本Script集合
	 * @param form    ScriptForm
     * @return 按执行顺序排列的脚本Script集合
     * @throws TaskException 操作失败
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
     * 得到脚本组中可运行的运算脚本Script集合
     * @return 脚本组中可运行的运算脚本Script集合
     * @throws TaskException
     */
    public List getCalculateScriptToExec() throws TaskException
    {
        return _calcuScripts;
    }

	private List getCalculateScripts( ScriptSuitForm form )
	{
		//得到指定执行顺序的脚本集合
		List result = getScriptsByExecSeqence( form );
		//没有指定执行顺序，得到所有运算脚本集合
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
     * 得到脚本组中可运行的审核脚本Script集合
     * @return 脚本组中可运行的审核脚本Script集合
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
     * 得到指定名称的运算脚本
     * @param name 脚本名称
     * @return 指定名称的运算脚本，没有找到，返回null
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
     * 得到所有脚本Script集合的Iterator
     * @return 所有脚本Script集合的Iterator
     * @throws TaskException 操作失败
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
     * 得到脚本组的名称
     * @return 脚本的名称
     */
    public String getName()
    {
        return _name;
    }

    /**
     * 得到备注
     * @return 备注
     */
    public String getMemo()
    {
        return _memo;
    }

}