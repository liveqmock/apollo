package cn.com.youtong.apollo.tabtree.presentation.dsp;

import com.cc.framework.common.DisplayObject;
import com.cc.framework.ui.model.TreeGroupDataModel;
import cn.com.youtong.apollo.tabtree.comm.RegionType;

/**
 * Superclass for all Elements in the RegionTree
 *
 * @author		<a href="mailto:gschulz@scc-gmbh.com">Gernot Schulz</a>
 * @version	$Revision: 1.5 $
 */
public abstract class RegionDsp
    implements DisplayObject {

  /**
   * The unique Key for the Region
   */
  private String region = null;

  /**
   * The Displayvalue for the Key
   */
  private String name = "";

  /**
   * ÃÓ±®◊¥Ã¨
   */
  private String fillState = "∑Ò";

  /**
   * Key of the Parent Region under which the
   * Node is added
   */
  private String parentKey = "";

  /**
   * The ParentNode
   */
  private RegionGroupDsp parent = null;

  private String id = "";

  /**
   * ÃÓ±® ±º‰
   */
  private String date = "";

  /**
   * …Û∫À±Í÷æ
   */
  private String audit = "";

  /**
   * …Û∫À◊¥Ã¨
   */
  private String auditState = "";

  /**
   * …Û∫À ±º‰
   */
  private String auditDate = "";

  private String auditUser = "";

  // -------------------------------------------------
  //                    Methods
  // -------------------------------------------------

  /**
   * Constructor
   */
  public RegionDsp() {
    super();
  }

  /**
   * Gets the region
   * @return Returns a String
   */
  public String getRegion() {
    return region;
  }

  /**
   * Sets the region
   * @param region The region to set
   */
  public void setRegion(String region) {
    this.region = region;
  }

  /**
   * @see  com.cc.framework.ui.model.TreeNodeDataModel#getParent()
   */
  public final TreeGroupDataModel getParent() {
    return parent;
  }

  /**
   * @see  com.cc.framework.ui.model.TreeNodeDataModel#setParent(TreeGroupDataModel)
   */
  public final void setParent(TreeGroupDataModel parent) {
    this.parent = (RegionGroupDsp) parent;
  }

  /**
   * @see  com.cc.framework.ui.model.TreeNodeDataModel#getParentKey()
   */
  public final String getParentKey() {
    return parentKey;
  }

  /**
   * @see  com.cc.framework.ui.model.TreeNodeDataModel#getUniqueKey()
   */
  public final String getUniqueKey() {
    return region;
  }

  /**
   * Gets the name
   * @return Returns a String
   */
  public String getName() {
    return "<a unit=" + region + " onclick='show();' style='CURSOR:hand'>" +
        name + "</a>";
  }

  /**
   * Sets the name
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the parentKey
   * @param	parentKey	The Parentkey
   */
  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  /**
   * Returns the Type of the Node
   * In our Sample there will be Regions and Countries.
   * So each Node must be identify as a Region or a Country
   * @return	RegionType
   */
  public abstract RegionType getType();

  /**
   * This Methode is called, when the Control is painted.
   * If the Method returns "True" the Add-Button is displayed
   * in the Add-Column. So ChildNodes can be inserted under
   * the current Node.
   * You can use a algorithm or condition to decide if a
   * Node can be inserted under the current Node
   * @return	boolean
   */
  public abstract boolean getAdd();

  /**
   * This Methode is called, when the Control is painted.
   * If the Method returns "True" the Edit-Button is displayed
   * in the EditColumn.
   * You can use a algorithm or condition to decide if the
   * User schould be able to Edit the Row (Node).
   * @return	boolean
   */
  public boolean getEditable() {
    // The Root-Element can't be edited
    return getParent() != null;
  }

  public String getFillState() {
    return fillState;
  }

  public void setFillState(String fillState) {
    this.fillState = fillState;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getAuditFlag() {
    return audit;
  }

  public void setAuditFlag(String audit) {
    this.audit = audit;
  }

  public String getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(String auditDate) {
    this.auditDate = auditDate;
  }

  public String getAuditState() {
    return auditState;
  }

  public void setAuditState(String auditState) {
    this.auditState = auditState;
  }

  public String getAuditUser() {
    return auditUser;
  }

  public void setAuditUser(String auditUser) {
    this.auditUser = auditUser;
  }
}
