package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AuthenticationRequestDTO;
import com.sinergy.chronosync.dto.request.UserRegisterRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.dto.response.UserRegisterResponseDTO;

/**
 * Authentication service interface.
 */
public interface AuthenticationService {

	/**
	 * Registers new user.
	 *
	 * @param request {@link UserRegisterRequestDTO} user register request
	 * @return {@link UserRegisterResponseDTO} user register response
	 */
	UserRegisterResponseDTO register(UserRegisterRequestDTO request);

	/**
	 * Authenticates provided user with username and password.
	 *
	 * @param request {@link AuthenticationRequestDTO} authentication request
	 * @return {@link AuthenticationResponse} JSON web token
	 */
	AuthenticationResponse authenticate(AuthenticationRequestDTO request);
}
