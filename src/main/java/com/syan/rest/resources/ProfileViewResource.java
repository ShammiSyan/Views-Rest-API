package com.syan.rest.resources;

import java.util.ArrayList;
import java.util.List;


import static com.syan.rest.util.ProfileConst.*;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.syan.rest.dao.ProfileDataDAO;
import com.syan.rest.dao.ProfileViewDAO;
import com.syan.rest.util.ProfileConst;
import com.syan.rest.core.ProfileData;
import com.syan.rest.core.ProfileView;

/**
 * User Profile Views resource class. It provides the information about creating
 * a new User Profile. It logs the views of a User profile by another user. It
 * also list the profile views of a User.
 * 
 * @author prsyan
 *
 */

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileViewResource extends BaseResource {

	/** Reference for the logger object. **/
	private static final Logger log = LoggerFactory
			.getLogger(ProfileViewResource.class);

	private final ProfileDataDAO profileDataDAO;
	private final ProfileViewDAO profileViewDAO;

	/** Parameterized Constructor **/
	public ProfileViewResource(ProfileDataDAO profileDataDAO,
			ProfileViewDAO profileViewDAO) {
		this.profileDataDAO = profileDataDAO;
		this.profileViewDAO = profileViewDAO;
	}

	/**
	 * Creates a Profile with User Data
	 * 
	 * @param Reference
	 *            Data of User Profile to be created
	 * @return Created User Profile
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response createProfileJson(ProfileData profileData) {
		Response response = createUser(profileData);
		return response;
	}

	/**
	 * Returns a List of existing User Profiles from UserData
	 * 
	 * @return
	 */
	@GET
	@UnitOfWork
	public Response getAllUserJson() {
		logStart("getAllUsersJson", "");
        Response response = null;
        List<ProfileData> profileData = getAllUser();
        if (profileData != null)
        {
            response = Response.ok(profileData).build();
            logDone("getAllUsersJson", "Profiles Found=" + profileData.size());
        }
        else
        {
            response = setResponseHeader(Response.status(Status.NO_CONTENT), 
                    ERROR_HDR, MSG_INTERNAL_ERROR);
            logDone("getAllUsersJson", "Failed");
        }
              
        return response;
	}

	/**
	 * Returns the requested Profile from the list existing User Profiles.
	 * 
	 * @param Id
	 *            of the existing User Profile
	 * 
	 * @return Profile Data
	 */
	@GET
	@Path("/{userProfileId}")
	@UnitOfWork
	public Response getUserJson(
			@PathParam("userProfileId") LongParam userProfileId) {
        logStart("getUserJson", "userProfileId=" + userProfileId);
        Response response = null;
        try{
		Long userId = userProfileId.get();
        if (userId == null)
        {
            log.warn("Get User: insufficient data - UserId is null.");  
            response = setResponseHeader(Response.status(Status.BAD_REQUEST), 
                    ERROR_HDR, MSG_BAD_ARG_NO_ID); 
            logDone("getUserJson", "UserId is null.");
            return response;
        }
        else{
		log.info("Getting the requested User");
		response = findSafely(userId);
        }
        }
        catch (Exception e) {
			log.error("Getting User Profile failed : " + e);
			response = setResponseHeader(Response.status(Status.NO_CONTENT),
					ProfileConst.ERROR_HDR, ProfileConst.MSG_INTERNAL_ERROR);
		}
        logDone("getUserJson", "Success");
		return response;
	}

	/**
	 * Logs the view when a User Visits another Profile
	 * 
	 * @param userProfileId
	 *            : Id of the Profile Owner
	 * @param viewerProfileId
	 *            : Id of the Profile Visitor
	 * @return Data of User Profile
	 */
	@GET
	@Path("/{viewerProfileId}/views/{userProfileId}")
	@Produces(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response logProfileView(
			@PathParam("userProfileId") LongParam userProfileId,
			@PathParam("viewerProfileId") LongParam viewerProfileId) {
		logStart("logProfileView", "userProfileId=" + userProfileId);
        Response response = null;
        try{
		Long userId = userProfileId.get();
		Long viewerId = viewerProfileId.get();
		if (userId == null) 
		{
			log.warn("log view: insufficient data - UserId is null");  
            logDone("logProfileView", "Failed");
            response = setResponseHeader(Response.status(Status.BAD_REQUEST), 
                    ERROR_HDR, MSG_BAD_ARG_NO_ID); 
            return response;
		} 
		if (viewerId == null) 
		{
			log.warn("log view: insufficient data - ViewerId is null");  
            logDone("logProfileView", "Failed.");
            response = setResponseHeader(Response.status(Status.BAD_REQUEST), 
                    ERROR_HDR, MSG_BAD_ARG_NO_ID); 
            return response;
		}
		if (userId == viewerId) {
			log.warn("log view: insufficient data - ViewerId and UserId cannot be Same");  
            logDone("logProfileView", "Failed");
            response = setResponseHeader(Response.status(Status.BAD_REQUEST), 
                    ERROR_HDR, MSG_BAD_ARG_SAME_ID); 
            return response;
		}
		response = findSafely(userId);
		if(response.getStatus() == Status.BAD_REQUEST.getStatusCode())
        {
			logDone("logProfileView", "Failed.");
			return response;
        }
		response = findSafely(viewerId);
		if(response.getStatus() == Status.BAD_REQUEST.getStatusCode())
        {
			logDone("logProfileView", "Failed.");
			return response;
        }
	    ProfileView profileView = profileViewDAO.recordProfileView(userId,viewerId);
			log.info("UserProfileViewResource : User "
					+ viewerId + " has visited User "
					+ userId + "'s Profile");
            response =  Response.ok(profileView).build();
        }
        catch (Exception ex)
        {
            log.error("Failed to record the Profile Views for : Userid="  + " - " + userProfileId +
                    ex);
            response = setResponseHeader(Response.status(Status.NO_CONTENT), 
                    ERROR_HDR, MSG_INTERNAL_ERROR);
        }

        logDone("logProfileView", "Success");
        return response;
	}
	
	/**
	 * Returns the List of latest 10 views within last 10 days for a User
	 * Profile
	 * 
	 * @param userProfileId
	 * @return List of latest views
	 */
	@GET
    @Path("/views/{userProfileId}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getProfileView(@PathParam("userProfileId") LongParam userProfileId)
    {
        logStart("getProfileView", "userProfileId=" + userProfileId);
        Long userId = userProfileId.get();
        Response response = null;
        if (userId == null)
        {
            log.warn("Get Profile View: insufficient data - User Profile Id is null");  
            logDone("getProfileView", "Profile Id is null.");
            response = setResponseHeader(Response.status(Status.BAD_REQUEST), 
                    ERROR_HDR, MSG_BAD_ARG_NO_ID); 
            return response;
        }
        response = findSafely(userId);
        if(response.getStatus() == Status.BAD_REQUEST.getStatusCode())
        {  
            logDone("getProfileView", "Profile Doesn't Exist.");
        	return response;
        }
        try
        {
            List<ProfileView> pvList = profileViewDAO.findProfileView(userId);
            response =  Response.ok(pvList).build();
        }
        catch (Exception ex)
        {
            log.error("Failed to get the profile views for : id=" + userId + " - " +
                    ex);
            response = setResponseHeader(Response.status(Status.NO_CONTENT), 
                    ERROR_HDR, MSG_INTERNAL_ERROR);
        }

        logDone("getProfileView", "Success");
        return response;
    }

	/**
	 * Returns a List of All existing User Profile Views.
	 * 
	 * @return Existing User Profile Views.
	 */
	@GET
	@Path("/views")
	@UnitOfWork
	public Response getAllViewsJson() {
		logStart("getAllViewsJson", "Getting All Users Views");
        Response response = null;
        List<ProfileView> profileView = getAllViews();
        if (profileView != null)
        {
            response = Response.ok(profileView).build();
            logDone("getAllViewsJson", "Number=" + profileView.size());
        }
        else
        {
            response = setResponseHeader(Response.status(Status.NO_CONTENT), 
                    ERROR_HDR, MSG_INTERNAL_ERROR);
            logDone("getAllViewsJson", "Failed");
        }
              
        return response;
		
	}

	// =========================================================================
	// Private Methods
	// =========================================================================

	/**
	 * 
	 * @param profileId
	 * @return
	 */
	private Response findSafely(Long profileId) {
		Response response = null;
		final Optional<ProfileData> profile = profileDataDAO
				.findById(profileId);
		log.info("Checking if the requested User Profile exists.");
		if (!profile.isPresent()) {
			log.error("Bad Request:User Pr"
					+ "ofile does not exist.");
			response = setResponseHeader(
					Response.status(Status.BAD_REQUEST),
					ProfileConst.ERROR_HDR,
					ProfileConst.MSG_RES_NOT_FOUND);
			return response;
		}
		log.info("Requested User Profile exists.");
		response = Response.ok(profile.get()).build();
		return response;
	}
	
	/**
	 * 
	 * @param profileData
	 * @return
	 */

	private Response createUser(ProfileData profileData) {
		Response response = null;
		try {
			String firstName = profileData.getFirstName();
			if (firstName == null || firstName.isEmpty()) {
				log.error(" First Name not found.");
				response = setResponseHeader(
						Response.status(Status.BAD_REQUEST),
						ProfileConst.ERROR_HDR,
						ProfileConst.MSG_BAD_ARG_NO_FIRST_NAME);
				return response;
			}
			log.info("Calling the Data Access Layer to create user");
			profileData = profileDataDAO.create(profileData);
			response = Response.ok(profileData).build();
		} catch (Exception ex) {
			log.error("User creation failed : " + ex);
			response = setResponseHeader(Response.status(Status.NO_CONTENT),
					ProfileConst.ERROR_HDR, ProfileConst.MSG_INTERNAL_ERROR);
		}
		return response;
	}
	
    /**
     * Gets all the existing User Profiles.
     * 
     * @return existing user Profiles.
     * 
     */
    private List<ProfileData> getAllUser()
    {
        List<ProfileData> profileData= new ArrayList<ProfileData>();
        try
        {
            log.debug("Getting Existing User Profiles");
            profileData  = profileDataDAO.findAll();;         
            log.info("Found User Profiles- " + profileData.size());
        }
        catch (Exception ex)
        {
            log.error("Failed to get User Profiles." + ex);
        }       
        return profileData;
    }
    
    /**
     * Gets all the Profile Views.
     * 
     * @return profileViews.
     */
    
    private List<ProfileView> getAllViews()
    {
        List<ProfileView> profileView= new ArrayList<ProfileView>();
        try
        {
            log.debug("Getting Existing Profile Views");
            profileView  = profileViewDAO.findAll();         
            log.info("Found Profile Views- " + profileView.size());
        }
        catch (Exception ex)
        {
            log.error("Failed to get profile views." + ex);
        }       
        return profileView;
    }
    
    
}
