package com.syan.rest.dao;

import java.util.List;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.syan.rest.core.ProfileData;

public class ProfileDataDAO extends AbstractDAO<ProfileData> {
	
	/** Reference for the logger object. **/
	private static final Logger log = LoggerFactory
			.getLogger(ProfileDataDAO.class);

	public ProfileDataDAO(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Find the Reference for an Id .
	 * 
	 * @param objectId
	 * @return if not null then returns optional instance containing reference
	 *         otherwise returns absent()
	 */
	public Optional<ProfileData> findById(Long objectId) {
		return Optional.fromNullable(get(objectId));
	}

	/**
	 * Creates a new User Profile along with User Data.
	 * 
	 * @return created user.
	 **/
	public ProfileData create(ProfileData profileData) {
		log.info("Persisting New User.");
		return persist(profileData);
	}

	/**
	 * Lists all the existing User Profiles.
	 * 
	 * @return list of existing users.
	 */
	public List<ProfileData> findAll() {
		log.info("Calling the named query to find all existing users.");
		return list(namedQuery("com.syan.rest.core.ProfileData.findAll"));
	}

}
