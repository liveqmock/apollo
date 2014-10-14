package cn.com.youtong.apollo.tabtree;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import cn.com.youtong.apollo.tabtree.comm.*;
import cn.com.youtong.apollo.tabtree.dbaccess.*;
import cn.com.youtong.apollo.tabtree.presentation.dsp.*;
import com.cc.framework.adapter.struts.*;
import com.cc.framework.ui.control.*;

/**
 * This Class is responsible to instantiate our TreeListControl.
 * This Class also implements the Methode which are called if
 * an Event in our Controls occurs.
 *
 * @author		<a href="mailto:gschulz@scc-gmbh.com">Gernot Schulz</a>
 * @version	$Revision: 1.4 $
 */
public class RegionBrowseAction extends FWAction
{

	protected String BEANNAME;

	/**
	 * Constructor for RegionBrowseAction.
	 */
	public RegionBrowseAction()
	{
		super();

		BEANNAME = "regions";
	}

	/**
	 * @see com.cc.framework.adapter.struts.FWAction#doExecute(ActionContext)
	 */
	public void doExecute(ActionContext ctx)
		throws IOException, ServletException
	{

		try
		{
			this.refreshRegionList(ctx);
		}
		catch(Throwable t)
		{
                        t.printStackTrace();
			log.error(t);
			ctx.addGlobalError(Messages.ERROR_DATABASE_QUERY);
		}

		// Display the Page with the UserList
		ctx.forwardToInput();
	}

	/**
	 * Fill's and refresh's the ListControl from the Database
	 * @param	ctx	ControlActionContext
	 * @throws	java.lang.Exception
	 */
	protected void refreshRegionList(ActionContext ctx)
		throws Exception
	{
		if(null != ctx.session().getAttribute(BEANNAME))
		{
			ctx.session().removeAttribute(BEANNAME);
		}

		DBRegion dbregion = new DBRegion();
		dbregion.setCollection((Collection) ctx.session().getAttribute("fillstates"));
		RegionGroupDsp dspData = dbregion.fetchDspOutline(ctx.request());

		TreelistControl regionList = new TreelistControl();
		regionList.setDataModel(dspData);

		ctx.session().setAttribute("collection", (Collection) ctx.session().getAttribute("fillstates"));
		ctx.session().setAttribute(BEANNAME, regionList);
		ctx.session().removeAttribute("fillstates");
	}

	// ------------------------------------------------
	//          TreeList-Control  Event Handler
	// ------------------------------------------------

	/**
	 * This Method is called if the Refresh-Button is clicked
	 * @param	ctx	ControlActionContext
	 * @throws	java.lang.Exception
	 */
	public void regions_onRefresh(ControlActionContext ctx)
		throws Exception
	{
		try
		{
			this.refreshRegionList(ctx);
		}
		catch(Throwable t)
		{
			log.error(t);
			ctx.addGlobalError(Messages.ERROR_DATABASE_QUERY, t);
		}
	}

	/**
	 * This Method is called when the Drilldown-Column is clicked
	 * In our Example we switch to the DetailView.
	 * @param	ctx		ControlActionContext
	 * @param	key 	UniqueKey, as it was created in the Datamodel (e.g. the Primarykey)
	 */
	public void regions_onDrilldown(ControlActionContext ctx, String key)
	{
		ctx.forwardByName(Forwards.DRILLDOWN, key);
	}

	/**
	 * This Method is called when the Add-Button is clicked
	 * @param	ctx		ControlActionContext
	 * @param	key 	UniqueKey, as it was created in the Datamodel (e.g. the Primarykey)
	 */
	public void regions_onAdd(ControlActionContext ctx, String key)
	{
		ctx.forwardByName(Forwards.ADD, key);
	}

}
