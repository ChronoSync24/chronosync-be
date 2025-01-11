package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.UserCreateRequestDTO;
import com.sinergy.chronosync.dto.response.UserCreateResponseDTO;

/**
 * User service interface class.
 */
public interface UserService {

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserCreateRequestDTO} user create request
	 * @return {@link UserCreateResponseDTO} user create response
	 */
	UserCreateResponseDTO create(UserCreateRequestDTO request);
}
