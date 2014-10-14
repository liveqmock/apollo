package cn.com.youtong.apollo.data;

import cn.com.youtong.apollo.usermanager.*;

/**
 * <p>Title:单位分配的信息 值对象 </p>
 * <p>Description: 该值对象包含了三方面信息<br><li>单位代码<li>组信息<li>单位权限信息</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class UnitAssignment
{
  /**单位代码*/
  private String unitID;
  /**单位名称*/
  private String unitName;
  /**组信息*/
  private Group group;
  /**单位权限信息*/
  private UnitPermission unitPermission;

  public UnitAssignment(String unitID, String unitName, Group group,
                        UnitPermission unitPermission)
  {
    this.unitID = unitID;
    this.unitName = unitName;
    this.group = group;
    this.unitPermission = unitPermission;
  }

  public Group getGroup()
  {
    return group;
  }

  public UnitPermission getUnitPermission()
  {
    return unitPermission;
  }

  public String getUnitID()
  {
    return unitID;
  }

  public String getUnitName()
  {
    return unitName;
  }
}