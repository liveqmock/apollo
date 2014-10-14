package cn.com.youtong.apollo.data;

import java.util.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import cn.com.youtong.apollo.data.db.DBUnitTreeNode;

/**
 * 单位树管理器
 */
public interface UnitTreeManager
{
	/**
	 * 根据给定的taskID 查找该任务中的所有的单位(以单位对象集合的形式返回)
	 * 根据给的paramname  得到单元数据信息
	 * @param taskID 任务ID号
	 * @param session Hibernate对象
	 * @return 单位对象的集合
	 * @throws HibernateException
	 */
	public List<DBUnitTreeNode> getAllUnitTreeNodes(String taskID,String paramname,UnitACL unitACL);
	/**
	 * 判断单位是否存在
	 * @param unitID 单位ID号
	 * @return 布尔值
	 */
	public boolean isUnitExist(String unitID);

	/**
	 * 得到单位森林
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest() throws ModelException;

	/**
	 * 得到单位森林
	 * @param unitACL 访问控制权限
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest(UnitACL unitACL) throws ModelException;

	/**
	 * 获得单位数据对象(单独的对象，不包括下级节点)
	 * @param unitIDs 单位ID集合
	 * @return 单位对象集合(UnitTreeNode)
	 * @throws ModelException
	 */
	public Collection getUnits(String[] unitIDs) throws ModelException;

	/**
	 * 获得指定任务的单位树，以unitID指定的单位为根 若unitID不存在则抛出异常
	 * @param unitID 单位ID号
	 * @return 单位树
	 * @throws ModelException
	 */
	public UnitTreeNode getUnitTree(String unitID) throws ModelException;

	/**
	 * 创建单位
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @return 新创建的单位节点对象
	 * @throws ModelException
	 */
	public UnitTreeNode createUnit(String code, String name, String reportType,
						   String parentCode, String HQCode, String p_Parent) throws
		ModelException;

	/**
	 * 更新单位
	 * @param unitID 单位id
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @return 更新后的单位节点对象
	 * @throws ModelException
	 */
	public UnitTreeNode updateUnit(String unitID, String code, String name,
						   String reportType, String parentCode, String HQCode,
						   String p_Parent,int display) throws ModelException;

	/**
	 * 删除单位
	 * @param id 单位id
	 * @throws ModelException
	 */
	public void deleteUnit(String id) throws ModelException;
	
	//取得单位树下的所有单位
	public void getUnits(UnitTreeNode node, Map map);

}