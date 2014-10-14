package cn.com.youtong.apollo.tabtree.presentation.dsp;

import com.cc.framework.ui.model.TreeNodeDataModel;
import cn.com.youtong.apollo.tabtree.comm.RegionType;

/**
 * Displayobject for a Country in the TreeList
 *
 * @author		<a href="mailto:gschulz@scc-gmbh.com">Gernot Schulz</a>
 * @version	$Revision: 1.4 $
 */
public class RegionCountryDsp extends RegionDsp implements TreeNodeDataModel {

        /**
         * The id for the Country
         */
        private String country = null;


        // -------------------------------------------------
        //                    Methods
        // -------------------------------------------------

        /**
         * Constructor
         */
        public RegionCountryDsp() {
                super();
        }

        /**
         * Identifies the Object as a Country-Node
         * @return	RegionType
         */
        public RegionType getType() {
                return RegionType.COUNTRY;
        }

        /**
         * Returns the Value which ist matched by the imagemap
         * to display a icon in the tree for this item.
         * @return	String
         */
        public String getImgType() {
                return RegionType.COUNTRY.getValue().toLowerCase();
        }

        /**
         * Gets the country
         * @return Returns a String
         */
        public String getCountry() {
                return country;
        }

        /**
         * Sets the country
         * @param country The country to set
         */
        public void setCountry(String country) {
                this.country = country;
        }

        /**
         * @see RegionDsp#getAdd()
         */
        public boolean getAdd() {
                // We don't allow other Child's under a Country-Node
                // So the TreeControl schould not display any Add-
                // Buttons for Country-Nodes
                return false;
        }
}
