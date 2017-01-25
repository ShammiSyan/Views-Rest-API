package com.syan.rest.core;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the class that defines DTO for the User profile views.
 * 
 * @author prsyan
 * 
 */
@Entity
@Table(name = "ProfileView")
@NamedQueries({
		@NamedQuery(name = "com.syan.rest.core.ProfileView.findViewsOfUserProfile", query = "SELECT pv FROM ProfileView pv WHERE userProfileId = :userProfileId AND viewDate > :viewDate ORDER BY viewDate DESC "),
		@NamedQuery(name = "com.syan.rest.core.ProfileView.findAll", query = "SELECT pv FROM ProfileView pv"),
		@NamedQuery(name = "com.syan.rest.core.ProfileView.DeleteOldProfileViews", query = "DELETE FROM ProfileView pv WHERE viewDate < :viewDate ") })
public class ProfileView {

	// =================================================
	// Data members
	// =================================================

	/**
	 * The unique identifier for the UserProfileView record
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ObjectId;

	/**
	 * The Unique Id of the Owner's Profile
	 */
	@Column(name = "userProfileId", nullable = false)
	private Long userProfileId;

	/**
	 * The Unique Id of the Visitor's profile
	 */
	@Column(name = "viewerProfileId", nullable = false)
	private Long viewerProfileId;

	/**
	 * The Time stamp for recording time of visit. We are displaying the time in UTC.
	 */
	@Column(name = "viewDate", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "UTC")
	private Timestamp viewDate;

	public Long getObjectId() {
		return ObjectId;
	}

	public void setObjectId(Long objectId) {
		this.ObjectId = objectId;
	}

	@JsonProperty
	public Long getUserProfileId() {
		return userProfileId;
	}

	@JsonProperty
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	@JsonProperty
	public Long getViewerProfileId() {
		return viewerProfileId;
	}

	@JsonProperty
	public void setViewerProfileId(Long viewerProfileId) {
		this.viewerProfileId = viewerProfileId;
	}

	@JsonProperty
	public Timestamp getViewDate() {
		return viewDate;
	}

	@JsonProperty
	public void setViewDate(Timestamp viewDate) {
		this.viewDate = viewDate;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof ProfileView)) {
			return false;
		}
		final ProfileView that = (ProfileView) object;

		return Objects.equals(this.ObjectId, that.ObjectId)
				&& Objects.equals(this.userProfileId, that.userProfileId)
				&& Objects.equals(this.viewerProfileId, that.viewerProfileId)
				&& Objects.equals(this.viewDate, that.viewDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ObjectId, userProfileId, viewerProfileId, viewDate);
	}

	/**
	 * Gets a displayable string representing the current state of the object.
	 * 
	 * @return the object state string.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ObjectId: " + getObjectId());
		buffer.append(" UserProfileId: " + userProfileId);
		buffer.append(" ViewerProfileId: " + viewerProfileId);
		buffer.append(" ViewDate: " + viewDate);
		return buffer.toString();
	}

}
