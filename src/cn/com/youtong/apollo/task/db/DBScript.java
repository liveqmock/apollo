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
 * 脚本
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
     * 得到脚本类型，类型定义见本接口常量
     * @return 脚本类型常量
     */
    public int getType()
    {
        return _type;
    }

    /**
     * 得到脚本的名称ID
     * @return 脚本的名称ID
     */
    public String getName()
    {
        return _name;
    }

    /**
     * 得到脚本的内容
     * @return 脚本的内容
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * 包装脚本内容
     * @return 包装后的脚本内容
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
			log.error("脚本包装失败", ex);
		}
		return content;
    }
}