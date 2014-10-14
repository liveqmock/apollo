package cn.com.youtong.apollo.task.db.form;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TaskForm implements Serializable {

    /** identifier field */
    private Integer taskID;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private java.util.Date dateCreated;

    /** nullable persistent field */
    private java.util.Date dateModified;

    /** persistent field */
    private int version;

    /** nullable persistent field */
    private String memo;

    /** nullable persistent field */
    private String activeScriptSuitName;

    /** persistent field */
    private String ID;

    /** persistent field */
    private int flag;

    /** nullable persistent field */
    private String reserved1;

    /** nullable persistent field */
    private String reserved2;

    /** nullable persistent field */
    private String reserved3;

    /** nullable persistent field */
    private cn.com.youtong.apollo.task.db.form.UnitMetaForm unitMeta;

    /** persistent field */
    private Set taskTimes;

    /** persistent field */
    private Set tables;

    /** persistent field */
    private Set scriptSuits;

    /** full constructor */
    public TaskForm(java.lang.Integer taskID, java.lang.String name, java.util.Date dateCreated, java.util.Date dateModified, int version, java.lang.String memo, java.lang.String ID, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, cn.com.youtong.apollo.task.db.form.UnitMetaForm unitMeta, Set taskTimes, Set tables, Set scriptSuits) {
        this.taskID = taskID;
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.version = version;
        this.memo = memo;
        this.ID = ID;
        this.flag = flag;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.unitMeta = unitMeta;
        this.taskTimes = taskTimes;
        this.tables = tables;
        this.scriptSuits = scriptSuits;
    }

    /** default constructor */
    public TaskForm() {
    }

    /** minimal constructor */
    public TaskForm(java.lang.Integer taskID, java.lang.String name, int version, java.lang.String ID, int flag, Set taskTimes, Set tables, Set scriptSuits) {
        this.taskID = taskID;
        this.name = name;
        this.version = version;
        this.ID = ID;
        this.flag = flag;
        this.taskTimes = taskTimes;
        this.tables = tables;
        this.scriptSuits = scriptSuits;
    }

    public java.lang.Integer getTaskID() {
        return this.taskID;
    }

    public void setTaskID(java.lang.Integer taskID) {
        this.taskID = taskID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
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

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public java.lang.String getMemo() {
        return this.memo;
    }

    public void setMemo(java.lang.String memo) {
        this.memo = memo;
    }

    public java.lang.String getID() {
        return this.ID;
    }

    public void setID(java.lang.String ID) {
        this.ID = ID;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public java.lang.String getReserved1() {
        return this.reserved1;
    }

    public void setReserved1(java.lang.String reserved1) {
        this.reserved1 = reserved1;
    }

    public java.lang.String getReserved2() {
        return this.reserved2;
    }

    public void setReserved2(java.lang.String reserved2) {
        this.reserved2 = reserved2;
    }

    public java.lang.String getReserved3() {
        return this.reserved3;
    }

    public void setReserved3(java.lang.String reserved3) {
        this.reserved3 = reserved3;
    }

    public cn.com.youtong.apollo.task.db.form.UnitMetaForm getUnitMeta() {
        return this.unitMeta;
    }

    public void setUnitMeta(cn.com.youtong.apollo.task.db.form.UnitMetaForm unitMeta) {
        this.unitMeta = unitMeta;
    }

    public java.util.Set getTaskTimes() {
        return this.taskTimes;
    }

    public void setTaskTimes(java.util.Set taskTimes) {
        this.taskTimes = taskTimes;
    }

    public java.util.Set getTables() {
        return this.tables;
    }

    public void setTables(java.util.Set tables) {
        this.tables = tables;
    }

    public java.util.Set getScriptSuits() {
        return this.scriptSuits;
    }

    public void setScriptSuits(java.util.Set scriptSuits) {
        this.scriptSuits = scriptSuits;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("taskID", getTaskID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TaskForm) ) return false;
        TaskForm castOther = (TaskForm) other;
        return new EqualsBuilder()
            .append(this.getTaskID(), castOther.getTaskID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTaskID())
            .toHashCode();
    }
    public String getActiveScriptSuitName()
    {
        return activeScriptSuitName;
    }
    public void setActiveScriptSuitName(String activeScriptSuitName)
    {
        this.activeScriptSuitName = activeScriptSuitName;
    }

}
