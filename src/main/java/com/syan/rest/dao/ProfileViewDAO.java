package com.syan.rest.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syan.rest.core.ProfileView;
import com.syan.rest.util.ProfileConst;

import io.dropwizard.hibernate.AbstractDAO;

public class ProfileViewDAO extends AbstractDAO<ProfileView> {

	/** Reference for the logger object. **/
	private static final Logger log = LoggerFactory
			.getLogger(ProfileViewDAO.class);

	public ProfileViewDAO(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Creates a User Profile View.
	 * 
	 * @return created Profile View.
	 **/
	public ProfileView create(ProfileView userProfileView) {
		log.info("Persisting a User Profile Visit");
		return persist(userProfileView);
	}

	/**
	 * Records the data when a User visits another user's profile.
	 * 
	 * @param userProfileId
	 * @param viewerProfileId
	 * @return profileView.
	 */
	public ProfileView recordProfileView(long userProfileId,
			long viewerProfileId) {
		ProfileView profileView = new ProfileView();
		profileView.setUserProfileId(userProfileId);
		profileView.setViewerProfileId(viewerProfileId);
		log.info(Timestamp.valueOf((LocalDateTime.now())).toString());
		profileView.setViewDate(Timestamp.valueOf((LocalDateTime.now())));
		return create(profileView);
	}

	/**
	 * Lists the 10 latest Profile views within last 10 days for a user.
	 * 
	 * @param userProfileId
	 * @return Lists of latest 10 profile views.
	 */
	public List<ProfileView> findProfileView(long userProfileId) {
		Timestamp dayLimit = Timestamp.valueOf(LocalDateTime.now().minusDays(
				ProfileConst.DEFUALT_MAX_DAYS_LIMIT));
		log.info(" Current Time :" + dayLimit);
		log.info(" Returning Last 10 views of User : " + userProfileId);
		return list(namedQuery(
				"com.syan.rest.core.ProfileView.findViewsOfUserProfile")
				.setParameter("userProfileId", userProfileId)
				.setParameter("viewDate", dayLimit)
				.setMaxResults(ProfileConst.DEFAULT_MAX_VIEWS_LIMIT));

	}

	/**
	 * Deletes the Profiles Views Older than 10 days from current Time stamp.
	 * 
	 * @param dayLimit
	 */
	public void deleteOldProfileView(Timestamp dayLimit) {
		log.info("Deleting the Profile Views older than 10 days");
		namedQuery(
				"com.syan.rest.core.ProfileView.DeleteOldProfileViews")
				.setParameter("viewDate", dayLimit).executeUpdate();
	}

	/**
	 * List all the existing User Profile Views.
	 * 
	 * @return list of all Profile Views.
	 */
	public List<ProfileView> findAll() {
		log.info("Calling the named query to find all existing user views.");
		return list(namedQuery("com.syan.rest.core.ProfileView.findAll"));
	}

}
