package cn.com.youtong.apollo.news.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import cn.com.youtong.apollo.news.YtaplNews;
import cn.com.youtong.apollo.news.base.BaseYtaplNewsDAO;


public class YtaplNewsDAO extends BaseYtaplNewsDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public YtaplNewsDAO () {
		_RootDAO.initialize();
	}
	
	public YtaplNews findByID(Integer newsID) throws HibernateException{
		YtaplNews news = new YtaplNews();
		Session s = null;
		try {
			s = getSession();
			List newss = s.createCriteria(getReferenceClass()).add(Expression.eq("Id", newsID)).list();
			if(newss.size()>0) news = (YtaplNews)(newss.iterator().next());
    		return news;
		}
		finally {
			closeSession(s);
		}
	}
}