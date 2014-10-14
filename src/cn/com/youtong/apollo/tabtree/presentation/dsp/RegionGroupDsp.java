package cn.com.youtong.apollo.tabtree.presentation.dsp;

import java.util.ArrayList;

import com.cc.framework.ui.model.TreeGroupDataModel;
import com.cc.framework.ui.model.TreeNodeDataModel;
import cn.com.youtong.apollo.tabtree.comm.RegionType;


/**
 * Display-Object for Group-Nodes in the TreeList
 *
 * @author	<a href="mailto:gschulz@scc-gmbh.com">Gernot Schulz</a>
 * @version	$Revision: 1.3 $
 */
public class RegionGroupDsp extends RegionDsp implements TreeGroupDataModel {

        /**
         * Aggregation of ChildNodes for this Node
         */
        private ArrayList children = new ArrayList();


        // -------------------------------------------------
        //                    Methods
        // -------------------------------------------------

        /**
         * Constructor
         */
        public RegionGroupDsp() {
                super();
        }

        /**
         * @see TreeGroupDataModel#getChild(int)
         */
        public TreeNodeDataModel getChild(int index) {
                return (TreeNodeDataModel) children.get(index);
        }

        /**
         * @see TreeGroupDataModel#addChild(TreeNodeDataModel)
         */
        public void addChild(TreeNodeDataModel child) {
                children.add(child);
        }

        /**
         * @see TreeGroupDataModel#size()
         */
        public int size() {
                return children.size();
        }

        /**
         * Identifies the Object as a Group-Node
         * or the Root-Element
         * @return	RegionType
         */
        public RegionType getType() {
                return RegionType.GROUP;
        }

        /**
         * Returns the Value which ist matched by the imagemap
         * to display a icon in the tree for this item.
         * @return	String
         */
        public String getImgType() {
                if (getUniqueKey().equals("*")) {
                        return RegionType.WORLD.getValue().toLowerCase();
                } else {
                        return RegionType.GROUP.getValue().toLowerCase();
                }
        }

        /**
         * @see RegionDsp#getAdd()
         */
        public boolean getAdd() {
                // A Group-Node can own other Group-Nodes or Country-Nodes
                // If the User should be able to add this Nodes,
                // we need an Add-Button.
                return true;
        }
}
