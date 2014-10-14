package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.script.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.script.ScriptEngine;

/**
 * �ű�
 */
public class DBScript implements Script
{
	private static Log log= LogFactory.getLog(Script.class);

	private int _type;
	private String _name;
	private String _content;
    public DBScript(ScriptForm form)
    {
		this._type = form.getType();
		this._name = form.getName();
		this._content = wrapScript( form) ;
    }

    /**
     * �õ��ű����ͣ����Ͷ�������ӿڳ���
     * @return �ű����ͳ���
     */
    public int getType()
    {
        return _type;
    }

    /**
     * �õ��ű�������ID
     * @return �ű�������ID
     */
    public String getName()
    {
        return _name;
    }

    /**
     * �õ��ű�������
     * @return �ű�������
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * ��װ�ű�����
     * @return ��װ��Ľű�����
     */
    private String wrapScript( ScriptForm form )
    {
		String content = "";
		try
		{
			content = Convertor.Clob2String(form.getContent());

            switch(form.getType())
			{
				case Script.AUDIT_CROSS_TABLE:
					content = ScriptEngine.ytauditScript2Js(content);
					content = "with(" + ScriptEngine.OBJECT_AUDIT + ") {\n"
						+ content + " \n}";
					break;
				case Script.AUDIT_IN_TABLE:
					content = ScriptEngine.ytauditScript2Js(content);
					content = "with(" + form.getName().toLowerCase() + ") { with("
						+ ScriptEngine.OBJECT_AUDIT + ") { \n" + content + " \n} }";
					break;
				case Script.CALCULATE_IN_TABLE:
					content = "with(" + form.getName().toLowerCase() + ") { \n"
						+ content + " \n}";
					break;
			}
		}
		catch(Exception ex)
		{
			log.error("�ű���װʧ��", ex);
		}
		return content;
    }
}