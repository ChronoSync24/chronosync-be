package com.sinergy.chronosync.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

/**
 * Service for logging user out.
 */
public interface LogoutService {

	/**
	 * Logs out the user by invalidating JWT.
	 *
	 * @param request        {@link HttpServletRequest} http request containing the authorization header
	 * @param response       {@link  HttpServletResponse} http response
	 * @param authentication {@link Authentication} object containing authentication
	 */
	void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
