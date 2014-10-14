package cn.com.youtong.apollo.log;

import java.util.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * log管理器接口
 */
public interface LogManager
{
	/**
	 * 数据上报方式 -- 网上直报
	 */
	public static final int WEB_MODE = 0;

	/**
	 * 数据上报方式 -- 客户端直报
	 */
	public static final int CLIENT_MODE = 1;

	/**
	 * 数据上报方式 -- 邮件（文件）上报
	 */
	public static final int MAIL_MODE = 2;

	/**
	 * 记录安全日志
	 * @param timeOccured 事件发生的时间
	 * @param type 事件类型
	 * @param source 产生事件的客户计算机的IP地址
	 * @param userName 触发事件的登录用户的名称
	 * @param memo 事件的详细描述
	 * @throws LogException
	 */
	public void logSecurityEvent(Date timeOccured, int type, String source,
								 String userName, String memo) throws
		LogException;

	/**
	 * 记录数据日志
	 * @param timeOccured 事件发生的时间
	 * @param type 事件类型
	 * @param source 产生事件的客户计算机的IP地址
	 * @param userName 触发事件的登录用户的名称
	 * @param memo 事件的详细描述
	 * @throws LogException
	 */
	public void logDataEvent(Date timeOccured, int type, String source,
							 String userName, String memo) throws LogException;

	/**
	 * 查询事件
	 * @param condition 查询条件
	 * @return 满足条件的事件Event集合的Iterator
	 * @throws LogException
	 */
	public Iterator queryEvent(EventQueryCondition condition) throws
		LogException;

	/**
	 * 得到所有安全事件
	 * @return 安全事件Event集合的Iterator
	 * @throws LogException
	 */
	public Iterator getAllSecurityEvents() throws LogException;

        /**
         *
         * @param startPage
         * @param pageNumber
         * @return
         * @throws LogException
         */
        public Iterator getSecurityEvents(int startPage,int pageNumber) throws LogException;

        /**
         * 得到全部熟练
         * @return
         * @throws LogException
         */
        public int getSecurityEventsCount() throws LogException;

	/**
	 * 得到所有数据事件
	 * @return 数据事件Event集合的Iterator
	 * @throws LogException
	 */
	public Iterator getAllDataEvents() throws LogException;

	/**
	 * 记录数据上报日志
	 * @param loadResult 上报结果
	 * @param user 上报数据的用户
	 * @param source 来源（客户机ip）
	 * @param loadMode 上报方式 （0 -- web； 1 -- webservice； 2 -- 邮件（文件））
	 * @throws LogException
	 */
	public void logLoadDataEvent(LoadResult loadResult, User user,
								 String source,
								 int loadMode) throws LogException;

}