package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.UserCreateRequestDTO;
import com.sinergy.chronosync.dto.response.UserCreateResponseDTO;
import org.hibernate.service.spi.ServiceException;

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

	/**
	 * Enables user account with provided id.
	 *
	 * @param id {@link Long} user id
	 */
	void enable(Long id) throws ServiceException;
}
