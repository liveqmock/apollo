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
		event.getServletContext().log("��ʱ������");  
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
		 //�������ʼ������������tomcat������ʱ�����������������������ʵ�ֶ�ʱ������
		 timer = new Timer(true);
		 event.getServletContext().log("�����ϱ���������ʱ�䶨ʱ��������");//�����־������tomcat��־�в鿴��
		 Date time = startTaskTime();   
		 timer.schedule(new CreateTimeFieldTask(), time);
	}
}
