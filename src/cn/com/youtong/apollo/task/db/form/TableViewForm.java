package cn.com.youtong.apollo.task.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TableViewForm implements Serializable {

    /** identifier field */
    private Integer viewID;

    /** persistent field */
    private Integer type;

    /** persistent field */
    private java.sql.Clob content;

    /** persistent field */
    private cn.com.youtong.apollo.task.db.form.TableForm table;

    /** full constructor */
    public TableViewForm(java.lang.Integer type, java.sql.Clob content, cn.com.youtong.apollo.task.db.form.TableForm table) {
        this.type = type;
        this.content = content;
        this.table = table;
    }

    /** default constructor */
    public TableViewForm() {
    }

    public java.lang.Integer getViewID() {
        return this.viewID;
    }

    public void setViewID(java.lang.Integer viewID) {
        this.viewID = viewID;
    }

    public java.lang.Integer getType() {
        return this.type;
    }

    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    public java.sql.Clob getContent() {
        return this.content;
    }

    public void setContent(java.sql.Clob content) {
        this.content = content;
    }

    public cn.com.youtong.apollo.task.db.form.TableForm getTable() {
        return this.table;
    }

    public void setTable(cn.com.youtong.apollo.task.db.form.TableForm table) {
        this.table = table;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("viewID", getViewID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TableViewForm) ) return false;
        TableViewForm castOther = (TableViewForm) other;
        return new EqualsBuilder()
            .append(this.getViewID(), castOther.getViewID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getViewID())
            .toHashCode();
    }

}
