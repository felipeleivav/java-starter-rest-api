package com.api.rest.rest;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.api.rest.bean.UserBean;
import com.api.rest.arq.exception.ConnectionException;
import com.api.rest.arq.exception.EncryptionException;
import com.api.rest.dao.UserDAO;
import com.api.rest.utils.AuthTools;
import com.api.rest.utils.CaptchaUtils;
import com.api.rest.utils.Constants;
import com.api.rest.utils.DataValidator;
import com.api.rest.utils.PropLoader;

@Path("/user")
public class UserRest {

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public String registerUser(UserBean user) throws ConnectionException {
		String returnMessage = "";

		boolean captcha = PropLoader.getB(Constants.CAPTCHA_REQUIRED_FOR_REGISTER);
		String captchaKey = PropLoader.get(Constants.CAPTCHA_KEY_FOR_REGISTER);

		if (PropLoader.getB(Constants.REGISTER_ENABLED)) {
			if (user != null) {
				if (user.getUsername() != null && !user.getUsername().isEmpty()) {
					if (user.getPassword() != null && !user.getPassword().isEmpty()) {
						if (user.getEmail() != null && !user.getEmail().isEmpty() && DataValidator.validateEmail(user.getEmail())) {
							UserDAO userDao = new UserDAO();
							if (userDao.userExists(user.getUsername()) == 0) {
								if (!userDao.registeredEmail(user.getEmail())) {
									if (!captcha || (captcha && CaptchaUtils.validateCaptcha(user, captchaKey))) {
										if (userDao.createUser(user)) {
											returnMessage = "OK";
										} else {
											returnMessage = "ERROR";
										}
									} else {
										returnMessage = "CAPTCHA_INVALID";
									}
								} else {
									returnMessage = "EMAIL_REGISTERED";
								}
							} else {
								returnMessage = "USERNAME_REGISTERED";
							}
							userDao.close();
						} else {
							returnMessage = "INVALID_EMAIL";
						}
					} else {
						returnMessage = "INVALID_PASSWORD";
					}
				} else {
					returnMessage = "INVALID_USERNAME";
				}
			} else {
				returnMessage = "INVALID_REQUEST";
			}
		} else {
			returnMessage = "REGISTER_DISABLED";
		}

		return returnMessage;
	}

	@POST
	@Path("/login")
	public String login(UserBean user) throws ConnectionException, EncryptionException {
		if (user != null && user.getUsername() != null && !user.getUsername().isEmpty() && user.getPassword() != null && !user.getPassword().isEmpty()) {
			UserDAO userDao = new UserDAO();
			boolean authStatus = userDao.validateUser(user.getUsername(), user.getPassword());
			userDao.close();
			if (authStatus) {
				String token = AuthTools.generateToken(user.getUsername(), user.getPassword(), new Date());
				return token;
			} else {
				return "WRONG_USERNAME_OR_PASSWORD";
			}
		} else {
			return "INVALID_REQUEST";
		}
	}

	@GET
	public int getUserId(@Context HttpServletRequest request) {
		int userId = Integer.parseInt(request.getAttribute("userid").toString());
		return userId;
	}

}
