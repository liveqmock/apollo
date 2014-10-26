package cn.com.youtong.apollo.timer;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 30);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 1);  
		calendar.set(Calendar.SECOND, 1); 
		time = calendar.getTime();
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
