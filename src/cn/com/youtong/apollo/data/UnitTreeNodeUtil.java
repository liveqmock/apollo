package cn.com.youtong.apollo.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

public class UnitTreeNodeUtil {

  public static void getAllChildren(Collection col, UnitTreeNode node) {
    col.add(node);
    Iterator iter = node.getChildren();

    while (iter.hasNext()) {
      getAllChildren(col, (UnitTreeNode) iter.next());
    }
  }

  /**
   * 得到所有的孩子节点
   * @param map
   * @param node
   */
  public static void getAllChildren(HashMap totalMap, UnitTreeNode unitTreeNode) {
    String reportType = unitTreeNode.getReportType();
    if (reportType == null) {
      putNode(totalMap, unitTreeNode.getUnitCode() + "0", unitTreeNode);
      putNode(totalMap, unitTreeNode.getUnitCode() + "9", unitTreeNode);
    }
    else {
      putNode(totalMap, unitTreeNode.getUnitCode() + reportType,
              unitTreeNode);
    }
    Iterator iter = unitTreeNode.getChildren();
    while (iter.hasNext()) {
      getAllChildren(totalMap, (UnitTreeNode) iter.next());
    }
  }

  /**
   *
   * @param totalMap
   * @param key
   * @param value
   */
  public static void putNode(HashMap totalMap, String key,
                             UnitTreeNode unitTreeNode) {
    if (!totalMap.containsKey(key)) {
      totalMap.put(key, unitTreeNode);
    }

  }

  /**
   * 得到所有的孩子节点
   * @param map
   * @param node
   */
  public static void getAllSon(HashMap map, UnitTreeNode node) {
    if (!map.containsKey(node.getUnitCode() + node.getReportType())) {
      map.put(node.getUnitCode() + node.getReportType(), node);
    }
    Iterator iter = node.getChildren();
    while (iter.hasNext()) {
      UnitTreeNode unitTreeNode = (UnitTreeNode) iter.next();
      if (!map.containsKey(unitTreeNode.getUnitCode() +
                           unitTreeNode.getReportType())) {
        map.put(unitTreeNode.getUnitCode() + unitTreeNode.getReportType(),
                unitTreeNode);
      }
    }
  }

  /**
   * 得到所有的祖先
   * @param map
   * @param node
   */
  public static void getAllAncestor(HashMap map, UnitTreeNode node) {
    UnitTreeNode parentNode = node.getParent();
    if (parentNode != null) {
      if (!map.containsKey(parentNode.getUnitCode() + parentNode.getReportType())) {
        map.put(parentNode.getUnitCode() + parentNode.getReportType(),
                parentNode);
      }
      getAllAncestor(map, parentNode);
    }
  }

}