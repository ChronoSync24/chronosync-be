package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.User;

/**
 * User service interface class.
 */
public interface UserService {

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserRequestDTO} user create request
	 * @return {@link User} user create response
	 */
	User create(UserRequestDTO request);
}
