package cn.com.youtong.apollo.usermanager.db.form;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class UserForm implements Serializable {

    /** identifier field */
    private Integer userID;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String password;

    /** persistent field */
    private String enterpriseName;

    /** persistent field */
    private String lawPersionCode;

    /** persistent field */
    private String lawPersionName;

    /** persistent field */
    private String lawPersionPhone;

    /** persistent field */
    private String contactPersionName;

    /** persistent field */
    private String contactPersionPhone;

    /** nullable persistent field */
    private String contactPersionMobile;

    /** persistent field */
    private String contactAddress;

    /** persistent field */
    private String postcode;

    /** nullable persistent field */
    private String email;

    /** nullable persistent field */
    private String fax;

    /** nullable persistent field */
    private java.util.Date dateCreated;

    /** nullable persistent field */
    private java.util.Date dateModified;

    /** nullable persistent field */
    private String memo;

    /** persistent field */
    private int flag;

    /** nullable persistent field */
    private String reserved1;

    /** nullable persistent field */
    private String reserved2;

    /** nullable persistent field */
    private String reserved3;

    /** persistent field */
    private cn.com.youtong.apollo.usermanager.db.form.RoleForm role;

    /** persistent field */
    private Set groups;

    /** full constructor */
    public UserForm(java.lang.Integer userID, java.lang.String name, java.lang.String password, java.lang.String enterpriseName, java.lang.String lawPersionCode, java.lang.String lawPersionName, java.lang.String lawPersionPhone, java.lang.String contactPersionName, java.lang.String contactPersionPhone, java.lang.String contactPersionMobile, java.lang.String contactAddress, java.lang.String postcode, java.lang.String email, java.lang.String fax, java.util.Date dateCreated, java.util.Date dateModified, java.lang.String memo, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3, cn.com.youtong.apollo.usermanager.db.form.RoleForm role, Set groups) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.enterpriseName = enterpriseName;
        this.lawPersionCode = lawPersionCode;
        this.lawPersionName = lawPersionName;
        this.lawPersionPhone = lawPersionPhone;
        this.contactPersionName = contactPersionName;
        this.contactPersionPhone = contactPersionPhone;
        this.contactPersionMobile = contactPersionMobile;
        this.contactAddress = contactAddress;
        this.postcode = postcode;
        this.email = email;
        this.fax = fax;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.memo = memo;
        this.flag = flag;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.role = role;
        this.groups = groups;
    }

    /** default constructor */
    public UserForm() {
    }

    /** minimal constructor */
    public UserForm(java.lang.Integer userID, java.lang.String name, java.lang.String enterpriseName, java.lang.String lawPersionCode, java.lang.String lawPersionName, java.lang.String lawPersionPhone, java.lang.String contactPersionName, java.lang.String contactPersionPhone, java.lang.String contactAddress, java.lang.String postcode, int flag, cn.com.youtong.apollo.usermanager.db.form.RoleForm role, Set groups) {
        this.userID = userID;
        this.name = name;
        this.enterpriseName = enterpriseName;
        this.lawPersionCode = lawPersionCode;
        this.lawPersionName = lawPersionName;
        this.lawPersionPhone = lawPersionPhone;
        this.contactPersionName = contactPersionName;
        this.contactPersionPhone = contactPersionPhone;
        this.contactAddress = contactAddress;
        this.postcode = postcode;
        this.flag = flag;
        this.role = role;
        this.groups = groups;
    }

    public java.lang.Integer getUserID() {
        return this.userID;
    }

    public void setUserID(java.lang.Integer userID) {
        this.userID = userID;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getPassword() {
        return this.password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getEnterpriseName() {
        return this.enterpriseName;
    }

    public void setEnterpriseName(java.lang.String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public java.lang.String getLawPersionCode() {
        return this.lawPersionCode;
    }

    public void setLawPersionCode(java.lang.String lawPersionCode) {
        this.lawPersionCode = lawPersionCode;
    }

    public java.lang.String getLawPersionName() {
        return this.lawPersionName;
    }

    public void setLawPersionName(java.lang.String lawPersionName) {
        this.lawPersionName = lawPersionName;
    }

    public java.lang.String getLawPersionPhone() {
        return this.lawPersionPhone;
    }

    public void setLawPersionPhone(java.lang.String lawPersionPhone) {
        this.lawPersionPhone = lawPersionPhone;
    }

    public java.lang.String getContactPersionName() {
        return this.contactPersionName;
    }

    public void setContactPersionName(java.lang.String contactPersionName) {
        this.contactPersionName = contactPersionName;
    }

    public java.lang.String getContactPersionPhone() {
        return this.contactPersionPhone;
    }

    public void setContactPersionPhone(java.lang.String contactPersionPhone) {
        this.contactPersionPhone = contactPersionPhone;
    }

    public java.lang.String getContactPersionMobile() {
        return this.contactPersionMobile;
    }

    public void setContactPersionMobile(java.lang.String contactPersionMobile) {
        this.contactPersionMobile = contactPersionMobile;
    }

    public java.lang.String getContactAddress() {
        return this.contactAddress;
    }

    public void setContactAddress(java.lang.String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public java.lang.String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(java.lang.String postcode) {
        this.postcode = postcode;
    }

    public java.lang.String getEmail() {
        return this.email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getFax() {
        return this.fax;
    }

    public void setFax(java.lang.String fax) {
        this.fax = fax;
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

    public java.lang.String getMemo() {
        return this.memo;
    }

    public void setMemo(java.lang.String memo) {
        this.memo = memo;
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

    public cn.com.youtong.apollo.usermanager.db.form.RoleForm getRole() {
        return this.role;
    }

    public void setRole(cn.com.youtong.apollo.usermanager.db.form.RoleForm role) {
        this.role = role;
    }

    public java.util.Set getGroups() {
        return this.groups;
    }

    public void setGroups(java.util.Set groups) {
        this.groups = groups;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userID", getUserID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof UserForm) ) return false;
        UserForm castOther = (UserForm) other;
        return new EqualsBuilder()
            .append(this.getUserID(), castOther.getUserID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserID())
            .toHashCode();
    }

}
