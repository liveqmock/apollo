package cn.com.youtong.apollo.analyse;

import java.util.*;
import java.io.*;
import cn.com.youtong.apollo.analyse.form.*;

/**
 * 分析Manager
 */
public interface AnalyseManager {
  /**
   * 按条件进行指标查询
   * @param condition 查询条件
   * @return 查询结果
   * @throws AnalyseException 查询失败
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition) throws
      AnalyseException;

  /**
   * 按模板进行指标查询
   * @param templateID 查询模板ID
   * @param taskTimeIDs 任务时间ID数组
   * @return 查询结果
   * @throws AnalyseException 查询失败
   */
  //public ScalarResultForm queryScalar(Integer templateID, Integer [] taskTimeIDs) throws AnalyseException;

  /**
   * 得到指定用户，指定任务的指标查询模板
   * @param userID 用户id
   * @param taskID 任务id
   * @return 指标查询模板ScalarQueryTemplate的Iterator
   * @throws AnalyseException
   */
  public Iterator getScalarQueryTemplates(Integer userID, String taskID) throws
      AnalyseException;

  /**
   * 导入指标查询模板
   * @param userID 用户id
   * @param templateIn 模板输入流
   * @throws AnalyseException
   */
  public void importScalarQueryTemplate(Integer userID, InputStream templateIn) throws
      AnalyseException;

  /**
   * 导出指标查询模板
   * @param templateID 模板ID
   * @param out 导出模板的输出流
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(Integer templateID, OutputStream out) throws
      AnalyseException;

  /**
   * 导出指标查询模板
   * @param taskID 任务ID
   * @param userID 用户ID
   * @param name 模板名称
   * @param out 导出模板的输出流
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(String taskID, Integer userID,
                                        String name, OutputStream out) throws
      AnalyseException;

  /**
   * 得到指定的指标查询模板
   * @param templateID 模板ID
   * @return 指标查询模板
   * @throws AnalyseException
   */
  public ScalarQueryTemplate getScalarQueryTemplate(Integer templateID) throws
      AnalyseException;

  /**
   * 创建指标查询模板
   * @param userID 用户ID
   * @param name 模板名称
   * @param condition 查询条件
   * @return 已保存的模板
   * @throws AnalyseException
   */
  //public ScalarQueryTemplate createScalarQueryTemplate(Integer userID, String name, ScalarQueryForm condition) throws AnalyseException;

  /**
   * 更新指标查询模板
   * @param templateID 模板ID
   * @param name 模板名称
   * @param condition 查询条件
   * @return 已保存的模板
   * @throws AnalyseException
   */
  //public ScalarQueryTemplate updateScalarQueryTemplate(Integer templateID, String name, ScalarQueryForm condition) throws AnalyseException;

  /**
   * 删除指定的指标查询模板
   * @param templateID 模板id
   * @throws AnalyseException
   */
  public void deleteScalarQueryTemplate(Integer templateID) throws
      AnalyseException;

  /**
   * 查询所有填报单位的填报情况
   * @param taskID 任务id
   * @param taskTimeID 任务时间id
   * @return 填报情况FillStateForm的集合
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID) throws
      AnalyseException;

  /**
   * 查询指定代码或名称的填报单位的填报情况（模糊查询）
   * @param taskID 任务id
   * @param taskTimeID 任务时间id
   * @param codeOrName 单位的代码或名称
   * @return 填报情况FillStateForm的集合
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID,
                                 String codeOrName) throws AnalyseException;

  public FillStateForm getFillStateByUnitID(String taskID, Integer taskTimeID,
                                            String unitID) throws
      AnalyseException;

  /**
   * 指定单位，和任务时间，执行标识为templateID的查询模板
   * @param templateID                 查询模板id
   * @param taskTimeIDs                任务时间
   * @param unitIDs                    单位代码
   * @return                           ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(Integer templateID,
                                      Integer[] taskTimeIDs,
                                      String[] unitIDs) throws AnalyseException;

  /**
   * 指定需要查询的单位进行模板查询
   * @param condition           查询条件
   * @param unitIDs               指定的查询单位
   * @return                    ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition,
                                      String[] unitIDs) throws AnalyseException;

  /**
   * 修改填报状态
   * @param stateType:状态
   * @param taskIDs 任务ID
   * @param unitIDs 单位ID
   * @param taskTimeIDs 时间ID
   * @return
   * @throws AnalyseException
   */
  public List alertState(int stateType, int taskID, List unitIDs,
                         int taskTimeID) throws AnalyseException;

  /**
   * 是否能够封存数据
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws AnalyseException
   */
  public boolean canUnenvelopSubmitData(String taskID, int taskTimeID,
                                        String unitID) throws AnalyseException;

  /**
   * 封存数据
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   */
  public void envelopSubmitData(String taskID, int taskTimeID, List unitIDList
                                ) throws AnalyseException;

  /**
   * 得到数据状态
   * @param taskID
   * @param taskTimeID
   * @return
   */
  public HashMap getAllSubmitDataState(String taskID, int taskTimeID
                                       ) throws AnalyseException;

  /**
   * 启封数据
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   * @param flag
   * @throws AnalyseException
   */
  public void unEnvelopSubmitData(String taskID, int taskTimeID,
                                  List unitIDList, int flag) throws
      AnalyseException;

  /**
   * 能编辑数据
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws AnalyseException
   */
  public boolean canEditorSubmitData(String taskID, int taskTimeID,
                                        String unitID) throws
      AnalyseException ;

  /**
 * 查询所有单位的审核情况
 * @param taskID
 * @param taskTimeID
 * @return
 * @throws AnalyseException
 */
public List getAuditStates(String taskID,int taskTimeID) throws AnalyseException;

}