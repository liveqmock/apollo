package cn.com.youtong.apollo.task;

/**
 * 任务时间.
 * <ul>时间
 * <li>任务时间
 * <li>上报开始
 * <li>催报时间
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface TaskTime
{
	/**
	 * 任务时间标识
	 * @return		任务时间标识
	 */
	Integer getTaskTimeID();

	/**
	 * 开始时间
	 * @return
	 */Date getFromTime();

	/**
	 * 截止时间
	 * @return
	 */Date getEndTime();

	/**
	 * 上报时间.
	 * @return
	 */Date getSubmitFromTime();

	/**
	 * 上报截至时间.
	 * @return
	 */Date getSubmitEndTime();

	/**
	 * 催报开始时间.
	 * @return
	 */Date getAttentionFromTime();

	/**
	 * 催报截止时间
	 * @return
	 */Date getAttentionEndTime();
}