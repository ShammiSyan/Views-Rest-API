package com.syan.rest.core;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO or Entity Class for User. It holds the User data of a particular profile.
 * 
 * @author prsyan
 *
 */

@Entity()
@Table(name = "ProfileData")
@NamedQueries({ @NamedQuery(name = "com.syan.rest.core.ProfileData.findAll", query = "SELECT pd FROM ProfileData pd"), })
public class ProfileData {

	// =================================================
	// Data members
	// =================================================

	/** Unique Identifier of the UserProfile Data **/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	/** First Name of the User **/
	@Column(name = "firstName", nullable = false)
	private String firstName;

	/** Last Name of the User **/
	@Column(name = "lastName", nullable = true)
	private String lastName;

	/** Country of the User **/
	@Column(name = "country", nullable = false)
	private String country;

	/** Default Constructor **/
	public ProfileData() {
	}

	/** Parameterized Constructor **/
	public ProfileData(String firstName, String lastName, String country) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
	}

	/** Getters and Setters **/
	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	@JsonProperty
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty
	public String getLastName() {
		return lastName;
	}

	@JsonProperty
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty
	public String getCountry() {
		return country;
	}

	@JsonProperty
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof ProfileData)) {
			return false;
		}
		final ProfileData that = (ProfileData) object;

		return Objects.equals(this.profileId, that.profileId)
				&& Objects.equals(this.firstName, that.firstName)
				&& Objects.equals(this.lastName, that.lastName)
				&& Objects.equals(this.country, that.country);
	}

	@Override
	public int hashCode() {
		return Objects.hash(profileId, firstName, lastName, country);
	}
	

	/**
	 * Gets a displayable string representing the current state of the object.
	 * 
	 * @return the object state string.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ProfileId: " + getProfileId());
		buffer.append(" FirstName: " + firstName);
		buffer.append(" LastName: " + lastName);
		buffer.append(" Country: " + country);
		return buffer.toString();
	}
}
