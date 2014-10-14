package cn.com.youtong.apollo.analyse.db;

import cn.com.youtong.apollo.services.*;

import cn.com.youtong.apollo.analyse.*;

public class DBAnalyseManagerFactory extends DefaultFactory implements AnalyseManagerFactory
{
    public DBAnalyseManagerFactory()
    {
    }

	public AnalyseManager createAnalyseManager()
		throws AnalyseException {
		return new DBAnalyseManager();
	}
}