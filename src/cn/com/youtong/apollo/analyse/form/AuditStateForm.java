package cn.com.youtong.apollo.analyse.form;

import java.util.Date;

public class AuditStateForm {
   private String unitID;
   private String  taskID;
   private Integer taskTimeID;
   private Date    auditDate;
   private Integer flag;
   private String auditor;

  public AuditStateForm(){}

  public Date getAuditDate() {
    return auditDate;
  }
  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }
  public String getAuditor() {
    return auditor;
  }
  public void setAuditor(String auditor) {
    this.auditor = auditor;
  }
  public String getTaskID() {
    return taskID;
  }
  public Integer getFlag() {
    return flag;
  }
  public void setFlag(Integer flag) {
    this.flag = flag;
  }
  public void setTaskID(String taskID) {
    this.taskID = taskID;
  }
  public Integer getTaskTimeID() {
    return taskTimeID;
  }
  public String getUnitID() {
    return unitID;
  }
  public void setUnitID(String unitID) {
    this.unitID = unitID;
  }
  public void setTaskTimeID(Integer taskTimeID) {
    this.taskTimeID = taskTimeID;
  }
}
