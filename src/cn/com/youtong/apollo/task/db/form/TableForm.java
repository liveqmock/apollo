package cn.com.youtong.apollo.task.db.form;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TableForm implements Serializable {

    /** identifier field */
    private Integer tableID;

    /** persistent field */
    private String name;

    /** persistent field */
    private int flag;

    /** persistent field */
    private String ID;

    /** nullable persistent field */
    private String reserved1;

    /** nullable persistent field */
    private String reserved2;

    /** nullable persistent field */
    private String reserved3;

    /** persistent field */
    private cn.com.youtong.apollo.task.db.form.TaskForm task;

    /** persistent field */
    private Set rows;

    /** persistent field */
    private Set tableViews;

    /** full constructor */
    public TableForm(java.lang.Integer tableID, java.lang.String name, int flag, java.lang.String ID, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, cn.com.youtong.apollo.task.db.form.TaskForm task, Set rows, Set tableViews) {
        this.tableID = tableID;
        this.name = name;
        this.flag = flag;
        this.ID = ID;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.task = task;
        this.rows = rows;
        this.tableViews = tableViews;
    }

    /** default constructor */
    public TableForm() {
    }

    /** minimal constructor */
    public TableForm(java.lang.Integer tableID, java.lang.String name, int flag, java.lang.String ID, cn.com.youtong.apollo.task.db.form.TaskForm task, Set rows, Set tableViews) {
        this.tableID = tableID;
        this.name = name;
        this.flag = flag;
        this.ID = ID;
        this.task = task;
        this.rows = rows;
        this.tableViews = tableViews;
    }

    public java.lang.Integer getTableID() {
        return this.tableID;
    }

    public void setTableID(java.lang.Integer tableID) {
        this.tableID = tableID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public java.lang.String getID() {
        return this.ID;
    }

    public void setID(java.lang.String ID) {
        this.ID = ID;
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

    public cn.com.youtong.apollo.task.db.form.TaskForm getTask() {
        return this.task;
    }

    public void setTask(cn.com.youtong.apollo.task.db.form.TaskForm task) {
        this.task = task;
    }

    public java.util.Set getRows() {
        return this.rows;
    }

    public void setRows(java.util.Set rows) {
        this.rows = rows;
    }

    public java.util.Set getTableViews() {
        return this.tableViews;
    }

    public void setTableViews(java.util.Set tableViews) {
        this.tableViews = tableViews;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tableID", getTableID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TableForm) ) return false;
        TableForm castOther = (TableForm) other;
        return new EqualsBuilder()
            .append(this.getTableID(), castOther.getTableID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTableID())
            .toHashCode();
    }

}
