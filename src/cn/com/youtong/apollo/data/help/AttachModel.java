package cn.com.youtong.apollo.data.help;

import java.io.Serializable;

public class AttachModel
    implements Serializable {

  private Integer ID;
  private String unitID;
  private String tableName;
  private String taskTimeID;
  private String fileName;
  private String fileSize;
  private String fileType;
  private String attachType;
  private String attachTypeID;
  private String content;

  public String getContent() {
    return content;
  }

  public String getAttachType() {
    return attachType;
  }

  public void setAttachType(String attachType) {
    this.attachType = attachType;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFileSize() {
    return fileSize;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileType() {
    return fileType;
  }

  public Integer getID() {
    return ID;
  }

  public void setID(Integer ID) {
    this.ID = ID;
  }

  public String getTaskTimeID() {
    return taskTimeID;
  }

  public void setTaskTimeID(String taskTimeID) {
    this.taskTimeID = taskTimeID;
  }

  public String getUnitID() {
    return unitID;
  }

  public void setUnitID(String unitID) {
    this.unitID = unitID;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

}
