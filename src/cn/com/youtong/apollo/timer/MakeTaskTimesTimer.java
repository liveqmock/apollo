package cn.com.youtong.apollo.timer;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.com.youtong.apollo.services.ApolloService;
import cn.com.youtong.apollo.services.Config;
public class MakeTaskTimesTimer implements ServletContextListener{
	private Timer timer = null;  
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁");  
	}
	public Date startTaskTime(){
		Date time = null;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		
		/*calendar.set(Calendar.MONTH, Config.getInt("cn.com.youtong.appollo.tasktime.month"));
		calendar.set(Calendar.DATE, Config.getInt("cn.com.youtong.appollo.tasktime.date"));
		calendar.set(Calendar.HOUR_OF_DAY, Config.getInt("cn.com.youtong.appollo.tasktime.hour"));  
		calendar.set(Calendar.MINUTE, Config.getInt("cn.com.youtong.appollo.tasktime.minute"));  
		calendar.set(Calendar.SECOND, Config.getInt("cn.com.youtong.appollo.tasktime.second")); 
		*/
		/*calendar.set(Calendar.MONTH, 12);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 1);  
		calendar.set(Calendar.SECOND, 1); */
		
		/*calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 1);  
		calendar.set(Calendar.SECOND,1); */
		
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 4);
		calendar.set(Calendar.HOUR_OF_DAY, 20);  
		calendar.set(Calendar.MINUTE, 49);  
		calendar.set(Calendar.SECOND,1); 
		
		time = calendar.getTime();
		System.out.println("启动任务时间====>"+time);
		return time;
	}
	
	
	public void contextInitialized(ServletContextEvent event) {
		 //在这里初始化监听器，在tomcat启动的时候监听器启动，可以在这里实现定时器功能
		 timer = new Timer(true);
		 event.getServletContext().log("创建上报数据任务时间定时器已启动");//添加日志，可在tomcat日志中查看到
		 Date time = startTaskTime();   
		 timer.schedule(new CreateTimeFieldTask(), time);
	}
}
