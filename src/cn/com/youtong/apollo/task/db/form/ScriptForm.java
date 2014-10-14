package cn.com.youtong.apollo.task.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ScriptForm implements Serializable {

    /** identifier field */
    private Integer scriptID;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private Integer suitID;

    /** persistent field */
    private int type;

    /** persistent field */
    private java.sql.Clob content;

    /** full constructor */
    public ScriptForm(java.lang.Integer scriptID, java.lang.String name, java.lang.Integer suitID, int type, java.sql.Clob content) {
        this.scriptID = scriptID;
        this.name = name;
        this.suitID = suitID;
        this.type = type;
        this.content = content;
    }

    /** default constructor */
    public ScriptForm() {
    }

    /** minimal constructor */
    public ScriptForm(java.lang.Integer scriptID, int type, java.sql.Clob content) {
        this.scriptID = scriptID;
        this.type = type;
        this.content = content;
    }

    public java.lang.Integer getScriptID() {
        return this.scriptID;
    }

    public void setScriptID(java.lang.Integer scriptID) {
        this.scriptID = scriptID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.Integer getSuitID() {
        return this.suitID;
    }

    public void setSuitID(java.lang.Integer suitID) {
        this.suitID = suitID;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public java.sql.Clob getContent() {
        return this.content;
    }

    public void setContent(java.sql.Clob content) {
        this.content = content;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("scriptID", getScriptID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ScriptForm) ) return false;
        ScriptForm castOther = (ScriptForm) other;
        return new EqualsBuilder()
            .append(this.getScriptID(), castOther.getScriptID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getScriptID())
            .toHashCode();
    }

}
