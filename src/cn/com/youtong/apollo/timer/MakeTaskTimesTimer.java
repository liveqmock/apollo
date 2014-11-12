package cn.com.youtong.apollo.timer;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.com.youtong.apollo.services.Config;
public class MakeTaskTimesTimer implements ServletContextListener{
	private Timer timer = null;  
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("��ʱ������");  
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
		
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 1);  
		calendar.set(Calendar.SECOND,1); 
		
		time = calendar.getTime();
		System.out.println("��������ʱ��====>"+time);
		return time;
	}
	
	
	public void contextInitialized(ServletContextEvent event) {
		 //�������ʼ������������tomcat������ʱ�����������������������ʵ�ֶ�ʱ������
		 timer = new Timer(true);
		 event.getServletContext().log("�����ϱ���������ʱ�䶨ʱ��������");//�����־������tomcat��־�в鿴��
		 Date time = startTaskTime();   
		 timer.schedule(new CreateTimeFieldTask(), time);
	}
}
