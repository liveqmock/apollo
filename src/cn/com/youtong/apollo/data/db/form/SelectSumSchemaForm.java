package cn.com.youtong.apollo.data.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SelectSumSchemaForm implements Serializable {

    /** identifier field */
    private Integer schemaID;

    /** persistent field */
    private String name;

    /** persistent field */
    private String taskID;

    /** nullable persistent field */
    private String memo;

    /** nullable persistent field */
    private java.util.Date dateCreated;

    /** nullable persistent field */
    private java.util.Date dateModified;

    /** nullable persistent field */
    private java.sql.Clob content;

    /** full constructor */
    public SelectSumSchemaForm(java.lang.Integer schemaID, java.lang.String name, java.lang.String taskID, java.lang.String memo, java.util.Date dateCreated, java.util.Date dateModified, java.sql.Clob content) {
        this.schemaID = schemaID;
        this.name = name;
        this.taskID = taskID;
        this.memo = memo;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.content = content;
    }

    /** default constructor */
    public SelectSumSchemaForm() {
    }

    /** minimal constructor */
    public SelectSumSchemaForm(java.lang.Integer schemaID, java.lang.String name, java.lang.String taskID) {
        this.schemaID = schemaID;
        this.name = name;
        this.taskID = taskID;
    }

    public java.lang.Integer getSchemaID() {
        return this.schemaID;
    }

    public void setSchemaID(java.lang.Integer schemaID) {
        this.schemaID = schemaID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getTaskID() {
        return this.taskID;
    }

    public void setTaskID(java.lang.String taskID) {
        this.taskID = taskID;
    }

    public java.lang.String getMemo() {
        return this.memo;
    }

    public void setMemo(java.lang.String memo) {
        this.memo = memo;
    }

    public java.util.Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public java.util.Date getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(java.util.Date dateModified) {
        this.dateModified = dateModified;
    }

    public java.sql.Clob getContent() {
        return this.content;
    }

    public void setContent(java.sql.Clob content) {
        this.content = content;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("schemaID", getSchemaID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SelectSumSchemaForm) ) return false;
        SelectSumSchemaForm castOther = (SelectSumSchemaForm) other;
        return new EqualsBuilder()
            .append(this.getSchemaID(), castOther.getSchemaID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSchemaID())
            .toHashCode();
    }

}
