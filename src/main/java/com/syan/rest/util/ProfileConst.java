package com.syan.rest.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileConst {

	// =========================================================================
	// Resource constants
	// =========================================================================
	/** Max Views allowed for a User. **/
	public static final int DEFAULT_MAX_VIEWS_LIMIT = 10;
	
	/** Max Days to look for views. **/
	public static final int DEFUALT_MAX_DAYS_LIMIT = 10;

	// =========================================================================
	// Message header constants
	// =========================================================================
	/** Response error message header name. **/
	public static final String ERROR_HDR = "REST_Error";

	/** Response error message header name. **/
	public static final String WARN_HDR = "REST_Warn";

	/** Response error message header name. **/
	public static final String INFO_HDR = "REST_Info";

	// =========================================================================
	// Error message constants
	// =========================================================================

	/** Unexpected internal error happened. **/
	public static final String MSG_INTERNAL_ERROR = "3000";

	/** The supplied arguments are bad. **/
	public static final String MSG_BAD_ARG_NO_FIRST_NAME = "3001";

	/** The supplied arguments are bad. **/
	public static final String MSG_BAD_ARG_NO_ID = "3002";

	/** The specified resource is not found. **/
	public static final String MSG_RES_NOT_FOUND = "3003";

	/** Both Id's are same **/
	public static final String MSG_BAD_ARG_SAME_ID = "3004";

	// =========================================================================
	// Public Methods
	// =========================================================================

	/**
	 * Gets the message string for the given message code.
	 * 
	 * @param aMsgKey
	 *            a message code.
	 * @return the string for the message.
	 */
	public static String getMessage(String aMsgKey) {
		String result = aMsgKey + " - Msg Undefined!";
		try {
			if (mMsgBundle != null) {
				result = mMsgBundle.getString(aMsgKey);
			}
		} catch (Exception e) {
			// Ignore probably message is not in the bundle.
			log.warn("Failed to get message string for " + aMsgKey, e);
		}
		return result;
	}

	// =========================================================================
	// Private Methods
	// =========================================================================

	/** The logger handle. **/
	private static final Logger log = LoggerFactory
			.getLogger(ProfileConst.class);

	/** The message resource property file that has the message strings. **/
	private static final String MSG_RESOURCE = "com.syan.rest.util.ResourceMessage";

	/** A reference to the message resource bundle. **/
	private static ResourceBundle mMsgBundle;

	/** Static initialization code for loading the message resource. **/
	static {
		try {
			// Load the properties as a resource.
			mMsgBundle = ResourceBundle.getBundle(MSG_RESOURCE);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to load resource bundle.");
		}
	}

}
