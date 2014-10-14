package cn.com.youtong.apollo.tabtree.dbaccess;

import java.util.ArrayList;

import com.cc.framework.ui.model.TreeNodeDataModel;
import com.cc.framework.util.TreeHelp;
import cn.com.youtong.apollo.tabtree.dbaccess.exception.NodeNotFoundException;
import cn.com.youtong.apollo.tabtree.presentation.dsp.RegionCountryDsp;
import cn.com.youtong.apollo.tabtree.presentation.dsp.RegionDsp;
import cn.com.youtong.apollo.tabtree.presentation.dsp.RegionGroupDsp;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import cn.com.youtong.apollo.servlet.RootServlet;
import cn.com.youtong.apollo.usermanager.*;

/**
 * DataBase for the Sample-Application which holds the RegionData.
 *
 * @author		<a href="mailto:gschulz@scc-gmbh.com">Gernot Schulz</a>
 * @version	$Revision: 1.6 $
 */
public final class DBRegion
{

        private Collection collection = new Vector();

        // ------------------------------------------------
        //                    Methods
        // ------------------------------------------------

        /**
         * Constructor
         */
        public DBRegion()
        {
                super();

        }

        public synchronized RegionGroupDsp fetchDspOutline(HttpServletRequest request)
                throws Exception
        {
                boolean canAudit= RootServlet.hasPrivilege(request, SetOfPrivileges.PRIVILEGE_AUDIT);

                ArrayList list1 = new ArrayList();

                //处理存在多个总根
                boolean presenceRoot = false;
                Iterator ite = collection.iterator();
                int i = 0;
                while(ite.hasNext())
                {
                        List lst = (List) ite.next();
                        if(lst.get(4) == null||!isParentExist(collection,(String)lst.get(4)))
                        {
                                i++;
                                if(i > 1)
                                {
                                        presenceRoot = true;
                                        break;
                                }
                        }
                }
                
                
                if(presenceRoot)
                {
                        RegionDsp dsproot = new RegionGroupDsp();
                        dsproot.setRegion("");
                        dsproot.setName("");
                        dsproot.setFillState("");
                        dsproot.setParentKey(null);
                        list1.add(dsproot);
                }

                Iterator it = collection.iterator();
                while(it.hasNext())
                {
	                	RegionDsp dsp = null;
	                    List list = (List) it.next();
	
	                    String country = (String) list.get(0);
	
	                    if("G".equals(country))
	                    {
	                            dsp = new RegionGroupDsp();
	                    }
	                    else
	                    {
	                            dsp = new RegionCountryDsp();
	                    }
                        
                        if(presenceRoot && list.get(4) == null)
                        {
                                dsp.setParentKey("");
                        }
                        else
                        {
                        	    if(isParentExist(collection,(String)list.get(4))) dsp.setParentKey((String) list.get(4));
                        	    else{
                        	    	if(presenceRoot)dsp.setParentKey("");
                        	    	else dsp.setParentKey(null);
                        	    }
                        }
                        dsp.setRegion((String) list.get(1));
                        dsp.setName((String) list.get(2));
                        dsp.setFillState((String) list.get(3));
                        dsp.setId((String) list.get(5));
                        dsp.setDate((String) list.get(6));
                        //审核标志
                        String audit="";
                        boolean auditValue= ((Boolean) list.get(7)).booleanValue();
                        if(canAudit)
                        {
                                audit= "<input type=checkbox unit='" + dsp.getRegion() +"' onclick='setAudit(this)'";
                                if(auditValue)
                                        audit += " checked";
                                audit+= ">";
                        }
                        else
                        {
                                audit = "<font color=\"blue\">";
                                audit += auditValue ? " 审核" : "未审核";
                                audit += "</font>";
                        }
                        dsp.setAuditFlag(audit);
                        dsp.setAuditState((String) list.get(8));
                        dsp.setAuditDate((String) list.get(9));
                        try{
                          dsp.setAuditUser( (String) list.get(10));
                        }
                        catch(Exception ex)
                        {

                        }

                        list1.add(dsp);
                }

                return(RegionGroupDsp) TreeHelp.createOutline(list1);
        }

        public void setCollection(Collection col)
        {
                collection = col;
        }

        /**
         * Returns the Data for the Region
         * @param	id	Identifier
         * @return	RegionDsp
         * @throws	NodeNotFoundException
         * @throws	Exception
         */
        public synchronized RegionDsp getRegionByKey(String id, HttpServletRequest request)
                throws NodeNotFoundException, Exception
        {

                TreeNodeDataModel node = TreeHelp.getNodeByKey(fetchDspOutline(request), id);

                return(RegionDsp) node;
        }
        
        private boolean isParentExist(Collection con,String parID){
        	boolean flag = false;
        	Iterator it = con.iterator();
        	while(it.hasNext()){
        		List list = (List) it.next();
        		if(((String)list.get(1)).equalsIgnoreCase(parID)){
        			flag = true;
        			break;
        		}
        	}
        	return flag;
        }

}
