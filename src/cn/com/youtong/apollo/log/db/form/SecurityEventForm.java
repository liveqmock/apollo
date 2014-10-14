package cn.com.youtong.apollo.log.db.form;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import cn.com.youtong.apollo.log.Event;

/** @author Hibernate CodeGenerator */
public class SecurityEventForm implements Serializable, Event, Comparable {

    /** identifier field */
    private Integer eventID;

    /** persistent field */
    private java.util.Date timeOccured;

    /** persistent field */
    private int type;

    /** nullable persistent field */
    private String source;

    /** nullable persistent field */
    private String userName;

    /** nullable persistent field */
    private String memo;

    /** full constructor */
    public SecurityEventForm(java.util.Date timeOccured, int type, java.lang.String source, java.lang.String userName, java.lang.String memo) {
        this.timeOccured = timeOccured;
        this.type = type;
        this.source = source;
        this.userName = userName;
        this.memo = memo;
    }

    /** default constructor */
    public SecurityEventForm() {
    }

    /** minimal constructor */
    public SecurityEventForm(java.util.Date timeOccured, int type) {
        this.timeOccured = timeOccured;
        this.type = type;
    }

    public java.lang.Integer getEventID() {
        return this.eventID;
    }

    public void setEventID(java.lang.Integer eventID) {
        this.eventID = eventID;
    }

    public java.util.Date getTimeOccured() {
        return this.timeOccured;
    }

    public void setTimeOccured(java.util.Date timeOccured) {
        this.timeOccured = timeOccured;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public java.lang.String getSource() {
        return this.source;
    }

    public void setSource(java.lang.String source) {
        this.source = source;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getMemo() {
        return this.memo;
    }

    public void setMemo(java.lang.String memo) {
        this.memo = memo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("eventID", getEventID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SecurityEventForm) ) return false;
        SecurityEventForm castOther = (SecurityEventForm) other;
        return new EqualsBuilder()
            .append(this.getEventID(), castOther.getEventID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getEventID())
            .toHashCode();
    }

	public int compareTo(Object obj)
	{
		int result = 0;
		Event event = (Event)obj;

		//比较时间
		result = event.getTimeOccured().compareTo(this.timeOccured);

		//比较id
		if(result == 0)
		{
			result = event.getEventID().compareTo(this.eventID);
		}

		return result;

	}

}
