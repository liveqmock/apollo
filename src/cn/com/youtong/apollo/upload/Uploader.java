package cn.com.youtong.apollo.upload;

import java.util.List;

import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Uploader
{
	/** 向上级服务器上报数据 */
    public void upload();

	public void upload( Task task, TaskTime taskTime, List unitIDs,
						String username, String password )
		throws UploadException;
}