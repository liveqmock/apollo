package cn.com.youtong.apollo.data;

/**
 * 用户对指定任务的单位ACL
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

import cn.com.youtong.apollo.usermanager.*;

public interface UnitACL
{
	/**
	 * 取要判断访问权限的用户
	 * @return ACL表的用户
	 */
	User getUser();

	/**
	 * 当前用户是否可以查看指定单位的数据
	 * @param unitID 单位ID
	 * @return TRUE - 可以读取， FALSE - 不可以读取
	 */boolean isReadable(String unitID);

	/**
	 * 单位数据是否可写
	 * @param unitID 单位ID
	 * @return TRUE - 可写，FALSE - 不可写
	 */
	boolean isWritable(String unitID);
}