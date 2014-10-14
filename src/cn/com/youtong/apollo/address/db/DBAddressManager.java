package cn.com.youtong.apollo.address.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.address.*;
import cn.com.youtong.apollo.address.db.form.*;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.Factory;
import org.apache.fulcrum.factory.*;

public class DBAddressManager
	implements AddressManager
{
	private Log log = LogFactory.getLog(DBAddressManager.class);
	private String QUERY_PK_HQL = "SELECT COUNT(*) from AddressInfoForm as info WHERE info.comp_id.taskID = ? AND info.comp_id.unitID = ?";
	private String QUERY_ALL_HQL = "SELECT addr FROM AddressInfoForm as addr";
	private String QUERY_TASK_ADDR_HQL = "SELECT addr FROM AddressInfoForm as addr WHERE addr.comp_id.taskID =?";

	public DBAddressManager()
	{
	}

	public void addAddressInfo(AddressInfo info)
		throws AddressException
	{
		AddressInfoPK pk = info.getPK();
		AddressInfoFormPK formPK = new AddressInfoFormPK(pk.getTaskID(), pk.getUnitID());
		AddressInfoForm form = new AddressInfoForm();
		form.setComp_id(formPK);
		form.setEmail(info.getEmail());
		form.setFax(info.getFax());
		form.setMobile(info.getMobile());
		form.setPhone(info.getPhone());

		Session session = null;
		Transaction trans = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(isAddressInfoDefined(formPK, session))
			{
				throw new AddressException("指定的单位联系信息已经存在");
			}
			trans = session.beginTransaction();
			session.save(form);
			trans.commit();
		}
		catch(HibernateException ex)
		{
			log.error("创建单位联系失败", ex);
			if(trans != null)
			{
				try
				{
					trans.rollback();
				}
				catch(HibernateException ex1)
				{
				}
			}
			throw new AddressException("创建单位联系失败");
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex2)
				{
				}
			}
		}
	}

	public void deleteAddressInfo(AddressInfoPK pk)
		throws AddressException
	{
		AddressInfoFormPK formPK = new AddressInfoFormPK(pk.getTaskID(), pk.getUnitID());
		AddressInfoForm form = new AddressInfoForm();
		form.setComp_id(formPK);

		Session session = null;
		Transaction trans = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(!isAddressInfoDefined(formPK, session))
			{
				throw new AddressException("指定的单位地址信息不存在");
			}
			trans = session.beginTransaction();
			session.delete(form);
			trans.commit();
		}
		catch(HibernateException ex)
		{
			log.error("删除单位联系信息失败", ex);
			if(trans != null)
			{
				try
				{
					trans.rollback();
				}
				catch(HibernateException ex1)
				{
				}
			}
			throw new AddressException("删除单位联系信息失败");
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex2)
				{
				}
			}
		}
	}

	public AddressInfo findByPK(AddressInfoPK pk)
		throws AddressException
	{
		AddressInfoFormPK formPK = new AddressInfoFormPK(pk.getTaskID(), pk.getUnitID());
		AddressInfoForm form = new AddressInfoForm();
		form.setComp_id(formPK);

		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(!isAddressInfoDefined(formPK, session))
			{
				return null;
			}
			session.load(form, formPK);

			DBAddressInfo info = new DBAddressInfo(form);
			String taskID = info.getPK().getTaskID();
			String unitID = info.getPK().getUnitID();

			ModelManagerFactory modelMngFcty = (ModelManagerFactory)
				Factory.getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			Collection col = modelMng.getUnitTreeManager().getUnits(new String[] { unitID });
			Iterator iter = col.iterator();
			UnitTreeNode node = (UnitTreeNode) iter.next();
			info.setUnitTreeNode(node);

			return new DBAddressInfo(form);
		}
		catch(HibernateException ex)
		{
			log.error("查找单位联系信息失败", ex);
			throw new AddressException("查找单位联系信息失败");
		}
		catch(Exception ex)
		{
			log.error("查找单位联系信息失败", ex);
			throw new AddressException(ex);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex2)
				{
				}
			}
		}
	}

	public void updateAddressInfo(AddressInfo info)
		throws AddressException
	{
		AddressInfoPK pk = info.getPK();
		AddressInfoFormPK formPK = new AddressInfoFormPK(pk.getTaskID(), pk.getUnitID());
		AddressInfoForm form = new AddressInfoForm();
		form.setComp_id(formPK);

		Session session = null;
		Transaction trans = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(!isAddressInfoDefined(formPK, session))
			{
				throw new AddressException("单位联系信息不存在");
			}
			trans = session.beginTransaction();
			session.load(form, formPK);
			form.setEmail(info.getEmail());
			form.setFax(info.getFax());
			form.setMobile(info.getMobile());
			form.setPhone(info.getPhone());
			session.update(form);
			trans.commit();
		}
		catch(HibernateException ex)
		{
			if(trans != null)
			{
				try
				{
					trans.rollback();
				}
				catch(HibernateException ex1)
				{
				}
			}
			log.error("更新单位联系信息失败", ex);
			throw new AddressException("更新单位联系信息失败");
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex2)
				{
				}
			}
		}
	}

	/**public Iterator getAllAddressInfo()
		throws AddressException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			Iterator formIter = session.find(QUERY_ALL_HQL).iterator();

			List list = new LinkedList();
			while (formIter.hasNext())
			{
				AddressInfoForm form = (AddressInfoForm) formIter.next();
				list.add(new DBAddressInfo(form));
			}
			return list.iterator();
		}
		catch(HibernateException ex)
		{
			throw new AddressException("查找所有单位联系信息出错", ex);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex1)
				{
				}
			}
		}
	}*/

	public Iterator getAddressInfoByTaskID(String taskID)
		throws AddressException
	{
		Session session = null;
		List list = new LinkedList();
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			Iterator formIter = session.find(this.QUERY_TASK_ADDR_HQL, taskID, Hibernate.STRING).iterator();

			while (formIter.hasNext())
			{
				AddressInfoForm form = (AddressInfoForm) formIter.next();
				list.add(new DBAddressInfo(form));
			}
		}
		catch(HibernateException ex)
		{
			log.error("", ex);
			throw new AddressException("根据任务ID查找所有单位联系信息出错", ex);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex1)
				{
				}
			}
		}

		String[] unitIDs = new String[list.size()];
		for (int i=0; i<unitIDs.length; i++)
		{
			DBAddressInfo info = (DBAddressInfo) list.get(i);
			unitIDs[i] = info.getPK().getUnitID();
		}

		Collection col = null;

		try
		{
			ModelManagerFactory modelMngFcty = (ModelManagerFactory)
				Factory.getInstance(ModelManagerFactory.class.getName());
			ModelManager modelMng = modelMngFcty.createModelManager(taskID);
			col = modelMng.getUnitTreeManager().getUnits(unitIDs);
		}
		catch(Exception ex2)
		{
			log.error("", ex2);
			throw new AddressException("根据任务ID查找所有单位联系信息出错", ex2);
		}

		Iterator iter = col.iterator();
		for (int i=0; i<unitIDs.length; i++)
		{
			DBAddressInfo info = (DBAddressInfo) list.get(i);
			UnitTreeNode node = (UnitTreeNode) iter.next();
			info.setUnitTreeNode(node);
		}

		return list.iterator();
	}

	private boolean isAddressInfoDefined(AddressInfoFormPK formPK, Session session)
		throws HibernateException
	{
		String[] params = new String[]
			{
			formPK.getTaskID(), formPK.getUnitID()};
		Type[] types = new Type[]
			{
			Hibernate.STRING, Hibernate.STRING};

		return((Integer) session.iterate(QUERY_PK_HQL, params, types).next()).intValue() > 0;
	}
}