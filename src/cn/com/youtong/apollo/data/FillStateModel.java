package cn.com.youtong.apollo.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Ìî³ä×´Ì¬
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class FillStateModel
    implements Serializable {

  private String untiID;
  private String taskID;
  private int taskTimeID;
  private Date fillData;
  private int flag;


  public Date getFillData() {
    return fillData;
  }

  public void setFillData(Date fillData) {
    this.fillData = fillData;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public String getTaskID() {
    return taskID;
  }

  public void setTaskID(String taskID) {
    this.taskID = taskID;
  }

  public int getTaskTimeID() {
    return taskTimeID;
  }

  public void setTaskTimeID(int taskTimeID) {
    this.taskTimeID = taskTimeID;
  }

  public String getUntiID() {
    return untiID;
  }

  public void setUntiID(String untiID) {
    this.untiID = untiID;
  }

}