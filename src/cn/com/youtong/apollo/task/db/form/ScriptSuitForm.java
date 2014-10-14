package cn.com.youtong.apollo.task.db.form;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ScriptSuitForm implements Serializable {

    /** identifier field */
    private Integer suitID;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private Integer taskID;

    /** nullable persistent field */
    private String memo;

    /** nullable persistent field */
    private java.util.Date dateCreated;

    /** nullable persistent field */
    private java.util.Date dateModified;

    /** nullable persistent field */
    private String execSeqence;

    /** persistent field */
    private Set scripts;

    /** full constructor */
    public ScriptSuitForm(java.lang.Integer suitID, java.lang.String name, java.lang.Integer taskID, java.lang.String memo, java.util.Date dateCreated, java.util.Date dateModified, java.lang.String execSeqence, Set scripts) {
        this.suitID = suitID;
        this.name = name;
        this.taskID = taskID;
        this.memo = memo;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.execSeqence = execSeqence;
        this.scripts = scripts;
    }

    /** default constructor */
    public ScriptSuitForm() {
    }

    /** minimal constructor */
    public ScriptSuitForm(java.lang.Integer suitID, Set scripts) {
        this.suitID = suitID;
        this.scripts = scripts;
    }

    public java.lang.Integer getSuitID() {
        return this.suitID;
    }

    public void setSuitID(java.lang.Integer suitID) {
        this.suitID = suitID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.Integer getTaskID() {
        return this.taskID;
    }

    public void setTaskID(java.lang.Integer taskID) {
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

    public java.lang.String getExecSeqence() {
        return this.execSeqence;
    }

    public void setExecSeqence(java.lang.String execSeqence) {
        this.execSeqence = execSeqence;
    }

    public java.util.Set getScripts() {
        return this.scripts;
    }

    public void setScripts(java.util.Set scripts) {
        this.scripts = scripts;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("suitID", getSuitID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ScriptSuitForm) ) return false;
        ScriptSuitForm castOther = (ScriptSuitForm) other;
        return new EqualsBuilder()
            .append(this.getSuitID(), castOther.getSuitID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSuitID())
            .toHashCode();
    }

}
