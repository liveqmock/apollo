package cn.com.youtong.apollo.servlet.unittree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 *��λ��ѡ��
 */
public class UnitCheckboxTree extends UnitTree {

  private String taskID;
  public UnitCheckboxTree(String taskID)
  {
          super(taskID);
          this.taskID = taskID;
  }

  protected String onClickAction(UnitTreeNode node)
  {
          return "javascript:changeUnit('" + node.id() + "','" + node.getUnitName() + "')";
  }
  protected String treeStyle()
  {
          return this.CHECKBOX_XLOAD_TREE;
  }

  protected String checkValue(UnitTreeNode node)
  {
          return node.id();
  }


}