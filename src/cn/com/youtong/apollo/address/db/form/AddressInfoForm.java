package cn.com.youtong.apollo.address.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AddressInfoForm implements Serializable {

    /** identifier field */
    private cn.com.youtong.apollo.address.db.form.AddressInfoFormPK comp_id;

    /** nullable persistent field */
    private String email;

    /** nullable persistent field */
    private String mobile;

    /** nullable persistent field */
    private String phone;

    /** nullable persistent field */
    private String fax;

    /** persistent field */
    private int flag;

    /** nullable persistent field */
    private String reserved1;

    /** nullable persistent field */
    private String reserved2;

    /** nullable persistent field */
    private String reserved3;

    /** full constructor */
    public AddressInfoForm(cn.com.youtong.apollo.address.db.form.AddressInfoFormPK comp_id, java.lang.String email, java.lang.String mobile, java.lang.String phone, java.lang.String fax, int flag, java.lang.String reserved1, java.lang.String reserved2, java.lang.String reserved3) {
        this.comp_id = comp_id;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.fax = fax;
        this.flag = flag;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
    }

    /** default constructor */
    public AddressInfoForm() {
    }

    /** minimal constructor */
    public AddressInfoForm(cn.com.youtong.apollo.address.db.form.AddressInfoFormPK comp_id, int flag) {
        this.comp_id = comp_id;
        this.flag = flag;
    }

    public cn.com.youtong.apollo.address.db.form.AddressInfoFormPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(cn.com.youtong.apollo.address.db.form.AddressInfoFormPK comp_id) {
        this.comp_id = comp_id;
    }

    public java.lang.String getEmail() {
        return this.email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getMobile() {
        return this.mobile;
    }

    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }

    public java.lang.String getPhone() {
        return this.phone;
    }

    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getFax() {
        return this.fax;
    }

    public void setFax(java.lang.String fax) {
        this.fax = fax;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AddressInfoForm) ) return false;
        AddressInfoForm castOther = (AddressInfoForm) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
