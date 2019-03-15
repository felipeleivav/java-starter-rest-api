package com.api.rest.filter;

import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.api.rest.arq.exception.ConnectionException;
import com.api.rest.arq.exception.EncryptionException;
import com.api.rest.utils.AuthTools;
import com.api.rest.utils.Constants;
import com.api.rest.utils.PropLoader;
import org.apache.log4j.Logger;

import com.api.rest.dao.UserDAO;

@Provider
@PreMatching
public class Authenticator implements ContainerRequestFilter {
	private static Logger logger = Logger.getLogger(Authenticator.class);

	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {
		String method = containerRequest.getMethod();
		String path = containerRequest.getUriInfo().getPath();

		/*
			PUBLIC APIS
			==========================
			Here you can specify your public-access APIs, in this case:
			  /user -> POST           : when a user signs up
			  /user/login -> PUT      : when a user is logging in
			  /meta -> any operation  : acknowledging API
			Any API you require to be public, code it here, the rest will be private and require authentication
		 */

		if (method.equals("OPTIONS")
			|| (path.equalsIgnoreCase("user") && (method.equals("PUT")))
			|| (path.equalsIgnoreCase("user/login") && method.equals("POST"))
			|| (path.equalsIgnoreCase("meta"))
		) {
			return;
		}
		
		String headerToken = containerRequest.getHeaderString(AUTHENTICATION_HEADER);
		
		if (null==headerToken) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
		
		String[] userData;
		
		try {
			userData = AuthTools.readToken(headerToken.substring(7));
		} catch (EncryptionException e) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			logger.error(headerToken);
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		
		try {
			UserDAO userDao = new UserDAO();
			boolean authStatus = userDao.validateUser(userData[0], userData[1]);
			
			Long timestampDiff = new Date().getTime()-Long.valueOf(userData[2]);
			Long maxDuration = PropLoader.getL(Constants.AUTH_SESSION_DURATION);
			
			if (authStatus && timestampDiff<maxDuration) {
				int userId = userDao.getUserId(userData[0]);
				containerRequest.setProperty("userid", userId);
				userDao.updateLastActivityDate(userId);
				userDao.close();
				logger.debug("User success: "+userData[0]);
			} else {
				userDao.close();
				logger.debug("User failed: "+userData[0]);
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		} catch (ConnectionException e) {
			logger.error("Authentication error ", e);
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
	}
	
}
