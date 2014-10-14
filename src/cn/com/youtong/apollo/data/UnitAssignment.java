package cn.com.youtong.apollo.data;

import cn.com.youtong.apollo.usermanager.*;

/**
 * <p>Title:��λ�������Ϣ ֵ���� </p>
 * <p>Description: ��ֵ�����������������Ϣ<br><li>��λ����<li>����Ϣ<li>��λȨ����Ϣ</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class UnitAssignment
{
  /**��λ����*/
  private String unitID;
  /**��λ����*/
  private String unitName;
  /**����Ϣ*/
  private Group group;
  /**��λȨ����Ϣ*/
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