package com.syan.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.syan.rest.dao.ProfileDataDAO;
import com.syan.rest.dao.ProfileViewDAO;
import com.syan.rest.core.ProfileData;
import com.syan.rest.core.ProfileView;
import com.syan.rest.resources.ProfileViewResource;

import junit.framework.TestCase;

/**
 * Unit test for ResourceClass.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileViewResourceTest 
    extends TestCase
{
	
	private static final ProfileDataDAO USER_PROFILE_DATA_DAO = mock(ProfileDataDAO.class);
    private static final ProfileViewDAO USER_PROFILE_VIEW_DAO = mock(ProfileViewDAO.class);
    
    
    @Captor
    private ArgumentCaptor<ProfileData> userProfileCaptor;
	private ProfileData userProfile1;
	private ProfileData userProfile2;
	private ProfileView userView;
	
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ProfileViewResource(USER_PROFILE_DATA_DAO, USER_PROFILE_VIEW_DAO))
            .build();
	
	@Before
	public void setUp() {
		userProfile1 = new ProfileData();
		userProfile1.setProfileId(1L);
        userProfile1.setFirstName("Jimmy");
        userProfile1.setLastName("Rehal");
        userProfile1.setCountry("India");
        
		userProfile2 = new ProfileData();
		userProfile2.setProfileId(2L);
        userProfile2.setFirstName("Sam");
        userProfile2.setLastName("Singh");
        userProfile2.setCountry("Sweden");
        
        userView = new ProfileView();
        userView.setObjectId(1L);
        userView.setUserProfileId(2L);
        userView.setViewerProfileId(1L);
        userView.setViewDate(Timestamp.valueOf((LocalDateTime.now())));
        
	}
	
	@After
	public void tearDown() {
		reset(USER_PROFILE_DATA_DAO);
		reset(USER_PROFILE_VIEW_DAO);
	}
	
	
    @Test
    public void createUserTest() throws JsonProcessingException {
        when(USER_PROFILE_DATA_DAO.create(any(ProfileData.class))).thenReturn(userProfile1);
        final Response response = resources.client().target("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(userProfile1, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(USER_PROFILE_DATA_DAO).create(userProfileCaptor.capture());
        assertThat(userProfileCaptor.getValue()).isEqualTo(userProfile1);
    }
   
    @Test
    public void getUserFoundTest() {
        when(USER_PROFILE_DATA_DAO.findById(1L)).thenReturn(Optional.of(userProfile1));
        ProfileData found = resources.getJerseyTest().target("/users/1").request().get(ProfileData.class);
        assertThat(found.getProfileId()).isEqualTo(userProfile1.getProfileId());
        verify(USER_PROFILE_DATA_DAO).findById(1L);
    }
    
    @Test
    public void getUserNotFoundTest() {
        when(USER_PROFILE_DATA_DAO.findById(3L)).thenReturn(Optional.<ProfileData>absent());
        final Response response = resources.getJerseyTest().target("/users/3").request().get();
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(USER_PROFILE_DATA_DAO).findById(3L);
    }
      
    @Test
	public void viewSuccessTest() {
	when(USER_PROFILE_DATA_DAO.findById(1L)).thenReturn(Optional.of(userProfile1));
    when(USER_PROFILE_DATA_DAO.findById(2L)).thenReturn(Optional.of(userProfile2));
	when(USER_PROFILE_VIEW_DAO.recordProfileView(userProfile2.getProfileId(),userProfile1.getProfileId())).thenReturn(userView);
	ProfileView found = resources.getJerseyTest().target("/users/1/views/2").request().get(ProfileView.class);
	assertThat(found.getUserProfileId()).isEqualTo(userProfile2.getProfileId());
	verify(USER_PROFILE_VIEW_DAO).recordProfileView(2L,1L);
    }
    
    @Test
	public void profileNotFoundTest() {
    when(USER_PROFILE_DATA_DAO.findById(2L)).thenReturn(Optional.of(userProfile2));
    when(USER_PROFILE_DATA_DAO.findById(3L)).thenReturn(Optional.<ProfileData>absent());
    final Response response = resources.getJerseyTest().target("/users/3/views/2").request().get();
    assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    verify(USER_PROFILE_DATA_DAO).findById(3L);	
    }
    
}
