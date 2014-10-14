package cn.com.youtong.apollo.address.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AddressInfoFormPK implements Serializable {

    /** identifier field */
    private String taskID;

    /** identifier field */
    private String unitID;

    /** full constructor */
    public AddressInfoFormPK(java.lang.String taskID, java.lang.String unitID) {
        this.taskID = taskID;
        this.unitID = unitID;
    }

    /** default constructor */
    public AddressInfoFormPK() {
    }

    public java.lang.String getTaskID() {
        return this.taskID;
    }

    public void setTaskID(java.lang.String taskID) {
        this.taskID = taskID;
    }

    public java.lang.String getUnitID() {
        return this.unitID;
    }

    public void setUnitID(java.lang.String unitID) {
        this.unitID = unitID;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("taskID", getTaskID())
            .append("unitID", getUnitID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AddressInfoFormPK) ) return false;
        AddressInfoFormPK castOther = (AddressInfoFormPK) other;
        return new EqualsBuilder()
            .append(this.getTaskID(), castOther.getTaskID())
            .append(this.getUnitID(), castOther.getUnitID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTaskID())
            .append(getUnitID())
            .toHashCode();
    }

}
