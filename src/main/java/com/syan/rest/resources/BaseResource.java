package com.syan.rest.resources;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.syan.rest.util.ProfileConst;

/**
 * Base Resource class for all the resources.
 * @author prsyan
 *
 */
public class BaseResource
{
    // =========================================================================
    // Data members
    // =========================================================================

    /** The logger handle.  **/
    private static Log log = LogFactory.getLog(BaseResource.class);
    
    /** For time audit. **/
    private long mSvcStartTime = 0;
    // =========================================================================
    // Protected Methods
    // =========================================================================
    
    /**
     * Sets the header for the response.
     * 
     * @param builder
     * @param type
     * @param key
     * @param msgParm - parms as CSV.
     * @return the response builder.
     */
    protected Response setResponseHeader(
            ResponseBuilder builder, String type, String key, String msgParm) 
    {
        String message = key + ":: " + ProfileConst.getMessage(key);
        
        if ((msgParm != null) && !msgParm.isEmpty())
        {
            message = message + "::[" + msgParm + "]";
        }
        return builder.entity(ProfileConst.getMessage(key)).header(type, message).build();
    }
    
    /**
     * Sets the header for the response.
     * 
     * @param builder
     * @param type
     * @param key
     * @return the response builder.
     */
    protected Response setResponseHeader(
            ResponseBuilder builder, String type, String key) 
    {
        return setResponseHeader(builder, type, key, null);
    }

    /**
     * Logs the service start and appends the message.
     * @param aMsg - an optional message.
     */
    protected void logStart(String method, String aMsg)
    {
        String msg = String.format("Start ======= %s - %s",
                                      method, aMsg);
        log.info(msg);
        mSvcStartTime = System.currentTimeMillis();
    }
    
    /**
     * Logs the service done and appends the message.
     * @param aMsg - an optional message.
     */
    protected void logDone(String method, String aMsg)
    {
        long elapse = System.currentTimeMillis() - mSvcStartTime;
        String msg = String.format("Done  ======= %s - %s (%s)\n",
             method, aMsg, elapse);
        log.info(msg);
        
    }
    
}