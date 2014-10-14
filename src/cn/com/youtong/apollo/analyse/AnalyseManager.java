package cn.com.youtong.apollo.analyse;

import java.util.*;
import java.io.*;
import cn.com.youtong.apollo.analyse.form.*;

/**
 * ����Manager
 */
public interface AnalyseManager {
  /**
   * ����������ָ���ѯ
   * @param condition ��ѯ����
   * @return ��ѯ���
   * @throws AnalyseException ��ѯʧ��
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition) throws
      AnalyseException;

  /**
   * ��ģ�����ָ���ѯ
   * @param templateID ��ѯģ��ID
   * @param taskTimeIDs ����ʱ��ID����
   * @return ��ѯ���
   * @throws AnalyseException ��ѯʧ��
   */
  //public ScalarResultForm queryScalar(Integer templateID, Integer [] taskTimeIDs) throws AnalyseException;

  /**
   * �õ�ָ���û���ָ�������ָ���ѯģ��
   * @param userID �û�id
   * @param taskID ����id
   * @return ָ���ѯģ��ScalarQueryTemplate��Iterator
   * @throws AnalyseException
   */
  public Iterator getScalarQueryTemplates(Integer userID, String taskID) throws
      AnalyseException;

  /**
   * ����ָ���ѯģ��
   * @param userID �û�id
   * @param templateIn ģ��������
   * @throws AnalyseException
   */
  public void importScalarQueryTemplate(Integer userID, InputStream templateIn) throws
      AnalyseException;

  /**
   * ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @param out ����ģ��������
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(Integer templateID, OutputStream out) throws
      AnalyseException;

  /**
   * ����ָ���ѯģ��
   * @param taskID ����ID
   * @param userID �û�ID
   * @param name ģ������
   * @param out ����ģ��������
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(String taskID, Integer userID,
                                        String name, OutputStream out) throws
      AnalyseException;

  /**
   * �õ�ָ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @return ָ���ѯģ��
   * @throws AnalyseException
   */
  public ScalarQueryTemplate getScalarQueryTemplate(Integer templateID) throws
      AnalyseException;

  /**
   * ����ָ���ѯģ��
   * @param userID �û�ID
   * @param name ģ������
   * @param condition ��ѯ����
   * @return �ѱ����ģ��
   * @throws AnalyseException
   */
  //public ScalarQueryTemplate createScalarQueryTemplate(Integer userID, String name, ScalarQueryForm condition) throws AnalyseException;

  /**
   * ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @param name ģ������
   * @param condition ��ѯ����
   * @return �ѱ����ģ��
   * @throws AnalyseException
   */
  //public ScalarQueryTemplate updateScalarQueryTemplate(Integer templateID, String name, ScalarQueryForm condition) throws AnalyseException;

  /**
   * ɾ��ָ����ָ���ѯģ��
   * @param templateID ģ��id
   * @throws AnalyseException
   */
  public void deleteScalarQueryTemplate(Integer templateID) throws
      AnalyseException;

  /**
   * ��ѯ�������λ������
   * @param taskID ����id
   * @param taskTimeID ����ʱ��id
   * @return ����FillStateForm�ļ���
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID) throws
      AnalyseException;

  /**
   * ��ѯָ����������Ƶ����λ��������ģ����ѯ��
   * @param taskID ����id
   * @param taskTimeID ����ʱ��id
   * @param codeOrName ��λ�Ĵ��������
   * @return ����FillStateForm�ļ���
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID,
                                 String codeOrName) throws AnalyseException;

  public FillStateForm getFillStateByUnitID(String taskID, Integer taskTimeID,
                                            String unitID) throws
      AnalyseException;

  /**
   * ָ����λ��������ʱ�䣬ִ�б�ʶΪtemplateID�Ĳ�ѯģ��
   * @param templateID                 ��ѯģ��id
   * @param taskTimeIDs                ����ʱ��
   * @param unitIDs                    ��λ����
   * @return                           ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(Integer templateID,
                                      Integer[] taskTimeIDs,
                                      String[] unitIDs) throws AnalyseException;

  /**
   * ָ����Ҫ��ѯ�ĵ�λ����ģ���ѯ
   * @param condition           ��ѯ����
   * @param unitIDs               ָ���Ĳ�ѯ��λ
   * @return                    ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition,
                                      String[] unitIDs) throws AnalyseException;

  /**
   * �޸��״̬
   * @param stateType:״̬
   * @param taskIDs ����ID
   * @param unitIDs ��λID
   * @param taskTimeIDs ʱ��ID
   * @return
   * @throws AnalyseException
   */
  public List alertState(int stateType, int taskID, List unitIDs,
                         int taskTimeID) throws AnalyseException;

  /**
   * �Ƿ��ܹ��������
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws AnalyseException
   */
  public boolean canUnenvelopSubmitData(String taskID, int taskTimeID,
                                        String unitID) throws AnalyseException;

  /**
   * �������
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   */
  public void envelopSubmitData(String taskID, int taskTimeID, List unitIDList
                                ) throws AnalyseException;

  /**
   * �õ�����״̬
   * @param taskID
   * @param taskTimeID
   * @return
   */
  public HashMap getAllSubmitDataState(String taskID, int taskTimeID
                                       ) throws AnalyseException;

  /**
   * ��������
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
   * �ܱ༭����
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
 * ��ѯ���е�λ��������
 * @param taskID
 * @param taskTimeID
 * @return
 * @throws AnalyseException
 */
public List getAuditStates(String taskID,int taskTimeID) throws AnalyseException;

}