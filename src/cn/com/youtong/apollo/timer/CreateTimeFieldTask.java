package cn.com.youtong.apollo.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.factory.FactoryException;

import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.TaskException;
import cn.com.youtong.apollo.task.TaskManager;
import cn.com.youtong.apollo.task.TaskManagerFactory;

/**
 * 根据当前的年份，创建第二年12个月份时间数据列表
 **/
public class CreateTimeFieldTask extends TimerTask {
	
	private Log log = LogFactory.getLog(this.getClass());
	private String taskID = "NEWQYKB";
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		System.out.println("=========================");
		System.out.println(calendar.get(Calendar.YEAR));
		System.out.println("=========================");
	}
	@Override
	public void run() {
	   try {
		   Calendar calendar = Calendar.getInstance();
		   TaskManager taskMng = ( (TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		   taskMng.publishTaskTime(taskID, calendar.get(Calendar.YEAR)+1);
		} catch (Exception e) {
			log.error("任务时间已经创建失败!",e);
		}
	    log.debug("任务时间已经创建完成");
	} 
}
