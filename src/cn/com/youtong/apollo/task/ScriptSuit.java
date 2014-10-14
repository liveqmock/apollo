package cn.com.youtong.apollo.task;

import java.util.*;
/**
 * 脚本组接口
 */

public interface ScriptSuit
{
    /**
     * 得到脚本组的创建时间
     * @return 脚本组的创建时间
     */
    public Date getDateCreated();

    /**
     * 得到脚本组的修改时间
     * @return 脚本组的修改时间
     */
    public Date getDateModified();

    /**
     * 得到脚本组中可运行的运算脚本Script集合
     * @return 脚本组中可运行的运算脚本Script集合
     * @throws TaskException
     */
    public List getCalculateScriptToExec() throws TaskException;

    /**
     * 得到脚本组中可运行的审核脚本Script集合
     * @return 脚本组中可运行的审核脚本Script集合
     * @throws TaskException
     */
    public List getAuditScriptToExec() throws TaskException;


    /**
     * 得到所有脚本Script集合的Iterator
     * @return 所有脚本Script集合的Iterator
     * @throws TaskException 操作失败
     */
    public Iterator getAllScripts() throws TaskException;

    /**
     * 得到脚本组的名称
     * @return 脚本的名称
     */
    public String getName();

    /**
     * 得到备注
     * @return 备注
     */
    public String getMemo();
}