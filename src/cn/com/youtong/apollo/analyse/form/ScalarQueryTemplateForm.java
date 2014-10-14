package cn.com.youtong.apollo.analyse.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ScalarQueryTemplateForm implements Serializable {

    /** identifier field */
    private Integer templateID;

    /** persistent field */
    private Integer userID;

    /** persistent field */
    private String taskID;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private java.sql.Clob content;

    /** full constructor */
    public ScalarQueryTemplateForm(java.lang.Integer templateID, String name, java.lang.Integer userID, java.lang.String taskID, java.sql.Clob content) {
        this.templateID = templateID;
        this.name = name;
        this.userID = userID;
        this.taskID = taskID;
        this.content = content;
    }

    /** default constructor */
    public ScalarQueryTemplateForm() {
    }

    /** minimal constructor */
    public ScalarQueryTemplateForm(java.lang.Integer templateID, String name, java.lang.Integer userID, java.lang.String taskID) {
        this.templateID = templateID;
        this.name = name;
        this.userID = userID;
        this.taskID = taskID;
    }

    public java.lang.Integer getTemplateID() {
        return this.templateID;
    }

    public void setTemplateID(java.lang.Integer templateID) {
        this.templateID = templateID;
    }

    public java.lang.Integer getUserID() {
        return this.userID;
    }

    public void setUserID(java.lang.Integer userID) {
        this.userID = userID;
    }

    public java.lang.String getTaskID() {
        return this.taskID;
    }

    public void setTaskID(java.lang.String taskID) {
        this.taskID = taskID;
    }

    public java.sql.Clob getContent() {
        return this.content;
    }

    public void setContent(java.sql.Clob content) {
        this.content = content;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("templateID", getTemplateID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ScalarQueryTemplateForm) ) return false;
        ScalarQueryTemplateForm castOther = (ScalarQueryTemplateForm) other;
        return new EqualsBuilder()
            .append(this.getTemplateID(), castOther.getTemplateID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTemplateID())
            .toHashCode();
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

}
